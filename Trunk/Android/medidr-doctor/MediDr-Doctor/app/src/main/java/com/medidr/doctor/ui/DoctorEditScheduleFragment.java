package com.medidr.doctor.ui;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.medidr.doctor.R;
import com.medidr.doctor.adapter.CustomEditWeeklyOffAdapter;
import com.medidr.doctor.adapter.CustomHospitalServiceAdapter;
import com.medidr.doctor.adapter.CustomWeeklyScheduledAdapter;
import com.medidr.doctor.adapter.WeeklyOffAdapter;
import com.medidr.doctor.model.DataManager;
import com.medidr.doctor.model.DocDtls;
import com.medidr.doctor.model.DocProfileDtls;
import com.medidr.doctor.model.DocScheduleDtls;
import com.medidr.doctor.model.DocServicesDtls;
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
public class DoctorEditScheduleFragment extends Fragment {


    private static final String TAG = "Logmessages";
    ListView lvCalender;
    //ListView lvweeklyOffList;
    ImageButton addMoreSchedule, addMoreWeekOff, nxtButton;
    TextView txtAppointment,errorMsg;
    DocDtls docDtls = null;
    List<DocScheduleDtls> docScheduleDtlslst = new ArrayList<DocScheduleDtls>();
    CustomWeeklyScheduledAdapter weeklyScheduleListViewAdapter = null;
    CustomEditWeeklyOffAdapter weeklyOffListViewAdapter = null;
    static List<DocScheduleDtls> updatedDocScheduleDtls = null;
    static List<String> updatedDocWeekOffList = null;
    List<String> weeklyOffList = null;
    List<String> weekOffListDayName = null;

    NetworkUtilService objNetwork = null;
    Boolean isConnectionExist = false;
    ShowAlertDialog objAlert = null;


    public DoctorEditScheduleFragment() {
        // Required empty public constructor
    }

    public static void setUpdatedDocScheduleDtls(List<DocScheduleDtls> updatedDocScheduleDtls1) {
        updatedDocScheduleDtls = updatedDocScheduleDtls1;
    }

    public static void setUpdatedWeekOff(List<String> updatedDocWeekOffList1) {
        updatedDocWeekOffList = updatedDocWeekOffList1;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_doctor_edit_schedule, container, false);

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
        toolbar.setTitle("Edit Schedule");

