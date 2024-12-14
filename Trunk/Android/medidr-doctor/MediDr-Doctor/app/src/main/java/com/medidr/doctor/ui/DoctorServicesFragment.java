package com.medidr.doctor.ui;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.medidr.doctor.model.DataManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medidr.doctor.R;
import com.medidr.doctor.adapter.CustomDoctorAppointmentAdapter;
import com.medidr.doctor.adapter.CustomHospitalServiceAdapter;
import com.medidr.doctor.adapter.HospitalServiceAdapter;
import com.medidr.doctor.model.AppointmentDtls;
import com.medidr.doctor.model.DataManager;
import com.medidr.doctor.model.DocDtls;
import com.medidr.doctor.model.DocHospitalDtls;
import com.medidr.doctor.model.DocServicesDtls;
import com.medidr.doctor.services.DoctorService;
import com.medidr.doctor.services.NetworkUtilService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorServicesFragment extends Fragment {

    private static final String TAG = "Logmessages";
/*
    private static List<DocServicesDtls> updatedDocServiceDtls = null;
    private static List<DocServicesDtls> deletedDocServiceDtls = null;
*/

    NetworkUtilService objNetwork = null;
    Boolean isConnectionExist = false;
    ShowAlertDialog objAlert = null;

    ListView hospServiceListView;
    TextView errorMsg;
    ImageButton addMore;
    ImageButton hospitalServiceNxt;
    CustomHospitalServiceAdapter hospitalServiceAdapter;
    DocDtls docDtls = null;
    List<DocServicesDtls> docServiceDtls = new ArrayList<DocServicesDtls>();

    public DoctorServicesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_doctor_services, container, false);
        errorMsg = (TextView) v.findViewById(R.id.errorMsg);
        errorMsg.setText(null);
        hospServiceListView = (ListView) v.findViewById(R.id.hospServiceListView);
        hospServiceListView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });

        addMore = (ImageButton) v.findViewById(R.id.addMore);
        hospitalServiceNxt = (ImageButton) v.findViewById(R.id.hospitalServiceNxt);

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

        toolbar.setTitle("Edit Services");
        docServiceDtls.clear();
        getDoctorServicesInfo();
        ///

        ///
        hospitalServiceNxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objNetwork = new NetworkUtilService(getActivity());
                objAlert = new ShowAlertDialog();
                isConnectionExist = objNetwork.checkMobileInternetConn();
                if (isConnectionExist) {
                    if (docServiceDtls.size() > 0) {
                        docDtls.setDocServicesDtls(docServiceDtls);
                        DoctorService service = new DoctorService();
                        service.addDoctorServicesDetails(getActivity(), docDtls);
                    }
                    Toast.makeText(getActivity(),
                            "Your Services are Saved", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
                } else {
                    objAlert.showAlertDialog(getActivity(), "No Internet Connection",
                            "check your connection and try again", false);
                }

            }
        });

        addMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int size = calculateServiceCount(docServiceDtls);
                if (size > 15) {
                    errorMsg.setText("You can add services upto 15");
                } else {
                    errorMsg.setText(null);
                    DocServicesDtls docObj = new DocServicesDtls();
                    docObj.setServiceName("X - Ray");
                    docObj.setDocServiceId(Long.parseLong("0"));
                    docObj.setServiceActiveFlag(true);
                    docObj.setDoctorId(docDtls.getAuthDtls().getUserId());
                    docServiceDtls.add(docObj);
                    hospitalServiceAdapter.notifyDataSetChanged();
                }
            }
        });

        return v;
    }

    public int calculateServiceCount(List<DocServicesDtls> docServiceDtls) {
        List<DocServicesDtls> objdocServiceDtls = new ArrayList<DocServicesDtls>();
        objdocServiceDtls = docServiceDtls;
        int serviceSize = 0;
        for (int i = 0; i < objdocServiceDtls.size(); i++) {
            DocServicesDtls docObj = objdocServiceDtls.get(i);
            if (docObj.isDeleted() == true) {
                objdocServiceDtls.remove(i);
            }

        }

        serviceSize = objdocServiceDtls.size();
        return serviceSize;
    }

    public void getDoctorServicesInfo() {

        objNetwork = new NetworkUtilService(getActivity());
        objAlert = new ShowAlertDialog();
        isConnectionExist = objNetwork.checkMobileInternetConn();
        if (isConnectionExist) {
            docDtls = DataManager.getDataManager().getDoctorDetails();
            Long doctorId = docDtls.getAuthDtls().getUserId();
            DoctorService service = new DoctorService();
            String path = "/doctors/getDoctorServices/" + doctorId;
            String responseResult = service.getInformationForDoctor(path);
            if (responseResult != null) {
                //assign data to listview

                try {
                    JSONObject jsonResponse = new JSONObject(responseResult);
                    //if (statusMessage.equalsIgnoreCase("SUCCESS") && statusCode.equalsIgnoreCase("200")) {
                    try {
                        JSONArray serviceDtlsArr = jsonResponse.getJSONArray("data");

                        for (int i = 0; i < serviceDtlsArr.length(); i++) {
                            JSONObject jsonObject = serviceDtlsArr.getJSONObject(i);
                        }

                        ObjectMapper mapper = new ObjectMapper();
                        docServiceDtls = mapper.readValue(serviceDtlsArr.toString(), new TypeReference<List<DocServicesDtls>>() {
                            // DataManager.getDataManager().getDoctorDetails().//(docServiceDtls);

                            //  docDtls.setDocHospitalDtls(docHospDtls);
                        });
                        hospitalServiceAdapter = new CustomHospitalServiceAdapter(getActivity(), docServiceDtls);
                        hospServiceListView.setAdapter(hospitalServiceAdapter);

                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }

                    //}
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            objAlert.showAlertDialog(getActivity(), "No Internet Connection",
                    "check your connection and try again", false);
        }

    }
/*    public static void setUpdatedServiceDtlsValues(List<DocServicesDtls> updatedDocServiceDtls1)
    {
        updatedDocServiceDtls = updatedDocServiceDtls1;
    }
    public static void setDeletedServiceDtlsValues(List<DocServicesDtls> deletedDocServiceDtls1)
    {
        deletedDocServiceDtls = deletedDocServiceDtls1;
    }*/


}
