package com.isut.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.isut.constants.Constants;
import com.isut.dto.ApiResponseDto;
import com.isut.dto.RatingRequestDto;
import com.isut.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.isut.service.IRatingService;

@CrossOrigin(origins = "*", maxAge = 36000000)
@RestController
@RequestMapping(Constants.API_BASE_URL)
public class RatingController {

	@Autowired
	private IRatingService ratingService;

	@RequestMapping(value = "/rating/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ApiResponseDto addRating(@Valid @RequestBody RatingRequestDto ratingRequestDto) {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		ratingService.addRating(ratingRequestDto, apiResponseDtoBuilder);
		return apiResponseDtoBuilder.build();
	}

	@RequestMapping(value = "/driver/{driverId}/ratings", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ApiResponseDto getRatingListByDriverId(@PathVariable(required = true) long driverId) {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		ratingService.getRatingListByDriverId(driverId, apiResponseDtoBuilder);
		return apiResponseDtoBuilder.build();
	}

	@RequestMapping(value = "/customer/{id}/ratings", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ApiResponseDto getRatingListByUserId(@PathVariable(required = true) long id) {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		ratingService.getRatingListByUserId(id, apiResponseDtoBuilder);
		return apiResponseDtoBuilder.build();
	}
}
