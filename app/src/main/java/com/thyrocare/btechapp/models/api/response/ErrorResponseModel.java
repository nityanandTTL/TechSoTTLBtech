package com.thyrocare.btechapp.models.api.response;

/**
 * Created by Orion on 9/11/15.
 */
public class ErrorResponseModel {
    private String CustomErrorCode;
    private String Message;
    private int status;

    public String getCustomErrorCode() {
        return CustomErrorCode;
    }

    public void setCustomErrorCode(String customErrorCode) {
        this.CustomErrorCode = customErrorCode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        this.Message = message;
    }

}