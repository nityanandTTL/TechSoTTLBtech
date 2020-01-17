package com.thyrocare.models.api.request;

public class NotificationMappingModel {
    /**
     * Client_Id : 100000
     * Token : epQXUG1CXf4:APA91bGvhbl6LX7mxDaajSxpyk8Y4z5Cs7JrwFFQtv_MhodsSd3hmwvGPn00a3n7S6SokW88VOBjVYzayiOW6o7dbUYdDK55J35ikkRZ2KB8VihgbcxeIl1yCnjstfKfsA_oc35Njm51
     * AppName : LeggyC_AllDevices
     * EnterBy : 100000
     * Topic :
     */

    private String Client_Id;
    private String Token;
    private String AppName;

    private String EnterBy;
    private String Topic;

    public String getClient_Id() {
        return Client_Id;
    }

    public void setClient_Id(String Client_Id) {
        this.Client_Id = Client_Id;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String Token) {
        this.Token = Token;
    }

    public String getAppName() {
        return AppName;
    }

    public void setAppName(String AppName) {
        this.AppName = AppName;
    }

    public String getEnterBy() {
        return EnterBy;
    }

    public void setEnterBy(String EnterBy) {
        this.EnterBy = EnterBy;
    }

    public String getTopic() {
        return Topic;
    }

    public void setTopic(String Topic) {
        this.Topic = Topic;
    }
}