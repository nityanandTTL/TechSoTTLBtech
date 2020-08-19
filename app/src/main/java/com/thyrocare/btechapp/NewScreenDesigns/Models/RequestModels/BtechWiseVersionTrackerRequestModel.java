package com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels;

public class BtechWiseVersionTrackerRequestModel {

    private int BtechID;
    private int Version;

    public int getBtechID() {
        return BtechID;
    }

    public void setBtechID(int btechID) {
        BtechID = btechID;
    }

    public int getVersion() {
        return Version;
    }

    public void setVersion(int version) {
        Version = version;
    }
}
