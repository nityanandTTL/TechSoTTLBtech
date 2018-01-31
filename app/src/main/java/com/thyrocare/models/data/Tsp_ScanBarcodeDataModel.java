package com.thyrocare.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 7/6/2017.
 */

public class Tsp_ScanBarcodeDataModel implements Parcelable {

    private String Barcode;
    private boolean IsReceived;


    protected Tsp_ScanBarcodeDataModel(Parcel in) {
        Barcode = in.readString();
        IsReceived = in.readByte() != 0;
    }

    public static final Creator<Tsp_ScanBarcodeDataModel> CREATOR = new Creator<Tsp_ScanBarcodeDataModel>() {
        @Override
        public Tsp_ScanBarcodeDataModel createFromParcel(Parcel in) {
            return new Tsp_ScanBarcodeDataModel(in);
        }

        @Override
        public Tsp_ScanBarcodeDataModel[] newArray(int size) {
            return new Tsp_ScanBarcodeDataModel[size];
        }
    };

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String barcode) {
        Barcode = barcode;
    }

    public boolean isReceived() {
        return IsReceived;
    }

    public void setReceived(boolean received) {
        IsReceived = received;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Barcode);
        dest.writeByte((byte) (IsReceived ? 1 : 0));
    }
}
