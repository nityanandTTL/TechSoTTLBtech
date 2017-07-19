package com.dhb.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 5/2/2017.
 */

public class Earning_NewRegisterModel implements Parcelable {


    private int BtechId;
    private String BtechName;
    private int FastingOrders;
    private int NonFastingOrders;
    private int TotalOrders;
    private int TotalEarning;
    private String CreditDate;
    private int CreditedAmount;

    public Earning_NewRegisterModel(Parcel in) {
        BtechId = in.readInt();
        BtechName = in.readString();
        FastingOrders = in.readInt();
        NonFastingOrders = in.readInt();
        TotalOrders = in.readInt();
        TotalEarning = in.readInt();
        CreditDate = in.readString();
        CreditedAmount = in.readInt();
    }

    public static final Creator<Earning_NewRegisterModel> CREATOR = new Creator<Earning_NewRegisterModel>() {
        @Override
        public Earning_NewRegisterModel createFromParcel(Parcel in) {
            return new Earning_NewRegisterModel(in);
        }

        @Override
        public Earning_NewRegisterModel[] newArray(int size) {
            return new Earning_NewRegisterModel[size];
        }
    };

    public Earning_NewRegisterModel() {

    }

    @Override
    public String toString() {
        return "Earning_NewRegisterModel{" +
                "BtechId=" + BtechId +
                ", BtechName='" + BtechName + '\'' +
                ", FastingOrders=" + FastingOrders +
                ", NonFastingOrders=" + NonFastingOrders +
                ", TotalOrders=" + TotalOrders +
                ", TotalEarning=" + TotalEarning +
                ", CreditDate='" + CreditDate + '\'' +
                ", CreditedAmount=" + CreditedAmount +
                '}';
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

    public int getFastingOrders() {
        return FastingOrders;
    }

    public void setFastingOrders(int fastingOrders) {
        FastingOrders = fastingOrders;
    }

    public int getNonFastingOrders() {
        return NonFastingOrders;
    }

    public void setNonFastingOrders(int nonFastingOrders) {
        NonFastingOrders = nonFastingOrders;
    }

    public int getTotalOrders() {
        return TotalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        TotalOrders = totalOrders;
    }

    public int getTotalEarning() {
        return TotalEarning;
    }

    public void setTotalEarning(int totalEarning) {
        TotalEarning = totalEarning;
    }

    public String getCreditDate() {
        return CreditDate;
    }

    public void setCreditDate(String creditDate) {
        CreditDate = creditDate;
    }

    public int getCreditedAmount() {
        return CreditedAmount;
    }

    public void setCreditedAmount(int creditedAmount) {
        CreditedAmount = creditedAmount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(BtechId);
        dest.writeString(BtechName);
        dest.writeInt(FastingOrders);
        dest.writeInt(NonFastingOrders);
        dest.writeInt(TotalOrders);
        dest.writeInt(TotalEarning);
        dest.writeString(CreditDate);
        dest.writeInt(CreditedAmount);
    }
}
