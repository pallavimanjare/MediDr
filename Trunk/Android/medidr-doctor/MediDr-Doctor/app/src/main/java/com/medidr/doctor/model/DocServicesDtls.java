package com.medidr.doctor.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DocServicesDtls {
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonIgnoreProperties(ignoreUnknown = true)	
	
	@JsonProperty("doc_service_id")
	private Long docServiceId;
	
	@JsonProperty("doctor_id")
	private Long doctorId;
	
	@JsonProperty("service_name")
	private String serviceName;
	
	@JsonProperty("service_active_flag")
	private Boolean serviceActiveFlag;
	
	@JsonProperty("modified")
	private boolean modified;
	
	@JsonProperty("deleted")
	private boolean deleted;

	public Long getDocServiceId() {
		return docServiceId;
	}

	public void setDocServiceId(Long docServiceId) {
		this.docServiceId = docServiceId;
	}

	public Long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Boolean getServiceActiveFlag() {
		return serviceActiveFlag;
	}

	public void setServiceActiveFlag(Boolean serviceActiveFlag) {
		this.serviceActiveFlag = serviceActiveFlag;
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

	@Override
	public String toString() {
		return String.format(
				"DocServicesDtls [docServiceId=%s, doctorId=%s, serviceName=%s, serviceActiveFlag=%s, modified=%s, deleted=%s]",
				docServiceId, doctorId, serviceName, serviceActiveFlag, modified, deleted);
	}
	
	
}
