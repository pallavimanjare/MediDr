package com.medidr.doctor.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CallAppointmentDtls {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)

    @JsonProperty("call_appointment_id")
    private Long callAppointmentId;

    @JsonProperty("doctor_id")
    private Long doctorId;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("appointment_date")
    private String appointmentDate;

    @JsonProperty("modified")
    private boolean modified;

    @JsonProperty("deleted")
    private boolean deleted;

    @JsonProperty("doctor_name")
    private String doctorName;

    @JsonProperty("hospital_name")
    private String hospitalName;

    @JsonProperty("hospital_address")
    private String hospitalAddress;

    @JsonProperty("patient_name")
    private String patientName;

    @JsonProperty("user_contact_number")
    private String userContactNumber;

    @JsonProperty("doctor_mobile_number")
    private String doctorMobileNumber;

    public Long getCallAppointmentId() {
        return callAppointmentId;
    }

    public void setCallAppointmentId(Long callAppointmentId) {
        this.callAppointmentId = callAppointmentId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
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

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getHospitalAddress() {
        return hospitalAddress;
    }

    public void setHospitalAddress(String hospitalAddress) {
        this.hospitalAddress = hospitalAddress;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getUserContactNumber() {
        return userContactNumber;
    }

    public void setUserContactNumber(String userContactNumber) {
        this.userContactNumber = userContactNumber;
    }

    public String getDoctorMobileNumber() {
        return doctorMobileNumber;
    }

    public void setDoctorMobileNumber(String doctorMobileNumber) {
        this.doctorMobileNumber = doctorMobileNumber;
    }

    @Override
    public String toString() {
        return String.format(
                "CallAppointmentDtls [callAppointmentId=%s, doctorId=%s, userId=%s, appointmentDate=%s, modified=%s, deleted=%s, doctorName=%s, hospitalName=%s, hospitalAddress=%s, patientName=%s, userContactNumber=%s, doctorMobileNumber=%s]",
                callAppointmentId, doctorId, userId, appointmentDate, modified, deleted, doctorName, hospitalName,
                hospitalAddress, patientName, userContactNumber, doctorMobileNumber);
    }

}
