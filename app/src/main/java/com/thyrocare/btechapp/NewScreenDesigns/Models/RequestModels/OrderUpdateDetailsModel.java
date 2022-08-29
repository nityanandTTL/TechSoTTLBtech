package com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Orion on 6/28/2017.
 */

public class OrderUpdateDetailsModel implements Parcelable {
    public static final Creator<OrderUpdateDetailsModel> CREATOR = new Creator<OrderUpdateDetailsModel>() {
        @Override
        public OrderUpdateDetailsModel createFromParcel(Parcel in) {
            return new OrderUpdateDetailsModel(in);
        }

        @Override
        public OrderUpdateDetailsModel[] newArray(int size) {
            return new OrderUpdateDetailsModel[size];
        }
    };
    private int BtechId;
    private String BtechName;
    private String ETA;
    private SCTDetailsModel sla;
    private String SOT;
    private String SCR;
    private String HCR;
    private String Receipt;
    private int PaymentDue;
    private String BookedBy;
    private String BookedByName;
    private String HistoryHeading;
    private String OrderHistory;
    private String BtechMobile;
    private ArrayList<OrderHistoryDetails> ordHistory;

    protected OrderUpdateDetailsModel(Parcel in) {
        BtechId = in.readInt();
        BtechName = in.readString();
        ETA = in.readString();
        sla = in.readParcelable(SCTDetailsModel.class.getClassLoader());
        SOT = in.readString();
        SCR = in.readString();
        HCR = in.readString();
        Receipt = in.readString();
        PaymentDue = in.readInt();
        BookedBy = in.readString();
        BookedByName = in.readString();
        HistoryHeading = in.readString();
        OrderHistory = in.readString();
        BtechMobile = in.readString();
        ordHistory = in.createTypedArrayList(OrderHistoryDetails.CREATOR);
    }

    public OrderUpdateDetailsModel() {
    }

    public ArrayList<OrderHistoryDetails> getOrdHistory() {
        return ordHistory;
    }

    public void setOrdHistory(ArrayList<OrderHistoryDetails> ordHistory) {
        this.ordHistory = ordHistory;
    }

    public int getBtechId() {
        return BtechId;
    }

    public void setBtechId(int btechId) {
        BtechId = btechId;
    }

    public String getBtechName() {
        return BtechName;
    }

    public void setBtechName(String btechName) {
        BtechName = btechName;
    }

    public String getETA() {
        return ETA;
    }

    public void setETA(String ETA) {
        this.ETA = ETA;
    }

    public SCTDetailsModel getSla() {
        return sla;
    }

    public void setSla(SCTDetailsModel sla) {
        this.sla = sla;
    }

    public String getSOT() {
        return SOT;
    }

    public void setSOT(String SOT) {
        this.SOT = SOT;
    }

    public String getSCR() {
        return SCR;
    }

    public void setSCR(String SCR) {
        this.SCR = SCR;
    }

    public String getReceipt() {
        return Receipt;
    }

    public void setReceipt(String receipt) {
        Receipt = receipt;
    }

    public int getPaymentDue() {
        return PaymentDue;
    }

    public void setPaymentDue(int paymentDue) {
        PaymentDue = paymentDue;
    }

    public String getBookedBy() {
        return BookedBy;
    }

    public void setBookedBy(String bookedBy) {
        BookedBy = bookedBy;
    }

    public String getHistoryHeading() {
        return HistoryHeading;
    }

    public void setHistoryHeading(String historyHeading) {
        HistoryHeading = historyHeading;
    }

    public String getOrderHistory() {
        return OrderHistory;
    }

    public void setOrderHistory(String orderHistory) {
        OrderHistory = orderHistory;
    }

    public String getHCR() {
        return HCR;
    }

    public void setHCR(String HCR) {
        this.HCR = HCR;
    }

    public String getBookedByName() {
        return BookedByName;
    }

    public void setBookedByName(String bookedByName) {
        BookedByName = bookedByName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(BtechId);
        dest.writeString(BtechName);
        dest.writeString(ETA);
        dest.writeParcelable(sla, flags);
        dest.writeString(SOT);
        dest.writeString(SCR);
        dest.writeString(HCR);
        dest.writeString(Receipt);
        dest.writeInt(PaymentDue);
        dest.writeString(BookedBy);
        dest.writeString(BookedByName);
        dest.writeString(HistoryHeading);
        dest.writeString(OrderHistory);
        dest.writeString(BtechMobile);
        dest.writeTypedList(ordHistory);
    }

    public String getBtechMobile() {
        return BtechMobile;
    }

    public void setBtechMobile(String btechMobile) {
        this.BtechMobile = btechMobile;
    }
}
