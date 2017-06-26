package com.dhb.models.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Pratik Ambhore on 4/19/2017.
 */

public class TestRateMasterModel extends BaseModel implements Parcelable {
    private int TestId;
    private int BrandId;
    private String BrandName;
    private String TestCode;
    private String TestType;
    private String Fasting;
    private ArrayList<TestSampleTypeModel> sampltype;
    private int LastMealEat;
    private int Rate;
    private int Discount;
    private int Incentive;
    private String Description;
    private ArrayList<ChildTestsModel> chldtests;
    private ArrayList<TestSkillsModel> tstSkills;
    private ArrayList<TestClinicalHistoryModel> tstClinicalHistory;

    public TestRateMasterModel() {
        super();
        sampltype = new ArrayList<>();
        chldtests = new ArrayList<>();
        tstSkills = new ArrayList<>();
        tstClinicalHistory = new ArrayList<>();
    }

    protected TestRateMasterModel(Parcel in) {
        super(in);
        TestId = in.readInt();
        BrandId = in.readInt();
        BrandName = in.readString();
        TestCode = in.readString();
        TestType = in.readString();
        Fasting = in.readString();
        sampltype = in.createTypedArrayList(TestSampleTypeModel.CREATOR);
        LastMealEat = in.readInt();
        Rate = in.readInt();
        Discount = in.readInt();
        Incentive = in.readInt();
        Description = in.readString();
        chldtests = in.createTypedArrayList(ChildTestsModel.CREATOR);
        tstSkills = in.createTypedArrayList(TestSkillsModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(TestId);
        dest.writeInt(BrandId);
        dest.writeString(BrandName);
        dest.writeString(TestCode);
        dest.writeString(TestType);
        dest.writeString(Fasting);
        dest.writeTypedList(sampltype);
        dest.writeInt(LastMealEat);
        dest.writeInt(Rate);
        dest.writeInt(Discount);
        dest.writeInt(Incentive);
        dest.writeString(Description);
        dest.writeTypedList(chldtests);
        dest.writeTypedList(tstSkills);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TestRateMasterModel> CREATOR = new Creator<TestRateMasterModel>() {
        @Override
        public TestRateMasterModel createFromParcel(Parcel in) {
            return new TestRateMasterModel(in);
        }

        @Override
        public TestRateMasterModel[] newArray(int size) {
            return new TestRateMasterModel[size];
        }
    };

    public int getTestId() {
        return TestId;
    }

    public void setTestId(int testId) {
        TestId = testId;
    }

    public int getBrandId() {
        return BrandId;
    }

    public void setBrandId(int brandId) {
        BrandId = brandId;
    }

    public String getBrandName() {
        return BrandName;
    }

    public void setBrandName(String brandName) {
        BrandName = brandName;
    }

    public String getTestCode() {
        return TestCode;
    }

    public void setTestCode(String testCode) {
        TestCode = testCode;
    }

    public String getTestType() {
        return TestType;
    }

    public void setTestType(String testType) {
        TestType = testType;
    }

    public String getFasting() {
        return Fasting;
    }

    public void setFasting(String fasting) {
        Fasting = fasting;
    }

    public ArrayList<TestSampleTypeModel> getSampltype() {
        return sampltype;
    }

    public void setSampltype(ArrayList<TestSampleTypeModel> sampltype) {
        this.sampltype = sampltype;
    }

    public int getLastMealEat() {
        return LastMealEat;
    }

    public void setLastMealEat(int lastMealEat) {
        LastMealEat = lastMealEat;
    }

    public int getRate() {
        return Rate;
    }

    public void setRate(int rate) {
        Rate = rate;
    }

    public int getDiscount() {
        return Discount;
    }

    public void setDiscount(int discount) {
        Discount = discount;
    }

    public int getIncentive() {
        return Incentive;
    }

    public void setIncentive(int incentive) {
        Incentive = incentive;
    }

    public ArrayList<ChildTestsModel> getChldtests() {
        return chldtests;
    }

    public void setChldtests(ArrayList<ChildTestsModel> chldtests) {
        this.chldtests = chldtests;
    }

    public ArrayList<TestSkillsModel> getTstSkills() {
        return tstSkills;
    }

    public void setTstSkills(ArrayList<TestSkillsModel> tstSkills) {
        this.tstSkills = tstSkills;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public ArrayList<TestClinicalHistoryModel> getTstClinicalHistory() {
        return tstClinicalHistory;
    }

    public void setTstClinicalHistory(ArrayList<TestClinicalHistoryModel> tstClinicalHistory) {
        this.tstClinicalHistory = tstClinicalHistory;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TestRateMasterModel){
            if(((TestRateMasterModel) obj).getTestCode().equals(getTestCode())){
                return true;
            }
        }
        return false;
    }

    public boolean checkIfChildsContained(TestRateMasterModel tt){
        boolean isChilds = false;
        if(getChldtests()!=null && getChldtests().size()>0) {
            int ttChildSize = tt.getChldtests().size();
            int commonChildsSize = 0;
            for (ChildTestsModel tc : tt.getChldtests()) {
                for (ChildTestsModel tChild : getChldtests()) {
                    if (tc.getChildTestCode()!=null && tc.getChildTestCode().equalsIgnoreCase(tChild.getChildTestCode())) {
                        commonChildsSize++;
                    }
                }
            }
            if (commonChildsSize>0 && commonChildsSize == ttChildSize) {
                isChilds = true;
            }
        }
        return isChilds;
    }
}
