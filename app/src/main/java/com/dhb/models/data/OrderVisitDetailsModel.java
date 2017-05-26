package com.dhb.models.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Pratik Ambhore on 4/21/2017.
 */

public class OrderVisitDetailsModel extends BaseModel implements Parcelable {

    private String VisitId;
    private String Slot;
    private String Response;
    private int SlotId;
    private ArrayList<OrderDetailsModel> allOrderdetails;
    private int Distance;
    private float EstIncome;

    public OrderVisitDetailsModel() {
        super();
    }

    protected OrderVisitDetailsModel(Parcel in) {
        super(in);
        VisitId = in.readString();
        Slot = in.readString();
        Response = in.readString();
        SlotId = in.readInt();
        allOrderdetails = in.createTypedArrayList(OrderDetailsModel.CREATOR);
        Distance = in.readInt();
        EstIncome = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(VisitId);
        dest.writeString(Slot);
        dest.writeString(Response);
        dest.writeInt(SlotId);
        dest.writeTypedList(allOrderdetails);
        dest.writeInt(Distance);
        dest.writeFloat(EstIncome);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderVisitDetailsModel> CREATOR = new Creator<OrderVisitDetailsModel>() {
        @Override
        public OrderVisitDetailsModel createFromParcel(Parcel in) {
            return new OrderVisitDetailsModel(in);
        }

        @Override
        public OrderVisitDetailsModel[] newArray(int size) {
            return new OrderVisitDetailsModel[size];
        }
    };

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public int getDistance() {
        return Distance;
    }

    public void setDistance(int distance) {
        Distance = distance;
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

    public int getSlotId() {
        return SlotId;
    }

    public void setSlotId(int slotId) {
        SlotId = slotId;
    }

    public ArrayList<OrderDetailsModel> getAllOrderdetails() {
        return allOrderdetails;
    }

    public void setAllOrderdetails(ArrayList<OrderDetailsModel> allOrderdetails) {
        this.allOrderdetails = allOrderdetails;
    }

    public float getEstIncome() {
        return EstIncome;
    }

    public void setEstIncome(float estIncome) {
        EstIncome = estIncome;
    }
}
