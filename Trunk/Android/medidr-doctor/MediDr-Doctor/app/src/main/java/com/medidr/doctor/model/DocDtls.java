package com.medidr.doctor.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DocDtls {
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonIgnoreProperties(ignoreUnknown = true)	
	
	@JsonProperty("auth_details")
	private AuthDtls authDtls;
	
	@JsonProperty("doctor_personal_details")
	private DocPersonalDtls docPersonalDtls;
	
	
	@JsonProperty("doctor_profile_settings")
	private DocProfileDtls docProfileDtls;
	
	@JsonProperty("doctor_services")
	private List<DocServicesDtls> docServicesDtls;
	
	@JsonProperty("doctor_schedules")
	private List<DocScheduleDtls> docScheduleDtls;

	@JsonProperty("address_details")
	private AddressDtls addressDtls;
	
	@JsonProperty("doctor_hospital_details")
	private DocHospitalDtls docHospitalDtls;

	@JsonProperty("appointment_details")
	private AppointmentDtls appointmentDtls;
	
	public AuthDtls getAuthDtls() {
		return authDtls;
	}

	public void setAuthDtls(AuthDtls authDtls) {
		this.authDtls = authDtls;
	}

	public DocPersonalDtls getDocPersonalDtls() {
		return docPersonalDtls;
	}

	public void setDocPersonalDtls(DocPersonalDtls docPersonalDtls) {
		this.docPersonalDtls = docPersonalDtls;
	}

	public DocProfileDtls getDocProfileDtls() {
		return docProfileDtls;
	}

	public void setDocProfileDtls(DocProfileDtls docProfileDtls) {
		this.docProfileDtls = docProfileDtls;
	}

	

	public List<DocServicesDtls> getDocServicesDtls() {
		return docServicesDtls;
	}

	public void setDocServicesDtls(List<DocServicesDtls> docServicesDtls) {
		this.docServicesDtls = docServicesDtls;
	}

	

	public List<DocScheduleDtls> getDocScheduleDtls() {
		return docScheduleDtls;
	}

	public void setDocScheduleDtls(List<DocScheduleDtls> docScheduleDtls) {
		this.docScheduleDtls = docScheduleDtls;
	}

	public AddressDtls getAddressDtls() {
		return addressDtls;
	}

	public void setAddressDtls(AddressDtls addressDtls) {
		this.addressDtls = addressDtls;
	}

	public DocHospitalDtls getDocHospitalDtls() {
		return docHospitalDtls;
	}

	public void setDocHospitalDtls(DocHospitalDtls docHospitalDtls) {
		this.docHospitalDtls = docHospitalDtls;
	}

	public AppointmentDtls getAppointmentDtls() {
		return appointmentDtls;
	}

	public void setAppointmentDtls(AppointmentDtls appointmentDtls) {
		this.appointmentDtls = appointmentDtls;
	}

	@Override
	public String toString() {
		return String.format(
				"DocDtls [authDtls=%s, docPersonalDtls=%s, docProfileDtls=%s, docServicesDtls=%s, docScheduleDtls=%s, addressDtls=%s, docHospitalDtls=%s, appointmentDtls=%s]",
				authDtls, docPersonalDtls, docProfileDtls, docServicesDtls, docScheduleDtls, addressDtls,
				docHospitalDtls, appointmentDtls);
	}

}
