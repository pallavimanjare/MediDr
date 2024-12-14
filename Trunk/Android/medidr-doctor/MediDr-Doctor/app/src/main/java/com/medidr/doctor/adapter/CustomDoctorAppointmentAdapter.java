package com.medidr.doctor.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medidr.doctor.R;
import com.medidr.doctor.model.AppointmentDtls;
import com.medidr.doctor.model.DataManager;
import com.medidr.doctor.ui.CancelAppointmentActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;



/**
 * Created by Sharvee on 4/18/2016.
 */

public class CustomDoctorAppointmentAdapter extends BaseAdapter{
    private static final String TAG = "LogMessages";
    private Activity activity;
    private LayoutInflater inflater;
    private List<AppointmentDtls> appointmentDtls;
    String strAppointmentDtls;
    String strUserDtls;
    private ProgressDialog pDialog;

    public CustomDoctorAppointmentAdapter(Activity activity, List<AppointmentDtls> appointmentDtls) {
        this.activity = activity;
        this.appointmentDtls = appointmentDtls;
    }

    public List<AppointmentDtls> getAppointmentList() {
        return appointmentDtls;
    }

    @Override
    public int getCount() {
        return appointmentDtls.size();
    }

    @Override
    public Object getItem(int location) {
        return appointmentDtls.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.appointment_list_row, null);
        if (position % 2 == 1) {
            convertView.setBackgroundColor(Color.WHITE);
            //#E6E4E4
        } else {
            convertView.setBackgroundResource(R.color.blueback);
        }


        TextView appointment_date = (TextView) convertView.findViewById(R.id.appointment_date);
        TextView appointment_time = (TextView) convertView.findViewById(R.id.appointment_time);
        TextView patient_name = (TextView) convertView.findViewById(R.id.patient_name);


        final AppointmentDtls appointmentRec = appointmentDtls.get(position);
        String strappointmentDate = appointmentRec.getAppointmentDate();
        String strappointmentTime = appointmentRec.getAppointmentFrom();
        String strpatientName = appointmentRec.getPatientName();

        String formattedAppointmentTime = null;
        SimpleDateFormat date12Format = new SimpleDateFormat("hh:mma");
        SimpleDateFormat date24Format = new SimpleDateFormat("HH:mm");
        try {
            formattedAppointmentTime = date12Format.format(date24Format.parse(strappointmentTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("E, dd MMM");
        String formattedAppointmentDate = null;
        String dateInString = strappointmentDate;

        try {
            Date date = formatter.parse(dateInString);
            formattedAppointmentDate = dateFormatter.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        appointment_date.setText(formattedAppointmentDate);
        appointment_time.setText(formattedAppointmentTime);
        patient_name.setText(strpatientName);
       /* String patientName = appointmentRec.getPatientName();
        String doctorName = appointmentRec.get();
        String hospitalName = appointmentRec.getHospitalName();
        String appointmentDate = appointmentRec.getAppointmentDate();
        String appointmentTime = appointmentRec.getAppointmentFrom();
        docProfileImg.setImageUrl(appointmentRec.getDoctorProfileThumbnailImage(), imageLoader);*/
        convertView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Do your stuff here
                Log.d(TAG,"test in "+DataManager.getDataManager()+"for appoint record "+appointmentRec.toString());
                DataManager.getDataManager().setAppointmentDtls(appointmentRec);
                pDialog = new ProgressDialog(activity);
                pDialog.setMessage("please wait...");
                pDialog.show();
                try {
                    //TO GO :go to next screen
                   /* String strAppointmentDtls = mapper.writeValueAsString(appointmentRec);
                    Intent appointemntDtlsActivity = new Intent(activity.getApplicationContext(), BookAppointmentDetailInfoActivity.class);
                    appointemntDtlsActivity.putExtra("appointmentDetails",strAppointmentDtls);
                    appointemntDtlsActivity.putExtra("userDetails",strUserDtls);
                    activity.startActivity(appointemntDtlsActivity);
                    hidePDialog();*/
                    Intent appointemntDtlsActivity = new Intent(activity.getApplicationContext(), CancelAppointmentActivity.class);
                    activity.startActivity(appointemntDtlsActivity);
                    pDialog.dismiss();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

        });

        return convertView;
    }


    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
}
