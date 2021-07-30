package com.thyrocare.btechapp.models.api.response;

public class PayTMResponseModel {

    /**
     * StatusCode : 200
     * PaymentLink : https://paytm.me/J-5nvbc
     * Response : Payment link is created successfully
     */

    private String StatusCode;
    private String PaymentLink;
    private String Response;

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String StatusCode) {
        this.StatusCode = StatusCode;
    }

    public String getPaymentLink() {
        return PaymentLink;
    }

    public void setPaymentLink(String PaymentLink) {
        this.PaymentLink = PaymentLink;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String Response) {
        this.Response = Response;
    }
}
