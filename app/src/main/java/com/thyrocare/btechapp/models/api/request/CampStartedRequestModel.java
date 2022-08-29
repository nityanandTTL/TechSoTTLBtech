package com.thyrocare.btechapp.models.api.request;

/**
 * Created by Orion on 5/12/2017.
 */

public class CampStartedRequestModel {
    int BtechId, CampId, Type;

    public int getBtechId() {
        return BtechId;
    }

    public void setBtechId(int btechId) {
        BtechId = btechId;
    }

    public int getCampId() {
        return CampId;
    }

    public void setCampId(int campId) {
        CampId = campId;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }
}
