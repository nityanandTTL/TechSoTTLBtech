package com.thyrocare.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 6/30/2017.
 */

public class BtechwithHub_BarcodeDataModel implements Parcelable {

    private int BtechID;
    private String BtechName;
    private String Barcode;
    private String BarcodeType;
    private String Latitude;
    private String Longitude;
    private boolean IsReceived;

    protected BtechwithHub_BarcodeDataModel(Parcel in) {
        BtechID = in.readInt();
        BtechName = in.readString();
        Barcode = in.readString();
        BarcodeType = in.readString();
        Latitude = in.readString();
        Longitude = in.readString();
        IsReceived = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(BtechID);
        dest.writeString(BtechName);
        dest.writeString(Barcode);
        dest.writeString(BarcodeType);
        dest.writeString(Latitude);
        dest.writeString(Longitude);
        dest.writeByte((byte) (IsReceived ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BtechwithHub_BarcodeDataModel> CREATOR = new Creator<BtechwithHub_BarcodeDataModel>() {
        @Override
        public BtechwithHub_BarcodeDataModel createFromParcel(Parcel in) {
            return new BtechwithHub_BarcodeDataModel(in);
        }

        @Override
        public BtechwithHub_BarcodeDataModel[] newArray(int size) {
            return new BtechwithHub_BarcodeDataModel[size];
        }
    };

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    @Override
    public String toString() {
        return "BtechwithHub_BarcodeDataModel{" +
                "BtechID=" + BtechID +
                ", BtechName='" + BtechName + '\'' +
                ", Barcode='" + Barcode + '\'' +
                ", BarcodeType='" + BarcodeType + '\'' +
                ", IsReceived=" + IsReceived +
                '}';
    }

    public int getBtechID() {
        return BtechID;
    }

    public void setBtechID(int btechID) {
        BtechID = btechID;
    }

    public String getBtechName() {
        return BtechName;
    }

    public void setBtechName(String btechName) {
        BtechName = btechName;
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String barcode) {
        Barcode = barcode;
    }

    public String getBarcodeType() {
        return BarcodeType;
    }

    public void setBarcodeType(String barcodeType) {
        BarcodeType = barcodeType;
    }

    public boolean isReceived() {
        return IsReceived;
    }

    public void setReceived(boolean received) {
        IsReceived = received;
    }

}
