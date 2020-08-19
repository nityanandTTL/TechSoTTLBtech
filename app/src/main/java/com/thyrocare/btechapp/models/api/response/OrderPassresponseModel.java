package com.thyrocare.btechapp.models.api.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.thyrocare.btechapp.models.data.Orderallocation;

import java.util.ArrayList;

/**
 * Created by E4904 on 9/1/2017.
 */

public class OrderPassresponseModel implements Parcelable{

    private ArrayList <Orderallocation> orderAllocation;
    private String Response;

    protected OrderPassresponseModel(Parcel in) {
        orderAllocation = in.createTypedArrayList(Orderallocation.CREATOR);
        Response = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(orderAllocation);
        dest.writeString(Response);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderPassresponseModel> CREATOR = new Creator<OrderPassresponseModel>() {
        @Override
        public OrderPassresponseModel createFromParcel(Parcel in) {
            return new OrderPassresponseModel(in);
        }

        @Override
        public OrderPassresponseModel[] newArray(int size) {
            return new OrderPassresponseModel[size];
        }
    };

    public ArrayList<Orderallocation> getOrderAllocation() {
        return orderAllocation;
    }

    public void setOrderAllocation(ArrayList<Orderallocation> orderAllocation) {
        this.orderAllocation = orderAllocation;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public OrderPassresponseModel() {
    }

}
