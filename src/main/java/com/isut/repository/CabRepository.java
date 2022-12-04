package com.isut.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isut.model.Cab;

@Repository
public interface CabRepository extends JpaRepository<Cab, Long> {

	Cab findByCarNumber(String carNumber);

	List<Cab> findByUserId(long userId);

	boolean existsByCarNumber(String carNumber);

	List<Cab> findByCarNameContaining(String carName);

	List<Cab> findByCarType(Integer carType);

	void deleteByUserId(long id);

}
