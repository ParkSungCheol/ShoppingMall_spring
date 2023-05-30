package com.example.shoppingmall.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

@Configuration
public class ElasticsearchConfig {
    @Value("${elasticsearch.cluster-name}")
    private String clusterName;

    @Value("${elasticsearch.cluster-nodes}")
    private String clusterNodes;

    @Bean
    public ElasticsearchOperations elasticsearchOperations() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(clusterNodes)
                .build();

        return new ElasticsearchRestTemplate((RestHighLevelClient) RestClients.create(clientConfiguration));
    }
}

