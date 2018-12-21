package com.thyrocare.models.api.request;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by E4904 on 9/2/2017.
 */

public class OrderAllocationTrackLocationRequestModel implements Parcelable {


    private String BtechId;
    private String VisitId;
    private String Latitude;
    private String Longitude;
    private int Status;

    protected OrderAllocationTrackLocationRequestModel(Parcel in) {
        BtechId = in.readString();
        VisitId = in.readString();
        Latitude = in.readString();
        Longitude = in.readString();
        Status = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(BtechId);
        dest.writeString(VisitId);
        dest.writeString(Latitude);
        dest.writeString(Longitude);
        dest.writeInt(Status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderAllocationTrackLocationRequestModel> CREATOR = new Creator<OrderAllocationTrackLocationRequestModel>() {
        @Override
        public OrderAllocationTrackLocationRequestModel createFromParcel(Parcel in) {
            return new OrderAllocationTrackLocationRequestModel(in);
        }

        @Override
        public OrderAllocationTrackLocationRequestModel[] newArray(int size) {
            return new OrderAllocationTrackLocationRequestModel[size];
        }
    };

    public String getBtechId() {
        return BtechId;
    }

    public void setBtechId(String btechId) {
        BtechId = btechId;
    }

    public String getVisitId() {
        return VisitId;
    }

    public void setVisitId(String visitId) {
        VisitId = visitId;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public OrderAllocationTrackLocationRequestModel() {
    }
}
