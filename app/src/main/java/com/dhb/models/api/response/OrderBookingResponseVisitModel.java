package com.dhb.models.api.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Vendor3 on 5/24/2017.
 */

public class OrderBookingResponseVisitModel implements Parcelable {
    private String oldVisitId;
    private String newVisitId;
    private ArrayList<OrderBookingResponseOrderModel> orderids;

    public OrderBookingResponseVisitModel() {
    }

    protected OrderBookingResponseVisitModel(Parcel in) {
        oldVisitId = in.readString();
        newVisitId = in.readString();
        orderids = in.createTypedArrayList(OrderBookingResponseOrderModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(oldVisitId);
        dest.writeString(newVisitId);
        dest.writeTypedList(orderids);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderBookingResponseVisitModel> CREATOR = new Creator<OrderBookingResponseVisitModel>() {
        @Override
        public OrderBookingResponseVisitModel createFromParcel(Parcel in) {
            return new OrderBookingResponseVisitModel(in);
        }

        @Override
        public OrderBookingResponseVisitModel[] newArray(int size) {
            return new OrderBookingResponseVisitModel[size];
        }
    };

    public String getOldVisitId() {
        return oldVisitId;
    }

    public void setOldVisitId(String oldVisitId) {
        this.oldVisitId = oldVisitId;
    }

    public String getNewVisitId() {
        return newVisitId;
    }

    public void setNewVisitId(String newVisitId) {
        this.newVisitId = newVisitId;
    }

    public ArrayList<OrderBookingResponseOrderModel> getOrderids() {
        return orderids;
    }

    public void setOrderids(ArrayList<OrderBookingResponseOrderModel> orderids) {
        this.orderids = orderids;
    }
}