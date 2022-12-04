package com.isut.service;

import org.springframework.stereotype.Service;

import com.isut.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.isut.model.User;

@Service
public interface IVerificationTokenService {

	String validateToken(String token);

	void resendRegistrationToken(Long id, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void sendVerificationToken(User user);

}
