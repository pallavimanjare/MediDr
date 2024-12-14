package com.medidr.doctor.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DocHospitalDtls {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonIgnoreProperties(ignoreUnknown = true)

	@JsonProperty("address_details")
	private AddressDtls addressDtls;


	@JsonProperty("image_master_details")
	private List<ImageMasterDtls> imageMasterDtls;

	@JsonProperty("doc_hosp_id")
	private Long docHospId;

	@JsonProperty("doctor_id")
	private Long doctorId;


	@JsonProperty("address_id")
	private Long addressId;

	@JsonProperty("hospital_name")
	private  String hospitalName;

	@JsonProperty("hospital_contact_number")
	private String hospitalContactNumber;

	@JsonProperty("email")
	private String hospitalEmail;

	@JsonProperty("modified")
	private boolean modified;

	@JsonProperty("deleted")
	private boolean deleted;

	public AddressDtls getAddressDtls() {
		return addressDtls;
	}

	public void setAddressDtls(AddressDtls addressDtls) {
		this.addressDtls = addressDtls;
	}

	public List<ImageMasterDtls> getImageMasterDtls() {
		return imageMasterDtls;
	}

	public void setImageMasterDtls(List<ImageMasterDtls> imageMasterDtls) {
		this.imageMasterDtls = imageMasterDtls;
	}

	public Long getDocHospId() {
		return docHospId;
	}

	public void setDocHospId(Long docHospId) {
		this.docHospId = docHospId;
	}

	public Long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getHospitalContactNumber() {
		return hospitalContactNumber;
	}

	public void setHospitalContactNumber(String hospitalContactNumber) {
		this.hospitalContactNumber = hospitalContactNumber;
	}

	public String getHospitalEmail() {
		return hospitalEmail;
	}

	public void setHospitalEmail(String hospitalEmail) {
		this.hospitalEmail = hospitalEmail;
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
				"DocHospitalDtls [addressDtls=%s, imageMasterDtls=%s, docHospId=%s, doctorId=%s, addressId=%s, hospitalName=%s, hospitalContactNumber=%s, hospitalEmail=%s, modified=%s, deleted=%s]",
				addressDtls, imageMasterDtls, docHospId, doctorId, addressId, hospitalName, hospitalContactNumber,
				hospitalEmail, modified, deleted);
	}



}
