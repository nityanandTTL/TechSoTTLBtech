package com.thyrocare.btechapp.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 5/16/2017.
 */

public class NarrationMasterModel implements Parcelable {
    public static final Creator<NarrationMasterModel> CREATOR = new Creator<NarrationMasterModel>() {
        @Override
        public NarrationMasterModel createFromParcel(Parcel in) {
            return new NarrationMasterModel(in);
        }

        @Override
        public NarrationMasterModel[] newArray(int size) {
            return new NarrationMasterModel[size];
        }
    };
    private String Narration;
    private int NarrationId;

    public NarrationMasterModel() {
    }

    protected NarrationMasterModel(Parcel in) {
        Narration = in.readString();
        NarrationId = in.readInt();
    }

    public String getNarration() {
        return Narration;
    }

    public void setNarration(String narration) {
        Narration = narration;
    }

    public int getNarrationId() {
        return NarrationId;
    }

    public void setNarrationId(int narrationId) {
        NarrationId = narrationId;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Narration);
        dest.writeInt(NarrationId);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
