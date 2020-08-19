package com.thyrocare.btechapp.models.data;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 5/2/2017.
 */

public class BTStockMaterialsModel implements Parcelable{
    private int materialID;
    private String materialName;
    private int virtualStock;
    private int actualStock;
    private String remarks;
    private String lastUpdated;

    protected BTStockMaterialsModel(Parcel in) {
        materialID = in.readInt();
        materialName = in.readString();
        virtualStock = in.readInt();
        actualStock = in.readInt();
        remarks = in.readString();
        lastUpdated = in.readString();
    }

    public static final Creator<BTStockMaterialsModel> CREATOR = new Creator<BTStockMaterialsModel>() {
        @Override
        public BTStockMaterialsModel createFromParcel(Parcel in) {
            return new BTStockMaterialsModel(in);
        }

        @Override
        public BTStockMaterialsModel[] newArray(int size) {
            return new BTStockMaterialsModel[size];
        }
    };

    public int getMaterialID() {
        return materialID;
    }

    public void setMaterialID(int materialID) {
        this.materialID = materialID;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public int getVirtualStock() {
        return virtualStock;
    }

    public void setVirtualStock(int virtualStock) {
        this.virtualStock = virtualStock;
    }

    public int getActualStock() {
        return actualStock;
    }

    public void setActualStock(int actualStock) {
        this.actualStock = actualStock;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(materialID);
        dest.writeString(materialName);
        dest.writeInt(virtualStock);
        dest.writeInt(actualStock);
        dest.writeString(remarks);
        dest.writeString(lastUpdated);
    }
}
