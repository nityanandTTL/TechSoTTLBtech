package com.dhb.models.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ArrayRes;

import java.util.ArrayList;

/**
 * Created by Orion on 4/19/2017.
 */

public class BeneficiaryDetailsModel extends BaseModel implements Parcelable {
    private int benId;
    private String OrderNo;
    private String Name;
    private int Age;
    private String aadhar;
    private String Gender;
    private String tests;
    private String testsCode;
    private String Fasting;
    private String Venepuncture;
    private ArrayList<BeneficiaryTestDetailsModel> testSampleType;
    private ArrayList<BeneficiaryBarcodeDetailsModel> barcodedtl;
    private ArrayList<BeneficiarySampleTypeDetailsModel> sampleType;
    private ArrayList<BeneficiaryTestWiseClinicalHistoryModel> clHistory;
    private ArrayList<BeneficiaryLabAlertsModel> labAlert;
    private String ProjId;
    private String remarks;

    private boolean isTestEdit;
    private boolean isAddBen;

    public BeneficiaryDetailsModel() {
        super();
        Age = 1;
    }

    protected BeneficiaryDetailsModel(Parcel in) {
        super(in);
        benId = in.readInt();
        OrderNo = in.readString();
        Name = in.readString();
        Age = in.readInt();
        aadhar = in.readString();
        Gender = in.readString();
        tests = in.readString();
        testsCode = in.readString();
        Fasting = in.readString();
        Venepuncture = in.readString();
        testSampleType = in.createTypedArrayList(BeneficiaryTestDetailsModel.CREATOR);
        barcodedtl = in.createTypedArrayList(BeneficiaryBarcodeDetailsModel.CREATOR);
        sampleType = in.createTypedArrayList(BeneficiarySampleTypeDetailsModel.CREATOR);
        clHistory = in.createTypedArrayList(BeneficiaryTestWiseClinicalHistoryModel.CREATOR);
        labAlert = in.createTypedArrayList(BeneficiaryLabAlertsModel.CREATOR);
        ProjId = in.readString();
        remarks = in.readString();
        isTestEdit = in.readByte() != 0;
        isAddBen = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(benId);
        dest.writeString(OrderNo);
        dest.writeString(Name);
        dest.writeInt(Age);
        dest.writeString(aadhar);
        dest.writeString(Gender);
        dest.writeString(tests);
        dest.writeString(testsCode);
        dest.writeString(Fasting);
        dest.writeString(Venepuncture);
        dest.writeTypedList(testSampleType);
        dest.writeTypedList(barcodedtl);
        dest.writeTypedList(sampleType);
        dest.writeTypedList(clHistory);
        dest.writeTypedList(labAlert);
        dest.writeString(ProjId);
        dest.writeString(remarks);
        dest.writeByte((byte) (isTestEdit ? 1 : 0));
        dest.writeByte((byte) (isAddBen ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BeneficiaryDetailsModel> CREATOR = new Creator<BeneficiaryDetailsModel>() {
        @Override
        public BeneficiaryDetailsModel createFromParcel(Parcel in) {
            return new BeneficiaryDetailsModel(in);
        }

        @Override
        public BeneficiaryDetailsModel[] newArray(int size) {
            return new BeneficiaryDetailsModel[size];
        }
    };

    public String getProjId() {
        return ProjId;
    }

    public void setProjId(String projId) {
        ProjId = projId;
    }

    public int getBenId() {
        return benId;
    }

    public void setBenId(int benId) {
        this.benId = benId;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getTests() {
        return tests;
    }

    public void setTests(String tests) {
        this.tests = tests;
    }

    public String getFasting() {
        return Fasting;
    }

    public void setFasting(String fasting) {
        Fasting = fasting;
    }

    public String getVenepuncture() {
        return Venepuncture;
    }

    public void setVenepuncture(String venepuncture) {
        Venepuncture = venepuncture;
    }

    public ArrayList<BeneficiaryBarcodeDetailsModel> getBarcodedtl() {
        return barcodedtl;
    }

    public void setBarcodedtl(ArrayList<BeneficiaryBarcodeDetailsModel> barcodedtl) {
        this.barcodedtl = barcodedtl;
    }

    public ArrayList<BeneficiarySampleTypeDetailsModel> getSampleType() {
        return sampleType;
    }

    public void setSampleType(ArrayList<BeneficiarySampleTypeDetailsModel> sampleType) {
        this.sampleType = sampleType;
    }

    public String getTestsCode() {
        return testsCode;
    }

    public void setTestsCode(String testsCode) {
        this.testsCode = testsCode;
    }

    public ArrayList<BeneficiaryTestWiseClinicalHistoryModel> getClHistory() {
        return clHistory;
    }

    public void setClHistory(ArrayList<BeneficiaryTestWiseClinicalHistoryModel> clHistory) {
        this.clHistory = clHistory;
    }

    public ArrayList<BeneficiaryLabAlertsModel> getLabAlert() {
        return labAlert;
    }

    public void setLabAlert(ArrayList<BeneficiaryLabAlertsModel> labAlert) {
        this.labAlert = labAlert;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public ArrayList<BeneficiaryTestDetailsModel> getTestSampleType() {
        return testSampleType;
    }

    public void setTestSampleType(ArrayList<BeneficiaryTestDetailsModel> testSampleType) {
        this.testSampleType = testSampleType;
    }

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }

    public boolean isTestEdit() {
        return isTestEdit;
    }

    public void setTestEdit(boolean testEdit) {
        isTestEdit = testEdit;
    }

    public boolean isAddBen() {
        return isAddBen;
    }

    public void setAddBen(boolean addBen) {
        isAddBen = addBen;
    }
}
