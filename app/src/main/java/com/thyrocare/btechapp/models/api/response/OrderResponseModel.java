package com.thyrocare.btechapp.models.api.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class OrderResponseModel implements Parcelable {
    ArrayList<PostOrderDataResponseModel> PostOrderDataResponse;

    protected OrderResponseModel(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderResponseModel> CREATOR = new Creator<OrderResponseModel>() {
        @Override
        public OrderResponseModel createFromParcel(Parcel in) {
            return new OrderResponseModel(in);
        }

        @Override
        public OrderResponseModel[] newArray(int size) {
            return new OrderResponseModel[size];
        }
    };

    public ArrayList<PostOrderDataResponseModel> getPostOrderDataResponse() {
        return PostOrderDataResponse;
    }

    public void setPostOrderDataResponse(ArrayList<PostOrderDataResponseModel> postOrderDataResponse) {
        PostOrderDataResponse = postOrderDataResponse;
    }
}
