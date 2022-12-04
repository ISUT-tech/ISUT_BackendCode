package com.isut.dto;

public class UserFilterWithPaginationDto {
	private UserFilterDto filter;
	private PaginationDto pagination;

	public UserFilterDto getFilter() {
		return filter;
	}

	public void setFilter(UserFilterDto filter) {
		this.filter = filter;
	}

	public PaginationDto getPagination() {
		return pagination;
	}

	public void setPagination(PaginationDto pagination) {
		this.pagination = pagination;
	}

}
