package com.dhb.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 5/15/2017.
 */

public class CampDetailsSampleTypeModel implements Parcelable{
    private String sampleType,Tests;

    protected CampDetailsSampleTypeModel(Parcel in) {
        sampleType = in.readString();
        Tests = in.readString();
        benId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sampleType);
        dest.writeString(Tests);
        dest.writeString(benId);
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

    public String getBenId() {
        return benId;
    }

    public void setBenId(String benId) {
        this.benId = benId;
    }

    private String benId;

    public CampDetailsSampleTypeModel() {
    }

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
