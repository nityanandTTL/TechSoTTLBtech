package com.dhb.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 5/25/2017.
 */

public class CartAPIResponseBeneficiariesModel implements Parcelable {
    private String OrderNo;
    private int BenId;
    private String Tests;
    private boolean Addben;

    public CartAPIResponseBeneficiariesModel() {
    }

    protected CartAPIResponseBeneficiariesModel(Parcel in) {
        OrderNo = in.readString();
        BenId = in.readInt();
        Tests = in.readString();
        Addben = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(OrderNo);
        dest.writeInt(BenId);
        dest.writeString(Tests);
        dest.writeByte((byte) (Addben ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CartAPIResponseBeneficiariesModel> CREATOR = new Creator<CartAPIResponseBeneficiariesModel>() {
        @Override
        public CartAPIResponseBeneficiariesModel createFromParcel(Parcel in) {
            return new CartAPIResponseBeneficiariesModel(in);
        }

        @Override
        public CartAPIResponseBeneficiariesModel[] newArray(int size) {
            return new CartAPIResponseBeneficiariesModel[size];
        }
    };

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public int getBenId() {
        return BenId;
    }

    public void setBenId(int benId) {
        BenId = benId;
    }

    public String getTests() {
        return Tests;
    }

    public void setTests(String tests) {
        Tests = tests;
    }

    public boolean isAddben() {
        return Addben;
    }

    public void setAddben(boolean addben) {
        Addben = addben;
    }
}
