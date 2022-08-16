package com.thyrocare.btechapp.models.api.request;

public class TCVerifyCouponRequestModel {
    String CouponCode;
    int AmountDue;

    public String getCouponCode() {
        return CouponCode;
    }

    public void setCouponCode(String couponCode) {
        CouponCode = couponCode;
    }

    public int getAmountDue() {
        return AmountDue;
    }

    public void setAmountDue(int amountDue) {
        AmountDue = amountDue;
    }
}
