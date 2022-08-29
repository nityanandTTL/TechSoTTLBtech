package com.thyrocare.btechapp.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 4/20/2017.
 */


public class OrderMaterialDetails extends BaseModel implements Parcelable {
    public static final Creator<OrderMaterialDetails> CREATOR = new Creator<OrderMaterialDetails>() {
        @Override
        public OrderMaterialDetails createFromParcel(Parcel in) {
            return new OrderMaterialDetails(in);
        }

        @Override
        public OrderMaterialDetails[] newArray(int size) {
            return new OrderMaterialDetails[size];
        }
    };
    private String MaterialID;
    private String MaterialName;
    private String ProposedQty;
    private String UnitSize;
    private String UnitRate;
    private String NetAmount;

    public OrderMaterialDetails() {
        super();
    }

    protected OrderMaterialDetails(Parcel in) {
        super(in);
        MaterialID = in.readString();
        MaterialName = in.readString();
        ProposedQty = in.readString();
        UnitSize = in.readString();
        UnitRate = in.readString();
        NetAmount = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(MaterialID);
        dest.writeString(MaterialName);
        dest.writeString(ProposedQty);
        dest.writeString(UnitSize);
        dest.writeString(UnitRate);
        dest.writeString(NetAmount);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
