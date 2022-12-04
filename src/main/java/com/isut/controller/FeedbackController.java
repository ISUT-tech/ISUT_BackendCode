package com.isut.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isut.constants.Constants;
import com.isut.dto.ApiResponseDto;
import com.isut.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.isut.dto.FeedbackRequestDto;
import com.isut.dto.RateUsRequestDto;
import com.isut.service.IFeedbackService;

@CrossOrigin(origins = "*", maxAge = 36000000)
@RestController
@RequestMapping(Constants.API_BASE_URL)
public class FeedbackController {

	@Autowired
	private IFeedbackService feedbackService;

	@RequestMapping(value = "/feedback/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ApiResponseDto addFeedback(@Valid @RequestBody FeedbackRequestDto feedbackRequestDto) {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		feedbackService.addFeedback(feedbackRequestDto, apiResponseDtoBuilder);
		return apiResponseDtoBuilder.build();
	}
	
	@RequestMapping(value = "/rateUs/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ApiResponseDto addRateUs(@Valid @RequestBody RateUsRequestDto rateUsRequestDto) {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		feedbackService.addRateUs(rateUsRequestDto, apiResponseDtoBuilder);
		return apiResponseDtoBuilder.build();
	}

	@RequestMapping(value = "/driver/{driverId}/feedbacks", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ApiResponseDto getFeedbackListByDriverId(@PathVariable(required = true) long driverId) {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		feedbackService.getFeedbackListByDriverId(driverId, apiResponseDtoBuilder);
		return apiResponseDtoBuilder.build();
	}

	@RequestMapping(value = "/customer/{id}/feedbacks", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ApiResponseDto getFeedbackListByUserId(@PathVariable(required = true) long id) {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		feedbackService.getFeedbackListByUserId(id, apiResponseDtoBuilder);
		return apiResponseDtoBuilder.build();
	}

	@PostMapping(value = "/customer/{id}/feedback", produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResponseDto sendFeedBackResponseByUserId(@PathVariable(required = true) long id,
			@RequestParam(required = true) String response) {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		feedbackService.sendFeedBackResponseByUserId(id, response, apiResponseDtoBuilder);
		return apiResponseDtoBuilder.build();
	}
}
