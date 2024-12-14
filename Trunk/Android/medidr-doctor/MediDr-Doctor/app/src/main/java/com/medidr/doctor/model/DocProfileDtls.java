package com.medidr.doctor.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DocProfileDtls {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonIgnoreProperties(ignoreUnknown = true)

	@JsonProperty("doctor_profile_settings_id")
	private Long doctorProfileSettingsId;

	@JsonProperty("doctor_id")
	private Long doctorId;

	@JsonProperty("appointment_time_freq")
	private Integer appointmentTimeFreq;

	@JsonProperty("activeFlag")
	private Boolean active_flag;

	@JsonProperty("featured_doc_flag")
	private Boolean featuredDocFlag;

	@JsonProperty("likes_count")
	private Integer likesCount;

	@JsonProperty("effective_date_from")
	private Date effectiveDateFrom;

	@JsonProperty("effective_date_to")
	private Date effectiveDateTo;

	@JsonProperty("service_flag")
	private Integer serviceFlag;

	@JsonProperty("weekoffday")
	List<String> weekoffday;

	@JsonProperty("modified")
	private boolean modified;

	@JsonProperty("deleted")
	private boolean deleted;


	@JsonProperty("effective_date_from_admin")
	private String effectiveDateFromAdmin;

	@JsonProperty("effective_date_to_admin")
	private String effectiveDateToAdmin;


	public String getEffectiveDateFromAdmin() {
		return effectiveDateFromAdmin;
	}

	public void setEffectiveDateFromAdmin(String effectiveDateFromAdmin) {
		this.effectiveDateFromAdmin = effectiveDateFromAdmin;
	}

	public String getEffectiveDateToAdmin() {
		return effectiveDateToAdmin;
	}

	public void setEffectiveDateToAdmin(String effectiveDateToAdmin) {
		this.effectiveDateToAdmin = effectiveDateToAdmin;
	}

	public Long getDoctorProfileSettingsId() {
		return doctorProfileSettingsId;
	}

	public void setDoctorProfileSettingsId(Long doctorProfileSettingsId) {
		this.doctorProfileSettingsId = doctorProfileSettingsId;
	}

	public Long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getAppointmentTimeFreq() {
		return appointmentTimeFreq;
	}

	public void setAppointmentTimeFreq(Integer appointmentTimeFreq) {
		this.appointmentTimeFreq = appointmentTimeFreq;
	}

	public Boolean getActive_flag() {
		return active_flag;
	}

	public void setActive_flag(Boolean active_flag) {
		this.active_flag = active_flag;
	}

	public Boolean getFeaturedDocFlag() {
		return featuredDocFlag;
	}

	public void setFeaturedDocFlag(Boolean featuredDocFlag) {
		this.featuredDocFlag = featuredDocFlag;
	}

	public Integer getLikesCount() {
		return likesCount;
	}

	public void setLikesCount(Integer likesCount) {
		this.likesCount = likesCount;
	}

	public Date getEffectiveDateFrom() {
		return effectiveDateFrom;
	}

	public void setEffectiveDateFrom(Date effectiveDateFrom) {
		this.effectiveDateFrom = effectiveDateFrom;
	}

	public Date getEffectiveDateTo() {
		return effectiveDateTo;
	}

	public void setEffectiveDateTo(Date effectiveDateTo) {
		this.effectiveDateTo = effectiveDateTo;
	}

	public Integer getServiceFlag() {
		return serviceFlag;
	}

	public void setServiceFlag(Integer serviceFlag) {
		this.serviceFlag = serviceFlag;
	}

	public List<String> getWeekoffday() {
		return weekoffday;
	}

	public void setWeekoffday(List<String> weekoffday) {
		this.weekoffday = weekoffday;
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
				"DocProfileDtls [doctorProfileSettingsId=%s, doctorId=%s, appointmentTimeFreq=%s, active_flag=%s, featuredDocFlag=%s, likesCount=%s, effectiveDateFrom=%s, effectiveDateTo=%s, serviceFlag=%s, weekoffday=%s, modified=%s, deleted=%s, effectiveDateFromAdmin=%s, effectiveDateToAdmin=%s]",
				doctorProfileSettingsId, doctorId, appointmentTimeFreq, active_flag, featuredDocFlag, likesCount,
				effectiveDateFrom, effectiveDateTo, serviceFlag, weekoffday, modified, deleted, effectiveDateFromAdmin,
				effectiveDateToAdmin);
	}



}
