package com.isut.dto;

import java.util.List;

import com.isut.model.Cab;

public class CabDetailsWithDriverRewardDto {

	private Long rewardPoints;
	private Long driverId;
	private List<Cab> cabs;

	public Long getRewardPoints() {
		return rewardPoints;
	}

	public void setRewardPoints(Long rewardPoints) {
		this.rewardPoints = rewardPoints;
	}

	public Long getDriverId() {
		return driverId;
	}

	public void setDriverId(Long driverId) {
		this.driverId = driverId;
	}

	public List<Cab> getCabs() {
		return cabs;
	}

	public void setCabs(List<Cab> cabs) {
		this.cabs = cabs;
	}

}
