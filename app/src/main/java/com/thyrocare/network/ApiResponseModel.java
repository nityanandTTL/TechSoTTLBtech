package com.thyrocare.network;

import java.util.HashMap;
import java.util.Map;

public class ApiResponseModel {

    private String responseData;

    private int statusCode;

    private Map<String, String> responseHeaderMap;

    public ApiResponseModel() {
        super();
        responseHeaderMap = new HashMap<String, String>();
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Map<String, String> getResponseHeaderMap() {
        return responseHeaderMap;
    }

    public void setResponseHeaderMap(Map<String, String> responseHeaderMap) {
        this.responseHeaderMap = responseHeaderMap;
    }

    public void putHedarMap(String key, String value) {
        this.responseHeaderMap.put(key, value);
    }



}