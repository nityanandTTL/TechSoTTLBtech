package com.thyrocare.models.data;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 4/20/2017.
 */

public class SampleDropDetailsbyTSPLMEDetailsModel implements Parcelable{

    private int SampleDropId;
    private int LMEUserId;
    private int TSPOLCId;
    private String SourceCode;
    private int SampleCount;
    private int Batch;
    private int Status;
    private String EntryDate;
    private String LastUpdated;
    private String StartedAt;
    private String CompletedAt;
    private String Name;
    private String Address;
    private String Pincode;
    private String ArrivedAt;

    protected SampleDropDetailsbyTSPLMEDetailsModel(Parcel in) {
        SampleDropId = in.readInt();
        LMEUserId = in.readInt();
        TSPOLCId = in.readInt();
        SourceCode = in.readString();
        SampleCount = in.readInt();
        Batch = in.readInt();
        Status = in.readInt();
        EntryDate = in.readString();
        LastUpdated = in.readString();
        StartedAt = in.readString();
        CompletedAt = in.readString();
        Name = in.readString();
        Address = in.readString();
        Pincode = in.readString();
        ArrivedAt = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(SampleDropId);
        dest.writeInt(LMEUserId);
        dest.writeInt(TSPOLCId);
        dest.writeString(SourceCode);
        dest.writeInt(SampleCount);
        dest.writeInt(Batch);
        dest.writeInt(Status);
        dest.writeString(EntryDate);
        dest.writeString(LastUpdated);
        dest.writeString(StartedAt);
        dest.writeString(CompletedAt);
        dest.writeString(Name);
        dest.writeString(Address);
        dest.writeString(Pincode);
        dest.writeString(ArrivedAt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SampleDropDetailsbyTSPLMEDetailsModel> CREATOR = new Creator<SampleDropDetailsbyTSPLMEDetailsModel>() {
        @Override
        public SampleDropDetailsbyTSPLMEDetailsModel createFromParcel(Parcel in) {
            return new SampleDropDetailsbyTSPLMEDetailsModel(in);
        }

        @Override
        public SampleDropDetailsbyTSPLMEDetailsModel[] newArray(int size) {
            return new SampleDropDetailsbyTSPLMEDetailsModel[size];
        }
    };

    public int getSampleDropId() {
        return SampleDropId;
    }

    public void setSampleDropId(int sampleDropId) {
        SampleDropId = sampleDropId;
    }

    public int getLMEUserId() {
        return LMEUserId;
    }

    public void setLMEUserId(int LMEUserId) {
        this.LMEUserId = LMEUserId;
    }

    public int getTSPOLCId() {
        return TSPOLCId;
    }

    public void setTSPOLCId(int TSPOLCId) {
        this.TSPOLCId = TSPOLCId;
    }

    public String getSourceCode() {
        return SourceCode;
    }

    public void setSourceCode(String sourceCode) {
        SourceCode = sourceCode;
    }

    public int getSampleCount() {
        return SampleCount;
    }

    public void setSampleCount(int sampleCount) {
        SampleCount = sampleCount;
    }

    public int getBatch() {
        return Batch;
    }

    public void setBatch(int batch) {
        Batch = batch;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getEntryDate() {
        return EntryDate;
    }

    public void setEntryDate(String entryDate) {
        EntryDate = entryDate;
    }

    public String getLastUpdated() {
        return LastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        LastUpdated = lastUpdated;
    }

    public String getStartedAt() {
        return StartedAt;
    }

    public void setStartedAt(String startedAt) {
        StartedAt = startedAt;
    }

    public String getCompletedAt() {
        return CompletedAt;
    }

    public void setCompletedAt(String completedAt) {
        CompletedAt = completedAt;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }

    public String getArrivedAt() {
        return ArrivedAt;
    }

    public void setArrivedAt(String arrivedAt) {
        ArrivedAt = arrivedAt;
    }
}
