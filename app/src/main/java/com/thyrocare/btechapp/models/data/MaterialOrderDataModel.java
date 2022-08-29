package com.thyrocare.btechapp.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 5/24/2017.
 */

public class MaterialOrderDataModel extends BaseModel implements Parcelable {
    public static final Creator<MaterialOrderDataModel> CREATOR = new Creator<MaterialOrderDataModel>() {
        @Override
        public MaterialOrderDataModel createFromParcel(Parcel in) {
            return new MaterialOrderDataModel(in);
        }

        @Override
        public MaterialOrderDataModel[] newArray(int size) {
            return new MaterialOrderDataModel[size];
        }
    };
    Integer MaterialId;
    Integer OrderQty;
    Integer Status;
    String item_name;
    String item_UnitCost;
    String item_UnitSize;

    public MaterialOrderDataModel() {
        super();
    }

    protected MaterialOrderDataModel(Parcel in) {
        super(in);
        MaterialId = in.readInt();
        OrderQty = in.readInt();
        Status = in.readInt();
        item_name = in.readString();
        item_UnitCost = in.readString();
        item_UnitSize = in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(MaterialId);
        dest.writeInt(OrderQty);
        dest.writeInt(Status);
        dest.writeString(item_name);
        dest.writeString(item_UnitCost);
        dest.writeString(item_UnitSize);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Integer getMaterialId() {
        return MaterialId;
    }

    public void setMaterialId(Integer materialId) {
        MaterialId = materialId;
    }

    public Integer getOrderQty() {
        return OrderQty;
    }

    public void setOrderQty(Integer orderQty) {
        OrderQty = orderQty;
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_UnitCost() {
        return item_UnitCost;
    }

    public void setItem_UnitCost(String item_UnitCost) {
        this.item_UnitCost = item_UnitCost;
    }

    public String getItem_UnitSize() {
        return item_UnitSize;
    }

    public void setItem_UnitSize(String item_UnitSize) {
        this.item_UnitSize = item_UnitSize;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MaterialOrderDataModel) {
            if (((MaterialOrderDataModel) obj).getMaterialId().equals(getMaterialId())) {
                return true;
            }
        }
        return false;
    }
}
