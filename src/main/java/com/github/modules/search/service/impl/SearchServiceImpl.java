package com.github.modules.search.service.impl;

import com.github.common.utils.PageUtils;
import com.github.modules.search.constant.HouseIndexConstant;
import com.github.modules.search.dto.HouseDTO;
import com.github.modules.search.form.HouseForm;
import com.github.modules.search.service.SearchService;

import com.github.modules.search.utils.ConditionRangeUtils;
import org.apache.catalina.startup.HomesUserDatabase;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.queryparser.xml.builders.BooleanQueryBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;

import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.rescore.QueryRescorerBuilder;
import org.elasticsearch.search.rescore.RescoreBuilder;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("searchService")
public class SearchServiceImpl implements SearchService {

    private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    @Autowired
    private TransportClient esClient;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PageUtils query(HouseForm houseForm) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        boolQueryBuilder.filter(
                QueryBuilders.termQuery(HouseIndexConstant.CITY_CN_NAME, houseForm.getCityCnName())
        );

        if (StringUtils.isNotBlank(houseForm.getRegionCnName()) && !"*".equals(houseForm.getRegionCnName())) {
            boolQueryBuilder.filter(
                    QueryBuilders.termQuery(HouseIndexConstant.REGION_CN_NAME, houseForm.getRegionCnName())
            );
        }

        if (StringUtils.isNotBlank(houseForm.getRentWay())) {
            boolQueryBuilder.filter(
                    QueryBuilders.termQuery(HouseIndexConstant.RENT_WAY, houseForm.getRentWay())
            );
        }

        if (StringUtils.isNotBlank(houseForm.getSourceWebsite())) {
            boolQueryBuilder.filter(
                    QueryBuilders.termQuery(HouseIndexConstant.SOURCE_WEBSITE, houseForm.getSourceWebsite())
            );
        }

        ConditionRangeUtils areaCondition = ConditionRangeUtils.matchArea(houseForm.getAreaBlock());
        if (!ConditionRangeUtils.ALL.equals(areaCondition)) {
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(HouseIndexConstant.AREA);
            if (areaCondition.getMin() > 0) {
                rangeQueryBuilder.gte(areaCondition.getMin());
            }

            if (areaCondition.getMax() > 0) {
                rangeQueryBuilder.lte(areaCondition.getMax());
            }
            boolQueryBuilder.filter(rangeQueryBuilder);
        }

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


        if (StringUtils.isNotBlank(houseForm.getKeyword())) {
            // title增加2.0权重
            boolQueryBuilder.must(
                    QueryBuilders.matchQuery(
                            HouseIndexConstant.TITLE, houseForm.getKeyword()
                    ).boost(2f)
            );

            // best_fields策略, tie_breaker参数优化dis_max搜索
            boolQueryBuilder.must(
                    QueryBuilders.multiMatchQuery(houseForm.getKeyword(),
                            HouseIndexConstant.COMMUNITY,
                            HouseIndexConstant.ADDRESS,
                            HouseIndexConstant.DESCRIPTION)
                            .type(MultiMatchQueryBuilder.Type.BEST_FIELDS)
                            .tieBreaker(0.3f)

            );
        }


//        boolQueryBuilder.should(
//                QueryBuilders.boolQuery()
//                        .should(
//                                QueryBuilders.matchPhraseQuery(HouseIndexConstant.TITLE, houseForm.getKeyword())
//                                        .slop(10))
//                        .should(
//                                QueryBuilders.matchPhraseQuery(HouseIndexConstant.COMMUNITY, houseForm.getKeyword())
//                                        .slop(5))
//                        .should(
//                                QueryBuilders.matchPhraseQuery(HouseIndexConstant.ADDRESS, houseForm.getKeyword())
//                                        .slop(10))
//                        .should(QueryBuilders.matchPhraseQuery(HouseIndexConstant.DESCRIPTION, houseForm.getKeyword())
//                                .slop(50))
//        );

//        ConstantScoreQueryBuilder constantScoreQueryBuilder = QueryBuilders.constantScoreQuery(
//                QueryBuilders.termQuery(HouseIndexConstant.CITY_CN_NAME, houseForm.getCityCnName()));
//
//        logger.debug(constantScoreQueryBuilder.toString());

        logger.debug(boolQueryBuilder.toString());

        SearchResponse searchResponse = esClient.prepareSearch(HouseIndexConstant.INDEX_NAME)
                .setTypes(HouseIndexConstant.TYPE_NAME)
                .setFrom(houseForm.getCurr())
                .setSize(houseForm.getLimit())
                .setQuery(boolQueryBuilder)
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

    @Override
    public HouseDTO queryById(String id) {

        GetResponse response = esClient.prepareGet(HouseIndexConstant.INDEX_NAME, HouseIndexConstant.TYPE_NAME, id).get();

        Assert.notNull(response.getSource(), String.format("id=%s 没有查询到数据", id));

        return modelMapper.map(response.getSource(), HouseDTO.class);
    }
}
