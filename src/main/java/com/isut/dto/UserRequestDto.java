package com.isut.dto;

import java.util.Date;

public class UserRequestDto {
	private String fullName;
	private String email;
	private String password;
	private String mobileNumber;
	private String appId;
	private String profileImage;
	private Date licenseExpired;

	public Date getLicenseExpired() {
		return licenseExpired;
	}

	public void setLicenseExpired(Date licenceExpired) {
		this.licenseExpired = licenceExpired;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

}
