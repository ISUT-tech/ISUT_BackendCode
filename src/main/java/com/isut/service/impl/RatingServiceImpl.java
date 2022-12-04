package com.isut.service.impl;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.isut.constants.Constants;
import com.isut.dto.RatingRequestDto;
import com.isut.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.isut.mapper.CustomMapper;
import com.isut.model.Rating;
import com.isut.repository.RatingRepository;
import com.isut.service.IRatingService;

@Service
public class RatingServiceImpl implements IRatingService {
	@Autowired
	private RatingRepository ratingRepository;

	@Autowired
	private CustomMapper customMapper;

	@Override
	public void addRating(@Valid RatingRequestDto ratingRequestDto, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		Rating rating = customMapper.ratingRequestDtoToRating(ratingRequestDto);
		rating.setCreatedAt(new Date());
		ratingRepository.save(rating);
		apiResponseDtoBuilder.withMessage(Constants.RATING_ADD_SUCCESS).withStatus(HttpStatus.OK).withData(rating);
	}

	@Override
	public void getRatingListByDriverId(long driverId, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		List<Rating> ratings = ratingRepository.findByDriverId(driverId);
		apiResponseDtoBuilder.withMessage("success").withStatus(HttpStatus.OK).withData(ratings);
	}

	@Override
	public void getRatingListByUserId(long userId, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		List<Rating> ratings = ratingRepository.findByUserId(userId);
		apiResponseDtoBuilder.withMessage("success").withStatus(HttpStatus.OK).withData(ratings);
	}

}
