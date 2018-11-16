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

    @Bean
    public HttpClient httpClient() {
        return HttpClients.createDefault();
    }


}
