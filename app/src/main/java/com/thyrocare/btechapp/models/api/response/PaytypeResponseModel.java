package com.thyrocare.btechapp.models.api.response;

public class PaytypeResponseModel {


    /**
     * RespId : RES000
     * Response : Order Paytype status
     * Paytype : POSTPAID
     */

    private String RespId;
    private String Response;
    private String Paytype;

    public String getRespId() {
        return RespId;
    }

    public void setRespId(String RespId) {
        this.RespId = RespId;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String Response) {
        this.Response = Response;
    }

    public String getPaytype() {
        return Paytype;
    }

    public void setPaytype(String Paytype) {
        this.Paytype = Paytype;
    }
}
