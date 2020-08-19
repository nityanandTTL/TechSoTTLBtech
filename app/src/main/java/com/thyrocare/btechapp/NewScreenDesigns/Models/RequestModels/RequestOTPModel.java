package com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels;

import java.io.Serializable;

public class RequestOTPModel implements Serializable {

    String api_key;
    String type;
    String mobile;
    String AccessToken;
    String otpTo;
    String orderno;
    String ReqId;


    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAccessToken() {
        return AccessToken;
    }

    public void setAccessToken(String accessToken) {
        AccessToken = accessToken;
    }

    public String getOtpTo() {
        return otpTo;
    }

    public void setOtpTo(String otpTo) {
        this.otpTo = otpTo;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getReqId() {
        return ReqId;
    }

    public void setReqId(String reqId) {
        ReqId = reqId;
    }
}
