package com.isut.service.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.isut.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.isut.dto.GraphYearDto;
import com.isut.dto.ShowGraphDto;
import com.isut.mapper.CustomMapper;
import com.isut.repository.BookingRepository;
import com.isut.repository.PromoCodeRepository;
import com.isut.repository.UserRepository;
import com.isut.repository.custom.BookingRepositoryCustom;

@ExtendWith(MockitoExtension.class)
public class GraphServiceImplTest {
	@InjectMocks
	GraphServiceImpl graphServiceImpl;
	@Mock
	UserRepository userRepository;
	@Mock
	BookingRepository bookingRepository;
	@Mock
	PromoCodeRepository promoCodeRepository;
	@Mock
	BookingRepositoryCustom bookingRepositoryCstm;
	@Mock
	CustomMapper customMapper;

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void getAllDetailsCount() {
		ApiResponseDtoBuilder apiResponseDtoBuilder = new ApiResponseDtoBuilder();

		ShowGraphDto showGraphDto = new ShowGraphDto();
		showGraphDto.setTotalUser(20);
		showGraphDto.setTotalDriver(40);
		showGraphDto.setTotalBooking(60);
		List<GraphYearDto> setGraphYearDto = new ArrayList<>();
		GraphYearDto graphYearDto = new GraphYearDto();
		graphYearDto.setMonth(1);
		graphYearDto.setCount(2);
		setGraphYearDto.add(graphYearDto);
		when(userRepository.countByRole(2)).thenReturn(20L);
		when(userRepository.countByRole(3)).thenReturn(40L);
		when(bookingRepository.count()).thenReturn(60L);

		graphServiceImpl.getAllDetailsCount(apiResponseDtoBuilder);
		assertTrue(apiResponseDtoBuilder.getMessage().equals("graph details fetch successfully"));
	}

}
