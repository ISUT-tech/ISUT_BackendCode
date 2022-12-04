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
import org.springframework.web.util.UriComponentsBuilder;

import com.isut.IsutAppApplication;
import com.isut.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.isut.dto.BookingFilterDto;
import com.isut.dto.BookingFilterWithPaginationDto;
import com.isut.dto.BookingRequestDto;
import com.isut.dto.PaginationDto;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = IsutAppApplication.class)
public class BookingControllerTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private final String URL = "http://localhost:";

	@Test
	public void addBooking() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		BookingRequestDto bookingRequestDto = new BookingRequestDto();
		bookingRequestDto.setDestinationLocation("sikar");
		bookingRequestDto.setDriverId(1L);
		bookingRequestDto.setPromoCode("test123");
		bookingRequestDto.setUserId(1l);
		bookingRequestDto.setUserMobileNumber("12345678");
		String url = URL + port + "/api/booking/add";
		HttpEntity<BookingRequestDto> request = new HttpEntity<>(bookingRequestDto, headers);

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.postForEntity(url, request,
				ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void updateBooking() throws Exception {
		String url = URL + port + "/api/booking/" + 1 + "/updateStatus/" + 1;

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.postForEntity(url, null,
				ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void getBookingListByDriverId() throws Exception {
		String url = URL + port + "/api/driver/" + 1 + "/bookings";

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.getForEntity(url,
				ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void getBookingListByUserId() throws Exception {
		String url = URL + port + "/api/customer/" + 1 + "/bookings";

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.getForEntity(url,
				ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}

	@Test
	public void getBookingListByFilterWithPagination() throws Exception {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		BookingFilterWithPaginationDto bookingFilterWithPaginationDto = new BookingFilterWithPaginationDto();
		BookingFilterDto bookingFilterDto = new BookingFilterDto();
		bookingFilterDto.setUserId(1);
		PaginationDto paginationDto = new PaginationDto();
		paginationDto.setCurrentPage(1);
		paginationDto.setPerPage(2);
		paginationDto.setTotalCount(6);
		paginationDto.setTotalPages(10);
		bookingFilterWithPaginationDto.setPagination(paginationDto);
		bookingFilterWithPaginationDto.setFilter(bookingFilterDto);

		String url = URL + port + "/api/bookings/pagination/filter";
		HttpEntity<BookingFilterWithPaginationDto> request = new HttpEntity<>(bookingFilterWithPaginationDto, headers);

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.postForEntity(url, request,
				ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}

	@Test
	public void updateDestinationLocation() throws Exception {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		long bookingId = 1L;
		String location = "test";
		String url = URL + port + "/api/booking/update/desinationlocation";

		HttpEntity<?> entity = new HttpEntity<>(headers);
		String urlTemplate = UriComponentsBuilder.fromHttpUrl(url).queryParam("bookingId", "{bookingId}")
				.queryParam("location", "{location}").encode().toUriString();
		Map<String, Object> params = new HashMap<>();
		params.put("bookingId", bookingId);
		params.put("location", location);

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.exchange(urlTemplate, HttpMethod.POST,
				entity, ApiResponseDtoBuilder.class, params);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void bookingSchedule() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		BookingRequestDto bookingRequestDto = new BookingRequestDto();
		bookingRequestDto.setUserMobileNumber("9999999999");
		bookingRequestDto.setUserId(123L);
		bookingRequestDto.setDriverId(321L);
		Date scheduleDate = new Date();
		bookingRequestDto.setScheduleDate(scheduleDate);
		String url = URL + port + "/api/booking/schedule";
		HttpEntity<BookingRequestDto> request = new HttpEntity<>(bookingRequestDto, headers);

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.postForEntity(url, request,
				ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	}

	@Test
	public void findBookingById() throws Exception {
		String url = URL + port + "/api/booking/" + 1L;

		ResponseEntity<ApiResponseDtoBuilder> responseEntity = restTemplate.postForEntity(url, null,
				ApiResponseDtoBuilder.class);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}

}
