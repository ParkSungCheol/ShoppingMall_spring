package com.example.shoppingmall.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.shoppingmall.Domain.Search;

@Mapper
public interface SearchMapper {

    List<Search> selectSearch(@Param("userId") String userId);
    void deleteSearch(@Param("userId") String userId);
    void insertSearch(@Param("searchList") List<Search> searchList);
}