package com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel;

import android.os.Parcel;
import android.os.Parcelable;

public class TSP_NBT_AvailabilityResponseModel implements Parcelable {

    public static final Creator<TSP_NBT_AvailabilityResponseModel> CREATOR = new Creator<TSP_NBT_AvailabilityResponseModel>() {
        @Override
        public TSP_NBT_AvailabilityResponseModel createFromParcel(Parcel in) {
            return new TSP_NBT_AvailabilityResponseModel(in);
        }

        @Override
        public TSP_NBT_AvailabilityResponseModel[] newArray(int size) {
            return new TSP_NBT_AvailabilityResponseModel[size];
        }
    };
    private String BtechID;
    private String Date;
    private String Name;

    protected TSP_NBT_AvailabilityResponseModel(Parcel in) {
        BtechID = in.readString();
        Date = in.readString();
        Name = in.readString();
    }

    public String getBtechID() {
        return BtechID;
    }

    public void setBtechID(String btechID) {
        BtechID = btechID;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(BtechID);
        parcel.writeString(Date);
        parcel.writeString(Name);
    }
}
