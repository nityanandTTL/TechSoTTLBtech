package com.thyrocare.models.api.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.thyrocare.models.data.BtechwithHub_BarcodeDataModel;

import java.util.ArrayList;

/**
 * Created by Orion on 6/30/2017.
 */

public class BtechwithHubResponseModel implements Parcelable {

    private String Response;
    private ArrayList<BtechwithHub_BarcodeDataModel> receivedHub;

    protected BtechwithHubResponseModel(Parcel in) {
        Response = in.readString();
        receivedHub = in.createTypedArrayList(BtechwithHub_BarcodeDataModel.CREATOR);
    }

    public static final Creator<BtechwithHubResponseModel> CREATOR = new Creator<BtechwithHubResponseModel>() {
        @Override
        public BtechwithHubResponseModel createFromParcel(Parcel in) {
            return new BtechwithHubResponseModel(in);
        }

        @Override
        public BtechwithHubResponseModel[] newArray(int size) {
            return new BtechwithHubResponseModel[size];
        }
    };

    @Override
    public String toString() {
        return "BtechwithHubResponseModel{" +
                "Response='" + Response + '\'' +
                ", receivedHub=" + receivedHub +
                '}';
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public ArrayList<BtechwithHub_BarcodeDataModel> getReceivedHub() {
        return receivedHub;
    }

    public void setReceivedHub(ArrayList<BtechwithHub_BarcodeDataModel> receivedHub) {
        this.receivedHub = receivedHub;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Response);
        dest.writeTypedList(receivedHub);
    }
}
