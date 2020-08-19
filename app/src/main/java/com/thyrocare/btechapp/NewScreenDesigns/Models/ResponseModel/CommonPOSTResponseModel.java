package com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel;

import com.google.gson.annotations.SerializedName;

public class CommonPOSTResponseModel {

    String RESPONSE;
    String RES_ID;
    String OTPNO;
    String Token;

    @SerializedName("Response")
    String Response1;

    @SerializedName("ResId")
    String RESID1;

    String RespId;
    String RequestId;

    public String getRESPONSE() {
        return RESPONSE;
    }

    public void setRESPONSE(String RESPONSE) {
        this.RESPONSE = RESPONSE;
    }

    public String getRES_ID() {
        return RES_ID;
    }

    public void setRES_ID(String RES_ID) {
        this.RES_ID = RES_ID;
    }

    public String getResponse1() {
        return Response1;
    }

    public void setResponse1(String response1) {
        Response1 = response1;
    }

    public String getRESID1() {
        return RESID1;
    }

    public void setRESID1(String RESID1) {
        this.RESID1 = RESID1;
    }

    public String getOTPNO() {
        return OTPNO;
    }

    public void setOTPNO(String OTPNO) {
        this.OTPNO = OTPNO;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getRespId() {
        return RespId;
    }

    public void setRespId(String respId) {
        RespId = respId;
    }

    public String getRequestId() {
        return RequestId;
    }

    public void setRequestId(String requestId) {
        RequestId = requestId;
    }
}
