package com.medidr.doctor.ui;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medidr.doctor.R;
import com.medidr.doctor.adapter.VacationPlannerAdapter;
import com.medidr.doctor.model.DataManager;
import com.medidr.doctor.model.DocDtls;
import com.medidr.doctor.model.DocVacationSchedules;
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
public class MyVacationFragment extends Fragment {

    ListView lvVacation;
    VacationPlannerAdapter vacationPlannerAdapter = null;
    TextView errorMsg;

    List<DocVacationSchedules> vacationDateList = new ArrayList<DocVacationSchedules>();
    NetworkUtilService objNetwork = null;
    Boolean isConnectionExist = false;
    ShowAlertDialog objAlert = null;
    DocDtls docDtls = null;

    ImageButton addMore;
    Button save;

    public MyVacationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_vacation, container, false);

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

        toolbar.setTitle("Vacation  Planner");
        docDtls = DataManager.getDataManager().getDoctorDetails();


        lvVacation = (ListView) v.findViewById(R.id.lvVacation);
        errorMsg = (TextView) v.findViewById(R.id.errorMsg);
        lvVacation.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
        View header = inflater.inflate(R.layout.vacation_planner_header, null);
        lvVacation.addHeaderView(header);
        // final VacationPlannerAdapter adapter = new VacationPlannerAdapter(getActivity(), vacationDateList);
        // lvVacation.setAdapter(adapter);

        getDoctorVacationSchedules();
        addMore = (ImageButton) v.findViewById(R.id.addMore);
        save = (Button) v.findViewById(R.id.save);
        addMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DocVacationSchedules vacationDtls = new DocVacationSchedules();
                vacationDtls.setDocVacationsId(0L);
                vacationDtls.setDoctorId(docDtls.getAuthDtls().getUserId());
                vacationDtls.setFromDate("");
                vacationDtls.setToDate("");
                //docScheduleDtls.add(scheduleDtls);
                vacationPlannerAdapter.addRow(vacationDtls);

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                objNetwork = new NetworkUtilService(getActivity());
                objAlert = new ShowAlertDialog();
                isConnectionExist = objNetwork.checkMobileInternetConn();
                if (isConnectionExist) {


                    if(!isValidList())
                    {
                        errorMsg.setText("Please select Vacation From and To Date");
                    }
                    else {
                        errorMsg.setText(null);
                        //check for valid list
                        String strConflictingDates = null;
                        DoctorService service = new DoctorService();
                        String statusCode = null;
                        String response = service.addDoctorVacationSchedule(vacationDateList);
                        JSONObject jsonObject = null;
                        String dataResponse = null;
                        try {
                            jsonObject = new JSONObject(response);
                            String header = jsonObject.getString("header");
                            JSONObject jsonObjectHeader = new JSONObject(header);
                            statusCode = jsonObjectHeader.getString("statusCode");
                            dataResponse = jsonObject.getJSONArray("data").toString();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (statusCode.equalsIgnoreCase("201")) {
                            // Successfully inserted/updated ....
                            Toast.makeText(getActivity(),
                                    "Your vacations are Saved", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            startActivity(intent);
                        }
                        if (statusCode.equalsIgnoreCase("202")) {
                            // Conflicts detected for vacation date range ....
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                            ObjectMapper mapper = new ObjectMapper();
                            List<String> conflictingDates = new ArrayList<String>();
                            try {
                                conflictingDates = mapper.readValue(dataResponse, new TypeReference<List<String>>() {
                                });
                            } catch (Exception exception) {
                                Log.d("TAG", "Exception in processing the data");
                            }

                            StringBuilder conflictingAppointmentsTxt = new StringBuilder("Please cancel your appointment for below conflicting dates ");
                            builder.setMessage(conflictingAppointmentsTxt);
                            for (int count = 0; count < conflictingDates.size(); count++) {
                                conflictingAppointmentsTxt.append(" \n ");
                                conflictingAppointmentsTxt.append(conflictingDates.get(count));
                            }

                            builder.setMessage(conflictingAppointmentsTxt.toString())
                                    .setCancelable(false)
                                    .setTitle("Appointment Dates")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }
                } else {
                    objAlert.showAlertDialog(getActivity(), "No Internet Connection",
                            "check your connection and try again", false);
                }

            }
        });


        return v;
    }

    public  boolean isValidList()
    {
        boolean isValidList = true;
        for(int i=0;i<vacationDateList.size();i++)
        {
            DocVacationSchedules vacation = vacationDateList.get(i);
            String fromdate = vacation.getFromDate();
            String todate = vacation.getToDate();
            if(fromdate.equalsIgnoreCase("") && todate.equalsIgnoreCase(""))
            {
                isValidList = false;
                break;
            }

        }
        return  isValidList;
    }
    public void getDoctorVacationSchedules() {

        objNetwork = new NetworkUtilService(getActivity());
        objAlert = new ShowAlertDialog();
        isConnectionExist = objNetwork.checkMobileInternetConn();
        if (isConnectionExist) {
            docDtls = DataManager.getDataManager().getDoctorDetails();
            Long doctorId = docDtls.getAuthDtls().getUserId();
            DoctorService service = new DoctorService();
            String path = "/doctors/getVacationSchedules/" + doctorId;
            String responseResult = service.getInformationForDoctor(path);
            if (responseResult != null) {
                //assign data to listview

                try {
                    JSONObject jsonResponse = new JSONObject(responseResult);
                    //if (statusMessage.equalsIgnoreCase("SUCCESS") && statusCode.equalsIgnoreCase("200")) {
                    try {
                        JSONArray vacationDtlsArr = jsonResponse.getJSONArray("data");

                        for (int i = 0; i < vacationDtlsArr.length(); i++) {
                            JSONObject jsonObject = vacationDtlsArr.getJSONObject(i);
                        }

                        ObjectMapper mapper = new ObjectMapper();
                        vacationDateList = mapper.readValue(vacationDtlsArr.toString(), new TypeReference<List<DocVacationSchedules>>() {
                            // DataManager.getDataManager().getDoctorDetails().//(docServiceDtls);

                            //  docDtls.setDocHospitalDtls(docHospDtls);
                        });
                        vacationPlannerAdapter = new VacationPlannerAdapter(getActivity(), vacationDateList);
                        lvVacation.setAdapter(vacationPlannerAdapter);

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
}
