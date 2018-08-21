package com.thyrocare.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by E4904 on 9/1/2017.
 */

public class Orderallocation implements Parcelable {


    private int BtechId;
    private String BtechName;
    private String Pincode;
    private String Mobile;


    protected Orderallocation(Parcel in) {
        BtechId = in.readInt();
        BtechName = in.readString();
        Pincode = in.readString();
        Mobile = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(BtechId);
        dest.writeString(BtechName);
        dest.writeString(Pincode);
        dest.writeString(Mobile);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Orderallocation> CREATOR = new Creator<Orderallocation>() {
        @Override
        public Orderallocation createFromParcel(Parcel in) {
            return new Orderallocation(in);
        }

        @Override
        public Orderallocation[] newArray(int size) {
            return new Orderallocation[size];
        }
    };

    public int getBtechId() {
        return BtechId;
    }

    public void setBtechId(int btechId) {
        BtechId = btechId;
    }

    public String getBtechName() {
        return BtechName;
    }

    public void setBtechName(String btechName) {
        BtechName = btechName;
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

    public Orderallocation() {
    }
}
