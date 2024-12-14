package com.medidr.doctor.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.medidr.doctor.R;
import com.medidr.doctor.model.AuthDtls;
import com.medidr.doctor.model.DataManager;
import com.medidr.doctor.model.DocDtls;
import com.medidr.doctor.model.DocPersonalDtls;
import com.medidr.doctor.model.DocProfileDtls;
import com.medidr.doctor.services.DoctorService;
import com.medidr.doctor.services.NetworkUtilService;
import com.medidr.doctor.ui.validation.UiValidation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Register Activity";

    private RelativeLayout layoutParams = null;
boolean isSpecilaityHasError = false;
String []strSpecilaityArr = null;

    private EditText doctorName, mobile, email, qualification, experience, consultaionFees, otherInfo;
    private AutoCompleteTextView specialty;
    private View btnNext = null;
    JSONArray specialitiesArr = null;
    private Context context;
    private TextView errorMsg;

    NetworkUtilService objNetwork = null;
    Boolean isConnectionExist = false;
    ShowAlertDialog objAlert = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initUI();

    }

    private void initUI() {
        Log.i(TAG, "initUI()");
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.header_logo);
       /* toolbar.setNavigationIcon(R.mipmap.ic_action_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/

        errorMsg = (TextView) findViewById(R.id.errorMsg);

        doctorName = (EditText) findViewById(R.id.doctor_name);

        mobile = (EditText) findViewById(R.id.mobile);

        email = (EditText) findViewById(R.id.email);

        specialty = (AutoCompleteTextView) findViewById(R.id.specialty);

        qualification = (EditText) findViewById(R.id.qualification);

        experience = (EditText) findViewById(R.id.experience);

        consultaionFees = (EditText) findViewById(R.id.consultationFees);

        otherInfo = (EditText) findViewById(R.id.other_info);

        btnNext = findViewById(R.id.next_button);

        btnNext.setOnClickListener(this);

        errorMsg.setText(null);
        AuthDtls authDetails = DataManager.getInstance().getAuthDetails();
        mobile.setText(authDetails.getMobile());
        mobile.setEnabled(false);

        //get speciality name
//getInformationForDoctor

        getSpecilaityForDoctor();

        specialty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    // on focus off
                    String str = specialty.getText().toString();

                    ListAdapter listAdapter = specialty.getAdapter();
                    for (int i = 0; i < listAdapter.getCount(); i++) {
                        String temp = listAdapter.getItem(i).toString();
                        if (str.compareTo(temp) == 0) {
                            Log.d(TAG,"get matched");
                           // isSpecilaityHasError = false;
                            return;
                        }
                    }
                    specialty.setError("Please select speciality");
                   // isSpecilaityHasError = true;


                }
            }
        });
        ///done with specilaity

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

        }
        return false;
    }
    public void getSpecilaityForDoctor() {
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
                ArrayAdapter<String> specialityAdapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, specialityArr);
                specialty.setAdapter(specialityAdapter);
                specialty.setThreshold(2);

            }


            Log.d(TAG, " After RESTful service response =================>"
                    + result);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_button:
              //  if (checkFields())
                if (!checkRequiredValidation()) {

                }
               else if (!checkRegxValidation()) {

                }
                else if(!checkCorrectSpecialitySelected())
                {
                    specialty.setError("Please select speciality");
                }

                else {
                    registerDoctor();
                }
                break;

            default:
                break;
        }
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
  /*  private boolean checkCorrectSpecialitySelected()
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
    }*/
    private void registerDoctor() {
        objNetwork = new NetworkUtilService(this);
        objAlert = new ShowAlertDialog();
        isConnectionExist = objNetwork.checkMobileInternetConn();
        Log.d(TAG, "internet is" + isConnectionExist);
        if (isConnectionExist) {
            //  btnNext.setEnabled(false);
            Log.i(TAG, "Register doc " + DataManager.getInstance().getAuthDetails());
            DocPersonalDtls personalDetails = new DocPersonalDtls();

            personalDetails.setFullName(doctorName.getText().toString());

            //personalDetails.setMobile(mobile.getText().toString());

            //personalDetails.setEmail(email.getText().toString());

            personalDetails.setSpeciality(specialty.getText().toString());

            personalDetails.setQualification(qualification.getText().toString());

            personalDetails.setExperience(Integer.valueOf(experience.getText().toString()));

            personalDetails.setConsultationFees(Integer.valueOf(consultaionFees.getText().toString()));

            personalDetails.setDoctorOtherInfo(otherInfo.getText().toString());

            DataManager.getInstance().setPersonalDetails(personalDetails);

            DocDtls details = new DocDtls();
            AuthDtls authDetails = DataManager.getInstance().getAuthDetails();
            authDetails.setEmail(email.getText().toString());
            authDetails.setMobile(mobile.getText().toString());
            details.setAuthDtls(authDetails);

            details.setDocPersonalDtls(personalDetails);
            DocProfileDtls profileDtls = new DocProfileDtls();
            profileDtls.setDoctorId(0L);
            details.setDocProfileDtls(profileDtls);

            Log.i(TAG, "Doc Details ===" + details);

            DoctorService services = new DoctorService();
            services.addDoctorPersonalDetails(this, details);

             Toast.makeText(getApplicationContext(),
                                    "Your record is Saved", Toast.LENGTH_LONG).show();
            // Correct flow
         Intent intent = new Intent(this, UplaodImageActivity.class);
           startActivity(intent);

       /*     Intent intent = new Intent(this, WeeklySchedule.class);
            this.startActivity(intent);*/
        } else {
            objAlert.showAlertDialog(RegisterActivity.this, "No Internet Connection",
                    "check your connection and try again", false);
        }
    }

    private boolean checkRequiredValidation() {
        boolean ret = true;

        if (!UiValidation.hasText(doctorName))
            ret = false;

        if(!UiValidation.hasText(mobile))
            ret = false;

        if(!UiValidation.hasText(email))
            ret = false;
        if (!UiValidation.hasText(specialty))
            ret = false;

        if(!UiValidation.hasText(qualification))
            ret = false;

        if(!UiValidation.hasText(experience))
            ret = false;

        if(!UiValidation.hasText(consultaionFees))
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
}
