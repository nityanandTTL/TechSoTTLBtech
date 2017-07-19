package com.dhb.models.api.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.dhb.models.data.CartAPIResponseOrderModel;

import java.util.ArrayList;

/**
 * Created by Orion on 5/25/2017.
 */

public class CartAPIResponseModel implements Parcelable {
    private ArrayList<CartAPIResponseOrderModel> orders;
    private String Response;

    public CartAPIResponseModel() {
    }

    protected CartAPIResponseModel(Parcel in) {
        orders = in.createTypedArrayList(CartAPIResponseOrderModel.CREATOR);
        Response = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(orders);
        dest.writeString(Response);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CartAPIResponseModel> CREATOR = new Creator<CartAPIResponseModel>() {
        @Override
        public CartAPIResponseModel createFromParcel(Parcel in) {
            return new CartAPIResponseModel(in);
        }

        @Override
        public CartAPIResponseModel[] newArray(int size) {
            return new CartAPIResponseModel[size];
        }
    };

    public ArrayList<CartAPIResponseOrderModel> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<CartAPIResponseOrderModel> orders) {
        this.orders = orders;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }
}
