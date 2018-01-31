package com.thyrocare.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by e5209 on 9/29/2017.
 */

public class TestDetailsModel implements Parcelable{
    private String testCode,description,unit;

    protected TestDetailsModel(Parcel in) {
        testCode = in.readString();
        description = in.readString();
        unit = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(testCode);
        dest.writeString(description);
        dest.writeString(unit);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TestDetailsModel> CREATOR = new Creator<TestDetailsModel>() {
        @Override
        public TestDetailsModel createFromParcel(Parcel in) {
            return new TestDetailsModel(in);
        }

        @Override
        public TestDetailsModel[] newArray(int size) {
            return new TestDetailsModel[size];
        }
    };

    public String getTestCode() {
        return testCode;
    }

    public void setTestCode(String testCode) {
        this.testCode = testCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
