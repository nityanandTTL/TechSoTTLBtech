package com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel;

import java.io.Serializable;
import java.util.ArrayList;

public class CampModuleMISResponseModel implements Serializable {


    String response;
    String responseID;
    ArrayList<Output> Output;

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

    public ArrayList<CampModuleMISResponseModel.Output> getOutput() {
        return Output;
    }

    public void setOutput(ArrayList<CampModuleMISResponseModel.Output> output) {
        Output = output;
    }

    public static class Output {
        String patientID;
        String name;
        String sct;
        String vialImage;
        String barcode;
        String sourceCode;
        String UniqueId;
        String CampID;

        public String getPatientID() {
            return patientID;
        }

        public void setPatientID(String patientID) {
            this.patientID = patientID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSct() {
            return sct;
        }

        public void setSct(String sct) {
            this.sct = sct;
        }

        public String getVialImage() {
            return vialImage;
        }

        public void setVialImage(String vialImage) {
            this.vialImage = vialImage;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public String getSourceCode() {
            return sourceCode;
        }

        public void setSourceCode(String sourceCode) {
            this.sourceCode = sourceCode;
        }

        public String getUniqueId() {
            return UniqueId;
        }

        public void setUniqueId(String uniqueId) {
            UniqueId = uniqueId;
        }

        public String getCampID() {
            return CampID;
        }

        public void setCampID(String campID) {
            CampID = campID;
        }
    }


}
