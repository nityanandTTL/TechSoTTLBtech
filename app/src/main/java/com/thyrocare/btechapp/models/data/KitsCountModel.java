package com.thyrocare.btechapp.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 5/2/2017.
 */

public class KitsCountModel implements Parcelable {
    public static final Creator<KitsCountModel> CREATOR = new Creator<KitsCountModel>() {
        @Override
        public KitsCountModel createFromParcel(Parcel in) {
            return new KitsCountModel(in);
        }

        @Override
        public KitsCountModel[] newArray(int size) {
            return new KitsCountModel[size];
        }
    };
    private String Kit;
    private int Value;

    public KitsCountModel() {
    }

    protected KitsCountModel(Parcel in) {
        Kit = in.readString();
        Value = in.readInt();
    }

    public String getKit() {
        return Kit;
    }

    public void setKit(String kit) {
        Kit = kit;
    }

    public int getValue() {
        return Value;
    }

    public void setValue(int value) {
        Value = value;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Kit);
        dest.writeInt(Value);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
