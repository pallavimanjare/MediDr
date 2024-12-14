package com.medidr.doctor.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.medidr.doctor.R;
import com.medidr.doctor.model.AppointmentDtls;

public class AppointmentDetailsAdapter extends BaseAdapter{

    public AppointmentDtls appointmentDetails;
    Activity activity;

    public AppointmentDetailsAdapter(Activity activity, AppointmentDtls appointmentDetails) {
        super();
        this.activity = activity;
        this.appointmentDetails = appointmentDetails;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    private class ViewHolder {
        TextView date;
        TextView time;
        TextView patientName;
        TextView patientMobile;
        TextView appointmentId;
        TextView username;
        TextView userMobile;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        // TODO Auto-generated method stub
        ViewHolder holder;
        LayoutInflater inflater =  activity.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.schedule_adapter, null);
            holder = new ViewHolder();
            holder.date = (TextView) convertView.findViewById(R.id.appointment_date);
            holder.time = (TextView) convertView.findViewById(R.id.appointment_time);
            holder.patientName = (TextView) convertView.findViewById(R.id.patientName);
            holder.appointmentId = (TextView) convertView.findViewById(R.id.appointment_id);
            holder.username = (TextView) convertView.findViewById(R.id.username);
            holder.userMobile = (TextView) convertView.findViewById(R.id.user_mobile);
            holder.patientMobile = (TextView) convertView.findViewById(R.id.patientMobile);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.date.setText(appointmentDetails.getAppointmentDate());
        holder.time.setText(appointmentDetails.getAppointmentFrom());
        holder.patientMobile.setText(appointmentDetails.getMobileNumber());
        holder.patientName.setText(appointmentDetails.getPatientName());


        return convertView;
    }

}
