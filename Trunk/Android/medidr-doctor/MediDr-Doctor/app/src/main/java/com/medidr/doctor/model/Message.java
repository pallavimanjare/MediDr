package com.medidr.doctor.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Message {

    public static final String MSG_SUGGESTION = "SUGGESTION";

    public static final String MSG_SENDER = "DOCTOR";

 //   public final String MSG_

    @JsonProperty("sender_id")
    private Long senderId;
    @JsonProperty("sender_type")
    private String senderType;
    @JsonProperty("msg_txt")
    private String msgText;
    @JsonProperty("msg_time")
    private Date msgTime;
    @JsonProperty("msg_type")
    private String msgType;

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getSenderType(String doctor) {
        return senderType;
    }

    public void setSenderType(String senderType) {
        this.senderType = senderType;
    }

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    public Date getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(Date msgTime) {
        this.msgTime = msgTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    @Override
    public String toString() {
        return "Message{" +
                "senderId=" + senderId +
                ", senderType='" + senderType + '\'' +
                ", msgText='" + msgText + '\'' +
                ", msgTime=" + msgTime +
                ", msgType='" + msgType + '\'' +
                '}';
    }
}
