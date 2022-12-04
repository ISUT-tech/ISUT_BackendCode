package com.isut.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.isut.constants.Constants;
import com.isut.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.isut.dto.FeedbackRequestDto;
import com.isut.dto.RateUsRequestDto;
import com.isut.mapper.CustomMapper;
import com.isut.model.Feedback;
import com.isut.model.User;
import com.isut.repository.FeedbackRepository;
import com.isut.repository.UserRepository;
import com.isut.service.IFeedbackService;
import com.isut.utility.Utility;

@Service
public class FeedbackServiceImpl implements IFeedbackService {
	@Autowired
	private FeedbackRepository feedbackRepository;

	@Autowired
	private CustomMapper customMapper;

	@Autowired
	private UserRepository userRepository;

	@Override
	public void addFeedback(@Valid FeedbackRequestDto feedbackRequestDto, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		Feedback feedback = customMapper.feedbackRequestDtoToFeedback(feedbackRequestDto);
		feedback.setCreatedAt(new Date());
		feedbackRepository.save(feedback);
		apiResponseDtoBuilder.withMessage(Constants.RATING_ADD_SUCCESS).withStatus(HttpStatus.OK).withData(feedback);
	}

	@Override
	public void getFeedbackListByDriverId(long driverId, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		List<Feedback> feedbacks = feedbackRepository.findByDriverId(driverId);
		apiResponseDtoBuilder.withMessage("success").withStatus(HttpStatus.OK).withData(feedbacks);
	}

	@Override
	public void getFeedbackListByUserId(long userId, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		List<Feedback> feedbacks = feedbackRepository.findByUserId(userId);
		apiResponseDtoBuilder.withMessage("success").withStatus(HttpStatus.OK).withData(feedbacks);
	}

	@Override
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

	@Override
	public void addRateUs(@Valid RateUsRequestDto rateUsRequestDto, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		Feedback feedback = customMapper.rateUsRequestDtoToFeedback(rateUsRequestDto);
		feedback.setCreatedAt(new Date());
		feedbackRepository.save(feedback);
		apiResponseDtoBuilder.withMessage(Constants.RATING_ADD_SUCCESS).withStatus(HttpStatus.OK).withData(feedback);

	}
}
