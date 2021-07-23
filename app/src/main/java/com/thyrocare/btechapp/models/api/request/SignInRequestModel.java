package com.thyrocare.btechapp.models.api.request;

public class SignInRequestModel {


    /**
     * Type : SIGNIN
     * Btechid : 12548756
     * SignInRemarks :
     * SignOutRemarks :
     * Apikey :
     */

    private String Type;
    private String Btechid;
    private String SignInRemarks;
    private String SignOutRemarks;
    private String Apikey;
    private String Latitude;
    private String Longitude;
    private String Outlongitude;
    private String OutLatitude;

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

    public String getOutlongitude() {
        return Outlongitude;
    }

    public void setOutlongitude(String outlongitude) {
        Outlongitude = outlongitude;
    }

    public String getOutLatitude() {
        return OutLatitude;
    }

    public void setOutLatitude(String outLatitude) {
        OutLatitude = outLatitude;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public String getBtechid() {
        return Btechid;
    }

    public void setBtechid(String Btechid) {
        this.Btechid = Btechid;
    }

    public String getSignInRemarks() {
        return SignInRemarks;
    }

    public void setSignInRemarks(String SignInRemarks) {
        this.SignInRemarks = SignInRemarks;
    }

    public String getSignOutRemarks() {
        return SignOutRemarks;
    }

    public void setSignOutRemarks(String SignOutRemarks) {
        this.SignOutRemarks = SignOutRemarks;
    }

    public String getApikey() {
        return Apikey;
    }

    public void setApikey(String Apikey) {
        this.Apikey = Apikey;
    }
}
