package com.medidr.doctor.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.medidr.doctor.R;
import com.medidr.doctor.model.AppointmentDtls;
import com.medidr.doctor.model.DataManager;
import com.medidr.doctor.ui.CancelAppointmentActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecyclerViewDataAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private static ProgressDialog pDialog;
    private List<AppointmentDtls> appointmentsList;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    static Activity activity;
    private OnLoadMoreListener onLoadMoreListener;

    public RecyclerViewDataAdapter(List<AppointmentDtls> appointments, RecyclerView recyclerView , Activity activity) {
        appointmentsList = appointments;
        this.activity = activity;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();


            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!loading
                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                // End has been reached
                                // Do something
                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                                loading = true;
                            }
                        }
                    });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return appointmentsList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.appointment_list_row, parent, false);


            vh = new AppointmentDtlsViewHolder(v);

        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AppointmentDtlsViewHolder) {

            //TO do
            //formatting date and time
            AppointmentDtls appointmentDtls = (AppointmentDtls) appointmentsList.get(position);

            String strappointmentDate = appointmentDtls.getAppointmentDate();
            String strappointmentTime = appointmentDtls.getAppointmentFrom();

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



            String strPatientName = appointmentDtls.getPatientName();
            strPatientName = getFormattedName(strPatientName);
            ((AppointmentDtlsViewHolder) holder).appointmentDate.setText(formattedAppointmentDate);

            ((AppointmentDtlsViewHolder) holder).appointmentTime.setText(formattedAppointmentTime);

            ((AppointmentDtlsViewHolder) holder).patientName.setText(strPatientName);

            ((AppointmentDtlsViewHolder) holder).appointmentDtls = appointmentDtls;

            if (position % 2 == 1) {

                final CardView view = (CardView) ((AppointmentDtlsViewHolder) holder).itemView;
                view.setCardBackgroundColor(Color.LTGRAY);
                //((AppointmentDtlsViewHolder) holder).appointmentDate.setBackgroundResource(R.color.buttonColor);

                //#E6E4E4
            } else {
                final CardView view = (CardView) ((AppointmentDtlsViewHolder) holder).itemView;
                view.setCardBackgroundColor(Color.WHITE);
               // ((AppointmentDtlsViewHolder) holder).appointmentDate.setBackgroundResource(R.color.blueback);

            }

        } else {
//            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return appointmentsList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    //
    public static class AppointmentDtlsViewHolder extends RecyclerView.ViewHolder {

        public TextView appointmentDate;

        public TextView appointmentTime;

        public TextView patientName;

        public AppointmentDtls appointmentDtls;

        public AppointmentDtlsViewHolder(View v) {

            super(v);

            appointmentDate = (TextView) v.findViewById(R.id.appointment_date);

            appointmentTime = (TextView) v.findViewById(R.id.appointment_time);

            patientName = (TextView) v.findViewById(R.id.patient_name);

            v.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // On click view Appointment details screen should get opened

                    DataManager.getDataManager().setAppointmentDtls(appointmentDtls);
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
                    //write code for go to cancel ppointment details


        }
    }

    public static String getFormattedName(String name)
    {
        String strFormattedName = null;
        String str1=null,str2=null,firstNameInitial=null,firstNameInUpper=null,lastName = null;
        if(name.indexOf(" ")== -1)
        {
            if(name.length()>=10)
            {
               // name = name.substring(0,10);
                strFormattedName = name;

            }
            else
            {
                strFormattedName = name;
            }
        }
        else
        {
            str1 = name.substring(0, name.indexOf(" "));
            str2 = name.substring(name.indexOf(" "), name.length());
            firstNameInitial = str1.substring(0,1);
            firstNameInUpper = firstNameInitial.toUpperCase();

            if(str2.length() >=10)
            {
                lastName= str2;
                strFormattedName = firstNameInUpper +"."+lastName;
            }
            else {
                strFormattedName = firstNameInUpper +"."+str2;
            }
        }

        return strFormattedName;
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }
}