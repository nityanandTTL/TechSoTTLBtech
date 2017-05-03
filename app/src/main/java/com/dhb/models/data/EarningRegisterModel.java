package com.dhb.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by E4904 on 4/28/2017.
 */

public class EarningRegisterModel extends BaseModel implements Parcelable {
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

    public EarningRegisterModel() {
        super();
    }

    protected EarningRegisterModel(Parcel in) {
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

    public static final Creator<EarningRegisterModel> CREATOR = new Creator<EarningRegisterModel>() {
        @Override
        public EarningRegisterModel createFromParcel(Parcel in) {
            return new EarningRegisterModel(in);
        }

        @Override
        public EarningRegisterModel[] newArray(int size) {
            return new EarningRegisterModel[size];
        }
    };





}
