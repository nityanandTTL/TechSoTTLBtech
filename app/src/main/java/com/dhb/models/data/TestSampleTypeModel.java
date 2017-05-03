package com.dhb.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Pratik Ambhore on 4/19/2017.
 */

public class TestSampleTypeModel extends BaseModel implements Parcelable {
    private String id;
    private String sampleType;

    public TestSampleTypeModel() {
        super();
    }

    protected TestSampleTypeModel(Parcel in) {
        super(in);
        id = in.readString();
        sampleType = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(id);
        dest.writeString(sampleType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TestSampleTypeModel> CREATOR = new Creator<TestSampleTypeModel>() {
        @Override
        public TestSampleTypeModel createFromParcel(Parcel in) {
            return new TestSampleTypeModel(in);
        }

        @Override
        public TestSampleTypeModel[] newArray(int size) {
            return new TestSampleTypeModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSampleType() {
        return sampleType;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }
}
