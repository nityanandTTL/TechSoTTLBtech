package com.thyrocare.models.api.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.thyrocare.models.data.Tsp_ScanBarcodeDataModel;

import java.util.ArrayList;

/**
 * Created by Orion on 7/6/2017.
 */

public class Tsp_ScanBarcodeResponseModel implements Parcelable {

    private String Response;
    private ArrayList<Tsp_ScanBarcodeDataModel> tspBarcodes;


    protected Tsp_ScanBarcodeResponseModel(Parcel in) {
        Response = in.readString();
        tspBarcodes = in.createTypedArrayList(Tsp_ScanBarcodeDataModel.CREATOR);
    }

    public static final Creator<Tsp_ScanBarcodeResponseModel> CREATOR = new Creator<Tsp_ScanBarcodeResponseModel>() {
        @Override
        public Tsp_ScanBarcodeResponseModel createFromParcel(Parcel in) {
            return new Tsp_ScanBarcodeResponseModel(in);
        }

        @Override
        public Tsp_ScanBarcodeResponseModel[] newArray(int size) {
            return new Tsp_ScanBarcodeResponseModel[size];
        }
    };

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public ArrayList<Tsp_ScanBarcodeDataModel> getTspBarcodes() {
        return tspBarcodes;
    }

    public void setTspBarcodes(ArrayList<Tsp_ScanBarcodeDataModel> tspBarcodes) {
        this.tspBarcodes = tspBarcodes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Response);
        dest.writeTypedList(tspBarcodes);
    }
}
