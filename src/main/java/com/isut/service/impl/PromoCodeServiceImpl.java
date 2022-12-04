package com.isut.service.impl;

import java.util.Date;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.isut.constants.Constants;
import com.isut.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.isut.dto.PromoCodeFilterWithPaginationDto;
import com.isut.dto.PromoCodeRequestDto;
import com.isut.mapper.CustomMapper;
import com.isut.model.PromoCode;
import com.isut.repository.PromoCodeRepository;
import com.isut.repository.custom.PromoCodeRepositoryCustom;
import com.isut.service.IPromoCodeService;

@Service
public class PromoCodeServiceImpl implements IPromoCodeService {

	@Autowired
	private PromoCodeRepository promoCodeRepository;

	@Autowired
	private CustomMapper customMapper;

	@Autowired
	private PromoCodeRepositoryCustom promoCodeRepositoryCustom;

	@Override
	public void addPromoCode(@Valid PromoCodeRequestDto promoCodeRequestDto,
			ApiResponseDtoBuilder apiResponseDtoBuilder) {
		if (existsByCode(promoCodeRequestDto.getCode())) {
			apiResponseDtoBuilder.withMessage(Constants.PROMO_CODE_ALREADY_EXIST)
					.withStatus(HttpStatus.ALREADY_REPORTED);
			return;
		}
		PromoCode promoCode = customMapper.promoCodeRequestDtoToPromoCode(promoCodeRequestDto);
		promoCode.setCreatedAt(new Date());
		save(promoCode);
		apiResponseDtoBuilder.withMessage(Constants.PROMO_CODE_ADD_SUCCESS).withStatus(HttpStatus.OK);
	}

	@Override
	public void updatePromoCode(@Valid PromoCode promoCode, ApiResponseDtoBuilder apiResponseDtoBuilder) {

	}

	@Override
	public void getPromoCodeById(long id, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		PromoCode promoCode = findById(id);
		if (promoCode == null) {
			apiResponseDtoBuilder.withMessage(Constants.PROMO_CODE_NOT_FOUND).withStatus(HttpStatus.NOT_FOUND);
			return;
		}
		apiResponseDtoBuilder.withMessage(Constants.PROMO_CODE_INFO).withStatus(HttpStatus.OK).withData(promoCode);
	}

	@Override
	public void getAllPromoCode(ApiResponseDtoBuilder apiResponseDtoBuilder) {
		apiResponseDtoBuilder.withMessage(Constants.PROMO_CODE_LIST).withStatus(HttpStatus.OK)
				.withData(promoCodeRepository.findAll());
	}

	@Override
	public void getPromoCodeListByFilterWithPagination(PromoCodeFilterWithPaginationDto filterWithPagination,
			ApiResponseDtoBuilder apiResponseDtoBuilder) {
		apiResponseDtoBuilder.withMessage(Constants.DATA_LIST).withStatus(HttpStatus.OK)
				.withData(promoCodeRepositoryCustom.getPromoCodeListByFilterWithPagination(filterWithPagination));
	}

	private void save(PromoCode promoCode) {
		promoCodeRepository.save(promoCode);
	}

	private boolean existsByCode(String code) {
		return promoCodeRepository.existsByCode(code);
	}

	private PromoCode findById(long id) {
		Optional<PromoCode> promoCode = promoCodeRepository.findById(id);
		return promoCode.isPresent() ? promoCode.get() : null;
	}

	@Override
	public void isPromocodeValid(String promoCode, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		PromoCode code = promoCodeRepository.findByCode(promoCode);
		if (code == null) {
			apiResponseDtoBuilder.withMessage("Invalid Promocode").withStatus(HttpStatus.NOT_FOUND);
			return;
		}
		apiResponseDtoBuilder.withMessage(Constants.PROMO_CODE_INFO).withStatus(HttpStatus.OK).withData(code);
	}

}
