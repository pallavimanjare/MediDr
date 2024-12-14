package com.medidr.doctor.ui;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.medidr.doctor.R;
import com.medidr.doctor.adapter.ExpandableListFAQ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorFaqFragment extends Fragment {

    ExpandableListFAQ listAdapter;
    ExpandableListView lvExpfaq;
    List<String> listDataHeader;
    String userDetailsStr;
    HashMap<String, List<String>> listDataChild;

    public DoctorFaqFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_doctor_faq, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((HomeActivity) getActivity()).setSupportActionBar(toolbar);
        ((HomeActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((HomeActivity) getActivity()).getSupportActionBar().setIcon(R.mipmap.header_logo);
        toolbar.setNavigationIcon(R.mipmap.ic_action_arrow_left);
        // ((HomeActivity) getActivity()).toolbar.setNavigationIcon(R.mipmap.close_button_copy);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getActivity().onBackPressed();
                Intent intent = new Intent(getActivity(),HomeActivity.class);
                startActivity(intent);
            }
        });
        toolbar.setTitle("FAQ");
        lvExpfaq = (ExpandableListView) v.findViewById(R.id.lvExpfaq);
        prepareListData();
        listAdapter = new ExpandableListFAQ(getActivity(), listDataHeader, listDataChild);
        // setting list adapter

        lvExpfaq.setAdapter(listAdapter);
        return v;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("  How to register my profile & service with Medidr Doctor Application ?");
        listDataHeader.add("  Are there any conditions for registration ?");
        listDataHeader.add("  Will the patient receive a confirmation message, after making an appointment ?");
        listDataHeader.add("  How do I get know about an appointment or call  from MediDr app ?");
        listDataHeader.add("  Can I update my MediDr profile ?");
        listDataHeader.add("  Can I manage my OPD schedule ?");
        listDataHeader.add("  Can I set my vacation dates ?");
        listDataHeader.add("  Will I get Patient appointment record ?");
        listDataHeader.add("  I practice in multiple clinics. How do I include and show them ?");
        listDataHeader.add("  I am interested in advertising my Clinic on MediDr , What next ?");
        listDataHeader.add("  What is your helpline number ?");


        // Adding child data
        List<String> firstq = new ArrayList<String>();
        firstq.add(" You can download 'MediDr Doctor' app from Google play store, it is free to download. You can create your new to MediDr Doctor Login and register your profile & services.");

        List<String> secondq = new ArrayList<String>();
        secondq.add("We do take nominal yearly subscription charges for registration the profile or for subsequently booking appointments with you. The basic conditions for registration are: That you agree with the terms and conditions of use as specified by  MediDr. That you keep your information relevant with regard. You honor the appointments that you confirm through our booking process and intimate us if there is a cancellation required due to unforeseen circumstances. You adequately inform your staff and clinic who will handle patients about  MediDr appointments and how to handle them.");


        List<String> thirdq = new ArrayList<String>();
        thirdq.add("Yes they will. Every appointment is confirmed SMS, specifying the appointment time and Doctor chosen.");

        List<String> fourq = new ArrayList<String>();
        fourq.add("Every time MediDr system will send confirm or cancel an appointment by SMS / Email/ Notifications to the Patient and Doctor.");


        List<String> fiveq = new ArrayList<String>();
        fiveq.add(" Yes, you can update your profile any time.");

        List<String> sixq = new ArrayList<String>();
        sixq.add("Yes, you can manage your own OPD schedule any time.");

        List<String> sevenq = new ArrayList<String>();
        sevenq.add(" Yes, you can set your vacation dates on that days patients will not take your appointment.");

        List<String> eightq = new ArrayList<String>();
        eightq.add("Yes, you can get report at menu of app portal .");

        List<String> nineq = new ArrayList<String>();
        nineq.add("By default your account allows you to add only one clinic on your Profile. If you attach another clinic to be added, please add in attached hospital address and others your information.");

        List<String> tenq = new ArrayList<String>();
        tenq.add("Great! Thanks for choosing us to promote your Clinic. You can just send mail care@medidr.in with subject to sponcered Clinic at our team will communicate with you. ");

        List<String> elevenq = new ArrayList<String>();
        elevenq.add("Our Help line number is 9146236467 and lines are open from 9 AM to 9 PM, all days of the week or send your email care@medidr.in");

        listDataChild.put(listDataHeader.get(0), firstq); // Header, Child data
        listDataChild.put(listDataHeader.get(1), secondq);
        listDataChild.put(listDataHeader.get(2), thirdq);
        listDataChild.put(listDataHeader.get(3), fourq);
        listDataChild.put(listDataHeader.get(4), fiveq);
        listDataChild.put(listDataHeader.get(5), sixq);
        listDataChild.put(listDataHeader.get(6), sevenq);
        listDataChild.put(listDataHeader.get(7), eightq);
        listDataChild.put(listDataHeader.get(8), nineq);
        listDataChild.put(listDataHeader.get(9), tenq);
        listDataChild.put(listDataHeader.get(10),elevenq);

    }

}
