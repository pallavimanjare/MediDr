package com.medidr.doctor.ui;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medidr.doctor.R;
import com.medidr.doctor.adapter.CustomDoctorAppointmentAdapter;
import com.medidr.doctor.adapter.OnLoadMoreListener;
import com.medidr.doctor.adapter.RecyclerViewDataAdapter;
import com.medidr.doctor.app.AppController;
import com.medidr.doctor.model.AppointmentDtls;
import com.medidr.doctor.model.DataManager;
import com.medidr.doctor.model.DocDtls;
import com.medidr.doctor.services.DoctorService;
import com.medidr.doctor.services.NetworkUtilService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorAppointmentHomeFragment extends Fragment {


    //  private ListView appointmentListView;
    TextView doctorIdVal;
    TextView doctorNameval;
    TextView txtAppointmentLbl;
    private CustomDoctorAppointmentAdapter adapter = null;
    private ProgressDialog pDialog;

    private RecyclerView mRecyclerView;
    private RecyclerViewDataAdapter mAdapter;

    List<AppointmentDtls> appointmentList;

    //on scroll
    private static int current_offset = 0;

    DocDtls docDtls = null;
    NetworkUtilService objNetwork = null;
    Boolean isConnectionExist = false;
    ShowAlertDialog objAlert = null;
    private LinearLayoutManager mLayoutManager;

    protected Handler handler;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public DoctorAppointmentHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_doctor_appointment_home, container, false);

        handler = new Handler();

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Medidr - Doctor");
        docDtls = DataManager.getDataManager().getDoctorDetails();
        Long doctorId = docDtls.getAuthDtls().getUserId();
        String doctorName = docDtls.getDocPersonalDtls().getFullName();
