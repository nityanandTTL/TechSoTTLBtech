package com.thyrocare.btechapp.models.api.request;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by e5209 on 9/18/2017.
 */

public class TrackBtechLocationRequestModel implements Parcelable {
    public static final Creator<TrackBtechLocationRequestModel> CREATOR = new Creator<TrackBtechLocationRequestModel>() {
        @Override
        public TrackBtechLocationRequestModel createFromParcel(Parcel in) {
            return new TrackBtechLocationRequestModel(in);
        }

        @Override
        public TrackBtechLocationRequestModel[] newArray(int size) {
            return new TrackBtechLocationRequestModel[size];
        }
    };
    private int BtechId;
    private Double Latitude, Longitude;

    public TrackBtechLocationRequestModel() {
    }

    protected TrackBtechLocationRequestModel(Parcel in) {
        BtechId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(BtechId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getBtechId() {
        return BtechId;
    }

    public void setBtechId(int btechId) {
        BtechId = btechId;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }
}
