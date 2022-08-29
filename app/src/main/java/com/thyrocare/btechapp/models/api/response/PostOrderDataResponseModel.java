package com.thyrocare.btechapp.models.api.response;

import android.os.Parcel;
import android.os.Parcelable;

public class PostOrderDataResponseModel implements Parcelable {
    public static final Creator<PostOrderDataResponseModel> CREATOR = new Creator<PostOrderDataResponseModel>() {
        @Override
        public PostOrderDataResponseModel createFromParcel(Parcel in) {
            return new PostOrderDataResponseModel(in);
        }

        @Override
        public PostOrderDataResponseModel[] newArray(int size) {
            return new PostOrderDataResponseModel[size];
        }
    };
    private String AGE, GENDER, LEAD_ID, NAME;

    protected PostOrderDataResponseModel(Parcel in) {
        AGE = in.readString();
        GENDER = in.readString();
        LEAD_ID = in.readString();
        NAME = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(AGE);
        dest.writeString(GENDER);
        dest.writeString(LEAD_ID);
        dest.writeString(NAME);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getAGE() {
        return AGE;
    }

    public void setAGE(String AGE) {
        this.AGE = AGE;
    }

    public String getGENDER() {
        return GENDER;
    }

    public void setGENDER(String GENDER) {
        this.GENDER = GENDER;
    }

    public String getLEAD_ID() {
        return LEAD_ID;
    }

    public void setLEAD_ID(String LEAD_ID) {
        this.LEAD_ID = LEAD_ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }
}
