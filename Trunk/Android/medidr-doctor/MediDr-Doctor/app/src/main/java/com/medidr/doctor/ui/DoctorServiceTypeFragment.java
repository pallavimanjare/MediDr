package com.medidr.doctor.ui;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medidr.doctor.R;
import com.medidr.doctor.model.DataManager;
import com.medidr.doctor.model.DocDtls;
import com.medidr.doctor.model.DocHospitalDtls;
import com.medidr.doctor.model.DocProfileDtls;
import com.medidr.doctor.model.ImageMasterDtls;
import com.medidr.doctor.services.DoctorService;
import com.medidr.doctor.services.NetworkUtilService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorServiceTypeFragment extends Fragment {

    private CheckBox checkBox;

    private View callButton = null;

    private View bookButton = null;

    private View bothButton = null;

    private View register = null;

    private final Integer CALL = 0;

    private final Integer BOOK = 1;

    private final Integer BOTH = 2;

    private Integer serviceType = -1;

    private String serviceTypeStr = null;

    TextView txtbankdtls;

    NetworkUtilService objNetwork = null;
    Boolean isConnectionExist = false;
    ShowAlertDialog objAlert = null;

    DocDtls docDtls = null;
    int serviceFlag = 0;

    public DoctorServiceTypeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_doctor_service_type, container, false);

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
        toolbar.setTitle("Edit Service Types");
        docDtls = DataManager.getDataManager().getDoctorDetails();

        getDoctorProfileInformation();
        checkBox = (CheckBox) v.findViewById(R.id.checkBox);
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

        txtbankdtls = (TextView) v.findViewById(R.id.txtbankdtls);
        callButton = (Button) v.findViewById(R.id.call_button);

        bookButton =(Button) v.findViewById(R.id.book_button);

        bothButton =(Button) v.findViewById(R.id.both_button);

        register =(ImageButton) v.findViewById(R.id.registerButton);

        serviceFlag = docDtls.getDocProfileDtls().getServiceFlag();

        String bankDtls = txtbankdtls.getText().toString();
        txtbankdtls.setText(Html.fromHtml(bankDtls));

        txtbankdtls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PaymentDetails.class);
                startActivity(intent);

            }
        });
        if(serviceFlag == 0)
        {
            callButton.setBackgroundResource(R.mipmap.call_selected_copy);
        }
        else if(serviceFlag == 1)
        {
            bookButton.setBackgroundResource(R.mipmap.book_selected_copy);
        }
        else if(serviceFlag == 2)
        {
            bothButton.setBackgroundResource(R.mipmap.both_selected_copy);
        }

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
        return v;
    }

    public void getDoctorProfileInformation() {


        objNetwork = new NetworkUtilService(getActivity());
        objAlert = new ShowAlertDialog();
        isConnectionExist = objNetwork.checkMobileInternetConn();
        if (isConnectionExist) {

            DoctorService service = new DoctorService();
            Long doctorId = docDtls.getAuthDtls().getUserId();
            String path = "/doctors/getDoctorProfileDetails/" + doctorId;
            String responseResult = service.getInformationForDoctor(path);

            if (!responseResult.equalsIgnoreCase("") && responseResult != null) {

                JSONObject jsonObject = null;
                try {
                    JSONObject jsonResponse = new JSONObject(responseResult);
                    String header = jsonResponse.getString("header");


                    JSONObject jsonObjectHeader = new JSONObject(header);
                    String statusCode = jsonObjectHeader.getString("statusCode");
                    String statusMessage = jsonObjectHeader.getString("statusMessage");
                    if (statusMessage.equalsIgnoreCase("SUCCESS") && statusCode.equalsIgnoreCase("200")) {
                        try {
                            JSONObject data = jsonResponse.getJSONObject("data");
                            ObjectMapper objectMapper = new ObjectMapper();
//                            DocDtls persistedDocDtls = objectMapper.readValue(data.toString(), DocDtls.class);
                            DocProfileDtls docProfileDtls = objectMapper.readValue(data.toString(), DocProfileDtls.class);
                           // DocProfileDtls docProfileDtls = persistedDocDtls.getDocProfileDtls();

                            DataManager.getDataManager().getDoctorDetails().setDocProfileDtls(docProfileDtls);
                            docDtls.setDocProfileDtls(docProfileDtls);

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

        } else {
            objAlert.showAlertDialog(getActivity(), "No Internet Connection",
                    "check your connection and try again", false);
        }
    }

    private void register() {

        objNetwork = new NetworkUtilService(getActivity());
        objAlert = new ShowAlertDialog();
        isConnectionExist = objNetwork.checkMobileInternetConn();
        if (isConnectionExist) {

            if(checkBox.isChecked() == false)
            {
                checkBox.setError("Please accept Terms and conditions");
            }
            else
            {
                DocProfileDtls profileDtls = DataManager.getDataManager().getProfileDtls();
                if (profileDtls == null) {
                    profileDtls = new DocProfileDtls();
                    profileDtls.setModified(true);
                }
                profileDtls.setServiceFlag(serviceType);
                DocDtls docDtls = DataManager.getDataManager().getDoctorDetails();
                docDtls.setDocProfileDtls(profileDtls);
                DoctorService service = new DoctorService();
                service.addServiceMode(getActivity(), docDtls, serviceTypeStr);

                Toast.makeText(getActivity(),
                        "Your Service Type is Saved", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
    }
    else
    {
        objAlert.showAlertDialog(getActivity(), "No Internet Connection",
                "check your connection and try again", false);
    }

    }
}
