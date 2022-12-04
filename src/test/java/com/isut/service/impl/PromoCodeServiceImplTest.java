package com.isut.service.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.isut.constants.Constants;
import com.isut.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.isut.dto.PromoCodeRequestDto;
import com.isut.mapper.CustomMapper;
import com.isut.model.PromoCode;
import com.isut.repository.PromoCodeRepository;
import com.isut.service.IVerificationTokenService;

@ExtendWith(MockitoExtension.class)
public class PromoCodeServiceImplTest {

	@InjectMocks
	PromoCodeServiceImpl promoCodeServiceImpl;

	@Mock
	PromoCodeRepository promoCodeRepository;

	@Mock
	CustomMapper customMapper;

	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Mock
	IVerificationTokenService verificationTokenService;

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void addPromoCode() {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		PromoCodeRequestDto promoCodeRequestDto = new PromoCodeRequestDto();
		promoCodeRequestDto.setCode("test");
		promoCodeRequestDto.setDescription("123test");
		promoCodeRequestDto.setDiscount(3);
		promoCodeRequestDto.setPromoCode("@123test");
		promoCodeRequestDto.setStatus(true);
		PromoCode promoCode = new PromoCode();
		promoCode.setCreatedAt(new Date());
		when(customMapper.promoCodeRequestDtoToPromoCode(promoCodeRequestDto)).thenReturn(promoCode);

		promoCodeServiceImpl.addPromoCode(promoCodeRequestDto, apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals(Constants.PROMO_CODE_ADD_SUCCESS));

	}

	@Test
	public void getPromoCodeById() {
		long id = 1L;
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		PromoCode promoCode = new PromoCode();
		promoCode.setCreatedAt(new Date());

		promoCodeServiceImpl.getPromoCodeById(id, apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals(Constants.PROMO_CODE_NOT_FOUND));

	}

	@Test
	public void isPromocodeValid() {
		String promoCode = "test";
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		PromoCode pCode = new PromoCode();
		pCode.setCreatedAt(new Date());
		when(promoCodeRepository.findByCode(promoCode)).thenReturn(pCode);

		promoCodeServiceImpl.isPromocodeValid(promoCode, apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals(Constants.PROMO_CODE_INFO));

	}

}
