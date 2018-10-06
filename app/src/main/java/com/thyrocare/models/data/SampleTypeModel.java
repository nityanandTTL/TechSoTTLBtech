package com.thyrocare.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 5/23/2017.
 */

class SampleTypeModel implements Parcelable {
    private String sampleType,Tests;

    protected SampleTypeModel(Parcel in) {
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

    public static final Creator<SampleTypeModel> CREATOR = new Creator<SampleTypeModel>() {
        @Override
        public SampleTypeModel createFromParcel(Parcel in) {
            return new SampleTypeModel(in);
        }

        @Override
        public SampleTypeModel[] newArray(int size) {
            return new SampleTypeModel[size];
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
