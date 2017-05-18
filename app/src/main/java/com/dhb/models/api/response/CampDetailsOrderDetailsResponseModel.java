package com.dhb.models.api.response;

import com.dhb.models.data.CampAllOrderDetailsModel;

import java.util.ArrayList;

/**
 * Created by vendor1 on 5/15/2017.
 */

public class CampDetailsOrderDetailsResponseModel {
    private int VisitId,SlotId,Slot,Distance;
    ArrayList<CampAllOrderDetailsModel> allOrderdetails;

    public int getVisitId() {
        return VisitId;
    }

    public void setVisitId(int visitId) {
        VisitId = visitId;
    }

    public int getSlotId() {
        return SlotId;
    }

    public void setSlotId(int slotId) {
        SlotId = slotId;
    }

    public int getSlot() {
        return Slot;
    }

    public void setSlot(int slot) {
        Slot = slot;
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
