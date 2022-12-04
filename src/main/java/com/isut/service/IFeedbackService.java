package com.isut.service;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.isut.dto.FeedbackRequestDto;
import com.isut.dto.RateUsRequestDto;
import com.isut.dto.ApiResponseDto.ApiResponseDtoBuilder;

@Service
public interface IFeedbackService {

	void addFeedback(@Valid FeedbackRequestDto feedbackRequestDto, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void getFeedbackListByDriverId(long driverId, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void getFeedbackListByUserId(long userId, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void sendFeedBackResponseByUserId(long feedbackId, String response, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void addRateUs(@Valid RateUsRequestDto rateUsRequestDto, ApiResponseDtoBuilder apiResponseDtoBuilder);

}
