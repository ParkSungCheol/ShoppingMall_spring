package com.example.shoppingmall.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.Max;
import org.elasticsearch.search.aggregations.metrics.MaxAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    public ElasticsearchService(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public List<Goods> getDataFromElasticsearch(SearchDto params, String date) {
    	// BoolQueryBuilder 생성
    	BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

    	if(params.getSearchValue() != null && !params.getSearchValue().equals("")) {
    		// must 조건 추가
    		BoolQueryBuilder mustQuery = QueryBuilders.boolQuery();
    		mustQuery.should(QueryBuilders.matchQuery("name", params.getSearchValue()));
    		mustQuery.should(QueryBuilders.matchQuery("name.nori", params.getSearchValue()));
    		mustQuery.should(QueryBuilders.matchQuery("name.ngram", params.getSearchValue()));
    		boolQuery.must(mustQuery);
    	}
    	boolQuery.filter(QueryBuilders.termQuery("is_deleted", 0));
    	if(params.getSearchMinPrice() != null && params.getSearchMinPrice() > 0) {
    		boolQuery.filter(QueryBuilders.rangeQuery("price").gt(params.getSearchMinPrice()));
    	}
    	if(params.getSearchMaxPrice() != null && params.getSearchMaxPrice() > 0) {
    		boolQuery.filter(QueryBuilders.rangeQuery("price").lt(params.getSearchMaxPrice()));
    	}

    	// insertion_time 조건 추가
    	boolQuery.filter(QueryBuilders.matchQuery("insertion_time", date));
    	
    	// NativeSearchQuery를 사용하여 쿼리 실행
    	NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder()
    	        .withQuery(boolQuery)
    	        .withPageable(PageRequest.of(params.getPage() - 1, params.getRecordSize()));
    	
    	// ORDER BY 절 추가
    	if (params.getOrderBy() == null || params.getOrderBy().equals("")) {
    		searchQuery.withSort(Sort.by(Sort.Direction.DESC, "_score"));
    	} else if (params.getOrderBy().equals("priceASC")) {
    		searchQuery.withSort(Sort.by(Sort.Direction.ASC, "price"));
    	} else if (params.getOrderBy().equals("priceDESC")) {
    		searchQuery.withSort(Sort.by(Sort.Direction.DESC, "price"));
    	} else if (params.getOrderBy().equals("dateASC")) {
    		searchQuery.withSort(Sort.by(Sort.Direction.ASC, "insertion_time"));
    	} else if (params.getOrderBy().equals("dateDESC")) {
    		searchQuery.withSort(Sort.by(Sort.Direction.DESC, "insertion_time"));
    	}

    	// NativeSearchQuery를 사용하여 쿼리 생성
    	NativeSearchQuery searchQueryComplete = searchQuery.build();
    	
    	logger.info("searchQuery.getQuery().toString() : " + searchQueryComplete.getQuery().toString());
    	
        SearchHits<Goods> searchHits = elasticsearchOperations.search(searchQueryComplete, Goods.class);
        List<Goods> dataList = new ArrayList<>();
        for (SearchHit<Goods> searchHit : searchHits) {
            dataList.add(searchHit.getContent());
        }
        logger.info("dataList.size() : " + dataList.size());
        return dataList;
    }
    
    public int count(SearchDto params, String date) {
    	
    	// BoolQueryBuilder 생성
    	BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

    	if(params.getSearchValue() != null && !params.getSearchValue().equals("")) {
    		// must 조건 추가
    		BoolQueryBuilder mustQuery = QueryBuilders.boolQuery();
    		mustQuery.should(QueryBuilders.matchQuery("name", params.getSearchValue()));
    		mustQuery.should(QueryBuilders.matchQuery("name.nori", params.getSearchValue()));
    		mustQuery.should(QueryBuilders.matchQuery("name.ngram", params.getSearchValue()));
    		boolQuery.must(mustQuery);
	    	
	    	logger.info("getSearchValue : " + params.getSearchValue());
    	}
    	boolQuery.filter(QueryBuilders.termQuery("is_deleted", 0));
    	if(params.getSearchMinPrice() != null && params.getSearchMinPrice() > 0) {
    		boolQuery.filter(QueryBuilders.rangeQuery("price").gt(params.getSearchMinPrice()));
    	}
    	if(params.getSearchMaxPrice() != null && params.getSearchMaxPrice() > 0) {
    		boolQuery.filter(QueryBuilders.rangeQuery("price").lt(params.getSearchMaxPrice()));
    	}
    	
    	// insertion_time 조건 추가
    	boolQuery.filter(QueryBuilders.matchQuery("insertion_time", date));
    	
    	// NativeSearchQueryBuilder를 사용하여 쿼리 생성
    	NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder()
    	        .withQuery(boolQuery);

    	// NativeSearchQuery를 사용하여 쿼리 생성
    	NativeSearchQuery searchQueryComplete = searchQuery.build();
    	
    	// 쿼리 실행
    	long countAggregation = elasticsearchOperations.count(searchQueryComplete, Goods.class);
    	if(countAggregation > 10000) { countAggregation = 10000; }
    	logger.info("countAggregation : " + countAggregation);
    	return (int) countAggregation;
    }
    
    public String getDate(SearchDto params) {
    	BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

    	if(params.getSearchValue() != null && !params.getSearchValue().equals("")) {
    		// must 조건 추가
    		BoolQueryBuilder mustQuery = QueryBuilders.boolQuery();
    		mustQuery.should(QueryBuilders.matchQuery("name", params.getSearchValue()));
    		mustQuery.should(QueryBuilders.matchQuery("name.nori", params.getSearchValue()));
    		mustQuery.should(QueryBuilders.matchQuery("name.ngram", params.getSearchValue()));
    		boolQuery.must(mustQuery);
    	}
    	boolQuery.filter(QueryBuilders.termQuery("is_deleted", 0));
    	if(params.getSearchMinPrice() != null && params.getSearchMinPrice() > 0) {
    		boolQuery.filter(QueryBuilders.rangeQuery("price").gt(params.getSearchMinPrice()));
    	}
    	if(params.getSearchMaxPrice() != null && params.getSearchMaxPrice() > 0) {
    		boolQuery.filter(QueryBuilders.rangeQuery("price").lt(params.getSearchMaxPrice()));
    	}

    	// NativeSearchQuery를 사용하여 쿼리 실행
    	NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder()
    	        .withQuery(boolQuery)
    	        .withPageable(PageRequest.of(0, 1));
    	
    	searchQuery.withSort(Sort.by(Sort.Direction.DESC, "insertion_time"));

    	// NativeSearchQuery를 사용하여 쿼리 생성
    	NativeSearchQuery searchQueryComplete = searchQuery.build();
    	
    	logger.info("searchQuery.getQuery().toString() : " + searchQueryComplete.getQuery().toString());
    	
        SearchHits<Goods> searchHits = elasticsearchOperations.search(searchQueryComplete, Goods.class);
        List<Goods> dataList = new ArrayList<>();
        for (SearchHit<Goods> searchHit : searchHits) {
            dataList.add(searchHit.getContent());
        }
        logger.info("dataList.size : " + dataList.size());
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String extractedDate = dateFormat.format(dataList.get(0).getInsertionTime());
        return "2023-05-31";
    }
}
