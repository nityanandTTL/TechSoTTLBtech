package com.thyrocare.btechapp.models.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Orion on 4/19/2017.
 */

public class BrandTestMasterModel extends BaseModel implements Parcelable {
    public static final Creator<BrandTestMasterModel> CREATOR = new Creator<BrandTestMasterModel>() {
        @Override
        public BrandTestMasterModel createFromParcel(Parcel in) {
            return new BrandTestMasterModel(in);
        }

        @Override
        public BrandTestMasterModel[] newArray(int size) {
            return new BrandTestMasterModel[size];
        }
    };
    private int BrandId;
    private String BrandName;
    private ArrayList<TestRateMasterModel> tstratemaster;

    public BrandTestMasterModel() {
        super();
    }

    protected BrandTestMasterModel(Parcel in) {
        super(in);
        BrandId = in.readInt();
        BrandName = in.readString();
        tstratemaster = in.createTypedArrayList(TestRateMasterModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(BrandId);
        dest.writeString(BrandName);
        dest.writeTypedList(tstratemaster);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getBrandId() {
        return BrandId;
    }

    public void setBrandId(int brandId) {
        BrandId = brandId;
    }

    public String getBrandName() {
        return BrandName;
    }

    public void setBrandName(String brandName) {
        BrandName = brandName;
    }

    public ArrayList<TestRateMasterModel> getTstratemaster() {
        return tstratemaster;
    }

    public void setTstratemaster(ArrayList<TestRateMasterModel> tstratemaster) {
        this.tstratemaster = tstratemaster;
    }
}
