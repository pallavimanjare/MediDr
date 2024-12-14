package com.medidr.doctor.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.medidr.doctor.R;
import com.medidr.doctor.adapter.HospitalServiceAdapter;
import com.medidr.doctor.model.DataManager;
import com.medidr.doctor.model.DocDtls;
import com.medidr.doctor.model.DocScheduleDtls;
import com.medidr.doctor.model.DocServicesDtls;
import com.medidr.doctor.services.DoctorService;
import com.medidr.doctor.services.NetworkUtilService;

import java.util.ArrayList;
import java.util.List;

public class HospitalServicesActivity extends AppCompatActivity {

    private View nxtButton = null;

    ArrayList<String> mainList = new ArrayList<String>();



    private View addMoreButton = null;

    ListView listView = null;
    TextView errorMsg;

    private TextView defaultServices = null;

    NetworkUtilService objNetwork = null;
    Boolean isConnectionExist = false;
    ShowAlertDialog objAlert = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_services);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.header_logo);
       /* toolbar.setNavigationIcon(R.mipmap.ic_action_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
*/
        errorMsg = (TextView)findViewById(R.id.errorMsg);
        errorMsg.setText(null);
        addMoreButton = findViewById(R.id.add_more);
        //  mainList.add("X-Ray");
        listView = (ListView)findViewById(R.id.hosp_service_listView);
        listView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });

        final HospitalServiceAdapter adapter = new HospitalServiceAdapter(this,mainList);
        listView.setAdapter(adapter);

        addMoreButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //check size of adapter
                int size = adapter.getCount();
                if(size > 15)
                {
                    errorMsg.setText("You can add services upto 15");
                    errorMsg.requestFocus();

                }
                else {
                    errorMsg.setText(null);
                    adapter.addRow("X - Ray");
                }
            }
        });

        nxtButton = findViewById(R.id.hospital_service_nxt);
        nxtButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                objNetwork = new NetworkUtilService(HospitalServicesActivity.this);
                objAlert = new ShowAlertDialog();
                isConnectionExist = objNetwork.checkMobileInternetConn();
                if (isConnectionExist) {
                    DocDtls docDtls = DataManager.getDataManager().getDoctorDetails();
                    List<DocServicesDtls> servicesDtlsList = new ArrayList<DocServicesDtls>();
                    for (String serviceName : mainList) {
                        DocServicesDtls serviceDtls = new DocServicesDtls();
                        serviceDtls.setDoctorId(docDtls.getAuthDtls().getUserId());
                        serviceDtls.setDocServiceId(0L);
                        serviceDtls.setServiceName(serviceName);
                        serviceDtls.setServiceActiveFlag(true);
                        servicesDtlsList.add(serviceDtls);
                    }
                    docDtls.setDocServicesDtls(servicesDtlsList);
                    DoctorService service = new DoctorService();
                    service.addDoctorServicesDetails(getApplicationContext(), docDtls);

                    Toast.makeText(getApplicationContext(),
                            "Your services are Saved", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(HospitalServicesActivity.this, ServiceActivity.class);
                    startActivity(intent);

                }
                else {
                    objAlert.showAlertDialog(HospitalServicesActivity.this, "No Internet Connection",
                            "check your connection and try again", false);
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

        }
        return false;
    }

}
