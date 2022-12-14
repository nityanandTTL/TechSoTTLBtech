package com.thyrocare.btechapp.models.api.request;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by E4904 on 9/1/2017.
 */

public class OrderPassRequestModel implements Parcelable {

    private int BtechId;
    private String VisitId;
    private String Mobile;
    private String OTP;
    private String OTPStatus;

    protected OrderPassRequestModel(Parcel in) {
        BtechId = in.readInt();
        VisitId = in.readString();
        Mobile = in.readString();
        OTP = in.readString();
        OTPStatus = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(BtechId);
        dest.writeString(VisitId);
        dest.writeString(Mobile);
        dest.writeString(OTP);
        dest.writeString(OTPStatus);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderPassRequestModel> CREATOR = new Creator<OrderPassRequestModel>() {
        @Override
        public OrderPassRequestModel createFromParcel(Parcel in) {
            return new OrderPassRequestModel(in);
        }

        @Override
        public OrderPassRequestModel[] newArray(int size) {
            return new OrderPassRequestModel[size];
        }
    };

    public int getBtechId() {
        return BtechId;
    }

    public void setBtechId(int btechId) {
        BtechId = btechId;
    }

    public String getVisitId() {
        return VisitId;
    }

    public void setVisitId(String visitId) {
        VisitId = visitId;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }

    public String getOTPStatus() {
        return OTPStatus;
    }

    public void setOTPStatus(String OTPStatus) {
        this.OTPStatus = OTPStatus;
    }

    public OrderPassRequestModel() {
    }


}
