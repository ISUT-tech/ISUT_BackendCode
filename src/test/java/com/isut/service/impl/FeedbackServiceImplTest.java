package com.isut.service.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.isut.constants.Constants;
import com.isut.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.isut.dto.FeedbackRequestDto;
import com.isut.mapper.CustomMapper;
import com.isut.model.Feedback;
import com.isut.model.User;
import com.isut.repository.BookingRepository;
import com.isut.repository.FeedbackRepository;
import com.isut.repository.PromoCodeRepository;
import com.isut.repository.UserRepository;
import com.isut.utility.Utility;

@ExtendWith(MockitoExtension.class)
public class FeedbackServiceImplTest {
	@InjectMocks
	FeedbackServiceImpl feedbackServiceImpl;
	@Mock
	UserRepository userRepository;
	@Mock
	FeedbackRepository feedbackRepository;
	@Mock
	BookingRepository bookingRepository;
	@Mock
	PromoCodeRepository promoCodeRepository;
	@Mock
	CustomMapper customMapper;

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
		User user = new User();
		Authentication auth = new UsernamePasswordAuthenticationToken(user, null);

		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	@Test
	public void addFeedback() {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		FeedbackRequestDto feedbackRequestDto = new FeedbackRequestDto();
		feedbackRequestDto.setBookingId(1L);
		feedbackRequestDto.setDriverId(2L);
		feedbackRequestDto.setFeedback("test");
		feedbackRequestDto.setUserId(4L);
		Feedback feedback = new Feedback();
		when(customMapper.feedbackRequestDtoToFeedback(feedbackRequestDto)).thenReturn(feedback);
		feedbackServiceImpl.addFeedback(feedbackRequestDto, apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals(Constants.RATING_ADD_SUCCESS));

	}

	@Test
	public void getFeedbackListByDriverId() {
		long driverId = 1L;
		List<Feedback> feedbacks = null;
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		when(feedbackRepository.findByDriverId(driverId)).thenReturn(feedbacks);
		feedbackServiceImpl.getFeedbackListByDriverId(driverId, apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals("success"));

	}

	@Test
	public void getFeedbackListByUserId() {
		long userId = 1L;
		List<Feedback> feedbacks = null;
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		when(feedbackRepository.findByUserId(userId)).thenReturn(feedbacks);
		feedbackServiceImpl.getFeedbackListByUserId(userId, apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals("success"));

	}

	public void sendFeedBackResponseByUserId(long feedbackId, String response,
			ApiResponseDtoBuilder apiResponseDtoBuilder) {
		Optional<Feedback> feedback = feedbackRepository.findById(feedbackId);
		User sessionUser = Utility.getSessionUser(userRepository);
		if (feedback.isPresent()) {
			if (sessionUser.getRole() == 0 || sessionUser.getRole() == 1) {
				Feedback feedBackResponse = new Feedback();
				feedBackResponse.setBookingId(feedback.get().getBookingId());
				feedBackResponse.setDriverId(feedback.get().getDriverId());
				feedBackResponse.setFeedback(response);
				feedBackResponse.setAdminId(sessionUser.getId());
				feedBackResponse.setUserId(feedback.get().getUserId());
				feedBackResponse.setCreatedAt(new Date());
				feedbackRepository.save(feedBackResponse);
				apiResponseDtoBuilder.withMessage(Constants.FEEDBACK_SENT_SUCCESSFULLY).withStatus(HttpStatus.OK);
				return;
			} else {
				apiResponseDtoBuilder.withMessage(Constants.UNAUTHORIZED).withStatus(HttpStatus.UNAUTHORIZED);
			}

		} else {
			apiResponseDtoBuilder.withMessage(Constants.FEEDBACK_NOT_FOUND).withStatus(HttpStatus.NOT_FOUND);
		}

	}

	@Test
	public void sendFeedBackResponseByUserId() {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		long feedbackId = 1L;
		String response = "Test";
		Feedback feedback = new Feedback();
		feedback.setId(1L);
		feedback.setAdminId(1L);
		feedback.setDriverId(1L);
		feedback.setBookingId(1L);
		feedback.setFeedback("good");
		feedback.setUserId(2L);
		Optional<Feedback> feedbackDb = Optional.of(feedback);
		when(feedbackRepository.findById(feedbackId)).thenReturn(feedbackDb);

		User sessionUser = new User();

		sessionUser.setId(1L);
		sessionUser.setFullName("Test name");
		sessionUser.setEmail("test@gmail.com");
		sessionUser.setMobileNumber("9999999999");
		sessionUser.setRole(3);
		sessionUser.setRole(1);

		User user = new User();
		user.setFullName("Test Admin");
		user.setEmail("testadmin@gmail.com");
		user.setMobileNumber("9999999999");
		when(userRepository.findByEmailOrMobileNumber(anyString(), anyString())).thenReturn(user);
		when(Utility.getSessionUser(userRepository)).thenReturn(sessionUser);

		feedbackServiceImpl.sendFeedBackResponseByUserId(feedbackId, response, apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals(Constants.FEEDBACK_SENT_SUCCESSFULLY));

	}

}
