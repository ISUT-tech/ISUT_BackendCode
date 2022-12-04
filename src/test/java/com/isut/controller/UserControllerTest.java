package com.isut.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import com.isut.IsutAppApplication;
import com.isut.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.isut.dto.ApiResponseDto;
import com.isut.dto.PaginationDto;
import com.isut.dto.UserFilterDto;
import com.isut.dto.UserFilterWithPaginationDto;
import com.isut.dto.UserRequestDto;
import com.isut.model.User;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = IsutAppApplication.class)
public class UserControllerTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private final String URL = "http://localhost:";

	@Test
	public void addAdmin() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		UserRequestDto userRequestDto = new UserRequestDto();
		userRequestDto.setFullName("Test Admin");
		userRequestDto.setEmail("testadmin@gmail.com");
		userRequestDto.setMobileNumber("9999999999");

		String url = URL + port + "/api/admin/add";
		HttpEntity<UserRequestDto> request = new HttpEntity<>(userRequestDto, headers);

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.postForEntity(url, request,
				ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void addUser() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		UserRequestDto userRequestDto = new UserRequestDto();
		userRequestDto.setFullName("Test Customer");
		userRequestDto.setEmail("testcustomer@gmail.com");
		userRequestDto.setMobileNumber("8888888888");
		userRequestDto.setPassword("Test@123");

		String url = URL + port + "/api/customer/add";
		HttpEntity<UserRequestDto> request = new HttpEntity<>(userRequestDto, headers);

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.postForEntity(url, request,
				ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void addDriver() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		UserRequestDto userRequestDto = new UserRequestDto();
		userRequestDto.setFullName("Test Driver");
		userRequestDto.setEmail("testdriver@gmail.com");
		userRequestDto.setMobileNumber("5858585858");
		userRequestDto.setPassword("Test@123");
		userRequestDto.setLicenseExpired(new Date());
		String url = URL + port + "/api/driver/add";
		HttpEntity<UserRequestDto> request = new HttpEntity<>(userRequestDto, headers);

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.postForEntity(url, request,
				ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void updateCustomer() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		User user = new User();
		user.setFullName("Test Customer");
		user.setEmail("testcustomer@gmail.com");
		user.setMobileNumber("8888888888");
		user.setId(1L);
		user.setCreatedAt(new Date());
		user.setUpdatedAt(new Date());
		user.setRole(2);

		String url = URL + port + "/api/customer/update";
		HttpEntity<User> request = new HttpEntity<>(user, headers);

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.postForEntity(url, request,
				ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void updateDriver() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		User user = new User();
		user.setFullName("Test Driver");
		user.setEmail("testdriver@gmail.com");
		user.setMobileNumber("7777777777");
		user.setId(1L);
		user.setCreatedAt(new Date());
		user.setUpdatedAt(new Date());
		user.setRole(3);

		String url = URL + port + "/api/driver/update";
		HttpEntity<User> request = new HttpEntity<>(user, headers);

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.postForEntity(url, request,
				ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void getUserByRole() throws Exception {
		String url = URL + port + "/api/customers/" + 2;

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.getForEntity(url,
				ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void getUserListByFilterWithPagination() throws Exception {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		UserFilterWithPaginationDto userFilterWithPaginationDto = new UserFilterWithPaginationDto();
		UserFilterDto userFilterDto = new UserFilterDto();
		userFilterDto.setRole(2);
		PaginationDto PaginationDto = new PaginationDto();
		PaginationDto.setCurrentPage(1);
		PaginationDto.setPerPage(10);
		userFilterWithPaginationDto.setPagination(PaginationDto);
		userFilterWithPaginationDto.setFilter(userFilterDto);
		String url = URL + port + "/api/customers/pagination/filter";
		HttpEntity<UserFilterWithPaginationDto> request = new HttpEntity<>(userFilterWithPaginationDto, headers);

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.postForEntity(url, request,
				ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void getUserDetailsById() throws Exception {
		String url = URL + port + "/api/customer/" + 2;

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.getForEntity(url,
				ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void isActiveUser() throws Exception {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		long id = 1L;
		boolean active = true;
		String url = URL + port + "/api/customer/active";

		HttpEntity<?> entity = new HttpEntity<>(headers);
		String urlTemplate = UriComponentsBuilder.fromHttpUrl(url).queryParam("id", "{id}")
				.queryParam("active", "{active}").encode().toUriString();
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		params.put("active", active);

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.exchange(urlTemplate, HttpMethod.POST,
				entity, ApiResponseDtoBuilder.class, params);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void deleteUserById() throws Exception {
		String url = URL + port + "/api/customer/" + 2 + "/delete";

		String urlTemplate = UriComponentsBuilder.fromHttpUrl(url).encode().toUriString();

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.exchange(urlTemplate, HttpMethod.DELETE,
				null, ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void referFriendByEmail() throws Exception {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String email = "test123@test.com";
		String url = URL + port + "/api/customer/refer";

		HttpEntity<?> entity = new HttpEntity<>(headers);
		String urlTemplate = UriComponentsBuilder.fromHttpUrl(url).queryParam("email", "{email}").encode()
				.toUriString();
		Map<String, Object> params = new HashMap<>();
		params.put("email", email);
		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.exchange(urlTemplate, HttpMethod.POST,
				entity, ApiResponseDtoBuilder.class, params);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void sendRewardToDriver() throws Exception {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		long driverId = 1L;
		long rewardPoint = 1L;
		String url = URL + port + "/api/customer/reward";

		HttpEntity<?> entity = new HttpEntity<>(headers);
		String urlTemplate = UriComponentsBuilder.fromHttpUrl(url).queryParam("rewardPoint", "{rewardPoint}")
				.queryParam("driverId", "{driverId}").encode().toUriString();
		Map<String, Object> params = new HashMap<>();
		params.put("rewardPoint", rewardPoint);
		params.put("driverId", driverId);
		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.exchange(urlTemplate, HttpMethod.POST,
				entity, ApiResponseDtoBuilder.class, params);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void isActiveDriver() throws Exception {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		long id = 1L;
		boolean active = true;
		String url = URL + port + "/api/driver/active";

		HttpEntity<?> entity = new HttpEntity<>(headers);
		String urlTemplate = UriComponentsBuilder.fromHttpUrl(url).queryParam("id", "{id}")
				.queryParam("active", "{active}").encode().toUriString();
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		params.put("active", active);

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.exchange(urlTemplate, HttpMethod.POST,
				entity, ApiResponseDtoBuilder.class, params);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}

	@Test
	public void forgotPassword() throws Exception {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String mobile="test";
		String password="test";
		String url = URL + port + "/api/public/user/forgot/password";

		HttpEntity<?> entity = new HttpEntity<>(headers);
		String urlTemplate = UriComponentsBuilder.fromHttpUrl(url).queryParam("mobile", "{mobile}")
				.queryParam("password", "{password}").encode().toUriString();
		Map<String, Object> params = new HashMap<>();
		params.put("mobile", mobile);
		params.put("password", password);

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.exchange(urlTemplate, HttpMethod.POST,
				entity, ApiResponseDtoBuilder.class, params);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}
}