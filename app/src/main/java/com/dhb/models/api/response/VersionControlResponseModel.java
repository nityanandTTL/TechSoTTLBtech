package com.dhb.models.api.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by E4904 on 5/11/2017.
 */

public class VersionControlResponseModel implements Parcelable{


    int Id;
    int APICurrentVerson;
    int APIMinVerson;
    int PlaystoreVerson;
    String PlaystoreVersonName;
    String AppUrl;
    Boolean Status;
    String EntryDate;
    String LastUpdated;


    protected VersionControlResponseModel(Parcel in) {
        Id = in.readInt();
        APICurrentVerson = in.readInt();
        APIMinVerson = in.readInt();
        PlaystoreVerson = in.readInt();
        PlaystoreVersonName = in.readString();
        AppUrl = in.readString();
        EntryDate = in.readString();
        LastUpdated = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeInt(APICurrentVerson);
        dest.writeInt(APIMinVerson);
        dest.writeInt(PlaystoreVerson);
        dest.writeString(PlaystoreVersonName);
        dest.writeString(AppUrl);
        dest.writeString(EntryDate);
        dest.writeString(LastUpdated);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VersionControlResponseModel> CREATOR = new Creator<VersionControlResponseModel>() {
        @Override
        public VersionControlResponseModel createFromParcel(Parcel in) {
            return new VersionControlResponseModel(in);
        }

        @Override
        public VersionControlResponseModel[] newArray(int size) {
            return new VersionControlResponseModel[size];
        }
    };

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

    public int getPlaystoreVerson() {
        return PlaystoreVerson;
    }

    public void setPlaystoreVerson(int playstoreVerson) {
        PlaystoreVerson = playstoreVerson;
    }

    public String getPlaystoreVersonName() {
        return PlaystoreVersonName;
    }

    public void setPlaystoreVersonName(String playstoreVersonName) {
        PlaystoreVersonName = playstoreVersonName;
    }

    public String getAppUrl() {
        return AppUrl;
    }

    public void setAppUrl(String appUrl) {
        AppUrl = appUrl;
    }

    public Boolean getStatus() {
        return Status;
    }

    public void setStatus(Boolean status) {
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

    public VersionControlResponseModel() {
    }
}
