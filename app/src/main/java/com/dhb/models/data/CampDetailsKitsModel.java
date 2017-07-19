package com.dhb.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 5/15/2017.
 */

public class CampDetailsKitsModel implements Parcelable{
    private int Value;
    private String Kit;

    public CampDetailsKitsModel() {
    }

    protected CampDetailsKitsModel(Parcel in) {
        Value = in.readInt();
        Kit = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Value);
        dest.writeString(Kit);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CampDetailsKitsModel> CREATOR = new Creator<CampDetailsKitsModel>() {
        @Override
        public CampDetailsKitsModel createFromParcel(Parcel in) {
            return new CampDetailsKitsModel(in);
        }

        @Override
        public CampDetailsKitsModel[] newArray(int size) {
            return new CampDetailsKitsModel[size];
        }
    };

    public int getValue() {
        return Value;
    }

    public void setValue(int value) {
        Value = value;
    }

    public String getKit() {
        return Kit;
    }

    public void setKit(String kit) {
        Kit = kit;
    }
}
