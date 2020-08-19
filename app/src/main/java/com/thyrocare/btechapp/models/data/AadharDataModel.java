package com.thyrocare.btechapp.models.data;

import android.os.Parcel;
import android.os.Parcelable;

public class AadharDataModel implements Parcelable {

    private String name;
    private String address;
    private String gender;
    private String aadharNumber;
    private String pincode;
    private String dob;
    private String house;
    private String street;
    private String lm;
    private String po;
    private String dist;
    private String subdist;
    private String state;
    private String vtc;
    private String mobile;
    private String email;
    private String loc;



    public AadharDataModel() {

    }

    protected AadharDataModel(Parcel in) {
        name = in.readString();
        address = in.readString();
        gender = in.readString();
        aadharNumber = in.readString();
        pincode = in.readString();
        dob = in.readString();
        house = in.readString();
        street = in.readString();
        lm = in.readString();
        po = in.readString();
        dist = in.readString();
        subdist = in.readString();
        state = in.readString();
        vtc = in.readString();
        mobile = in.readString();
        email = in.readString();
        loc=in.readString();
    }

    public static final Creator<AadharDataModel> CREATOR = new Creator<AadharDataModel>() {
        @Override
        public AadharDataModel createFromParcel(Parcel in) {
            return new AadharDataModel(in);
        }

        @Override
        public AadharDataModel[] newArray(int size) {
            return new AadharDataModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getLm() {
        return lm;
    }

    public void setLm(String lm) {
        this.lm = lm;
    }

    public String getPo() {
        return po;
    }

    public void setPo(String po) {
        this.po = po;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public String getSubdist() {
        return subdist;
    }

    public void setSubdist(String subdist) {
        this.subdist = subdist;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getVtc() {
        return vtc;
    }

    public void setVtc(String vtc) {
        this.vtc = vtc;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }
    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(gender);
        dest.writeString(aadharNumber);
        dest.writeString(pincode);
        dest.writeString(dob);
        dest.writeString(house);
        dest.writeString(street);
        dest.writeString(lm);
        dest.writeString(po);
        dest.writeString(dist);
        dest.writeString(subdist);
        dest.writeString(state);
        dest.writeString(vtc);
        dest.writeString(mobile);
        dest.writeString(email);
        dest.writeString(loc);
    }
}