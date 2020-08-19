package com.thyrocare.btechapp.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 5/3/2017.
 */

public class MaterialsStocksModel extends  BaseModel implements Parcelable {




    Integer MaterialID;
    Integer ActualStock;

    public MaterialsStocksModel() {
        super ();
    }

    protected MaterialsStocksModel(Parcel in) {
        super(in);
        MaterialID = in.readInt();
        ActualStock = in.readInt();


    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(MaterialID);
        dest.writeInt(ActualStock);

    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MaterialsStocksModel){
            if(((MaterialsStocksModel) obj).getMaterialID().equals(this.getMaterialID())){
                return true;
            }
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    public static final Creator<MaterialsStocksModel> CREATOR = new Creator<MaterialsStocksModel>() {
        @Override
        public MaterialsStocksModel createFromParcel(Parcel in) {
            return new MaterialsStocksModel(in);
        }

        @Override
        public MaterialsStocksModel[] newArray(int size) {
            return new MaterialsStocksModel[size];
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
}
