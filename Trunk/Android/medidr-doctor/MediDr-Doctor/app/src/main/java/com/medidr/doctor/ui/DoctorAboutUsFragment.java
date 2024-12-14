package com.medidr.doctor.ui;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import com.medidr.doctor.R;

import java.nio.InvalidMarkException;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorAboutUsFragment extends Fragment  {




    TextView lblaboutUs;
    ImageView imgBack, imgShare;

    public DoctorAboutUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_doctor_about_us, container, false);



        imgBack = (ImageView) v.findViewById(R.id.imgBack);
        imgShare = (ImageView) v.findViewById(R.id.imgShare);
        lblaboutUs = (TextView) v.findViewById(R.id.lblaboutUs);
        String txttv = lblaboutUs.getText().toString();
        lblaboutUs.setText(Html.fromHtml(txttv));

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });
        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareBody =  "MediDr Doctor app will increases Doctor's popularity in the online community and in the medical fraternity. Download FREE - https://play.google.com/store/apps/details?id=com.medidr.doctor";
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                //sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "MediDr is exclusive user friendly android app for public  which will help them to Search Doctors,Clinics,Hospital.Get free Doctors appointment.Download https://googleplaystore/share/medidr app");

                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }

        });
        return v;


    }

}
