package com.thyrocare.NewScreenDesigns.Models.ResponseModel;

public class VersionControlResponseModel {

    int Id;
    int APICurrentVerson;
    int APIMinVerson;
    boolean Status;
    String EntryDate;
    String LastUpdated;
    String AppUrl;
    String PlaystoreVersonName;
    int PlaystoreVerson;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getAPICurrentVerson() {
        return APICurrentVerson;
    }

    public void setAPICurrentVerson(int APICurrentVerson) {
        this.APICurrentVerson = APICurrentVerson;
    }

    public int getAPIMinVerson() {
        return APIMinVerson;
    }

    public void setAPIMinVerson(int APIMinVerson) {
        this.APIMinVerson = APIMinVerson;
    }

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
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

    public String getAppUrl() {
        return AppUrl;
    }

    public void setAppUrl(String appUrl) {
        AppUrl = appUrl;
    }

    public String getPlaystoreVersonName() {
        return PlaystoreVersonName;
    }

    public void setPlaystoreVersonName(String playstoreVersonName) {
        PlaystoreVersonName = playstoreVersonName;
    }

    public int getPlaystoreVerson() {
        return PlaystoreVerson;
    }

    public void setPlaystoreVerson(int playstoreVerson) {
        PlaystoreVerson = playstoreVerson;
    }
}
