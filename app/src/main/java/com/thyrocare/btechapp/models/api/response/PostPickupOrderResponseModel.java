package com.thyrocare.btechapp.models.api.response;

public class PostPickupOrderResponseModel {


    /**
     * RespId : RES000
     * Response : Order picked up successfully.
     */

    private String RespId;
    private String Response;

    public String getRespId() {
        return RespId;
    }

    public void setRespId(String respId) {
        RespId = respId;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }
}
