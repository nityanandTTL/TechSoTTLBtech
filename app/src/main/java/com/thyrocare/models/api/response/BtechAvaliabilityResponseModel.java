package com.thyrocare.models.api.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 6/22/2017.
 */

public class BtechAvaliabilityResponseModel implements Parcelable {
    private int NumberofDays;

    public BtechAvaliabilityResponseModel() {
    }

    protected BtechAvaliabilityResponseModel(Parcel in) {
        NumberofDays = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(NumberofDays);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BtechAvaliabilityResponseModel> CREATOR = new Creator<BtechAvaliabilityResponseModel>() {
        @Override
        public BtechAvaliabilityResponseModel createFromParcel(Parcel in) {
            return new BtechAvaliabilityResponseModel(in);
        }

        @Override
        public BtechAvaliabilityResponseModel[] newArray(int size) {
            return new BtechAvaliabilityResponseModel[size];
        }
    };

    public int getNumberofDays() {
        return NumberofDays;
    }

    public void setNumberofDays(int numberofDays) {
        NumberofDays = numberofDays;
    }
}
