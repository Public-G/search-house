package com.github.modules.search.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.modules.search.constant.BucketConstant;
import com.github.modules.search.constant.HouseIndexConstant;
import com.github.modules.search.dto.HouseBucketDTO;
import com.github.modules.search.dto.IndexChartDTO;
import com.github.modules.search.dto.IndexDTO;
import com.github.modules.search.service.IndexService;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.*;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class IndexServiceImpl implements IndexService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TransportClient esClient;

    /**
     * 未通过Chart展示的数据
     *
     * @param city
     * @return
     */
    @Override
    public IndexDTO fetchRealtimeData(String city) {
        // 聚合城市并聚合其平均租金
        AvgAggregationBuilder   avgAggregationBuilder  = buildAvgPriceAggregation();
        TermsAggregationBuilder aggregationCityBuilder = buildtermsAggregationt(BucketConstant.AGG_CITY, HouseIndexConstant.CITY)
                .order(Terms.Order.aggregation(BucketConstant.AVG_PRICE, false)); // 平均租金降序;
        aggregationCityBuilder.subAggregation(avgAggregationBuilder);

        LocalDate      minDate        = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()); // 当月1号
        LocalDate      maxDate        = minDate.plusMonths(1L); // 隔月1号
        ExtendedBounds extendedBounds = new ExtendedBounds(minDate.toString(), maxDate.toString());

        // date hitogram之统计当月每周获取的房源总数
        DateHistogramAggregationBuilder dateHistogramAggregationBuilder =
                buildDateHistogramAgg(BucketConstant.AGG_WEEK, HouseIndexConstant.CREATE_TIME, DateHistogramInterval.WEEK, extendedBounds);

        // 聚合房源来源网站
        TermsAggregationBuilder aggregationWebsiteBuilder = buildtermsAggregationt(BucketConstant.AGG_WEBSITE, HouseIndexConstant.WEBSITE);


        SearchRequestBuilder searchRequestBuilder = buildSearchRequestBuilder()
                .addAggregation(aggregationCityBuilder)
                .addAggregation(aggregationWebsiteBuilder)
                .addAggregation(dateHistogramAggregationBuilder);

        logger.debug(searchRequestBuilder.toString());

        SearchResponse response = searchRequestBuilder.get();

        IndexDTO indexDTO = new IndexDTO();
        if (response.status() != RestStatus.OK) {
            logger.warn("Aggregate status is not ok for " + searchRequestBuilder.toString());
            return indexDTO;
        }

        // 房源总数
        Long houseCount = response.getHits().totalHits;
        indexDTO.setHouseCount(houseCount);

        // 获取当前城市平均租金和其排名
        List<? extends Terms.Bucket> cityBuckets = fetchBuckets(response, BucketConstant.AGG_CITY);
        int                          bucketSize  = cityBuckets.size();
        for (int index = 0; index < bucketSize; index++) {
            Terms.Bucket bucket = cityBuckets.get(index);
            if (city.equals(bucket.getKeyAsString())) {
                InternalAvg internalAvg = bucket.getAggregations().get(BucketConstant.AVG_PRICE);
                long        avgPrice    = Math.round(internalAvg.getValue());
                indexDTO.setAvgPrice(avgPrice);
                indexDTO.setTop(index + 1);

                indexDTO.setCityHouseCount(bucket.getDocCount());
            }
        }

        // 房源来源网站数据
        List<? extends Terms.Bucket> websiteBuckets = fetchBuckets(response, BucketConstant.AGG_WEBSITE);
        indexDTO.setWebsiteCount(websiteBuckets.size());

        // 当前周
        List<InternalDateHistogram.Bucket> weekBuckets = fetchDateHistogramBuckets(response, BucketConstant.AGG_WEEK);
        for (InternalDateHistogram.Bucket weekBucket : weekBuckets) {
            LocalDate bucketDate = LocalDate.parse(weekBucket.getKeyAsString());
            int       days       = Period.between(bucketDate, LocalDate.now()).getDays(); // 当前时间与bucketDate距离天数(大的在后)
            if (days >= 0 && days <= 7) {
                indexDTO.setHouseWeekCount(weekBucket.getDocCount());
            }
        }

        return indexDTO;
    }

    /**
     * 租金走势
     *
     * @param city
     * @return
     */
    @Override
    public IndexChartDTO fetchRentTrend(String city) {
        // 按照城市过滤
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .filter(QueryBuilders.termQuery(HouseIndexConstant.CITY, city));

        // 平均租金
        AvgAggregationBuilder avgAggregationBuilder = buildAvgPriceAggregation();

        LocalDate      maxDate        = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()); // 当月1号
        LocalDate      minDate        = maxDate.minusMonths(3L); // 前三个月
        ExtendedBounds extendedBounds = new ExtendedBounds(minDate.toString(), maxDate.toString());

        // date hitogram之统计4个月内租金情况
        DateHistogramAggregationBuilder dateHistogramAggregationBuilder =
                buildDateHistogramAgg(BucketConstant.AGG_MONTH, HouseIndexConstant.RELEASE_TIME,
                        DateHistogramInterval.MONTH, extendedBounds);
        dateHistogramAggregationBuilder.subAggregation(avgAggregationBuilder);


        SearchRequestBuilder searchRequestBuilder = buildSearchRequestBuilder()
                .setQuery(boolQueryBuilder)
                .addAggregation(dateHistogramAggregationBuilder);

        logger.debug(searchRequestBuilder.toString());

        SearchResponse response      = searchRequestBuilder.get();
        IndexChartDTO  indexChartDTO = new IndexChartDTO();
        if (response.status() != RestStatus.OK) {
            logger.warn("Aggregate status is not ok for " + searchRequestBuilder.toString());
            return indexChartDTO;
        }

        List<InternalDateHistogram.Bucket> monthBuckets = fetchDateHistogramBuckets(response, BucketConstant.AGG_MONTH);
        int                                bucketSize   = monthBuckets.size();
        indexChartDTO = initChartDTO(bucketSize, indexChartDTO);

        for (int index = 0; index < bucketSize; index++) {
            InternalDateHistogram.Bucket bucket = monthBuckets.get(index);

            indexChartDTO.getxAxis()[index] = bucket.getKeyAsString().substring(0, 7);

            InternalAvg internalAvg = bucket.getAggregations().get(BucketConstant.AVG_PRICE);
            indexChartDTO.getData()[index] = Math.round(internalAvg.getValue());
        }

        return indexChartDTO;
    }

    /**
     * 租金Top10
     *
     * @return
     */
    @Override
    public IndexChartDTO fetchRentTop() {
        AvgAggregationBuilder avgAggregationBuilder = buildAvgPriceAggregation();
        TermsAggregationBuilder aggregationCityBuilder = buildtermsAggregationt(BucketConstant.AGG_CITY, HouseIndexConstant.CITY)
                .order(Terms.Order.aggregation(BucketConstant.AVG_PRICE, false)); // 平均租金降序
        aggregationCityBuilder.subAggregation(avgAggregationBuilder);

        SearchRequestBuilder searchRequestBuilder = buildSearchRequestBuilder()
                .addAggregation(aggregationCityBuilder);

        logger.debug(searchRequestBuilder.toString());

        SearchResponse response      = searchRequestBuilder.get();
        IndexChartDTO  indexChartDTO = new IndexChartDTO();
        if (response.status() != RestStatus.OK) {
            logger.warn("Aggregate status is not ok for " + searchRequestBuilder.toString());
            return indexChartDTO;
        }

        List<? extends Terms.Bucket> buckets    = fetchBuckets(response, BucketConstant.AGG_CITY);
        int                          bucketSize = buckets.size();
        indexChartDTO = initChartDTO(bucketSize, indexChartDTO);

        for (int index = 0; index < bucketSize; index++) {
            if (index == 10) { // 收集前10个
                break;
            }

            Terms.Bucket bucket      = buckets.get(index);
            InternalAvg  internalAvg = bucket.getAggregations().get(BucketConstant.AVG_PRICE);
            indexChartDTO.getData()[index] = Math.round(internalAvg.getValue());

            indexChartDTO.getxAxis()[index] = bucket.getKeyAsString();
        }

        return indexChartDTO;
    }

    @Override
    public IndexChartDTO fetchRentRegion(String city) {
        // 按照城市过滤
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .filter(QueryBuilders.termQuery(HouseIndexConstant.CITY, city));

        // 各区域平均租金
        AvgAggregationBuilder   avgAggregationBuilder  = buildAvgPriceAggregation();
        TermsAggregationBuilder aggregationCityBuilder = buildtermsAggregationt(BucketConstant.AGG_REGION, HouseIndexConstant.REGION);
        aggregationCityBuilder.subAggregation(avgAggregationBuilder);

        SearchRequestBuilder searchRequestBuilder = buildSearchRequestBuilder()
                .setQuery(boolQueryBuilder)
                .addAggregation(aggregationCityBuilder);

        logger.debug(searchRequestBuilder.toString());

        SearchResponse response      = searchRequestBuilder.get();
        IndexChartDTO  indexChartDTO = new IndexChartDTO();
        if (response.status() != RestStatus.OK) {
            logger.warn("Aggregate status is not ok for " + searchRequestBuilder.toString());
            return indexChartDTO;
        }

        List<? extends Terms.Bucket> buckets    = fetchBuckets(response, BucketConstant.AGG_REGION);
        int                          bucketSize = buckets.size();
        indexChartDTO = initChartDTO(bucketSize, indexChartDTO);

        for (int index = 0; index < bucketSize; index++) {
            Terms.Bucket bucket      = buckets.get(index);
            InternalAvg  internalAvg = bucket.getAggregations().get(BucketConstant.AVG_PRICE);
            indexChartDTO.getData()[index] = Math.round(internalAvg.getValue());

            indexChartDTO.getxAxis()[index] = bucket.getKeyAsString();
        }

        return indexChartDTO;
    }

    @Override
    public IndexChartDTO fetchRentTypePie(String city) {
        // 按照城市过滤
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .filter(QueryBuilders.termQuery(HouseIndexConstant.CITY, city));

        TermsAggregationBuilder aggregationCityBuilder =
                buildtermsAggregationt(BucketConstant.AGG_ROOMNUM, HouseIndexConstant.ROOM_NUM);

        SearchRequestBuilder searchRequestBuilder = buildSearchRequestBuilder()
                .setQuery(boolQueryBuilder)
                .addAggregation(aggregationCityBuilder);

        logger.debug(searchRequestBuilder.toString());

        SearchResponse response      = searchRequestBuilder.get();
        IndexChartDTO  indexChartDTO = new IndexChartDTO();
        if (response.status() != RestStatus.OK) {
            logger.warn("Aggregate status is not ok for " + searchRequestBuilder.toString());
            return indexChartDTO;
        }

        List<? extends Terms.Bucket> buckets    = fetchBuckets(response, BucketConstant.AGG_ROOMNUM);
        int                          bucketSize = buckets.size();
        indexChartDTO = initChartDTO(bucketSize, indexChartDTO);

        for (int index = 0; index < bucketSize; index++) {
            Terms.Bucket bucket = buckets.get(index);

            indexChartDTO.getxAxis()[index] = bucket.getKeyAsString() + "室";

            indexChartDTO.getData()[index] = bucket.getDocCount();
        }

        return indexChartDTO;
    }

    @Override
    public IndexChartDTO fetchRentRentPie(String city) {
        // 按照城市过滤
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .filter(QueryBuilders.termQuery(HouseIndexConstant.CITY, city));

        // 区间2000
        HistogramAggregationBuilder histogramAggregationBuilder = AggregationBuilders.histogram(BucketConstant.HISTOGRAM_PRICE)
                .field(HouseIndexConstant.PRICE).interval(2000);

        SearchRequestBuilder searchRequestBuilder = buildSearchRequestBuilder()
                .setQuery(boolQueryBuilder)
                .addAggregation(histogramAggregationBuilder);

        logger.debug(searchRequestBuilder.toString());

        SearchResponse response      = searchRequestBuilder.get();
        IndexChartDTO  indexChartDTO = new IndexChartDTO();
        if (response.status() != RestStatus.OK) {
            logger.warn("Aggregate status is not ok for " + searchRequestBuilder.toString());
            return indexChartDTO;
        }

        InternalHistogram              internalHistogram = response.getAggregations().get(BucketConstant.HISTOGRAM_PRICE);
        List<InternalHistogram.Bucket> buckets           = internalHistogram.getBuckets();
        int                            bucketSize        = buckets.size();
        indexChartDTO = initChartDTO(bucketSize, indexChartDTO);

        for (int index = 0; index < bucketSize; index++) {
            InternalHistogram.Bucket bucket = buckets.get(index);

            long docCount = bucket.getDocCount();
            if (docCount == 0) { // 跳过数量为0的
                continue;
            }

            Long key = Math.round((Double) bucket.getKey());
            indexChartDTO.getxAxis()[index] = key + "-" + (key + 2000L);

            indexChartDTO.getData()[index] = docCount;
        }

        return indexChartDTO;
    }

    @Override
    public IndexChartDTO fetchRegionPie(String city) {
        // 按照城市过滤
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .filter(QueryBuilders.termQuery(HouseIndexConstant.CITY, city));

        TermsAggregationBuilder termsAggregationBuilder =
                buildtermsAggregationt(BucketConstant.AGG_REGION, HouseIndexConstant.REGION);

        SearchRequestBuilder searchRequestBuilder = buildSearchRequestBuilder()
                .setQuery(boolQueryBuilder)
                .addAggregation(termsAggregationBuilder);

        logger.debug(searchRequestBuilder.toString());

        SearchResponse response      = searchRequestBuilder.get();
        IndexChartDTO  indexChartDTO = new IndexChartDTO();
        if (response.status() != RestStatus.OK) {
            logger.warn("Aggregate status is not ok for " + searchRequestBuilder.toString());
            return indexChartDTO;
        }

        List<? extends Terms.Bucket> buckets    = fetchBuckets(response, BucketConstant.AGG_REGION);
        int                          bucketSize = buckets.size();
        indexChartDTO = initChartDTO(bucketSize, indexChartDTO);

        for (int index = 0; index < bucketSize; index++) {
            Terms.Bucket bucket = buckets.get(index);

            indexChartDTO.getxAxis()[index] = bucket.getKeyAsString();

            indexChartDTO.getData()[index] = bucket.getDocCount();
        }

        return indexChartDTO;

    }

    /**
     * 构建TermsAggregationBuilder
     *
     * @param bucketName
     * @param field
     * @return
     */
    private TermsAggregationBuilder buildtermsAggregationt(String bucketName, String field) {
        return AggregationBuilders.terms(bucketName)
                .field(field);

    }

    /**
     * 构建DateHistogramAggregationBuilder
     *
     * @param field
     * @param dateHistogramInterval
     * @param extendedBounds
     * @return
     */
    private DateHistogramAggregationBuilder buildDateHistogramAgg(String bucketName, String field,
                                                                  DateHistogramInterval dateHistogramInterval,
                                                                  ExtendedBounds extendedBounds) {
        return AggregationBuilders.dateHistogram(bucketName)
                .field(field)
                .dateHistogramInterval(dateHistogramInterval)
                .minDocCount(0L) // 一条数据都没有，那么这个区间也是要返回的，不然默认是会过滤掉这个区间
                .format("yyyy-MM-dd")
                .extendedBounds(extendedBounds);
    }

    /**
     * 聚合平均价格
     *
     * @return
     */
    private AvgAggregationBuilder buildAvgPriceAggregation() {
        return AggregationBuilders.avg(BucketConstant.AVG_PRICE)
                .field(HouseIndexConstant.PRICE);
    }

    /**
     * 构建SearchRequestBuilder
     *
     * @return
     */
    private SearchRequestBuilder buildSearchRequestBuilder() {
        return esClient.prepareSearch(HouseIndexConstant.INDEX_NAME)
                .setTypes(HouseIndexConstant.TYPE_NAME)
                .setSize(0);
    }

    /**
     * 初始化IndexChartDTO
     *
     * @param bucketSize
     * @param indexChartDTO
     * @return
     */
    private IndexChartDTO initChartDTO(int bucketSize, IndexChartDTO indexChartDTO) {
        String[] xAxis = new String[bucketSize];
        Long[]   data  = new Long[bucketSize];

        indexChartDTO.setxAxis(xAxis);
        indexChartDTO.setData(data);
        return indexChartDTO;
    }

    /**
     * 获取Terms Buckets
     *
     * @param response
     * @param bucketName
     * @return
     */
    private List<? extends Terms.Bucket> fetchBuckets(SearchResponse response, String bucketName) {
        Aggregations aggregations = response.getAggregations();

        Terms terms = aggregations.get(bucketName);
        return terms.getBuckets();
    }

    /**
     * 获取DateHistogram Buckets
     *
     * @param response
     * @param bucketName
     * @return
     */
    private List<InternalDateHistogram.Bucket> fetchDateHistogramBuckets(SearchResponse response, String bucketName) {
        InternalDateHistogram internalDateHistogram = response.getAggregations().get(bucketName);
        return internalDateHistogram.getBuckets();
    }


}
