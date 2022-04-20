package com.thyrocare.btechapp.models.api.response;

import android.os.Parcel;
import android.os.Parcelable;

public class GetPECancelRemarksResponseModel implements Parcelable {

    /**
     * Id : 283
     * Reason : False/Duplicate Booking
     */

    private Integer Id;
    private String Reason;

    protected GetPECancelRemarksResponseModel(Parcel in) {
        if (in.readByte() == 0) {
            Id = null;
        } else {
            Id = in.readInt();
        }
        Reason = in.readString();
    }

    public static final Creator<GetPECancelRemarksResponseModel> CREATOR = new Creator<GetPECancelRemarksResponseModel>() {
        @Override
        public GetPECancelRemarksResponseModel createFromParcel(Parcel in) {
            return new GetPECancelRemarksResponseModel(in);
        }

        @Override
        public GetPECancelRemarksResponseModel[] newArray(int size) {
            return new GetPECancelRemarksResponseModel[size];
        }
    };

    public Integer getId() {
        return Id;
    }

    public void setId(Integer Id) {
        this.Id = Id;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String Reason) {
        this.Reason = Reason;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (Id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(Id);
        }
        dest.writeString(Reason);
    }
}
