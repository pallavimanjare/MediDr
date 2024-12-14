package com.medidr.doctor.ui;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.medidr.doctor.R;
import com.medidr.doctor.adapter.WeeklyOffAdapter;
import com.medidr.doctor.model.DataManager;
import com.medidr.doctor.model.DocDtls;
import com.medidr.doctor.model.DocProfileDtls;
import com.medidr.doctor.model.DocScheduleDtls;
import com.medidr.doctor.services.DoctorService;
import com.medidr.doctor.services.NetworkUtilService;

import java.util.ArrayList;
import java.util.List;

public class WeeklySchedule extends AppCompatActivity {

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS A LISTITEMS
    List<DocScheduleDtls> mainList = null;

    List<String> weeklyOffList = null;

    ArrayAdapter<String> adapter;

    private ListView listView;
    // private ListView weeklyOffListView;

    private View addMoreButton = null;

    //  private View addMoreWeeklyOff = null;

    private View nxtButton = null;


    NetworkUtilService objNetwork = null;
    Boolean isConnectionExist = false;
    ShowAlertDialog objAlert = null;

    private Spinner appointmentTime;
    DocDtls docDtls = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_weekly_schedule);

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
        docDtls = DataManager.getDataManager().getDoctorDetails();
        mainList = new ArrayList<DocScheduleDtls>();
        weeklyOffList = new ArrayList<String>();
        addMoreButton = findViewById(R.id.add_more_schedule);
        listView = (ListView) findViewById(R.id.listView);

        //weeklyOffListView = (ListView) findViewById(R.id.weeklyOffList);
        //addMoreWeeklyOff = findViewById(R.id.add_more_off);

        final Activity activity = this;


        appointmentTime = (Spinner) findViewById(R.id.appointment_spinner);

        ArrayAdapter<CharSequence> appAdapter = ArrayAdapter.createFromResource(activity, R.array.minutes, R.layout.custom_spinner);
        appAdapter.setDropDownViewResource(R.layout.custom_spinner);
        appointmentTime.setAdapter(appAdapter);
        if (mainList == null || mainList.size() == 0) {
            DocScheduleDtls scheduleDetails = new DocScheduleDtls();
            scheduleDetails.setDayId("2");
            scheduleDetails.setDocScheId(Long.valueOf(0));
            scheduleDetails.setDoctorId(docDtls.getAuthDtls().getUserId());
            scheduleDetails.setTimeFrom("9:00 AM");
            scheduleDetails.setTimeTo("10:00 AM");
            scheduleDetails.setModified(false);
            scheduleDetails.setDeleted(false);
            mainList.add(scheduleDetails);
        }

        final WeeklyScheduleListViewAdapter adapter = new WeeklyScheduleListViewAdapter(activity, mainList);
        listView.setAdapter(adapter);

        addMoreButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                DocScheduleDtls scheduleDetails = new DocScheduleDtls();
                scheduleDetails.setDayId("1");
                scheduleDetails.setDocScheId(Long.valueOf(0));
                scheduleDetails.setDoctorId(docDtls.getAuthDtls().getUserId());
                scheduleDetails.setTimeFrom("");
                scheduleDetails.setTimeTo("");
                scheduleDetails.setModified(false);
                scheduleDetails.setDeleted(false);
                adapter.addRow(scheduleDetails);

            }
        });


        // final WeeklyOffAdapter weeklyOffAdapter = new WeeklyOffAdapter(activity,weeklyOffList);
        // weeklyOffListView.setAdapter(weeklyOffAdapter);

      /*  addMoreWeeklyOff.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                weeklyOffAdapter.addRow("Add weekly off days");

            }
        });*/
        nxtButton = findViewById(R.id.nxtButton);
        nxtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                objNetwork = new NetworkUtilService(WeeklySchedule.this);
                objAlert = new ShowAlertDialog();
                isConnectionExist = objNetwork.checkMobileInternetConn();


                if (isConnectionExist) {

                    DocDtls docDtls = DataManager.getDataManager().getDoctorDetails();
                    docDtls.setDocScheduleDtls(mainList);
                    DocProfileDtls docProfileDtls = docDtls.getDocProfileDtls();
                    if (docProfileDtls == null) {
                        if (mainList != null && mainList.size() > 0) {
                            //calculate weekoff days
                            weeklyOffList = calculateWeekOffFromWeekDays(mainList);
                            docProfileDtls = new DocProfileDtls();
                            docProfileDtls.setDoctorId(docDtls.getAuthDtls().getUserId());
                            docProfileDtls.setWeekoffday(weeklyOffList);
                            docProfileDtls.setModified(false);
                            docProfileDtls.setDeleted(false);
                            String strAppointmentTime = appointmentTime.getSelectedItem().toString();
                            if (strAppointmentTime.equalsIgnoreCase("Appointment Time Duration in Minutes")) {
                                docProfileDtls.setAppointmentTimeFreq(15);
                            } else {
                                docProfileDtls.setAppointmentTimeFreq(Integer.parseInt(appointmentTime.getSelectedItem().toString()));
                            }
                            docDtls.setDocProfileDtls(docProfileDtls);
                        }
                    }


                    DoctorService service = new DoctorService();
                    service.addDoctorWeeklySchedule(getApplicationContext(), docDtls);

                    Toast.makeText(getApplicationContext(),
                            "Your schedule is Saved", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(WeeklySchedule.this, HospitalServicesActivity.class);
                    startActivity(intent);

                } else {
                    objAlert.showAlertDialog(WeeklySchedule.this, "No Internet Connection",
                            "check your connection and try again", false);
                }
            }
        });
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

        }
        return false;
    }
}
