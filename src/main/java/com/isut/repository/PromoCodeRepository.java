package com.isut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isut.model.PromoCode;

@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode, Long> {

	boolean existsByCode(String code);

	PromoCode findByCode(String promoCode);

}
