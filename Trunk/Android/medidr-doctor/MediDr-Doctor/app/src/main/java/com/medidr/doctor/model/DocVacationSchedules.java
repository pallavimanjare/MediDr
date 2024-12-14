package com.medidr.doctor.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DocVacationSchedules {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)

    @JsonProperty("doc_vacations_id")
    private Long docVacationsId;

    @JsonProperty("doctor_id")
    private Long doctorId;

    @JsonProperty("vacation_from_date")
    private String fromDate;

    @JsonProperty("vacation_to_date")
    private String toDate;

    public Long getDocVacationsId() {
        return docVacationsId;
    }

    public void setDocVacationsId(Long docVacationsId) {
        this.docVacationsId = docVacationsId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    @Override
    public String toString() {
        return String.format("DocVacationSchedules [docVacationsId=%s, doctorId=%s, fromDate=%s, toDate=%s]",
                docVacationsId, doctorId, fromDate, toDate);
    }

}
