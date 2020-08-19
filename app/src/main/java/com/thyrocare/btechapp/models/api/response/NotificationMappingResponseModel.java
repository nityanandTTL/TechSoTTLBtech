package com.thyrocare.btechapp.models.api.response;


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

    public void setResponse(String Response) {
        this.Response = Response;
    }

    public String getResponseId() {
        return ResponseId;
    }

    public void setResponseId(String ResponseId) {
        this.ResponseId = ResponseId;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String Token) {
        this.Token = Token;
    }
}