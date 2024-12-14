package com.medidr.doctor.model;

import android.util.Log;

import com.medidr.doctor.model.resposne.DashBoardResponseDetails;

/**
 * Created by mansi on 3/19/2016.
 */
public class DataManager {

    private static DataManager dataManager;

    private AuthDtls authDetails;

    private DocDtls doctorDetails;

    private DocPersonalDtls personalDetails;

    private DocHospitalDtls hospitalDetails;

    private DocServicesDtls servicesDetails;

    private DocProfileDtls profileDtls;

    private AppointmentDtls appointmentDtls;

    private DashBoardResponseDetails dashBoardResponseDetails;

    private DataManager(){
        Log.i("DATAManager","Creating Singelton");

    }

    public static synchronized DataManager getInstance(){
        if(dataManager == null){
            dataManager = new DataManager();
        }
        return dataManager;
    }

    public static void flush(){
        dataManager = null;
    }

    public static DataManager getDataManager() {
        return dataManager;
    }

    public DocPersonalDtls getPersonalDetails() {
        return personalDetails;
    }

    public void setPersonalDetails(DocPersonalDtls personalDetails) {
        this.personalDetails = personalDetails;
    }

    public static void setDataManager(DataManager dataManager) {
        DataManager.dataManager = dataManager;
    }

    public AppointmentDtls getAppointmentDtls() {
        return appointmentDtls;
    }

    public void setAppointmentDtls(AppointmentDtls appointmentDtls) {
        this.appointmentDtls = appointmentDtls;
    }




    public DocHospitalDtls getHospitalDetails() {
        return hospitalDetails;
    }

    public void setHospitalDetails(DocHospitalDtls hospitalDetails) {
        this.hospitalDetails = hospitalDetails;
    }

    public DocServicesDtls getServicesDetails() {
        return servicesDetails;
    }

    public void setServicesDetails(DocServicesDtls servicesDetails) {
        this.servicesDetails = servicesDetails;
    }

    public DashBoardResponseDetails getDashBoardResponseDetails() {
        return dashBoardResponseDetails;
    }

    public void setDashBoardResponseDetails(DashBoardResponseDetails dashBoardResponseDetails) {
        this.dashBoardResponseDetails = dashBoardResponseDetails;
    }

    public AuthDtls getAuthDetails() {
        return authDetails;
    }

    public void setAuthDetails(AuthDtls authDetails) {
        this.authDetails = authDetails;
    }

    public DocDtls getDoctorDetails() {
        return doctorDetails;
    }

    public void setDoctorDetails(DocDtls doctorDetails) {
        this.doctorDetails = doctorDetails;
    }

    public DocProfileDtls getProfileDtls() {
        return profileDtls;
    }

    public void setProfileDtls(DocProfileDtls profileDtls) {
        this.profileDtls = profileDtls;
    }
}

