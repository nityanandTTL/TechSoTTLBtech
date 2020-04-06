package com.thyrocare.NewScreenDesigns.Models.ResponseModel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Orion on 6/20/2017.*/



public class BeneficiaryTestDetailsModel implements Parcelable {
    private String Tests;
    private String Fasting;
    private String ProjId;
    private String TestType;
    private ArrayList<ChildTestsModel> chldtests;
    private ArrayList<TestSampleTypeModel> sampleType;
    private ArrayList<TestClinicalHistoryModel> tstClinicalHistory;

    public BeneficiaryTestDetailsModel() {
    }

    protected BeneficiaryTestDetailsModel(Parcel in) {
        Tests = in.readString();
        Fasting = in.readString();
        ProjId = in.readString();
        TestType = in.readString();
        chldtests = in.createTypedArrayList(ChildTestsModel.CREATOR);
        sampleType = in.createTypedArrayList(TestSampleTypeModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Tests);
        dest.writeString(Fasting);
        dest.writeString(ProjId);
        dest.writeString(TestType);
        dest.writeTypedList(chldtests);
        dest.writeTypedList(sampleType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BeneficiaryTestDetailsModel> CREATOR = new Creator<BeneficiaryTestDetailsModel>() {
        @Override
        public BeneficiaryTestDetailsModel createFromParcel(Parcel in) {
            return new BeneficiaryTestDetailsModel(in);
        }

        @Override
        public BeneficiaryTestDetailsModel[] newArray(int size) {
            return new BeneficiaryTestDetailsModel[size];
        }
    };

    public String getTests() {
        return Tests;
    }

    public void setTests(String tests) {
        Tests = tests;
    }

    public String getFasting() {
        return Fasting;
    }

    public void setFasting(String fasting) {
        Fasting = fasting;
    }

    public ArrayList<ChildTestsModel> getChldtests() {
        return chldtests;
    }

    public void setChldtests(ArrayList<ChildTestsModel> chldtests) {
        this.chldtests = chldtests;
    }

    public ArrayList<TestSampleTypeModel> getSampleType() {
        return sampleType;
    }

    public void setSampleType(ArrayList<TestSampleTypeModel> sampleType) {
        this.sampleType = sampleType;
    }

    public String getProjId() {
        return ProjId;
    }

    public void setProjId(String projId) {
        ProjId = projId;
    }

    public ArrayList<TestClinicalHistoryModel> getTstClinicalHistory() {
        return tstClinicalHistory;
    }

    public void setTstClinicalHistory(ArrayList<TestClinicalHistoryModel> tstClinicalHistory) {
        this.tstClinicalHistory = tstClinicalHistory;
    }

    public String getTestType() {
        return TestType;
    }

    public void setTestType(String testType) {
        TestType = testType;
    }
}
