package com.isut.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isut.model.Tip;

@Repository
public interface TipRepository extends JpaRepository<Tip, Long> {

	List<Tip> findByDriverId(long driverId);

	List<Tip> findByUserId(long userId);

	Optional<Tip> findByBookingId(long bookingId);

}
