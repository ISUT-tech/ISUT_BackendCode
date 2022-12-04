package com.isut.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
import com.isut.dto.TipRequestDto;
import com.isut.repository.BookingRepository;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = IsutAppApplication.class)
public class TipControllerTest {

	@LocalServerPort
	private int port;
	@Mock
	BookingRepository bookingRepository;
	@Autowired
	private TestRestTemplate restTemplate;

	private final String URL = "http://localhost:";

	@Test
	public void addTip() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		TipRequestDto tipRequestDto = new TipRequestDto();
		tipRequestDto.setBookingId(7L);
		tipRequestDto.setDriverId(5L);
		tipRequestDto.setTip(3);
		tipRequestDto.setUserId(3L);

		String url = URL + port + "/api/tip/add";
		HttpEntity<TipRequestDto> request = new HttpEntity<>(tipRequestDto, headers);

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.postForEntity(url, request,
				ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void getTipListByDriverId() throws Exception {
		long driverId = 1l;
		String url = URL + port + "/api/driver/" + driverId + "/tips";

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.getForEntity(url,
				ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void getTipListByUserId() throws Exception {
		long id = 1l;
		String url = URL + port + "/api/customer/" + id + "/tips";

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.getForEntity(url,
				ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}
}
