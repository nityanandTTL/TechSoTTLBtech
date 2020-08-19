package com.thyrocare.btechapp.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by e5209@thyrocare.com on 4/10/18.
 */

public class DateWiseWLMISDetailsModel implements Parcelable{
    private String CompletedAt,WL,PickUp;

    protected DateWiseWLMISDetailsModel(Parcel in) {
        CompletedAt = in.readString();
        WL = in.readString();
        PickUp = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(CompletedAt);
        dest.writeString(WL);
        dest.writeString(PickUp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DateWiseWLMISDetailsModel> CREATOR = new Creator<DateWiseWLMISDetailsModel>() {
        @Override
        public DateWiseWLMISDetailsModel createFromParcel(Parcel in) {
            return new DateWiseWLMISDetailsModel(in);
        }

        @Override
        public DateWiseWLMISDetailsModel[] newArray(int size) {
            return new DateWiseWLMISDetailsModel[size];
        }
    };

    public String getCompletedAt() {
        return CompletedAt;
    }

    public void setCompletedAt(String completedAt) {
        CompletedAt = completedAt;
    }

    public String getWL() {
        return WL;
    }

    public void setWL(String WL) {
        this.WL = WL;
    }

    public String getPickUp() {
        return PickUp;
    }

    public void setPickUp(String pickUp) {
        PickUp = pickUp;
    }
}
