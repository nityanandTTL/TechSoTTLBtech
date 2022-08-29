package com.thyrocare.btechapp.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 4/19/2017.
 */

public class BaseModel implements Parcelable {
    public static final Creator<BaseModel> CREATOR = new Creator<BaseModel>() {
        @Override
        public BaseModel createFromParcel(Parcel in) {
            return new BaseModel(in);
        }

        @Override
        public BaseModel[] newArray(int size) {
            return new BaseModel[size];
        }
    };
    private String recordStatus;
    private long createdAt;
    private String createdBy;
    private long updatedAt;
    private String updatedBy;
    private String syncStatus;
    private String syncAction;

    public BaseModel() {
        recordStatus = "A";
    }

    protected BaseModel(Parcel in) {
        recordStatus = in.readString();
        createdAt = in.readLong();
        createdBy = in.readString();
        updatedAt = in.readLong();
        updatedBy = in.readString();
        syncStatus = in.readString();
        syncAction = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(recordStatus);
        dest.writeLong(createdAt);
        dest.writeString(createdBy);
        dest.writeLong(updatedAt);
        dest.writeString(updatedBy);
        dest.writeString(syncStatus);
        dest.writeString(syncAction);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }

    public String getSyncAction() {
        return syncAction;
    }

    public void setSyncAction(String syncAction) {
        this.syncAction = syncAction;
    }
}
