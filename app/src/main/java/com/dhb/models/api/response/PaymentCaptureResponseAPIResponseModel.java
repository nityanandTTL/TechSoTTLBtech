package com.dhb.models.api.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vendor3 on 5/16/2017.
 */

public class PaymentCaptureResponseAPIResponseModel implements Parcelable {
    private String ResponseCode;
    private String TransactionId;
    private String OrderNo;
    private String SourceCode;
    private String ACCode;
    private String Status;
    private String ChequeNo;
    private String ResponseMessage;
    private PaymentProcessAPIResponseModel ReqParameters;

    public PaymentCaptureResponseAPIResponseModel() {
    }

    protected PaymentCaptureResponseAPIResponseModel(Parcel in) {
        ResponseCode = in.readString();
        TransactionId = in.readString();
        OrderNo = in.readString();
        SourceCode = in.readString();
        ACCode = in.readString();
        Status = in.readString();
        ChequeNo = in.readString();
        ResponseMessage = in.readString();
        ReqParameters = in.readParcelable(PaymentProcessAPIResponseModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ResponseCode);
        dest.writeString(TransactionId);
        dest.writeString(OrderNo);
        dest.writeString(SourceCode);
        dest.writeString(ACCode);
        dest.writeString(Status);
        dest.writeString(ChequeNo);
        dest.writeString(ResponseMessage);
        dest.writeParcelable(ReqParameters, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PaymentCaptureResponseAPIResponseModel> CREATOR = new Creator<PaymentCaptureResponseAPIResponseModel>() {
        @Override
        public PaymentCaptureResponseAPIResponseModel createFromParcel(Parcel in) {
            return new PaymentCaptureResponseAPIResponseModel(in);
        }

        @Override
        public PaymentCaptureResponseAPIResponseModel[] newArray(int size) {
            return new PaymentCaptureResponseAPIResponseModel[size];
        }
    };

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    public String getTransactionId() {
        return TransactionId;
    }

    public void setTransactionId(String transactionId) {
        TransactionId = transactionId;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        ResponseMessage = responseMessage;
    }

    public PaymentProcessAPIResponseModel getReqParameters() {
        return ReqParameters;
    }

    public void setReqParameters(PaymentProcessAPIResponseModel reqParameters) {
        ReqParameters = reqParameters;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getSourceCode() {
        return SourceCode;
    }

    public void setSourceCode(String sourceCode) {
        SourceCode = sourceCode;
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

    public void setStatus(String status) {
        Status = status;
    }

    public String getChequeNo() {
        return ChequeNo;
    }

    public void setChequeNo(String chequeNo) {
        ChequeNo = chequeNo;
    }
}
