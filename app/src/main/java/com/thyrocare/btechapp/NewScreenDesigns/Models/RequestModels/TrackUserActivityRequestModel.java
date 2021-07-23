package com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels;

import com.google.gson.annotations.SerializedName;

public class TrackUserActivityRequestModel {
    private String IMIENo;
    private int AppId;
    private String ModType;
    private String Version;
    private String OS;
    private String UserID;
    private String Token;
    private String Islogin;
    private String lat;
    private String longi;
    private String macadd;
    private String ipadd;
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public String getMacadd() {
        return macadd;
    }

    public void setMacadd(String macadd) {
        this.macadd = macadd;
    }

    public String getIpadd() {
        return ipadd;
    }

    public void setIpadd(String ipadd) {
        this.ipadd = ipadd;
    }

    public String getIMIENo() {
        return IMIENo;
    }

    public void setIMIENo(String IMIENo) {
        this.IMIENo = IMIENo;
    }

    public int getAppId() {
        return AppId;
    }

    public void setAppId(int AppId) {
        this.AppId = AppId;
    }

    public String getModType() {
        return ModType;
    }

    public void setModType(String ModType) {
        this.ModType = ModType;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String Version) {
        this.Version = Version;
    }

    public String getOS() {
        return OS;
    }

    public void setOS(String OS) {
        this.OS = OS;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String Token) {
        this.Token = Token;
    }

    public String getIslogin() {
        return Islogin;
    }

    public void setIslogin(String Islogin) {
        this.Islogin = Islogin;
    }
}