package com.example.shoppingmall.Service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Service;

import com.example.shoppingmall.Domain.Goods;
import com.example.shoppingmall.Domain.SearchDto;

@Service
public class ElasticsearchService {
    private final ElasticsearchOperations elasticsearchOperations;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

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
        logger.info("stringQuery : " + stringQuery.toString() );

        SearchHits<Goods> searchHits = elasticsearchOperations.search(stringQuery, Goods.class);
        List<Goods> dataList = new ArrayList<>();
        for (SearchHit<Goods> searchHit : searchHits) {
            dataList.add(searchHit.getContent());
        }
        return dataList;
    }
}
