package com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels;

import java.io.Serializable;
import java.util.ArrayList;

public class SubmitB2BWoeRequestModel implements Serializable {

    Woe woe;
    ArrayList<Barcodelist> barcodelist;
    String woe_type;
    String api_key;

    public Woe getWoe() {
        return woe;
    }

    public void setWoe(Woe woe) {
        this.woe = woe;
    }

    public ArrayList<SubmitB2BWoeRequestModel.Barcodelist> getBarcodelist() {
        return barcodelist;
    }

    public void setBarcodelist(ArrayList<SubmitB2BWoeRequestModel.Barcodelist> barcodelist) {
        this.barcodelist = barcodelist;
    }

    public String getWoe_type() {
        return woe_type;
    }

    public void setWoe_type(String woe_type) {
        this.woe_type = woe_type;
    }

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public static class Woe {

        String ORDER_NO;
        String MAIN_SOURCE;
        String SUB_SOURCE_CODE;
        String TYPE;
        String REF_DR_ID;
        String REF_DR_NAME;
        String ALERT_MESSAGE;
        String CUSTOMER_ID;
        String PATIENT_NAME;
        String CONTACT_NO;
        String ADDRESS;
        String PINCODE;
        int AGE;
        String AGE_TYPE;
        String GENDER;
        String EMAIL_ID;
        String AADHAR_NO;
        String SPECIMEN_COLLECTION_TIME;
        String BRAND;
        int TOTAL_AMOUNT;
        String AMOUNT_COLLECTED;
        int AMOUNT_DUE;
        int DELIVERY_MODE;
        String STATUS;
        String REMARKS;
        int WO_STAGE;
        String LEAD_ID;
        int SR_NO;
        String SPECIMEN_SOURCE;
        String PRODUCT;
        String LAB_ID;
        String LAB_NAME;
        String LAB_ADDRESS;
        String BCT_ID;
        String CAMP_ID;
        String ENTERED_BY;
        String WO_MODE;
        String APP_ID;
        String WATER_SOURCE;
        String CONT_PERSON;
        String OS;
        String purpose;
        String ADDITIONAL3;

        public String getORDER_NO() {
            return ORDER_NO;
        }

        public void setORDER_NO(String ORDER_NO) {
            this.ORDER_NO = ORDER_NO;
        }

        public String getMAIN_SOURCE() {
            return MAIN_SOURCE;
        }

        public void setMAIN_SOURCE(String MAIN_SOURCE) {
            this.MAIN_SOURCE = MAIN_SOURCE;
        }

        public String getSUB_SOURCE_CODE() {
            return SUB_SOURCE_CODE;
        }

        public void setSUB_SOURCE_CODE(String SUB_SOURCE_CODE) {
            this.SUB_SOURCE_CODE = SUB_SOURCE_CODE;
        }

        public String getTYPE() {
            return TYPE;
        }

        public void setTYPE(String TYPE) {
            this.TYPE = TYPE;
        }

        public String getREF_DR_ID() {
            return REF_DR_ID;
        }

        public void setREF_DR_ID(String REF_DR_ID) {
            this.REF_DR_ID = REF_DR_ID;
        }

        public String getREF_DR_NAME() {
            return REF_DR_NAME;
        }

        public void setREF_DR_NAME(String REF_DR_NAME) {
            this.REF_DR_NAME = REF_DR_NAME;
        }

        public String getALERT_MESSAGE() {
            return ALERT_MESSAGE;
        }

        public void setALERT_MESSAGE(String ALERT_MESSAGE) {
            this.ALERT_MESSAGE = ALERT_MESSAGE;
        }

        public String getCUSTOMER_ID() {
            return CUSTOMER_ID;
        }

        public void setCUSTOMER_ID(String CUSTOMER_ID) {
            this.CUSTOMER_ID = CUSTOMER_ID;
        }

        public String getPATIENT_NAME() {
            return PATIENT_NAME;
        }

        public void setPATIENT_NAME(String PATIENT_NAME) {
            this.PATIENT_NAME = PATIENT_NAME;
        }

        public String getCONTACT_NO() {
            return CONTACT_NO;
        }

        public void setCONTACT_NO(String CONTACT_NO) {
            this.CONTACT_NO = CONTACT_NO;
        }

        public String getADDRESS() {
            return ADDRESS;
        }

        public void setADDRESS(String ADDRESS) {
            this.ADDRESS = ADDRESS;
        }

        public String getPINCODE() {
            return PINCODE;
        }

        public void setPINCODE(String PINCODE) {
            this.PINCODE = PINCODE;
        }

        public int getAGE() {
            return AGE;
        }

        public void setAGE(int AGE) {
            this.AGE = AGE;
        }

        public String getAGE_TYPE() {
            return AGE_TYPE;
        }

        public void setAGE_TYPE(String AGE_TYPE) {
            this.AGE_TYPE = AGE_TYPE;
        }

        public String getGENDER() {
            return GENDER;
        }

        public void setGENDER(String GENDER) {
            this.GENDER = GENDER;
        }

        public String getEMAIL_ID() {
            return EMAIL_ID;
        }

        public void setEMAIL_ID(String EMAIL_ID) {
            this.EMAIL_ID = EMAIL_ID;
        }

        public String getAADHAR_NO() {
            return AADHAR_NO;
        }

        public void setAADHAR_NO(String AADHAR_NO) {
            this.AADHAR_NO = AADHAR_NO;
        }

        public String getSPECIMEN_COLLECTION_TIME() {
            return SPECIMEN_COLLECTION_TIME;
        }

        public void setSPECIMEN_COLLECTION_TIME(String SPECIMEN_COLLECTION_TIME) {
            this.SPECIMEN_COLLECTION_TIME = SPECIMEN_COLLECTION_TIME;
        }

        public String getBRAND() {
            return BRAND;
        }

        public void setBRAND(String BRAND) {
            this.BRAND = BRAND;
        }

        public int getTOTAL_AMOUNT() {
            return TOTAL_AMOUNT;
        }

        public void setTOTAL_AMOUNT(int TOTAL_AMOUNT) {
            this.TOTAL_AMOUNT = TOTAL_AMOUNT;
        }

        public String getAMOUNT_COLLECTED() {
            return AMOUNT_COLLECTED;
        }

        public void setAMOUNT_COLLECTED(String AMOUNT_COLLECTED) {
            this.AMOUNT_COLLECTED = AMOUNT_COLLECTED;
        }

        public int getAMOUNT_DUE() {
            return AMOUNT_DUE;
        }

        public void setAMOUNT_DUE(int AMOUNT_DUE) {
            this.AMOUNT_DUE = AMOUNT_DUE;
        }

        public int getDELIVERY_MODE() {
            return DELIVERY_MODE;
        }

        public void setDELIVERY_MODE(int DELIVERY_MODE) {
            this.DELIVERY_MODE = DELIVERY_MODE;
        }

        public String getSTATUS() {
            return STATUS;
        }

        public void setSTATUS(String STATUS) {
            this.STATUS = STATUS;
        }

        public String getREMARKS() {
            return REMARKS;
        }

        public void setREMARKS(String REMARKS) {
            this.REMARKS = REMARKS;
        }

        public int getWO_STAGE() {
            return WO_STAGE;
        }

        public void setWO_STAGE(int WO_STAGE) {
            this.WO_STAGE = WO_STAGE;
        }

        public String getLEAD_ID() {
            return LEAD_ID;
        }

        public void setLEAD_ID(String LEAD_ID) {
            this.LEAD_ID = LEAD_ID;
        }

        public int getSR_NO() {
            return SR_NO;
        }

        public void setSR_NO(int SR_NO) {
            this.SR_NO = SR_NO;
        }

        public String getSPECIMEN_SOURCE() {
            return SPECIMEN_SOURCE;
        }

        public void setSPECIMEN_SOURCE(String SPECIMEN_SOURCE) {
            this.SPECIMEN_SOURCE = SPECIMEN_SOURCE;
        }

        public String getPRODUCT() {
            return PRODUCT;
        }

        public void setPRODUCT(String PRODUCT) {
            this.PRODUCT = PRODUCT;
        }

        public String getLAB_ID() {
            return LAB_ID;
        }

        public void setLAB_ID(String LAB_ID) {
            this.LAB_ID = LAB_ID;
        }

        public String getLAB_NAME() {
            return LAB_NAME;
        }

        public void setLAB_NAME(String LAB_NAME) {
            this.LAB_NAME = LAB_NAME;
        }

        public String getLAB_ADDRESS() {
            return LAB_ADDRESS;
        }

        public void setLAB_ADDRESS(String LAB_ADDRESS) {
            this.LAB_ADDRESS = LAB_ADDRESS;
        }

        public String getBCT_ID() {
            return BCT_ID;
        }

        public void setBCT_ID(String BCT_ID) {
            this.BCT_ID = BCT_ID;
        }

        public String getCAMP_ID() {
            return CAMP_ID;
        }

        public void setCAMP_ID(String CAMP_ID) {
            this.CAMP_ID = CAMP_ID;
        }

        public String getENTERED_BY() {
            return ENTERED_BY;
        }

        public void setENTERED_BY(String ENTERED_BY) {
            this.ENTERED_BY = ENTERED_BY;
        }

        public String getWO_MODE() {
            return WO_MODE;
        }

        public void setWO_MODE(String WO_MODE) {
            this.WO_MODE = WO_MODE;
        }

        public String getAPP_ID() {
            return APP_ID;
        }

        public void setAPP_ID(String APP_ID) {
            this.APP_ID = APP_ID;
        }

        public String getWATER_SOURCE() {
            return WATER_SOURCE;
        }

        public void setWATER_SOURCE(String WATER_SOURCE) {
            this.WATER_SOURCE = WATER_SOURCE;
        }

        public String getCONT_PERSON() {
            return CONT_PERSON;
        }

        public void setCONT_PERSON(String CONT_PERSON) {
            this.CONT_PERSON = CONT_PERSON;
        }

        public String getOS() {
            return OS;
        }

        public void setOS(String OS) {
            this.OS = OS;
        }

        public String getPurpose() {
            return purpose;
        }

        public void setPurpose(String purpose) {
            this.purpose = purpose;
        }

        public String getADDITIONAL3() {
            return ADDITIONAL3;
        }

        public void setADDITIONAL3(String ADDITIONAL3) {
            this.ADDITIONAL3 = ADDITIONAL3;
        }
    }

    public static class Barcodelist {
        String SAMPLE_TYPE;
        String BARCODE;
        String TESTS;

        public String getSAMPLE_TYPE() {
            return SAMPLE_TYPE;
        }

        public void setSAMPLE_TYPE(String SAMPLE_TYPE) {
            this.SAMPLE_TYPE = SAMPLE_TYPE;
        }

        public String getBARCODE() {
            return BARCODE;
        }

        public void setBARCODE(String BARCODE) {
            this.BARCODE = BARCODE;
        }

        public String getTESTS() {
            return TESTS;
        }

        public void setTESTS(String TESTS) {
            this.TESTS = TESTS;
        }
    }

}
