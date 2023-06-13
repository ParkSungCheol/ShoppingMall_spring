package com.example.shoppingmall;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
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
    
	@PostMapping("/updateSearch")
    @Transactional(value="txManager")
	public ResponseEntity<?> updateSearch(@RequestBody String param,
            				 HttpServletRequest request, 
            				 HttpServletResponse response) throws JsonMappingException, JsonProcessingException, UnsupportedEncodingException {
		logger.info(param);
		ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> data = objectMapper.readValue(param, HashMap.class);
        
        String userId = data.get("userId");
        String searchListParam = data.get("searchList");
		String decodedString = URLDecoder.decode(searchListParam, "UTF-8");
	    List<Map<String, Object>> searchList = objectMapper.readValue(decodedString, new TypeReference<List<Map<String, Object>>>(){});
	    List<Search> convertedSearchList = new ArrayList<Search>();
	    
	    // 디코딩된 JSON 배열 사용
	    for (Map<String, Object> searchItem : searchList) {
	        String searchValue = (String) searchItem.get("searchValue");
	        String price = (String) searchItem.get("price");
	        String term = (String) searchItem.get("term");
	        String useYn = (String) searchItem.get("useYn");
	        
	        // 검색 항목 처리 로직 작성
	        Search search = new Search();
	        search.setPrice(Integer.parseInt(price));
	        search.setSearchValue(searchValue);
	        search.setTerm(term);
	        search.setUserId(userId);
	        search.setUseYn(useYn);
	        
	        convertedSearchList.add(search);
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
