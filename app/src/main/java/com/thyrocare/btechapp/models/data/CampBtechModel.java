package com.thyrocare.btechapp.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 5/10/2017.
 */

public class CampBtechModel implements Parcelable{
    private int BtechId;
    private String Mobile;
    private String Status;

    public CampBtechModel() {
    }

    protected CampBtechModel(Parcel in) {
        BtechId = in.readInt();
        Mobile = in.readString();
        Status = in.readString();
        Name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(BtechId);
        dest.writeString(Mobile);
        dest.writeString(Status);
        dest.writeString(Name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CampBtechModel> CREATOR = new Creator<CampBtechModel>() {
        @Override
        public CampBtechModel createFromParcel(Parcel in) {
            return new CampBtechModel(in);
        }

        @Override
        public CampBtechModel[] newArray(int size) {
            return new CampBtechModel[size];
        }
    };

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    private String Name;

    public int getBtechId() {
        return BtechId;
    }

    public void setBtechId(int btechId) {
        BtechId = btechId;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
