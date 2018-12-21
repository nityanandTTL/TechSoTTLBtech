package com.thyrocare.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 5/3/2017.
 */

public class MaterialsStocksModel_new extends  BaseModel implements Parcelable {

    Integer MaterialID;
    Integer ActualStock;
    Integer VirtualStock;

    public MaterialsStocksModel_new() {
        super ();
    }

    protected MaterialsStocksModel_new(Parcel in) {
        super(in);
        MaterialID = in.readInt();
        ActualStock = in.readInt();
        VirtualStock = in.readInt();


    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(MaterialID);
        dest.writeInt(ActualStock);
        dest.writeInt(VirtualStock);

    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MaterialsStocksModel_new){
            if(((MaterialsStocksModel_new) obj).getMaterialID().equals(this.getMaterialID())){
                return true;
            }
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    public static final Creator<MaterialsStocksModel_new> CREATOR = new Creator<MaterialsStocksModel_new>() {
        @Override
        public MaterialsStocksModel_new createFromParcel(Parcel in) {
            return new MaterialsStocksModel_new(in);
        }

        @Override
        public MaterialsStocksModel_new[] newArray(int size) {
            return new MaterialsStocksModel_new[size];
        }
    };


    public Integer getMaterialID() {
        return MaterialID;
    }

    public void setMaterialID(Integer materialID) {
        MaterialID = materialID;
    }

    public Integer getActualStock() {
        return ActualStock;
    }

    public void setActualStock(Integer actualStock) {
        ActualStock = actualStock;
    }

    public Integer getVirtualStock() {
        return VirtualStock;
    }

    public void setVirtualStock(Integer virtualStock) {
        VirtualStock = virtualStock;
    }
}
