package com.thyrocare.btechapp.models.api.response;

public class PayTMVerifyResponseModel {

    /**
     * ResponseCode : null
     * TransactionId : 0
     * OrderNo : VL856AE5
     * SourceCode : null
     * ACCode : null
     * Status : RES0000
     * ChequeNo : null
     * ResponseMessage : Payment Captured Successfully
     * ReqParameters : null
     */

    private String ResponseCode;
    private Integer TransactionId;
    private String OrderNo;
    private String SourceCode;
    private String ACCode;
    private String Status;
    private String ChequeNo;
    private String ResponseMessage;
    private String ReqParameters;

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String ResponseCode) {
        this.ResponseCode = ResponseCode;
    }

    public Integer getTransactionId() {
        return TransactionId;
    }

    public void setTransactionId(Integer TransactionId) {
        this.TransactionId = TransactionId;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String OrderNo) {
        this.OrderNo = OrderNo;
    }

    public String getSourceCode() {
        return SourceCode;
    }

    public void setSourceCode(String SourceCode) {
        this.SourceCode = SourceCode;
    }

    public String getACCode() {
        return ACCode;
    }

    public void setACCode(String ACCode) {
        this.ACCode = ACCode;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getChequeNo() {
        return ChequeNo;
    }

    public void setChequeNo(String ChequeNo) {
        this.ChequeNo = ChequeNo;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }

    public void setResponseMessage(String ResponseMessage) {
        this.ResponseMessage = ResponseMessage;
    }

    public String getReqParameters() {
        return ReqParameters;
    }

    public void setReqParameters(String ReqParameters) {
        this.ReqParameters = ReqParameters;
    }
}
