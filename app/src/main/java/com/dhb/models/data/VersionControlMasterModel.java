package com.dhb.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by E4904 on 5/11/2017.
 */

public class VersionControlMasterModel extends BaseModel implements Parcelable {
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

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getEntryDate() {
        return EntryDate;
    }

    public void setEntryDate(String entryDate) {
        EntryDate = entryDate;
    }

    int Id;
    int CurrentVirson;
    int MinVirson;
    String Status;
    String EntryDate;

    public VersionControlMasterModel() {
        super ();
    }

/*   protected VersionControlMasterModel(Parcel in) {
        super(in);

    }*/

    protected VersionControlMasterModel(Parcel in) {
        super(in);
        CurrentVirson = in.readInt();
        MinVirson = in.readInt();
        Id = in.readInt();
        Status=in.readString();
        EntryDate = in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(Id);
        dest.writeInt(MinVirson);
        dest.writeInt(CurrentVirson);
        dest.writeString(Status);
        dest.writeString(EntryDate);
    }

    @Override
    public int describeContents() {
        return super.describeContents();
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
}
