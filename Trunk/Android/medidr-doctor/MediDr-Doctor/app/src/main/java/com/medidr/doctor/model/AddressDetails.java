package com.medidr.doctor.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;


@JsonRootName("address_details")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressDetails {

    @JsonProperty("text_address")
    private String textAddress;

    @JsonProperty("address_id")
    private Long addressId;

    @JsonProperty("zipcode")
    private Integer zipcode;

    @JsonProperty("longitude")
    private Integer longitude;

    @JsonProperty("latitude")
    private Integer latitude;

    public String getTextAddress() {
        return textAddress;
    }

    public void setTextAddress(String textAddress) {
        this.textAddress = textAddress;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Integer getZipcode() {
        return zipcode;
    }

    public void setZipcode(Integer zipcode) {
        this.zipcode = zipcode;
    }

    public Integer getLongitude() {
        return longitude;
    }

    public void setLongitude(Integer longitude) {
        this.longitude = longitude;
    }

    public Integer getLatitude() {
        return latitude;
    }

    public void setLatitude(Integer latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "AddressDetails{" +
                "textAddress='" + textAddress + '\'' +
                ", addressId=" + addressId +
                ", zipcode=" + zipcode +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
