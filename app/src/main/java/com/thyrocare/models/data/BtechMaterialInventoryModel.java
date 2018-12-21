package com.thyrocare.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 4/20/2017.
 */

public class BtechMaterialInventoryModel extends BaseModel implements Parcelable {
    String MaterialID;
    String MaterialName;
    String VirtualStock;
    String LastUpdated;

    public BtechMaterialInventoryModel() {
        super();
    }

    protected BtechMaterialInventoryModel(Parcel in) {
        super(in);
        MaterialID = in.readString();
        MaterialName = in.readString();
        VirtualStock = in.readString();
        LastUpdated = in.readString();
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

    public static final Creator<BtechMaterialInventoryModel> CREATOR = new Creator<BtechMaterialInventoryModel>() {
        @Override
        public BtechMaterialInventoryModel createFromParcel(Parcel in) {
            return new BtechMaterialInventoryModel(in);
        }

        @Override
        public BtechMaterialInventoryModel[] newArray(int size) {
            return new BtechMaterialInventoryModel[size];
        }
    };
}
