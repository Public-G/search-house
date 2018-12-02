package com.github.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * ElasticSearch配置
 *
 * @author ZEALER
 * @date 2018-11-01
 */
@Configuration
public class ElasticSearchConfig {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${elasticsearch.host}")
    private String esHost;

    @Value("${elasticsearch.port}")
    private int esPort;

    @Value("${elasticsearch.cluster.name}")
    private String esName;

    @Bean
    public TransportClient esClient() throws UnknownHostException {
        Settings settings = Settings.builder()
                .put("cluster.name", this.esName)
                .put("client.transport.sniff", true) //自动发现新加入集群的机器(仅限同一网段)
                .build();

        InetSocketTransportAddress master = new InetSocketTransportAddress(
                InetAddress.getByName(this.esHost), this.esPort
        );

        return new PreBuiltTransportClient(settings)
                .addTransportAddress(master);
    }

    /**
     * 根据请求的数量或大小自动刷新批量操作，或者在给定时间段之后
     */
    @Bean
    public BulkProcessor bulkProcessor(TransportClient esClient) {
        return BulkProcessor.builder(esClient,
                new BulkProcessor.Listener() {

                    /**
                     * 批量执行之前调用此方法
                     * @param executionId
                     * @param request
                     */
                    @Override
                    public void beforeBulk(long executionId, BulkRequest request) {
                        logger.debug("请求总数 ：{}", String.valueOf(request.numberOfActions()));
                    }

                    /**
                     * 批量执行后调用此方法，可以检查是否存在一些失败的请求
                     * @param executionId
                     * @param request
                     * @param response
                     */
                    @Override
                    public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
                        logger.debug(" flush是否有失败 : {}，消息：{}", response.hasFailures(), response.buildFailureMessage());
                    }

                    /**
                     * 当批量失败并引发Throwable时，将调用此方法
                     * @param executionId
                     * @param request
                     * @param failure
                     */
                    @Override
                    public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                        logger.error("{} 条请求失败, 原因 : ", request.numberOfActions(), failure);
                    }
                })
                // 1000个请求执行批量处理。
                .setBulkActions(1000)
                // 5MB执行flush。
                .setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB))
                // 无论请求数量多少(请求数 > 0 的情况下)，每隔3分钟flush一次。
                .setFlushInterval(TimeValue.timeValueMinutes(3L))
                // 设置并发请求数，0表示只允许执行单个请求，值1表示允许执行1个并发请求（意味着异步执行flush操作），同时累积新的批量请求。
                .setConcurrentRequests(1)
                // 当一个或多个批量项请求失败并且EsRejectedExecutionException指示可用于处理请求的计算资源太少时，就会尝试重试失败策略。
                // 初始等待100ms，呈指数级增长，最多重试三次。
                .setBackoffPolicy(
                        BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
                .build();
    }


}
