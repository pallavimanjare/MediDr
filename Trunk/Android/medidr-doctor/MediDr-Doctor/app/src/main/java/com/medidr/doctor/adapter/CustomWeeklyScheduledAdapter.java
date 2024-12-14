package com.medidr.doctor.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.medidr.doctor.R;
import com.medidr.doctor.config.DateUtil;
import com.medidr.doctor.model.DocScheduleDtls;
import com.medidr.doctor.model.DocVacationSchedules;
import com.medidr.doctor.model.enums.DayName;
import com.medidr.doctor.ui.DoctorEditScheduleFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Sharvee on 4/22/2016.
 */
public class CustomWeeklyScheduledAdapter extends BaseAdapter {
    private static final String TAG = "LogMessages";

    private String default_from_time = "09:00 am";

    private String default_to_time = "10:00 am";

    private int pos, pHour, pMinutes;

    private boolean fromFlag, toFlag;

    private int rowCount = 0;

    int nullCount = 0;

    private boolean isDaySelected = true;
    private Activity activity;
    private LayoutInflater inflater;
    private List<DocScheduleDtls> docScheduleDtlsList;

    public CustomWeeklyScheduledAdapter(Activity activity, List<DocScheduleDtls> docScheduleDtlsList) {
        this.activity = activity;
        this.docScheduleDtlsList = docScheduleDtlsList;
    }


    @Override
    public int getCount() {
        return docScheduleDtlsList.size();
    }

    @Override
    public Object getItem(int location) {
        return docScheduleDtlsList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        private Spinner autoDay;
        private TextView from;
        private TextView to;
        private ImageButton btnDelete;
        private int rowNum;
        private boolean userSelectedDay = false;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.edit_schedule_adapter, null);

        holder = new ViewHolder();

        holder.autoDay = (Spinner) convertView.findViewById(R.id.day);
        holder.from = (TextView) convertView.findViewById(R.id.from);
        holder.to = (TextView) convertView.findViewById(R.id.to);
        holder.btnDelete = (ImageButton) convertView.findViewById(R.id.btnscheduleDelete);
        rowCount = position;
        holder.rowNum = rowCount;

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity, R.array.week_days, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        holder.autoDay.setAdapter(adapter);

        final DocScheduleDtls docScheduleDtlsRec = docScheduleDtlsList.get(position);

        Integer dayId = Integer.parseInt(docScheduleDtlsRec.getDayId());

        final String fromTime = docScheduleDtlsRec.getTimeFrom();
        final String toTime = docScheduleDtlsRec.getTimeTo();
        String dayName = getDayNameFromId(dayId);
        holder.autoDay.setSelection(dayId);

        String fromAppointmentTime = DateUtil.getTimeIn12HrsFmt(fromTime);
        String toAppointmentTime = DateUtil.getTimeIn12HrsFmt(toTime);
        holder.from.setText(fromAppointmentTime);
        holder.to.setText(toAppointmentTime);

