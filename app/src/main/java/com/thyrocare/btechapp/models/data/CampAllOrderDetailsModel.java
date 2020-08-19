package com.thyrocare.btechapp.models.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Orion on 5/15/2017.
 */

public class CampAllOrderDetailsModel implements Parcelable{
    private int BrandId, Margin, Discount,  ReportHC, AmountDue,Distance;
    private String OrderNo, Address, Pincode, Mobile, Email, PayType,  Status,Refcode ;
    private double Longitude,Latitude;
    private ArrayList<CampDetailsBenMasterModel> benMaster;

    protected CampAllOrderDetailsModel(Parcel in) {
        BrandId = in.readInt();
        Margin = in.readInt();
        Discount = in.readInt();
        ReportHC = in.readInt();
        AmountDue = in.readInt();
        Distance = in.readInt();
        OrderNo = in.readString();
        Address = in.readString();
        Pincode = in.readString();
        Mobile = in.readString();
        Email = in.readString();
        PayType = in.readString();
        Status = in.readString();
        Refcode = in.readString();
        Longitude = in.readDouble();
        Latitude = in.readDouble();
        benMaster = in.createTypedArrayList(CampDetailsBenMasterModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(BrandId);
        dest.writeInt(Margin);
        dest.writeInt(Discount);
        dest.writeInt(ReportHC);
        dest.writeInt(AmountDue);
        dest.writeInt(Distance);
        dest.writeString(OrderNo);
        dest.writeString(Address);
        dest.writeString(Pincode);
        dest.writeString(Mobile);
        dest.writeString(Email);
        dest.writeString(PayType);
        dest.writeString(Status);
        dest.writeString(Refcode);
        dest.writeDouble(Longitude);
        dest.writeDouble(Latitude);
        dest.writeTypedList(benMaster);
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

    public void setRefcode(String refcode) {
        Refcode = refcode;
    }

    public CampAllOrderDetailsModel() {
        // Required empty public constructor
    }

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

    public String getRefcode() {
        return Refcode;
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

    public int getAmountDue() {
        return AmountDue;
    }

    public void setAmountDue(int amountDue) {
        AmountDue = amountDue;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }/*

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }*/

    public ArrayList<CampDetailsBenMasterModel> getBenMaster() {
        return benMaster;
    }

    public void setBenMaster(ArrayList<CampDetailsBenMasterModel> benMaster) {
        this.benMaster = benMaster;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }
}
