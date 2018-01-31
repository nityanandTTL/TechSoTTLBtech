package com.thyrocare.models.api.request;

/**
 * Created by Orion on 5/2/2017.
 */

public class OlcStartRequestModel {
    private String BtechId;
    private String ClientId;
    private int Type;

    public String getBtechId() {
        return BtechId;
    }

    public void setBtechId(String btechId) {
        BtechId = btechId;
    }

    public String getClientId() {
        return ClientId;
    }

    public void setClientId(String clientId) {
        ClientId = clientId;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }
}
