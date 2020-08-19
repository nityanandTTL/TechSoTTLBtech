package com.thyrocare.btechapp.models.api.response;

import java.util.ArrayList;

public class MainMaterialModel {
    String Code,Response,ResponseId;
    ArrayList<MaterialDetailsModel2> MaterialDetails;

    public MainMaterialModel() {
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public String getResponseId() {
        return ResponseId;
    }

    public void setResponseId(String responseId) {
        ResponseId = responseId;
    }

    public ArrayList<MaterialDetailsModel2> getMaterialDetails() {
        return MaterialDetails;
    }

    public void setMaterialDetails(ArrayList<MaterialDetailsModel2> materialDetails) {
        MaterialDetails = materialDetails;
    }
}
