package com.dhb.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 5/10/2017.
 */
public class BeneficiaryLabAlertsModel implements Parcelable{
    private int LabAlertId;
    private int BenId;

    public BeneficiaryLabAlertsModel() {
    }

    protected BeneficiaryLabAlertsModel(Parcel in) {
        LabAlertId = in.readInt();
        BenId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(LabAlertId);
        dest.writeInt(BenId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BeneficiaryLabAlertsModel> CREATOR = new Creator<BeneficiaryLabAlertsModel>() {
        @Override
        public BeneficiaryLabAlertsModel createFromParcel(Parcel in) {
            return new BeneficiaryLabAlertsModel(in);
        }

        @Override
        public BeneficiaryLabAlertsModel[] newArray(int size) {
            return new BeneficiaryLabAlertsModel[size];
        }
    };

    public int getBenId() {
        return BenId;
    }

    public void setBenId(int benId) {
        BenId = benId;
    }

    public int getLabAlertId() {
        return LabAlertId;
    }

    public void setLabAlertId(int labAlertId) {
        LabAlertId = labAlertId;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof BeneficiaryLabAlertsModel){
            if(((BeneficiaryLabAlertsModel) obj).getLabAlertId()==getLabAlertId() && ((BeneficiaryLabAlertsModel) obj).getBenId()==getBenId()){
                return true;
            }
        }
        return false;
    }
}
