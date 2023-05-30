package com.example.shoppingmall.Service;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Service;

import com.example.shoppingmall.Domain.Goods;
import com.example.shoppingmall.Domain.SearchDto;

import java.util.ArrayList;
import java.util.List;

@Service
public class ElasticsearchService {
    private final ElasticsearchOperations elasticsearchOperations;

    @Autowired
    public ElasticsearchService(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public List<Goods> getDataFromElasticsearch(SearchDto params) {
    	String queryString = "{\"query\": {"
    		    + "     \"bool\": {"
    		    + "       \"should\": ["
    		    + "          { \"match\": { \"name\": \"틴캐시 5만\" } },"
    		    + "          { \"match\": { \"name.nori\": \"틴캐시 5만\" } },"
    		    + "          { \"match\": { \"name.ngram\": \"틴캐시 5만\" } }"
    		    + "       ]"
    		    + "     }"
    		    + "  }}";

        StringQuery stringQuery = new StringQuery(queryString);

        SearchHits<Goods> searchHits = elasticsearchOperations.search(stringQuery, Goods.class);
        List<Goods> dataList = new ArrayList<>();
        for (SearchHit<Goods> searchHit : searchHits) {
            dataList.add(searchHit.getContent());
        }
        return dataList;
    }
}
