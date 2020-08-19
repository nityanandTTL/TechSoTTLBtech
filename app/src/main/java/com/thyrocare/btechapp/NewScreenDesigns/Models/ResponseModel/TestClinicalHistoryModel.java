package com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel;

/**
 * Created by Orion on 5/5/2017.
 */
public class TestClinicalHistoryModel {
    private int ClinicalHtrId;
    private String ClinicalHistory;

    public int getClinicalHtrId() {
        return ClinicalHtrId;
    }

    public void setClinicalHtrId(int clinicalHtrId) {
        ClinicalHtrId = clinicalHtrId;
    }

    public String getClinicalHistory() {
        return ClinicalHistory;
    }

    public void setClinicalHistory(String clinicalHistory) {
        ClinicalHistory = clinicalHistory;
    }
}
