package com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 5/8/2017.
 */

public class BeneficiaryTestWiseClinicalHistoryModel implements Parcelable {
    public static final Creator<BeneficiaryTestWiseClinicalHistoryModel> CREATOR = new Creator<BeneficiaryTestWiseClinicalHistoryModel>() {
        @Override
        public BeneficiaryTestWiseClinicalHistoryModel createFromParcel(Parcel in) {
            return new BeneficiaryTestWiseClinicalHistoryModel(in);
        }

        @Override
        public BeneficiaryTestWiseClinicalHistoryModel[] newArray(int size) {
            return new BeneficiaryTestWiseClinicalHistoryModel[size];
        }
    };
    private int BenId;
    private int ClinicalHistoryId;
    private String Test;

    public BeneficiaryTestWiseClinicalHistoryModel() {
    }

    protected BeneficiaryTestWiseClinicalHistoryModel(Parcel in) {
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
        if (obj instanceof BeneficiaryTestWiseClinicalHistoryModel) {
            if (((BeneficiaryTestWiseClinicalHistoryModel) obj).getClinicalHistoryId() == getClinicalHistoryId() && ((BeneficiaryTestWiseClinicalHistoryModel) obj).getTest().equals(getTest())) {
                return true;
            }
        }
        return false;
    }
}
