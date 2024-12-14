package com.medidr.doctor.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AddressDtls {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonIgnoreProperties(ignoreUnknown = true)

	@JsonProperty("address_id")
	private Long addressId;

	@JsonProperty("zipcode")
	private Integer zipCode;

	@JsonProperty("text_address")
	private String textAddress;
	
	@JsonProperty("Longitude")
	private Double Longitude;
	
	@JsonProperty("Latitude")
	private Double Latitude;

	@JsonProperty("modified")
	private boolean modified;

	@JsonProperty("deleted")
	private boolean deleted;

	@JsonProperty("city")
	private String city;

	@JsonProperty("locality")
	private String locality;

	@JsonProperty("state")
	private String state;

	@JsonProperty("country")
	private String country;

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public Integer getZipCode() {
		return zipCode;
	}

	public void setZipCode(Integer zipCode) {
		this.zipCode = zipCode;
	}

	public String getTextAddress() {
		return textAddress;
	}

	public void setTextAddress(String textAddress) {
		this.textAddress = textAddress;
	}

	public Double getLongitude() {
		return Longitude;
	}

	public void setLongitude(Double longitude) {
		Longitude = longitude;
	}

	public Double getLatitude() {
		return Latitude;
	}

	public void setLatitude(Double latitude) {
		Latitude = latitude;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return String
				.format("AddressDtls [addressId=%s, zipCode=%s, textAddress=%s, Longitude=%s, Latitude=%s, locality=%s, city=%s, state=%s, country=%s, modified=%s, deleted=%s]",
						addressId, zipCode, textAddress, Longitude, Latitude, locality, city, state, country, modified,
						deleted);
	}

}
