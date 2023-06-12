package com.example.shoppingmall;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.shoppingmall.Domain.Search;
import com.example.shoppingmall.Service.SearchService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class SearchController {

	@Autowired
    private SearchService searchService;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
    
	@GetMapping("/updateSearch")
    @Transactional(value="txManager")
	public ResponseEntity<?> updateSearch(@RequestParam("userId") String userId,
	        				 @RequestParam("searchList") List<Map<String, Object>> searchListParam,
            				 HttpServletRequest request, 
            				 HttpServletResponse response) throws JsonMappingException, JsonProcessingException {
//		logger.info(searchListParam);
//		ObjectMapper objectMapper = new ObjectMapper();
//	    List<Map<String, Object>> searchList = objectMapper.readValue(searchListParam, new TypeReference<List<Map<String, Object>>>(){});
	    
	    // 디코딩된 JSON 배열 사용
	    for (Map<String, Object> searchItem : searchListParam) {
	        String searchValue = (String) searchItem.get("searchValue");
	        Integer price = (Integer) searchItem.get("price");
	        String term = (String) searchItem.get("term");
	        Integer useYn = (Integer) searchItem.get("useYn");
	        
	        // 검색 항목 처리 로직 작성
	        
	        // 예시: 로그 출력
	        logger.info("searchValue: " + searchValue);
	        logger.info("price: " + price);
	        logger.info("term: " + term);
	        logger.info("useYn: " + useYn);
	    }
//		searchService.updateSearch(userId, convertedSearchList);
		
		return new ResponseEntity<>("ok", HttpStatus.OK);
	}
	
	@GetMapping("/selectSearch")
	@Transactional(value="txManager")
	public List<Search> selectSearch(@RequestParam("userId") String userId) {
		return searchService.selectSearch(userId);
	}
}
