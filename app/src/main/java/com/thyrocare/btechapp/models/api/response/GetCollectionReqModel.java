package com.thyrocare.btechapp.models.api.response;

public class GetCollectionReqModel {


    /**
     * apiKey :
     * pincode : 110001
     */

    private String apiKey;
    private String pincode;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
