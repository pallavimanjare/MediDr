package com.medidr.doctor.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medidr.doctor.R;
import com.medidr.doctor.model.DataManager;
import com.medidr.doctor.model.DocDtls;
import com.medidr.doctor.model.DocProfileDtls;
import com.medidr.doctor.model.ServiceTypeChrgsDtls;
import com.medidr.doctor.services.DoctorService;
import com.medidr.doctor.services.NetworkUtilService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PaymentDetails extends AppCompatActivity {

    TextView paybottomlbl;
    NetworkUtilService objNetwork = null;
    Boolean isConnectionExist = false;
    ShowAlertDialog objAlert = null;

    List<ServiceTypeChrgsDtls> serviceChargs = new ArrayList<>();
    TextView bothhead,bookhead,callhead;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.header_logo);
        toolbar.setNavigationIcon(R.mipmap.close_button_copy);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(PaymentDetails.this, ServiceActivity.class);
                startActivity(intent);*/
                finish();
            }
        });
        paybottomlbl = (TextView) findViewById(R.id.paybottomlbl);
        String txttv=paybottomlbl.getText().toString();
        paybottomlbl.setText(Html.fromHtml(txttv));

        bothhead = (TextView) findViewById(R.id.bothhead);
        bookhead = (TextView) findViewById(R.id.bookhead);
        callhead = (TextView) findViewById(R.id.callhead);

        setMedidrServiceTypeChargesDetails();

    }

    public void setMedidrServiceTypeChargesDetails() {


        objNetwork = new NetworkUtilService(PaymentDetails.this);
        objAlert = new ShowAlertDialog();
        isConnectionExist = objNetwork.checkMobileInternetConn();
        if (isConnectionExist) {

            DoctorService service = new DoctorService();

            String path = "/doctors/getMedidrServiceTypeCharges";
            String responseResult = service.getInformationForDoctor(path);

            if (!responseResult.equalsIgnoreCase("") && responseResult != null) {

                JSONObject jsonObject = null;
                try {
                    JSONObject jsonResponse = new JSONObject(responseResult);
                    String datastr = jsonResponse.getString("data");
                   JSONArray data = jsonResponse.getJSONArray("data");
                    String header = jsonResponse.getString("header");


                    JSONObject jsonObjectHeader = new JSONObject(header);
                    String statusCode = jsonObjectHeader.getString("statusCode");
                    String statusMessage = jsonObjectHeader.getString("statusMessage");
                    if (statusMessage.equalsIgnoreCase("SUCCESS") && statusCode.equalsIgnoreCase("200")) {
                        try {
                            JSONArray serviceChargesData = jsonResponse.getJSONArray("data");

                            ObjectMapper mapper = new ObjectMapper();
                            try {
                                serviceChargs = mapper.readValue(serviceChargesData.toString(), new TypeReference<List<ServiceTypeChrgsDtls>>() {
                                });

                                for(ServiceTypeChrgsDtls serviceCharges: serviceChargs) {
                                    if(serviceCharges.getServiceType() == 0) {
                                        callhead.setText("" + (int)serviceCharges.getServiceCharge() + "/- for One Year");
                                    }
                                    if(serviceCharges.getServiceType() == 1) {
                                        bookhead.setText("" + (int)serviceCharges.getServiceCharge() + "/- for One Year");
                                    }
                                    if(serviceCharges.getServiceType() == 2) {
                                        bothhead.setText("" + (int)serviceCharges.getServiceCharge() + "/- for One Year");
                                    }

                                }
                            } catch (Exception exception) {
                                Log.d("TAG", "Exception in processing the data");
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } else {
            objAlert.showAlertDialog(PaymentDetails.this, "No Internet Connection",
                    "check your connection and try again", false);
        }
    }


}
