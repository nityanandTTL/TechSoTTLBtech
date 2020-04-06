package com.thyrocare.NewScreenDesigns.Models.RequestModels;

public class NotificationMappingRequestModel {

    private String Client_Id;
    private String Token;
    private String AppName;
    private String EnterBy;
    private String Topic;

    public String getClient_Id() {
        return Client_Id;
    }

    public void setClient_Id(String client_Id) {
        Client_Id = client_Id;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getAppName() {
        return AppName;
    }

    public void setAppName(String appName) {
        AppName = appName;
    }

    public String getEnterBy() {
        return EnterBy;
    }

    public void setEnterBy(String enterBy) {
        EnterBy = enterBy;
    }

    public String getTopic() {
        return Topic;
    }

    public void setTopic(String topic) {
        Topic = topic;
    }
}
