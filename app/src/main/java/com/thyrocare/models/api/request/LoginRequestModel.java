package com.thyrocare.models.api.request;

/**
 * Created by Orion on 4/20/2017.
 */

public class LoginRequestModel {
    String UserName;
    String Password;
    String grant_type;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }
}
