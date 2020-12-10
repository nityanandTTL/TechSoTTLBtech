package com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel;

import androidx.annotation.NonNull;

import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.SubmitB2BWoeRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;

public class CampPatientSearchDetailResponseModel implements Serializable {

    String  response;
    String  responseID;
    ArrayList<Camp> camp;

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

    public ArrayList<Camp> getCamp() {
        return camp;
    }

    public void setCamp(ArrayList<Camp> camp) {
        this.camp = camp;
    }

    public static class Camp {

        String campID;
        String campName;
        String sourceCode;
        String stech;
        String numberOfCollection;
        String scfType;
        String icfType;
        String icfName;
        String campAddress;
        String campPincode;
        String refHospital;
        String refDrname;
        String refDrEmail;
        String refDrNumber;
        String spocName;
        String spocContactNumber;
        String amountCollected;
        ArrayList<PatientDetails> patientDetails;
        ArrayList<SubmitB2BWoeRequestModel.Barcodelist> barcodelist;

        public String getCampID() {
            return campID;
        }

        public void setCampID(String campID) {
            this.campID = campID;
        }

        public String getCampName() {
            return campName;
        }

        public void setCampName(String campName) {
            this.campName = campName;
        }

        public String getSourceCode() {
            return sourceCode;
        }

        public void setSourceCode(String sourceCode) {
            this.sourceCode = sourceCode;
        }

        public String getStech() {
            return stech;
        }

        public void setStech(String stech) {
            this.stech = stech;
        }

        public String getNumberOfCollection() {
            return numberOfCollection;
        }

        public void setNumberOfCollection(String numberOfCollection) {
            this.numberOfCollection = numberOfCollection;
        }

        public String getScfType() {
            return scfType;
        }

        public void setScfType(String scfType) {
            this.scfType = scfType;
        }

        public String getIcfType() {
            return icfType;
        }

        public void setIcfType(String icfType) {
            this.icfType = icfType;
        }

        public String getIcfName() {
            return icfName;
        }

        public void setIcfName(String icfName) {
            this.icfName = icfName;
        }

        public String getCampAddress() {
            return campAddress;
        }

        public void setCampAddress(String campAddress) {
            this.campAddress = campAddress;
        }

        public String getCampPincode() {
            return campPincode;
        }

        public void setCampPincode(String campPincode) {
            this.campPincode = campPincode;
        }

        public String getRefHospital() {
            return refHospital;
        }

        public void setRefHospital(String refHospital) {
            this.refHospital = refHospital;
        }

        public String getRefDrname() {
            return refDrname;
        }

        public void setRefDrname(String refDrname) {
            this.refDrname = refDrname;
        }

        public String getRefDrEmail() {
            return refDrEmail;
        }

        public void setRefDrEmail(String refDrEmail) {
            this.refDrEmail = refDrEmail;
        }

        public String getRefDrNumber() {
            return refDrNumber;
        }

        public void setRefDrNumber(String refDrNumber) {
            this.refDrNumber = refDrNumber;
        }

        public String getSpocName() {
            return spocName;
        }

        public void setSpocName(String spocName) {
            this.spocName = spocName;
        }

        public String getSpocContactNumber() {
            return spocContactNumber;
        }

        public void setSpocContactNumber(String spocContactNumber) {
            this.spocContactNumber = spocContactNumber;
        }

        public String getAmountCollected() {
            return amountCollected;
        }

        public void setAmountCollected(String amountCollected) {
            this.amountCollected = amountCollected;
        }


        public ArrayList<PatientDetails> getPatientDetails() {
            return patientDetails;
        }

        public void setPatientDetails(ArrayList<PatientDetails> patientDetails) {
            this.patientDetails = patientDetails;
        }

        public ArrayList<SubmitB2BWoeRequestModel.Barcodelist> getBarcodelist() {
            return barcodelist;
        }

        public void setBarcodelist(ArrayList<SubmitB2BWoeRequestModel.Barcodelist> barcodelist) {
            this.barcodelist = barcodelist;
        }

        @NonNull
        @Override
        public String toString() {
            if (!StringUtils.isNull(campName) && campName.equalsIgnoreCase(" - Select Camp - ")){
                return campName;
            }else{
                if (!StringUtils.isNull(campName)){
                    return campName + " ("+campID+")";
                }else{
                    return campID;
                }

            }

        }
    }

    public static class PatientDetails {
        String sourceCode;
        String name;
        String age;
        String ageType;
        String gender;
        String contactNo;
        String pincode;
        String UniqueId;
        String address;
        String UID;

        public String getSourceCode() {
            return sourceCode;
        }

        public void setSourceCode(String sourceCode) {
            this.sourceCode = sourceCode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getAgeType() {
            return ageType;
        }

        public void setAgeType(String ageType) {
            this.ageType = ageType;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getContactNo() {
            return contactNo;
        }

        public void setContactNo(String contactNo) {
            this.contactNo = contactNo;
        }

        public String getPincode() {
            return pincode;
        }

        public void setPincode(String pincode) {
            this.pincode = pincode;
        }

        public String getUniqueId() {
            return UniqueId;
        }

        public void setUniqueId(String uniqueId) {
            UniqueId = uniqueId;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getUID() {
            return UID;
        }

        public void setUID(String UID) {
            this.UID = UID;
        }
    }

}
