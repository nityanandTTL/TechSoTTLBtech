package com.thyrocare.btechapp.models.api.response;

import java.io.Serializable;

public class NewCommonResponseModel implements Serializable {

    String id;
    String respId;
    String response;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRespId() {
        return respId;
    }

    public void setRespId(String respId) {
        this.respId = respId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
