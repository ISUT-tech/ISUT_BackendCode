package com.isut.repostory.custom.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.isut.dto.PaginationDataDto;
import com.isut.dto.PaginationDto;
import com.isut.dto.UserFilterWithPaginationDto;
import com.isut.dto.UserListResponseDto;
import com.isut.mapper.CustomMapper;
import com.isut.model.User;
import com.isut.repostory.custom.UserRepositoryCustom;
import com.isut.utility.Utility;

@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	private CustomMapper customMapper;

	@Override
	public PaginationDto getUserListByFilterWithPagination(UserFilterWithPaginationDto filterWithPagination) {
		String countQuery = "SELECT count(*) from user_details t";
		String query = "SELECT t.* from user_details t";
		String addableQuery = " where t.role = " + filterWithPagination.getFilter().getRole();
		boolean flag = true;
		boolean whereFlag = false;
		if (filterWithPagination.getFilter().getKeyword() != null
				&& !filterWithPagination.getFilter().getKeyword().isEmpty()) {
			addableQuery += Utility.addWhere(whereFlag) + Utility.addANDOrOR(flag) + " t.full_name like '%"
					+ filterWithPagination.getFilter().getKeyword() + "%' or t.mobile_number like '%"
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
		queryString = entityManager.createNativeQuery(query + addableQuery + limitQuery, User.class);
		List<User> userList = queryString.getResultList();
		List<UserListResponseDto> dataList = customMapper.userListToUserListResponseDtoList(userList);
		filterWithPagination.getPagination().setData(dataList);
		filterWithPagination.getPagination().setTotalCount(totalCounts);
		filterWithPagination.getPagination().setTotalPages(paginationDataDto.getTotalPages());
		return filterWithPagination.getPagination();
	}
}
