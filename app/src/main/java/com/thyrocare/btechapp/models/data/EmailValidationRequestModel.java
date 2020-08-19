package com.thyrocare.btechapp.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by e5209@thyrocare.com on 8/9/18.
 */

public class EmailValidationRequestModel implements Parcelable{
    private String AppID;
    private String EmailID;

    public EmailValidationRequestModel() {
    }

    protected EmailValidationRequestModel(Parcel in) {
        AppID = in.readString();
        EmailID = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(AppID);
        dest.writeString(EmailID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EmailValidationRequestModel> CREATOR = new Creator<EmailValidationRequestModel>() {
        @Override
        public EmailValidationRequestModel createFromParcel(Parcel in) {
            return new EmailValidationRequestModel(in);
        }

        @Override
        public EmailValidationRequestModel[] newArray(int size) {
            return new EmailValidationRequestModel[size];
        }
    };

    public String getAppID() {
        return AppID;
    }

    public void setAppID(String appID) {
        AppID = appID;
    }

    public String getEmailID() {
        return EmailID;
    }

    public void setEmailID(String emailID) {
        EmailID = emailID;
    }
}
