package com.medidr.doctor.ui;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.os.StrictMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.medidr.doctor.R;
import com.medidr.doctor.model.AuthDtls;
import com.medidr.doctor.model.DataManager;
import com.medidr.doctor.model.DocDtls;
import com.medidr.doctor.model.DocPersonalDtls;
import com.medidr.doctor.services.DoctorService;
import com.medidr.doctor.services.NetworkUtilService;
import com.medidr.doctor.ui.validation.UiValidation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorEditPersonalInfoFragment extends Fragment {

    private static final String TAG = "Logmessages";


    private EditText doctorName, mobile, email, qualification, experience, consultaionFees, otherInfo;
    AutoCompleteTextView specialty;
    JSONArray specialitiesArr = null;

    private ImageButton btnNext = null;

    NetworkUtilService objNetwork = null;
    Boolean isConnectionExist = false;
    ShowAlertDialog objAlert = null;

    TextView errorMsg;
    String []strSpecilaityArr = null;
    public DoctorEditPersonalInfoFragment() {
        // Required empty public constructor
    }

    DocDtls docDtls = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_doctor_edit_personal_info, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((HomeActivity) getActivity()).setSupportActionBar(toolbar);
        ((HomeActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((HomeActivity) getActivity()).getSupportActionBar().setIcon(R.mipmap.header_logo);
        toolbar.setNavigationIcon(R.mipmap.ic_action_arrow_left);
        // ((HomeActivity) getActivity()).toolbar.setNavigationIcon(R.mipmap.close_button_copy);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getActivity().onBackPressed();
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });
        toolbar.setTitle("Edit Personal Information");
        docDtls = DataManager.getDataManager().getDoctorDetails();

        errorMsg= (TextView) v.findViewById(R.id.errorMsg);
        doctorName = (EditText) v.findViewById(R.id.doctorName);
        mobile = (EditText) v.findViewById(R.id.mobile);
        email = (EditText) v.findViewById(R.id.email);
        specialty = (AutoCompleteTextView) v.findViewById(R.id.specialty);
        qualification = (EditText) v.findViewById(R.id.qualification);
        experience = (EditText) v.findViewById(R.id.experience);
        consultaionFees = (EditText) v.findViewById(R.id.consultationFees);
        otherInfo = (EditText) v.findViewById(R.id.otherInfo);
        btnNext = (ImageButton) v.findViewById(R.id.btnNext);


        String strDocName = docDtls.getDocPersonalDtls().getFullName();
        String strSpeciality = docDtls.getDocPersonalDtls().getSpeciality();
        String strExperince = String.valueOf(docDtls.getDocPersonalDtls().getExperience());
        String strConsulationFees = String.valueOf(docDtls.getDocPersonalDtls().getConsultationFees());
        String strOtherInfo = docDtls.getDocPersonalDtls().getDoctorOtherInfo();
        String strMobile = docDtls.getAuthDtls().getMobile();
        String strEmail = docDtls.getAuthDtls().getEmail();
        String strQualification = docDtls.getDocPersonalDtls().getQualification();
        doctorName.setText(strDocName);
        mobile.setText(strMobile);
        email.setText(strEmail);
        specialty.setText(strSpeciality);
        qualification.setText(strQualification);
        otherInfo.setText(strOtherInfo);
        consultaionFees.setText(strConsulationFees);
        experience.setText(strExperince);
        mobile.setEnabled(false);

        doctorName.requestFocus();
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editDoctorPersonalDetails();
            }
        });

        //get specilaity from ddatabase
        getSpecilaityForDoctor();

        //specialty
        specialty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) {
                    // on focus off
                    String str = specialty.getText().toString();

                    ListAdapter listAdapter = specialty.getAdapter();
                    for(int i = 0; i < listAdapter.getCount(); i++) {
                        String temp = listAdapter.getItem(i).toString();
                        if(str.compareTo(temp) == 0) {
                            return;
                        }
                    }
                    specialty.setError("Please select speciality");



                }
            }
        });
        //done with specilaity
        return v;
    }

    public void getSpecilaityForDoctor() {
        objNetwork = new NetworkUtilService(getActivity());
        objAlert = new ShowAlertDialog();
        isConnectionExist = objNetwork.checkMobileInternetConn();
        if (isConnectionExist) {
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                StringBuffer result = new StringBuffer();
                DoctorService objDocService = new DoctorService();

                String path = "/doctors/getSpeciality";
                String response = objDocService.getInformationForDoctor(path);
                // HttpResponse response =
                Log.d(TAG, " getting speciality as:: =================>"
                        + response);

                if (response != null) {
                    JSONObject jsonResponse = new JSONObject(response);
                    specialitiesArr = jsonResponse.getJSONArray("data");
                    String specialityArr[] = new String[specialitiesArr.length()];

                    for (int i = 0; i < specialitiesArr.length(); i++) {
                        JSONObject jsonSpeciality = specialitiesArr.getJSONObject(i);
                        specialityArr[i] = jsonSpeciality.getString("speciality");
                    }
                    strSpecilaityArr = specialityArr;
                    ArrayAdapter<String> specialityAdapter = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_list_item_1, specialityArr);
                    specialty.setAdapter(specialityAdapter);
                    specialty.setThreshold(2);

                }


                Log.d(TAG, " After RESTful service response =================>"
                        + result);


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            objAlert.showAlertDialog(getActivity(), "No Internet Connection",
                    "check your connection and try again", false);
        }
    }


    private boolean checkRequiredValidation() {
        boolean ret = true;

        if (!UiValidation.hasText(doctorName))
            ret = false;

        if (!UiValidation.hasText(mobile))
            ret = false;

        if (!UiValidation.hasText(email))
            ret = false;
        if (!UiValidation.hasText(specialty))
            ret = false;

        if (!UiValidation.hasText(qualification))
            ret = false;

        if (!UiValidation.hasText(experience))
            ret = false;

        if (!UiValidation.hasText(consultaionFees))
            ret = false;
        return ret;
    }

    private boolean checkRegxValidation() {
        boolean ret = true;

        if (!UiValidation.isValidMobileNumber(mobile))
            ret = false;
        if (!UiValidation.isValidEmailAddress(email))
            ret = false;

        return ret;

    }
    private boolean checkCorrectSpecialitySelected()
    {
        List validList = Arrays.asList(strSpecilaityArr);
        boolean flag = true;
        String selectedSpeciality = specialty.getText().toString();
        if(validList.contains(selectedSpeciality))
        {
            errorMsg.setText(null);
            flag = true;
        }
        else
        {
            flag = false;
        }
        return flag;
    }
    private void editDoctorPersonalDetails() {

        objNetwork = new NetworkUtilService(getActivity());
        objAlert = new ShowAlertDialog();
        isConnectionExist = objNetwork.checkMobileInternetConn();

       // String strSpecialityError = specialty.getError().toString();
        if (isConnectionExist) {
            if (!checkRequiredValidation()) {
            } else if (!checkRegxValidation()) {
            }
            else if(!checkCorrectSpecialitySelected())
            {
                specialty.setError("Please select speciality");
            }

            else {
                AuthDtls authDetails = docDtls.getAuthDtls();

                authDetails.setEmail(email.getText().toString());
                authDetails.setMobile(mobile.getText().toString());
                authDetails.setModified(true);
                authDetails.setUserType("DOCTOR");
                docDtls.setAuthDtls(authDetails);

                DocPersonalDtls docPersonalDtls = docDtls.getDocPersonalDtls();
                docPersonalDtls.setFullName(doctorName.getText().toString());
                docPersonalDtls.setSpeciality(specialty.getText().toString());
                docPersonalDtls.setQualification(qualification.getText().toString());
                docPersonalDtls.setExperience(Integer.parseInt(experience.getText().toString()));
                docPersonalDtls.setConsultationFees(Integer.parseInt(consultaionFees.getText().toString()));
                docPersonalDtls.setDoctorOtherInfo(otherInfo.getText().toString());
                docPersonalDtls.setModified(true);
                docDtls.setDocPersonalDtls(docPersonalDtls);

                DoctorService services = new DoctorService();
                services.addDoctorPersonalDetails(getActivity(), docDtls);


                Toast.makeText(getActivity(),
                        "Your record is Saved", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
                Log.d(TAG, "updating values in docdtls");
            }
        } else {
            objAlert.showAlertDialog(getActivity(), "No Internet Connection",
                    "check your connection and try again", false);

        }

    }


}
