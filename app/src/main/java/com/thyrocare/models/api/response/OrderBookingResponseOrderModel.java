package com.thyrocare.models.api.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Orion on 5/24/2017.
 */

public class OrderBookingResponseOrderModel implements Parcelable {
    private String oldOrderId;
    private String newOrderId;
    private ArrayList<OrderBookingResponseBeneficiaryModel> benfids;

    public OrderBookingResponseOrderModel() {
    }

    protected OrderBookingResponseOrderModel(Parcel in) {
        oldOrderId = in.readString();
        newOrderId = in.readString();
        benfids = in.createTypedArrayList(OrderBookingResponseBeneficiaryModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(oldOrderId);
        dest.writeString(newOrderId);
        dest.writeTypedList(benfids);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderBookingResponseOrderModel> CREATOR = new Creator<OrderBookingResponseOrderModel>() {
        @Override
        public OrderBookingResponseOrderModel createFromParcel(Parcel in) {
            return new OrderBookingResponseOrderModel(in);
        }

        @Override
        public OrderBookingResponseOrderModel[] newArray(int size) {
            return new OrderBookingResponseOrderModel[size];
        }
    };

    public String getOldOrderId() {
        return oldOrderId;
    }

    public void setOldOrderId(String oldOrderId) {
        this.oldOrderId = oldOrderId;
    }

    public String getNewOrderId() {
        return newOrderId;
    }

    public void setNewOrderId(String newOrderId) {
        this.newOrderId = newOrderId;
    }

    public ArrayList<OrderBookingResponseBeneficiaryModel> getBenfids() {
        return benfids;
    }

    public void setBenfids(ArrayList<OrderBookingResponseBeneficiaryModel> benfids) {
        this.benfids = benfids;
    }
}
