package com.medidr.doctor.ui;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.nfc.Tag;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import com.medidr.doctor.R;
import com.medidr.doctor.config.DateUtil;
import com.medidr.doctor.model.DocScheduleDtls;
import com.medidr.doctor.model.enums.DayName;

import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by mansi on 3/21/2016.
 */
public class WeeklyScheduleListViewAdapter extends BaseAdapter {
    private static final String TAG = "LogMessages";

    private String default_from_time = "09:00 am";

    private String default_to_time = "10:00 am";

    private int pos, pHour, pMinutes;

    private boolean fromFlag, toFlag;

    private int rowCount = 0;


    public List<DocScheduleDtls> list;
    Activity activity;

    public WeeklyScheduleListViewAdapter(Activity activity, List<DocScheduleDtls> list) {
        super();
        this.activity = activity;
        this.list = list;
    }


    TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Log.i(" onTimeSet............", "");
            pHour = hourOfDay;
            pMinutes = minute;
            if (fromFlag) updateFromTime(pos);
            else {
                updateToTime(pos);
            }

        }


    };


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


    public void addRow(DocScheduleDtls dtls) {

        Log.i("Schedule Adapter", "ADD row");
        this.list.add(dtls);
        notifyDataSetChanged();
        // updateView();

    }

    private class ViewHolder {
        private Spinner spinner;
        private TextView from;
        private TextView to;
        private int rowNum;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        // TODO Auto-generated method stub
        final ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.schedule_adapter, null);
            holder = new ViewHolder();
            holder.spinner = (Spinner) convertView.findViewById(R.id.day);
            holder.from = (TextView) convertView.findViewById(R.id.from);
            holder.to = (TextView) convertView.findViewById(R.id.to);
            holder.rowNum = rowCount;
            rowCount++;

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity, R.array.week_days, android.R.layout.simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            convertView.setTag(holder);
            holder.spinner.setAdapter(adapter);
         //   holder.from.setText(default_from_time);
           // holder.to.setText(default_to_time);


            Log.d(TAG, " @@@@@@@@@@@@@@@@@@@@ " + holder.spinner);
            //TODO Handle Day in Schedule
            holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // your code here
                    String dayName = holder.spinner.getSelectedItem().toString();
                    DocScheduleDtls docScheduleDtls = list.get(holder.rowNum);
                    Integer dayId = DayName.valueOf(dayName.toUpperCase()).ordinal();
                    docScheduleDtls.setDayId(dayId.toString());
                    //write code to check schedule already exist
                    list.set(holder.rowNum, docScheduleDtls);
                    DocScheduleDtls schedule = list.get(holder.rowNum);
                    boolean isValidTime = DateUtil.isValidTime(schedule.getTimeFrom().toString(), schedule.getTimeTo().toString(), true);
                    if (isValidTime == true) {

                        String errorMsg = "Schedule Start Time " + schedule.getTimeFrom() + "<br> should be lesser than End Time " + schedule.getTimeTo();
                        errorResult(errorMsg);
                        ////check here time already exist or not for that day
                    } else {

                        boolean isScheduleExist = DateUtil.isDatesInRangeAdd(list, schedule, holder.rowNum);
                        if (isScheduleExist == true) {
                            String errorMsg = "Selected time " + schedule.getTimeFrom() + " to " + schedule.getTimeTo() + " ,<br> conflict with existing schedules for " + dayName;
                            errorResult(errorMsg);
                        } else {
                            errorResult(null);
                        }

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                    String a = "Inside item not selected";
                }

            });

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.from.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    final Calendar cal = Calendar.getInstance();
                    pHour = cal.get(Calendar.HOUR_OF_DAY);
                    pMinutes = cal.get(Calendar.MINUTE);
                    pos = position;
                    fromFlag = true;
                    toFlag = false;
                    TimePickerDialog tp = new TimePickerDialog(activity, mTimeSetListener, pHour, pMinutes, false);
                    tp.show();
                }
                return true;
            }
        });
        holder.to.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    final Calendar cal = Calendar.getInstance();
                    pHour = cal.get(Calendar.HOUR_OF_DAY);
                    pMinutes = cal.get(Calendar.MINUTE);
                    pos = position;
                    toFlag = true;
                    fromFlag = false;
                    TimePickerDialog tp = new TimePickerDialog(activity, mTimeSetListener, pHour, pMinutes, false);
                    tp.show();
                }
                return true;
            }
        });

           /*holder.spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               public void onItemClick(AdapterView<?> parent, View view,
                                       int position, long id) {
                   // When clicked, show a toast with the TextView text
                   String day = ((TextView)view).getText().toString();
                   Log.d("TAG", day);

               }
           });*/

        return convertView;
    }

    private void updateFromTime(int pos) {
        String strHour = "";
        String strMinute = String.valueOf(pMinutes);
        String strAmPm = "am";
        if (pHour >= 12) {
            if (pHour > 12) {
                pHour = pHour - 12;
            }
            strAmPm = "pm";
        }
        strHour = String.valueOf(pHour);
        if (strHour.length() == 1) {
            strHour = "0" + String.valueOf(pHour);
        }

        if (strMinute.length() == 1) {
            strMinute = "0" + strMinute;
        }
        String time = strHour + ":" + strMinute + " " + strAmPm;
        default_from_time = time;
        //default_to_time = String.valueOf(pMinutes);
        updateView(pos);


    }

    private void updateToTime(int pos) {
        String strHour = "";
        String strMinute = String.valueOf(pMinutes);
        String strAmPm = "am";
        if (pHour >= 12) {
            if (pHour > 12) {
                pHour = pHour - 12;
            }
            strAmPm = "pm";
        }
        strHour = String.valueOf(pHour);
        if (strHour.length() == 1) {
            strHour = "0" + String.valueOf(pHour);
        }

        if (strMinute.length() == 1) {
            strMinute = "0" + strMinute;
        }
        String time = strHour + ":" + strMinute + " " + strAmPm;
        default_to_time = time;
        updateView(pos);


    }

    public String get24HourFormatTime(String time) {
        String formatted24time = time;
        SimpleDateFormat date12Format = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat date24Format = new SimpleDateFormat("HH:mm:ss");
        try {
            formatted24time = date24Format.format(date12Format.parse(time));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatted24time;
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


    public  void errorResult(String errorMsgtxt)
    {
        TextView errorMsg = (TextView) ((Activity) activity).findViewById(R.id.errorMsg);
        ImageButton nxtButton = (ImageButton) ((Activity) activity).findViewById(R.id.nxtButton);
        ImageButton add_more_schedule = (ImageButton) ((Activity) activity).findViewById(R.id.add_more_schedule);
        if(errorMsgtxt==null)
        {
            errorMsg.setText(null);
            nxtButton.setEnabled(true);
            add_more_schedule.setEnabled(true);
        }
        else  {

            errorMsg.setText(Html.fromHtml(errorMsgtxt));
            nxtButton.setEnabled(false);
            add_more_schedule.setEnabled(false);
        }
    }

    private void updateView(int index) {

        ListView view = (ListView) activity.findViewById(R.id.listView);
        View v = view.getChildAt(index - view.getFirstVisiblePosition());
        ViewHolder holder;


        if (v == null) {
            return;
        }

        holder = new ViewHolder();
        holder.from = (TextView) v.findViewById(R.id.from);
        holder.to = (TextView) v.findViewById(R.id.to);
        v.setTag(holder);


        //write a code to check valid schedule is selected
        holder.from.setError(null);
        holder.from.setText(default_from_time);
        list.get(pos).setTimeFrom(default_from_time);
        DocScheduleDtls schedule = list.get(pos);
        boolean isValidTime = DateUtil.isValidTime(schedule.getTimeFrom().toString(), schedule.getTimeTo().toString(), true);

        if (isValidTime == true) {

            String errorMsg = "Schedule Start Time "+schedule.getTimeFrom()+"<br> should be lesser than End Time "+schedule.getTimeTo();
            errorResult(errorMsg);
            ////check here time already exist or not for that day
        } else

        {
            boolean isScheduleExist = DateUtil.isDatesInRangeAdd(list, schedule,pos);
            if (isScheduleExist == true) {
                String dayName = getDayNameFromId(Integer.parseInt(schedule.getDayId()));
                String errorMsg ="Selected time "+schedule.getTimeFrom()+" to "+ schedule.getTimeTo()+" ,<br> conflict with existing schedules for "+dayName;

                errorResult(errorMsg);

            } else {
                errorResult(null);
            }
        }
        holder.to.setError(null);
        holder.to.setText(default_to_time);
        list.get(pos).setTimeTo(default_to_time);
        boolean isValidToTime = DateUtil.isValidTime(schedule.getTimeFrom().toString(), schedule.getTimeTo().toString(), true);

        if (isValidToTime == true) {
            String errorMsg = "Schedule Start Time "+schedule.getTimeFrom()+"<br> should be lesser than End Time "+schedule.getTimeTo();
            errorResult(errorMsg);
            ////check here time already exist or not for that day
        } else
        {
        boolean istoScheduleExist = DateUtil.isDatesInRangeAdd(list, schedule,pos);
        if (istoScheduleExist == true) {
            String dayName = getDayNameFromId(Integer.parseInt(schedule.getDayId()));
            String errorMsg ="Selected time "+schedule.getTimeFrom()+" to "+ schedule.getTimeTo()+" ,<br> conflict with existing schedules for "+dayName;
            errorResult(errorMsg);
        } else {
            errorResult(null);
        }


    }

    }

}
