package com.dhb.models.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Pratik Ambhore on 4/19/2017.
 */

public class BeneficiaryDetailsModel extends BaseModel implements Parcelable{
    private int benId;
    private String OrderNo;
    private String Name;
    private int Age;
    private String Gender;
    private String tests;
    private String testsCode;
    private String Fasting;
    private byte[] Venepuncture;
    private ArrayList<BarcodeDetailsModel> barcodedtl;
    private ArrayList<BeneficiarySampleTypeDetailsModel> sampleType;
    private ArrayList<TestRateMasterModel> testsList;
    private ArrayList<TestWiseBeneficiaryClinicalHistoryModel> clHistory;
    private ArrayList<BeneficiaryLabAlertsModel> labAlert;

    public BeneficiaryDetailsModel() {
        super();
    }

    protected BeneficiaryDetailsModel(Parcel in) {
        super(in);
        benId = in.readInt();
        OrderNo = in.readString();
        Name = in.readString();
        Age = in.readInt();
        Gender = in.readString();
        tests = in.readString();
        testsCode = in.readString();
        Fasting = in.readString();
        Venepuncture = in.createByteArray();
        barcodedtl = in.createTypedArrayList(BarcodeDetailsModel.CREATOR);
        sampleType = in.createTypedArrayList(BeneficiarySampleTypeDetailsModel.CREATOR);
        testsList = in.createTypedArrayList(TestRateMasterModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(benId);
        dest.writeString(OrderNo);
        dest.writeString(Name);
        dest.writeInt(Age);
        dest.writeString(Gender);
        dest.writeString(tests);
        dest.writeString(testsCode);
        dest.writeString(Fasting);
        dest.writeByteArray(Venepuncture);
        dest.writeTypedList(barcodedtl);
        dest.writeTypedList(sampleType);
        dest.writeTypedList(testsList);
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

    public byte[] getVenepuncture() {
        return Venepuncture;
    }

    public void setVenepuncture(byte[] venepuncture) {
        Venepuncture = venepuncture;
    }

    public ArrayList<BarcodeDetailsModel> getBarcodedtl() {
        return barcodedtl;
    }

    public void setBarcodedtl(ArrayList<BarcodeDetailsModel> barcodedtl) {
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

    public ArrayList<TestRateMasterModel> getTestsList() {
        return testsList;
    }

    public void setTestsList(ArrayList<TestRateMasterModel> testsList) {
        this.testsList = testsList;
    }
    public ArrayList<TestWiseBeneficiaryClinicalHistoryModel> getClHistory() {
        return clHistory;
    }

    public void setClHistory(ArrayList<TestWiseBeneficiaryClinicalHistoryModel> clHistory) {
        this.clHistory = clHistory;
    }

    public ArrayList<BeneficiaryLabAlertsModel> getLabAlert() {
        return labAlert;
    }

    public void setLabAlert(ArrayList<BeneficiaryLabAlertsModel> labAlert) {
        this.labAlert = labAlert;
    }
}
