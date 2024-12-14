package com.medidr.doctor.model.resposne;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.medidr.doctor.model.AppointmentDtls;
import com.medidr.doctor.model.DocPersonalDtls;

import java.util.List;

public class DashBoardResponseDetails {

    @JsonProperty("doctor_details")
    private DocPersonalDtls doctorPersonalDetails;

    @JsonProperty("appointment_details")
    private List<AppointmentDtls> appointmentDetails;

    public DocPersonalDtls getDoctorPersonalDetails() {
        return doctorPersonalDetails;
    }

    public void setDoctorPersonalDetails(DocPersonalDtls doctorPersonalDetails) {
        this.doctorPersonalDetails = doctorPersonalDetails;
    }

    public List<AppointmentDtls> getAppointmentDetails() {
        return appointmentDetails;
    }

    public void setAppointmentDetails(List<AppointmentDtls> appointmentDetails) {
        this.appointmentDetails = appointmentDetails;
    }

    @Override
    public String toString() {
        return "DashBoardResponseDetails{" +
                "doctorPersonalDetails=" + doctorPersonalDetails +
                ", appointmentDetails=" + appointmentDetails +
                '}';
    }
}
