package com.github.search;

import com.github.SearchHouseApplicationTests;
import com.github.modules.search.constant.HouseIndexConstant;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ElasticSearchTests extends SearchHouseApplicationTests{

    @Autowired
    private TransportClient esClient;

    @After
    public void closeClient(){
        // 关闭连接
        esClient.close();
    }

//    @Test
//    public void test() {
//        esClient.close();
//        esClient.admin().indices().prepareCreate("test_close_index").get();
//    }

    @Test
    public void createIndex() throws IOException {
        // 1 创建索引
        esClient.admin().indices().prepareCreate("es_index").get();
        esClient.close();
    }

    @Test
    public void deleteIndex(){
        // 1 删除索引
        esClient.admin().indices().prepareDelete("es_index").get();
    }

    // 新建文档（源数据json串）
    @Test
    public void createIndexByJson() throws UnknownHostException {
        // 1 文档数据准备
        String json = "{" + "\"id\":\"1\"," + "\"title\":\"基于Lucene的搜索服务器\","
                + "\"content\":\"它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口\"" + "}";

        IndexResponse indexResponse = esClient.prepareIndex("es_index", "es_type", "1")
                .setSource(json)
                .execute()
                .actionGet();

        // 3 打印返回的结果
        System.out.println("index:" + indexResponse.getIndex());
        System.out.println("type:" + indexResponse.getType());
        System.out.println("id:" + indexResponse.getId());
        System.out.println("version:" + indexResponse.getVersion());
        System.out.println("result:" + indexResponse.getResult());
    }

    // 新建文档（源数据map方式添加json）
    @Test
    public void createIndexByMap() {
        // 1 文档数据准备
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("id", "2");
        json.put("title", "基于Lucene的搜索服务器");
        json.put("content", "它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口");

        IndexResponse indexResponse = esClient.prepareIndex("es_index", "es_type", "2")
                .setSource(json)
                .execute()
                .actionGet();

        // 3 打印返回的结果
        System.out.println("index:" + indexResponse.getIndex());
        System.out.println("type:" + indexResponse.getType());
        System.out.println("id:" + indexResponse.getId());
        System.out.println("version:" + indexResponse.getVersion());
        System.out.println("result:" + indexResponse.getResult());
    }

    // 新建文档（源数据es构建器添加json）
    @Test
    public void createIndexByJsonBuilder() throws Exception {
        // 1 通过es自带的帮助类，构建json数据
        XContentBuilder builder = XContentFactory.jsonBuilder().startObject()
                .field("id", "3")
                .field("title", "基于Lucene的搜索服务器")
                .field("content", "它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口。")
                .endObject();

        // 2 创建文档
        IndexResponse indexResponse = esClient.prepareIndex("es_index", "es_type", "3").setSource(builder).get();

        // 3 打印返回的结果
        System.out.println("index:" + indexResponse.getIndex());
        System.out.println("type:" + indexResponse.getType());
        System.out.println("id:" + indexResponse.getId());
        System.out.println("version:" + indexResponse.getVersion());
        System.out.println("result:" + indexResponse.getResult());
    }

    // 搜索文档数据（单个索引）
    @Test
    public void getData() throws Exception {

//        // 1 查询文档
//        GetResponse response = esClient.prepareGet("es_index", "es_type", "1").get();
        GetResponse response = esClient.prepareGet(HouseIndexConstant.INDEX_NAME, HouseIndexConstant.TYPE_NAME, "6fd184fee8d4a0697581f1f0f13dc20b").get();

        // 2 打印搜索的结果
        System.out.println(response.getSource());
    }


    // 搜索文档数据（多个索引）
    @Test
    public void getMultiData() {

        // 1 查询多个文档
        MultiGetResponse response = esClient.prepareMultiGet()
                .add("es_index", "es_type", "1")
                .add("es_index", "es_type", "2", "3")
                .add("es_index", "es_type", "2").get();

        // 2 遍历返回的结果
        for(MultiGetItemResponse itemResponse:response){
            GetResponse getResponse = itemResponse.getResponse();

            // 如果获取到查询结果
            if (getResponse.isExists()) {
                String sourceAsString = getResponse.getSourceAsString();
                System.out.println(sourceAsString);
            }
        }

    }

    // 更新文档数据（update）
    @Test
    public void updateData() throws Throwable {

        // 1 创建更新数据的请求对象
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index("es_index");
        updateRequest.type("es_type");
        updateRequest.id("3");

        updateRequest.doc(XContentFactory.jsonBuilder().startObject()
                // 对没有的字段添加, 对已有的字段替换
                .field("title", "基于Lucene的搜索服务器")
                .field("content",
                        "它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口。...")
                .field("createDate", "2018-10-22").endObject());

        // 2 获取更新后的值
        UpdateResponse indexResponse = esClient.update(updateRequest).get();

        // 3 打印返回的结果
        System.out.println("index:" + indexResponse.getIndex());
        System.out.println("type:" + indexResponse.getType());
        System.out.println("id:" + indexResponse.getId());
        System.out.println("version:" + indexResponse.getVersion());
        System.out.println("create:" + indexResponse.getResult());
    }

    // 更新文档数据（upsert）
    @Test
    public void testUpsert() throws Exception {

        // 设置查询条件, 查找不到则添加
        IndexRequest indexRequest =
                new IndexRequest("es_index", "es_type", "5").source(XContentFactory.jsonBuilder().startObject()
                        .field("title", "搜索服务器")
                        .field("content","它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口。Elasticsearch是用Java开发的，并作为Apache许可条款下的开放源码发布，是当前流行的企业级搜索引擎。设计用于云计算中，能够达到实时搜索，稳定，可靠，快速，安装使用方便。")
                        .endObject());

        // 设置更新, 查找到更新下面的设置
        UpdateRequest upsert =
                new UpdateRequest("es_index", "es_type", "5").doc(XContentFactory.jsonBuilder().startObject()
                        .field("user", "李四")
                        .endObject())
                        .upsert(indexRequest);

        esClient.update(upsert).get();
    }

    // 删除文档数据（prepareDelete）
    @Test
    public void deleteData() {

        // 1 删除文档数据
        DeleteResponse indexResponse = esClient.prepareDelete("es_index", "es_type", "5").get();

        // 2 打印返回的结果
        System.out.println("index:" + indexResponse.getIndex());
        System.out.println("type:" + indexResponse.getType());
        System.out.println("id:" + indexResponse.getId());
        System.out.println("version:" + indexResponse.getVersion());
        System.out.println("found:" + indexResponse.getResult());
    }

    // 查询所有（matchAllQuery）
    @Test
    public void matchAllQuery() {

        // 1 执行查询
        SearchResponse searchResponse = esClient.prepareSearch("es_index").setTypes("es_type")
                .setQuery(QueryBuilders.matchAllQuery()).get();

        // 2 打印查询结果
        SearchHits hits = searchResponse.getHits(); // 获取命中次数，查询结果有多少对象
        System.out.println("查询结果有：" + hits.getTotalHits() + "条");

        for (SearchHit hit : hits) {
            System.out.println("hit : " + hit.getSource());
        }

        Iterator<SearchHit> iterator = hits.iterator();

        while (iterator.hasNext()) {
            SearchHit searchHit = iterator.next(); // 每个查询对象
            System.out.println(searchHit.getSourceAsString()); // 获取字符串格式打印
        }
    }

    // 对所有字段分词查询（queryStringQuery）
    @Test
    public void query() {
        // 1 条件查询
        SearchResponse searchResponse = esClient.prepareSearch("es_index").setTypes("es_type")
                .setQuery(QueryBuilders.queryStringQuery("全文")).get();

        // 2 打印查询结果
        SearchHits hits = searchResponse.getHits(); // 获取命中次数，查询结果有多少对象
        System.out.println("查询结果有：" + hits.getTotalHits() + "条");

        Iterator<SearchHit> iterator = hits.iterator();

        while (iterator.hasNext()) {
            SearchHit searchHit = iterator.next(); // 每个查询对象

            System.out.println(searchHit.getSourceAsString()); // 获取字符串格式打印
        }
    }

    /**
     * 通配符查询（wildcardQuery）
     *  ：表示多个字符（任意的字符）
     *  ？：表示单个字符
     */
    @Test
    public void wildcardQuery() {

        // 1 通配符查询
        SearchResponse searchResponse = esClient.prepareSearch("es_index").setTypes("es_type")
                .setQuery(QueryBuilders.wildcardQuery("content", "*全*")).get();

        // 2 打印查询结果
        SearchHits hits = searchResponse.getHits(); // 获取命中次数，查询结果有多少对象
        System.out.println("查询结果有：" + hits.getTotalHits() + "条");

        Iterator<SearchHit> iterator = hits.iterator();

        while (iterator.hasNext()) {
            SearchHit searchHit = iterator.next(); // 每个查询对象

            System.out.println(searchHit.getSourceAsString()); // 获取字符串格式打印
        }
    }

    // 词条查询（TermQuery, 完全匹配）
    @Test
    public void termQuery() {
        // 1 第一field查询
        SearchResponse searchResponse = esClient.prepareSearch("es_index").setTypes("es_type")
                .setQuery(QueryBuilders.termQuery("content", "全")).get();

        // 2 打印查询结果
        SearchHits hits = searchResponse.getHits(); // 获取命中次数，查询结果有多少对象
        System.out.println("查询结果有：" + hits.getTotalHits() + "条");

        Iterator<SearchHit> iterator = hits.iterator();

        while (iterator.hasNext()) {
            SearchHit searchHit = iterator.next(); // 每个查询对象

            System.out.println(searchHit.getSourceAsString()); // 获取字符串格式打印
        }
    }

    // 模糊查询（fuzzy）
    @Test
    public void fuzzy() {

        // 1 模糊查询
        SearchResponse searchResponse = esClient.prepareSearch("es_index").setTypes("es_type")
                .setQuery(QueryBuilders.fuzzyQuery("title", "lucene")).get();

        // 2 打印查询结果
        SearchHits hits = searchResponse.getHits(); // 获取命中次数，查询结果有多少对象
        System.out.println("查询结果有：" + hits.getTotalHits() + "条");

        Iterator<SearchHit> iterator = hits.iterator();

        while (iterator.hasNext()) {
            SearchHit searchHit = iterator.next(); // 每个查询对象

            System.out.println(searchHit.getSourceAsString()); // 获取字符串格式打印
        }
    }






}

