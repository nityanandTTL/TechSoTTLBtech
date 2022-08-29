package com.thyrocare.btechapp.models.data;

import java.util.ArrayList;

/**
 * Created by Orion on 5/4/2017.
 */

public class TestTypeWiseTestRateMasterModelsList {
    String testType;
    ArrayList<TestRateMasterModel> testRateMasterModels;

    public TestTypeWiseTestRateMasterModelsList() {
    }

    public TestTypeWiseTestRateMasterModelsList(String testType, ArrayList<TestRateMasterModel> testRateMasterModels) {
        this.testType = testType;
        this.testRateMasterModels = testRateMasterModels;
    }

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
