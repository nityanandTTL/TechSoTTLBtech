package com.dhb.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vendor1 on 5/15/2017.
 */

public class CampDetailsSampleTypeModel implements Parcelable{
    private String sampleType,Tests;

    public CampDetailsSampleTypeModel() {
    }

    protected CampDetailsSampleTypeModel(Parcel in) {
        sampleType = in.readString();
        Tests = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sampleType);
        dest.writeString(Tests);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CampDetailsSampleTypeModel> CREATOR = new Creator<CampDetailsSampleTypeModel>() {
        @Override
        public CampDetailsSampleTypeModel createFromParcel(Parcel in) {
            return new CampDetailsSampleTypeModel(in);
        }

        @Override
        public CampDetailsSampleTypeModel[] newArray(int size) {
            return new CampDetailsSampleTypeModel[size];
        }
    };

    public String getSampleType() {
        return sampleType;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    public String getTests() {
        return Tests;
    }

    public void setTests(String tests) {
        Tests = tests;
    }
}
