package com.dhb.models.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Pratik Ambhore on 4/19/2017.
 */

public class OrderBookingModel extends BaseModel implements Parcelable {
    private int BtechId;
    private String VisitId;
    private ArrayList<OrderDetailsModel> orddtl;

    public OrderBookingModel() {
        super();
    }

    protected OrderBookingModel(Parcel in) {
        super(in);
        BtechId = in.readInt();
        VisitId = in.readString();
        orddtl = in.createTypedArrayList(OrderDetailsModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(BtechId);
        dest.writeString(VisitId);
        dest.writeTypedList(orddtl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderBookingModel> CREATOR = new Creator<OrderBookingModel>() {
        @Override
        public OrderBookingModel createFromParcel(Parcel in) {
            return new OrderBookingModel(in);
        }

        @Override
        public OrderBookingModel[] newArray(int size) {
            return new OrderBookingModel[size];
        }
    };

    public int getBtechId() {
        return BtechId;
    }

    public void setBtechId(int btechId) {
        BtechId = btechId;
    }

    public String getVisitId() {
        return VisitId;
    }

    public void setVisitId(String visitId) {
        VisitId = visitId;
    }

    public ArrayList<OrderDetailsModel> getOrddtl() {
        return orddtl;
    }

    public void setOrddtl(ArrayList<OrderDetailsModel> orddtl) {
        this.orddtl = orddtl;
    }
}
