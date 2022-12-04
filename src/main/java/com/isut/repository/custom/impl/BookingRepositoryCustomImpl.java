package com.isut.repostory.custom.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.isut.dto.BookingFilterWithPaginationDto;
import com.isut.dto.PaginationDataDto;
import com.isut.dto.PaginationDto;
import com.isut.model.Booking;
import com.isut.repostory.custom.BookingRepositoryCustom;
import com.isut.utility.Utility;

@Repository
public class BookingRepositoryCustomImpl implements BookingRepositoryCustom {

	@PersistenceContext
	EntityManager entityManager;

	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public PaginationDto getBookingListByFilterWithPagination(BookingFilterWithPaginationDto filterWithPagination) {
		String countQuery = "SELECT count(*) from booking_details t";
		String query = "SELECT t.* from booking_details t";
		String addableQuery = "";
		boolean flag = false;
		boolean whereFlag = true;
		if (filterWithPagination.getFilter().getUserId() != 0) {
			addableQuery += Utility.addWhere(whereFlag) + Utility.addANDOrOR(flag) + " t.user_id = "
					+ filterWithPagination.getFilter().getUserId();
			flag = true;
			whereFlag = false;
		}

		if (filterWithPagination.getFilter().getDriverId() != 0) {
			addableQuery += Utility.addWhere(whereFlag) + Utility.addANDOrOR(flag) + " t.driver_id = "
					+ filterWithPagination.getFilter().getDriverId();
			flag = true;
			whereFlag = false;
		}

		Query queryString = entityManager.createNativeQuery(countQuery + addableQuery);
		int totalCounts = ((Number) queryString.getSingleResult()).intValue();
		PaginationDataDto paginationDataDto = Utility.getPaginationData(totalCounts,
				filterWithPagination.getPagination());
		String limitQuery = " order by t.id desc limit " + paginationDataDto.getFrom() + ","
				+ paginationDataDto.getTo();
		queryString = entityManager.createNativeQuery(query + addableQuery + limitQuery, Booking.class);
		List<Booking> dataList = queryString.getResultList();
		filterWithPagination.getPagination().setData(dataList);
		filterWithPagination.getPagination().setTotalCount(totalCounts);
		filterWithPagination.getPagination().setTotalPages(paginationDataDto.getTotalPages());
		return filterWithPagination.getPagination();
	}

	@Override
	public int getBookingsBetweenDate(Date sDate, Date eDate) {
		String countQuery = "SELECT count(*) from booking_details t where";
		countQuery += " t.created_at between '" + formatter.format(sDate) + " 00:00:00' and '" + formatter.format(eDate)
				+ " 23:59:59'";
		Query queryString = entityManager.createNativeQuery(countQuery);
		int totalCounts = ((Number) queryString.getSingleResult()).intValue();
		return totalCounts;
	}

}
