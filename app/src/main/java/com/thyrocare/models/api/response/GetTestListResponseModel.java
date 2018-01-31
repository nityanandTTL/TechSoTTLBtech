package com.thyrocare.models.api.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.thyrocare.models.data.TestGroupListModel;

import java.util.ArrayList;

/**
 * Created by e5209 on 9/29/2017.
 */

public class GetTestListResponseModel implements Parcelable{
    private String leadId,status;
    private ArrayList<TestGroupListModel> testGroupList;

    public GetTestListResponseModel() {
    }

    protected GetTestListResponseModel(Parcel in) {
        leadId = in.readString();
        status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(leadId);
        dest.writeString(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GetTestListResponseModel> CREATOR = new Creator<GetTestListResponseModel>() {
        @Override
        public GetTestListResponseModel createFromParcel(Parcel in) {
            return new GetTestListResponseModel(in);
        }

        @Override
        public GetTestListResponseModel[] newArray(int size) {
            return new GetTestListResponseModel[size];
        }
    };

    public String getLeadId() {
        return leadId;
    }

    public void setLeadId(String leadId) {
        this.leadId = leadId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<TestGroupListModel> getTestGroupList() {
        return testGroupList;
    }

    public void setTestGroupList(ArrayList<TestGroupListModel> testGroupList) {
        this.testGroupList = testGroupList;
    }
}
