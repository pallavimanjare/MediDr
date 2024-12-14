package com.medidr.doctor.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;

import com.medidr.doctor.R;

import java.util.List;

public class CustomWeeklyOffAdapter extends BaseAdapter{
public List<String> list;
    private int rowCount = 0;
Activity activity;

        public CustomWeeklyOffAdapter(Activity activity, List<String> list) {
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

       public void addRow(String offDay){

        Log.i("Schedule Adapter", "ADD row");
        this.list.add(offDay);
        notifyDataSetChanged();
        // updateView();

        }

         private class ViewHolder {
             Spinner spinner;
             private int rowNum;
         }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        // TODO Auto-generated method stub
        final ViewHolder holder;
        LayoutInflater inflater =  activity.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.weekly_off_adapter, null);
            holder = new ViewHolder();
            holder.spinner = (Spinner) convertView.findViewById(R.id.weekly_off_spinner);
            holder.rowNum = rowCount;
            rowCount++;

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity,R.array.week_days,android.R.layout.simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.spinner.setAdapter(adapter);
            convertView.setTag(holder);


            //TODO Handle Day in Schedule
            holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // your code here
                    String weekoffDayName = holder.spinner.getSelectedItem().toString();
                  //  list.set(holder.rowNum, weekoffDayName);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                    String a = "Inside item not selected";
                }

            });

        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }


}
