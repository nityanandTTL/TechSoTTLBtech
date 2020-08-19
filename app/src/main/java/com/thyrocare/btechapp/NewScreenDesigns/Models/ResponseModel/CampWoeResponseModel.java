package com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel;

import java.io.Serializable;

public class CampWoeResponseModel implements Serializable {

    String LeadID;
    String RES_ID;
    String barcode_id;
    String barcode_patient_id;
    String message;
    String status;

    public String getLeadID() {
        return LeadID;
    }

    public void setLeadID(String leadID) {
        LeadID = leadID;
    }

    public String getRES_ID() {
        return RES_ID;
    }

    public void setRES_ID(String RES_ID) {
        this.RES_ID = RES_ID;
    }

    public String getBarcode_id() {
        return barcode_id;
    }

    public void setBarcode_id(String barcode_id) {
        this.barcode_id = barcode_id;
    }

    public String getBarcode_patient_id() {
        return barcode_patient_id;
    }

    public void setBarcode_patient_id(String barcode_patient_id) {
        this.barcode_patient_id = barcode_patient_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
