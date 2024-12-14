package com.medidr.doctor.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AppointmentDtls {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)

    @JsonProperty("appointment_id")
    private Long appointmentId;

    @JsonProperty("doctor_id")
    private Long doctorId;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("appointment_date")
    private String appointmentDate;

    @JsonProperty("appointment_from")
    private String appointmentFrom;

    @JsonProperty("appointment_to")
    private String appointmentTo;

    @JsonProperty("appointment_stage")
    private String appointmentStage;

    @JsonProperty("patient_name")
    private String patientName;

    @JsonProperty("mobile_number")
    private String mobileNumber;

    @JsonProperty("gender")
    private Integer gender;

    @JsonProperty("relation")
    private Integer relation;

    @JsonProperty("age")
    private Integer age;

    @JsonProperty("fullname")
    private String doctorName;

    @JsonProperty("user_mobile_number")
    private String userMobileNumber;

    @JsonProperty("user_name")
    private String userName;


    @JsonProperty("hospital_name")
    private String hospitalName;

    @JsonProperty("hospital_address")
    private String hospitalAddress;

    @JsonProperty("zipcode")
    private int zipcode;

    @JsonProperty("modified")
    private boolean modified;

    @JsonProperty("deleted")
    private boolean deleted;

    @JsonProperty("doctor_profile_thumbnail_image")
    private String doctorProfileThumbnailImage;

    @JsonProperty("doctor_mobile_number")
    private String doctorMobileNumber;

    @JsonProperty("city")
    private String patientCity;

    public String getPatientCity() {
        return patientCity;
    }

    public void setPatientCity(String patientCity) {
        this.patientCity = patientCity;
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

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
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

    public String getAppointmentFrom() {
        return appointmentFrom;
    }

    public void setAppointmentFrom(String appointmentFrom) {
        this.appointmentFrom = appointmentFrom;
    }

    public String getAppointmentTo() {
        return appointmentTo;
    }

    public void setAppointmentTo(String appointmentTo) {
        this.appointmentTo = appointmentTo;
    }

    public String getAppointmentStage() {
        return appointmentStage;
    }

    public void setAppointmentStage(String appointmentStage) {
        this.appointmentStage = appointmentStage;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getRelation() {
        return relation;
    }

    public int getZipcode() {
        return zipcode;
    }


    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
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


    public void setRelation(Integer relation) {
        this.relation = relation;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getDoctorProfileThumbnailImage() {
        return doctorProfileThumbnailImage;
    }

    public void setDoctorProfileThumbnailImage(String doctorProfileThumbnailImage) {
        this.doctorProfileThumbnailImage = doctorProfileThumbnailImage;
    }


    public String getUserMobileNumber() {
        return userMobileNumber;
    }

    public void setUserMobileNumber(String userMobileNumber) {
        this.userMobileNumber = userMobileNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDoctorMobileNumber() {
        return doctorMobileNumber;
    }

    public void setDoctorMobileNumber(String doctorMobileNumber) {
        this.doctorMobileNumber = doctorMobileNumber;
    }

    @JsonProperty("latitude")
    private Double doctorAddrLatitude;

    @JsonProperty("longitude")
    private Double doctorAddrLongigtude;



    public Double getDoctorAddrLatitude() {
        return doctorAddrLatitude;
    }

    public void setDoctorAddrLatitude(Double doctorAddrLatitude) {
        this.doctorAddrLatitude = doctorAddrLatitude;
    }

    public Double getDoctorAddrLongigtude() {
        return doctorAddrLongigtude;
    }

    public void setDoctorAddrLongigtude(Double doctorAddrLongigtude) {
        this.doctorAddrLongigtude = doctorAddrLongigtude;
    }
    @Override
    public String toString() {
        return String.format(
                "AppointmentDtls [appointmentId=%s, doctorId=%s, userId=%s, appointmentDate=%s, appointmentFrom=%s, appointmentTo=%s, appointmentStage=%s, patientName=%s, mobileNumber=%s, gender=%s, relation=%s, age=%s, doctorName=%s, userMobileNumber=%s, userName=%s, hospitalName=%s, hospitalAddress=%s, zipcode=%s, modified=%s, deleted=%s, doctorProfileThumbnailImage=%s, doctorMobileNumber=%s, patientCity=%s, doctorAddrLatitude=%s, doctorAddrLongigtude=%s]",
                appointmentId, doctorId, userId, appointmentDate, appointmentFrom, appointmentTo, appointmentStage,
                patientName, mobileNumber, gender, relation, age, doctorName, userMobileNumber, userName, hospitalName,
                hospitalAddress, zipcode, modified, deleted, doctorProfileThumbnailImage, doctorMobileNumber,
                patientCity, doctorAddrLatitude, doctorAddrLongigtude);
    }

}
