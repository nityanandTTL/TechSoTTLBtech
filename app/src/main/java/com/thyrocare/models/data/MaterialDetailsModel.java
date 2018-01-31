package com.thyrocare.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 5/2/2017.
 */

public class MaterialDetailsModel extends BaseModel implements Parcelable {

    public Integer getMaterialId() {
        return MaterialId;
    }

    public void setMaterialId(Integer materialId) {
        MaterialId = materialId;
    }

    public String getMaterialName() {
        return MaterialName;
    }

    public void setMaterialName(String materialName) {
        MaterialName = materialName;
    }

    public String getShortName() {
        return ShortName;
    }

    public void setShortName(String shortName) {
        ShortName = shortName;
    }

    public String getUnitCost() {
        return UnitCost;
    }

    public void setUnitCost(String unitCost) {
        UnitCost = unitCost;
    }

    public String getUnitSize() {
        return UnitSize;
    }

    public void setUnitSize(String unitSize) {
        UnitSize = unitSize;
    }

    public String getTax() {
        return Tax;
    }

    public void setTax(String tax) {
        Tax = tax;
    }

    Integer MaterialId;
    String MaterialName;
    String ShortName;
    String UnitCost;
    String UnitSize;
    String Tax;

    public MaterialDetailsModel() {
        super ();
    }

    protected MaterialDetailsModel(Parcel in) {
        super(in);
        MaterialId = in.readInt();
        MaterialName = in.readString();
        ShortName = in.readString();
        UnitCost = in.readString();
        UnitSize = in.readString();
        Tax = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(MaterialId);
        dest.writeString(MaterialName);
        dest.writeString(ShortName);
        dest.writeString(UnitCost);
        dest.writeString(UnitSize);
        dest.writeString(Tax);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MaterialDetailsModel> CREATOR = new Creator<MaterialDetailsModel>() {
        @Override
        public MaterialDetailsModel createFromParcel(Parcel in) {
            return new MaterialDetailsModel(in);
        }

        @Override
        public MaterialDetailsModel[] newArray(int size) {
            return new MaterialDetailsModel[size];
        }
    };
}
