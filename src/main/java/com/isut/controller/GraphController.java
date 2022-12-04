package com.isut.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isut.constants.Constants;
import com.isut.dto.ApiResponseDto;
import com.isut.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.isut.service.IGraphService;

@CrossOrigin(origins = "*", maxAge = 36000000)
@RestController
@RequestMapping(Constants.API_BASE_URL)
public class GraphController {

	@Autowired
	private IGraphService graphService;

	@GetMapping(value = "/graph", produces = MediaType.APPLICATION_JSON_VALUE)
	private ApiResponseDto getAllCounts() {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		graphService.getAllDetailsCount(apiResponseDtoBuilder);
		return apiResponseDtoBuilder.build();
	}
}
