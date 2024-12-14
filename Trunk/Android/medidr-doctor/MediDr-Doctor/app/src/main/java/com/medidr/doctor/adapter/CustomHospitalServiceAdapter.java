package com.medidr.doctor.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.medidr.doctor.R;
import com.medidr.doctor.model.AppointmentDtls;
import com.medidr.doctor.model.DataManager;
import com.medidr.doctor.model.DocScheduleDtls;
import com.medidr.doctor.model.DocServicesDtls;
import com.medidr.doctor.ui.CancelAppointmentActivity;
import com.medidr.doctor.ui.DoctorEditScheduleFragment;
import com.medidr.doctor.ui.DoctorServicesFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Sharvee on 4/21/2016.
 */
public class CustomHospitalServiceAdapter extends BaseAdapter {
    private static final String TAG = "LogMessages";
    private Activity activity;
    private LayoutInflater inflater;
    private List<DocServicesDtls> docServicesDtls;
    private List<DocServicesDtls> updatedDocServiceDtls = new ArrayList<DocServicesDtls>();
    private int count =0;

    private List<DocServicesDtls> docServiceDtlsPerm;
    private ProgressDialog pDialog;

    public CustomHospitalServiceAdapter(Activity activity, List<DocServicesDtls> docServicesDtls) {
        this.activity = activity;
        this.docServicesDtls = docServicesDtls;

    }

    @Override
    public int getCount() {
        return docServicesDtls.size();
    }

    @Override
    public Object getItem(int location) {
        return docServicesDtls.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        EditText edServiceName;
        private int rowNum;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        LayoutInflater inflater =  activity.getLayoutInflater();

        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.hospital_services_list_row, null);

            holder = new ViewHolder();
            holder.edServiceName = (EditText) convertView.findViewById(R.id.edServiceName);

            //holder.btnserviceDelete = (ImageButton) convertView.findViewById(R.id.btnserviceDelete);
            holder.rowNum = position;

            final DocServicesDtls docServicesDtlsRec = docServicesDtls.get(position);
            String strServiceName = docServicesDtlsRec.getServiceName();

            holder.edServiceName.setText(strServiceName);

/*            holder.edServiceName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                    if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                            actionId == EditorInfo.IME_ACTION_DONE ||
                            event.getAction() == KeyEvent.ACTION_DOWN &&
                            event.getKeyCode() == KeyEvent.KEYCODE_ENTER || event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                        Log.d(TAG, "inside edittext action listener");
                        // Do whatever you want here
                        DocServicesDtls serviceDtls = new DocServicesDtls();
                        serviceDtls.setDoctorId(docServicesDtlsRec.getDoctorId());
                        serviceDtls.setDocServiceId(Long.parseLong("0"));
                        serviceDtls.setServiceName(holder.edServiceName.getText().toString());
                        serviceDtls.setServiceActiveFlag(false);
                        serviceDtls.setModified(false);
                        updatedDocServiceDtls.add(serviceDtls);
                        DoctorServicesFragment.setUpdatedServiceDtlsValues(updatedDocServiceDtls);
                        return true;
                    }
                    return false;

                }
            });*/



            holder.edServiceName.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int DRAWABLE_LEFT = 0;
                    final int DRAWABLE_TOP = 1;
                    final int DRAWABLE_RIGHT = 2;
                    final int DRAWABLE_BOTTOM = 3;

                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (event.getRawX() >= (holder.edServiceName.getRight() - holder.edServiceName.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                            builder.setMessage("Are you sure you want to Remove?")
                                    .setCancelable(false)
                                    .setTitle("Remove")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            holder.edServiceName.setFocusable(false);
                                            DocServicesDtls serviceDtls = new DocServicesDtls();
                                            serviceDtls.setDoctorId(docServicesDtlsRec.getDoctorId());
                                            serviceDtls.setDocServiceId(docServicesDtlsRec.getDocServiceId());
                                            serviceDtls.setServiceName(holder.edServiceName.getText().toString());
                                            serviceDtls.setServiceActiveFlag(docServicesDtlsRec.getServiceActiveFlag());
                                            serviceDtls.setDeleted(true);
                                            docServicesDtls.set(holder.rowNum, serviceDtls);
                                            holder.edServiceName.setEnabled(false);
                                            holder.edServiceName.setVisibility(View.GONE);

                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();


                        }
                    }
                    return false;
                }
            });

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.edServiceName.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {

                    String enteredServiceName = holder.edServiceName.getText().toString();

                    DocServicesDtls serviceDtls = docServicesDtls.get(holder.rowNum);
                    Long ID = serviceDtls.getDocServiceId();
                    if(ID == 0)
                    {
                        serviceDtls.setModified(false);
                        serviceDtls.setServiceName(enteredServiceName);
                    }
                    else if(ID != 0)
                    {
                        if(serviceDtls.getDocServiceId()!=0) {
                            serviceDtls.setModified(true);
                            serviceDtls.setServiceName(enteredServiceName);
                        }
                    }


                    docServicesDtls.set(holder.rowNum, serviceDtls);
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
