package com.thyrocare.btechapp.models.api.request;

public class PEBenWiseApptSlotRequestModel {


    /**
     * apiKey : string
     * pincode : string
     * date : string
     * product : string
     * projectId : string
     * benCount : 0
     */

    private String apiKey;
    private String pincode;
    private String date;
    private String product;
    private String projectId;
    private Integer benCount;
    private String App;

    public String getApp() {
        return App;
    }

    public void setApp(String app) {
        App = app;
    }

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Integer getBenCount() {
        return benCount;
    }

    public void setBenCount(Integer benCount) {
        this.benCount = benCount;
    }
}
