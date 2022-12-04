package com.isut.service;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.isut.dto.PromoCodeFilterWithPaginationDto;
import com.isut.dto.PromoCodeRequestDto;
import com.isut.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.isut.model.PromoCode;

@Service
public interface IPromoCodeService {

	void addPromoCode(@Valid PromoCodeRequestDto promoCodeRequestDto, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void updatePromoCode(@Valid PromoCode promoCode, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void getPromoCodeById(long id, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void getAllPromoCode(ApiResponseDtoBuilder apiResponseDtoBuilder);

	void getPromoCodeListByFilterWithPagination(PromoCodeFilterWithPaginationDto filterWithPagination,
			ApiResponseDtoBuilder apiResponseDtoBuilder);

	void isPromocodeValid(String promoCode, ApiResponseDtoBuilder apiResponseDtoBuilder);

}
