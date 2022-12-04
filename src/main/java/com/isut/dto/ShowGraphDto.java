package com.isut.dto;

import java.util.List;

public class ShowGraphDto {

	private long totalUser;
	private long totalDriver;
	private long totalBooking;
	private List<GraphYearDto> graphYearDto;

	public long getTotalUser() {
		return totalUser;
	}

	public void setTotalUser(long totalUser) {
		this.totalUser = totalUser;
	}

	public long getTotalDriver() {
		return totalDriver;
	}

	public void setTotalDriver(long totalDriver) {
		this.totalDriver = totalDriver;
	}

	public long getTotalBooking() {
		return totalBooking;
	}

	public void setTotalBooking(long totalBooking) {
		this.totalBooking = totalBooking;
	}

	public List<GraphYearDto> getGraphYearDto() {
		return graphYearDto;
	}

	public void setGraphYearDto(List<GraphYearDto> graphYearDto) {
		this.graphYearDto = graphYearDto;
	}

}
