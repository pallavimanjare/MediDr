package com.medidr.doctor.ui;

import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class ResetPasswordActivity extends AppCompatActivity {

    EditText edpassword;
    EditText edretypepassword;
    Button btnresetpassword;
    String userType = null;
    String mobile = null;
    String otp = null;
    TextView lblresetpassword;
    TextView lblsubheaderresetpassword;

    AuthDtls authDtls = new AuthDtls();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

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

        authDtls = DataManager.getDataManager().getAuthDetails();

        this.edpassword = (EditText) this.findViewById(R.id.edpassword);
        this.edretypepassword = (EditText) this.findViewById(R.id.edretypepassword);
        this.btnresetpassword = (Button) this.findViewById(R.id.btnresetpassword);
        this.lblresetpassword = (TextView) this.findViewById(R.id.lblresetpassword);
        this.lblsubheaderresetpassword = (TextView) this.findViewById(R.id.lblsubheaderresetpassword);

        btnresetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!checkRequiredValidation()) {

                }
                else if (edpassword.length() < 4) {
                    edpassword.setError("Enter altleast 4 digit password");

                }
                else if (!edpassword.getText().toString()
                        .equals(edretypepassword.getText().toString())) {
                    edretypepassword.setError("Password and Reset Password do not match.");

                }
                else
                {
                       authDtls.setPassword(edpassword.getText().toString());
                        LongRunningResetPassword restInvokeTask = new LongRunningResetPassword(authDtls);
                        restInvokeTask
                                .setLongRunningResetPassword(ResetPasswordActivity.this);
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

        if (!UiValidation.hasText(this.edpassword))
            ret = false;
        if (!UiValidation.hasText(this.edretypepassword))
            ret = false;

        return ret;
    }


}



class LongRunningResetPassword extends AsyncTask<Void, Void, String> {

    private static final String TAG = "LogMessages";
    private ResetPasswordActivity resetPasswordActivity;
    AuthDtls authDtls;
    public ProgressDialog dialog;

    public LongRunningResetPassword(AuthDtls authDtls) {
        this.authDtls = authDtls;

    }

    public void setLongRunningResetPassword(
            ResetPasswordActivity resetPasswordActivity) {
        this.resetPasswordActivity = resetPasswordActivity;
    }


    @Override
    protected void onPreExecute() {

        dialog = new ProgressDialog(this.resetPasswordActivity);
        dialog.setCancelable(true);
        dialog.setMessage("please wait...");
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);

    }

    @Override
    protected String doInBackground(Void... params) {
        // TODO Auto-generated method stub
        StringBuffer result = new StringBuffer();
        String response = null;
        try {
            DoctorService objDoctorService = new DoctorService();
            response = objDoctorService.resetPassword(authDtls);


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

                String responseResult = results.toString();

                JSONObject jsonResponse = new JSONObject(responseResult);

                String header = jsonResponse.getString("header");
                Log.d(TAG,"header as :"+header);
                JSONObject jsonObjectHeader = new JSONObject(header);
                String statusCode = jsonObjectHeader.getString("statusCode");
                String statusMessage = jsonObjectHeader.getString("statusMessage");

                if(statusMessage.equalsIgnoreCase("SUCCESS") && statusCode.equalsIgnoreCase("200"))
                {
                    Toast.makeText(resetPasswordActivity,
                            "Your password is changed successfully...", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this.resetPasswordActivity,login.class);
                    resetPasswordActivity.startActivity(intent);
                    dialog.dismiss();


                }
                else if(!statusMessage.equalsIgnoreCase("SUCCESS") && statusCode.equalsIgnoreCase("200"))
                {
                    dialog.dismiss();

                }


            }

        } catch (Exception exception) {
            Log.d(TAG, " Exception in moving to login activity ..... "
                    + exception.getMessage());
        }
    }

}