        errorMsg = (TextView) v.findViewById(R.id.errorMsg);
        lvCalender = (ListView) v.findViewById(R.id.lvCalender);
        // lvweeklyOffList = (ListView) v.findViewById(R.id.lvweeklyOffList);
        lvCalender.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
        addMoreSchedule = (ImageButton) v.findViewById(R.id.addMoreSchedule);
        // addMoreWeekOff = (ImageButton) v.findViewById(R.id.addMoreWeekOff);
        txtAppointment = (TextView) v.findViewById(R.id.txtAppointment);
        nxtButton = (ImageButton) v.findViewById(R.id.nxtButton);
        weeklyOffList = new ArrayList<String>();
        getDoctorScheduleInfo();
        String strAppointmentTime = String.valueOf(docDtls.getDocProfileDtls().getAppointmentTimeFreq());
        strAppointmentTime = "Appointment Time Duration in minutes :" + strAppointmentTime;
        txtAppointment.setText(strAppointmentTime);
        //   weeklyScheduleListViewAdapter = new CustomWeeklyScheduledAdapter(getActivity(),docScheduleDtlslst);
        //   lvCalender.setAdapter(weeklyScheduleListViewAdapter);
        addMoreSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocScheduleDtls scheduleDtls = new DocScheduleDtls();
                scheduleDtls.setDoctorId(docDtls.getAuthDtls().getUserId());
                scheduleDtls.setDocScheId(Long.parseLong("0"));
                scheduleDtls.setDayId("1");
                scheduleDtls.setTimeFrom("");
                scheduleDtls.setTimeTo("");
                scheduleDtls.setAdded(true);
                //docScheduleDtls.add(scheduleDtls);
                weeklyScheduleListViewAdapter.addRow(scheduleDtls);
                Log.d(TAG, "adding row in  adapter");
            }
        });
      /*  addMoreWeekOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                weeklyOffListViewAdapter.addRow("Fromdate");
            }
        });*/


        nxtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objNetwork = new NetworkUtilService(getActivity());
                objAlert = new ShowAlertDialog();
                isConnectionExist = objNetwork.checkMobileInternetConn();
                if (isConnectionExist) {

                    if(!isValidList())
                    {
                        errorMsg.setText("Please select Schedule From and To Time");
                    }
                    else
                    {
                        errorMsg.setText(null);
                    DoctorService service = new DoctorService();
                    if (updatedDocScheduleDtls != null && updatedDocScheduleDtls.size() > 0) {
                        docDtls.setDocScheduleDtls(updatedDocScheduleDtls);
                    }

                    if (updatedDocScheduleDtls != null && updatedDocScheduleDtls.size() > 0) {
                        //calculate weekoff days
                        weeklyOffList = calculateWeekOffFromWeekDays(updatedDocScheduleDtls);

                        DocProfileDtls docProfileDtls = docDtls.getDocProfileDtls();
                        docProfileDtls.setWeekoffday(weeklyOffList);
                        docProfileDtls.setModified(false);
                        docProfileDtls.setDeleted(false);
                        docDtls.setDocProfileDtls(docProfileDtls);
                    }
                    Toast.makeText(getActivity(),
                            "Your schedule is Saved", Toast.LENGTH_LONG).show();
                    service.addDoctorWeeklySchedule(getActivity(), docDtls);
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
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
        for(int i=0;i<docScheduleDtlslst.size();i++)
        {
            DocScheduleDtls schedule = docScheduleDtlslst.get(i);
            String fromTime = schedule.getTimeFrom();
            String toTime = schedule.getTimeTo();
            addMoreSchedule.setEnabled(true);
            nxtButton.setEnabled(true);
            if(fromTime.equalsIgnoreCase("") && toTime.equalsIgnoreCase(""))
            {
                isValidList = false;
                addMoreSchedule.setEnabled(false);
                nxtButton.setEnabled(false);
                break;
            }

        }
        return  isValidList;
    }
    public List<String> convertListIntoDayListFormat(List<String> weekOffListWithDayId) {
        List<String> weekOffListWithDayName = new ArrayList<String>();

        for (int i = 0; i < weekOffListWithDayId.size(); i++) {
            String dayId = weekOffListWithDayId.get(i);
            weekOffListWithDayName.add(dayId);
        }
        return weekOffListWithDayName;


    }

    public void getDoctorScheduleInfo() {

        objNetwork = new NetworkUtilService(getActivity());
        objAlert = new ShowAlertDialog();
        isConnectionExist = objNetwork.checkMobileInternetConn();
        if (isConnectionExist) {
            docDtls = DataManager.getDataManager().getDoctorDetails();
            Long doctorId = docDtls.getAuthDtls().getUserId();
            DoctorService service = new DoctorService();
            String path = "/doctors/getDoctorScheduleForEdit/" + doctorId;
            String responseResult = service.getInformationForDoctor(path);
            if (responseResult != null) {
                //assign data to listview

                try {
                    JSONObject jsonResponse = null;

                    jsonResponse = new JSONObject(responseResult);
                    //if (statusMessage.equalsIgnoreCase("SUCCESS") && statusCode.equalsIgnoreCase("200")) {
                    try {
                        // to do get doctor schedules as well as doctor profile details
                        JSONObject data = jsonResponse.getJSONObject("data");
                        ObjectMapper objectMapper = new ObjectMapper();
                        DocDtls persistedDocDtls = objectMapper.readValue(data.toString(), DocDtls.class);
                        docDtls.setDocScheduleDtls(persistedDocDtls.getDocScheduleDtls());
                        docDtls.setDocProfileDtls(persistedDocDtls.getDocProfileDtls());

                        docScheduleDtlslst = docDtls.getDocScheduleDtls();
                        weeklyScheduleListViewAdapter = new CustomWeeklyScheduledAdapter(getActivity(), docScheduleDtlslst);
                        lvCalender.setAdapter(weeklyScheduleListViewAdapter);

                      /*  if(docDtls.getDocProfileDtls().getWeekoffday() !=null) {
                            weeklyOffList = convertListIntoDayListFormat(docDtls.getDocProfileDtls().getWeekoffday());
                        }*/


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

    public List<String> calculateWeekOffFromWeekDays(List<DocScheduleDtls> mainList) {
        DocScheduleDtls docSchedule = null;
        List<String> weeklyOffList = new ArrayList<>();
        weeklyOffList.add("Sunday");
        weeklyOffList.add("Monday");
        weeklyOffList.add("Tuesday");
        weeklyOffList.add("Wednesday");
        weeklyOffList.add("Thursday");
        weeklyOffList.add("Friday");
        weeklyOffList.add("Saturday");
        String dayID = null;
        for (int i = 0; i < mainList.size(); i++) {
            docSchedule = mainList.get(i);
            dayID = docSchedule.getDayId();
            switch (dayID) {
                case "0":
                    weeklyOffList.remove("Sunday");
                    break;
                case "1":
                    weeklyOffList.remove("Monday");
                    break;
                case "2":
                    weeklyOffList.remove("Tuesday");
                    break;
                case "3":
                    weeklyOffList.remove("Wednesday");
                    break;
                case "4":
                    weeklyOffList.remove("Thursday");
                    break;
                case "5":
                    weeklyOffList.remove("Friday");
                    break;
                case "6":
                    weeklyOffList.remove("Saturday");
                    break;

            }
            weeklyOffList.remove(dayID);


        }
        return weeklyOffList;

    }

}
