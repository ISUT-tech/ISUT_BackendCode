package com.isut.repostory.custom;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.isut.dto.BookingFilterWithPaginationDto;
import com.isut.dto.PaginationDto;
import com.isut.model.Booking;

@Repository
public interface BookingRepositoryCustom {
	PaginationDto getBookingListByFilterWithPagination(BookingFilterWithPaginationDto filterWithPagination);

	int getBookingsBetweenDate(Date sDate, Date eDate);

}