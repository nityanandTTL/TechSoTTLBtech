package com.dhb.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vendor3 on 5/25/2017.
 */

public class CartRequestBeneficiaryModel implements Parcelable {
    private String OrderNo;
    private String BenId;
    private String Tests;

    private String ProjId;
    private Boolean Addben;
    private Boolean isTestEdit;


    public CartRequestBeneficiaryModel() {
    }

    protected CartRequestBeneficiaryModel(Parcel in) {
        OrderNo = in.readString();
        BenId = in.readString();
        Tests = in.readString();
        ProjId = in.readString();
        isTestEdit = in.readByte() != 0;
        Addben = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(OrderNo);
        dest.writeString(BenId);
        dest.writeString(Tests);
        dest.writeString(ProjId);
        dest.writeByte((byte) (isTestEdit ? 1 : 0));
        dest.writeByte((byte) (Addben ? 1 : 0));

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CartRequestBeneficiaryModel> CREATOR = new Creator<CartRequestBeneficiaryModel>() {
        @Override
        public CartRequestBeneficiaryModel createFromParcel(Parcel in) {
            return new CartRequestBeneficiaryModel(in);
        }

        @Override
        public CartRequestBeneficiaryModel[] newArray(int size) {
            return new CartRequestBeneficiaryModel[size];
        }
    };

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getBenId() {
        return BenId;
    }

    public void setBenId(String benId) {
        BenId = benId;
    }

    public String getTests() {
        return Tests;
    }

    public void setTests(String tests) {
        Tests = tests;
    }



    public String getProjId() {
        return ProjId;
    }

    public void setProjId(String projId) {
        ProjId = projId;
    }

    public Boolean getAddben() {
        return Addben;
    }

    public void setAddben(Boolean addben) {
        Addben = addben;
    }

    public Boolean getTestEdit() {
        return isTestEdit;
    }

    public void setTestEdit(Boolean testEdit) {
        isTestEdit = testEdit;
    }
}
