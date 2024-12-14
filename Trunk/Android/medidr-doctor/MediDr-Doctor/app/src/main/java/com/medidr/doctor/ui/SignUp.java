package com.medidr.doctor.ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medidr.doctor.R;
import com.medidr.doctor.gcm.QuickstartPreferences;
import com.medidr.doctor.gcm.RegistrationIntentService;
import com.medidr.doctor.model.AuthDtls;
import com.medidr.doctor.model.DataManager;
import com.medidr.doctor.model.DocDtls;
import com.medidr.doctor.model.Login;
import com.medidr.doctor.services.DoctorService;
import com.medidr.doctor.services.NetworkUtilService;
import com.medidr.doctor.ui.validation.UiValidation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SignUp extends AppCompatActivity {

    private EditText mobile,password,rePassword;

    private View signUp,signIn;

    private TextView errorMsg;

    NetworkUtilService objNetwork = null;
    Boolean isConnectionExist = false;
    ShowAlertDialog objAlert = null;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;

    public ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        initUI();

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
//                mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
//                    mInformationTextView.setText(getString(R.string.gcm_send_message));
                } else {
//                    mInformationTextView.setText(getString(R.string.token_error_message));
                }
            }
        };

        // Registering BroadcastReceiver
        registerReceiver();

        // Start IntentService to register this application with GCM.
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);


    }

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }
    private void initUI(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.header_logo);
        toolbar.setNavigationIcon(R.mipmap.ic_action_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // redirectLoginScreen();
                finish();
            }
        });

        signIn = findViewById(R.id.sign_in_button);
        signUp = findViewById(R.id.sign_up_button);
        mobile = (EditText) findViewById(R.id.sign_up_mobile);
        password = (EditText) findViewById(R.id.sign_up_password);
        rePassword = (EditText) findViewById(R.id.sign_up_re_pass);

        errorMsg = (TextView) findViewById(R.id.errorMsg);
        errorMsg.setText(null);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectLoginScreen();
            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new ProgressDialog(SignUp.this);
                dialog.setCancelable(true);
                dialog.setMessage("please wait...");
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
               /* if (checkFields()) {
                    signUp();
                }*/
                if (!checkRequiredValidation()) {
                    dialog.dismiss();
                } else if (password.length() < 4) {
                    dialog.dismiss();
                    password.setError("Enter altleast 4 digit password");

                } else if (!password.getText().toString()
                        .equals(rePassword.getText().toString())) {
                    dialog.dismiss();
                    rePassword.setError("Password is not matching.");

                } else if (!checkRegxValidation()) {
                    dialog.dismiss();
                } else {
                    signUp();
                }

            }
        });
    }

private  void redirectLoginScreen()
{
    Intent intent = new Intent(getApplicationContext(), login.class);
    startActivity(intent);

}
    private void signUp(){
        objNetwork = new NetworkUtilService(this);
        objAlert = new ShowAlertDialog();
        isConnectionExist = objNetwork.checkMobileInternetConn();

        if (isConnectionExist) {
         /*   String mobileNumber = mobile.getText().toString();
            String docPassword = password.getText().toString();
            AuthDtls authDetails = new AuthDtls();
            authDetails.setMobile(mobileNumber);
            authDetails.setPassword(docPassword);
            authDetails.setUserType("DOCTOR");
            DataManager.getInstance().setAuthDetails(authDetails);*/
            authenticate();
            //to do check doctor exist if no then only allow doctor to procced further

           /* Intent intent = new Intent(SignUp.this, RegisterActivity.class);
            startActivity(intent);*/
            /* Intent intent = new Intent(SignUp.this, UplaodImageActivity.class);
            startActivity(intent);*/

        }
        else {
            dialog.dismiss();
            objAlert.showAlertDialog(SignUp.this, "No Internet Connection",
                    "check your connection and try again", false);
        }
    }

    private void authenticate() {

        objNetwork = new NetworkUtilService(this);
        objAlert = new ShowAlertDialog();
        isConnectionExist = objNetwork.checkMobileInternetConn();

        if (isConnectionExist) {
            String mobileNumber = mobile.getText().toString();
            String docPassword = password.getText().toString();


                DoctorService service = new DoctorService();
                String responseResult = service.authenticateDoctor(mobileNumber);
                if (!responseResult.equalsIgnoreCase("") && responseResult != null) {

                    JSONObject jsonObject = null;
                    try {
                        JSONObject jsonResponse = new JSONObject(responseResult);
                        String header = jsonResponse.getString("header");


                        JSONObject jsonObjectHeader = new JSONObject(header);
                        String statusCode = jsonObjectHeader.getString("statusCode");
                        String statusMessage = jsonObjectHeader.getString("statusMessage");

                        if (statusMessage.equalsIgnoreCase("SUCCESS") && statusCode.equalsIgnoreCase("200")) {

                            AuthDtls authDetails = new AuthDtls();
                            authDetails.setMobile(mobileNumber);
                            authDetails.setPassword(docPassword);
                            authDetails.setUserType("DOCTOR");
                            DataManager.getInstance().setAuthDetails(authDetails);
                            Toast.makeText(getApplicationContext(),
                                    "Your record is Saved.", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(SignUp.this, RegisterActivity.class);
                            startActivity(intent);
                            dialog.dismiss();
                        }
                        else
                        {
                            dialog.dismiss();
                            errorMsg.setText("Mobile number is exist in MediDr Record");
                           /* Toast.makeText(getApplicationContext(),
                                    "Mobile number is exist in MediDr Record", Toast.LENGTH_LONG).show();*/

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }



        }
        else {
            dialog.dismiss();
            objAlert.showAlertDialog(SignUp.this, "No Internet Connection",
                    "check your connection and try again", false);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

        }
        return false;
    }

    private boolean checkRequiredValidation() {
        boolean ret = true;

        mobile = (EditText) findViewById(R.id.sign_up_mobile);
        password = (EditText) findViewById(R.id.sign_up_password);
        rePassword = (EditText) findViewById(R.id.sign_up_re_pass);

        if (!UiValidation.hasText(mobile))
            ret = false;

        if(!UiValidation.hasText(password))
            ret = false;

        if(!UiValidation.hasText(rePassword))
            ret = false;
        return ret;
    }

    private boolean checkRegxValidation() {
        boolean ret = true;
        mobile = (EditText) findViewById(R.id.sign_up_mobile);
        if (!UiValidation.isValidMobileNumber(mobile))
            ret = false;

        return ret;

    }
    /*private Boolean checkFields(){

        String mobileNumber = mobile.getText().toString();
        String docPassword = password.getText().toString();

        if(mobileNumber.length() == 0 || mobileNumber.trim().length() ==0){
            mobile.requestFocus();
            String errorMsg = "Please provide mobile number!!!";
            mobile.setError(errorMsg);
            return Boolean.FALSE;
        }
        if(docPassword.length() == 0 || docPassword.trim().length() ==0){
            password.requestFocus();
            String errorMsg = "Please provide password!!!";
            password.setError(errorMsg);
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }*/


}
