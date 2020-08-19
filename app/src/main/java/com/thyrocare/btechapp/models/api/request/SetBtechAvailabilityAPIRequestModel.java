package com.thyrocare.btechapp.models.api.request;

/**
 * Created by Orion on 5/2/2017.
 */
public class SetBtechAvailabilityAPIRequestModel {
    private int ID;
    private int BtechId;
    private boolean Available;
    private String AvailableDate;
    private String EntryDate;
    private String LastUpdated;
    private String slots;

    public SetBtechAvailabilityAPIRequestModel() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getBtechId() {
        return BtechId;
    }

    public void setBtechId(int btechId) {
        BtechId = btechId;
    }

    public boolean isAvailable() {
        return Available;
    }

    public void setAvailable(boolean available) {
        Available = available;
    }

    public String getAvailableDate() {
        return AvailableDate;
    }

    public void setAvailableDate(String availableDate) {
        AvailableDate = availableDate;
    }

    public String getEntryDate() {
        return EntryDate;
    }

    public void setEntryDate(String entryDate) {
        EntryDate = entryDate;
    }

    public String getLastUpdated() {
        return LastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        LastUpdated = lastUpdated;
    }

    public String getSlots() {
        return slots;
    }

    public void setSlots(String slots) {
        this.slots = slots;
    }
}
