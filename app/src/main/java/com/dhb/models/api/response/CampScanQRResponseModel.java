package com.dhb.models.api.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.dhb.models.data.CampAllOrderDetailsModel;

import java.util.ArrayList;

/**
 * Created by vendor1 on 5/15/2017.
 */

public class CampScanQRResponseModel implements Parcelable{
    private int SlotId, Distance;
    ArrayList<CampAllOrderDetailsModel> allOrderdetails;
    private String VisitId, Slot;

    public CampScanQRResponseModel() {
        // Required empty public constructor
    }

    protected CampScanQRResponseModel(Parcel in) {
        SlotId = in.readInt();
        Distance = in.readInt();
        allOrderdetails = in.createTypedArrayList(CampAllOrderDetailsModel.CREATOR);
        VisitId = in.readString();
        Slot = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(SlotId);
        dest.writeInt(Distance);
        dest.writeTypedList(allOrderdetails);
        dest.writeString(VisitId);
        dest.writeString(Slot);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CampScanQRResponseModel> CREATOR = new Creator<CampScanQRResponseModel>() {
        @Override
        public CampScanQRResponseModel createFromParcel(Parcel in) {
            return new CampScanQRResponseModel(in);
        }

        @Override
        public CampScanQRResponseModel[] newArray(int size) {
            return new CampScanQRResponseModel[size];
        }
    };

    public String getSlot() {
        return Slot;
    }

    public void setSlot(String slot) {
        Slot = slot;
    }

    public String getVisitId() {
        return VisitId;
    }

    public void setVisitId(String visitId) {
        VisitId = visitId;
    }

    public int getSlotId() {
        return SlotId;
    }

    public void setSlotId(int slotId) {
        SlotId = slotId;
    }


    public int getDistance() {
        return Distance;
    }

    public void setDistance(int distance) {
        Distance = distance;
    }

    public ArrayList<CampAllOrderDetailsModel> getAllOrderdetails() {
        return allOrderdetails;
    }

    public void setAllOrderdetails(ArrayList<CampAllOrderDetailsModel> allOrderdetails) {
        this.allOrderdetails = allOrderdetails;
    }
}
