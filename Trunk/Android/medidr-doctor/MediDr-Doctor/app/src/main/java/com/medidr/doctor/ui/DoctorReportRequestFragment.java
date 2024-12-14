package com.medidr.doctor.ui;


import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.medidr.doctor.R;
import com.medidr.doctor.model.DataManager;
import com.medidr.doctor.model.DocDtls;
import com.medidr.doctor.services.DoctorService;
import com.medidr.doctor.services.NetworkUtilService;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorReportRequestFragment extends Fragment {

    String filePath = null;
    ImageButton getReport;
    EditText fromdate;
    EditText todate;
    DocDtls docDtls = null;
    TextView errorMsg;
    TextView txtdownloadlnk;
    RelativeLayout relativeLayout5;

    NetworkUtilService objNetwork = null;
    Boolean isConnectionExist = false;
    ShowAlertDialog objAlert = null;

    Calendar myCalendar = Calendar.getInstance();

    private SimpleDateFormat dateFormatter;
    private DatePickerDialog fromDatePickerDialog;

    public DoctorReportRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_doctor_report_request, container, false);

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


        toolbar.setTitle("Report Request");

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

        docDtls = DataManager.getDataManager().getDoctorDetails();
        getReport = (ImageButton) v.findViewById(R.id.getReport);
        fromdate = (EditText) v.findViewById(R.id.fromdate);
        txtdownloadlnk = (TextView) v.findViewById(R.id.txtdownloadlnk);
        relativeLayout5 = (RelativeLayout) v.findViewById(R.id.relativeLayout5);
        relativeLayout5.setVisibility(View.GONE);


        todate = (EditText) v.findViewById(R.id.todate);
        errorMsg = (TextView) v.findViewById(R.id.errorMsg);
        errorMsg.setText(null);


        Date today = new Date();
        todate.setText(dateFormatter.format(today.getTime()));

        txtdownloadlnk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                try {
                    File file = new File(filePath);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.ms-excel");
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), "No Application Available to View Excel", Toast.LENGTH_SHORT).show();
                }

            }
        });


        fromdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        todate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        final String message = "please generate report for date From : " + fromdate.getText().toString() + "To :" + todate.getText().toString();

        this.getReport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                objNetwork = new NetworkUtilService(getActivity());
                objAlert = new ShowAlertDialog();
                isConnectionExist = objNetwork.checkMobileInternetConn();
                if (isConnectionExist) {
                    String myFormat = "dd/MM/yyyy"; //In which you need put here
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                    String strfromdate = fromdate.getText().toString();
                    String strtodate = todate.getText().toString();

                    if (strfromdate != null && strtodate != null) {
                        Boolean isDateValid = CheckDates(strfromdate, strtodate);
                        if (isDateValid == true) {

                            DoctorService service = new DoctorService();
                            DocDtls docDtls = DataManager.getInstance().getDoctorDetails();

                            filePath = service.getAppointmentHistoryExcel(getActivity(), docDtls.getAuthDtls().getUserId(),
                                    fromdate.getText().toString(), todate.getText().toString());

                            Toast.makeText(getActivity(),
                                    "Excel Report is downloaded", Toast.LENGTH_LONG).show();
                            relativeLayout5.setVisibility(View.VISIBLE);
                            // Intent intent = new Intent(getActivity(), HomeActivity.class);
                            //startActivity(intent);
                        }
                        else
                        {
                            errorMsg.setText("Report Request From Date should lesser than To Date");
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

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "dd/MM/yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            fromdate.setText(sdf.format(myCalendar.getTime()));

            String strfromdate = fromdate.getText().toString();
            String strtodate = todate.getText().toString();

            if (strfromdate != null && strtodate != null) {
                Boolean isDateValid = CheckDates(strfromdate, strtodate);
                if (isDateValid == true) {

                    todate.setText(sdf.format(myCalendar.getTime()));
                    errorMsg.setText("");

                } else {
                    errorMsg.setText("Report Request From Date should lesser than To Date");
                }

            }
            //  updateLabel();

        }

    };

    DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "dd/MM/yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            String strfromdate = fromdate.getText().toString();
            String strtodate = sdf.format(myCalendar.getTime());

            if (strfromdate != null && strtodate != null) {
                Boolean isDateValid = CheckDates(strfromdate, strtodate);
                if (isDateValid == true) {

                    todate.setText(sdf.format(myCalendar.getTime()));
                    errorMsg.setText("");

                } else {
                    errorMsg.setText("Report Request From Date should lesser than To Date");
                }

            }


            //  updateLabel();

        }

    };

    public static boolean CheckDates(String fromdate, String todate) {

        SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy");

        boolean b = false;

        try {
            if (dfDate.parse(fromdate).equals(dfDate.parse(todate))) {
                b = true;  // If two dates are equal.
                System.out.println("start date is equals end date");
            } else if (dfDate.parse(fromdate).after(dfDate.parse(todate))) {
                b = false; // If start date is after the end date.
                System.out.println("start date is after end date");
            } else if (dfDate.parse(fromdate).before(dfDate.parse(todate))) {
                b = true;  // If start date is before end date.
                System.out.println("start date is before end date");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return b;
    }
}
