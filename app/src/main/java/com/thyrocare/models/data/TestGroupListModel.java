package com.thyrocare.models.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by e5209 on 9/29/2017.
 */

public class TestGroupListModel implements Parcelable{
    private String groupName,testCount;
    private ArrayList<TestDetailsModel> testDetails;

    protected TestGroupListModel(Parcel in) {
        groupName = in.readString();
        testCount = in.readString();
        testDetails = in.createTypedArrayList(TestDetailsModel.CREATOR);
    }

    public TestGroupListModel() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(groupName);
        dest.writeString(testCount);
        dest.writeTypedList(testDetails);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TestGroupListModel> CREATOR = new Creator<TestGroupListModel>() {
        @Override
        public TestGroupListModel createFromParcel(Parcel in) {
            return new TestGroupListModel(in);
        }

        @Override
        public TestGroupListModel[] newArray(int size) {
            return new TestGroupListModel[size];
        }
    };

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getTestCount() {
        return testCount;
    }

    public void setTestCount(String testCount) {
        this.testCount = testCount;
    }

    public ArrayList<TestDetailsModel> getTestDetails() {
        return testDetails;
    }

    public void setTestDetails(ArrayList<TestDetailsModel> testDetails) {
        this.testDetails = testDetails;
    }
}
