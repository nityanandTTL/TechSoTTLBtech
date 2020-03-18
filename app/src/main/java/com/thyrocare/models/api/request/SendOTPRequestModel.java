package com.thyrocare.models.api.request;

import java.io.Serializable;

public class SendOTPRequestModel implements Serializable {

    String Mobile;
    String orderno;

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }
}
