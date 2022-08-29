package com.thyrocare.btechapp.models.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by e5233@thyrocare.com on 26/6/18.
 */

public class DispositionDataModel extends BaseModel implements Parcelable {

    public static final Creator<DispositionDataModel> CREATOR = new Creator<DispositionDataModel>() {
        @Override
        public DispositionDataModel createFromParcel(Parcel in) {
            return new DispositionDataModel(in);
        }

        @Override
        public DispositionDataModel[] newArray(int size) {
            return new DispositionDataModel[size];
        }
    };
    private ArrayList<DispositionDetailsModel> allDisp;
    private String Response;

    protected DispositionDataModel(Parcel in) {
        allDisp = in.createTypedArrayList(DispositionDetailsModel.CREATOR);
        Response = in.readString();
    }

    public DispositionDataModel() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(allDisp);
        dest.writeString(Response);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public ArrayList<DispositionDetailsModel> getAllDisp() {
        return allDisp;
    }

    public void setAllDisp(ArrayList<DispositionDetailsModel> allDisp) {
        this.allDisp = allDisp;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }
}
