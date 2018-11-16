package com.github.modules.data.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.common.constant.SysConstant;
import com.github.common.utils.PageUtils;
import com.github.modules.data.entity.SupportAreaEntity;
import com.github.modules.data.pojo.BaiduMapLocation;
import com.github.modules.data.pojo.HouseIndexTemplate;
import com.github.modules.data.pojo.HouseSuggest;
import com.github.modules.data.service.SupportAreaService;
import com.github.modules.search.constant.HouseIndexConstant;
import com.github.modules.search.dto.HouseDTO;
import com.github.modules.data.service.HouseService;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeAction;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequestBuilder;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHit;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.github.common.constant.SysConstant.RequestParam.CURR;
import static com.github.common.constant.SysConstant.RequestParam.LIMIT;

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
    private SupportAreaService supportAreaService;

    private Map<String, String> regionMap = new HashMap<>();

    /**
     * 根据请求的数量或大小自动刷新批量操作，或者在给定时间段之后
     */
    @Bean
    public BulkProcessor bulkProcessor() {
        return BulkProcessor.builder(esClient,
                new BulkProcessor.Listener() {

                    /**
                     * 批量执行之前调用此方法
                     * @param executionId
                     * @param request
                     */
                    @Override
                    public void beforeBulk(long executionId, BulkRequest request) {
                        logger.debug("numberOfActions ：{}", String.valueOf(request.numberOfActions()));
                    }

                    /**
                     * 批量执行后调用此方法，可以检查是否存在一些失败的请求
                     * @param executionId
                     * @param request
                     * @param response
                     */
                    @Override
                    public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
                        if (regionMap.size() > 0) {
                            List<SupportAreaEntity> areaEntityList = new ArrayList<>();

                            Set<Map.Entry<String, String>> entries = regionMap.entrySet();
                            for (Map.Entry<String, String> entry : entries) {
                                SupportAreaEntity areaEntity = supportAreaService.findByCnName(entry.getValue());

                                SupportAreaEntity supportAreaEntity =
                                        new SupportAreaEntity(areaEntity.getAreaId(), entry.getKey(), SysConstant.AreaLevel.REGION.getValue());
                                areaEntityList.add(supportAreaEntity);
                            }
                            supportAreaService.save(areaEntityList);

                            regionMap.clear();
                        }
                        logger.debug(" fail request {}", String.valueOf(response.hasFailures()));
                    }

                    /**
                     * 当批量失败并引发Throwable时，将调用此方法
                     * @param executionId
                     * @param request
                     * @param failure
                     */
                    @Override
                    public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                        logger.error("{} data bulk failed, reason : ", request.numberOfActions(), failure);
                    }
                })
                // 1000个请求执行批量处理。
                .setBulkActions(1000)
                // 5MB执行flush。
                .setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB))
                // 无论请求数量多少(请求数 > 0 的情况下)，每隔2个小时flush一次。
                .setFlushInterval(TimeValue.timeValueMillis(10L))
//                .setFlushInterval(TimeValue.timeValueHours(2L))
                // 设置并发请求数，0表示只允许执行单个请求，值1表示允许执行1个并发请求（意味着异步执行flush操作），同时累积新的批量请求。
                .setConcurrentRequests(1)
                // 当一个或多个批量项请求失败并且EsRejectedExecutionException指示可用于处理请求的计算资源太少时，就会尝试重试失败策略。
                // 初始等待100ms，呈指数级增长，最多重试三次。
                .setBackoffPolicy(
                        BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
                .build();
    }

    @Override
    public void saveOrUpdate(HouseIndexTemplate houseIndexTemplate) {
        regionMap.put(houseIndexTemplate.getRegion(), houseIndexTemplate.getCity());

        fetchSuggest(houseIndexTemplate);

        BaiduMapLocation baiduMapLocation = supportAreaService.getBaiduMapLocation(houseIndexTemplate.getCity(), houseIndexTemplate.getRegion(),
                houseIndexTemplate.getAddress(), houseIndexTemplate.getCommunity());

        houseIndexTemplate.setLocation(baiduMapLocation);

        try {
            String json = objectMapper.writeValueAsString(houseIndexTemplate);

            bulkProcessor.add(new IndexRequest(HouseIndexConstant.INDEX_NAME, HouseIndexConstant.TYPE_NAME).source(json));
        } catch (JsonProcessingException e) {
            logger.error("bulkProcessor failed , reason : {}", e);
        }
    }

    private void fetchSuggest(HouseIndexTemplate houseIndexTemplate) {
        // 获取分词结果
        AnalyzeRequestBuilder analyzeRequestBuilder = new AnalyzeRequestBuilder(
                this.esClient, AnalyzeAction.INSTANCE, HouseIndexConstant.INDEX_NAME,
                houseIndexTemplate.getTitle(), houseIndexTemplate.getAddress(),
                houseIndexTemplate.getDescription());
        analyzeRequestBuilder.setAnalyzer("ik_smart");
        AnalyzeResponse analyzeTokens = analyzeRequestBuilder.get();

        List<AnalyzeResponse.AnalyzeToken> tokens = analyzeTokens.getTokens();
        if (tokens == null) {
            logger.warn("Can not analyze token for sourceUrlId : {}", houseIndexTemplate.getSourceUrlId());
        }

        List<HouseSuggest> suggests = new ArrayList<>();
        Set<String> inputSet = new HashSet<>();
        for (AnalyzeResponse.AnalyzeToken token : tokens) {
            // 排除长度小于2或者相应的类型
            String tokenType = token.getType();
            if (!(token.getTerm().length() > 2 || StringUtils.equalsIgnoreCase(tokenType, "CN_WORD")
                    || StringUtils.equalsIgnoreCase(tokenType, "TYPE_CQUAN"))) {
                continue;
            }

            inputSet.add(token.getTerm());
        }
        // 默认权重一样
        HouseSuggest suggest = new HouseSuggest();
        suggest.setInput(inputSet);
        suggests.add(suggest);

        // 定制化小区自动补全
        HouseSuggest suggestCommunity = new HouseSuggest();
        suggestCommunity.setInput(Sets.newHashSet(houseIndexTemplate.getCommunity()));
        suggestCommunity.setWeight(20);
        suggests.add(suggestCommunity);

        houseIndexTemplate.setSuggest(suggests);
    }

    @Override
    public PageUtils findPage(Map<String, String> params) {
        Integer curr  = Integer.valueOf(params.get(CURR.getName()));
        Integer limit = Integer.valueOf(params.get(LIMIT.getName()));

        SearchResponse searchResponse = esClient.prepareSearch(HouseIndexConstant.INDEX_NAME)
                .setTypes(HouseIndexConstant.TYPE_NAME)
                .setFrom(curr - 1)
                .setSize(limit)
                .get();

        SearchHit[] hits = searchResponse.getHits().getHits();

        List<HouseDTO> dataList = new ArrayList<>();
        for (SearchHit hit : hits) {
            HouseDTO houseDTO = modelMapper.map(hit.getSource(), HouseDTO.class);
            houseDTO.setId(hit.getId());

            dataList.add(houseDTO);
        }

        return new PageUtils(searchResponse.getHits().getTotalHits(), dataList);
    }
}
