package com.medidr.doctor.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.medidr.doctor.R;
import com.medidr.doctor.model.AppointmentDtls;
import com.medidr.doctor.model.AuthDtls;
import com.medidr.doctor.model.DataManager;
import com.medidr.doctor.model.DocDtls;
import com.medidr.doctor.model.DocProfileDtls;
import com.medidr.doctor.services.DoctorService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CancelAppointmentActivity extends AppCompatActivity {

   private TextView appointmentDateVal;
    private TextView         appointmentTimeVal;
    private TextView appointmentIdVal;
    private TextView patientNameVal;
    private TextView patientMobileVal;
    private TextView userNameVal;
    private TextView userMobileVal;

    private ImageButton btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_appointment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.header_logo);
        toolbar.setNavigationIcon(R.mipmap.ic_action_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        DocDtls docDtls = DataManager.getDataManager().getDoctorDetails();
        final AppointmentDtls appointmentDtls = DataManager.getDataManager().getAppointmentDtls();
        appointmentDtls.setDoctorMobileNumber(docDtls.getAuthDtls().getMobile());
        this.appointmentDateVal = (TextView) findViewById(R.id.appointmentDateVal);
        this.appointmentTimeVal = (TextView) findViewById(R.id.appointmentTimeVal);
        this.appointmentIdVal = (TextView) findViewById(R.id.appointmentIdVal);
        this.patientNameVal = (TextView) findViewById(R.id.patientNameVal);
        this.patientMobileVal = (TextView) findViewById(R.id.patientMobileVal);
        this.userNameVal = (TextView) findViewById(R.id.userNameVal);
        this.userMobileVal = (TextView) findViewById(R.id.userMobileVal);
        this.btnCancel = (ImageButton) findViewById(R.id.btnCancel);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("E, dd MMM");
        String formattedAppointmentDate = null;
        String dateInString = appointmentDtls.getAppointmentDate();

        try {
            Date date = formatter.parse(dateInString);
            formattedAppointmentDate = dateFormatter.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        appointmentDateVal.setText(formattedAppointmentDate);

        String formattedAppointmentTime = null;
        SimpleDateFormat date12Format = new SimpleDateFormat("hh:mma");
        SimpleDateFormat date24Format = new SimpleDateFormat("HH:mm");
        try {
            formattedAppointmentTime = date12Format.format(date24Format.parse(appointmentDtls.getAppointmentFrom()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        appointmentTimeVal.setText(formattedAppointmentTime);
        appointmentIdVal.setText(appointmentDtls.getAppointmentId().toString());
        patientNameVal.setText(appointmentDtls.getPatientName());
        patientMobileVal.setText(appointmentDtls.getMobileNumber());
        userNameVal.setText(appointmentDtls.getUserName());
        userMobileVal.setText(appointmentDtls.getUserMobileNumber());


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //write a code for cancel appointment
                AlertDialog.Builder builder = new AlertDialog.Builder(CancelAppointmentActivity.this);

                builder.setMessage("Are you sure you want to cancel appointment?")
                        .setCancelable(false)
                        .setTitle("Appointment Cancellation")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DoctorService docService = new DoctorService();
                                String responseResult = docService.cancelAppointment(appointmentDtls);
                                Intent intent = new Intent(CancelAppointmentActivity.this, HomeActivity.class);
                                startActivity(intent);
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
        });
    }
}
