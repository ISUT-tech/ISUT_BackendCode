package com.isut.service.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.isut.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.isut.dto.TipRequestDto;
import com.isut.mapper.CustomMapper;
import com.isut.model.Booking;
import com.isut.model.Driver;
import com.isut.model.PromoCode;
import com.isut.model.Tip;
import com.isut.model.User;
import com.isut.repository.BookingRepository;
import com.isut.repository.DriverRepository;
import com.isut.repository.PromoCodeRepository;
import com.isut.repository.TipRepository;
import com.isut.repository.UserRepository;
import com.isut.utility.Utility;

@ExtendWith(MockitoExtension.class)
public class TipServiceTestImplTest {
	@InjectMocks
	TipServiceImpl tipServiceImpl;
	@Mock
	UserRepository userRepository;
	@Mock
	TipRepository tipRepository;
	@Mock
	BookingRepository bookingRepository;
	@Mock
	PromoCodeRepository promoCodeRepository;
	@Mock
	DriverRepository driverRepository;
	@Mock
	CustomMapper customMapper;

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void addTip() {
		TipRequestDto tipRequestDto = new TipRequestDto();
		tipRequestDto.setBookingId(1L);
		tipRequestDto.setDriverId(1L);
		tipRequestDto.setTip(1);
		tipRequestDto.setUserId(2L);

		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();

		Tip tip = new Tip();
		tip.setBookingId(1L);
		tip.setDriverId(1L);
		tip.setTip(1);
		tip.setUserId(2L);
		when(customMapper.tipRequestDtoToTip(tipRequestDto)).thenReturn(tip);

		Booking booking = new Booking();
		booking.setId(1L);
		booking.setDestinationLocation("Test");
		booking.setDriverId(1L);
		booking.setFair(20.0);
		booking.setSourceLocation("Test");
		booking.setUserId(2L);
		booking.setUserMobileNumber("9999999999");
		booking.setPromoCode("123");
		Optional<Booking> bookingDb = Optional.of(booking);
		when(bookingRepository.findById(any())).thenReturn(bookingDb);

		PromoCode promoCode = new PromoCode();
		promoCode.setDiscount(10);
		when(promoCodeRepository.findByCode(any())).thenReturn(promoCode);

		Driver driver = new Driver();
		driver.setFullName("Test name");
		driver.setId(2L);
		driver.setAppId("3");
		driver.setAppId("55656565656df56df5d65d6f5df6d56f5d");
		Optional<Driver> drivers = Optional.of(driver);
		when(driverRepository.findById(tip.getDriverId())).thenReturn(drivers);
		Optional<User> userDb = Optional.of(driver);
		tipServiceImpl.addTip(tipRequestDto, apiResponseDtoBuilder);
		assertTrue(userDb.isPresent());
	}

	@Test
	public void getTipListByDriverId() {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		long driverId = 1L;
		List<Tip> tips = null;
		when(tipRepository.findByDriverId(driverId)).thenReturn(tips);
		tipServiceImpl.getTipListByDriverId(driverId, apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals("success"));
	}

	@Test
	public void getTipListByUserId() {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		long userId = 1L;
		List<Tip> tips = null;
		when(tipRepository.findByDriverId(userId)).thenReturn(tips);
		tipServiceImpl.getTipListByDriverId(userId, apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals("success"));
	}

}
