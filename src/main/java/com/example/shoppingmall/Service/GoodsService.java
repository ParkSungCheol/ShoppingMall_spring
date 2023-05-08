package com.example.shoppingmall.Service;

import com.example.shoppingmall.Domain.Goods;
import com.example.shoppingmall.Domain.Pagination;
import com.example.shoppingmall.Domain.PagingResponse;
import com.example.shoppingmall.Domain.SearchDto;
import com.example.shoppingmall.Mapper.GoodsMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GoodsService {

	@Autowired
    private GoodsMapper goodsMapper;
	/**
     * 게시글 리스트 조회
     * @param params - search conditions
     * @return list & pagination information
     */
    public PagingResponse<Goods> getGoodsList(final SearchDto params) {

        // 조건에 해당하는 데이터가 없는 경우, 응답 데이터에 비어있는 리스트와 null을 담아 반환
        int count = goodsMapper.count(params);
        if (count < 1) {
            return new PagingResponse<>(Collections.emptyList(), null, null);
        }

        // Pagination 객체를 생성해서 페이지 정보 계산 후 SearchDto 타입의 객체인 params에 계산된 페이지 정보 저장
        Pagination pagination = new Pagination(count, params);
        params.setPagination(pagination);

        // 계산된 페이지 정보의 일부(limitStart, recordSize)를 기준으로 리스트 데이터 조회 후 응답 데이터 반환
        List<Goods> list = goodsMapper.getGoodsList(params);
        return new PagingResponse<>(list, pagination, params);
    }
    
    public void deleteGoodsList() {
    	goodsMapper.deleteGoodsList();
    }
    
    public void insertGoodsList() {
    	goodsMapper.insertGoodsList();
    }
}