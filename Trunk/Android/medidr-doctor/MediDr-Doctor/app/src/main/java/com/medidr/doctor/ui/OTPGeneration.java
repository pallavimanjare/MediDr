package com.medidr.doctor.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.medidr.doctor.services.DoctorService;
import com.medidr.doctor.ui.validation.UiValidation;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class OTPGeneration extends AppCompatActivity {

    Button btnsubmit;
    EditText edotp;
    TextView errorMsg;
    TextView lblotp;
    TextView lblotp1;

    AuthDtls authDtls = new AuthDtls();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpgeneration);

        authDtls = DataManager.getDataManager().getAuthDetails();
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

        btnsubmit = (Button) findViewById(R.id.btnsubmit);
        edotp = (EditText) findViewById(R.id.edotp);
        errorMsg = (TextView) findViewById(R.id.errorMsg);
        lblotp = (TextView) findViewById(R.id.lblotp);
        lblotp1 = (TextView) findViewById(R.id.lblotp1);

     //   edotp.setText(authDtls.getOTP());
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!checkRequiredValidation()) {

                } else {


                        authDtls.setOTP(edotp.getText().toString());
                        LongRunningValidateOTP restInvokeTask = new LongRunningValidateOTP(authDtls);
                        restInvokeTask
                                .setLongRunningOTPGeneration(OTPGeneration.this);
                        restInvokeTask.execute();

                }
            }
        });

    }

    public void showErrorMessage(String ErrorMsg) {

        TextView errorMsg = (TextView) findViewById(R.id.errorMsg);
        errorMsg.setText(ErrorMsg);
    }
    private boolean checkRequiredValidation() {
        boolean ret = true;
        edotp = (EditText) findViewById(R.id.edotp);
        if (!UiValidation.hasText(edotp))
            ret = false;

        return ret;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

        }
        return false;
    }

}


class LongRunningValidateOTP extends AsyncTask<Void, Void, String> {

    private static final String TAG = "LogMessages";
    private OTPGeneration otpGeneration;

    private AuthDtls authDtls;
    private String mobilenumber = null;
    private String OTP = null;


    public ProgressDialog dialog;

    @Override
    protected void onPreExecute() {

        dialog = new ProgressDialog(this.otpGeneration);
        dialog.setCancelable(true);
        dialog.setMessage("please wait...");
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);

    }

    public LongRunningValidateOTP(AuthDtls authDtls) {
        this.authDtls = authDtls;
    }

    public void setLongRunningOTPGeneration(
            OTPGeneration otpGeneration) {
        this.otpGeneration = otpGeneration;
    }

    @Override
    protected String doInBackground(Void... params) {
        // TODO Auto-generated method stub

        StringBuffer result = new StringBuffer();
        String response = null;
        try {
            DoctorService objDoctorService = new DoctorService();

            response = objDoctorService.getValidateOTP(authDtls);


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

                String responseResult = results.toString();

                JSONObject jsonResponse = new JSONObject(responseResult);

                String header = jsonResponse.getString("header");
                Log.d(TAG, "header as :" + header);
                JSONObject jsonObjectHeader = new JSONObject(header);
                String statusCode = jsonObjectHeader.getString("statusCode");
                String statusMessage = jsonObjectHeader.getString("statusMessage");

                if (!statusMessage.equalsIgnoreCase("SUCCESS") && statusCode.equalsIgnoreCase("500")) {
                    otpGeneration.showErrorMessage(statusMessage);
                    dialog.dismiss();
                } else if (statusMessage.equalsIgnoreCase("SUCCESS") && statusCode.equalsIgnoreCase("200")) {

                    JSONObject data = jsonResponse.getJSONObject("data");
                    ObjectMapper objectMapper = new ObjectMapper();
                    AuthDtls persistedAuthDtls = objectMapper.readValue(data.toString(), AuthDtls.class);
                    DataManager.getDataManager().getInstance();
                    DataManager.getDataManager().setAuthDetails(persistedAuthDtls);
                    Toast.makeText(otpGeneration,
                            "You OTP is save.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this.otpGeneration, ResetPasswordActivity.class);
                    this.otpGeneration.startActivity(intent);
                    dialog.dismiss();
                }


            }

        } catch (Exception exception) {
            Log.d(TAG, " Exception in moving to login activity ..... "
                    + exception.getMessage());
        }


    }
}
