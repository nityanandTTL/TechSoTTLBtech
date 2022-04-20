package com.thyrocare.btechapp.models.api.response;

public class PECutomerIntimationSMSResponeModel {

    /**
     * status : 200
     * message : SMS sent successfully
     * Response : SUCCESS
     * PaymentResponse : null
     */

    private Integer status;
    private String message;
    private String Response;
    private String ResponseId;
    private Object PaymentResponse;

    public String getResponseId() {
        return ResponseId;
    }

    public void setResponseId(String responseId) {
        ResponseId = responseId;
    }

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

    public String getResponse() {
        return Response;
    }

    public void setResponse(String Response) {
        this.Response = Response;
    }

    public Object getPaymentResponse() {
        return PaymentResponse;
    }

    public void setPaymentResponse(Object PaymentResponse) {
        this.PaymentResponse = PaymentResponse;
    }
}
