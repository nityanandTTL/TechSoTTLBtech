package com.thyrocare.models.api.request;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by e4904@thyrocare.com on 23/3/18.
 */

public class EmailgetModel implements Parcelable
{

    private String result;
    private String reason;
    private String disposable;
    private String accept_all;
    private String role;
    private String free;
    private String email;
    private String user;
    private String domain;
    private String safe_to_send;
    private String did_you_mean;
    private String success;
    private String message;


    protected EmailgetModel(Parcel in) {
        result = in.readString();
        reason = in.readString();
        disposable = in.readString();
        accept_all = in.readString();
        role = in.readString();
        free = in.readString();
        email = in.readString();
        user = in.readString();
        domain = in.readString();
        safe_to_send = in.readString();
        did_you_mean = in.readString();
        success = in.readString();
        message = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(result);
        dest.writeString(reason);
        dest.writeString(disposable);
        dest.writeString(accept_all);
        dest.writeString(role);
        dest.writeString(free);
        dest.writeString(email);
        dest.writeString(user);
        dest.writeString(domain);
        dest.writeString(safe_to_send);
        dest.writeString(did_you_mean);
        dest.writeString(success);
        dest.writeString(message);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EmailgetModel> CREATOR = new Creator<EmailgetModel>() {
        @Override
        public EmailgetModel createFromParcel(Parcel in) {
            return new EmailgetModel(in);
        }

        @Override
        public EmailgetModel[] newArray(int size) {
            return new EmailgetModel[size];
        }
    };

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDisposable() {
        return disposable;
    }

    public void setDisposable(String disposable) {
        this.disposable = disposable;
    }

    public String getAccept_all() {
        return accept_all;
    }

    public void setAccept_all(String accept_all) {
        this.accept_all = accept_all;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFree() {
        return free;
    }

    public void setFree(String free) {
        this.free = free;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getSafe_to_send() {
        return safe_to_send;
    }

    public void setSafe_to_send(String safe_to_send) {
        this.safe_to_send = safe_to_send;
    }

    public String getDid_you_mean() {
        return did_you_mean;
    }

    public void setDid_you_mean(String did_you_mean) {
        this.did_you_mean = did_you_mean;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
