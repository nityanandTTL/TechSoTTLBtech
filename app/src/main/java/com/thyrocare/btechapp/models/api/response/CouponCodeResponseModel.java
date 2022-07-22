package com.thyrocare.btechapp.models.api.response;

import java.util.ArrayList;

public class CouponCodeResponseModel {
    public ArrayList<Coupons> Data;

    public ArrayList<Coupons> getData() {
        return Data;
    }

    public void setData(ArrayList data) {
        Data = data;
    }

    public static class Coupons {
        String CouponCode, CouponSubHead, CouponHead;

        public String getCouponCode() {
            return CouponCode;
        }

        public void setCouponCode(String couponCode) {
            CouponCode = couponCode;
        }

        public String getCouponSubHead() {
            return CouponSubHead;
        }

        public void setCouponSubHead(String couponSubHead) {
            CouponSubHead = couponSubHead;
        }

        public String getCouponHead() {
            return CouponHead;
        }

        public void setCouponHead(String couponHead) {
            CouponHead = couponHead;
        }
    }
}
