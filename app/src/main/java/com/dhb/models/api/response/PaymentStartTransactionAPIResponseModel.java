package com.dhb.models.api.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.dhb.models.data.PaymentNameValueModel;

import java.util.ArrayList;

/**
 * Created by Vendor3 on 5/16/2017.
 */

public class PaymentStartTransactionAPIResponseModel implements Parcelable {
    private String ResponseCode;
    private String TransactionId;
    private String ResponseMessage;
    private String tokenData;
    private PaymentProcessAPIResponseModel ReqParameters;

    public PaymentStartTransactionAPIResponseModel() {
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

    protected PaymentStartTransactionAPIResponseModel(Parcel in) {
        ResponseCode = in.readString();
        TransactionId = in.readString();
        ResponseMessage = in.readString();
        tokenData = in.readString();
        ReqParameters = in.readParcelable(PaymentProcessAPIResponseModel.class.getClassLoader());
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
}
