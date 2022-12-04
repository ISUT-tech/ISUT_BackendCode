package com.isut.service;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.isut.dto.CabFilterWithPaginationDto;
import com.isut.dto.CabRequestDto;
import com.isut.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.isut.model.Cab;

@Service
public interface ICabService {

	void addCabDetails(@Valid CabRequestDto cabRequestDto, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void updateCabDetails(@Valid Cab cab, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void getCabDetailsByUserId(long userId, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void getAllCabDetails(ApiResponseDtoBuilder apiResponseDtoBuilder);

	void getCabListByFilterWithPagination(CabFilterWithPaginationDto filterWithPagination,
			ApiResponseDtoBuilder apiResponseDtoBuilder);

	void getCabDetailsById(long id, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void isActiveCab(long id, boolean active, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void changeStatus(long id, int status, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void selectCarByName(String carName, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void selectCarByType(Integer carType, ApiResponseDtoBuilder apiResponseDtoBuilder);

}
