package com.thyrocare.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 5/8/2017.<br/>
 *  http://bts.dxscloud.com/btsapi/api/BtechClients/884543107<br/>
 */

public class BtechClientsModel implements Parcelable {
    private int ClientId,Distance,Sequence;
    private String Name,Address,Pincode,Mobile,Latitude,Longitude,PickupStatus,ScannedBracode,BarcodeType;

    protected BtechClientsModel(Parcel in) {
        ClientId = in.readInt();
        Distance = in.readInt();
        Sequence = in.readInt();
        Name = in.readString();
        Address = in.readString();
        Pincode = in.readString();
        Mobile = in.readString();
        Latitude = in.readString();
        Longitude = in.readString();
        PickupStatus = in.readString();
        ScannedBracode = in.readString();
        BarcodeType = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ClientId);
        dest.writeInt(Distance);
        dest.writeInt(Sequence);
        dest.writeString(Name);
        dest.writeString(Address);
        dest.writeString(Pincode);
        dest.writeString(Mobile);
        dest.writeString(Latitude);
        dest.writeString(Longitude);
        dest.writeString(PickupStatus);
        dest.writeString(ScannedBracode);
        dest.writeString(BarcodeType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BtechClientsModel> CREATOR = new Creator<BtechClientsModel>() {
        @Override
        public BtechClientsModel createFromParcel(Parcel in) {
            return new BtechClientsModel(in);
        }

        @Override
        public BtechClientsModel[] newArray(int size) {
            return new BtechClientsModel[size];
        }
    };

    public int getClientId() {
        return ClientId;
    }

    public void setClientId(int clientId) {
        ClientId = clientId;
    }

    public int getDistance() {
        return Distance;
    }

    public void setDistance(int distance) {
        Distance = distance;
    }

    public int getSequence() {
        return Sequence;
    }

    public void setSequence(int sequence) {
        Sequence = sequence;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
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

    public String getPickupStatus() {
        return PickupStatus;
    }

    public void setPickupStatus(String pickupStatus) {
        PickupStatus = pickupStatus;
    }

    public String getScannedBracode() {
        return ScannedBracode;
    }

    public void setScannedBracode(String scannedBracode) {
        ScannedBracode = scannedBracode;
    }

    public String getBarcodeType() {
        return BarcodeType;
    }

    public void setBarcodeType(String barcodeType) {
        BarcodeType = barcodeType;
    }
}
