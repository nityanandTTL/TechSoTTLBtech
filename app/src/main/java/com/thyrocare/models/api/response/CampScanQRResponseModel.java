package com.thyrocare.models.api.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.thyrocare.models.data.CampAllOrderDetailsModel;

import java.util.ArrayList;

/**
 * Created by Orion on 5/15/2017.
 */

public class CampScanQRResponseModel implements Parcelable{
    private int SlotId;
    private double Distance;
    ArrayList<CampAllOrderDetailsModel> allOrderdetails;
    private String VisitId, Slot;

    public CampScanQRResponseModel() {
    }

    public int getSlotId() {
        return SlotId;
    }

    public void setSlotId(int slotId) {
        SlotId = slotId;
    }

    public double getDistance() {
        return Distance;
    }

    public void setDistance(double distance) {
        Distance = distance;
    }

    public ArrayList<CampAllOrderDetailsModel> getAllOrderdetails() {
        return allOrderdetails;
    }

    public void setAllOrderdetails(ArrayList<CampAllOrderDetailsModel> allOrderdetails) {
        this.allOrderdetails = allOrderdetails;
    }

    public String getVisitId() {
        return VisitId;
    }

    public void setVisitId(String visitId) {
        VisitId = visitId;
    }

    public String getSlot() {
        return Slot;
    }

    public void setSlot(String slot) {
        Slot = slot;
    }

    protected CampScanQRResponseModel(Parcel in) {
        SlotId = in.readInt();
        Distance = in.readDouble();
        allOrderdetails = in.createTypedArrayList(CampAllOrderDetailsModel.CREATOR);
        VisitId = in.readString();
        Slot = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(SlotId);
        dest.writeDouble(Distance);
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
}
