package com.thyrocare.btechapp.models.api.response;

/**
 * Created by Orion on 6/22/2017.
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
