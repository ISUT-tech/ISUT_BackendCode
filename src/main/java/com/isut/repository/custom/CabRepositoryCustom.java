package com.isut.repostory.custom;

import com.isut.dto.CabFilterWithPaginationDto;
import com.isut.dto.PaginationDto;

public interface CabRepositoryCustom {

	PaginationDto getCabListByFilterWithPagination(CabFilterWithPaginationDto filterWithPagination);
}
