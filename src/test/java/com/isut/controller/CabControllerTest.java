package com.isut.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.isut.IsutAppApplication;
import com.isut.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.isut.dto.CabFilterDto;
import com.isut.dto.CabFilterWithPaginationDto;
import com.isut.dto.CabRequestDto;
import com.isut.dto.PaginationDto;
import com.isut.model.Cab;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = IsutAppApplication.class)
public class CabControllerTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private final String URL = "http://localhost:";

	@Test
	public void updateCabDetails() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		Cab cab = new Cab();
		cab.setCarModel("test");
		cab.setCarImages("test");
		cab.setCarModel("test");
		cab.setCarName("bmw");
		cab.setCarNumber("543565test");
		cab.setCity("test");
		cab.setState("US");
		cab.setState("test");
		cab.setId(1L);

		String url = URL + port + "/api/cab/update";
		HttpEntity<Cab> request = new HttpEntity<>(cab, headers);

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.postForEntity(url, request,
				ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void getCabDetailsByUserId() throws Exception {
		String url = URL + port + "/api/customer/" + 1L + "/cabs";

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.getForEntity(url,
				ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void getCabDetailsById() throws Exception {
		String url = URL + port + "/api/cab/" + 1L + "/cabs";

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.getForEntity(url,
				ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void getAllCabDetails() throws Exception {
		String url = URL + port + "/api/cabs";

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.getForEntity(url,
				ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void getCabListByFilterWithPagination() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		CabFilterDto CabFilterDto = new CabFilterDto();
		CabFilterWithPaginationDto cabFilterWithPaginationDto = new CabFilterWithPaginationDto();
		PaginationDto paginationDto = new PaginationDto();
		paginationDto.setCurrentPage(1);
		paginationDto.setPerPage(2);
		cabFilterWithPaginationDto.setPagination(paginationDto);
		cabFilterWithPaginationDto.setFilter(CabFilterDto);
		String url = URL + port + "/api/cabs/pagination/filter";
		HttpEntity<CabFilterWithPaginationDto> request = new HttpEntity<>(cabFilterWithPaginationDto, headers);

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.postForEntity(url, request,
				ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void isActiveCab() throws Exception {
		String url = URL + port + "/api/cab/" + 1L + "/" + true;

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.postForEntity(url, null,
				ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void changeStatus() throws Exception {
		String url = URL + port + "/api/cab/" + 1L + "/status/" + 0;

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.postForEntity(url, null,
				ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void selectCarByName() throws Exception {
		String cabName = "test";
		String url = URL + port + "/api/cab/" + cabName;

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.postForEntity(url, null,
				ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void selectCarByType() throws Exception {
		int carType = 01;
		String url = URL + port + "/api/cab/" + carType;

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.getForEntity(url,
				ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}
}
