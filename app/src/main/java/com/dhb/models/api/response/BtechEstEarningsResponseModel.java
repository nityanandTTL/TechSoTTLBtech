package com.dhb.models.api.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.dhb.models.data.BtechEarningsModel;

import java.util.ArrayList;

/**
 * Created by Orion on 6/21/2017.
 */

public class BtechEstEarningsResponseModel implements Parcelable{
    ArrayList<BtechEarningsModel> btechEarnings;
    private int Distance;
    private String Response;


    public BtechEstEarningsResponseModel() {
    }


    protected BtechEstEarningsResponseModel(Parcel in) {
        btechEarnings = in.createTypedArrayList(BtechEarningsModel.CREATOR);
        Distance = in.readInt();
        Response = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(btechEarnings);
        dest.writeInt(Distance);
        dest.writeString(Response);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BtechEstEarningsResponseModel> CREATOR = new Creator<BtechEstEarningsResponseModel>() {
        @Override
        public BtechEstEarningsResponseModel createFromParcel(Parcel in) {
            return new BtechEstEarningsResponseModel(in);
        }

        @Override
        public BtechEstEarningsResponseModel[] newArray(int size) {
            return new BtechEstEarningsResponseModel[size];
        }
    };

    public ArrayList<BtechEarningsModel> getBtechEarnings() {
        return btechEarnings;
    }

    public void setBtechEarnings(ArrayList<BtechEarningsModel> btechEarnings) {
        this.btechEarnings = btechEarnings;
    }
    public int getDistance() {
        return Distance;
    }

    public void setDistance(int distance) {
        Distance = distance;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }
}
