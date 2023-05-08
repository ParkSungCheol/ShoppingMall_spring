package com.example.shoppingmall.Mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.shoppingmall.Domain.Goods;
import com.example.shoppingmall.Domain.SearchDto;

import java.util.List;

@Mapper
public interface GoodsMapper {

    void deleteGoodsList();
    void insertGoodsList();
    
    /**
     * 게시글 리스트 조회
     * @return 게시글 리스트
     */
    List<Goods> getGoodsList(SearchDto params);

    /**
     * 게시글 수 카운팅
     * @return 게시글 수
     */
    int count(SearchDto params);
}