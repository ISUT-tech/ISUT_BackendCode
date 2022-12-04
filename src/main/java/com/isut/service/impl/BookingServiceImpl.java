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
import com.isut.dto.BookingFilterWithPaginationDto;
import com.isut.dto.BookingRequestDto;
import com.isut.dto.BookingWithTipDto;
import com.isut.dto.DataDto;
import com.isut.dto.NotificationDto;
import com.isut.mapper.CustomMapper;
import com.isut.model.Booking;
import com.isut.model.Customer;
import com.isut.model.Driver;
import com.isut.model.PromoCode;
import com.isut.model.Tip;
import com.isut.model.User;
import com.isut.repository.BookingRepository;
import com.isut.repository.PromoCodeRepository;
import com.isut.repository.TipRepository;
import com.isut.repository.UserRepository;
import com.isut.repository.custom.BookingRepositoryCustom;
import com.isut.service.IBookingService;
import com.isut.service.IEmailService;
import com.isut.service.IUserService;
import com.isut.utility.Utility;

@Service
public class BookingServiceImpl implements IBookingService {
	@Autowired
	private BookingRepository bookingRepository;
	@Autowired
	private PromoCodeRepository promoCodeRepository;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TipRepository tipRepository;

	@Autowired
	private CustomMapper customMapper;

	@Autowired
	private BookingRepositoryCustom bookingRepositoryCustom;
	@Autowired
	private IEmailService emailService;
	@Autowired
	private IUserService userService;

	@Override
	public void addBooking(@Valid BookingRequestDto bookingRequestDto, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		if (bookingRequestDto.getPromoCode() != null && !bookingRequestDto.getPromoCode().isEmpty()) {
			PromoCode promoCode = promoCodeRepository.findByCode(bookingRequestDto.getPromoCode());
			if (promoCode == null) {
				apiResponseDtoBuilder.withMessage("Invalid promo code").withStatus(HttpStatus.NOT_FOUND);
				return;
			}
		}
		Booking booking = customMapper.bookingRequestDtoToBooking(bookingRequestDto);
		Date date = new Date();
		booking.setCreatedAt(date);
		NotificationDto bookingDto = new NotificationDto();
		DataDto dataDto = new DataDto();
		dataDto.setDestinationLocation(booking.getDestinationLocation());
		dataDto.setSourceLocation(booking.getSourceLocation());
		dataDto.setFair(booking.getFair());
		dataDto.setUserId(booking.getUserId());
		dataDto.setDriverId(booking.getDriverId());
		bookingRepository.save(booking);
		dataDto.setBookingId(booking.getId());
		dataDto.setStatus(booking.getStatus());
		Driver driver = userService.findDriverById(booking.getDriverId());
		if (driver != null) {
			dataDto.setName(driver.getFullName());
			bookingDto.setTo(driver.getAppId());
			bookingDto.setData(dataDto);
			Utility.sendNotification(bookingDto);
		}

		apiResponseDtoBuilder.withMessage(Constants.NOTIFICATION_ADDED_SUCCESSFULLY).withStatus(HttpStatus.OK)
				.withData(booking);
	}

	@Override
	public void updateBooking(@Valid int status, long bookingId, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		Optional<Booking> booking = bookingRepository.findById(bookingId);

		DataDto dataDto = new DataDto();
		if (!booking.isPresent()) {
			apiResponseDtoBuilder.withMessage(Constants.NO_NOTIFICATION_EXISTS).withStatus(HttpStatus.OK);
			return;
		}
		dataDto.setDestinationLocation(booking.get().getDestinationLocation());
		dataDto.setSourceLocation(booking.get().getSourceLocation());
		dataDto.setFair(booking.get().getFair());
		dataDto.setUserId(booking.get().getUserId());
		dataDto.setDriverId(booking.get().getDriverId());
		dataDto.setBookingId(booking.get().getId());
		dataDto.setStatus(status);
		Customer customer = userService.findCustomerById(booking.get().getUserId());
		if (customer != null) {
			NotificationDto bookingDto = new NotificationDto();
			dataDto.setName(customer.getFullName());
			bookingDto.setTo(customer.getAppId());
			bookingDto.setData(dataDto);
			Utility.sendNotification(bookingDto);

			new Thread(() -> {
				Double fair = booking.get().getFair();
				if (booking.get().getPromoCode() != null && !booking.get().getPromoCode().isEmpty()) {
					PromoCode promoCode = promoCodeRepository.findByCode(booking.get().getPromoCode());
					if (promoCode != null) {
						fair = fair - promoCode.getDiscount();
					}
				}
				String subject = "Your invoice";
				String body = "Your invoice has been generated. Please pay $" + fair
						+ " to your driver\n\n Kind Regards\n Team ISUT";
				emailService.sendEmail(customer.getEmail(), subject, body, "", null, null);
			}).start();
		}
		booking.get().setStatus(status);
		booking.get().setUpdatedAt(new Date());
		bookingRepository.save(booking.get());

		Optional<Tip> tip = tipRepository.findByBookingId(booking.get().getId());
		BookingWithTipDto bookingWithTip = new BookingWithTipDto();
		if (booking.get().getPromoCode() != null && !booking.get().getPromoCode().isEmpty()) {
			PromoCode promoCode = promoCodeRepository.findByCode(booking.get().getPromoCode());
			if (promoCode != null) {
				bookingWithTip.setDiscount(promoCode.getDiscount());
			}
		}
		bookingWithTip.setBooking(booking.get());
		if (tip.isPresent()) {
			bookingWithTip.setTip(tip.get().getTip());
		}
		apiResponseDtoBuilder.withMessage(Constants.NOTIFICATION_UPDATED_SUCCESSFULLY).withStatus(HttpStatus.OK)
				.withData(bookingWithTip);
	}

