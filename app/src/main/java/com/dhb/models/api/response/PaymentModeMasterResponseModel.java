package com.dhb.models.api.response;

/**
 * Created by vendor1 on 6/22/2017.
 */

public class PaymentModeMasterResponseModel {
    private int ModeId;
    private String ModeName;

    public PaymentModeMasterResponseModel() {
    }

    public int getModeId() {
        return ModeId;
    }

    public void setModeId(int modeId) {
        ModeId = modeId;
    }

    public String getModeName() {
        return ModeName;
    }

    public void setModeName(String modeName) {
        ModeName = modeName;
    }
}
