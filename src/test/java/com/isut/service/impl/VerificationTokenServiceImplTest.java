package com.isut.service.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.isut.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.isut.mapper.CustomMapper;
import com.isut.model.User;
import com.isut.model.VerificationToken;
import com.isut.repository.BookingRepository;
import com.isut.repository.PromoCodeRepository;
import com.isut.repository.TipRepository;
import com.isut.repository.VerificationTokenRepository;
import com.isut.service.IUserService;

@ExtendWith(MockitoExtension.class)
public class VerificationTokenServiceImplTest {
	@InjectMocks
	VerificationTokenServiceImpl verificationTokenServiceImpl;
	@Mock
	VerificationTokenRepository verificationTokenRepository;

	@Mock
	IUserService userService;
	@Mock
	TipRepository tipRepository;
	@Mock
	BookingRepository bookingRepository;
	@Mock
	PromoCodeRepository promoCodeRepository;
	@Mock
	CustomMapper customMapper;

	public static final String TOKEN_INVALID = "invalidToken";
	public static final String TOKEN_EXPIRED = "expired";
	public static final String TOKEN_VALID = "valid";

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
	}

	@SuppressWarnings("null")
	@Test
	public void validateToken() {
		String token = "test";
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken(token);

		Date expiryDate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(expiryDate);
		cal.add(Calendar.DATE, 1);
		expiryDate = cal.getTime();
		verificationToken.setExpiryDate(expiryDate);
		verificationToken.setUser(23L);
		when(verificationTokenRepository.findByToken(token)).thenReturn(verificationToken);
		User user = new User();
		user.setRole(4);
		when(userService.findById(verificationToken.getUser())).thenReturn(user);
		String results = verificationTokenServiceImpl.validateToken(token);
		assertTrue(results.equals("Thank you for verify your email!!"));
	}

	@Test
	public void resendRegistrationToken() {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		long id = 1L;
		User user = new User();
		user.setId(1L);
		when(userService.findById(id)).thenReturn(user);
		verificationTokenServiceImpl.resendRegistrationToken(id, apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals("Confirmation email has been sent"));

	}

}
