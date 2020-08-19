package com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel;

public class NotificationMappingResponseModel {

    private String LMECode;
    private String Response;
    private String ResponseId;
    private String Token;

    public String getLMECode() {
        return LMECode;
    }

    public void setLMECode(String LMECode) {
        this.LMECode = LMECode;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public String getResponseId() {
        return ResponseId;
    }

    public void setResponseId(String responseId) {
        ResponseId = responseId;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }
}
