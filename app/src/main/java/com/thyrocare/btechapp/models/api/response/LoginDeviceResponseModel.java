package com.thyrocare.btechapp.models.api.response;

/**
 * Created by Orion on 4/20/2017.
 */

public class LoginDeviceResponseModel {
    int RespId;
    String RespMessage;

    public int getRespId() {
        return RespId;
    }

    public void setRespId(int respId) {
        RespId = respId;
    }

    public String getRespMessage() {
        return RespMessage;
    }

    public void setRespMessage(String respMessage) {
        RespMessage = respMessage;
    }
}
