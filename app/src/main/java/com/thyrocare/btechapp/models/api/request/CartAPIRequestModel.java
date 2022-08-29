package com.thyrocare.btechapp.models.api.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.thyrocare.btechapp.models.data.CartRequestBeneficiaryModel;
import com.thyrocare.btechapp.models.data.CartRequestOrderModel;

import java.util.ArrayList;

/**
 * Created by Orion on 5/25/2017.
 */

public class CartAPIRequestModel implements Parcelable {
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
    String VisitId;
    String Distance;
    int DisAmtDue;
    ArrayList<CartRequestOrderModel> orders;
    ArrayList<CartRequestBeneficiaryModel> beneficiaries;
    public CartAPIRequestModel() {
    }
    protected CartAPIRequestModel(Parcel in) {
        VisitId = in.readString();
        Distance = in.readString();
        DisAmtDue = in.readInt();
        orders = in.createTypedArrayList(CartRequestOrderModel.CREATOR);
        beneficiaries = in.createTypedArrayList(CartRequestBeneficiaryModel.CREATOR);
    }

    public int getDisAmtDue() {
        return DisAmtDue;
    }

    public void setDisAmtDue(int disAmtDue) {
        DisAmtDue = disAmtDue;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(VisitId);
        dest.writeString(Distance);
        dest.writeInt(DisAmtDue);
        dest.writeTypedList(orders);
        dest.writeTypedList(beneficiaries);
    }

    @Override
    public int describeContents() {
        return 0;
    }

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
