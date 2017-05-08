package com.dhb.models.data;

/**
 * Created by ISRO on 5/8/2017.
 */

public class TestWiseBeneficiaryClinicalHistoryModel {
    private int BenId;
    private int ClinicalHistoryId;
    private String Test;

    public int getBenId() {
        return BenId;
    }

    public void setBenId(int benId) {
        BenId = benId;
    }

    public int getClinicalHistoryId() {
        return ClinicalHistoryId;
    }

    public void setClinicalHistoryId(int clinicalHistoryId) {
        ClinicalHistoryId = clinicalHistoryId;
    }

    public String getTest() {
        return Test;
    }

    public void setTest(String test) {
        Test = test;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TestWiseBeneficiaryClinicalHistoryModel){
            if(((TestWiseBeneficiaryClinicalHistoryModel) obj).getClinicalHistoryId()== getClinicalHistoryId() && ((TestWiseBeneficiaryClinicalHistoryModel) obj).getTest().equals(getTest())){
                return true;
            }
        }
        return false;
    }
}
