package com.dhb.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by E4904 on 5/24/2017.
 */

public class MaterialOrderDataModel  extends BaseModel implements Parcelable {
    Integer MaterialId;
    Integer OrderQty;
    Integer Status;

    public MaterialOrderDataModel() {
        super ();
    }

    protected MaterialOrderDataModel(Parcel in) {
        super(in);
        MaterialId = in.readInt();
        OrderQty = in.readInt();
        Status = in.readInt();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(MaterialId);
        dest.writeInt(OrderQty);
        dest.writeInt(Status);

    }

    @Override
    public int describeContents() {
        return 0;
    }

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

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MaterialOrderDataModel){
            if(((MaterialOrderDataModel) obj).getMaterialId().equals(getMaterialId())){
                return true;
            }
        }
        return false;
    }
}
