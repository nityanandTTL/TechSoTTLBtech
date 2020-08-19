package com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels;

import java.io.Serializable;

public class GetSSLKeyRequestModel implements Serializable {

    String AppId;

    public String getAppId() {
        return AppId;
    }

    public void setAppId(String appId) {
        AppId = appId;
    }
}
