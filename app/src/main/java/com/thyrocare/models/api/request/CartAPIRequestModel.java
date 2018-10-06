package com.thyrocare.models.api.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.thyrocare.models.data.CartRequestBeneficiaryModel;
import com.thyrocare.models.data.CartRequestOrderModel;

import java.util.ArrayList;

/**
 * Created by Orion on 5/25/2017.
 */

public class CartAPIRequestModel implements Parcelable {
    String VisitId;
    String Distance;
    ArrayList<CartRequestOrderModel> orders;
    ArrayList<CartRequestBeneficiaryModel> beneficiaries;

    public CartAPIRequestModel() {
    }

    protected CartAPIRequestModel(Parcel in) {
        VisitId = in.readString();
        Distance = in.readString();
        orders = in.createTypedArrayList(CartRequestOrderModel.CREATOR);
        beneficiaries = in.createTypedArrayList(CartRequestBeneficiaryModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(VisitId);
        dest.writeString(Distance);
        dest.writeTypedList(orders);
        dest.writeTypedList(beneficiaries);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CartAPIRequestModel> CREATOR = new Creator<CartAPIRequestModel>() {
        @Override
        public CartAPIRequestModel createFromParcel(Parcel in) {
            return new CartAPIRequestModel(in);
        }

        @Override
        public CartAPIRequestModel[] newArray(int size) {
            return new CartAPIRequestModel[size];
        }
    };

    public String getVisitId() {
        return VisitId;
    }

    public void setVisitId(String visitId) {
        VisitId = visitId;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public ArrayList<CartRequestOrderModel> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<CartRequestOrderModel> orders) {
        this.orders = orders;
    }

    public ArrayList<CartRequestBeneficiaryModel> getBeneficiaries() {
        return beneficiaries;
    }

    public void setBeneficiaries(ArrayList<CartRequestBeneficiaryModel> beneficiaries) {
        this.beneficiaries = beneficiaries;
    }
}
