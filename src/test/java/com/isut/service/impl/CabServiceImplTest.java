package com.isut.service.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.isut.constants.Constants;
import com.isut.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.isut.dto.CabFilterDto;
import com.isut.dto.CabFilterWithPaginationDto;
import com.isut.dto.CabRequestDto;
import com.isut.dto.PaginationDto;
import com.isut.mapper.CustomMapper;
import com.isut.model.Cab;
import com.isut.model.Driver;
import com.isut.model.License;
import com.isut.model.User;
import com.isut.repository.CabRepository;
import com.isut.repository.DriverRepository;
import com.isut.repository.LicenseRepository;
import com.isut.repository.UserRepository;
import com.isut.repostory.custom.CabRepositoryCustom;
import com.isut.service.IUserService;
import com.isut.service.IVerificationTokenService;

@ExtendWith(MockitoExtension.class)
public class CabServiceImplTest {

	@InjectMocks
	CabServiceImpl cabServiceImpl;

	@Mock
	CabRepository cabRepository;
	@Mock
	UserRepository userRepository;
	@Mock
	IUserService userService;
	@Mock
	CustomMapper customMapper;

	@Mock
	CabRepositoryCustom cabRepositoryCustom;

	@Mock
	IVerificationTokenService verificationTokenService;
	@Mock
	DriverRepository driverRepository;
	@Mock
	LicenseRepository licenseRepository;

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void addCabDetails() {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		CabRequestDto cabRequestDto = new CabRequestDto();
		cabRequestDto.setCarImages("test");
		cabRequestDto.setCarModel("123v");
		cabRequestDto.setCarName("tata");
		cabRequestDto.setCarNumber("4s");
		cabRequestDto.setCarType("w");
		cabRequestDto.setCity("test");
		Cab cab = new Cab();
		when(customMapper.cabRequestDtoToCab(cabRequestDto)).thenReturn(cab);
		User sessionUser = new User();
		sessionUser.setRole(3);
		sessionUser.setId(1L);
		when(userService.getSessionUser()).thenReturn(sessionUser);
		License license = new License();
		when(licenseRepository.findByDriverId(sessionUser.getId())).thenReturn(license);
		cabServiceImpl.addCabDetails(cabRequestDto, apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals(Constants.CAR_DETAILS_ADDED_SUCCESSFULLY));
	}

	@Test
	public void updateCabDetails() {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		Cab cab = new Cab();
		cab.setCarImages("test");
		cab.setCarModel("123v");
		cab.setCarName("tata");
		cab.setCarNumber("4s");
		cab.setCity("test");
		cab.setId(1L);
		Optional<Cab> cabCheck = Optional.of(cab);
		when(cabRepository.findById(cab.getId())).thenReturn(cabCheck);
		assertTrue(cabCheck.isPresent());
		cabServiceImpl.updateCabDetails(cab, apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals(Constants.CAR_DETAILS_UPDATED_SUCCESSFULLY));

	}

	@Test
	public void getCabDetailsByUserId() {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		long userId = 1L;

		Cab cab = new Cab();
		cab.setCarImages("test");
		cab.setCarModel("123v");
		cab.setCarName("tata");
		cab.setCarNumber("4s");
		cab.setCity("test");
		cab.setId(1L);

		List<Cab> cabCheck = new ArrayList<>();
		cabCheck.add(cab);
		when(cabRepository.findByUserId(userId)).thenReturn(cabCheck);
		Driver driver = new Driver();
		driver.setFullName("Test name");
		driver.setId(2L);
		driver.setAppId("3");
		driver.setRole(3);
		driver.setAppId("55656565656df56df5d65d6f5df6d56f5d");
		Optional<Driver> drivers = Optional.of(driver);
		when(driverRepository.findById(userId)).thenReturn(drivers);
		cabServiceImpl.getCabDetailsByUserId(userId, apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals("success"));

	}

	@Test
	public void getCabDetailsById() {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		long id = 1L;
		Cab cab = new Cab();
		cab.setCarImages("test");
		cab.setCarModel("123v");
		cab.setCarName("tata");
		cab.setCarNumber("4s");
		cab.setCity("test");
		cab.setId(1L);
		Optional<Cab> cabCheck = Optional.of(cab);
		when(cabRepository.findById(id)).thenReturn(cabCheck);
		assertTrue(cabCheck.isPresent());
		cabServiceImpl.getCabDetailsById(id, apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals("success"));

	}

