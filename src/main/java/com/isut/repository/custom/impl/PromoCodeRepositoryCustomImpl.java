package com.isut.repository.custom.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.isut.constants.Constants;
import com.isut.dto.PaginationDataDto;
import com.isut.dto.PaginationDto;
import com.isut.dto.PromoCodeFilterWithPaginationDto;
import com.isut.model.PromoCode;
import com.isut.repository.custom.PromoCodeRepositoryCustom;
import com.isut.utility.Utility;

@Repository
public class PromoCodeRepositoryCustomImpl implements PromoCodeRepositoryCustom {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public PaginationDto getPromoCodeListByFilterWithPagination(PromoCodeFilterWithPaginationDto filterWithPagination) {
		String countQuery = "SELECT count(*) from " + Constants.PROMO_CODE_TABLE_NAME + " t";
		String query = "SELECT t.* from " + Constants.PROMO_CODE_TABLE_NAME + " t";
		String addableQuery = "";
		boolean flag = false;
		boolean whereFlag = true;
		if (filterWithPagination.getFilter().getKeyword() != null
				&& !filterWithPagination.getFilter().getKeyword().isEmpty()) {
			addableQuery += Utility.addWhere(whereFlag) + Utility.addANDOrOR(flag) + " t.code like '%";
			flag = true;
			whereFlag = false;
		}

		Query queryString = entityManager.createNativeQuery(countQuery + addableQuery);
		int totalCounts = ((Number) queryString.getSingleResult()).intValue();
		PaginationDataDto paginationDataDto = Utility.getPaginationData(totalCounts,
				filterWithPagination.getPagination());
		String limitQuery = " order by t.id desc limit " + paginationDataDto.getFrom() + ","
				+ paginationDataDto.getTo();
		queryString = entityManager.createNativeQuery(query + addableQuery + limitQuery, PromoCode.class);
		List<PromoCode> dataList = queryString.getResultList();
		filterWithPagination.getPagination().setData(dataList);
		filterWithPagination.getPagination().setTotalCount(totalCounts);
		filterWithPagination.getPagination().setTotalPages(paginationDataDto.getTotalPages());
		return filterWithPagination.getPagination();
	}
}
