package com.example.shoppingmall.Service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram.Bucket;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
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


@Service
public class ElasticsearchService {
    private final ElasticsearchOperations elasticsearchOperations;
    private final RestHighLevelClient client;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    public ElasticsearchService(ElasticsearchOperations elasticsearchOperations, RestHighLevelClient client) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.client = client;
    }
    
    public String getStatisticData(String search) throws IOException {
    	QueryBuilder nameMatchQueryBuilder = QueryBuilders.matchQuery("name", search)
    	        .operator(Operator.OR);

    	QueryBuilder nameNoriMatchQueryBuilder = QueryBuilders.matchQuery("name.nori", search)
    	        .operator(Operator.OR);

    	QueryBuilder nameNgramMatchQueryBuilder = QueryBuilders.matchQuery("name.ngram", search)
    	        .operator(Operator.OR);

    	QueryBuilder mustQueryBuilder = QueryBuilders.boolQuery()
    	        .should(nameMatchQueryBuilder)
    	        .should(nameNoriMatchQueryBuilder)
    	        .should(nameNgramMatchQueryBuilder);

    	QueryBuilder filterQueryBuilder = QueryBuilders.termQuery("is_deleted", 0);

    	DateHistogramInterval interval = DateHistogramInterval.DAY;
    	DateHistogramAggregationBuilder aggregations = AggregationBuilders.dateHistogram("dates")
    													.field("insertion_time")
    													.fixedInterval(interval)
    													.subAggregation(AggregationBuilders.avg("average_price").field("price"))
    													.order(BucketOrder.key(false));

    	SearchSourceBuilder query = SearchSourceBuilder.searchSource().query(QueryBuilders.boolQuery()
															                .must(mustQueryBuilder)
															                .filter(filterQueryBuilder)).aggregation(aggregations).size(0);

    	String jsonQuery = query.toString();
    	logger.info("####### jsonQuery : {}", jsonQuery);
    	
    	SearchRequest request = new SearchRequest();
    	request.indices("goods").source(query);
    	SearchResponse response = client.search(request, RequestOptions.DEFAULT);
    	
    	logger.info("####### response : {}", response.toString());
    	Aggregations aggregation = response.getAggregations();
    	
    	logger.info("####### Aggregations : {}", aggregation.toString());
    	if (aggregation != null) {
    	    ParsedDateHistogram dateHistogram = aggregation.get("dates");

    	    if (dateHistogram != null) {
    	        List<? extends Bucket> buckets = dateHistogram.getBuckets();

    	        for (Bucket bucket : buckets) {
    	            String keyAsString = bucket.getKeyAsString();
    	            long docCount = bucket.getDocCount();
    	            double averagePrice = Double.parseDouble(bucket.getAggregations().get("average_price"));

    	            logger.info("keyAsString : {}", keyAsString);
    	            logger.info("docCount : {}", docCount);
    	            logger.info("averagePrice : {}", averagePrice);
    	        }
    	    }
    	}
    	return jsonQuery;
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
    		boolQuery.filter(QueryBuilders.rangeQuery("price").gte(params.getSearchMinPrice()));
    	}
    	if(params.getSearchMaxPrice() != null && params.getSearchMaxPrice() > 0) {
    		boolQuery.filter(QueryBuilders.rangeQuery("price").lte(params.getSearchMaxPrice()));
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
    	
    	logger.info("[ getDataFromElasticsearch ] Query : " + searchQueryComplete.getQuery().toString());
    	
        SearchHits<Goods> searchHits = elasticsearchOperations.search(searchQueryComplete, Goods.class);
        List<Goods> dataList = new ArrayList<>();
        for (SearchHit<Goods> searchHit : searchHits) {
            dataList.add(searchHit.getContent());
        }
        logger.info("[ getDataFromElasticsearch ] dataList.size() : " + dataList.size());
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
	    	
	    	logger.info("[ count ] getSearchValue : " + params.getSearchValue());
    	}
    	boolQuery.filter(QueryBuilders.termQuery("is_deleted", 0));
    	if(params.getSearchMinPrice() != null && params.getSearchMinPrice() > 0) {
    		boolQuery.filter(QueryBuilders.rangeQuery("price").gte(params.getSearchMinPrice()));
    	}
    	if(params.getSearchMaxPrice() != null && params.getSearchMaxPrice() > 0) {
    		boolQuery.filter(QueryBuilders.rangeQuery("price").lte(params.getSearchMaxPrice()));
    	}
    	
    	// insertion_time 조건 추가
    	boolQuery.filter(QueryBuilders.matchQuery("insertion_time", date));
    	
    	// NativeSearchQueryBuilder를 사용하여 쿼리 생성
    	NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder()
    	        .withQuery(boolQuery)
    	        .withMinScore(0.0f); // adjust_pure_negative를 false로 설정

    	// NativeSearchQuery를 사용하여 쿼리 생성
    	NativeSearchQuery searchQueryComplete = searchQuery.build();
    	
    	logger.info("[ count ] date : " + date);
    	logger.info("[ count ] Query : " + searchQueryComplete.getQuery().toString());
    	
    	// 쿼리 실행
    	long countAggregation = elasticsearchOperations.count(searchQueryComplete, Goods.class);
    	logger.info("[ count ] countAggregation : " + countAggregation);
    	if(countAggregation > 800) { countAggregation = 800; }
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
    		boolQuery.filter(QueryBuilders.rangeQuery("price").gte(params.getSearchMinPrice()));
    	}
    	if(params.getSearchMaxPrice() != null && params.getSearchMaxPrice() > 0) {
    		boolQuery.filter(QueryBuilders.rangeQuery("price").lte(params.getSearchMaxPrice()));
    	}

    	// NativeSearchQuery를 사용하여 쿼리 실행
    	NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder()
    	        .withQuery(boolQuery)
    	        .withPageable(PageRequest.of(0, 1));
    	
    	searchQuery.withSort(Sort.by(Sort.Direction.DESC, "insertion_time"));

    	// NativeSearchQuery를 사용하여 쿼리 생성
    	NativeSearchQuery searchQueryComplete = searchQuery.build();
    	
    	logger.info("[ getDate ] Query : " + searchQueryComplete.getQuery().toString());
    	
        SearchHits<Goods> searchHits = elasticsearchOperations.search(searchQueryComplete, Goods.class);
        List<Goods> dataList = new ArrayList<>();
        for (SearchHit<Goods> searchHit : searchHits) {
            dataList.add(searchHit.getContent());
        }
        logger.info("[ getDate ] dataList.size : " + dataList.size());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if(dataList.size() < 1) {
        	return null;
        }
        String extractedDate = dataList.get(0).getInsertionTime().format(formatter);
        logger.info("[ getDate ] getInsertionTime : " + dataList.get(0).getInsertionTime());
        return extractedDate;
    }
}
