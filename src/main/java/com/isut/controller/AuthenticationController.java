package com.isut.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.isut.config.JwtTokenUtil;
import com.isut.constants.AuthorizationConstants;
import com.isut.constants.Constants;
import com.isut.dto.ApiResponseDto;
import com.isut.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.isut.dto.LoginUser;
import com.isut.model.Cab;
import com.isut.model.Customer;
import com.isut.model.Driver;
import com.isut.model.User;
import com.isut.repository.CabRepository;
import com.isut.service.IUserService;

@CrossOrigin(origins = "*", maxAge = 360000000)
@RestController
@RequestMapping(Constants.API_BASE_URL + "/auth")
public class AuthenticationController {

	private static final String TOKEN = "token";

	private static final String USER = "user";

	private static final String LOGINEDUSER = "user_details";

	private static final String ISCABREGISTERED = "isCabRegistered";

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private IUserService userService;

	@Autowired
	private CabRepository cabRepository;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ApiResponseDto userlogin(@RequestBody LoginUser loginUser) throws AuthenticationException {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		User checkUser = userService.findByMobileNumberOrEmail(loginUser.getUsername(), loginUser.getUsername());
		if (checkUser == null) {
			apiResponseDtoBuilder.withStatus(HttpStatus.UNAUTHORIZED).withMessage(Constants.NO_Mobile_EXISTS);
			return apiResponseDtoBuilder.build();
		}
		if (checkUser.getRole() == 2) {
			Customer customer = userService.findCustomerById(checkUser.getId());
			customer.setAppId(loginUser.getAppId());
			userService.saveCustomer(customer);
		} else if (checkUser.getRole() == 3) {
			Driver driver = userService.findDriverById(checkUser.getId());
			driver.setAppId(loginUser.getAppId());
			userService.saveDriver(driver);
		}
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));
		final UserDetails user = userDetailsService.loadUserByUsername(loginUser.getUsername());
		final String token = jwtTokenUtil.generateToken(user);
		List<Cab> cab = cabRepository.findByUserId(checkUser.getId());
		boolean isCabRegistered;
		if (cab.size() == 0) {
			isCabRegistered = false;
		} else {
			isCabRegistered = true;
		}
		Map<String, Object> response = setTokenDetails(user, token, checkUser, isCabRegistered);
		apiResponseDtoBuilder.withStatus(HttpStatus.OK).withMessage(AuthorizationConstants.LOGIN_SUCESSFULL)
				.withData(response);
		return apiResponseDtoBuilder.build();
	}

	@RequestMapping(value = "/admin/login", method = RequestMethod.POST)
	public ApiResponseDto adminlogin(@RequestBody LoginUser loginUser) throws AuthenticationException {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();
		User checkUser = userService.findByMobileNumberOrEmail(loginUser.getUsername(), loginUser.getUsername());
		if (checkUser == null) {
			apiResponseDtoBuilder.withStatus(HttpStatus.UNAUTHORIZED).withMessage(Constants.INVALID_USERNAME);
			return apiResponseDtoBuilder.build();
		}
		if (checkUser.getRole() != 0 && checkUser.getRole() != 1) {
			apiResponseDtoBuilder.withStatus(HttpStatus.UNAUTHORIZED).withMessage(Constants.UNAUTHORIZED);
			return apiResponseDtoBuilder.build();
		}
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));
		final UserDetails user = userDetailsService.loadUserByUsername(loginUser.getUsername());
		final String token = jwtTokenUtil.generateToken(user);
		Map<String, Object> response = setTokenDetails(user, token, checkUser, false);
		apiResponseDtoBuilder.withStatus(HttpStatus.OK).withMessage(AuthorizationConstants.LOGIN_SUCESSFULL)
				.withData(response);
		return apiResponseDtoBuilder.build();
	}

	private Map<String, Object> setTokenDetails(final UserDetails user, final String token, final User userDetails,
			boolean isCabResgistered) {
		Map<String, Object> response = new HashMap<>();
		response.put(USER, user);
		response.put(TOKEN, token);
		response.put(LOGINEDUSER, userDetails);
		response.put(ISCABREGISTERED, isCabResgistered);
		return response;
	}
}
