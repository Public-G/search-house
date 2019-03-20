package com.github.modules.data.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.common.exception.SHException;
import com.github.common.utils.PageUtils;
import com.github.modules.base.form.PageForm;
import com.github.modules.data.pojo.BaiduMapLocation;
import com.github.modules.data.pojo.HouseIndexTemplate;
import com.github.modules.data.pojo.HouseSuggest;
import com.github.modules.data.service.BaiduMapService;
import com.github.modules.search.constant.HouseIndexConstant;
import com.github.modules.search.dto.HouseDTO;
import com.github.modules.data.service.HouseService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeAction;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequestBuilder;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.*;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.github.common.constant.SysConstant.RequestParam.CURR;
import static com.github.common.constant.SysConstant.RequestParam.LIMIT;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@Service("houseService")
public class HouseServiceImpl implements HouseService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TransportClient esClient;

    @Autowired
    private BulkProcessor bulkProcessor;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BaiduMapService baiduMapService;

    private volatile JsonNode jsonNode;

    @Override
    public void saveOrUpdate(HouseIndexTemplate houseIndexTemplate) {
        // 格式化区域信息
        formatRegion(houseIndexTemplate);

        // 获取搜索建议词条
        fetchSuggest(houseIndexTemplate);

        // 获取百度经纬度
        BaiduMapLocation baiduMapLocation =
                baiduMapService.getBaiduMapLocation(houseIndexTemplate.getCity(), houseIndexTemplate.getRegion(),
                        houseIndexTemplate.getAddress(), houseIndexTemplate.getCommunity());
        if (baiduMapLocation == null) {
            throw new SHException("无法获取百度经纬度");
        }
        houseIndexTemplate.setLocation(baiduMapLocation);

        addBulkProcessor(houseIndexTemplate);

        // 上传LBS(单独上传，批量上传受限)
        baiduMapService.uploadLBS(baiduMapLocation, houseIndexTemplate.getTitle(), houseIndexTemplate.getAddress(),
                houseIndexTemplate.getSourceUrlId(), houseIndexTemplate.getPrice(), houseIndexTemplate.getSquare());
    }

    @Override
    public void update(HouseIndexTemplate houseIndexTemplate) {
        addBulkProcessor(houseIndexTemplate);

        // 上传LBS(更新)
        baiduMapService.uploadLBS(houseIndexTemplate.getLocation(), houseIndexTemplate.getTitle(), houseIndexTemplate.getAddress(),
                houseIndexTemplate.getSourceUrlId(), houseIndexTemplate.getPrice(), houseIndexTemplate.getSquare());
    }

    @Override
    public void delete(String... sourceUrlId) {
        for (String id : sourceUrlId) {
            // 加入批量操作
            bulkProcessor.add(new DeleteRequest(HouseIndexConstant.INDEX_NAME, HouseIndexConstant.TYPE_NAME, id));

            // 删除LBS中的数据
            baiduMapService.removeLBS(id);
        }
    }

    private void fetchSuggest(HouseIndexTemplate houseIndexTemplate) {
        AnalyzeRequestBuilder analyzeRequestBuilder;

        String description = houseIndexTemplate.getDescription();
        // 获取分词结果
        if (StringUtils.isNotBlank(description)) {
            analyzeRequestBuilder = new AnalyzeRequestBuilder(
                    this.esClient, AnalyzeAction.INSTANCE, HouseIndexConstant.INDEX_NAME,
                    houseIndexTemplate.getTitle(), description);
        } else {
            analyzeRequestBuilder = new AnalyzeRequestBuilder(
                    this.esClient, AnalyzeAction.INSTANCE, HouseIndexConstant.INDEX_NAME,
                    houseIndexTemplate.getTitle());
        }
        // 这里的分词器要和 索引/搜索 时使用的分词器一致，否则可能按照搜索建议搜索反而搜索不到内容
        analyzeRequestBuilder.setAnalyzer("ik_smart");

        logger.debug(analyzeRequestBuilder.toString());

//        AnalyzeResponse analyzeTokens = analyzeRequestBuilder.get();
        List<AnalyzeResponse.AnalyzeToken> tokens = analyzeRequestBuilder.execute().actionGet().getTokens();

        List<HouseSuggest> suggests = new ArrayList<>();
        if (tokens == null) {
            logger.warn("Can not analyze token for sourceUrlId : {}", houseIndexTemplate.getSourceUrlId());

        } else {
            Set<String> inputSet = new HashSet<>();
            for (AnalyzeResponse.AnalyzeToken token : tokens) {
                // 排除长度小于3或者相应的类型
                String tokenType = token.getType();
                if (token.getTerm().length() < 3 || (!StringUtils.equalsIgnoreCase(tokenType, "CN_WORD")
                        && !StringUtils.equalsIgnoreCase(tokenType, "TYPE_CQUAN"))) {
                    continue;
                }

                inputSet.add(token.getTerm());
            }
            // 默认权重一样
            HouseSuggest suggest = new HouseSuggest();
            suggest.setInput(inputSet);
            suggests.add(suggest);

            // 定制化 小区/户型 自动补全
//            HouseSuggest suggestCommunity = new HouseSuggest();
//            suggestCommunity.setInput(Sets.newHashSet(houseIndexTemplate.getCommunity(), houseIndexTemplate.getHouseType()));
//            suggestCommunity.setWeight(20);
//            suggests.add(suggestCommunity);
        }

        // 定制化小区自动补全
        String community = houseIndexTemplate.getCommunity();
        if (StringUtils.isNotBlank(community)) {
            HouseSuggest suggestCommunity = new HouseSuggest();
            suggestCommunity.setInput(Sets.newHashSet(community));
            suggestCommunity.setWeight(20);
            suggests.add(suggestCommunity);
        }

        if (suggests.size() > 0) {
            houseIndexTemplate.setSuggest(suggests);
        }
    }

    private void formatRegion(HouseIndexTemplate houseIndexTemplate) {
        if (jsonNode == null) {
            synchronized (this) {
                if (jsonNode == null) {
                    try {
                        jsonNode = objectMapper.readTree(
                                new File("src/main/resources/static/lib/json/city_region.json"));
                    } catch (IOException e) {
                        logger.error("加载区域json文件出现异常", e);
                    }
                }
            }
        }

        String region = houseIndexTemplate.getRegion();

        // 格式化区域信息
        for (JsonNode item : jsonNode) {
            String name = item.get("name").asText();
            if (name.contains(region)) {
                houseIndexTemplate.setRegion(name);
                break;
            }
        }
    }

    /**
     * 房源列表（后台管理部分）
     *
     * @param pageForm
     * @return
     */
    @Override
    public PageUtils findPage(PageForm pageForm) {
        Integer curr  = pageForm.getCurr();
        Integer limit = pageForm.getLimit();

        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(HouseIndexConstant.INDEX_NAME)
                .setTypes(HouseIndexConstant.TYPE_NAME);

        String keyword = pageForm.getKeyword();
        if (StringUtils.isNotBlank(keyword)) {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

            boolQueryBuilder.must(
                    QueryBuilders.multiMatchQuery(keyword)
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

            searchRequestBuilder.setQuery(boolQueryBuilder);
        }

        SearchResponse searchResponse = searchRequestBuilder
                .setFrom(curr - 1)
                .setSize(limit)
                .get();

        SearchHit[] hits = searchResponse.getHits().getHits();

        List<HouseIndexTemplate> dataList = new ArrayList<>();
        for (SearchHit hit : hits) {
            try {
                HouseIndexTemplate houseIndexTemplate = objectMapper.readValue(hit.getSourceAsString(), HouseIndexTemplate.class);
                dataList.add(houseIndexTemplate);
            } catch (IOException e) {
                logger.error("objectMapper转换ES数据出现异常", e);
            }
        }

        return new PageUtils(searchResponse.getHits().getTotalHits(), dataList);
    }

    @Override
    public HouseIndexTemplate findById(String id) {
        GetRequestBuilder getRequestBuilder =
                esClient.prepareGet(HouseIndexConstant.INDEX_NAME, HouseIndexConstant.TYPE_NAME, id);

        GetResponse response       = getRequestBuilder.get();
        String      sourceAsString = response.getSourceAsString();

        HouseIndexTemplate houseIndexTemplate = new HouseIndexTemplate();
        if (StringUtils.isNotBlank(sourceAsString)) {
            try {
                houseIndexTemplate = objectMapper.readValue(sourceAsString, HouseIndexTemplate.class);
            } catch (IOException e) {
                logger.warn("房源数据从json字符串转换成对象出现异常", e);
            }
        }

        return houseIndexTemplate;
    }

    @Override
    public List<HouseIndexTemplate> multiGet(String... sourceUrlIds) {
        List<HouseIndexTemplate> houseIndexTemplateList = new ArrayList<>();

        MultiGetResponse multiGetItemResponses =
                esClient.prepareMultiGet()
                        .add(HouseIndexConstant.INDEX_NAME, HouseIndexConstant.TYPE_NAME, sourceUrlIds)
                        .get();

        for (MultiGetItemResponse next : multiGetItemResponses) {
            String sourceAsString = next.getResponse().getSourceAsString();
            try {
                if (StringUtils.isNotBlank(sourceAsString)) {
                    HouseIndexTemplate houseIndexTemplate = objectMapper.readValue(sourceAsString, HouseIndexTemplate.class);
                    houseIndexTemplateList.add(houseIndexTemplate);
                }
            } catch (IOException e) {
                logger.warn("房源数据从json字符串转换成对象出现异常", e);
            }
        }

        return houseIndexTemplateList;
    }

    @Override
    public Set<String> scrollGetIds() {
        MatchAllQueryBuilder queryBuilders = QueryBuilders.matchAllQuery();
        boolean sourceUrlId = SearchSourceBuilder._SOURCE_FIELD
                .match(HouseIndexConstant.SOURCE_URL_ID);
        SearchResponse scrollResp = esClient.prepareSearch(HouseIndexConstant.INDEX_NAME)
                .addSort("_doc", SortOrder.DESC) // 采用基于_doc进行排序的方式，性能较高。
                .setScroll(new TimeValue(60000))  // 时间窗口
                .setQuery(queryBuilders)
                .setSize(100)
                .setFetchSource(sourceUrlId)
                .get();

        Set<String> sourceUrlIds = new HashSet<>();
        do {
            for (SearchHit hit : scrollResp.getHits().getHits()) {
                String id = hit.getId();
                sourceUrlIds.add(id);
            }

            scrollResp = esClient.prepareSearchScroll(scrollResp.getScrollId())
                    .setScroll(new TimeValue(60000))
                    .execute()
                    .actionGet();
        }
        while (scrollResp.getHits().getHits().length != 0); // Zero hits mark the end of the scroll and the while loop.

        return sourceUrlIds;
    }

    private void addBulkProcessor(HouseIndexTemplate houseIndexTemplate) {
        try {
            String json = objectMapper.writeValueAsString(houseIndexTemplate);

            // 加入bulkProcessor
            bulkProcessor.add(new IndexRequest(HouseIndexConstant.INDEX_NAME, HouseIndexConstant.TYPE_NAME,
                    houseIndexTemplate.getSourceUrlId()).source(json, XContentType.JSON));
        } catch (JsonProcessingException e) {
            logger.error("objectMapper转换房源json信息出现异常", e);
        }
    }
}
