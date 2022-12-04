package com.isut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isut.model.License;

@Repository
public interface LicenseRepository extends JpaRepository<License, Long> {

	License findByDriverId(Long id);

}
