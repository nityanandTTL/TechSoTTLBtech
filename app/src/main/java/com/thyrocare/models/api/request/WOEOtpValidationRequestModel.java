package com.thyrocare.models.api.request;

import java.io.Serializable;

public class WOEOtpValidationRequestModel implements Serializable {

    String otp;
    String otpTo;
    String orderno;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
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
}
