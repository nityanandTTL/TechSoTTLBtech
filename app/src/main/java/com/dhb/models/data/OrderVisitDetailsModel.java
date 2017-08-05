package com.dhb.models.data;



import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Orion on 4/21/2017.<br/>
 * http://bts.dxscloud.com/btsapi/api/OrderVisitDetails/884543107<br/>
 */

public class OrderVisitDetailsModel extends BaseModel implements Parcelable {

    private String VisitId;
    private String Slot;
    private String Response;
    private int SlotId;
    private ArrayList<OrderDetailsModel> allOrderdetails;
    private float Distance;
    private float EstIncome;
    private String AppointmentDate;

    public OrderVisitDetailsModel() {
        super();
    }

    protected OrderVisitDetailsModel(Parcel in) {
        VisitId = in.readString();
        Slot = in.readString();
        Response = in.readString();
        SlotId = in.readInt();
        allOrderdetails = in.createTypedArrayList(OrderDetailsModel.CREATOR);
        Distance = in.readFloat();
        EstIncome = in.readFloat();
        AppointmentDate = in.readString();
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

    public float getDistance() {
        return Distance;
    }

    public void setDistance(float distance) {
        Distance = distance;
    }

    public float getEstIncome() {
        return EstIncome;
    }

    public void setEstIncome(float estIncome) {
        EstIncome = estIncome;
    }

    public String getAppointmentDate() {
        return AppointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        AppointmentDate = appointmentDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(VisitId);
        dest.writeString(Slot);
        dest.writeString(Response);
        dest.writeInt(SlotId);
        dest.writeTypedList(allOrderdetails);
        dest.writeFloat(Distance);
        dest.writeFloat(EstIncome);
        dest.writeString(AppointmentDate);
    }
}
