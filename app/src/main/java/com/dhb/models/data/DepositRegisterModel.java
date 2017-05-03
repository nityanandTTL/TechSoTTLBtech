package com.dhb.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by E4904 on 5/2/2017.
 */

public class DepositRegisterModel extends BaseModel implements Parcelable {

    String Date;
    Integer Amount;
    String Remarks;

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

    public DepositRegisterModel() {
        super();
    }

    protected DepositRegisterModel(Parcel in) {
        super(in);
        Date = in.readString();
        Amount=in.readInt();
        Remarks = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(Date);
        dest.writeInt(Amount);
        dest.writeString(Remarks);
    }

    @Override
    public int describeContents() {
        return 0;
    }

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





}
