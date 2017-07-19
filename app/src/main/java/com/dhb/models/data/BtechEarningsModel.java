package com.dhb.models.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Orion on 6/21/2017.
 */

public class BtechEarningsModel implements Parcelable{
    ArrayList<VisitEarningsModel> visitEarnings;

    public BtechEarningsModel() {
    }

    protected BtechEarningsModel(Parcel in) {
        visitEarnings = in.createTypedArrayList(VisitEarningsModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(visitEarnings);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BtechEarningsModel> CREATOR = new Creator<BtechEarningsModel>() {
        @Override
        public BtechEarningsModel createFromParcel(Parcel in) {
            return new BtechEarningsModel(in);
        }

        @Override
        public BtechEarningsModel[] newArray(int size) {
            return new BtechEarningsModel[size];
        }
    };

    public ArrayList<VisitEarningsModel> getVisitEarnings() {
        return visitEarnings;
    }

    public void setVisitEarnings(ArrayList<VisitEarningsModel> visitEarnings) {
        this.visitEarnings = visitEarnings;
    }

}
