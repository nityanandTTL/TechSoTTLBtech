package com.thyrocare.models.api.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.thyrocare.models.data.Tsp_SendMode_DataModel;

import java.util.ArrayList;

/**
 * Created by Orion on 7/4/2017.
 */

public class Tsp_SendConsignment_Modes_ResponseModel implements Parcelable {
    private String Response;
    private ArrayList<Tsp_SendMode_DataModel> courierModes;

    protected Tsp_SendConsignment_Modes_ResponseModel(Parcel in) {
        Response = in.readString();
        courierModes = in.createTypedArrayList(Tsp_SendMode_DataModel.CREATOR);
    }

    public static final Creator<Tsp_SendConsignment_Modes_ResponseModel> CREATOR = new Creator<Tsp_SendConsignment_Modes_ResponseModel>() {
        @Override
        public Tsp_SendConsignment_Modes_ResponseModel createFromParcel(Parcel in) {
            return new Tsp_SendConsignment_Modes_ResponseModel(in);
        }

        @Override
        public Tsp_SendConsignment_Modes_ResponseModel[] newArray(int size) {
            return new Tsp_SendConsignment_Modes_ResponseModel[size];
        }
    };

    @Override
    public String toString() {
        return "Tsp_SendConsignment_Modes_ResponseModel{" +
                "Response='" + Response + '\'' +
                ", courierModes=" + courierModes +
                '}';
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public ArrayList<Tsp_SendMode_DataModel> getCourierModes() {
        return courierModes;
    }

    public void setCourierModes(ArrayList<Tsp_SendMode_DataModel> courierModes) {
        this.courierModes = courierModes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Response);
        dest.writeTypedList(courierModes);
    }
}
