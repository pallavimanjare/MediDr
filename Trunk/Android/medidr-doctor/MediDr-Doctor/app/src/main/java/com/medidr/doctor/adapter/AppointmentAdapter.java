package com.medidr.doctor.adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.medidr.doctor.R;
import com.medidr.doctor.model.AppointmentDtls;

import java.util.List;

public class AppointmentAdapter extends BaseAdapter{
    public List<AppointmentDtls> list;
    Activity activity;

    public AppointmentAdapter(Activity activity, List<AppointmentDtls> list) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
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
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.time = (TextView) convertView.findViewById(R.id.from);
            holder.patientName = (TextView) convertView.findViewById(R.id.patientNamr);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        AppointmentDtls map = list.get(position);
        holder.date.setText(map.getAppointmentDate());
        holder.time.setText(map.getAppointmentFrom());
        holder.patientName.setText(map.getPatientName());


        return convertView;
    }


}
