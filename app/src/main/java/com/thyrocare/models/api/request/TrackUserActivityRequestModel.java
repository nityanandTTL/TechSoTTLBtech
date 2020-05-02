package com.thyrocare.models.api.request;

public class TrackUserActivityRequestModel {
    private String IMIENo;
    private int AppId;
    private String ModType;
    private String Version;
    private String OS;
    private String UserID;
    private String Token;
    private String Islogin;

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