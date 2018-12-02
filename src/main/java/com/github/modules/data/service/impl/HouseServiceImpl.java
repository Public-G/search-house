package com.github.modules.data.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.common.utils.PageUtils;
import com.github.modules.data.pojo.BaiduMapLocation;
import com.github.modules.data.pojo.HouseIndexTemplate;
import com.github.modules.data.pojo.HouseSuggest;
import com.github.modules.data.service.BaiduMapService;
import com.github.modules.search.constant.HouseIndexConstant;
import com.github.modules.search.dto.HouseDTO;
import com.github.modules.data.service.HouseService;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeAction;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequestBuilder;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.SearchHit;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
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
    private BaiduMapService baiduMapService;

    private JsonNode jsonNode;

    @Override
    public void saveOrUpdate(HouseIndexTemplate houseIndexTemplate) {
        // 设置区域代码
        formatRegion(houseIndexTemplate);

        // 获取搜索建议词条
//        fetchSuggest(houseIndexTemplate);

        // 获取百度经纬度
        BaiduMapLocation baiduMapLocation =
                baiduMapService.getBaiduMapLocation(houseIndexTemplate.getCity(), houseIndexTemplate.getRegion(),
                        houseIndexTemplate.getAddress(), houseIndexTemplate.getCommunity());
        houseIndexTemplate.setLocation(baiduMapLocation);

        try {
            String json = objectMapper.writeValueAsString(houseIndexTemplate);

            // 加入bulkProcessor
            bulkProcessor.add(new IndexRequest(HouseIndexConstant.INDEX_NAME, HouseIndexConstant.TYPE_NAME)
                    .source(json, XContentType.JSON));

            // 上传LBS(单独上传，批量上传受限)
//            baiduMapService.uploadLBS(baiduMapLocation, houseIndexTemplate.getTitle(), houseIndexTemplate.getAddress(),
//                    houseIndexTemplate.getSourceUrlId(), houseIndexTemplate.getPrice(), houseIndexTemplate.getSquare());

        } catch (JsonProcessingException e) {
            logger.error("ElasticSearch批量操作失败", e);
        }
    }

    private void fetchSuggest(HouseIndexTemplate houseIndexTemplate) {
        // 获取分词结果
        AnalyzeRequestBuilder analyzeRequestBuilder = new AnalyzeRequestBuilder(
                this.esClient, AnalyzeAction.INSTANCE, HouseIndexConstant.INDEX_NAME,
                houseIndexTemplate.getTitle(), houseIndexTemplate.getAddress(),
                houseIndexTemplate.getDescription());

        // 这里的分词器要和 索引/搜索 时使用的分词器一致，否则可能按照搜索建议搜索反而搜索不到内容
        analyzeRequestBuilder.setAnalyzer("ik_smart");

        logger.debug(analyzeRequestBuilder.toString());

        AnalyzeResponse analyzeTokens = analyzeRequestBuilder.get();

        List<AnalyzeResponse.AnalyzeToken> tokens = analyzeTokens.getTokens();
        if (tokens == null) {
            logger.warn("Can not analyze token for sourceUrlId : {}", houseIndexTemplate.getSourceUrlId());

        } else {
            List<HouseSuggest> suggests = new ArrayList<>();
            Set<String>        inputSet = new HashSet<>();
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
            HouseSuggest suggestCommunity = new HouseSuggest();
            suggestCommunity.setInput(Sets.newHashSet(houseIndexTemplate.getCommunity(), houseIndexTemplate.getHouseType()));
            suggestCommunity.setWeight(20);
            suggests.add(suggestCommunity);

            houseIndexTemplate.setSuggest(suggests);
        }
    }


    private void formatRegion(HouseIndexTemplate houseIndexTemplate) {
        if (null == jsonNode) {
            synchronized (this) {
                if (null == jsonNode) {
                    try {
                        jsonNode = objectMapper.readTree(
                                new File("src/main/resources/static/lib/json/city_region.json"));
                    } catch (IOException e) {
                        logger.error("加载json文件出现异常", e);
                    }
                }
            }
        }

        // 设置区划代码
        for (JsonNode item : jsonNode) {
            if (item.get("name").asText().contains(houseIndexTemplate.getRegion())) {
                houseIndexTemplate.setRegion(item.get("name").asText());
                break;
            }
        }
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
            houseDTO.setSourceUrl(hit.getId());

            dataList.add(houseDTO);
        }

        return new PageUtils(searchResponse.getHits().getTotalHits(), dataList);
    }
}
