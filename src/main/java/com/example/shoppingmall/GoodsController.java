package com.example.shoppingmall;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.shoppingmall.Domain.Goods;
import com.example.shoppingmall.Domain.PagingResponse;
import com.example.shoppingmall.Domain.SearchDto;
import com.example.shoppingmall.Service.GoodsService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class GoodsController {

	@Autowired
	private GoodsService goodsService;

	@GetMapping("/goods")
    @Transactional(value="txManager")
    public PagingResponse<Goods> getGoodsList (@RequestParam Map<String, String> param) {
		ObjectMapper objectMapper = new ObjectMapper();
    	SearchDto searchDto = objectMapper.convertValue(param, SearchDto.class);
        return goodsService.getGoodsList(searchDto);
    }
}
