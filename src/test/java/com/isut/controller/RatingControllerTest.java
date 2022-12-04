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
import com.isut.dto.RatingRequestDto;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = IsutAppApplication.class)
public class RatingControllerTest {
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private final String URL = "http://localhost:";

	@Test
	public void addRating() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RatingRequestDto ratingRequestDto = new RatingRequestDto();
		ratingRequestDto.setBookingId(1L);
		ratingRequestDto.setDriverId(2L);
		ratingRequestDto.setRating(4);
		ratingRequestDto.setUserId(1L);
		String url = URL + port + "/api/rating/add";
		HttpEntity<RatingRequestDto> request = new HttpEntity<>(ratingRequestDto, headers);

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.postForEntity(url, request,
				ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void getRatingListByDriverId() throws Exception {
		long driverId = 1l;
		String url = URL + port + "/api/driver/" + driverId + "/ratings";

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.getForEntity(url,
				ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void getRatingListByUserId() throws Exception {
		long id = 1l;
		String url = URL + port + "/api/customer/" + id + "/ratings";

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.getForEntity(url,
				ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}
}
