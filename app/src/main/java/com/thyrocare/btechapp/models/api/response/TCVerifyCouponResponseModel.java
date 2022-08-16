package com.thyrocare.btechapp.models.api.response;

public class TCVerifyCouponResponseModel {
    String CouponCode;
    Boolean IsCouponApplicable;
    Integer AmountDue, Discountvalue, Finalvalue;

    public String getCouponCode() {
        return CouponCode;
    }

    public void setCouponCode(String couponCode) {
        CouponCode = couponCode;
    }

    public Boolean getCouponApplicable() {
        return IsCouponApplicable;
    }

    public void setCouponApplicable(Boolean couponApplicable) {
        IsCouponApplicable = couponApplicable;
    }

    public Integer getAmountDue() {
        return AmountDue;
    }

    public void setAmountDue(Integer amountDue) {
        AmountDue = amountDue;
    }

    public Integer getDiscountvalue() {
        return Discountvalue;
    }

    public void setDiscountvalue(Integer discountvalue) {
        Discountvalue = discountvalue;
    }

    public Integer getFinalvalue() {
        return Finalvalue;
    }

    public void setFinalvalue(Integer finalvalue) {
        Finalvalue = finalvalue;
    }
}
