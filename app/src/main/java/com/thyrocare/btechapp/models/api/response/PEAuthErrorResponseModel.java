package com.thyrocare.btechapp.models.api.response;

public class PEAuthErrorResponseModel {

    /**
     * status : 0
     * message : <string>
     */

    private Integer status;
    private String message;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
