package com.isut.mapper;

import java.util.List;

import javax.validation.Valid;

import org.mapstruct.Mapper;

import com.isut.dto.AdminRequestDto;
import com.isut.dto.BookingRequestDto;
import com.isut.dto.CabRequestDto;
import com.isut.dto.FeedbackRequestDto;
import com.isut.dto.PromoCodeRequestDto;
import com.isut.dto.RateUsRequestDto;
import com.isut.dto.RatingRequestDto;
import com.isut.dto.TipRequestDto;
import com.isut.dto.UserListResponseDto;
import com.isut.dto.UserRequestDto;
import com.isut.model.Admin;
import com.isut.model.Booking;
import com.isut.model.Cab;
import com.isut.model.Customer;
import com.isut.model.Driver;
import com.isut.model.Feedback;
import com.isut.model.PromoCode;
import com.isut.model.Rating;
import com.isut.model.Tip;
import com.isut.model.User;

@Mapper(componentModel = "spring")
public interface CustomMapper {

	User userRequestDtoToUser(UserRequestDto userRequestDto);

	List<UserListResponseDto> userListToUserListResponseDtoList(List<User> userList);

	Cab cabRequestDtoToCab(@Valid CabRequestDto cabRequestDto);

	Booking bookingRequestDtoToBooking(@Valid BookingRequestDto bookingRequestDto);

	PromoCode promoCodeRequestDtoToPromoCode(@Valid PromoCodeRequestDto promoCodeRequestDto);

	Rating ratingRequestDtoToRating(@Valid RatingRequestDto ratingRequestDto);

	Tip tipRequestDtoToTip(@Valid TipRequestDto tipRequestDto);

	Feedback feedbackRequestDtoToFeedback(@Valid FeedbackRequestDto feedbackRequestDto);

	Admin adminRequestDtoToAdmin(AdminRequestDto adminRequestDto);

	Customer userRequestDtoToCustomer(UserRequestDto userRequestDto);

	Driver userRequestDtoToDriver(UserRequestDto userRequestDto);

	Feedback rateUsRequestDtoToFeedback(@Valid RateUsRequestDto rateUsRequestDto);

}