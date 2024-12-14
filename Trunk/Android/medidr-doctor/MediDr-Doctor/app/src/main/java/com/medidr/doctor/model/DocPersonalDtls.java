package com.medidr.doctor.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DocPersonalDtls {
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonIgnoreProperties(ignoreUnknown = true)	
	
	@JsonProperty("doctor_id")
	private Long doctorId;
	
	@JsonProperty("fullname")
	private String fullName;
	
	@JsonProperty("speciality")
	private String speciality;
	
	@JsonProperty("qualification")
	private String qualification;
	
	@JsonProperty("experience")
	private Integer experience;
	
	@JsonProperty("modified")
	private boolean modified;	
	
	@JsonProperty("deleted")
	private boolean deleted;
	

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@JsonProperty("consultation_fees")
	private Integer consultationFees;
	
	@JsonProperty("doctor_profile_thumbnail_image")
	private String doctorProfileThumbnailImage;	
	
	@JsonProperty("doctor_profile_image")
	private String doctorProfileImage;
	
	@JsonProperty("doctor_other_info")
	private String doctorOtherInfo;

	public Long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getSpeciality() {
		return speciality;
	}

	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public Integer getExperience() {
		return experience;
	}

	public void setExperience(Integer experience) {
		this.experience = experience;
	}

	public String getDoctorProfileThumbnailImage() {
		return doctorProfileThumbnailImage;
	}

	public void setDoctorProfileThumbnailImage(String doctorProfileThumbnailImage) {
		this.doctorProfileThumbnailImage = doctorProfileThumbnailImage;
	}

	public String getDoctorProfileImage() {
		return doctorProfileImage;
	}

	public void setDoctorProfileImage(String doctorProfileImage) {
		this.doctorProfileImage = doctorProfileImage;
	}

	public String getDoctorOtherInfo() {
		return doctorOtherInfo;
	}

	public void setDoctorOtherInfo(String doctorOtherInfo) {
		this.doctorOtherInfo = doctorOtherInfo;
	}	

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}

	
	public Integer getConsultationFees() {
		return consultationFees;
	}

	public void setConsultationFees(Integer consultationFees) {
		this.consultationFees = consultationFees;
	}

	@Override
	public String toString() {
		return String.format(
				"DocPersonalDtls [doctorId=%s, fullName=%s, speciality=%s, qualification=%s, experience=%s, modified=%s, deleted=%s, consultationFees=%s, doctorProfileThumbnailImage=%s, doctorProfileImage=%s, doctorOtherInfo=%s]",
				doctorId, fullName, speciality, qualification, experience, modified, deleted, consultationFees,
				doctorProfileThumbnailImage, doctorProfileImage, doctorOtherInfo);
	}

	
	
	

}
