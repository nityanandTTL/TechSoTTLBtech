package com.thyrocare.btechapp.models.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Orion on 5/15/2017.
 */
public class OrderBookingDetailsModel implements Parcelable {
    public static final Creator<OrderBookingDetailsModel> CREATOR = new Creator<OrderBookingDetailsModel>() {
        @Override
        public OrderBookingDetailsModel createFromParcel(Parcel in) {
            return new OrderBookingDetailsModel(in);
        }

        @Override
        public OrderBookingDetailsModel[] newArray(int size) {
            return new OrderBookingDetailsModel[size];
        }
    };
    private int BtechId;
    private String VisitId;
    private String ProcessLocation;
    private int PaymentMode;
    private int coupon;
    private ArrayList<OrderDetailsModel> orddtl;

    public OrderBookingDetailsModel() {
    }

    protected OrderBookingDetailsModel(Parcel in) {
        BtechId = in.readInt();
        VisitId = in.readString();
        ProcessLocation = in.readString();
        PaymentMode = in.readInt();
        coupon = in.readInt();
        orddtl = in.createTypedArrayList(OrderDetailsModel.CREATOR);
    }

    public int getCoupon() {
        return coupon;
    }

    public void setCoupon(int coupon) {
        this.coupon = coupon;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(BtechId);
        dest.writeString(VisitId);
        dest.writeString(ProcessLocation);
        dest.writeInt(PaymentMode);
        dest.writeTypedList(orddtl);
        dest.writeInt(coupon);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getBtechId() {
        return BtechId;
    }

    public void setBtechId(int btechId) {
        BtechId = btechId;
    }

    public String getVisitId() {
        return VisitId;
    }

    public void setVisitId(String visitId) {
        VisitId = visitId;
    }

    public String getProcessLocation() {
        return ProcessLocation;
    }

    public void setProcessLocation(String processLocation) {
        ProcessLocation = processLocation;
    }

    public ArrayList<OrderDetailsModel> getOrddtl() {
        return orddtl;
    }

    public void setOrddtl(ArrayList<OrderDetailsModel> orddtl) {
        this.orddtl = orddtl;
    }

    public int getPaymentMode() {
        return PaymentMode;
    }

    public void setPaymentMode(int paymentMode) {
        PaymentMode = paymentMode;
    }
}
