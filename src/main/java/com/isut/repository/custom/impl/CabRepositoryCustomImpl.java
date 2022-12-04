package com.isut.repostory.custom.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.isut.dto.CabFilterWithPaginationDto;
import com.isut.dto.PaginationDataDto;
import com.isut.dto.PaginationDto;
import com.isut.model.Cab;
import com.isut.repostory.custom.CabRepositoryCustom;
import com.isut.utility.Utility;

@Repository
public class CabRepositoryCustomImpl implements CabRepositoryCustom {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public PaginationDto getCabListByFilterWithPagination(CabFilterWithPaginationDto filterWithPagination) {
		String countQuery = "SELECT count(*) from cab_details t";
		String query = "SELECT t.* from cab_details t";
		String addableQuery = "";
		boolean flag = false;
		boolean whereFlag = true;
		if (filterWithPagination.getFilter().getKeyword() != null
				&& !filterWithPagination.getFilter().getKeyword().isEmpty()) {
			addableQuery += Utility.addWhere(whereFlag) + Utility.addANDOrOR(flag) + " t.car_model like '%"
					+ filterWithPagination.getFilter().getKeyword() + "%' or t.license_number like '%"
					+ filterWithPagination.getFilter().getKeyword() + "%' or t.car_name like '%"
					+ filterWithPagination.getFilter().getKeyword() + "%' or t.car_number like '%"
					+ filterWithPagination.getFilter().getKeyword() + "%'";
			flag = true;
			whereFlag = false;
		}

		Query queryString = entityManager.createNativeQuery(countQuery + addableQuery);
		int totalCounts = ((Number) queryString.getSingleResult()).intValue();
		PaginationDataDto paginationDataDto = Utility.getPaginationData(totalCounts,
				filterWithPagination.getPagination());
		String limitQuery = " order by t.id desc limit " + paginationDataDto.getFrom() + ","
				+ paginationDataDto.getTo();
		queryString = entityManager.createNativeQuery(query + addableQuery + limitQuery, Cab.class);
		List<Cab> dataList = queryString.getResultList();
		filterWithPagination.getPagination().setData(dataList);
		filterWithPagination.getPagination().setTotalCount(totalCounts);
		filterWithPagination.getPagination().setTotalPages(paginationDataDto.getTotalPages());
		return filterWithPagination.getPagination();
	}
}
