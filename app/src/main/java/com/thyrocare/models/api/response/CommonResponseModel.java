package com.thyrocare.models.api.response;

import java.io.Serializable;

public class CommonResponseModel implements Serializable {

    String RES_ID;
    String RESPONSE;
    String Message;

    public String getRES_ID() {
        return RES_ID;
    }

    public void setRES_ID(String RES_ID) {
        this.RES_ID = RES_ID;
    }

    public String getRESPONSE() {
        return RESPONSE;
    }

    public void setRESPONSE(String RESPONSE) {
        this.RESPONSE = RESPONSE;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
