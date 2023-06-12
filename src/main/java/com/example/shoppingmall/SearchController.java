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
	        				 @RequestParam("searchList") String searchListParam,
            				 HttpServletRequest request, 
            				 HttpServletResponse response) throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		List<Search> convertedSearchList = new ArrayList<Search>();
		// URL 디코딩된 JSON 문자열을 다시 객체로 변환
//        List<Map<String, Object>> searchList = objectMapper.readValue(searchListParam, new TypeReference<List<Map<String, Object>>>() {});
//		for (Map<String, Object> search : searchList) {
//			Search convertedSearch = objectMapper.convertValue(search, Search.class);
//			convertedSearchList.add(convertedSearch);
//		}
		List<Map<String, Object>> searchList = parseSearchList(searchListParam);
		logger.info(searchList.get(0).get("searchValue").toString());
//		searchService.updateSearch(userId, convertedSearchList);
		
		return new ResponseEntity<>("ok", HttpStatus.OK);
	}
	
	private List<Map<String, Object>> parseSearchList(String searchListString) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(searchListString, new TypeReference<List<Map<String, Object>>>() {});
        } catch (JsonProcessingException e) {
            // 파싱에 실패한 경우 예외 처리
            e.printStackTrace();
            return null;
        }
    }
	
	@GetMapping("/selectSearch")
	@Transactional(value="txManager")
	public List<Search> selectSearch(@RequestParam("userId") String userId) {
		return searchService.selectSearch(userId);
	}
}
