package com.isut.dto;

public class CabFilterWithPaginationDto {
	private CabFilterDto filter;
	private PaginationDto pagination;

	public CabFilterDto getFilter() {
		return filter;
	}

	public void setFilter(CabFilterDto filter) {
		this.filter = filter;
	}

	public PaginationDto getPagination() {
		return pagination;
	}

	public void setPagination(PaginationDto pagination) {
		this.pagination = pagination;
	}

}
