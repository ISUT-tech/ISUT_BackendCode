package com.isut.service;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.isut.dto.AdminRequestDto;
import com.isut.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.isut.dto.UserFilterWithPaginationDto;
import com.isut.dto.UserRequestDto;
import com.isut.model.Admin;
import com.isut.model.Customer;
import com.isut.model.Driver;
import com.isut.model.User;

@Service
public interface IUserService {

	void addUser(UserRequestDto userRequestDto, ApiResponseDtoBuilder apiResponseDtoBuilder,
			HttpServletRequest request);

	void addDriver(@Valid UserRequestDto userRequestDto, ApiResponseDtoBuilder apiResponseDtoBuilder,
			HttpServletRequest request);

	Driver findDriverById(Long driverId);

	void getUserByRole(int role, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void getUserListByFilterWithPagination(UserFilterWithPaginationDto filterWithPagination,
			ApiResponseDtoBuilder apiResponseDtoBuilder);

	void getUserDetailsById(long id, ApiResponseDtoBuilder apiResponseDtoBuilder);

	User findByMobileNumber(String username);

	User findByMobileNumberOrEmail(String username, String username2);

	void save(User checkUser);

	void isActiveUser(long id, boolean active, ApiResponseDtoBuilder apiResponseDtoBuilder);

	User findById(Long id);

	void sendTemporaryPassword(String username, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void deleteUserById(long id, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void addAdmin(@Valid AdminRequestDto adminRequestDto, ApiResponseDtoBuilder apiResponseDtoBuilder,
			HttpServletRequest request);

	void referFriendByEmail(String email, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void updateCustomer(@Valid Customer user, ApiResponseDtoBuilder apiResponseDtoBuilder);

	User getSessionUser();

	void updateDriver(@Valid Driver user, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void saveDriver(Driver customer);

	Customer findCustomerById(Long id);

	void saveCustomer(Customer customer);

	void sendRewardToDriver(long rewardPoint, long driverId, ApiResponseDtoBuilder apiResponseDtoBuilder);

	void isActiveDriver(long id, boolean active, ApiResponseDtoBuilder apiResponseDtoBuilder);

	ApiResponseDtoBuilder isAccountValid(ApiResponseDtoBuilder apiResponseDtoBuilder);

	Admin findAdminByMobileNumberOrEmail(String username, String username2);

	Admin findAdminByEmail(String username);

	User findByMobileNumberOrEmailAndPassword(String username, String username2, String password);

	void forgotPassword(ApiResponseDtoBuilder apiResponseDtoBuilder, String mobile, String password);

}
