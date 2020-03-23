package com.thyrocare.models.api.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by E4904 on 8/10/2017.
 */

public class RemarksRequestToReleaseResponseModel implements Parcelable {

    private int Id;
    private String Remarks;

    protected RemarksRequestToReleaseResponseModel(Parcel in) {
        Id = in.readInt();
        Remarks = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(Remarks);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RemarksRequestToReleaseResponseModel> CREATOR = new Creator<RemarksRequestToReleaseResponseModel>() {
        @Override
        public RemarksRequestToReleaseResponseModel createFromParcel(Parcel in) {
            return new RemarksRequestToReleaseResponseModel(in);
        }

        @Override
        public RemarksRequestToReleaseResponseModel[] newArray(int size) {
            return new RemarksRequestToReleaseResponseModel[size];
        }
    };

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public RemarksRequestToReleaseResponseModel() {
    }


}
