package com.thyrocare.btechapp.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 5/2/2017.
 */

public class BTMaterialsModel extends BaseModel implements Parcelable {
    public static final Creator<BTMaterialsModel> CREATOR = new Creator<BTMaterialsModel>() {
        @Override
        public BTMaterialsModel createFromParcel(Parcel in) {
            return new BTMaterialsModel(in);
        }

        @Override
        public BTMaterialsModel[] newArray(int size) {
            return new BTMaterialsModel[size];
        }
    };
    String MaterialID;
    String MaterialName;
    String VirtualStock;
    String LastUpdated;

    public BTMaterialsModel() {
        super();
        VirtualStock = "0";
    }

    protected BTMaterialsModel(Parcel in) {
        super(in);
        MaterialID = in.readString();
        MaterialName = in.readString();
        VirtualStock = in.readString();
        LastUpdated = in.readString();


    }

    public String getMaterialID() {
        return MaterialID;
    }

    public void setMaterialID(String materialID) {
        MaterialID = materialID;
    }

    public String getMaterialName() {
        return MaterialName;
    }

    public void setMaterialName(String materialName) {
        MaterialName = materialName;
    }

    public String getVirtualStock() {
        return VirtualStock;
    }

    public void setVirtualStock(String virtualStock) {
        VirtualStock = virtualStock;
    }

    public String getLastUpdated() {
        return LastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        LastUpdated = lastUpdated;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(MaterialID);
        dest.writeString(MaterialName);
        dest.writeString(VirtualStock);
        dest.writeString(LastUpdated);
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
