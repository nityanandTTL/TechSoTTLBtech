package com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels;

import java.io.Serializable;

public class CampWisePatientDetailRequestModel implements Serializable {

    String CampId;
    String filtervalue;

    public String getCampId() {
        return CampId;
    }

    public void setCampId(String campId) {
        CampId = campId;
    }

    public String getFiltervalue() {
        return filtervalue;
    }

    public void setFiltervalue(String filtervalue) {
        this.filtervalue = filtervalue;
    }
}
