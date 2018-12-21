package com.thyrocare.models.data;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Orion on 4/20/2017.
 */

public class SampleDropDetailsbyTSPLMEDetailsModel implements Parcelable {

    private int LMEUserId;
    private String TSPOLCId;
    private String SourceCode;
    private String Name;
    private String Address;
    private String Pincode;
    private ArrayList<SampleDropBarcodeList> BarcodeList;


    protected SampleDropDetailsbyTSPLMEDetailsModel(Parcel in) {
        LMEUserId = in.readInt();
        TSPOLCId = in.readString();
        SourceCode = in.readString();
        Name = in.readString();
        Address = in.readString();
        Pincode = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(LMEUserId);
        dest.writeString(TSPOLCId);
        dest.writeString(SourceCode);
        dest.writeString(Name);
        dest.writeString(Address);
        dest.writeString(Pincode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SampleDropDetailsbyTSPLMEDetailsModel> CREATOR = new Creator<SampleDropDetailsbyTSPLMEDetailsModel>() {
        @Override
        public SampleDropDetailsbyTSPLMEDetailsModel createFromParcel(Parcel in) {
            return new SampleDropDetailsbyTSPLMEDetailsModel(in);
        }

        @Override
        public SampleDropDetailsbyTSPLMEDetailsModel[] newArray(int size) {
            return new SampleDropDetailsbyTSPLMEDetailsModel[size];
        }
    };

    public int getLMEUserId() {
        return LMEUserId;
    }

    public void setLMEUserId(int LMEUserId) {
        this.LMEUserId = LMEUserId;
    }

    public String getTSPOLCId() {
        return TSPOLCId;
    }

    public void setTSPOLCId(String TSPOLCId) {
        this.TSPOLCId = TSPOLCId;
    }

    public String getSourceCode() {
        return SourceCode;
    }

    public void setSourceCode(String sourceCode) {
        SourceCode = sourceCode;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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

    public ArrayList<SampleDropBarcodeList> getBarcodeList() {
        return BarcodeList;
    }

    public void setBarcodeList(ArrayList<SampleDropBarcodeList> barcodeList) {
        BarcodeList = barcodeList;
    }
}