        holder.autoDay.setOnTouchListener(new AdapterView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                holder.userSelectedDay = true;
                return false;
            }
        });

        holder.autoDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (holder.userSelectedDay) {
                    holder.userSelectedDay = false;

                    // your code here
                    String dayName = holder.autoDay.getSelectedItem().toString();
                    Integer dayId = DayName.valueOf(dayName.toUpperCase()).ordinal();

                    String displayFromTime = holder.from.getText().toString();
                    String displayToTime = holder.to.getText().toString();

                    //convert times in 24 hour format
                    String fromTimeIn24HrsFmt = DateUtil.getTimeIn24HrsFmt(displayFromTime);
                    String toTimeIn24HrsFmt = DateUtil.getTimeIn24HrsFmt(displayToTime);

                    DocScheduleDtls scheduleDtls = createDocScheduleInAction(docScheduleDtlsRec, dayId, fromTimeIn24HrsFmt, toTimeIn24HrsFmt);
                    docScheduleDtlsList.set(holder.rowNum, scheduleDtls);

                    if (isValidTimesForSchedule(fromTimeIn24HrsFmt) && isValidTimesForSchedule(toTimeIn24HrsFmt)) {

                        holder.rowNum = rowCount;

                        boolean validSelectedTimeRange = DateUtil.isScheduleEndTimeBeforeStartTime(scheduleDtls.getTimeFrom(), scheduleDtls.getTimeTo());

                        if (validSelectedTimeRange) {
                            boolean isConflictedTime = DateUtil.isDatesInRange(docScheduleDtlsList, scheduleDtls);
                            if (isConflictedTime) {
                                //TODO Flash a message on Screen
                                String errorMsg = "Selected time " + holder.from.getText().toString() + " to " + holder.to.getText().toString() + " ,<br> conflict with existing schedules for " + dayName;
                                errorResult(errorMsg);
                            } else {
                                errorResult(null);
                                DoctorEditScheduleFragment.setUpdatedDocScheduleDtls(docScheduleDtlsList);
                            }
                        } else {
                            //TODO Flash a message on Screen
                            String errorMsg = "Schedule Start Time " + holder.from.getText().toString() + "<br> should be lesser than End Time " + holder.to.getText().toString();
                            errorResult(errorMsg);
                        }

                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                String a = "Inside item not selected";
            }

        });


        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // your action here
                //wrie a code for alert box
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                builder.setMessage("Are you sure you want to Remove?")
                        .setCancelable(false)
                        .setTitle("Remove")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                holder.autoDay.setEnabled(false);
                                holder.from.setEnabled(false);
                                holder.to.setEnabled(false);
                                DocScheduleDtls scheduleDtls = docScheduleDtlsList.get(position);
                                scheduleDtls.setDeleted(true);
                                DoctorEditScheduleFragment.setUpdatedDocScheduleDtls(docScheduleDtlsList);
                                holder.autoDay.setVisibility(View.GONE);
                                holder.from.setVisibility(View.GONE);
                                holder.to.setVisibility(View.GONE);
                                holder.btnDelete.setVisibility(View.GONE);
                                errorResult(null);

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

        });

        holder.from.setOnTouchListener(new View.OnTouchListener()

                                       {
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
                                               // from.rowNum = pos;
                                               return true;
                                           }
                                       }

        );
        holder.to.setOnTouchListener(new View.OnTouchListener()

                                     {
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
                                     }

        );

        return convertView;
    }
    public void addRow(DocScheduleDtls dtls) {

        Log.i("Schedule Adapter", "ADD row");
        //
        this.docScheduleDtlsList.add(dtls);
        notifyDataSetChanged();
        // updateView();

    }


    private DocScheduleDtls createDocScheduleInAction(DocScheduleDtls docScheduleDtlsRec, int dayId, String fromTimeIn24HrsFmt, String toTimeIn24HrsFmt) {
        DocScheduleDtls scheduleDtls = new DocScheduleDtls();//docScheduleDtlsList.get(holder.rowNum);
        scheduleDtls.setDoctorId(docScheduleDtlsRec.getDoctorId());
        if(docScheduleDtlsRec.getDocScheId() != null && docScheduleDtlsRec.getDocScheId() > 0) {
            scheduleDtls.setDocScheId(docScheduleDtlsRec.getDocScheId());
        }
        else {
            scheduleDtls.setDocScheId(0L);
        }
        scheduleDtls.setDayId(String.valueOf(dayId));
        scheduleDtls.setTimeFrom(fromTimeIn24HrsFmt);
        scheduleDtls.setTimeTo(toTimeIn24HrsFmt);
        scheduleDtls.setModified(false);
        scheduleDtls.setDeleted(false);
        return scheduleDtls;
    }

    private boolean isValidTimesForSchedule(String inputString) {
        boolean flag = false;
        if (inputString != null && !"".equalsIgnoreCase(inputString)) {
            flag = true;
        }
        return flag;
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

    TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Log.i(" onTimeSet............", "");
            pHour = hourOfDay;
            pMinutes = minute;

            if (fromFlag) {
                updateFromTime(pos);
            } else {
                updateToTime(pos);
            }

            updateView(pos, fromFlag);
        }

    };

    private void updateFromTime(int pos) {
        default_from_time = getDateTimeIn12HourFmt();
    }

    private void updateToTime(int pos) {
        default_to_time = getDateTimeIn12HourFmt();
    }

    private String getDateTimeIn12HourFmt() {
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
        return time;
    }

    public void errorResult(String errorMsgtxt) {
        TextView errorMsg = (TextView) ((Activity) activity).findViewById(R.id.errorMsg);
        ImageButton nxtButton = (ImageButton) ((Activity) activity).findViewById(R.id.nxtButton);
        ImageButton addMoreSchedule = (ImageButton) ((Activity) activity).findViewById(R.id.addMoreSchedule);
        if (errorMsgtxt == null) {
            errorMsg.setText(null);
            nxtButton.setEnabled(true);
            addMoreSchedule.setEnabled(true);
        } else {

            errorMsg.setText(Html.fromHtml(errorMsgtxt));
            nxtButton.setEnabled(false);
            addMoreSchedule.setEnabled(false);
        }
    }

    private void updateView(int index, boolean fromFlag) {

        ListView view = (ListView) activity.findViewById(R.id.lvCalender);
        View v = view.getChildAt(index - view.getFirstVisiblePosition());
        ViewHolder holder;
        if (v == null) {
            return;
        }
        holder = new ViewHolder();
        holder.from = (TextView) v.findViewById(R.id.from);
        holder.to = (TextView) v.findViewById(R.id.to);
        v.setTag(holder);

        String selecteddayName = docScheduleDtlsList.get(index).getDayId();
        Integer selecteddayId = Integer.parseInt(selecteddayName);

        if (fromFlag) {

            holder.from.setText(default_from_time);

            DocScheduleDtls scheduleDtls = docScheduleDtlsList.get(index);
            String displayFromTime = holder.from.getText().toString();
            String displayToTime = holder.to.getText().toString();

            //convert times in 24 hour format
            String fromTimeIn24HrsFmt = DateUtil.getTimeIn24HrsFmt(displayFromTime);
            String toTimeIn24HrsFmt = DateUtil.getTimeIn24HrsFmt(displayToTime);

            if (isValidTimesForSchedule(fromTimeIn24HrsFmt) && isValidTimesForSchedule(toTimeIn24HrsFmt)) {

                scheduleDtls.setTimeFrom(fromTimeIn24HrsFmt);
                scheduleDtls.setTimeTo(toTimeIn24HrsFmt);
                //check valid time
                boolean validSelectedTimeRange = DateUtil.isScheduleEndTimeBeforeStartTime(scheduleDtls.getTimeFrom(), scheduleDtls.getTimeTo());

                if (validSelectedTimeRange) {
                    boolean isConflictedTime = DateUtil.isDatesInRange(docScheduleDtlsList, scheduleDtls);
                    if (isConflictedTime) {
                        //TODO Flash a message on Screen
                        String dayName = getDayNameFromId(selecteddayId);
                        String errorMsg = "Selected time " + holder.from.getText().toString() + " to " + holder.to.getText().toString() + " ,<br> conflict with existing schedules for " + dayName;
                        errorResult(errorMsg);
                    } else {
                        errorResult(null);
                       // docScheduleDtlsList.set(holder.rowNum, scheduleDtls);
                        DoctorEditScheduleFragment.setUpdatedDocScheduleDtls(docScheduleDtlsList);
                    }
                } else {
                    //TODO Flash a message on Screen
                    String errorMsg = "Schedule Start Time " + holder.from.getText().toString() + "<br> should be lesser than End Time " + holder.to.getText().toString();
                    errorResult(errorMsg);
                }
            }
            //check here time already exist or not for that day
        } else {
            holder.to.setText(default_to_time);

            DocScheduleDtls scheduleDtls = docScheduleDtlsList.get(index);
            String displayFromTime = holder.from.getText().toString();
            String displayToTime = holder.to.getText().toString();

            //convert times in 24 hour format
            String fromTimeIn24HrsFmt = DateUtil.getTimeIn24HrsFmt(displayFromTime);
            String toTimeIn24HrsFmt = DateUtil.getTimeIn24HrsFmt(displayToTime);

            if (isValidTimesForSchedule(fromTimeIn24HrsFmt) && isValidTimesForSchedule(toTimeIn24HrsFmt)) {

                scheduleDtls.setTimeFrom(fromTimeIn24HrsFmt);
                scheduleDtls.setTimeTo(toTimeIn24HrsFmt);

                //check valid time
                boolean validSelectedTimeRange = DateUtil.isScheduleEndTimeBeforeStartTime(scheduleDtls.getTimeFrom(), scheduleDtls.getTimeTo());

                if (validSelectedTimeRange) {
                    boolean isConflictedTime = DateUtil.isDatesInRange(docScheduleDtlsList, scheduleDtls);
                    if (isConflictedTime) {
                        //TODO Flash a message on Screen
                        String dayName = getDayNameFromId(selecteddayId);
                        String errorMsg = "Selected time " + holder.from.getText().toString() + " to " + holder.to.getText().toString() + " ,<br> conflict with existing schedules for " + dayName;
                        errorResult(errorMsg);
                    } else {
                        errorResult(null);
                        // docScheduleDtlsList.set(holder.rowNum, scheduleDtls);
                        DoctorEditScheduleFragment.setUpdatedDocScheduleDtls(docScheduleDtlsList);
                    }
                } else {
                    //TODO Flash a message on Screen
                    String errorMsg = "Schedule Start Time " + holder.from.getText().toString() + "<br> should be lesser than End Time " + holder.to.getText().toString();
                    errorResult(errorMsg);
                }
            }
        }

        if (docScheduleDtlsList.get(index).getDocScheId() > 0) {
            docScheduleDtlsList.get(index).setModified(true);

            if (fromFlag) {
                String updatedToTime = docScheduleDtlsList.get(index).getTimeTo();
                docScheduleDtlsList.get(index).setTimeTo(DateUtil.getTimeIn12HrsFmt(updatedToTime));
            } else {
                String updatedFromTime = docScheduleDtlsList.get(index).getTimeFrom();
                docScheduleDtlsList.get(index).setTimeFrom(DateUtil.getTimeIn12HrsFmt(updatedFromTime));
            }

        }

    }

}
