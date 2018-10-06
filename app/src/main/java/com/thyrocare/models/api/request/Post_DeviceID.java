package com.thyrocare.models.api.request;

/**
 * Created by e5233@thyrocare.com on 30/4/18.
 */

public class Post_DeviceID {

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
