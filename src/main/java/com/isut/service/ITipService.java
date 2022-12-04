package com.isut.service;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.isut.dto.TipRequestDto;
import com.isut.dto.ApiResponseDto.ApiResponseDtoBuilder;

@Service
public interface ITipService {

	void addTip(@Valid TipRequestDto tipRequestDto, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void getTipListByDriverId(long driverId, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void getTipListByUserId(long userId, ApiResponseDtoBuilder apiResponseDtoBuilder);

}
