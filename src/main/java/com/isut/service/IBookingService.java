package com.isut.service;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.isut.dto.BookingFilterWithPaginationDto;
import com.isut.dto.BookingRequestDto;
import com.isut.dto.ApiResponseDto.ApiResponseDtoBuilder;

@Service
public interface IBookingService {

	void addBooking(@Valid BookingRequestDto bookingRequestDto, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void updateBooking(@Valid int status, long notificationId, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void getBookingListByDriverId(long driverId, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void getBookingListByUserId(long userId, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void getBookingListByFilterWithPagination(BookingFilterWithPaginationDto filterWithPagination,
			ApiResponseDtoBuilder apiResponseDtoBuilder);

	void updateDestinationLocation(String location, Long bookingId, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void bookingSchedule(@Valid BookingRequestDto bookingRequestDto, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void findBookingById(Long bookingId, ApiResponseDtoBuilder apiResponseDtoBuilder);

}
