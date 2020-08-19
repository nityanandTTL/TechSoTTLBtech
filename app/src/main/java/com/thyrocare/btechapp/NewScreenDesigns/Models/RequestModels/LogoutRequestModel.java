package com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels;

public class LogoutRequestModel {

    private String UserId;
    private String DeviceId;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }
}
