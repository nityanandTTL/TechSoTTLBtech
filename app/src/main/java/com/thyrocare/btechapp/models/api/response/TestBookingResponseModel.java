package com.thyrocare.btechapp.models.api.response;

import android.os.Parcel;
import android.os.Parcelable;

public class TestBookingResponseModel implements Parcelable {
    public static final Creator<TestBookingResponseModel> CREATOR = new Creator<TestBookingResponseModel>() {
        @Override
        public TestBookingResponseModel createFromParcel(Parcel in) {
            return new TestBookingResponseModel(in);
        }

        @Override
        public TestBookingResponseModel[] newArray(int size) {
            return new TestBookingResponseModel[size];
        }
    };
    private String ADDRESS, BOOKED_BY, EMAIL, FASTING, MOBILE, MODE, ORDER_NO, PAY_TYPE, PRODUCT, REF_ORDERID, REPORT_HARD_COPY, RESPONSE, RES_ID, SERVICE_TYPE, STATUS;
    private OrderResponseModel ORDERRESPONSE;
    private int CUSTOMER_RATE;

    protected TestBookingResponseModel(Parcel in) {
        ADDRESS = in.readString();
        BOOKED_BY = in.readString();
        EMAIL = in.readString();
        FASTING = in.readString();
        MOBILE = in.readString();
        MODE = in.readString();
        ORDER_NO = in.readString();
        PAY_TYPE = in.readString();
        PRODUCT = in.readString();
        REF_ORDERID = in.readString();
        REPORT_HARD_COPY = in.readString();
        RESPONSE = in.readString();
        RES_ID = in.readString();
        SERVICE_TYPE = in.readString();
        STATUS = in.readString();
        CUSTOMER_RATE = in.readInt();
    }

    public TestBookingResponseModel() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ADDRESS);
        dest.writeString(BOOKED_BY);
        dest.writeString(EMAIL);
        dest.writeString(FASTING);
        dest.writeString(MOBILE);
        dest.writeString(MODE);
        dest.writeString(ORDER_NO);
        dest.writeString(PAY_TYPE);
        dest.writeString(PRODUCT);
        dest.writeString(REF_ORDERID);
        dest.writeString(REPORT_HARD_COPY);
        dest.writeString(RESPONSE);
        dest.writeString(RES_ID);
        dest.writeString(SERVICE_TYPE);
        dest.writeString(STATUS);
        dest.writeInt(CUSTOMER_RATE);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getBOOKED_BY() {
        return BOOKED_BY;
    }

    public void setBOOKED_BY(String BOOKED_BY) {
        this.BOOKED_BY = BOOKED_BY;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getFASTING() {
        return FASTING;
    }

    public void setFASTING(String FASTING) {
        this.FASTING = FASTING;
    }

    public String getMOBILE() {
        return MOBILE;
    }

    public void setMOBILE(String MOBILE) {
        this.MOBILE = MOBILE;
    }

    public String getMODE() {
        return MODE;
    }

    public void setMODE(String MODE) {
        this.MODE = MODE;
    }

    public String getORDER_NO() {
        return ORDER_NO;
    }

    public void setORDER_NO(String ORDER_NO) {
        this.ORDER_NO = ORDER_NO;
    }

    public String getPAY_TYPE() {
        return PAY_TYPE;
    }

    public void setPAY_TYPE(String PAY_TYPE) {
        this.PAY_TYPE = PAY_TYPE;
    }

    public String getPRODUCT() {
        return PRODUCT;
    }

    public void setPRODUCT(String PRODUCT) {
        this.PRODUCT = PRODUCT;
    }

    public String getREF_ORDERID() {
        return REF_ORDERID;
    }

    public void setREF_ORDERID(String REF_ORDERID) {
        this.REF_ORDERID = REF_ORDERID;
    }

    public String getREPORT_HARD_COPY() {
        return REPORT_HARD_COPY;
    }

    public void setREPORT_HARD_COPY(String REPORT_HARD_COPY) {
        this.REPORT_HARD_COPY = REPORT_HARD_COPY;
    }

    public String getRESPONSE() {
        return RESPONSE;
    }

    public void setRESPONSE(String RESPONSE) {
        this.RESPONSE = RESPONSE;
    }

    public String getRES_ID() {
        return RES_ID;
    }

    public void setRES_ID(String RES_ID) {
        this.RES_ID = RES_ID;
    }

    public String getSERVICE_TYPE() {
        return SERVICE_TYPE;
    }

    public void setSERVICE_TYPE(String SERVICE_TYPE) {
        this.SERVICE_TYPE = SERVICE_TYPE;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public OrderResponseModel getORDERRESPONSE() {
        return ORDERRESPONSE;
    }

    public void setORDERRESPONSE(OrderResponseModel ORDERRESPONSE) {
        this.ORDERRESPONSE = ORDERRESPONSE;
    }

    public int getCUSTOMER_RATE() {
        return CUSTOMER_RATE;
    }

    public void setCUSTOMER_RATE(int CUSTOMER_RATE) {
        this.CUSTOMER_RATE = CUSTOMER_RATE;
    }
}
