package com.isut.service;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.isut.dto.RatingRequestDto;
import com.isut.dto.ApiResponseDto.ApiResponseDtoBuilder;

@Service
public interface IRatingService {

	void addRating(@Valid RatingRequestDto ratingRequestDto, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void getRatingListByDriverId(long driverId, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void getRatingListByUserId(long userId, ApiResponseDtoBuilder apiResponseDtoBuilder);

}
