package com.thyrocare.btechapp.models.api.request;

public class PEPaymentRequestModel {

    String TransactionId;
    String OrderNo;

    public String getTransactionId() {
        return TransactionId;
    }

    public void setTransactionId(String transactionId) {
        TransactionId = transactionId;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }
}
