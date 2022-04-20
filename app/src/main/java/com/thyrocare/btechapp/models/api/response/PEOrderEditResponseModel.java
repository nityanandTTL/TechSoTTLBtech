package com.thyrocare.btechapp.models.api.response;

public class PEOrderEditResponseModel {
    /**
     * status : 0
     * message : null
     * Response : FAILED
     */

    private Integer status;
    private Object message;
    private String Response;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String Response) {
        this.Response = Response;
    }
}
