package com.isut.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.isut.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.isut.dto.DataDto;
import com.isut.dto.NotificationDto;
import com.isut.dto.TipRequestDto;
import com.isut.mapper.CustomMapper;
import com.isut.model.Booking;
import com.isut.model.Driver;
import com.isut.model.PromoCode;
import com.isut.model.Tip;
import com.isut.repository.BookingRepository;
import com.isut.repository.DriverRepository;
import com.isut.repository.PromoCodeRepository;
import com.isut.repository.TipRepository;
import com.isut.repository.UserRepository;
import com.isut.service.ITipService;
import com.isut.utility.Utility;

@Service
public class TipServiceImpl implements ITipService {
	@Autowired
	private TipRepository tipRepository;

	@Autowired
	private CustomMapper customMapper;

	@Autowired
	private UserRepository userRepostory;

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private PromoCodeRepository promoCodeRepository;

	@Autowired
	private DriverRepository driverRepository;

	@Override
	public void addTip(@Valid TipRequestDto tipRequestDto, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		Tip tip = customMapper.tipRequestDtoToTip(tipRequestDto);
		tip.setCreatedAt(new Date());
		tipRepository.save(tip);
		Optional<Booking> booking = bookingRepository.findById(tip.getBookingId());
		if (booking.isPresent()) {
			NotificationDto bookingDto = new NotificationDto();
			DataDto dataDto = new DataDto();
			dataDto.setDestinationLocation(booking.get().getDestinationLocation());
			dataDto.setSourceLocation(booking.get().getSourceLocation());
			dataDto.setFair(booking.get().getFair());
			dataDto.setUserId(booking.get().getUserId());
			dataDto.setDriverId(booking.get().getDriverId());
			dataDto.setBookingId(booking.get().getId());
			dataDto.setStatus(booking.get().getStatus());
			if (booking.get().getPromoCode() != null && !booking.get().getPromoCode().isEmpty()) {
				PromoCode promoCode = promoCodeRepository.findByCode(booking.get().getPromoCode());
				if (promoCode != null) {
					dataDto.setDiscount(promoCode.getDiscount());
				}
			}
			Optional<Driver> driver = driverRepository.findById(tip.getDriverId());
			if (driver.isPresent()) {
				dataDto.setName(driver.get().getFullName());
				dataDto.setIsInvoice(true);
				bookingDto.setTo(driver.get().getAppId());
				bookingDto.setData(dataDto);
				Utility.sendNotification(bookingDto);
			}
		}
		apiResponseDtoBuilder.withMessage("Tip added successfully").withStatus(HttpStatus.OK).withData(tip);
	}

	@Override
	public void getTipListByDriverId(long driverId, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		List<Tip> tips = tipRepository.findByDriverId(driverId);
		apiResponseDtoBuilder.withMessage("success").withStatus(HttpStatus.OK).withData(tips);
	}

	@Override
	public void getTipListByUserId(long userId, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		List<Tip> tips = tipRepository.findByUserId(userId);
		apiResponseDtoBuilder.withMessage("success").withStatus(HttpStatus.OK).withData(tips);
	}

}
