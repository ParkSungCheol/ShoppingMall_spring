package com.example.shoppingmall;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.shoppingmall.Domain.Goods;
import com.example.shoppingmall.Domain.PagingResponse;
import com.example.shoppingmall.Domain.SearchDto;
import com.example.shoppingmall.Service.ElasticsearchService;
import com.example.shoppingmall.Service.GoodsService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class GoodsController {

	@Autowired
	private GoodsService goodsService;
	@Autowired
	private ElasticsearchService elasticsearchService;

	@GetMapping("/goods")
    @Transactional(value="txManager")
    public PagingResponse<Goods> getGoodsList (@RequestParam Map<String, String> param) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
    	SearchDto searchDto = objectMapper.convertValue(param, SearchDto.class);
        return goodsService.getGoodsList(searchDto);
    }
	
	@GetMapping("/statistic")
    @Transactional(value="txManager")
    public ResponseEntity<?> getStatisticData (@RequestParam Map<String, String> param) throws IOException {
		elasticsearchService.getStatisticData(param.get("searchValue"));
		return new ResponseEntity<>("ok", HttpStatus.OK);
    }
}
