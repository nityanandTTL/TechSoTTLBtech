package com.thyrocare.models.api.request;

import java.io.Serializable;

public class GetAccessTokenForOTPRequestModel implements Serializable {

    String AppId;
    String Version;
    String Purpose;

    public String getAppId() {
        return AppId;
    }

    public void setAppId(String appId) {
        AppId = appId;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getPurpose() {
        return Purpose;
    }

    public void setPurpose(String purpose) {
        Purpose = purpose;
    }
}
