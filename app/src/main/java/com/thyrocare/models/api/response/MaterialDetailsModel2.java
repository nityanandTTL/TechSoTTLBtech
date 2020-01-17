package com.thyrocare.models.api.response;

import android.os.Parcel;
import android.os.Parcelable;

public class MaterialDetailsModel2 implements Parcelable {

    String MaterialId,MaterialName,OpeningStock,UsedStock;

    public MaterialDetailsModel2() {
    }

    protected MaterialDetailsModel2(Parcel in) {
        MaterialId = in.readString();
        MaterialName = in.readString();
        OpeningStock = in.readString();
        UsedStock = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(MaterialId);
        dest.writeString(MaterialName);
        dest.writeString(OpeningStock);
        dest.writeString(UsedStock);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MaterialDetailsModel2> CREATOR = new Creator<MaterialDetailsModel2>() {
        @Override
        public MaterialDetailsModel2 createFromParcel(Parcel in) {
            return new MaterialDetailsModel2(in);
        }

        @Override
        public MaterialDetailsModel2[] newArray(int size) {
            return new MaterialDetailsModel2[size];
        }
    };

    public String getMaterialId() {
        return MaterialId;
    }

    public void setMaterialId(String materialId) {
        MaterialId = materialId;
    }

    public String getMaterialName() {
        return MaterialName;
    }

    public void setMaterialName(String materialName) {
        MaterialName = materialName;
    }

    public String getOpeningStock() {
        return OpeningStock;
    }

    public void setOpeningStock(String openingStock) {
        OpeningStock = openingStock;
    }

    public String getUsedStock() {
        return UsedStock;
    }

    public void setUsedStock(String usedStock) {
        UsedStock = usedStock;
    }
}
