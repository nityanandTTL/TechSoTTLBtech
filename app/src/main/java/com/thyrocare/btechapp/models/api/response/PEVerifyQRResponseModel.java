package com.thyrocare.btechapp.models.api.response;

import com.google.gson.annotations.SerializedName;


public class PEVerifyQRResponseModel {
    @SerializedName("data")
    public DataDTO data;
    @SerializedName("status")
    public Boolean status;

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public static class DataDTO {
        @SerializedName("message")
        public String message;
        @SerializedName("status")
        public String status;
        @SerializedName("amount")
        public Double amount;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
