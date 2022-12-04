package com.isut.service.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.isut.constants.Constants;
import com.isut.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.isut.dto.BookingFilterDto;
import com.isut.dto.BookingFilterWithPaginationDto;
import com.isut.dto.BookingRequestDto;
import com.isut.dto.BookingWithTipDto;
import com.isut.dto.DataDto;
import com.isut.dto.NotificationDto;
import com.isut.dto.PaginationDto;
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
import com.isut.service.IUserService;
import com.isut.utility.Utility;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {
	@InjectMocks
	BookingServiceImpl bookingServiceImpl;
	@Mock
	UserRepository userRepository;
	@Mock
	TipRepository tipRepository;
	@Mock
	BookingRepository bookingRepository;
	@Mock
	BookingRepositoryCustom bookingRepositoryCustom;
	@Mock
	PromoCodeRepository promoCodeRepository;
	@Mock
	CustomMapper customMapper;
	@Mock
	IUserService userService;

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void addBooking() {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		BookingRequestDto bookingRequestDto = new BookingRequestDto();
		bookingRequestDto.setDestinationLocation("Test");
		bookingRequestDto.setDriverId(1L);
		bookingRequestDto.setFair(20.0);
		bookingRequestDto.setSourceLocation("Test");
		bookingRequestDto.setUserId(2L);
		bookingRequestDto.setUserMobileNumber("9999999999");
		Booking booking = new Booking();
		booking.setId(1L);
		booking.setDestinationLocation("Test");
		booking.setDriverId(1L);
		booking.setFair(20.0);
		booking.setSourceLocation("Test");
		booking.setUserId(2L);
		booking.setUserMobileNumber("9999999999");
		booking.setPromoCode("123");
		when(customMapper.bookingRequestDtoToBooking(bookingRequestDto)).thenReturn(booking);
		Date date = new Date();
		booking.setCreatedAt(date);
		DataDto dataDto = new DataDto();
		dataDto.setDestinationLocation("test");
		dataDto.setSourceLocation("test123");
		dataDto.setFair(5d);
		dataDto.setUserId(1L);
		dataDto.setDriverId(2L);
		dataDto.setBookingId(booking.getId());
		dataDto.setStatus(booking.getStatus());
		when(bookingRepository.save(booking)).thenReturn(booking);

		Driver driver = new Driver();
		driver.setFullName("Test name");
		driver.setAppId("55656565656df56df5d65d6f5df6d56f5d");
		Optional<User> userDb = Optional.of(driver);
		assertTrue(userDb.isPresent());
		bookingServiceImpl.addBooking(bookingRequestDto, apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals(Constants.NOTIFICATION_ADDED_SUCCESSFULLY));

	}

	@Test
	public void updateBooking() {
		int status = 1;
		long bookingId = 1L;
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
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
		when(bookingRepository.findById(bookingId)).thenReturn(bookingDb);
		assertTrue(bookingDb.isPresent());

		booking.setStatus(status);
		booking.setUpdatedAt(new Date());
		when(bookingRepository.save(booking)).thenReturn(booking);

		Tip tip = new Tip();
		tip.setBookingId(1L);
		tip.setDriverId(1L);
		tip.setTip(1);
		tip.setUserId(2L);
		Optional<Tip> tipDb = Optional.of(tip);
		when(tipRepository.findByBookingId(booking.getId())).thenReturn(tipDb);

		Customer customer = new Customer();
		customer.setAppId("1");
		customer.setId(1L);
		when(userService.findCustomerById(2L)).thenReturn(customer);

		assertTrue(tipDb.isPresent());
		bookingServiceImpl.updateBooking(status, bookingId, apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals(Constants.NOTIFICATION_UPDATED_SUCCESSFULLY));

	}

	@Test
	public void getBookingListByDriverId() {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		long driverId = 1L;
		List<Booking> bookings = null;
		when(bookingRepository.findByDriverId(driverId)).thenReturn(bookings);
		bookingServiceImpl.getBookingListByDriverId(driverId, apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals("success"));
	}

	@Test
	public void getBookingListByUserId() {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		long userId = 1L;
		List<Booking> bookings = null;
		when(bookingRepository.findByUserId(userId)).thenReturn(bookings);
		bookingServiceImpl.getBookingListByUserId(userId, apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals("success"));
	}

	@Test
	public void getBookingListByFilterWithPagination() {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		BookingFilterWithPaginationDto bookingFilterWithPaginationDto = new BookingFilterWithPaginationDto();
		BookingFilterDto bookingFilterDto = new BookingFilterDto();
		bookingFilterDto.setDriverId(1L);
		bookingFilterDto.setUserId(2L);
		bookingFilterWithPaginationDto.setFilter(bookingFilterDto);
		PaginationDto paginationDto = new PaginationDto();
		paginationDto.setCurrentPage(1);
		paginationDto.setPerPage(2);
		paginationDto.setTotalCount(20);
		paginationDto.setTotalPages(50);
		bookingFilterWithPaginationDto.setPagination(paginationDto);
		when(bookingRepositoryCustom.getBookingListByFilterWithPagination(bookingFilterWithPaginationDto))
				.thenReturn(paginationDto);
		bookingServiceImpl.getBookingListByFilterWithPagination(bookingFilterWithPaginationDto, apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals("success"));
	}

	@Test
	public void updateDestinationLocation() {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		String location = "test";
		Long bookingId = 1L;
		Booking Booking = new Booking();
		Optional<Booking> bookingDb = Optional.of(Booking);
		when(bookingRepository.findById(bookingId)).thenReturn(bookingDb);
		bookingServiceImpl.updateDestinationLocation(location, bookingId, apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals("Destination location update successfully"));
	}

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

	@Test
	public void bookingSchedule() {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		BookingRequestDto bookingRequestDto = new BookingRequestDto();
		bookingRequestDto.setDestinationLocation("test");
		bookingRequestDto.setDriverId(1L);
		bookingRequestDto.setFair(2d);
		bookingRequestDto.setPromoCode("123@test");
		bookingRequestDto.setSourceLocation("test");
		bookingRequestDto.setUserId(2L);
		bookingRequestDto.setUserMobileNumber("9999999999");

		Booking booking = new Booking();
		booking.setId(1L);
		booking.setDestinationLocation("Test");
		booking.setDriverId(1L);
		booking.setFair(20.0);
		booking.setSourceLocation("Test");
		booking.setUserId(2L);
		booking.setUserMobileNumber("9999999999");
		booking.setPromoCode("123");
		when(customMapper.bookingRequestDtoToBooking(bookingRequestDto)).thenReturn(booking);
		Driver driver = new Driver();
		driver.setFullName("Test name");
		driver.setAppId("55656565656df56df5d65d6f5df6d56f5d");
		Optional<User> userDb = Optional.of(driver);
		when(userRepository.findById(booking.getDriverId())).thenReturn(userDb);
		assertTrue(userDb.isPresent());
		bookingServiceImpl.bookingSchedule(bookingRequestDto, apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals(Constants.BOOKING_SCHEDULE));
	}

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

	@Test
	public void findBookingById() {
		Long bookingId = 1L;
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
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
		when(bookingRepository.findById(bookingId)).thenReturn(bookingDb);
		assertTrue(bookingDb.isPresent());
		bookingServiceImpl.findBookingById(bookingId, apiResponseDtoBuilder);

	}
}
