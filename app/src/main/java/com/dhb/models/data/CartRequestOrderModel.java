package com.dhb.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 5/25/2017.
 */

public class CartRequestOrderModel implements Parcelable {
    private String OrderNo;
    private String BrandId;
    private int HC;

    public CartRequestOrderModel() {
    }

    protected CartRequestOrderModel(Parcel in) {
        OrderNo = in.readString();
        BrandId = in.readString();
        HC = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(OrderNo);
        dest.writeString(BrandId);
        dest.writeInt(HC);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CartRequestOrderModel> CREATOR = new Creator<CartRequestOrderModel>() {
        @Override
        public CartRequestOrderModel createFromParcel(Parcel in) {
            return new CartRequestOrderModel(in);
        }

        @Override
        public CartRequestOrderModel[] newArray(int size) {
            return new CartRequestOrderModel[size];
        }
    };

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getBrandId() {
        return BrandId;
    }

    public void setBrandId(String brandId) {
        BrandId = brandId;
    }

    public int getHC() {
        return HC;
    }

    public void setHC(int HC) {
        this.HC = HC;
    }
}
