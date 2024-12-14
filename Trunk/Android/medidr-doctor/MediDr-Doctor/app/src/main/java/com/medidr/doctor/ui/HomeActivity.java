package com.medidr.doctor.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;

import com.android.volley.toolbox.ImageLoader;

import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.medidr.doctor.R;
import com.medidr.doctor.app.AppController;
import com.medidr.doctor.gcm.QuickstartPreferences;
import com.medidr.doctor.gcm.RegistrationIntentService;
import com.medidr.doctor.model.AuthDtls;
import com.medidr.doctor.model.DataManager;
import com.medidr.doctor.model.DocDtls;
import com.medidr.doctor.services.DoctorService;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private SharedPreferences tokenPreferences;
    private SharedPreferences.Editor tokenPrefsEditor;
    private Boolean isTokenGenerated;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;

    //clear login preferences on sign out
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    DocDtls docDtls = null;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.header_logo);

        docDtls = DataManager.getDataManager().getDoctorDetails();
        //******************Token generattion Code ******************************//

        tokenPreferences = getSharedPreferences("tokenPrefs", MODE_PRIVATE);
        tokenPrefsEditor = tokenPreferences.edit();
        isTokenGenerated = tokenPreferences.getBoolean("isTokenGenerated", false);
        if (isTokenGenerated == false) {
            tokenPrefsEditor.putBoolean("isTokenGenerated", true);
            tokenPrefsEditor.commit();
            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    SharedPreferences sharedPreferences =
                            PreferenceManager.getDefaultSharedPreferences(context);
                    boolean sentToken = sharedPreferences
                            .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                    String strToken = sharedPreferences.getString(QuickstartPreferences.DEVICE_TOKEN_VALUE, null);

                    if (strToken != "" || strToken != null) {

                        try {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                    .permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                            AuthDtls authDtls = new AuthDtls();
                            authDtls.setUserId(docDtls.getAuthDtls().getUserId());
                            authDtls.setUserType("DOCTOR");
                            authDtls.setGcmToken(strToken);
                            DoctorService docService = new DoctorService();
                            String response = docService.updateGCMTokenForDoctor(authDtls);

                        } catch (Exception exception) {
                            exception.printStackTrace();

                        }
                        if (sentToken) {
                        } else {
                        }

                    }
                }

            };

            registerReceiver();
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        } else if (isTokenGenerated == true) {

        }


        //******************Done with Token generattion Code ******************************//
        //write a code for open close drawer

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = null;
        Fragment fragment = null;
        fragment = new DoctorAppointmentHomeFragment();
        ft = fm.beginTransaction();
        ft.replace(R.id.content_frame, fragment).addToBackStack(null);
        ft.commit();

        //done with fragments

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_home);
        CircularNetworkImageView docProfileImg = (CircularNetworkImageView) headerView.findViewById(R.id.docProfileImg);
        TextView navheaderdoctorName = (TextView) headerView.findViewById(R.id.navheaderdoctorName);
        TextView navheaderdocId = (TextView) headerView.findViewById(R.id.navheaderdocId);

        navheaderdocId.setText(String.valueOf(docDtls.getAuthDtls().getUserId()));
        navheaderdoctorName.setText(docDtls.getDocPersonalDtls().getFullName());
        if (docDtls.getDocPersonalDtls().getDoctorProfileThumbnailImage() == null) {
            docProfileImg.setBackgroundResource(R.drawable.doctor_profile_pic);
        } else {
            docProfileImg.setImageUrl(docDtls.getDocPersonalDtls().getDoctorProfileThumbnailImage(), imageLoader);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Are you sure you want to EXIT?")
                    .setCancelable(false)
                    .setTitle("Exit")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                            loginPrefsEditor = loginPreferences.edit();
                            loginPrefsEditor.putBoolean("saveLogin", false);
                            loginPrefsEditor.putBoolean("isBack", true);
                            loginPrefsEditor.commit();
                            tokenPrefsEditor.clear().commit();
                            finishAffinity();
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
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        //if (id == R.id.action_settings) {
        menu.findItem(R.id.action_settings).setVisible(false);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {

                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = null;
        Fragment fragment = null;

        if (id == R.id.nav_personal_info) {
            // Handle the camera action
            fragment = new DoctorEditPersonalInfoFragment();
            ft = fm.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null);
            ft.commit();
            setTitle(item.getTitle());

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        } else if (id == R.id.nav_profile_photo) {
            fragment = new DoctorProfilePhotoFragment();
            ft = fm.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null);
            ft.commit();
            setTitle(item.getTitle());

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;

        } else if (id == R.id.nav_hospital_info) {
            fragment = new DoctorHospitalInfoFragment();
            ft = fm.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null);
            ft.commit();
            setTitle(item.getTitle());

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;

        } else if (id == R.id.nav_services) {
            fragment = new DoctorServicesFragment();
            ft = fm.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null);
            ft.commit();
            setTitle(item.getTitle());

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;

        } else if (id == R.id.nav_service_types) {
            fragment = new DoctorServiceTypeFragment();
            ft = fm.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null);
            ft.commit();
            setTitle(item.getTitle());

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;

        } else if (id == R.id.nav_edit_schedule) {
            fragment = new DoctorEditScheduleFragment();
            ft = fm.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null);
            ft.commit();
            setTitle(item.getTitle());

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;

        } else if (id == R.id.nav_my_vacation) {
            fragment = new MyVacationFragment();
            ft = fm.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null);
            ft.commit();
            setTitle(item.getTitle());

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;

        } else if (id == R.id.nav_report_request) {

            fragment = new DoctorReportRequestFragment();
            ft = fm.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null);
            ft.commit();
            setTitle(item.getTitle());

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        } else if (id == R.id.nav_about_is) {

            fragment = new DoctorAboutUsFragment();
            ft = fm.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null);
            ft.commit();
            setTitle(item.getTitle());

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        } else if (id == R.id.nav_suggestions) {

            fragment = new DoctorSuggestionsFragment();
            ft = fm.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null);
            ft.commit();
            setTitle(item.getTitle());

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        } else if (id == R.id.nav_faq) {

            fragment = new DoctorFaqFragment();
            ft = fm.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null);
            ft.commit();
            setTitle(item.getTitle());

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        } else if (id == R.id.nav_signout) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Are you sure you want to Sign Out of MediDr Doctor App?")
                    .setCancelable(false)
                    .setTitle("Sign Out")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                            loginPrefsEditor = loginPreferences.edit();
                            loginPrefsEditor.putBoolean("saveLogin", true);
                            loginPrefsEditor.putBoolean("isBack", false);
                            loginPrefsEditor.commit();
                            tokenPrefsEditor.clear().commit();
                            finishAffinity();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

            return true;
        }

        return true;
    }
}
