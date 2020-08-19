package com.thyrocare.btechapp.models.api.request;

import java.io.Serializable;

public class StockAvailabilityRequestModel implements Serializable {

    String APIKey;
    String userCode;

    public String getAPIKey() {
        return APIKey;
    }

    public void setAPIKey(String APIKey) {
        this.APIKey = APIKey;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
}
