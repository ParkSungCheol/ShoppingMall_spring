package com.example.shoppingmall.Mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.shoppingmall.Domain.Goods;

import java.util.List;

@Mapper
public interface GoodsMapper {

    List<Goods> getUserList();
}