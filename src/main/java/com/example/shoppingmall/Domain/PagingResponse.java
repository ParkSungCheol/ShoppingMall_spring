package com.example.shoppingmall.Domain;
import java.util.ArrayList;
import java.util.List;

public class PagingResponse<T> {

    private List<T> list = new ArrayList<>();
    private Pagination pagination;
    private SearchDto searchDto;

    public PagingResponse(List<T> list, Pagination pagination, SearchDto searchDto) {
        this.list.addAll(list);
        this.pagination = pagination;
        this.searchDto = searchDto;
    }

	public List<T> getList() {
		return list;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public SearchDto getSearchDto() {
		return searchDto;
	}
}