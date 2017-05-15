package com.dhb.models.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by ISRO on 5/15/2017.
 */
public class OrderBookingDetailsModel implements Parcelable{
    private int BtechId;
    private String VisitId;
    private ArrayList<OrderDetailsModel> orddtl;

    public OrderBookingDetailsModel() {
    }

    public int getBtechId() {
        return BtechId;
    }

    public void setBtechId(int btechId) {
        BtechId = btechId;
    }

    public String getVisitId() {
        return VisitId;
    }

    public void setVisitId(String visitId) {
        VisitId = visitId;
    }

    public ArrayList<OrderDetailsModel> getOrddtl() {
        return orddtl;
    }

    public void setOrddtl(ArrayList<OrderDetailsModel> orddtl) {
        this.orddtl = orddtl;
    }

    protected OrderBookingDetailsModel(Parcel in) {
        BtechId = in.readInt();
        VisitId = in.readString();
        orddtl = in.createTypedArrayList(OrderDetailsModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(BtechId);
        dest.writeString(VisitId);
        dest.writeTypedList(orddtl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderBookingDetailsModel> CREATOR = new Creator<OrderBookingDetailsModel>() {
        @Override
        public OrderBookingDetailsModel createFromParcel(Parcel in) {
            return new OrderBookingDetailsModel(in);
        }

        @Override
        public OrderBookingDetailsModel[] newArray(int size) {
            return new OrderBookingDetailsModel[size];
        }
    };
}