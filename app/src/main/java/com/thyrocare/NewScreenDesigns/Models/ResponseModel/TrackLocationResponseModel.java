package com.thyrocare.NewScreenDesigns.Models.ResponseModel;

public class TrackLocationResponseModel {


    /**
     * Message : Invalid Btech Id! Mandatory!
     * CustomErrorCode : 12
     */

    private String Message;
    private int CustomErrorCode;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public int getCustomErrorCode() {
        return CustomErrorCode;
    }

    public void setCustomErrorCode(int CustomErrorCode) {
        this.CustomErrorCode = CustomErrorCode;
    }
}
