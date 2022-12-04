package com.isut.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isut.model.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

	List<Booking> findByDriverId(long driverId);

	List<Booking> findByUserId(long userId);

}
