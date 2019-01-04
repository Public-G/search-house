package com.github.modules.search.service.impl;

import com.github.modules.search.constant.BucketConstant;
import com.github.modules.search.constant.HouseIndexConstant;
import com.github.modules.search.dto.HouseBucketDTO;
import com.github.modules.search.service.MapService;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("mapService")
public class MapServiceImpl implements MapService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TransportClient esClient;

    @Override
    public List<HouseBucketDTO> mapAggsByCity(String city) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        boolQueryBuilder.filter(QueryBuilders.termQuery(HouseIndexConstant.CITY, city));

        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms(BucketConstant.AGG_REGION)
                .field(HouseIndexConstant.REGION);

        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(HouseIndexConstant.INDEX_NAME)
                .setTypes(HouseIndexConstant.TYPE_NAME)
                .setSize(0)
                .setQuery(boolQueryBuilder)
                .addAggregation(aggregationBuilder);

        logger.debug(searchRequestBuilder.toString());

        SearchResponse      response = searchRequestBuilder.get();
        List<HouseBucketDTO> buckets  = new ArrayList<>();
        if (response.status() != RestStatus.OK) {
            logger.warn("Aggregate status is not ok for " + searchRequestBuilder.toString());
            return buckets;
        }

        Terms terms = response.getAggregations().get(BucketConstant.AGG_REGION);
        for (Terms.Bucket bucket : terms.getBuckets()) {
            buckets.add(new HouseBucketDTO(bucket.getKeyAsString(), bucket.getDocCount()));
        }

        return buckets;
    }
}
