package com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.thyrocare.btechapp.models.data.BeneficiaryBarcodeDetailsModel;
import com.thyrocare.btechapp.models.data.BeneficiaryLabAlertsModel;
import com.thyrocare.btechapp.models.data.BeneficiarySampleTypeDetailsModel;
import com.thyrocare.btechapp.models.data.BeneficiaryTestDetailsModel;
import com.thyrocare.btechapp.models.data.BeneficiaryTestWiseClinicalHistoryModel;

import java.util.ArrayList;

public class BeneficaryWiseScanbarcodeModel implements Parcelable {

    public static final Creator<BeneficaryWiseScanbarcodeModel> CREATOR = new Creator<BeneficaryWiseScanbarcodeModel>() {
        @Override
        public BeneficaryWiseScanbarcodeModel createFromParcel(Parcel in) {
            return new BeneficaryWiseScanbarcodeModel(in);
        }

        @Override
        public BeneficaryWiseScanbarcodeModel[] newArray(int size) {
            return new BeneficaryWiseScanbarcodeModel[size];
        }
    };
    private int benId;
    private String OrderNo;
    private String Name;
    private String BtechName;
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
    private String LeadId;
    private boolean isTestEdit;
    private boolean isAddBen;
    private boolean isTRF;

    protected BeneficaryWiseScanbarcodeModel(Parcel in) {
        benId = in.readInt();
        OrderNo = in.readString();
        Name = in.readString();
        BtechName = in.readString();
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
        LeadId = in.readString();
        isTestEdit = in.readByte() != 0;
        isAddBen = in.readByte() != 0;
        isTRF = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(benId);
        dest.writeString(OrderNo);
        dest.writeString(Name);
        dest.writeString(BtechName);
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
        dest.writeString(LeadId);
        dest.writeByte((byte) (isTestEdit ? 1 : 0));
        dest.writeByte((byte) (isAddBen ? 1 : 0));
        dest.writeByte((byte) (isTRF ? 1 : 0));
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

    public String getBtechName() {
        return BtechName;
    }

    public void setBtechName(String btechName) {
        BtechName = btechName;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
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

    public String getTestsCode() {
        return testsCode;
    }

    public void setTestsCode(String testsCode) {
        this.testsCode = testsCode;
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

    public ArrayList<BeneficiaryTestDetailsModel> getTestSampleType() {
        return testSampleType;
    }

    public void setTestSampleType(ArrayList<BeneficiaryTestDetailsModel> testSampleType) {
        this.testSampleType = testSampleType;
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

    public String getProjId() {
        return ProjId;
    }

    public void setProjId(String projId) {
        ProjId = projId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getLeadId() {
        return LeadId;
    }

    public void setLeadId(String leadId) {
        LeadId = leadId;
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

    public boolean isTRF() {
        return isTRF;
    }

    public void setTRF(boolean TRF) {
        isTRF = TRF;
    }
}
