package com.isut.service;

import org.springframework.stereotype.Service;

import com.isut.dto.ApiResponseDto.ApiResponseDtoBuilder;

@Service
public interface IGraphService {

	void getAllDetailsCount(ApiResponseDtoBuilder apiResponseDtoBuilder);

}
