package com.thyrocare.btechapp.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 4/27/2017.
 */

public class HUBBTechModel extends BaseModel implements Parcelable {
    public static final Creator<HUBBTechModel> CREATOR = new Creator<HUBBTechModel>() {
        @Override
        public HUBBTechModel createFromParcel(Parcel in) {
            return new HUBBTechModel(in);
        }

        @Override
        public HUBBTechModel[] newArray(int size) {
            return new HUBBTechModel[size];
        }
    };
    private int HubId;
    private String Incharge;
    private String Address;
    private String Pincode;
    private String CutOffTime;
    private String Latitude;
    private String Longitude;

    public HUBBTechModel() {
        super();
    }

    protected HUBBTechModel(Parcel in) {
        super(in);
        HubId = in.readInt();
        Incharge = in.readString();
        Address = in.readString();
        Pincode = in.readString();
        CutOffTime = in.readString();
        Latitude = in.readString();
        Longitude = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(HubId);
        dest.writeString(Incharge);
        dest.writeString(Address);
        dest.writeString(Pincode);
        dest.writeString(CutOffTime);
        dest.writeString(Latitude);
        dest.writeString(Longitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getHubId() {
        return HubId;
    }

    public void setHubId(int hubId) {
        HubId = hubId;
    }

    public String getIncharge() {
        return Incharge;
    }

    public void setIncharge(String incharge) {
        Incharge = incharge;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }

    public String getCutOffTime() {
        return CutOffTime;
    }

    public void setCutOffTime(String cutOffTime) {
        CutOffTime = cutOffTime;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

}
