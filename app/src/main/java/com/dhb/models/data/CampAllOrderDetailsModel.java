package com.dhb.models.data;

import java.util.ArrayList;

/**
 * Created by vendor1 on 5/15/2017.
 */

public class CampAllOrderDetailsModel {
    private int BrandId, Margin, Discount, Refcode, ReportHC, Distance;
    private String OrderNo, Address, Pincode, Mobile, Email, PayType, AmountDue, Status, Longitude, Latitude;
    private ArrayList<CampDetailsBenMasterModel> benMaster;

    public int getBrandId() {
        return BrandId;
    }

    public void setBrandId(int brandId) {
        BrandId = brandId;
    }

    public int getMargin() {
        return Margin;
    }

    public void setMargin(int margin) {
        Margin = margin;
    }

    public int getDiscount() {
        return Discount;
    }

    public void setDiscount(int discount) {
        Discount = discount;
    }

    public int getRefcode() {
        return Refcode;
    }

    public void setRefcode(int refcode) {
        Refcode = refcode;
    }

    public int getReportHC() {
        return ReportHC;
    }

    public void setReportHC(int reportHC) {
        ReportHC = reportHC;
    }

    public int getDistance() {
        return Distance;
    }

    public void setDistance(int distance) {
        Distance = distance;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPayType() {
        return PayType;
    }

    public void setPayType(String payType) {
        PayType = payType;
    }

    public String getAmountDue() {
        return AmountDue;
    }

    public void setAmountDue(String amountDue) {
        AmountDue = amountDue;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public ArrayList<CampDetailsBenMasterModel> getBenMaster() {
        return benMaster;
    }

    public void setBenMaster(ArrayList<CampDetailsBenMasterModel> benMaster) {
        this.benMaster = benMaster;
    }
}
