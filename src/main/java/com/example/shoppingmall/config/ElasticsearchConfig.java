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
    @Bean
    public ElasticsearchOperations elasticsearchOperations() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("ec2-54-180-119-204.ap-northeast-2.compute.amazonaws.com:3308")
                .build();

        return new ElasticsearchRestTemplate(RestClients.create(clientConfiguration).rest());
    }
}

