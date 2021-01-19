package com.thyrocare.btechapp.models.api.response;

import java.io.Serializable;

public class LeadgenerationResponseModel implements Serializable {

    String respId;
    String response;

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
