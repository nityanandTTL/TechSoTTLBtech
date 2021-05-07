package com.thyrocare.btechapp.models.data;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 4/20/2017.
 */

public class DeviceLoginDetailsModel implements Parcelable {
    private String DeviceId;
    private String Url;
    private int UserId;
    private int IsStech;

    protected DeviceLoginDetailsModel(Parcel in) {
        DeviceId = in.readString();
        Url = in.readString();
        UserId = in.readInt();
        IsStech = in.readInt();
    }

    public static final Creator<DeviceLoginDetailsModel> CREATOR = new Creator<DeviceLoginDetailsModel>() {
        @Override
        public DeviceLoginDetailsModel createFromParcel(Parcel in) {
            return new DeviceLoginDetailsModel(in);
        }

        @Override
        public DeviceLoginDetailsModel[] newArray(int size) {
            return new DeviceLoginDetailsModel[size];
        }
    };

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public int getIsStech() {
        return IsStech;
    }

    public void setIsStech(int isStech) {
        IsStech = isStech;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(DeviceId);
        dest.writeInt(UserId);
        dest.writeInt(IsStech);
        dest.writeString(Url);
    }
}
