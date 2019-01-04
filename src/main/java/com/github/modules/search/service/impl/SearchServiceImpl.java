package com.github.modules.search.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.common.utils.PageUtils;
import com.github.modules.data.pojo.HouseIndexTemplate;
import com.github.modules.data.service.HouseService;
import com.github.modules.search.constant.HouseIndexConstant;
import com.github.modules.search.dto.HouseDTO;
import com.github.modules.search.dto.HouseListDTO;
import com.github.modules.search.entity.CollectEntity;
import com.github.modules.search.form.HouseForm;
import com.github.modules.search.form.HouseSort;
import com.github.modules.search.service.CollectService;
import com.github.modules.search.service.SearchService;

import com.github.modules.search.utils.ConditionRangeUtils;
import org.apache.catalina.startup.HomesUserDatabase;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.queryparser.xml.builders.BooleanQueryBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequestBuilder;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;

import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.rescore.QueryRescorerBuilder;
import org.elasticsearch.search.rescore.RescoreBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.*;

@Service("searchService")
public class SearchServiceImpl implements SearchService {

    private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    @Autowired
    private TransportClient esClient;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CollectService collectService;

    @Autowired
    private HouseService houseService;

    @Override
    public Set<String> suggest(String prefix) {
        CompletionSuggestionBuilder suggestionBuilder =
                SuggestBuilders.completionSuggestion(HouseIndexConstant.SUGGEST).prefix(prefix);

        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion("autocomplete", suggestionBuilder);

        // search语法，获取前10条
        SearchRequestBuilder requestBuilder = esClient.prepareSearch(HouseIndexConstant.INDEX_NAME)
                .setTypes(HouseIndexConstant.TYPE_NAME)
                .suggest(suggestBuilder)
                .setSize(10);
        SearchResponse searchResponse = requestBuilder.get();

        logger.debug(requestBuilder.toString());

        Suggest suggest = searchResponse.getSuggest();
        if (suggest == null) {
            return new HashSet<>();
        }

        // 根据自定义的suggest名获取result
        Suggest.Suggestion result = suggest.getSuggestion("autocomplete");

        Set<String> suggestSet = new HashSet<>();

        for (Object term : result.getEntries()) {
            if (term instanceof CompletionSuggestion.Entry) {
                CompletionSuggestion.Entry item = (CompletionSuggestion.Entry) term;

                if (item.getOptions().isEmpty()) {
                    continue;
                }

                // 获取 options 中的 text
                for (CompletionSuggestion.Entry.Option option : item.getOptions()) {
                    String tip = option.getText().string();
                    suggestSet.add(tip);
                }
            }
        }

        return Collections.unmodifiableSet(suggestSet);
    }

    @Override
    public PageUtils query(HouseForm houseForm) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        // 筛选条件
        addOption(boolQueryBuilder, houseForm);

        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(HouseIndexConstant.INDEX_NAME)
                .setTypes(HouseIndexConstant.TYPE_NAME);

        if (StringUtils.isNotBlank(houseForm.getKeyword())) {
            // 以下field满足其中之一即可。
            // 使用best_fields策略, tie_breaker参数优化dis_max搜索。
            // minimum_should_match去长尾，只有匹配一定数量的关键词的数据才返回。
            boolQueryBuilder.must(
                    QueryBuilders.multiMatchQuery(houseForm.getKeyword())
                            // 搜索的内容在某一个不分词的字段，权重给大一点，区分开来
                            .field(HouseIndexConstant.REGION, 10f)
                            .field(HouseIndexConstant.HOUSE_TYPE, 10f)
                            .field(HouseIndexConstant.COMMUNITY, 10f)
                            .field(HouseIndexConstant.TITLE, 2f)
                            .field(HouseIndexConstant.ADDRESS)
                            .field(HouseIndexConstant.DESCRIPTION)
                            .type(MultiMatchQueryBuilder.Type.BEST_FIELDS)
                            .tieBreaker(0.4f)
                            .minimumShouldMatch("50%"));

            QueryRescorerBuilder queryRescorerBuilder = RescoreBuilder.queryRescorer(boolQueryBuilder.should(
                    // 使用近似匹配实现召回率与精准度的平衡，should匹配到将会贡献分数
                    QueryBuilders.boolQuery()
                            .should(QueryBuilders.matchPhraseQuery(
                                    HouseIndexConstant.COMMUNITY, houseForm.getKeyword()).slop(3).boost(2f))
                            .should(QueryBuilders.matchPhraseQuery(
                                    HouseIndexConstant.TITLE, houseForm.getKeyword()).slop(8))
                            .should(QueryBuilders.matchPhraseQuery(
                                    HouseIndexConstant.ADDRESS, houseForm.getKeyword()).slop(5))
                            .should(QueryBuilders.matchPhraseQuery(
                                    HouseIndexConstant.DESCRIPTION, houseForm.getKeyword()).slop(10))));

            // 前50条使用重打分机制优化近似匹配搜索的性能
            searchRequestBuilder.setRescorer(queryRescorerBuilder, 50);
        }

        searchRequestBuilder.setFrom(houseForm.getCurr() - 1)
                            .setSize(houseForm.getLimit())
                            .setQuery(boolQueryBuilder)
                            .addSort(HouseSort.getSortKey(houseForm.getOrderBy()),
                                        SortOrder.fromString(houseForm.getOrderDirection()
                                    ));

