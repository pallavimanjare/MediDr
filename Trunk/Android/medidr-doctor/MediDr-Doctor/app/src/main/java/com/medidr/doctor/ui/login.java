package com.medidr.doctor.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.medidr.doctor.R;
import com.medidr.doctor.model.DataManager;
import com.medidr.doctor.model.DocDtls;
import com.medidr.doctor.model.Login;
import com.medidr.doctor.services.DoctorService;
import com.medidr.doctor.services.NetworkUtilService;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import android.provider.Settings;
public class login extends AppCompatActivity {

    private static final String TAG = "Login Activity";

    private View registerButton = null;

    private View signUpButton = null;

    private View forgotPassword = null;

    private EditText mobileNo, password;
    private TextView errorMsg;

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    private Boolean isBack;

    private CheckBox chkRemember;

    NetworkUtilService objNetwork = null;
    Boolean isConnectionExist = false;
    ShowAlertDialog objAlert = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.header_logo);
        toolbar.setTitle("MediDr - Doctor");

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //Play Services is not installed/enabled
                GooglePlayServicesUtil.showErrorNotification(resultCode, this);
            } else {
                //This device does not support Play Services
            }
        }


        //apply write permission
        if (Build.VERSION.SDK_INT > 22) {
            isStoragePermissionGranted();
        }
        //done with write permission

        registerButton = findViewById(R.id.register_doctor);
        mobileNo = (EditText) findViewById(R.id.mobile);
        password = (EditText) findViewById(R.id.password);
        chkRemember = (CheckBox) findViewById(R.id.chkRemember);
        errorMsg = (TextView) findViewById(R.id.errorMsg);
        errorMsg.setText(null);
        errorMsg.setVisibility(View.GONE);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        isBack = loginPreferences.getBoolean("isBack", false);
        if(isBack == true)
        {
            String strMobile = loginPreferences.getString("mobile", "");
            String strPassword = loginPreferences.getString("password", "");
            mobileNo.setText(strMobile);
            password.setText(strPassword);
             chkRemember.setChecked(true);
             authenticate();
            //TO DO redirect to home screen
        }
        if (saveLogin == true) {
            String strMobile = loginPreferences.getString("mobile", "");
            String strPassword = loginPreferences.getString("password", "");
            mobileNo.setText(strMobile);
            password.setText(strPassword);
           // chkRemember.setChecked(true);
           // authenticate();
            //TO DO redirect to home screen

        }
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);

            }
        });

        signUpButton = findViewById(R.id.sign_up);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFields()) {

                    authenticate();
                }


            }
        });


        forgotPassword = findViewById(R.id.forgot_password_login);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent);

            }
        });
        if (Build.VERSION.SDK_INT <= 22) {
            setAutoOrientationEnabled(getApplicationContext(), false);
        }

    }
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CALL_PHONE}, 1);
/*            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {
                objAlert.showAlertDialog(this, "Title", "Hey", true);
                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }*/
        }
        return true;
    }

    @Override
    public void onBackPressed()
    {
        // code here to show dialog
         // optional depending on your needs
    }
    public static void setAutoOrientationEnabled(Context context, boolean enabled)
    {
        Settings.System.putInt( context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, enabled ? 1 : 0);
    }

    private void authenticate() {
        Log.i(TAG, "authenticate: ");
        objNetwork = new NetworkUtilService(this);
        objAlert = new ShowAlertDialog();
        isConnectionExist = objNetwork.checkMobileInternetConn();
        Log.d(TAG, "internet is" + isConnectionExist);
        if (isConnectionExist) {
            String mobileNumber = mobileNo.getText().toString();
            String docPassword = password.getText().toString();

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mobileNo.getWindowToken(), 0);

            if (mobileNumber.length() != 0 && password.length() != 0) {
                if (chkRemember.isChecked()) {
                    loginPrefsEditor.putBoolean("saveLogin", true);
                    loginPrefsEditor.putString("mobile", mobileNumber);
                    loginPrefsEditor.putString("password", docPassword);
                    loginPrefsEditor.commit();
                } else {
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                }

                Login login = new Login();
                login.setMobile(mobileNumber);
                login.setPassword(docPassword);
                login.setUser_type(Login.USER_TYPE);
                DoctorService service = new DoctorService();
                String responseResult = service.login(this, login);
                if (!responseResult.equalsIgnoreCase("") && responseResult != null) {
                    Log.d(TAG, "result is :" + responseResult);
                    JSONObject jsonObject = null;
                    try {
                        JSONObject jsonResponse = new JSONObject(responseResult);
                        String header = jsonResponse.getString("header");
                        Log.d(TAG, "header as :" + header);

                        JSONObject jsonObjectHeader = new JSONObject(header);
                        String statusCode = jsonObjectHeader.getString("statusCode");
                        String statusMessage = jsonObjectHeader.getString("statusMessage");
                        if (!statusMessage.equalsIgnoreCase("SUCCESS") && statusCode.equalsIgnoreCase("200")) {
                            errorMsg.setText(statusMessage);
                            errorMsg.setVisibility(View.VISIBLE);
                        } else if (statusMessage.equalsIgnoreCase("SUCCESS") && statusCode.equalsIgnoreCase("200")) {
                            try {
                                JSONObject data = jsonResponse.getJSONObject("data");
                                ObjectMapper objectMapper = new ObjectMapper();
                                DocDtls persistedDocDtls = objectMapper.readValue(data.toString(), DocDtls.class);
                                DataManager.getDataManager().getInstance();
                                DataManager.getDataManager().setDoctorDetails(persistedDocDtls);
                                Intent intent = new Intent(login.this, HomeActivity.class);
                                startActivity(intent);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (JsonMappingException e) {
                                e.printStackTrace();
                            } catch (JsonParseException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        }
        else {
            objAlert.showAlertDialog(login.this, "No Internet Connection",
                    "check your connection and try again", false);
        }
    }

    private Boolean checkFields() {

        String mobileNumber = mobileNo.getText().toString();
        String docPassword = password.getText().toString();

        if (mobileNumber.length() == 0 || mobileNumber.trim().length() == 0) {
            mobileNo.requestFocus();
            String errorMsg = "Please provide mobile number!!!";
            mobileNo.setError(errorMsg);
            return Boolean.FALSE;
        }
        if (docPassword.length() == 0 || docPassword.trim().length() == 0) {
            password.requestFocus();
            String errorMsg = "Please provide password!!!";
            password.setError(errorMsg);
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

}
