package com.isut.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

import com.isut.IsutAppApplication;
import com.isut.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.isut.dto.ApiResponseDto;
import com.isut.dto.FeedbackRequestDto;
import com.isut.dto.RateUsRequestDto;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = IsutAppApplication.class)
public class FeedbackControllerTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private final String URL = "http://localhost:";

	@Test
	public void addFeedback() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		FeedbackRequestDto feedbackRequestDto = new FeedbackRequestDto();
		feedbackRequestDto.setBookingId(1L);
		feedbackRequestDto.setDriverId(2L);
		feedbackRequestDto.setFeedback("test");
		feedbackRequestDto.setUserId(3L);

		String url = URL + port + "/api/feedback/add";
		HttpEntity<FeedbackRequestDto> request = new HttpEntity<>(feedbackRequestDto, headers);

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.postForEntity(url, request,
				ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void getFeedbackListByDriverId() throws Exception {
		long driverId = 1L;
		String url = URL + port + "/api/driver/" + driverId + "/feedbacks";

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.getForEntity(url,
				ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void getFeedbackListByUserId() throws Exception {
		long id = 1L;
		String url = URL + port + "/api/customer/" + id + "/feedbacks";

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.getForEntity(url,
				ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void sendFeedBackResponseByUserId() throws Exception {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		long id = 1L;
		String response = "Testing Ok";
		String url = URL + port + "/api/customer/" + id + "/feedback";

		HttpEntity<?> entity = new HttpEntity<>(headers);
		String urlTemplate = UriComponentsBuilder.fromHttpUrl(url).queryParam("response", "{response}").encode()
				.toUriString();
		Map<String, Object> params = new HashMap<>();
		params.put("response", response);
		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.exchange(urlTemplate, HttpMethod.POST,
				entity, ApiResponseDtoBuilder.class, params);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void addRateUs() throws Exception {HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);

	RateUsRequestDto rateUsRequestDto = new RateUsRequestDto();
	rateUsRequestDto.setFeedback("test");
	rateUsRequestDto.setUserId(3L);

	String url = URL + port + "/api/rateUs/add";
	HttpEntity<RateUsRequestDto> request = new HttpEntity<>(rateUsRequestDto, headers);

	ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.postForEntity(url, request,
			ApiResponseDtoBuilder.class);

	assertEquals(HttpStatus.OK, responseEntity.getStatusCode());}
}
