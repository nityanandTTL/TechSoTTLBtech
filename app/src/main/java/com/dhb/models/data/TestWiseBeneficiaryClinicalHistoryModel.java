package com.dhb.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ISRO on 5/8/2017.
 */

public class TestWiseBeneficiaryClinicalHistoryModel implements Parcelable{
    private int BenId;
    private int ClinicalHistoryId;
    private String Test;

    public TestWiseBeneficiaryClinicalHistoryModel() {
    }

    protected TestWiseBeneficiaryClinicalHistoryModel(Parcel in) {
        BenId = in.readInt();
        ClinicalHistoryId = in.readInt();
        Test = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(BenId);
        dest.writeInt(ClinicalHistoryId);
        dest.writeString(Test);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TestWiseBeneficiaryClinicalHistoryModel> CREATOR = new Creator<TestWiseBeneficiaryClinicalHistoryModel>() {
        @Override
        public TestWiseBeneficiaryClinicalHistoryModel createFromParcel(Parcel in) {
            return new TestWiseBeneficiaryClinicalHistoryModel(in);
        }

        @Override
        public TestWiseBeneficiaryClinicalHistoryModel[] newArray(int size) {
            return new TestWiseBeneficiaryClinicalHistoryModel[size];
        }
    };

    public int getBenId() {
        return BenId;
    }

    public void setBenId(int benId) {
        BenId = benId;
    }

    public int getClinicalHistoryId() {
        return ClinicalHistoryId;
    }

    public void setClinicalHistoryId(int clinicalHistoryId) {
        ClinicalHistoryId = clinicalHistoryId;
    }

    public String getTest() {
        return Test;
    }

    public void setTest(String test) {
        Test = test;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TestWiseBeneficiaryClinicalHistoryModel){
            if(((TestWiseBeneficiaryClinicalHistoryModel) obj).getClinicalHistoryId()== getClinicalHistoryId() && ((TestWiseBeneficiaryClinicalHistoryModel) obj).getTest().equals(getTest())){
                return true;
            }
        }
        return false;
    }
}
