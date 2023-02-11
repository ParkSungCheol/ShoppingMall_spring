package com.example.shoppingmall.Service;

import com.example.shoppingmall.Domain.Goods;
import com.example.shoppingmall.Mapper.GoodsMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GoodsService {

	@Autowired
    private GoodsMapper goodsMapper;

    public List<Goods> getGoodsList() {
        return goodsMapper.getGoodsList();
    }
}