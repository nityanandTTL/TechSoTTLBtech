package com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel;

import androidx.annotation.NonNull;

import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.SubmitCampWoeRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;

public class FinalMainCampWisePatentDetailsModel implements Serializable {

    CampPatientSearchDetailResponseModel.Camp camp;
    CampPatientSearchDetailResponseModel.PatientDetails patientDetails;
    ArrayList<SubmitCampWoeRequestModel.Barcodelist> barcodelistArrayList;

    public CampPatientSearchDetailResponseModel.Camp getCamp() {
        return camp;
    }

    public void setCamp(CampPatientSearchDetailResponseModel.Camp camp) {
        this.camp = camp;
    }

    public CampPatientSearchDetailResponseModel.PatientDetails getPatientDetails() {
        return patientDetails;
    }

    public void setPatientDetails(CampPatientSearchDetailResponseModel.PatientDetails patientDetails) {
        this.patientDetails = patientDetails;
    }

    public ArrayList<SubmitCampWoeRequestModel.Barcodelist> getBarcodelistArrayList() {
        return barcodelistArrayList;
    }

    public void setBarcodelistArrayList(ArrayList<SubmitCampWoeRequestModel.Barcodelist> barcodelistArrayList) {
        this.barcodelistArrayList = barcodelistArrayList;
    }

    @NonNull
    @Override
    public String toString() {
        if (patientDetails!= null && !StringUtils.isNull(patientDetails.name) && patientDetails.name.equalsIgnoreCase("- Search Patient -")){
            return patientDetails.name;
        }else{
            return patientDetails.name + "("+patientDetails.gender+"/"+patientDetails.age+")\n"+patientDetails.UniqueId;
        }

    }
}
