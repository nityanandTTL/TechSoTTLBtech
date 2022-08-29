package com.thyrocare.btechapp.models.api.request;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by e5209 on 10/11/2017.
 */

public class ServiceUpdateRequestModel implements Parcelable {
    public static final Creator<ServiceUpdateRequestModel> CREATOR = new Creator<ServiceUpdateRequestModel>() {
        @Override
        public ServiceUpdateRequestModel createFromParcel(Parcel in) {
            return new ServiceUpdateRequestModel(in);
        }

        @Override
        public ServiceUpdateRequestModel[] newArray(int size) {
            return new ServiceUpdateRequestModel[size];
        }
    };
    private String VisitId;

    public ServiceUpdateRequestModel() {
    }

    protected ServiceUpdateRequestModel(Parcel in) {
        VisitId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(VisitId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getVisitId() {
        return VisitId;
    }

    public void setVisitId(String visitId) {
        VisitId = visitId;
    }
}
