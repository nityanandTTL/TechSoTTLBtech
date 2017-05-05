package com.dhb.models.data;

import java.util.ArrayList;

/**
 * Created by vendor1 on 5/4/2017.
 */

public class TestTypeWiseTestRateMasterModelsList {
    String testType;
    ArrayList<TestRateMasterModel> testRateMasterModels;

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public ArrayList<TestRateMasterModel> getTestRateMasterModels() {
        return testRateMasterModels;
    }

    public void setTestRateMasterModels(ArrayList<TestRateMasterModel> testRateMasterModels) {
        this.testRateMasterModels = testRateMasterModels;
    }
}