	@Override
	public void getBookingListByDriverId(long driverId, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		List<Booking> bookings = bookingRepository.findByDriverId(driverId);
		apiResponseDtoBuilder.withMessage("success").withStatus(HttpStatus.OK).withData(bookings);
	}

	@Override
	public void getBookingListByUserId(long userId, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		List<Booking> bookings = bookingRepository.findByUserId(userId);
		apiResponseDtoBuilder.withMessage("success").withStatus(HttpStatus.OK).withData(bookings);

	}

	@Override
	public void getBookingListByFilterWithPagination(BookingFilterWithPaginationDto filterWithPagination,
			ApiResponseDtoBuilder apiResponseDtoBuilder) {
		apiResponseDtoBuilder.withMessage("success").withStatus(HttpStatus.OK)
				.withData(bookingRepositoryCustom.getBookingListByFilterWithPagination(filterWithPagination));
	}

	@Override
	public void updateDestinationLocation(String location, Long bookingId,
			ApiResponseDtoBuilder apiResponseDtoBuilder) {
		Optional<Booking> booking = bookingRepository.findById(bookingId);
		if (!booking.isPresent()) {
			apiResponseDtoBuilder.withMessage(Constants.NO_NOTIFICATION_EXISTS).withStatus(HttpStatus.OK);
			return;
		}
		booking.get().setDestinationLocation(location);
		booking.get().setUpdatedAt(new Date());
		bookingRepository.save(booking.get());
		apiResponseDtoBuilder.withMessage("Destination location update successfully").withStatus(HttpStatus.OK)
				.withData(booking);
	}

	@Override
	public void bookingSchedule(@Valid BookingRequestDto bookingRequestDto,
			ApiResponseDtoBuilder apiResponseDtoBuilder) {
		Booking booking = customMapper.bookingRequestDtoToBooking(bookingRequestDto);
		Date date = new Date();
		booking.setCreatedAt(date);
		NotificationDto bookingDto = new NotificationDto();
		DataDto dataDto = new DataDto();
		dataDto.setDestinationLocation(booking.getDestinationLocation());
		dataDto.setSourceLocation(booking.getSourceLocation());
		dataDto.setFair(booking.getFair());
		dataDto.setUserId(booking.getUserId());
		dataDto.setDriverId(booking.getDriverId());
		bookingRepository.save(booking);
		dataDto.setBookingId(booking.getId());
		Optional<User> driver = userRepository.findById(booking.getDriverId());
		if (driver.isPresent()) {
			dataDto.setName(driver.get().getFullName());
			bookingDto.setData(dataDto);
			Utility.sendNotification(bookingDto);
		}
		apiResponseDtoBuilder.withMessage(Constants.BOOKING_SCHEDULE).withStatus(HttpStatus.OK).withData(booking);

	}

	@Override
	public void findBookingById(Long bookingId, ApiResponseDtoBuilder apiResponseDtoBuilder) {

		Optional<Booking> booking = bookingRepository.findById(bookingId);
		if (booking.isPresent()) {
			Optional<Tip> tip = tipRepository.findByBookingId(booking.get().getId());
			BookingWithTipDto bookingWithTip = new BookingWithTipDto();
			if (booking.get().getPromoCode() != null && !booking.get().getPromoCode().isEmpty()) {
				PromoCode promoCode = promoCodeRepository.findByCode(booking.get().getPromoCode());
				if (promoCode != null) {
					bookingWithTip.setDiscount(promoCode.getDiscount());
				}
			}
			if (tip.isPresent()) {
				bookingWithTip.setBooking(booking.get());
				bookingWithTip.setTip(tip.get().getTip());
				apiResponseDtoBuilder.withMessage("success").withStatus(HttpStatus.OK).withData(bookingWithTip);
				return;
			} else {
				bookingWithTip.setBooking(booking.get());
				apiResponseDtoBuilder.withMessage("success").withStatus(HttpStatus.OK).withData(bookingWithTip);
				return;
			}
		} else {
			apiResponseDtoBuilder.withMessage("No Booking Found").withStatus(HttpStatus.NOT_FOUND).withData(booking);
			return;
		}

	}

}
