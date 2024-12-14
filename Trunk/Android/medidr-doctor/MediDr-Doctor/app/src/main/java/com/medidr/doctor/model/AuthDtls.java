package com.medidr.doctor.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthDtls {
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonIgnoreProperties(ignoreUnknown = true)	
	
	@JsonProperty("auth_dtl_id")
	private Long userId;
	
	@JsonProperty("user_type")
	private String userType;
	
	@JsonProperty("password")
	private String password;
	
	@JsonProperty("mobile")
	private String mobile;
	
	@JsonProperty("email")
	private String email;	
	
	@JsonProperty("modified")
	private boolean modified;		
	
	@JsonProperty("deleted")
	private boolean deleted;

	@JsonProperty("otp")
	private String OTP;
	
	@JsonProperty("gcm_token")
	private String gcmToken;

	@JsonProperty("device_id")
	private String deviceId;


	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getOTP() {
		return OTP;
	}

	public void setOTP(String oTP) {
		OTP = oTP;
	}

	
	public String getGcmToken() {
		return gcmToken;
	}

	public void setGcmToken(String gcmToken) {
		this.gcmToken = gcmToken;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@Override
	public String toString() {
		return String
				.format("AuthDtls [userId=%s, userType=%s, password=%s, mobile=%s, email=%s, modified=%s, deleted=%s, OTP=%s, gcmToken=%s, deviceId=%s]",
						userId, userType, password, mobile, email, modified, deleted, OTP, gcmToken, deviceId);
	}

	

	
}
