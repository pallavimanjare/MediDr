package com.medidr.doctor.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medidr.doctor.R;
import com.medidr.doctor.model.AuthDtls;
import com.medidr.doctor.model.DataManager;
import com.medidr.doctor.model.DocDtls;
import com.medidr.doctor.services.DoctorService;
import com.medidr.doctor.ui.validation.UiValidation;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ForgotPasswordActivity extends AppCompatActivity {

    private static final String TAG = "LogMessages";
    Button btnsubmit;

    String strOtp = null;
    TextView errorMsg;
    EditText edfrgtmobil;
    TextView lblmobileno;
    TextView lblfrgtpswd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.header_logo);
        toolbar.setNavigationIcon(R.mipmap.ic_action_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.btnsubmit = (Button) this.findViewById(R.id.btnsubmit);
        this.errorMsg = (TextView) this.findViewById(R.id.errorMsg);
        this.edfrgtmobil = (EditText) this.findViewById(R.id.edfrgtmobil);
        this.lblmobileno = (TextView) this.findViewById(R.id.lblmobileno);
        this.lblfrgtpswd = (TextView) this.findViewById(R.id.lblfrgtpswd);
        this.errorMsg.setText("");

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!checkRequiredValidation()) {

                } else if (!checkRegxValidation()) {

                } else {

                    AuthDtls authDtls = new AuthDtls();
                    authDtls.setMobile(edfrgtmobil.getText().toString());
                    authDtls.setUserType("DOCTOR");

                    //redirect to reset password screen

                    StringBuffer result = new StringBuffer();
                    authDtls.setUserType("DOCTOR");
                    authDtls.setMobile(edfrgtmobil.getText().toString());

                    LongRunningForgotPassword restInvokeTask = new LongRunningForgotPassword(authDtls);
                    restInvokeTask
                            .setLongRunningForgotPassword(ForgotPasswordActivity.this);
                    restInvokeTask.execute();

                }
            }
        });

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

        }
        return false;
    }


    private boolean checkRequiredValidation() {
        boolean ret = true;
        edfrgtmobil = (EditText) findViewById(R.id.edfrgtmobil);
        if (!UiValidation.hasText(edfrgtmobil))
            ret = false;

        return ret;
    }

    private boolean checkRegxValidation() {
        boolean ret = true;
        edfrgtmobil = (EditText) findViewById(R.id.edfrgtmobil);
        if (!UiValidation.isValidMobileNumber(edfrgtmobil))
            ret = false;
        return ret;

    }
    public void showErrorMessage(String ErrorMsg) {

        TextView errorMsg = (TextView) findViewById(R.id.errorMsg);
        errorMsg.setText(ErrorMsg);
    }

}



class LongRunningForgotPassword extends AsyncTask<Void, Void, String> {

    private static final String TAG = "LogMessages";
    private ForgotPasswordActivity forgotPasswordActivity;

    private AuthDtls authDtls;
    private String  mobilenumber = null;
    private String  OTP = null;


    public ProgressDialog dialog;
    @Override
    protected void onPreExecute() {

        dialog = new ProgressDialog(this.forgotPasswordActivity);
        dialog.setCancelable(true);
        dialog.setMessage("please wait...");
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);

    }
    public LongRunningForgotPassword(AuthDtls authDtls) {
        this.authDtls = authDtls;
    }
    public void setLongRunningForgotPassword(
            ForgotPasswordActivity forgotPasswordActivity) {
        this.forgotPasswordActivity = forgotPasswordActivity;
    }

    @Override
    protected String doInBackground(Void... params) {
        // TODO Auto-generated method stub
        StringBuffer result = new StringBuffer();
        String response = null;
        try {
            DoctorService objDoctorService = new DoctorService();

            response = objDoctorService.getOTPForForgotPassword(authDtls);


        } catch (Exception exception) {
            exception.printStackTrace();
            Log.d(TAG,
                    " Exception in RESTful service response =================>"
                            + exception.getMessage());
        }

        return response.toString();

    }

    @Override
    protected void onPostExecute(String results) {

        Log.d(TAG, " Entered onPostExecute results  =================>"
                + results);

        try {

            if (results != null) {

                Log.d(TAG, "result on forgot screen is" + results);
                String userType = null;
                String password = null;
                String mobile = null;
                String otp = null;
                Long userId = null;
                String responseResult = results.toString();

                JSONObject jsonResponse = new JSONObject(responseResult);

                String header = jsonResponse.getString("header");
                Log.d(TAG,"header as :"+header);
                JSONObject jsonObjectHeader = new JSONObject(header);
                String statusCode = jsonObjectHeader.getString("statusCode");
                String statusMessage = jsonObjectHeader.getString("statusMessage");

                if(!statusMessage.equalsIgnoreCase("SUCCESS") && statusCode.equalsIgnoreCase("200"))
                {
                    forgotPasswordActivity.showErrorMessage(statusMessage);
                    dialog.dismiss();
                }
                else if(statusMessage.equalsIgnoreCase("SUCCESS") && statusCode.equalsIgnoreCase("200"))
                {

                    JSONObject data = jsonResponse.getJSONObject("data");
                    ObjectMapper objectMapper = new ObjectMapper();
                    AuthDtls persistedAuthDtls = objectMapper.readValue(data.toString(), AuthDtls.class);
                    DataManager.getDataManager().getInstance();
                    DataManager.getDataManager().setAuthDetails(persistedAuthDtls);
                    Intent intent = new Intent(this.forgotPasswordActivity, OTPGeneration.class);
                    this.forgotPasswordActivity.startActivity(intent);
                    Toast.makeText(forgotPasswordActivity,
                            "OTP sent as SMS on you mobile", Toast.LENGTH_LONG).show();
                    dialog.dismiss();

                }


            }

        } catch (Exception exception) {
            Log.d(TAG, " Exception in moving to login activity ..... "
                    + exception.getMessage());
        }


    }

}
/*

}*/


