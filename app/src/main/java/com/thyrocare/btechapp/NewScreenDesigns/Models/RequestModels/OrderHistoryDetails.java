package com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderHistoryDetails implements Parcelable {
    public static final Creator<OrderHistoryDetails> CREATOR = new Creator<OrderHistoryDetails>() {
        @Override
        public OrderHistoryDetails createFromParcel(Parcel in) {
            return new OrderHistoryDetails(in);
        }

        @Override
        public OrderHistoryDetails[] newArray(int size) {
            return new OrderHistoryDetails[size];
        }
    };
    private String OrdHistory;

    public OrderHistoryDetails() {
    }

    protected OrderHistoryDetails(Parcel in) {
        OrdHistory = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(OrdHistory);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getOrdHistory() {
        return OrdHistory;
    }

    public void setOrdHistory(String ordHistory) {
        OrdHistory = ordHistory;
    }
}