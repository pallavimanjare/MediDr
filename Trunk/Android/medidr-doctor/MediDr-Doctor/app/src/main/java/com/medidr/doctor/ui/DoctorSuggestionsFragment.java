package com.medidr.doctor.ui;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.medidr.doctor.R;
import com.medidr.doctor.model.DataManager;
import com.medidr.doctor.model.DocDtls;
import com.medidr.doctor.model.Message;
import com.medidr.doctor.services.DoctorService;
import com.medidr.doctor.services.NetworkUtilService;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorSuggestionsFragment extends Fragment {


    private static final String TAG = "LogMessages";
    private EditText edMessage;
    private Button btnSend;
    private Button btncall;
    DocDtls docDtls = null;

    Message message = null;
    NetworkUtilService objNetwork = null;
    Boolean isConnectionExist = false;
    ShowAlertDialog objAlert = null;

    public DoctorSuggestionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_doctor_suggestions, container, false);

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
                Intent intent = new Intent(getActivity(),HomeActivity.class);
                startActivity(intent);
            }
        });
        toolbar.setTitle("Suggestions");
        docDtls = DataManager.getDataManager().getDoctorDetails();
        edMessage = (EditText) v.findViewById(R.id.edMessage);
        btnSend = (Button) v.findViewById(R.id.btnSend);
        btncall = (Button) v.findViewById(R.id.btncall);

        this.btnSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                objNetwork = new NetworkUtilService(getActivity());
                objAlert = new ShowAlertDialog();
                isConnectionExist = objNetwork.checkMobileInternetConn();
                if (isConnectionExist) {
                    try {
                        Object is = null;
                        StringBuffer result = new StringBuffer();
                        // JSONObject messageDtls = new JSONObject();


                        Date e = new Date();
                        Message messageDtls = new Message();
                        messageDtls.setSenderId(docDtls.getAuthDtls().getUserId());
                        messageDtls.setMsgText(edMessage.getText().toString());
                        messageDtls.setSenderType("DOCTOR");
                        messageDtls.setMsgTime(e);
                        messageDtls.setMsgType("SUGGESTION");

                        Log.d("LogMessages", "messageDtls json has created as :" + messageDtls.toString());
                        DoctorService service = new DoctorService();
                        String responsreResult = service.sendMessageNew(messageDtls);
                        if (responsreResult != null) {
                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            startActivity(intent);
                        }

                    } catch (Exception var14) {
                        var14.printStackTrace();
                        Log.d("LogMessages", " Exception in RESTful service response =================>" + var14.getMessage());
                    }
                } else {
                    objAlert.showAlertDialog(getActivity(), "No Internet Connection",
                            "check your connection and try again", false);
                }


            }


        });

        this.btncall.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.CALL");
                String contactNumber = "9146236467";
                intent.setData(Uri.parse("tel:" + contactNumber));
                if (ActivityCompat.checkSelfPermission(DoctorSuggestionsFragment.this.getActivity(), "android.permission.CALL_PHONE") == 0) {
                    DoctorSuggestionsFragment.this.getActivity().startActivity(intent);
                }
            }
        });
        return v;
    }

}
