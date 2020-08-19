package com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels;

import java.io.Serializable;

public class CampPatientDetailRequestModel implements Serializable {

    String btechId;

    public String getBtechId() {
        return btechId;
    }

    public void setBtechId(String btechId) {
        this.btechId = btechId;
    }
}
