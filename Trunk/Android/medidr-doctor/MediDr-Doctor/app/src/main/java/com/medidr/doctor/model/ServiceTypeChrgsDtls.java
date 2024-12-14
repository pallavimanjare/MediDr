package com.medidr.doctor.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


public class ServiceTypeChrgsDtls {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)

    @JsonProperty("serv_chrgs_id")
    private Long servChrgsId;

    @JsonProperty("service_type")
    private int serviceType;

    @JsonProperty("service_charge")
    private double serviceCharge;

    public Long getServChrgsId() {
        return servChrgsId;
    }

    public void setServChrgsId(Long servChrgsId) {
        this.servChrgsId = servChrgsId;
    }

    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public double getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    @Override
    public String toString() {
        return String.format("ServiceTypeChrgsDtls [servChrgsId=%s, serviceType=%s, serviceCharge=%s]", servChrgsId,
                serviceType, serviceCharge);
    }

}

