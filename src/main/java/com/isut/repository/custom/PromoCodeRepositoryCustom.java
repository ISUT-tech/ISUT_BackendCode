package com.isut.repository.custom;

import com.isut.dto.PaginationDto;
import com.isut.dto.PromoCodeFilterWithPaginationDto;

public interface PromoCodeRepositoryCustom {

	PaginationDto getPromoCodeListByFilterWithPagination(PromoCodeFilterWithPaginationDto filterWithPagination);
}
