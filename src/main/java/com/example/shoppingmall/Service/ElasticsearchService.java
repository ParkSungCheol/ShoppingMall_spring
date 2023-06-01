package com.example.shoppingmall.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
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
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ElasticsearchService {
    private final RestHighLevelClient client;
    private final ElasticsearchOperations elasticsearchOperations;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    public ElasticsearchService(RestHighLevelClient client, ElasticsearchOperations elasticsearchOperations) {
        this.client = client;
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public List<Goods> getDataFromElasticsearch(SearchDto params, String date) throws IOException {
    	SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
    	BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

    	if (params.getSearchValue() != null && !params.getSearchValue().equals("")) {
    	    BoolQueryBuilder mustQuery = QueryBuilders.boolQuery();
    	    mustQuery.should(QueryBuilders.matchQuery("name", params.getSearchValue()));
    	    mustQuery.should(QueryBuilders.matchQuery("name.nori", params.getSearchValue()));
    	    mustQuery.should(QueryBuilders.matchQuery("name.ngram", params.getSearchValue()));
    	    boolQuery.must(mustQuery);
    	}

    	boolQuery.filter(QueryBuilders.termQuery("is_deleted", 0));

    	if (params.getSearchMinPrice() != null && params.getSearchMinPrice() > 0) {
    	    boolQuery.filter(QueryBuilders.rangeQuery("price").gt(params.getSearchMinPrice()));
    	}

    	if (params.getSearchMaxPrice() != null && params.getSearchMaxPrice() > 0) {
    	    boolQuery.filter(QueryBuilders.rangeQuery("price").lt(params.getSearchMaxPrice()));
    	}

    	boolQuery.filter(QueryBuilders.matchQuery("insertion_time", date));

    	sourceBuilder.query(boolQuery);

    	// ORDER BY 절 추가
    	if (params.getOrderBy() == null || params.getOrderBy().equals("")) {
    	    sourceBuilder.sort("_score", SortOrder.DESC);
    	} else if (params.getOrderBy().equals("priceASC")) {
    	    sourceBuilder.sort("price", SortOrder.ASC);
    	} else if (params.getOrderBy().equals("priceDESC")) {
    	    sourceBuilder.sort("price", SortOrder.DESC);
    	} else if (params.getOrderBy().equals("dateASC")) {
    	    sourceBuilder.sort("insertion_time", SortOrder.ASC);
    	} else if (params.getOrderBy().equals("dateDESC")) {
    	    sourceBuilder.sort("insertion_time", SortOrder.DESC);
    	}

    	sourceBuilder.aggregation(AggregationBuilders.terms("unique_docs")
    	        .script(new Script("doc['price'].value + '|' + doc['sellid.keyword'].value + '|' + doc['name.keyword'].value"))
		    	.subAggregation(
		                AggregationBuilders.topHits("sample_doc")
		                        .size(1)
		        ));

    	sourceBuilder.from((params.getPage() - 1) * params.getRecordSize());
    	sourceBuilder.size(params.getRecordSize());

    	SearchRequest searchRequest = new SearchRequest("goods");
    	searchRequest.source(sourceBuilder);
    	searchRequest.scroll(TimeValue.timeValueMinutes(1));

    	SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
    	Terms terms = searchResponse.getAggregations().get("unique_docs");
    	// 검색 결과 처리
    	List<Goods> dataList = new ArrayList<>();
    	ObjectMapper objectMapper = new ObjectMapper();
    	for (Bucket bucket : terms.getBuckets()) {
    		// "_source" 데이터에 접근
    		int count = 0;
            SearchResponse sampleDocResponse = bucket.getAggregations().get("sample_doc");
            for(org.elasticsearch.search.SearchHit searchHit : sampleDocResponse.getHits()) {
            	Goods goods = objectMapper.convertValue(searchHit.getSourceAsMap(), Goods.class);
            	logger.info(goods.toString() + " count : " + ++count);
        	    dataList.add(goods);
            }
    	}

    	// 집계 결과 처리
    	Aggregations aggregations = searchResponse.getAggregations();
    	// 집계 결과에 접근하여 필요한 처리 수행

    	// 사용이 끝난 경우 클라이언트를 닫아줍니다.
    	client.close();

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
    	        .withQuery(boolQuery)
    	        .addAggregation(AggregationBuilders.terms("unique_docs")
                        .script(new Script("doc['price'].value + '|' + doc['sellid.keyword'].value + '|' + doc['name.keyword'].value")));

    	// NativeSearchQuery를 사용하여 쿼리 생성
    	NativeSearchQuery searchQueryComplete = searchQuery.build();
    	
    	// 쿼리 실행
    	long countAggregation = elasticsearchOperations.count(searchQueryComplete, Goods.class);
    	if(countAggregation > 800) { countAggregation = 800; }
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String extractedDate = dateFormat.format(dataList.get(0).toString());
        return "2023-05-31";
    }
}
