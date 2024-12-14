package com.medidr.doctor.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


public class DocScheduleDtls {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)


    @JsonProperty("doc_sche_id")
    private Long docScheId;

    @JsonProperty("doctor_id")
    private Long doctorId;

    @JsonProperty("day_id")
    private String dayId;

    @JsonProperty("time_from")
    private String timeFrom;

    @JsonProperty("time_to")
    private String timeTo;

    @JsonProperty("modified")
    private boolean modified;

    @JsonProperty("deleted")
    private boolean deleted;

    @JsonProperty("added")
    private boolean added;




    public Long getDocScheId() {
        return docScheId;
    }

    public void setDocScheId(Long docScheId) {
        this.docScheId = docScheId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getDayId() {
        return dayId;
    }

    public void setDayId(String dayId) {
        this.dayId = dayId;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
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

    public boolean isAdded() {
        return added;
    }

    public void setAdded(boolean added) {
        this.added = added;
    }

    @Override
    public String toString() {
        return String.format(
                "DocScheduleDtls [docScheId=%s, doctorId=%s, dayId=%s, timeFrom=%s, timeTo=%s, modified=%s, deleted=%s, added=%s]",
                docScheId, doctorId, dayId, timeFrom, timeTo, modified, deleted, added);
    }
}
