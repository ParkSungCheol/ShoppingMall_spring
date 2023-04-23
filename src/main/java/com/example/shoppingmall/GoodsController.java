package com.example.shoppingmall;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shoppingmall.Domain.Goods;
import com.example.shoppingmall.Service.GoodsService;

@RestController
public class GoodsController {

	@Autowired
	private GoodsService goodsService;

	@GetMapping("/goods")
    @Transactional(value="txManager")
    public List<Goods> getGoodsList () {
        return goodsService.getGoodsList();
    }
}
