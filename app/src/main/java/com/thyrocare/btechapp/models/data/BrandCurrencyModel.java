package com.thyrocare.btechapp.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 4/28/2017.
 */
public class BrandCurrencyModel implements Parcelable {
    public static final Creator<BrandCurrencyModel> CREATOR = new Creator<BrandCurrencyModel>() {
        @Override
        public BrandCurrencyModel createFromParcel(Parcel in) {
            return new BrandCurrencyModel(in);
        }

        @Override
        public BrandCurrencyModel[] newArray(int size) {
            return new BrandCurrencyModel[size];
        }
    };
    private int CurrencyId;
    private String Currency;
    private String Sign;

    public BrandCurrencyModel() {
    }

    protected BrandCurrencyModel(Parcel in) {
        CurrencyId = in.readInt();
        Currency = in.readString();
        Sign = in.readString();
    }

    public int getCurrencyId() {
        return CurrencyId;
    }

    public void setCurrencyId(int currencyId) {
        CurrencyId = currencyId;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public String getSign() {
        return Sign;
    }

    public void setSign(String sign) {
        Sign = sign;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(CurrencyId);
        dest.writeString(Currency);
        dest.writeString(Sign);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
