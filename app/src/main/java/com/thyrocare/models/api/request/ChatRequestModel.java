package com.thyrocare.models.api.request;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 6/27/2017.
 */

public class ChatRequestModel implements Parcelable {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String code;

    public ChatRequestModel() {
    }


    protected ChatRequestModel(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        phone = in.readString();
        password = in.readString();
        code = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(password);
        dest.writeString(code);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ChatRequestModel> CREATOR = new Creator<ChatRequestModel>() {
        @Override
        public ChatRequestModel createFromParcel(Parcel in) {
            return new ChatRequestModel(in);
        }

        @Override
        public ChatRequestModel[] newArray(int size) {
            return new ChatRequestModel[size];
        }
    };

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
