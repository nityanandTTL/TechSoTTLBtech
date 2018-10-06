package com.thyrocare.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nityanand on 9/21/2017.
 */

public class TSPNBT_AvilModel implements Parcelable{
    private String BtechID;
    private String Date;
    private String Name;

    public TSPNBT_AvilModel(){

    }


    protected TSPNBT_AvilModel(Parcel in) {
        BtechID = in.readString();
        Date = in.readString();
        Name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(BtechID);
        dest.writeString(Date);
        dest.writeString(Name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TSPNBT_AvilModel> CREATOR = new Creator<TSPNBT_AvilModel>() {
        @Override
        public TSPNBT_AvilModel createFromParcel(Parcel in) {
            return new TSPNBT_AvilModel(in);
        }

        @Override
        public TSPNBT_AvilModel[] newArray(int size) {
            return new TSPNBT_AvilModel[size];
        }
    };

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



}
