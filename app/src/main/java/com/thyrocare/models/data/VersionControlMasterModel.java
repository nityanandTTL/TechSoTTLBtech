package com.thyrocare.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 5/11/2017.
 */

public class VersionControlMasterModel implements Parcelable {


    int Id;
    int APICurrentVerson;
    int APIMinVerson;
    boolean Status;
    String EntryDate;
    String LastUpdated;
    String AppUrl;
    String PlaystoreVersonName;
    int PlaystoreVerson;

    public VersionControlMasterModel() {
    }


    protected VersionControlMasterModel(Parcel in) {
        Id = in.readInt();
        APICurrentVerson = in.readInt();
        APIMinVerson = in.readInt();
        Status = in.readByte() != 0;
        EntryDate = in.readString();
        LastUpdated = in.readString();
        AppUrl = in.readString();
        PlaystoreVersonName = in.readString();
        PlaystoreVerson = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeInt(APICurrentVerson);
        dest.writeInt(APIMinVerson);
        dest.writeByte((byte) (Status ? 1 : 0));
        dest.writeString(EntryDate);
        dest.writeString(LastUpdated);
        dest.writeString(AppUrl);
        dest.writeString(PlaystoreVersonName);
        dest.writeInt(PlaystoreVerson);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VersionControlMasterModel> CREATOR = new Creator<VersionControlMasterModel>() {
        @Override
        public VersionControlMasterModel createFromParcel(Parcel in) {
            return new VersionControlMasterModel(in);
        }

        @Override
        public VersionControlMasterModel[] newArray(int size) {
            return new VersionControlMasterModel[size];
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

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }
}
