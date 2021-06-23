package com.thyrocare.btechapp.models.api.request;

public class GetTestCodeRequestModel {


    /**
     * Test : COVID-19
     * TSP : COV01
     */

    private String Test;
    private String TSP;

    public String getTest() {
        return Test;
    }

    public void setTest(String Test) {
        this.Test = Test;
    }

    public String getTSP() {
        return TSP;
    }

    public void setTSP(String TSP) {
        this.TSP = TSP;
    }
}
