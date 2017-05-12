package com.dhb.models.api.response;

/**
 * Created by E4904 on 5/11/2017.
 */

public class VersionControlResponseModel {


    int Id;
    int CurrentVirson;
    int MinVirson;
    String Status;
    String EntryDate;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getCurrentVirson() {
        return CurrentVirson;
    }

    public void setCurrentVirson(int currentVirson) {
        CurrentVirson = currentVirson;
    }

    public int getMinVirson() {
        return MinVirson;
    }

    public void setMinVirson(int minVirson) {
        MinVirson = minVirson;
    }



    public String getEntryDate() {
        return EntryDate;
    }

    public void setEntryDate(String entryDate) {
        EntryDate = entryDate;
    }

}
