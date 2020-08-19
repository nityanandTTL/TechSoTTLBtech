package com.thyrocare.btechapp.models.api.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.thyrocare.btechapp.models.data.CampDetailModel;

import java.util.ArrayList;

/**
 * Created by Orion on 5/11/2017.<br/>
 * /CampDetails/MyCampDetails<br/>
 */

public class CampListDisplayResponseModel implements Parcelable{
    String Response;
    ArrayList<CampDetailModel> campDetail;
    public CampListDisplayResponseModel() {
        // Required empty public constructor
    }

    protected CampListDisplayResponseModel(Parcel in) {
        Response = in.readString();
        campDetail = in.createTypedArrayList(CampDetailModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Response);
        dest.writeTypedList(campDetail);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CampListDisplayResponseModel> CREATOR = new Creator<CampListDisplayResponseModel>() {
        @Override
        public CampListDisplayResponseModel createFromParcel(Parcel in) {
            return new CampListDisplayResponseModel(in);
        }

        @Override
        public CampListDisplayResponseModel[] newArray(int size) {
            return new CampListDisplayResponseModel[size];
        }
    };

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public ArrayList<CampDetailModel> getCampDetail() {
        return campDetail;
    }

    public void setCampDetail(ArrayList<CampDetailModel> campDetail) {
        this.campDetail = campDetail;
    }
}
