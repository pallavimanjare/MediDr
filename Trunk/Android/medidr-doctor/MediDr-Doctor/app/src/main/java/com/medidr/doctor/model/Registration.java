package com.medidr.doctor.model;

/**
 * Created by mansi on 3/13/2016.
 */
public class Registration {

    private String doctorName;

    private String email;

    private String mobile;

    private String specialty;

    private Integer experience;

    private String qualifications;

    private Integer consultationFees;

    public Registration(String doctorName, String email, String mobile, String specialty,  String qualifications, Integer experience,Integer consultationFees) {
        this.doctorName = doctorName;
        this.email = email;
        this.mobile = mobile;
        this.specialty = specialty;
        this.experience = experience;
        this.qualifications = qualifications;
        this.consultationFees = consultationFees;
    }
}
