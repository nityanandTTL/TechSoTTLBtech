package com.thyrocare.btechapp.models.api.response;

public class SignSummaryResponseModel {


    /**
     * Btechid : 0
     * SigninTime : null
     * SignoutTime : null
     * Response : FAILED
     * ResponseId : RES000X
     */

    private String Btechid;
    private String SigninTime;
    private String SignoutTime;
    private String Response;
    private String ResponseId;

    public String getBtechid() {
        return Btechid;
    }

    public void setBtechid(String Btechid) {
        this.Btechid = Btechid;
    }

    public String getSigninTime() {
        return SigninTime;
    }

    public void setSigninTime(String SigninTime) {
        this.SigninTime = SigninTime;
    }

    public String getSignoutTime() {
        return SignoutTime;
    }

    public void setSignoutTime(String SignoutTime) {
        this.SignoutTime = SignoutTime;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String Response) {
        this.Response = Response;
    }

    public String getResponseId() {
        return ResponseId;
    }

    public void setResponseId(String ResponseId) {
        this.ResponseId = ResponseId;
    }
}
