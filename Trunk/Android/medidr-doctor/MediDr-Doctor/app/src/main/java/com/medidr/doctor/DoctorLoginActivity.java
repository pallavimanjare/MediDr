package com.medidr.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;

import com.medidr.doctor.ui.NavigationDrawer;
import com.medidr.doctor.ui.RegisterActivity;
import com.medidr.doctor.ui.WeeklySchedule;
import com.medidr.doctor.ui.login;


public class DoctorLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      Intent intent = new Intent(getApplicationContext(),
              login.class);
        startActivity(intent);
    }

}
