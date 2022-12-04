package com.isut.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isut.model.Driver;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

	List<Driver> findByRole(int role);

	boolean existsByMobileNumber(String mobileNumber);

	Driver findByMobileNumber(String mobileNumber);

}
