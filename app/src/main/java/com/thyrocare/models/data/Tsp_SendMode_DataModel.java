package com.thyrocare.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 7/4/2017.
 */

public class Tsp_SendMode_DataModel implements Parcelable{

    private int ModeId;
    private String Mode;

    public Tsp_SendMode_DataModel(Parcel in) {
        ModeId = in.readInt();
        Mode = in.readString();
    }

    public static final Creator<Tsp_SendMode_DataModel> CREATOR = new Creator<Tsp_SendMode_DataModel>() {
        @Override
        public Tsp_SendMode_DataModel createFromParcel(Parcel in) {
            return new Tsp_SendMode_DataModel(in);
        }

        @Override
        public Tsp_SendMode_DataModel[] newArray(int size) {
            return new Tsp_SendMode_DataModel[size];
        }
    };

    public Tsp_SendMode_DataModel() {

    }

    @Override
    public String toString() {
        return "Tsp_SendMode_DataModel{" +
                "ModeId=" + ModeId +
                ", Mode='" + Mode + '\'' +
                '}';
    }

    public int getModeId() {
        return ModeId;
    }

    public void setModeId(int modeId) {
        ModeId = modeId;
    }

    public String getMode() {
        return Mode;
    }

    public void setMode(String mode) {
        Mode = mode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ModeId);
        dest.writeString(Mode);
    }
}
