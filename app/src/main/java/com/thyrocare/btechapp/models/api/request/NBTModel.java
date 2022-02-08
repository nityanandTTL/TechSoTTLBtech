package com.thyrocare.btechapp.models.api.request;

public class NBTModel {
    private String BtechID;
    private String Date;
    private String Name;

    public String getBtechID() {
        return BtechID;
    }

    public void setBtechID(String btechID) {
        BtechID = btechID;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
