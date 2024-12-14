package com.medidr.doctor.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.medidr.doctor.R;
import com.medidr.doctor.model.DocProfileDtls;
import com.medidr.doctor.model.DocScheduleDtls;
import com.medidr.doctor.model.DocServicesDtls;
import com.medidr.doctor.model.enums.DayName;
import com.medidr.doctor.ui.DoctorEditScheduleFragment;

import java.util.ArrayList;
import java.util.List;

public class CustomEditWeeklyOffAdapter extends BaseAdapter{
public List<String> list;
    private int rowCount = 0;
Activity activity;
    private List<String> updatedWeekOffsList = new ArrayList<String>();

        public CustomEditWeeklyOffAdapter(Activity activity, List<String> list) {
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
             AutoCompleteTextView autoDay;
             ImageButton btnWeekoffDelete;

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
            convertView = inflater.inflate(R.layout.edit_weekly_off_adapter, null);
            holder = new ViewHolder();

            holder.autoDay = (AutoCompleteTextView) convertView.findViewById(R.id.autoDay);

           // holder.btnWeekoffDelete = (ImageButton) convertView.findViewById(R.id.btnWeekoffDelete);
            holder.rowNum = rowCount;
            rowCount++;

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity, R.array.week_days, android.R.layout.simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.autoDay.setAdapter(adapter);
            holder.autoDay.setThreshold(1);

         // int weekOffId =Integer.parseInt(list.get(position)) ;
          //  holder.weekOffSpinner.setSelection(weekOffId,false);
            String dayName =list.get(position);
            //getDayNameFromId(weekOffId);
            holder.autoDay.setText(dayName);
            convertView.setTag(holder);

            holder.autoDay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String weekOffdayName = holder.autoDay.getText().toString();
                    list.set(holder.rowNum, weekOffdayName);
                    DoctorEditScheduleFragment.setUpdatedWeekOff(list);

                }
            });

            holder.autoDay.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int DRAWABLE_LEFT = 0;
                    final int DRAWABLE_TOP = 1;
                    final int DRAWABLE_RIGHT = 2;
                    final int DRAWABLE_BOTTOM = 3;

                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (event.getRawX() >= (holder.autoDay.getRight() - holder.autoDay.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            //wrie a code for alert box
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                            builder.setMessage("Are you sure you want to Remove?")
                                    .setCancelable(false)
                                    .setTitle("Remove")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                            /*loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                            loginPrefsEditor = loginPreferences.edit();
                            saveLogin = loginPreferences.getBoolean("saveLogin", false);
                            loginPrefsEditor.clear();
                            loginPrefsEditor.commit();*/
                                            String weekOffdayName = holder.autoDay.getText().toString();
                                            list.remove(weekOffdayName);
                                            DoctorEditScheduleFragment.setUpdatedWeekOff(list);
                                            holder.autoDay.setEnabled(false);

                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();

                            //done with alert box

                        }
                    }
                    return false;
                }
            });
          /*  holder.btnWeekoffDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String weekOffdayName = holder.autoDay.getText().toString();
                    list.remove(weekOffdayName);
                    DoctorEditScheduleFragment.setUpdatedWeekOff(list);
                }
            });
*/
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    public String getDayNameFromId(int dayId) {
        String dayName = null;
        switch (dayId) {
            case 0:
                dayName = "Sunday";
                break;
            case 1:
                dayName = "Monday";
                break;
            case 2:
                dayName = "Tuesday";
                break;
            case 3:
                dayName = "Wednesday";
                break;
            case 4:
                dayName = "Thursday";
                break;
            case 5:
                dayName = "Friday";
                break;
            case 6:
                dayName = "Saturday";
                break;

        }
        return dayName;

    }
}
