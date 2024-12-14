package com.medidr.doctor.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.medidr.doctor.R;
import com.medidr.doctor.model.DocScheduleDtls;
import com.medidr.doctor.model.DocServicesDtls;
import com.medidr.doctor.model.DocVacationSchedules;
import com.medidr.doctor.model.enums.DayName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Sharvee on 4/22/2016.
 */
public class VacationPlannerAdapter extends BaseAdapter {
    public List<DocVacationSchedules> list;
    private int rowCount = 0;
    Activity activity;
int nullCount = 0;
    String selectedFromDate = null;
    String selectedToDate = null;
    Calendar myCalendar = Calendar.getInstance();

    private SimpleDateFormat dateFormatter;
    private DatePickerDialog fromDatePickerDialog;

    public VacationPlannerAdapter(Activity activity, List<DocVacationSchedules> list) {
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

    public void addRow(DocVacationSchedules docVacationPlannerDtls) {

        Log.i("Vacation Adapter", "ADD row");
        this.list.add(docVacationPlannerDtls);
        //get date in vacation planner form and to date
        //if no date is selected in vacation planner block add row and save button
        if(this.list.size()>0)
        {

            for(int i=0;i<list.size();i++)
            {
                DocVacationSchedules vacation = list.get(i);
                String fromdate = vacation.getFromDate();
                String todate = vacation.getToDate();
                if(fromdate.equalsIgnoreCase("") && todate.equalsIgnoreCase(""))
                {
                    nullCount = nullCount+1;
                    if(nullCount>1)
                        break;
                }

            }
            if(nullCount>1)
            {
                TextView errorMsg = (TextView) ((Activity) activity).findViewById(R.id.errorMsg);
                ImageButton addMore = (ImageButton) ((Activity) activity).findViewById(R.id.addMore);
                Button save = (Button) ((Activity) activity).findViewById(R.id.save);
                errorMsg.setText("Vacation date should not blanked");
                addMore.setEnabled(false);
                save.setEnabled(false);
                this.list.remove(list.size() - 1);
                nullCount = 0;

            }
            else {
                TextView errorMsg = (TextView) ((Activity) activity).findViewById(R.id.errorMsg);
                ImageButton addMore = (ImageButton) ((Activity) activity).findViewById(R.id.addMore);
                Button save = (Button) ((Activity) activity).findViewById(R.id.save);
                errorMsg.setText(null);
                addMore.setEnabled(true);
                save.setEnabled(true);
                notifyDataSetChanged();
            }
        }

        // updateView();

    }

    private class ViewHolder {
        TextView fromDate;
        TextView toDate;
        private int rowNum;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        // TODO Auto-generated method stub
        final ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.vacation_planner_row, null);
            holder = new ViewHolder();
            holder.fromDate = (TextView) convertView.findViewById(R.id.fromDate);
            holder.toDate = (TextView) convertView.findViewById(R.id.toDate);
            holder.rowNum = position;

            //if already has data
            final DocVacationSchedules docVacationSchedulesRec = list.get(position);
            final String fromdate = docVacationSchedulesRec.getFromDate();
            String todate = docVacationSchedulesRec.getToDate();
            //display date in proper format


            holder.fromDate.setText(fromdate);
            holder.toDate.setText(todate);

//done with has data

            final DocVacationSchedules vacationDtls = list.get(holder.rowNum);

            holder.fromDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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
                            holder.fromDate.setText(sdf.format(myCalendar.getTime()));
                            vacationDtls.setFromDate(sdf.format(myCalendar.getTime()));
                            list.set(holder.rowNum, vacationDtls);
                            if(holder.fromDate.getText().toString().equalsIgnoreCase("") || holder.toDate.getText().toString().equalsIgnoreCase(""))
                            {
                                TextView errorMsg = (TextView) ((Activity) activity).findViewById(R.id.errorMsg);
                                ImageButton addMore = (ImageButton) ((Activity) activity).findViewById(R.id.addMore);
                                Button save = (Button) ((Activity) activity).findViewById(R.id.save);
                                errorMsg.setText("Please select Vacation From and To date");
                                addMore.setEnabled(false);
                                save.setEnabled(false);
                            }
                            else
                            {
                                TextView errorMsg = (TextView) ((Activity) activity).findViewById(R.id.errorMsg);
                                ImageButton addMore = (ImageButton) ((Activity) activity).findViewById(R.id.addMore);
                                Button save = (Button) ((Activity) activity).findViewById(R.id.save);
                                errorMsg.setText(null);
                                addMore.setEnabled(true);
                                save.setEnabled(true);
                            }
                            if(!holder.fromDate.getText().toString().equalsIgnoreCase("") && !holder.toDate.getText().toString().equalsIgnoreCase(""))
                            {
                                String fromdate = holder.fromDate.getText().toString();
                                String todate = holder.toDate.getText().toString();
                                Boolean isValidDate = CheckDates(fromdate, todate);
                                if(isValidDate) {
                                    holder.fromDate.setError(null);
                                    holder.toDate.setError(null);
                                    vacationDtls.setFromDate(sdf.format(myCalendar.getTime()));
                                    list.set(holder.rowNum, vacationDtls);
                                    ImageButton addMore = (ImageButton) ((Activity) activity).findViewById(R.id.addMore);
                                    Button save = (Button) ((Activity) activity).findViewById(R.id.save);
                                    addMore.setEnabled(true);
                                    save.setEnabled(true);
                                }
                                else
                                {
                                    holder.fromDate.setError("FromDate should be before Todate");

                                    ImageButton addMore = (ImageButton) ((Activity) activity).findViewById(R.id.addMore);
                                    Button save = (Button) ((Activity) activity).findViewById(R.id.save);
                                    addMore.setEnabled(false);
                                    save.setEnabled(false);
                                }
                            }

                        }

                    };

                    new DatePickerDialog(activity, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                }
            });

            holder.toDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                            holder.toDate.setText(sdf.format(myCalendar.getTime()));

                            String strfromdate = holder.fromDate.getText().toString();
                            String strtodate = sdf.format(myCalendar.getTime());
                            Boolean isValidDate = CheckDates(strfromdate, strtodate);
                            if(isValidDate) {
                                holder.toDate.setError(null);
                                holder.fromDate.setError(null);
                                vacationDtls.setToDate(sdf.format(myCalendar.getTime()));
                                list.set(holder.rowNum, vacationDtls);
                                ImageButton addMore = (ImageButton) ((Activity) activity).findViewById(R.id.addMore);
                                Button save = (Button) ((Activity) activity).findViewById(R.id.save);
                                addMore.setEnabled(true);
                                save.setEnabled(true);
                            }
                            else
                            {
                                holder.toDate.setError("Todate should be after fromdate");
                                ImageButton addMore = (ImageButton) ((Activity) activity).findViewById(R.id.addMore);
                                Button save = (Button) ((Activity) activity).findViewById(R.id.save);
                                addMore.setEnabled(false);
                                save.setEnabled(false);
                            }
                            if(holder.fromDate.getText().toString().equalsIgnoreCase("") || holder.toDate.getText().toString().equalsIgnoreCase(""))
                            {
                                TextView errorMsg = (TextView) ((Activity) activity).findViewById(R.id.errorMsg);
                                ImageButton addMore = (ImageButton) ((Activity) activity).findViewById(R.id.addMore);
                                Button save = (Button) ((Activity) activity).findViewById(R.id.save);
                                errorMsg.setText("Please select Vacation From and To date");
                                addMore.setEnabled(false);
                                save.setEnabled(false);
                            }
                            else
                            {
                                TextView errorMsg = (TextView) ((Activity) activity).findViewById(R.id.errorMsg);
                                ImageButton addMore = (ImageButton) ((Activity) activity).findViewById(R.id.addMore);
                                Button save = (Button) ((Activity) activity).findViewById(R.id.save);
                                errorMsg.setText(null);
                                addMore.setEnabled(true);
                                save.setEnabled(true);
                            }

                        }

                    };

                    new DatePickerDialog(activity, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        return convertView;
    }
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