        logger.debug(searchRequestBuilder.toString());

        SearchResponse searchResponse = searchRequestBuilder.get();
        SearchHit[]    hits           = searchResponse.getHits().getHits();

        List<HouseListDTO> dataList = new ArrayList<>();
        for (SearchHit hit : hits) {
            HouseListDTO houseListDTO = modelMapper.map(hit.getSource(), HouseListDTO.class);
            dataList.add(houseListDTO);
        }

        return new PageUtils(searchResponse.getHits().getTotalHits(), dataList);
    }

    @Override
    public HouseDTO queryById(String id) {

        ConstantScoreQueryBuilder constantScoreQueryBuilder
                = QueryBuilders.constantScoreQuery(QueryBuilders.termQuery(HouseIndexConstant.SOURCE_URL_ID, id));

        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(HouseIndexConstant.INDEX_NAME)
                .setTypes(HouseIndexConstant.TYPE_NAME)
                .setQuery(constantScoreQueryBuilder);

        logger.debug(searchRequestBuilder.toString());

        SearchResponse searchResponse = searchRequestBuilder.get();
        SearchHit[]    hits           = searchResponse.getHits().getHits();

        if (hits.length <= 0) {
            logger.warn(String.format("id=%s 没有查询到数据", id));
        } else {
            return modelMapper.map(hits[0].getSource(), HouseDTO.class);
        }

        return new HouseDTO();
    }

    @Override
    public List<HouseListDTO> queryById(Long userId) {
        List<CollectEntity> collectEntityList = collectService.findByUserId(userId);

        Set<String> sourceUrlIds = new HashSet<>();
        for (CollectEntity collectEntity : collectEntityList) {
            sourceUrlIds.add(collectEntity.getHouseId());
        }

        String[] ids = sourceUrlIds.toArray(new String[]{});
        List<HouseIndexTemplate> houseIndexTemplateList = houseService.multiGet(ids);

        List<HouseListDTO> houseListDTOList = new ArrayList<>();
        for (HouseIndexTemplate houseIndexTemplate : houseIndexTemplateList) {
            HouseListDTO houseListDTO = modelMapper.map(houseIndexTemplate, HouseListDTO.class);
            houseListDTOList.add(houseListDTO);
        }

        return houseListDTOList;
    }

    private void addOption(BoolQueryBuilder boolQueryBuilder, HouseForm houseForm) {
        // 城市过滤
        boolQueryBuilder.filter(
                QueryBuilders.termQuery(HouseIndexConstant.CITY, houseForm.getCity())
        );

        // 区域过滤
        String region = houseForm.getRegion();
        if (StringUtils.isNotBlank(region) && !"*".equals(region)) {
            boolQueryBuilder.filter(
                    QueryBuilders.termQuery(HouseIndexConstant.REGION, region)
            );
        }

        // 出租方式过滤
        String rentWay = houseForm.getRentWay();
        if (StringUtils.isNotBlank(rentWay) && !"*".equals(rentWay)) {
            boolQueryBuilder.filter(
                    QueryBuilders.termQuery(HouseIndexConstant.RENT_WAY, rentWay)
            );
        }

        // 户型（房间数）过滤
        String roomNum = houseForm.getRoomNum();
        if (StringUtils.isNotBlank(roomNum) && !"*".equals(roomNum)) {
            String[] splitResult = roomNum.split("[-]");

            // 房间数+
            if (splitResult.length == 1) {
                boolQueryBuilder.filter(
                        QueryBuilders.termQuery(HouseIndexConstant.ROOM_NUM, roomNum)
                );
            } else if (splitResult.length == 2 ) {
                RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(HouseIndexConstant.ROOM_NUM);
                Integer roomNumPlus = Integer.valueOf(splitResult[0]);
                rangeQueryBuilder.gte(roomNumPlus);
                boolQueryBuilder.filter(rangeQueryBuilder);
            }
        }

        // 来源网站过滤
        String sourceWebsite = houseForm.getSourceWebsite();
        if (StringUtils.isNotBlank(sourceWebsite) && !"*".equals(sourceWebsite)) {
            boolQueryBuilder.filter(
                    QueryBuilders.termQuery(HouseIndexConstant.WEBSITE, sourceWebsite)
            );
        }

        // 面积区间
        ConditionRangeUtils squareCondition = ConditionRangeUtils.matchArea(houseForm.getSquareBlock());
        if (!ConditionRangeUtils.ALL.equals(squareCondition)) {
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(HouseIndexConstant.SQUARE);
            if (squareCondition.getMin() > 0) {
                rangeQueryBuilder.gte(squareCondition.getMin());
            }

            if (squareCondition.getMax() > 0) {
                rangeQueryBuilder.lte(squareCondition.getMax());
            }
            boolQueryBuilder.filter(rangeQueryBuilder);
        }

        // 价格区间
        ConditionRangeUtils priceCondition = ConditionRangeUtils.matchPrice(houseForm.getPriceBlock());
        if (!ConditionRangeUtils.ALL.equals(priceCondition)) {
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(HouseIndexConstant.PRICE);
            if (priceCondition.getMin() > 0) {
                rangeQueryBuilder.gte(priceCondition.getMin());
            }

            if (priceCondition.getMax() > 0) {
                rangeQueryBuilder.lte(priceCondition.getMax());
            }
            boolQueryBuilder.filter(rangeQueryBuilder);
        }
    }
}
