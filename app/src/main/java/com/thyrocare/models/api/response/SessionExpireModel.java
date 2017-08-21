package com.thyrocare.models.api.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Orion on 19/8/15.
 */
public class SessionExpireModel {

    private String status;
    private String message;
    @SerializedName("status_code")
    private String statusCode;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}
