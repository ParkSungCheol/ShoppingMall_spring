package com.example.shoppingmall.Service;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import com.example.shoppingmall.Domain.Goods;
import com.example.shoppingmall.Domain.SearchDto;

@Service
public class ElasticsearchService {
    private final ElasticsearchOperations elasticsearchOperations;

    @Autowired
    public ElasticsearchService(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public List<Goods> getDataFromElasticsearch(SearchDto params) {
    	// BoolQueryBuilder 생성
    	BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

    	// MatchQueryBuilder를 사용하여 should 절에 조건 추가
    	MatchQueryBuilder matchQuery1 = QueryBuilders.matchQuery("name", "틴캐시 5만");
    	MatchQueryBuilder matchQuery2 = QueryBuilders.matchQuery("name.nori", "틴캐시 5만");
    	MatchQueryBuilder matchQuery3 = QueryBuilders.matchQuery("name.ngram", "틴캐시 5만");

    	boolQuery.should(matchQuery1);
    	boolQuery.should(matchQuery2);
    	boolQuery.should(matchQuery3);

    	// NativeSearchQuery를 사용하여 쿼리 실행
    	NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
    	        .withQuery(boolQuery)
    	        .build();

        SearchHits<Goods> searchHits = elasticsearchOperations.search(searchQuery, Goods.class);
        List<Goods> dataList = new ArrayList<>();
        for (SearchHit<Goods> searchHit : searchHits) {
            dataList.add(searchHit.getContent());
        }
        return dataList;
    }
}
