package com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 6/28/2017.
 */

public class SCTDetailsModel implements Parcelable {
    public static final Creator<SCTDetailsModel> CREATOR = new Creator<SCTDetailsModel>() {
        @Override
        public SCTDetailsModel createFromParcel(Parcel in) {
            return new SCTDetailsModel(in);
        }

        @Override
        public SCTDetailsModel[] newArray(int size) {
            return new SCTDetailsModel[size];
        }
    };
    public String SCTDate;
    public String SLA;

    public SCTDetailsModel() {
    }

    protected SCTDetailsModel(Parcel in) {
        SCTDate = in.readString();
        SLA = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(SCTDate);
        dest.writeString(SLA);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getSCTDate() {
        return SCTDate;
    }

    public void setSCTDate(String SCTDate) {
        this.SCTDate = SCTDate;
    }

    public String getSLA() {
        return SLA;
    }

    public void setSLA(String SLA) {
        this.SLA = SLA;
    }
}
