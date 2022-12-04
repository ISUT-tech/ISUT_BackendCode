package com.isut.service.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.isut.constants.Constants;
import com.isut.dto.RatingRequestDto;
import com.isut.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.isut.mapper.CustomMapper;
import com.isut.model.Rating;
import com.isut.model.Tip;
import com.isut.repository.BookingRepository;
import com.isut.repository.PromoCodeRepository;
import com.isut.repository.RatingRepository;
import com.isut.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class RatingServiceImplTest {
	@InjectMocks
	RatingServiceImpl ratingServiceImpl;
	@Mock
	UserRepository userRepository;
	@Mock
	RatingRepository ratingRepository;
	@Mock
	BookingRepository bookingRepository;
	@Mock
	PromoCodeRepository promoCodeRepository;
	@Mock
	CustomMapper customMapper;

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void addRating() {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		RatingRequestDto ratingRequestDto = new RatingRequestDto();
		ratingRequestDto.setBookingId(1L);
		ratingRequestDto.setDriverId(2L);
		ratingRequestDto.setRating(3);
		ratingRequestDto.setUserId(4L);
		Rating rating = new Rating();
		rating.setCreatedAt(new Date());
		when(customMapper.ratingRequestDtoToRating(ratingRequestDto)).thenReturn(rating);
		ratingServiceImpl.addRating(ratingRequestDto, apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals(Constants.RATING_ADD_SUCCESS));

	}

	@Test
	public void getRatingListByDriverId() {
		long driverId = 1L;
		List<Rating> ratings = null;
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		when(ratingRepository.findByDriverId(driverId)).thenReturn(ratings);
		ratingServiceImpl.getRatingListByDriverId(driverId, apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals("success"));

	}

	@Test
	public void getRatingListByUserId() {
		long userId = 1L;
		List<Rating> ratings = null;
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		when(ratingRepository.findByUserId(userId)).thenReturn(ratings);
		ratingServiceImpl.getRatingListByUserId(userId, apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals("success"));

	}

}
