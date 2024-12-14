package com.medidr.doctor.adapter;


import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import com.medidr.doctor.R;
import java.util.List;

public class HospitalServiceAdapter extends BaseAdapter{
    public List<String> list;
    Activity activity;
    int count = 0;

    public HospitalServiceAdapter(Activity activity, List<String> list) {
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

    public void addRow(String service){

        Log.i("Schedule Adapter", "ADD row");
        this.list.add(service);
        notifyDataSetChanged();
        // updateView();

    }

    private class ViewHolder {
        EditText service;
        int rowNum;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        // TODO Auto-generated method stub
        final ViewHolder holder;
        LayoutInflater inflater =  activity.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.hospital_service_adapter, null);
            holder = new ViewHolder();
            holder.service = (EditText) convertView.findViewById(R.id.hospital_service);
            holder.rowNum = count;
            count++;

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.service.setText(list.get(position));
        //TODO Handle Service Names
        holder.service.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    String enteredServiceName = ((EditText) v).getText()
                            .toString();

                    list.set(holder.rowNum, enteredServiceName);
                }
            }
        });


        return convertView;
    }


}