	@Test
	public void getAllCabDetails() {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		long id = 1L;
		Cab cab = new Cab();
		cab.setCarImages("test");
		cab.setCarModel("123v");
		cab.setCarName("tata");
		cab.setCarNumber("4s");
		cab.setCity("test");
		cab.setId(1L);
		List<Cab> cabList = new ArrayList<>();
		cabList.add(cab);
		when(cabRepository.findAll()).thenReturn(cabList);
		cabServiceImpl.getAllCabDetails(apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals("success"));

	}

	@Test
	public void getCabListByFilterWithPagination() {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		CabFilterWithPaginationDto CabFilterWithPaginationDto = new CabFilterWithPaginationDto();
		CabFilterDto cabFilterDto = new CabFilterDto();
		cabFilterDto.setKeyword("test");
		CabFilterWithPaginationDto.setFilter(null);

		PaginationDto paginationDto = new PaginationDto();
		paginationDto.setCurrentPage(1);
		paginationDto.setPerPage(5);
		paginationDto.setTotalCount(23);
		paginationDto.setTotalPages(30);
		CabFilterWithPaginationDto.setPagination(paginationDto);

		when(cabRepositoryCustom.getCabListByFilterWithPagination(CabFilterWithPaginationDto))
				.thenReturn(paginationDto);
		cabServiceImpl.getCabListByFilterWithPagination(CabFilterWithPaginationDto, apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals(Constants.DATA_LIST));

	}

	@Test
	public void isActiveCab() {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		long id = 1L;
		boolean active = true;
		Cab cab = new Cab();
		cab.setCarImages("test");
		cab.setCarModel("123v");
		cab.setCarName("tata");
		cab.setCarNumber("4s");
		cab.setCity("test");
		cab.setId(1L);
		Optional<Cab> cabCheck = Optional.of(cab);
		when(cabRepository.findById(id)).thenReturn(cabCheck);
		assertTrue(cabCheck.isPresent());
		cabServiceImpl.isActiveCab(id, active, apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals(Constants.CAB_ACTIVE_SUCCESS));

	}

	@Test
	public void changeStatus() {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		long id = 1L;
		int status = 1;

		Cab cab = new Cab();
		cab.setCarImages("test");
		cab.setCarModel("123v");
		cab.setCarName("tata");
		cab.setCarNumber("4s");
		cab.setCity("test");
		cab.setId(1L);
		Optional<Cab> cabCheck = Optional.of(cab);
		when(cabRepository.findById(id)).thenReturn(cabCheck);
		assertTrue(cabCheck.isPresent());
		cabServiceImpl.changeStatus(id, status, apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals(Constants.CAB_APPROVE_SUCCESS));

	}

	@Test
	public void selectCarByName() {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		String carName = "BMW";
		Cab cab = new Cab();
		cab.setCarImages("test");
		cab.setCarModel("123v");
		cab.setCarName("tata");
		cab.setCarNumber("4s");
		cab.setCity("test");
		cab.setId(1L);
		List<Cab> cabList = new ArrayList<>();
		cabList.add(cab);
		when(cabRepository.findByCarNameContaining(carName)).thenReturn(cabList);
		cabServiceImpl.selectCarByName(carName, apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals("success"));

	}

	@Test
	public void selectCarByType() {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		int carType = 7;
		Cab cab = new Cab();
		cab.setCarImages("test");
		cab.setCarModel("123v");
		cab.setCarName("tata");
		cab.setCarNumber("4s");
		cab.setCity("test");
		cab.setId(1L);
		List<Cab> cabList = new ArrayList<>();
		cabList.add(cab);
		when(cabRepository.findByCarType(carType)).thenReturn(cabList);
		User user = new User();
		user.setId(1L);
		Optional<User> userDb = Optional.of(user);
		when(userRepository.findById(cab.getUserId())).thenReturn(userDb);
		cabServiceImpl.selectCarByType(carType, apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals("success"));

	}
}
