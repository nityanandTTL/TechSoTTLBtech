package com.dhb.models.api.request;

/**
 * Created by vendor1 on 5/2/2017.
 */

public class HubStartRequestModel {
    private String BtechId;
    private String HubId;
    private int Type;

    public String getBtechId() {
        return BtechId;
    }

    public void setBtechId(String btechId) {
        BtechId = btechId;
    }

    public String getHubId() {
        return HubId;
    }

    public void setHubId(String hubId) {
        HubId = hubId;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }
}
