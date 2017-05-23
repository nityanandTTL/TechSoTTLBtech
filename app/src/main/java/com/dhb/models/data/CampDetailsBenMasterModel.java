package com.dhb.models.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by vendor1 on 5/15/2017.
 */

public class CampDetailsBenMasterModel implements Parcelable{
    private int benId,Age,ProjId;
    private String Name,Gender,testsCode,Fasting;
    private ArrayList<CampDetailsSampleTypeModel> sampleType;
    private ArrayList<CampDetailsKitsModel> kits;
    private String OrderNo;

    protected CampDetailsBenMasterModel(Parcel in) {
        benId = in.readInt();
        Age = in.readInt();
        ProjId = in.readInt();
        Name = in.readString();
        Gender = in.readString();
        testsCode = in.readString();
        Fasting = in.readString();
        OrderNo = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(benId);
        dest.writeInt(Age);
        dest.writeInt(ProjId);
        dest.writeString(Name);
        dest.writeString(Gender);
        dest.writeString(testsCode);
        dest.writeString(Fasting);
        dest.writeString(OrderNo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CampDetailsBenMasterModel> CREATOR = new Creator<CampDetailsBenMasterModel>() {
        @Override
        public CampDetailsBenMasterModel createFromParcel(Parcel in) {
            return new CampDetailsBenMasterModel(in);
        }

        @Override
        public CampDetailsBenMasterModel[] newArray(int size) {
            return new CampDetailsBenMasterModel[size];
        }
    };

    public int getBenId() {
        return benId;
    }

    public void setBenId(int benId) {
        this.benId = benId;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public int getProjId() {
        return ProjId;
    }

    public void setProjId(int projId) {
        ProjId = projId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getTestsCode() {
        return testsCode;
    }

    public void setTestsCode(String testsCode) {
        this.testsCode = testsCode;
    }

    public String getFasting() {
        return Fasting;
    }

    public void setFasting(String fasting) {
        Fasting = fasting;
    }

    public ArrayList<CampDetailsSampleTypeModel> getSampleType() {
        return sampleType;
    }

    public void setSampleType(ArrayList<CampDetailsSampleTypeModel> sampleType) {
        this.sampleType = sampleType;
    }

    public ArrayList<CampDetailsKitsModel> getKits() {
        return kits;
    }

    public void setKits(ArrayList<CampDetailsKitsModel> kits) {
        this.kits = kits;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }
}
