package com.thyrocare.btechapp.models.api.response;

import com.google.gson.annotations.SerializedName;

public class UpdatePaymentResponseModel {
    @SerializedName("Response")
    public String response;
    @SerializedName("ResposeID")
    public String resposeID;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getResposeID() {
        return resposeID;
    }

    public void setResposeID(String resposeID) {
        this.resposeID = resposeID;
    }
}
