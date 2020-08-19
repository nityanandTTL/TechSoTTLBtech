package com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel;

import java.io.Serializable;
import java.util.ArrayList;

public class CampWisePatientDetailResponseModel implements Serializable {

    String response;
    String responseID;
    ArrayList<CampPatientSearchDetailResponseModel.PatientDetails> Patient;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getResponseID() {
        return responseID;
    }

    public void setResponseID(String responseID) {
        this.responseID = responseID;
    }

    public ArrayList<CampPatientSearchDetailResponseModel.PatientDetails> getPatient() {
        return Patient;
    }

    public void setPatient(ArrayList<CampPatientSearchDetailResponseModel.PatientDetails> patient) {
        Patient = patient;
    }
}
