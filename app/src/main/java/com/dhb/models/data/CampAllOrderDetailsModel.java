package com.dhb.models.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by vendor1 on 5/15/2017.
 */

public class CampAllOrderDetailsModel implements Parcelable{
    private int BrandId, Margin, Discount, Refcode, ReportHC, Distance;
    private String OrderNo, Address, Pincode, Mobile, Email, PayType, AmountDue, Status, Longitude, Latitude;
    private ArrayList<CampDetailsBenMasterModel> benMaster;

    protected CampAllOrderDetailsModel(Parcel in) {
        BrandId = in.readInt();
        Margin = in.readInt();
        Discount = in.readInt();
        Refcode = in.readInt();
        ReportHC = in.readInt();
        Distance = in.readInt();
        OrderNo = in.readString();
        Address = in.readString();
        Pincode = in.readString();
        Mobile = in.readString();
        Email = in.readString();
        PayType = in.readString();
        AmountDue = in.readString();
        Status = in.readString();
        Longitude = in.readString();
        Latitude = in.readString();
    }
    public CampAllOrderDetailsModel() {
        // Required empty public constructor
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(BrandId);
        dest.writeInt(Margin);
        dest.writeInt(Discount);
        dest.writeInt(Refcode);
        dest.writeInt(ReportHC);
        dest.writeInt(Distance);
        dest.writeString(OrderNo);
        dest.writeString(Address);
        dest.writeString(Pincode);
        dest.writeString(Mobile);
        dest.writeString(Email);
        dest.writeString(PayType);
        dest.writeString(AmountDue);
        dest.writeString(Status);
        dest.writeString(Longitude);
        dest.writeString(Latitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CampAllOrderDetailsModel> CREATOR = new Creator<CampAllOrderDetailsModel>() {
        @Override
        public CampAllOrderDetailsModel createFromParcel(Parcel in) {
            return new CampAllOrderDetailsModel(in);
        }

        @Override
        public CampAllOrderDetailsModel[] newArray(int size) {
            return new CampAllOrderDetailsModel[size];
        }
    };

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
