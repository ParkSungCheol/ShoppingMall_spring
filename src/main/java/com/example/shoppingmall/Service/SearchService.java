package com.example.shoppingmall.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.shoppingmall.Domain.Search;
import com.example.shoppingmall.Mapper.SearchMapper;

@Service
public class SearchService {

	private final SearchMapper searchMapper;

    @Autowired
    public SearchService(SearchMapper searchMapper) {
    	this.searchMapper = searchMapper;
    }

    public List<Search> selectSearch(String userId) {
    	return searchMapper.selectSearch(userId);
    }
    
    public void updateSearch(String userId, List<Search> searchList) {
    	searchMapper.deleteSearch(userId);
    	searchMapper.insertSearch(searchList);
    }
}