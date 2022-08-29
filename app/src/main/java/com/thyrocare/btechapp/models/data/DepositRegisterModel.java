package com.thyrocare.btechapp.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 5/2/2017.
 */

public class DepositRegisterModel implements Parcelable {

    public static final Creator<DepositRegisterModel> CREATOR = new Creator<DepositRegisterModel>() {
        @Override
        public DepositRegisterModel createFromParcel(Parcel in) {
            return new DepositRegisterModel(in);
        }

        @Override
        public DepositRegisterModel[] newArray(int size) {
            return new DepositRegisterModel[size];
        }
    };
    String Date;
    Integer Amount;
    String Remarks;

    protected DepositRegisterModel(Parcel in) {
        Date = in.readString();
        Remarks = in.readString();
    }

    @Override
    public String toString() {
        return "DepositRegisterModel{" +
                "Date='" + Date + '\'' +
                ", Amount=" + Amount +
                ", Remarks='" + Remarks + '\'' +
                '}';
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public Integer getAmount() {
        return Amount;
    }

    public void setAmount(Integer amount) {
        Amount = amount;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Date);
        dest.writeString(Remarks);
    }
}
