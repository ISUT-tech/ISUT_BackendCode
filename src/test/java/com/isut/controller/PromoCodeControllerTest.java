package com.isut.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

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
import org.springframework.web.util.UriComponentsBuilder;

import com.isut.IsutAppApplication;
import com.isut.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.isut.dto.PaginationDto;
import com.isut.dto.PromoCodeFilterDto;
import com.isut.dto.PromoCodeFilterWithPaginationDto;
import com.isut.dto.PromoCodeRequestDto;
import com.isut.model.PromoCode;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = IsutAppApplication.class)
public class PromoCodeControllerTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private final String URL = "http://localhost:";

	@Test
	public void addPromoCode() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		PromoCodeRequestDto promoCodeRequestDto = new PromoCodeRequestDto();
		promoCodeRequestDto.setCode("test");
		promoCodeRequestDto.setDescription("test123");
		promoCodeRequestDto.setDiscount(5);
		promoCodeRequestDto.setPromoCode("123test1234");
		promoCodeRequestDto.setStatus(true);

		String url = URL + port + "/api/promoCode/add";
		HttpEntity<PromoCodeRequestDto> request = new HttpEntity<>(promoCodeRequestDto, headers);

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.postForEntity(url, request,
				ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void updatePromoCode() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		PromoCode promoCode = new PromoCode();
		promoCode.setCode("test");
		promoCode.setDescription("test123");
		promoCode.setDiscount(5);
		promoCode.setStatus(true);

		String url = URL + port + "/api/promoCode/add";
		HttpEntity<PromoCode> request = new HttpEntity<>(promoCode, headers);

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.postForEntity(url, request,
				ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void getPromoCodeById() throws Exception {
		long id = 1L;
		String url = URL + port + "/api/promoCode/" + id;

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.getForEntity(url,
				ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void getAllPromoCode() throws Exception {
		long id = 1L;
		String url = URL + port + "/api/promoCodes";

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.getForEntity(url,
				ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void getPromoCodeListByFilterWithPagination() throws Exception {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		PromoCodeFilterWithPaginationDto promoCodeFilterWithPaginationDto = new PromoCodeFilterWithPaginationDto();
		PromoCodeFilterDto promoCodeFilterDto = new PromoCodeFilterDto();
		PaginationDto PaginationDto = new PaginationDto();
		PaginationDto.setCurrentPage(1);
		PaginationDto.setPerPage(2);
		promoCodeFilterWithPaginationDto.setPagination(PaginationDto);
		promoCodeFilterWithPaginationDto.setFilter(promoCodeFilterDto);

		String url = URL + port + "/api/promocodes/pagination/filter";
		HttpEntity<PromoCodeFilterWithPaginationDto> request = new HttpEntity<>(promoCodeFilterWithPaginationDto,
				headers);

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.postForEntity(url, request,
				ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void isPromocodeValid() throws Exception {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String promoCode = "test123";

		String url = URL + port + "/api/promoCode/isValid";

		HttpEntity<?> entity = new HttpEntity<>(headers);
		String urlTemplate = UriComponentsBuilder.fromHttpUrl(url).queryParam("promoCode", "{promoCode}").encode()
				.toUriString();
		Map<String, Object> params = new HashMap<>();
		params.put("promoCode", promoCode);

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.exchange(urlTemplate, HttpMethod.POST,
				entity, ApiResponseDtoBuilder.class, params);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

}
