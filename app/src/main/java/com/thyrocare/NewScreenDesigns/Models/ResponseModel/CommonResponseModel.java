package com.thyrocare.NewScreenDesigns.Models.ResponseModel;


import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class CommonResponseModel {

    String Response;
    String RES_ID;
    String message;
    String Version_no;
    boolean SSL_PINNING;

    @SerializedName("RESPONSE")
    String RESPONSE1;

    public CommonResponseModel() {
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public String getRES_ID() {
        return RES_ID;
    }

    public void setRES_ID(String RES_ID) {
        this.RES_ID = RES_ID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getVersion_no() {
        return Version_no;
    }

    public void setVersion_no(String version_no) {
        Version_no = version_no;
    }

    public boolean isSSL_PINNING() {
        return SSL_PINNING;
    }

    public void setSSL_PINNING(boolean SSL_PINNING) {
        this.SSL_PINNING = SSL_PINNING;
    }

    public String getRESPONSE1() {
        return RESPONSE1;
    }

    public void setRESPONSE1(String RESPONSE1) {
        this.RESPONSE1 = RESPONSE1;
    }
}
