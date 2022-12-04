package com.isut.dto;

import java.util.List;

import com.isut.model.Cab;
import com.isut.model.User;

public class UserWithCabDetails {
	private User user;
	private String licenseNumber;
	private List<Cab> cabs;

	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Cab> getCabs() {
		return cabs;
	}

	public void setCabs(List<Cab> cabs) {
		this.cabs = cabs;
	}

}
