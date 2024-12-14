package com.medidr.doctor.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.medidr.doctor.R;
import com.medidr.doctor.model.DataManager;
import com.medidr.doctor.model.Message;
import com.medidr.doctor.services.DoctorService;

import java.util.Calendar;

public class SuggestionActivity extends Activity implements View.OnClickListener{

    private View sendButton;

    private View callButton;

    private EditText suggestionText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
        initUI();
    }

    private  void initUI(){

        suggestionText = (EditText) findViewById(R.id.suggestionText);

        sendButton = findViewById(R.id.send_suggestion);

        callButton = findViewById(R.id.call_suggestion);

        sendButton.setOnClickListener(this);
        callButton.setOnClickListener(this);



    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.send_suggestion:
                if(checkFields()){
                   sendMessage();
                }
                break;

            case R.id.call_suggestion:
                callForSuggestion();
                break;

            default:
                break;
        }
    }


    private void sendMessage(){

        Message message = new Message();

        message.setMsgText(suggestionText.getText().toString());
        message.setMsgTime(Calendar.getInstance().getTime());
        message.setSenderId(DataManager.getInstance().getPersonalDetails().getDoctorId());
        message.setSenderType(Message.MSG_SENDER);
        message.setMsgType(Message.MSG_SUGGESTION);

        DoctorService service = new DoctorService();
        service.sendMessage(this,message);


    }

    private void callForSuggestion(){

    }

    private Boolean checkFields(){

        String msg = suggestionText.getText().toString();

        if(msg.length() == 0 || msg.trim().length() ==0){
            suggestionText.requestFocus();
            String errorMsg = "Please provide suggestions!!!";
            suggestionText.setError(errorMsg);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
