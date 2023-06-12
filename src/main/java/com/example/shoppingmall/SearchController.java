package com.example.shoppingmall;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.shoppingmall.Domain.Search;
import com.example.shoppingmall.Service.SearchService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class SearchController {

	@Autowired
    private SearchService searchService;
    
	@GetMapping("/updateSearch")
    @Transactional(value="txManager")
	public ResponseEntity<?> updateSearch(@RequestParam("userId") String userId,
            				 @RequestParam("searchList") List<Map<String, String>> searchList) {
		ObjectMapper objectMapper = new ObjectMapper();
		List<Search> convertedSearchList = new ArrayList<Search>();
		for (Map<String, String> search : searchList) {
			Search convertedSearch = objectMapper.convertValue(search, Search.class);
			convertedSearchList.add(convertedSearch);
		}
		searchService.updateSearch(userId, convertedSearchList);
		
		return new ResponseEntity<>("ok", HttpStatus.OK);
	}
	
	@GetMapping("/selectSearch")
	@Transactional(value="txManager")
	public List<Search> selectSearch(@RequestParam("userId") String userId) {
		return searchService.selectSearch(userId);
	}
}
