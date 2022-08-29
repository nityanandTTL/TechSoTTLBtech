package com.thyrocare.btechapp.models.data;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 4/20/2017.
 */

public class WLMISDetailsModel implements Parcelable {

    public static final Creator<WLMISDetailsModel> CREATOR = new Creator<WLMISDetailsModel>() {
        @Override
        public WLMISDetailsModel createFromParcel(Parcel in) {
            return new WLMISDetailsModel(in);
        }

        @Override
        public WLMISDetailsModel[] newArray(int size) {
            return new WLMISDetailsModel[size];
        }
    };
    private String LMECode;
    private int Batch;
    private String StartedAt;
    private String CompletedAt;
    private int WL;

    protected WLMISDetailsModel(Parcel in) {
        LMECode = in.readString();
        Batch = in.readInt();
        StartedAt = in.readString();
        CompletedAt = in.readString();
        WL = in.readInt();
    }

    public String getLMECode() {
        return LMECode;
    }

    public void setLMECode(String LMECode) {
        this.LMECode = LMECode;
    }

    public int getBatch() {
        return Batch;
    }

    public void setBatch(int batch) {
        Batch = batch;
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

    public int getWL() {
        return WL;
    }

    public void setWL(int WL) {
        this.WL = WL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(LMECode);
        dest.writeInt(Batch);
        dest.writeString(StartedAt);
        dest.writeString(CompletedAt);
        dest.writeInt(WL);
    }
}
