package com.isut.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.isut.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.isut.dto.GraphYearDto;
import com.isut.dto.ShowGraphDto;
import com.isut.repository.BookingRepository;
import com.isut.repository.UserRepository;
import com.isut.repository.custom.BookingRepositoryCustom;
import com.isut.service.IGraphService;
import com.isut.utility.Utility;

@Service
public class GraphServiceImpl implements IGraphService {

	@Autowired
	private BookingRepository bookingRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BookingRepositoryCustom bookingRepositoryCstm;

	@Override
	public void getAllDetailsCount(ApiResponseDtoBuilder apiResponseDtoBuilder) {

		ShowGraphDto showGraphDto = new ShowGraphDto();
		showGraphDto.setTotalUser(userRepository.countByRole(2));
		showGraphDto.setTotalDriver(userRepository.countByRole(3));
		showGraphDto.setTotalBooking(bookingRepository.count());
		List<GraphYearDto> setGraphYearDto = setGraphYearDto();
		showGraphDto.setGraphYearDto(setGraphYearDto);
		apiResponseDtoBuilder.withMessage("graph details fetch successfully").withStatus(HttpStatus.OK)
				.withData(showGraphDto);
	}

	private List<GraphYearDto> setGraphYearDto() {
		Calendar cal = Calendar.getInstance();
		int min = cal.getActualMinimum(Calendar.DATE);
		int res = cal.getActualMaximum(Calendar.DATE);
		List<GraphYearDto> dataList = new ArrayList<>();
		for (int i = 0; i < 12; i++) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, -i);
			min = calendar.getActualMinimum(Calendar.DATE);
			res = calendar.getActualMaximum(Calendar.DATE);
			getBookingCount(min, res, dataList, calendar.getTime());
		}
		return dataList;
	}

	private void getBookingCount(int min, int res, List<GraphYearDto> listRevenueDto, Date date) {
		Date sDate = Utility.getFormatedDateFromDate(getDateFromCalendar(min, date), 00, 00);
		Date eDate = Utility.getFormatedDateFromDate(getDateFromCalendar(res, date), 23, 59);
		int billingDetails = bookingRepositoryCstm.getBookingsBetweenDate(sDate, eDate);
		GraphYearDto graphDto = new GraphYearDto();
		graphDto.setCount(billingDetails);
		graphDto.setMonth(date.getMonth() + 1);
		listRevenueDto.add(graphDto);
	}

	private Date getDateFromCalendar(int min, Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(date.getYear() + 1900, date.getMonth(), min);
		return calendar.getTime();
	}

	public static Date getFormatedDateFromDate(Date date, int hourOfDay, int minute) {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.AM_PM, 0);
			calendar.set(Calendar.HOUR, hourOfDay);
			calendar.set(Calendar.MINUTE, minute);
			calendar.set(Calendar.SECOND, 0);
			return calendar.getTime();

		} catch (Exception e) {
			return null;
		}
	}
}
