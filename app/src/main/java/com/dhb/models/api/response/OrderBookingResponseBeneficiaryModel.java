package com.dhb.models.api.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 5/24/2017.
 */

public class OrderBookingResponseBeneficiaryModel implements Parcelable {
    private String oldBenIds;
    private String newBenIds;

    public OrderBookingResponseBeneficiaryModel() {
    }

    protected OrderBookingResponseBeneficiaryModel(Parcel in) {
        oldBenIds = in.readString();
        newBenIds = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(oldBenIds);
        dest.writeString(newBenIds);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderBookingResponseBeneficiaryModel> CREATOR = new Creator<OrderBookingResponseBeneficiaryModel>() {
        @Override
        public OrderBookingResponseBeneficiaryModel createFromParcel(Parcel in) {
            return new OrderBookingResponseBeneficiaryModel(in);
        }

        @Override
        public OrderBookingResponseBeneficiaryModel[] newArray(int size) {
            return new OrderBookingResponseBeneficiaryModel[size];
        }
    };

    public String getOldBenIds() {
        return oldBenIds;
    }

    public void setOldBenIds(String oldBenIds) {
        this.oldBenIds = oldBenIds;
    }

    public String getNewBenIds() {
        return newBenIds;
    }

    public void setNewBenIds(String newBenIds) {
        this.newBenIds = newBenIds;
    }
}