//        this.appointmentListView = (ListView) v.findViewById(R.id.appointmentListView);
        this.doctorIdVal = (TextView) v.findViewById(R.id.doctorIdVal);
        this.doctorNameval = (TextView) v.findViewById(R.id.doctorNameval);
        this.txtAppointmentLbl = (TextView) v.findViewById(R.id.txtAppointmentLbl);
        doctorIdVal.setText(String.valueOf(doctorId));
        doctorNameval.setText(doctorName);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.appointmentRecycler);
        // View header = inflater.inflate(R.layout.appointment_list_header, null);
        // appointmentListView.addHeaderView(header);

        txtAppointmentLbl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //  if (event.getRawX() >= (txtAppointmentLbl.getRight() - txtAppointmentLbl.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    if (event.getRawX() <= (txtAppointmentLbl.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())) {

                        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                        drawer.openDrawer(GravityCompat.START);

                        return true;
                    } else if (event.getRawX() >= (txtAppointmentLbl.getRight() - txtAppointmentLbl.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = null;
                        Fragment fragment = null;
                        fragment = new DoctorEditScheduleFragment();
                        ft = fm.beginTransaction();
                        ft.replace(R.id.content_frame, fragment).addToBackStack(null);
                        ft.commit();
                        return true;
                    }
                }
                return false;
            }
        });


        appointmentList = new ArrayList<AppointmentDtls>();
        loadData(current_offset);
        //getAppointmentListForDoctor();

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());

        // use a linear layout manager
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerViewDataAdapter(appointmentList, mRecyclerView,getActivity());
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom
                appointmentList.add(null);
                mAdapter.notifyItemInserted(appointmentList.size() - 1);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //   remove progress item
                        appointmentList.remove(appointmentList.size() - 1);
                        mAdapter.notifyItemRemoved(appointmentList.size());
                        //add items one by one
                        int start = appointmentList.size();
                        loadMoreData(start);

                        mAdapter.notifyItemInserted(appointmentList.size());
                        mAdapter.setLoaded();
                        //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                    }
                }, 2000);
            }
        });

        CircularNetworkImageView docProfileImg = (CircularNetworkImageView) v.findViewById(R.id.docProfileImg);
        docProfileImg.setImageUrl(docDtls.getDocPersonalDtls().getDoctorProfileThumbnailImage(), imageLoader);

        return v;
    }


    public void loadData(int current_page) {

        objNetwork = new NetworkUtilService(getActivity());
        objAlert = new ShowAlertDialog();
        isConnectionExist = objNetwork.checkMobileInternetConn();
        if (isConnectionExist) {
            docDtls = DataManager.getDataManager().getDoctorDetails();
            Long doctorId = docDtls.getAuthDtls().getUserId();

            current_offset = 0;
            if (adapter != null) {
                current_offset = adapter.getCount();
            }
            DoctorService service = new DoctorService();
            String path = "/doctors/getAppointmentDetailsForDoctor/" + doctorId + "?offset=" + current_offset + "&limit=5";
            String responseResult = service.getInformationForDoctor(path);

            if (responseResult != null) {
                //assign data to listview

                try {
                    JSONObject jsonResponse = null;

                    jsonResponse = new JSONObject(responseResult);

                    String strheader = jsonResponse.getString("header");

                    JSONObject jsonObjectHeader = new JSONObject(strheader);
                    String statusCode = jsonObjectHeader.getString("statusCode");
                    String statusMessage = jsonObjectHeader.getString("statusMessage");

                    if (statusMessage.equalsIgnoreCase("SUCCESS") && statusCode.equalsIgnoreCase("200")) {
                        try {
                            JSONArray appointmentDtlsArr = jsonResponse.getJSONArray("data");

                            ObjectMapper mapper = new ObjectMapper();
                            List<AppointmentDtls> appointmentDtlsLst = mapper.readValue(appointmentDtlsArr.toString(), new TypeReference<List<AppointmentDtls>>() {
                            });
                            appointmentList.addAll(appointmentDtlsLst);

                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            objAlert.showAlertDialog(getActivity(), "No Internet Connection",
                    "check your connection and try again", false);
        }


    }

    public void loadMoreData(int current_offset) {
        objNetwork = new NetworkUtilService(getActivity());
        objAlert = new ShowAlertDialog();
        isConnectionExist = objNetwork.checkMobileInternetConn();
        if (isConnectionExist) {
            docDtls = DataManager.getDataManager().getDoctorDetails();
            Long doctorId = docDtls.getAuthDtls().getUserId();

/*            int lastNumber = 0;
            if(adapter != null) {
                lastNumber = adapter.getCount();
            }*/
            DoctorService service = new DoctorService();
            String path = "/doctors/getAppointmentDetailsForDoctor/" + doctorId + "?offset=" + current_offset + "&limit=5";
            String responseResult = service.getInformationForDoctor(path);

            if (responseResult != null) {
                //assign data to listview

                try {
                    JSONObject jsonResponse = null;

                    jsonResponse = new JSONObject(responseResult);

                    String strheader = jsonResponse.getString("header");

                    JSONObject jsonObjectHeader = new JSONObject(strheader);
                    String statusCode = jsonObjectHeader.getString("statusCode");
                    String statusMessage = jsonObjectHeader.getString("statusMessage");

                    if (statusMessage.equalsIgnoreCase("SUCCESS") && statusCode.equalsIgnoreCase("200")) {
                        try {
                            JSONArray appointmentDtlsArr = jsonResponse.getJSONArray("data");

                            ObjectMapper mapper = new ObjectMapper();
                            List<AppointmentDtls> appointmentDtlsLst = mapper.readValue(appointmentDtlsArr.toString(), new TypeReference<List<AppointmentDtls>>() {
                            });

                            appointmentList.addAll(appointmentDtlsLst);
/*                            mAdapter.notifyDataSetChanged();
                            mAdapter.notifyItemInserted(appointmentDtlsLst.size() - 1);*/

                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            objAlert.showAlertDialog(getActivity(), "No Internet Connection",
                    "check your connection and try again", false);
        }
    }

}
