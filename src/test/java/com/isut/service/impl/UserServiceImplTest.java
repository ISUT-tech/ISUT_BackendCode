package com.isut.service.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.isut.constants.Constants;
import com.isut.dto.AdminRequestDto;
import com.isut.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.isut.dto.PaginationDto;
import com.isut.dto.UserFilterDto;
import com.isut.dto.UserFilterWithPaginationDto;
import com.isut.dto.UserRequestDto;
import com.isut.mapper.CustomMapper;
import com.isut.model.Admin;
import com.isut.model.Cab;
import com.isut.model.Customer;
import com.isut.model.Driver;
import com.isut.model.License;
import com.isut.model.User;
import com.isut.repository.AdminRepository;
import com.isut.repository.CabRepository;
import com.isut.repository.CustomerRepository;
import com.isut.repository.DriverRepository;
import com.isut.repository.UserRepository;
import com.isut.repostory.custom.UserRepositoryCustom;
import com.isut.service.IVerificationTokenService;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

	@InjectMocks
	UserServiceImpl userService;

	@Mock
	UserRepository userRepository;
	@Mock
	UserRepositoryCustom userRepositoryCustom;
	@Mock
	CustomMapper customMapper;
	@Mock
	CabRepository cabRepository;
	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;
	@Mock
	AdminRepository adminRepository;
	@Mock
	CustomerRepository customerRepository;
	@Mock
	IVerificationTokenService verificationTokenService;
	@Mock
	DriverRepository driverRepository;

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
		User user = new User();
		Authentication auth = new UsernamePasswordAuthenticationToken(user, null);

		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	@Test
	public void addAdmin() {
		AdminRequestDto userRequestDto = new AdminRequestDto();
		userRequestDto.setFullName("Test Admin");
		userRequestDto.setEmail("testadmin@gmail.com");
		userRequestDto.setMobileNumber("9999999999");
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		HttpServletRequest request = null;
		Admin user = new Admin();
		user.setFullName("Test Admin");
		user.setEmail("testadmin@gmail.com");
		user.setMobileNumber("9999999999");

		when(customMapper.adminRequestDtoToAdmin(userRequestDto)).thenReturn(user);
		when(adminRepository.save(user)).thenReturn(user);
		userService.addAdmin(userRequestDto, apiResponseDtoBuilder, request);
		assertTrue(apiResponseDtoBuilder.getMessage().equals(Constants.USER_ADD_SUCCESS));
	}

	@Test
	public void addAdminIfMobileNumberAlreadyExist() {
		AdminRequestDto userRequestDto = new AdminRequestDto();
		userRequestDto.setFullName("Test Admin");
		userRequestDto.setEmail("testadmin@gmail.com");
		userRequestDto.setMobileNumber("7777777777");
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		HttpServletRequest request = null;

		when(userRepository.existsByMobileNumber(userRequestDto.getMobileNumber())).thenReturn(true);
		userService.addAdmin(userRequestDto, apiResponseDtoBuilder, request);
		assertTrue(apiResponseDtoBuilder.getMessage().equals(Constants.MOBILE_NUMBER_ALREADY_EXISTS));
	}

	@Test
	public void addAdminIfEmailAlreadyExist() {
		AdminRequestDto userRequestDto = new AdminRequestDto();
		userRequestDto.setFullName("Test Admin");
		userRequestDto.setEmail("testadmin@gmail.com");
		userRequestDto.setMobileNumber("9999999999");
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		HttpServletRequest request = null;

		when(userRepository.existsByEmail(userRequestDto.getEmail())).thenReturn(true);
		userService.addAdmin(userRequestDto, apiResponseDtoBuilder, request);
		assertTrue(apiResponseDtoBuilder.getMessage().equals(Constants.EMAIL_ALREADY_EXISTS));
	}

	@Test
	public void addUser() {
		UserRequestDto userRequestDto = new UserRequestDto();
		userRequestDto.setFullName("Test Admin");
		userRequestDto.setEmail("testadmin@gmail.com");
		userRequestDto.setMobileNumber("9999999999");
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		HttpServletRequest request = null;
		Customer customer = new Customer();
		customer.setMobileNumber("8888888888");
		when(customMapper.userRequestDtoToCustomer(userRequestDto)).thenReturn(customer);
		when(customerRepository.save(customer)).thenReturn(customer);
		userService.addUser(userRequestDto, apiResponseDtoBuilder, request);
		assertTrue(apiResponseDtoBuilder.getMessage().equals(Constants.USER_ADD_SUCCESS));
	}

	@Test
	public void addUserIfMobileNumberAlreadyExist() {
		UserRequestDto userRequestDto = new UserRequestDto();
		userRequestDto.setFullName("Test Admin");
		userRequestDto.setEmail("testadmin@gmail.com");
		userRequestDto.setMobileNumber("7777777777");
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		HttpServletRequest request = null;

		when(userRepository.existsByMobileNumber(userRequestDto.getMobileNumber())).thenReturn(true);
		userService.addUser(userRequestDto, apiResponseDtoBuilder, request);
		assertTrue(apiResponseDtoBuilder.getMessage().equals(Constants.MOBILE_NUMBER_ALREADY_EXISTS));
	}

	@Test
	public void addUserIfEmailAlreadyExist() {
		UserRequestDto userRequestDto = new UserRequestDto();
		userRequestDto.setFullName("Test Admin");
		userRequestDto.setEmail("testadmin@gmail.com");
		userRequestDto.setMobileNumber("9999999999");
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		HttpServletRequest request = null;

		when(userRepository.existsByEmail(userRequestDto.getEmail())).thenReturn(true);
		userService.addUser(userRequestDto, apiResponseDtoBuilder, request);
		assertTrue(apiResponseDtoBuilder.getMessage().equals(Constants.EMAIL_ALREADY_EXISTS));
	}

	@Test
	public void addDriver() {
		UserRequestDto userRequestDto = new UserRequestDto();
		userRequestDto.setFullName("Test Driver");
		userRequestDto.setEmail("testdriver@gmail.com");
		userRequestDto.setMobileNumber("8888888888");
		userRequestDto.setLicenseExpired(new Date());
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		HttpServletRequest request = null;
		Driver user = new Driver();
		user.setFullName("Test Admin");
		user.setEmail("testadmin@gmail.com");
		user.setMobileNumber("9999999999");

		when(customMapper.userRequestDtoToDriver(userRequestDto)).thenReturn(user);
		userService.addDriver(userRequestDto, apiResponseDtoBuilder, request);
		assertTrue(apiResponseDtoBuilder.getMessage().equals(Constants.USER_ADD_SUCCESS));
	}

	@Test
	public void addDriverIfMobileNumberAlreadyExist() {
		UserRequestDto userRequestDto = new UserRequestDto();
		userRequestDto.setFullName("Test Admin");
		userRequestDto.setEmail("testadmin@gmail.com");
		userRequestDto.setMobileNumber("7777777777");
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		HttpServletRequest request = null;

		when(userRepository.existsByMobileNumber(userRequestDto.getMobileNumber())).thenReturn(true);
		userService.addUser(userRequestDto, apiResponseDtoBuilder, request);
		assertTrue(apiResponseDtoBuilder.getMessage().equals(Constants.MOBILE_NUMBER_ALREADY_EXISTS));
	}

	@Test
	public void addDriverIfEmailAlreadyExist() {
		UserRequestDto userRequestDto = new UserRequestDto();
		userRequestDto.setFullName("Test Admin");
		userRequestDto.setEmail("testadmin@gmail.com");
		userRequestDto.setMobileNumber("9999999999");
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		HttpServletRequest request = null;

		when(userRepository.existsByEmail(userRequestDto.getEmail())).thenReturn(true);
		userService.addDriver(userRequestDto, apiResponseDtoBuilder, request);
		assertTrue(apiResponseDtoBuilder.getMessage().equals(Constants.EMAIL_ALREADY_EXISTS));
	}

	@Test
	public void getUserByRole() {
		int role = 1;
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		userService.getUserByRole(role, apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals("success"));
	}

	@Test
	public void updateDriver() {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		Driver driver = new Driver();
		driver.setRole(1);
		driver.setAppId("1");
		driver.setEmail("test@123.com");
		driver.setFullName("test");
		driver.setMobileNumber("88585858585");
		driver.setRewardPoints(4L);
		userService.updateDriver(driver, apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals(Constants.NO_USER_EXISTS));
	}

	@Test
	public void getUserListByFilterWithPagination() {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();

		UserFilterWithPaginationDto UserFilterWithPaginationDto = new UserFilterWithPaginationDto();
		UserFilterDto userFilterDto = new UserFilterDto();
		userFilterDto.setKeyword("test");
		userFilterDto.setRole(1);
		UserFilterWithPaginationDto.setFilter(userFilterDto);

		PaginationDto paginationDto = new PaginationDto();
		paginationDto.setCurrentPage(1);
		paginationDto.setPerPage(5);
		paginationDto.setTotalCount(23);
		paginationDto.setTotalPages(30);

		UserFilterWithPaginationDto.setPagination(paginationDto);
		when(userRepositoryCustom.getUserListByFilterWithPagination(UserFilterWithPaginationDto))
				.thenReturn(paginationDto);
		userService.getUserListByFilterWithPagination(UserFilterWithPaginationDto, apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals(Constants.DATA_LIST));
	}

	@Test
	public void getUserDetailsById() {
		long id = 1L;
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		User user = new User();
		user.setFullName("Test Admin");
		user.setEmail("testadmin@gmail.com");
		user.setMobileNumber("9999999999");
		user.setId(1L);
		Optional<User> userDb = Optional.of(user);
		when(userRepository.findById(id)).thenReturn(userDb);

		List<Cab> cabList = new ArrayList<>();
		when(cabRepository.findByUserId(1L)).thenReturn(cabList);
		userService.getUserDetailsById(id, apiResponseDtoBuilder);
		assertTrue(userDb.isPresent());

	}

	@Test
	public void isActiveUser() {
		long id = 1L;
		boolean active = true;
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		User user = new User();
		user.setFullName("Test Admin");
		user.setEmail("testadmin@gmail.com");
		user.setMobileNumber("9999999999");
		user.setRole(1);
		Optional<User> userDb = Optional.of(user);
		when(userRepository.findById(id)).thenReturn(userDb);
		userService.isActiveUser(id, active, apiResponseDtoBuilder);
		assertTrue(userDb.isPresent());

	}

	@Test
	public void findByMobileNumber() {
		String username = "test";
		User user = new User();
		when(userRepository.findByMobileNumber(username)).thenReturn(user);
		userService.findByMobileNumber(username);
	}

	@Test
	public void findByMobileNumberOrEmail() {
		String mobileNumber = "2222222222";
		String email = "test123@t.com";
		User user = new User();
		when(userRepository.findByMobileNumberOrEmail(mobileNumber, email)).thenReturn(user);
		userService.findByMobileNumberOrEmail(mobileNumber, email);
	}

	@Test
	public void save() {
		User user = new User();
		user.setFullName("Test Admin");
		user.setEmail("testadmin@gmail.com");
		user.setMobileNumber("9999999999");
		userService.save(user);
	}

	@Test
	public void findById() {
		long id = 1L;
		User user = new User();
		user.setFullName("Test Admin");
		user.setEmail("testadmin@gmail.com");
		user.setMobileNumber("9999999999");
		Optional<User> userDb = Optional.of(user);
		when(userRepository.findById(id)).thenReturn(userDb);
		assertTrue(userDb.isPresent());
		userService.findById(id);
	}

	@Test
	public void sendTemporaryPassword() {
		String username = "test";
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		User user = new User();
		when(userRepository.findByEmailOrMobileNumber(username, username)).thenReturn(user);
		assertTrue(user != null);
		userService.sendTemporaryPassword(username, apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals(Constants.SEND_DETAILS_TO_YOUR_EMAIL));
	}

	@Test
	public void deleteUserById() {
		long id = 1L;
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		User user = new User();
		user.setFullName("Test Admin");
		user.setEmail("testadmin@gmail.com");
		user.setMobileNumber("9999999999");
		user.setRole(4);
		Optional<User> userDb = Optional.of(user);
		when(userRepository.findById(id)).thenReturn(userDb);
		assertTrue(userDb.isPresent());
		userService.deleteUserById(id, apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals(Constants.USER_DELETED_SUCCESSFULLY));
	}

	@Test
	public void getSessionUser() {
		User user = new User();
		user.setFullName("Test Admin");
		user.setEmail("testadmin@gmail.com");
		user.setMobileNumber("9999999999");
		when(userRepository.findByEmailOrMobileNumber(anyString(), anyString())).thenReturn(user);
		userService.getSessionUser();
	}

	@Test
	public void referFriendByEmail() {
		User user = new User();
		user.setFullName("Test Admin");
		user.setEmail("testadmin@gmail.com");
		user.setMobileNumber("9999999999");
		userService.getSessionUser();
	}

	@Test
	public void sendRewardToDriver() {
		long rewardPoint = 1L;
		long drivierId = 2L;
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		Driver user = new Driver();
		user.setFullName("Test Admin");
		user.setEmail("testadmin@gmail.com");
		user.setMobileNumber("9999999999");
		user.setRole(5);

		Optional<Driver> userDb = Optional.of(user);
		when(driverRepository.findById(drivierId)).thenReturn(userDb);
		assertTrue(userDb.isPresent());
		userService.sendRewardToDriver(rewardPoint, drivierId, apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals(Constants.DRIVER_NOT_FOUND));
	}

	@Test
	public void isActiveDriver() {
		boolean active = true;
		long id = 2L;
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		User user = new User();
		user.setFullName("Test Admin");
		user.setEmail("testadmin@gmail.com");
		user.setMobileNumber("9999999999");
		Optional<User> userDb = Optional.of(user);
		when(userRepository.findByIdAndRole(id, 3)).thenReturn(userDb);
		assertTrue(userDb.isPresent());
		userService.isActiveDriver(id, active, apiResponseDtoBuilder);
	}

	@Test
	public void isAccountValid() {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		User user = new User();
		user.setFullName("Test Admin");
		user.setEmail("testadmin@gmail.com");
		user.setMobileNumber("9999999999");
		user.setId(1L);
		when(userService.getSessionUser()).thenReturn(user);
		Driver driver = new Driver();
		driver.setRole(1);
		driver.setAppId("1");
		driver.setEmail("test@123.com");
		driver.setFullName("test");
		driver.setMobileNumber("88585858585");
		driver.setRewardPoints(4L);
		Optional<Driver> driverDb = Optional.of(driver);
		when(driverRepository.findById(1L)).thenReturn(driverDb);
		userService.isAccountValid(apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals("success"));
	}
	@Test
	public void updateCustomer() {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		Customer user = new Customer();
		user.setFullName("Test Admin");
		user.setEmail("testadmin@gmail.com");
		user.setMobileNumber("9999999999");
		user.setId(1L);
		userService.updateCustomer(user, apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals(Constants.NO_USER_EXISTS));
	}

	@Test
	public void forgotPassword() {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		String mobile="9999999999";
		String password="test";
		User user = new User();
		user.setFullName("Test Admin");
		user.setEmail("testadmin@gmail.com");
		user.setMobileNumber("9999999999");
		user.setId(1L);
		user.setRole(1);
		when(userRepository.findByMobileNumber("9999999999")).thenReturn(user);
		userService.forgotPassword(apiResponseDtoBuilder, mobile, password);
		assertTrue(apiResponseDtoBuilder.getMessage().equals("Password changed successfully"));
	}	
}
