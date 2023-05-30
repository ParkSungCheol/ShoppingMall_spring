package com.example.shoppingmall.Mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.shoppingmall.Domain.Goods;
import com.example.shoppingmall.Domain.SearchDto;

import java.util.List;

@Mapper
public interface GoodsMapper {

    void deleteGoodsList();
    void insertGoodsList();
    
}