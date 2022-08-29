package com.thyrocare.btechapp.models.api.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 5/16/2017.
 */

public class PaymentStartTransactionAPIResponseModel implements Parcelable {
    public static final Creator<PaymentStartTransactionAPIResponseModel> CREATOR = new Creator<PaymentStartTransactionAPIResponseModel>() {
        @Override
        public PaymentStartTransactionAPIResponseModel createFromParcel(Parcel in) {
            return new PaymentStartTransactionAPIResponseModel(in);
        }

        @Override
        public PaymentStartTransactionAPIResponseModel[] newArray(int size) {
            return new PaymentStartTransactionAPIResponseModel[size];
        }
    };
    private String ResponseCode;
    private String TransactionId;
    private String ResponseMessage;
    private String tokenData;
    private PaymentProcessAPIResponseModel ReqParameters;

    public PaymentStartTransactionAPIResponseModel() {
    }

    protected PaymentStartTransactionAPIResponseModel(Parcel in) {
        ResponseCode = in.readString();
        TransactionId = in.readString();
        ResponseMessage = in.readString();
        tokenData = in.readString();
        ReqParameters = in.readParcelable(PaymentProcessAPIResponseModel.class.getClassLoader());
    }

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

    public String getTokenData() {
        return tokenData;
    }

    public void setTokenData(String tokenData) {
        this.tokenData = tokenData;
    }

    public PaymentProcessAPIResponseModel getReqParameters() {
        return ReqParameters;
    }

    public void setReqParameters(PaymentProcessAPIResponseModel reqParameters) {
        ReqParameters = reqParameters;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ResponseCode);
        dest.writeString(TransactionId);
        dest.writeString(ResponseMessage);
        dest.writeString(tokenData);
        dest.writeParcelable(ReqParameters, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
