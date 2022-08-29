package com.thyrocare.btechapp.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 4/19/2017.
 */

public class BeneficiarySampleTypeDetailsModel extends BaseModel implements Parcelable {
    public static final Creator<BeneficiarySampleTypeDetailsModel> CREATOR = new Creator<BeneficiarySampleTypeDetailsModel>() {
        @Override
        public BeneficiarySampleTypeDetailsModel createFromParcel(Parcel in) {
            return new BeneficiarySampleTypeDetailsModel(in);
        }

        @Override
        public BeneficiarySampleTypeDetailsModel[] newArray(int size) {
            return new BeneficiarySampleTypeDetailsModel[size];
        }
    };
    private String id;
    private int benId;
    private String sampleType;
    private String Tests;

    public BeneficiarySampleTypeDetailsModel() {
        super();
    }

    protected BeneficiarySampleTypeDetailsModel(Parcel in) {
        super(in);
        id = in.readString();
        benId = in.readInt();
        sampleType = in.readString();
        Tests = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(id);
        dest.writeInt(benId);
        dest.writeString(sampleType);
        dest.writeString(Tests);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getBenId() {
        return benId;
    }

    public void setBenId(int benId) {
        this.benId = benId;
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BeneficiarySampleTypeDetailsModel) {
            if (((BeneficiarySampleTypeDetailsModel) obj).getSampleType().equals(getSampleType())) {
                return true;
            }
        }
        return false;
    }
}
