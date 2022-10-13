package com.thyrocare.btechapp.models.api.response;

public class ConfirmOrderResponseModel {
    String message;
    int status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int isStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
