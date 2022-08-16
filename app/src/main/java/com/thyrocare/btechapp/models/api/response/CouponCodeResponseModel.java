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
        Boolean Status;
        int MinRate;
        int MaxRate;

        public int getDiscount() {
            return Discount;
        }

        public void setDiscount(int discount) {
            Discount = discount;
        }

        int Discount;

        public int getMinRate() {
            return MinRate;
        }

        public void setMinRate(int minRate) {
            MinRate = minRate;
        }

        public int getMaxRate() {
            return MaxRate;
        }

        public void setMaxRate(int maxRate) {
            MaxRate = maxRate;
        }

        public Boolean getStatus() {
            return Status;
        }

        public void setStatus(Boolean status) {
            Status = status;
        }

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
