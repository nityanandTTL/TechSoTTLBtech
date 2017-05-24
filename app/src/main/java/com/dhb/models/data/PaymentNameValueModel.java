package com.dhb.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vendor3 on 5/16/2017.
 */

public class PaymentNameValueModel implements Parcelable{
    private String Key;
    private String Value;
    private String Required;
    private String Hint;

    public PaymentNameValueModel() {
    }

    protected PaymentNameValueModel(Parcel in) {
        Key = in.readString();
        Value = in.readString();
        Required = in.readString();
        Hint = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Key);
        dest.writeString(Value);
        dest.writeString(Required);
        dest.writeString(Hint);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PaymentNameValueModel> CREATOR = new Creator<PaymentNameValueModel>() {
        @Override
        public PaymentNameValueModel createFromParcel(Parcel in) {
            return new PaymentNameValueModel(in);
        }

        @Override
        public PaymentNameValueModel[] newArray(int size) {
            return new PaymentNameValueModel[size];
        }
    };

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getRequired() {
        return Required;
    }

    public void setRequired(String required) {
        Required = required;
    }

    public String getHint() {
        return Hint;
    }

    public void setHint(String hint) {
        Hint = hint;
    }
}
