package com.medidr.doctor.config;

/**
 * Created by Pallavi on 3/13/2016.
 */
public class Constants {

    public static String HOST = "52.37.82.67";
   // public static String HOST = "14.97.84.11";


    public static final String SCHEME = "http://";

    public static final String PORT = "8080";

    public static final String APP = "/medidr-service/v1";

    public static final String LOGIN_URI = "/doctors/getDocAuthentication";

    public static final String REGISTER_URI = "";

    public static final String DASHBOARD_DETAILS_URI = "/doctors/getdoctordashboarddetails/";

    public static final String CANCEL_APPOINTMENT_URI = "/doctors/cancelappointment";

    public static final String SEND_MESSAGE = "/doctors/sendmessage";

    public static final String ADD_DOCTOR = "/doctors/addDoctor";

    public static final String ADD_HOSPITAL_DETAILS = "/doctors/addDoctorHospitalDetails";

    public static final String ADD_DOCTORS_SCHEDULE_DETAILS = "/doctors/addDoctorScheduleDetails";

    public static final String ADD_DOCTORS_SERVICES_DETAILS = "/doctors/addDoctorServices";

    public static final String GET_PERSONAL_DETAILS = "/doctors/getDoctorPersonalDtls/";

    public static final String GET_HOSPITALS_INFORMATION = "/doctors/getdoctorhospitalinformation/";

    public static final String GET_DOCTORS_SCHEDULE = "/doctors/getdoctorscheduleinformation/";

    public static final String SELECT_DOCTOR_SERVICES = "/doctors/selectDoctorServices/";

    public static final String FORGOT_PASSWORD = "/doctors/forgotpassword";

    public static final String GENERATE_OTP = "/auth/getOTPforForgotPassword";

    public static final String VALIDATE_OTP = "/auth/getOTPValidation";

    public static final String RESET_PASSWORD = "/auth/resetPassword";

    public static final String GET_APPOINTMENT_DETAILS = "/doctors/getAppointmentDetailsForDoctor/";

    public static final String SignUp_URI = "/doctors/checkDocMobileExist";

    public static final String ADD_VACATION_SCHEDULE = "/doctors/addVacationSchedules";

    public static final String GET_APPOINTMENTS_HISTORY_EXCEL = "/doctors/getAppointmentHistory";


}
