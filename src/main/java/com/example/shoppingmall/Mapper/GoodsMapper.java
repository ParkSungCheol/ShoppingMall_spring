package com.example.shoppingmall.Mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodsMapper {

    void deleteGoodsList();
    void insertGoodsList();
    
}