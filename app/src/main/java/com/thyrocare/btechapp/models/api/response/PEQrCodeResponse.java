package com.thyrocare.btechapp.models.api.response;

import com.google.gson.annotations.SerializedName;


public class PEQrCodeResponse {

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
        @SerializedName("image")
        public String image;
        @SerializedName("qr_code")
        public String qrCode;
        @SerializedName("qr_data")
        public String qrData;
        @SerializedName("message")
        public Object message;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getQrCode() {
            return qrCode;
        }

        public void setQrCode(String qrCode) {
            this.qrCode = qrCode;
        }

        public String getQrData() {
            return qrData;
        }

        public void setQrData(String qrData) {
            this.qrData = qrData;
        }

        public Object getMessage() {
            return message;
        }

        public void setMessage(Object message) {
            this.message = message;
        }
    }
}
