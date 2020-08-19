package com.thyrocare.btechapp.models.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Orion on 4/20/2017.
 */

public class BtechMaterialOrderDetails extends BaseModel implements Parcelable {

    private String OrderDate;
    private int OrderID;
    private ArrayList<OrderMaterialDetails> OMDtls;

    public BtechMaterialOrderDetails() {
        super();
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int orderID) {
        OrderID = orderID;
    }

    public ArrayList<OrderMaterialDetails> getOMDtls() {
        return OMDtls;
    }

    public void setOMDtls(ArrayList<OrderMaterialDetails> OMDtls) {
        this.OMDtls = OMDtls;
    }

    protected BtechMaterialOrderDetails(Parcel in) {
        super(in);
        OrderDate = in.readString();
        OrderID = in.readInt();
        OMDtls = in.createTypedArrayList(OrderMaterialDetails.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(OrderDate);
        dest.writeInt(OrderID);
        dest.writeTypedList(OMDtls);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BtechMaterialOrderDetails> CREATOR = new Creator<BtechMaterialOrderDetails>() {
        @Override
        public BtechMaterialOrderDetails createFromParcel(Parcel in) {
            return new BtechMaterialOrderDetails(in);
        }

        @Override
        public BtechMaterialOrderDetails[] newArray(int size) {
            return new BtechMaterialOrderDetails[size];
        }
    };
}
