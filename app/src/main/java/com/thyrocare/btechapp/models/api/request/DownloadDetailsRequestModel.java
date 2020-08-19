package com.thyrocare.btechapp.models.api.request;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by E4904 on 9/4/2017.
 */

public class DownloadDetailsRequestModel implements Parcelable {

    private int BtechID;
    private int Version;

    protected DownloadDetailsRequestModel(Parcel in) {
        BtechID = in.readInt();
        Version = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(BtechID);
        dest.writeInt(Version);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DownloadDetailsRequestModel> CREATOR = new Creator<DownloadDetailsRequestModel>() {
        @Override
        public DownloadDetailsRequestModel createFromParcel(Parcel in) {
            return new DownloadDetailsRequestModel(in);
        }

        @Override
        public DownloadDetailsRequestModel[] newArray(int size) {
            return new DownloadDetailsRequestModel[size];
        }
    };

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

    public DownloadDetailsRequestModel() {



    }
}
