package com.isut.dto;

public class BookingFilterWithPaginationDto {
	private BookingFilterDto filter;
	private PaginationDto pagination;

	public BookingFilterDto getFilter() {
		return filter;
	}

	public void setFilter(BookingFilterDto filter) {
		this.filter = filter;
	}

	public PaginationDto getPagination() {
		return pagination;
	}

	public void setPagination(PaginationDto pagination) {
		this.pagination = pagination;
	}

}
