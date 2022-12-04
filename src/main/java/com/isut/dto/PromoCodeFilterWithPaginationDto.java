package com.isut.dto;

public class PromoCodeFilterWithPaginationDto {
	private PromoCodeFilterDto filter;
	private PaginationDto pagination;

	public PromoCodeFilterDto getFilter() {
		return filter;
	}

	public void setFilter(PromoCodeFilterDto filter) {
		this.filter = filter;
	}

	public PaginationDto getPagination() {
		return pagination;
	}

	public void setPagination(PaginationDto pagination) {
		this.pagination = pagination;
	}

}
