package com.isut.repository.custom;

import com.isut.dto.PaginationDto;
import com.isut.dto.UserFilterWithPaginationDto;

public interface UserRepositoryCustom {
	PaginationDto getUserListByFilterWithPagination(UserFilterWithPaginationDto filterWithPagination);
}
