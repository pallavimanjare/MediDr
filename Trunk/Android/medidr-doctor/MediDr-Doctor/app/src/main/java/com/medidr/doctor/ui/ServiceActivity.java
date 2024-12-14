package com.medidr.doctor.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.medidr.doctor.DoctorLoginActivity;
import com.medidr.doctor.R;
import com.medidr.doctor.model.DataManager;
import com.medidr.doctor.model.DocDtls;
import com.medidr.doctor.model.DocProfileDtls;
import com.medidr.doctor.model.DocServicesDtls;
import com.medidr.doctor.services.DoctorService;
import com.medidr.doctor.services.NetworkUtilService;

import java.util.ArrayList;
import java.util.List;

public class ServiceActivity extends AppCompatActivity {

    private View callButton = null;

    private View bookButton = null;

    private View bothButton = null;

    private View register = null;

    private final Integer CALL = 0;

    private final Integer BOOK = 1;

    private final Integer BOTH = 2;

    private Integer serviceType = -1;

    private String serviceTypeStr = null;
    private CheckBox checkBox;

    TextView banlDtlsLink;

    NetworkUtilService objNetwork = null;
    Boolean isConnectionExist = false;
    ShowAlertDialog objAlert = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_type);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.header_logo);
        /*toolbar.setNavigationIcon(R.mipmap.ic_action_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
*/

        callButton = findViewById(R.id.call_button);

        bookButton = findViewById(R.id.book_button);

        bothButton = findViewById(R.id.both_button);

        register = findViewById(R.id.registerButton);

        checkBox = (CheckBox) findViewById(R.id.checkBox);

        checkBox.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (checkBox.isChecked() == true) {
                                                checkBox.setError(null);
                                            } else {
                                                checkBox.setError("Please accept Terms and conditions");
                                            }
                                        }
                                    });


        banlDtlsLink = (TextView) findViewById(R.id.banlDtlsLink);

        String strbankDtls = banlDtlsLink.getText().toString();
        banlDtlsLink.setText(Html.fromHtml(strbankDtls));
        banlDtlsLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceActivity.this,PaymentDetails.class);
                startActivity(intent);

            }
        });
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceType = CALL;
                serviceTypeStr = "CALL";
                callButton.setBackgroundResource(R.mipmap.call_selected_copy);
                bookButton.setBackgroundResource(R.mipmap.book);
                bothButton.setBackgroundResource(R.mipmap.both);

            }
        });

        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceType = BOOK;
                serviceTypeStr = "BOOK";
                callButton.setBackgroundResource(R.mipmap.call);
                bookButton.setBackgroundResource(R.mipmap.book_selected_copy);
                bothButton.setBackgroundResource(R.mipmap.both);
            }
        });

        bothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceType = BOTH;
                serviceTypeStr = "BOTH";
                callButton.setBackgroundResource(R.mipmap.call);
                bookButton.setBackgroundResource(R.mipmap.book);
                bothButton.setBackgroundResource(R.mipmap.both_selected_copy);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }


    public  void setServiceTypeInformation()
    {

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

        }
        return false;
    }
    private void register() {

        objNetwork = new NetworkUtilService(ServiceActivity.this);
        objAlert = new ShowAlertDialog();
        isConnectionExist = objNetwork.checkMobileInternetConn();
        if (isConnectionExist) {

            if(checkBox.isChecked() == false)
            {
                checkBox.setError("Please accept Terms and conditions");
            }
            else {
                DocProfileDtls profileDtls = DataManager.getDataManager().getProfileDtls();
                if (profileDtls == null) {
                    profileDtls = new DocProfileDtls();
                    profileDtls.setDoctorProfileSettingsId(0L);
                }
                profileDtls.setServiceFlag(serviceType);
                DocDtls docDtls = DataManager.getDataManager().getDoctorDetails();
                docDtls.setDocProfileDtls(profileDtls);
                DoctorService service = new DoctorService();
                service.addServiceMode(getApplicationContext(), docDtls, serviceTypeStr);

                Intent intent = new Intent(ServiceActivity.this, login.class);
                startActivity(intent);
            }
        }
        else {
            objAlert.showAlertDialog(ServiceActivity.this, "No Internet Connection",
                    "check your connection and try again", false);
        }
    }
}
