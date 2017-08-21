package com.thyrocare.models.api.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.thyrocare.models.data.PaymentNameValueModel;

import java.util.ArrayList;

/**
 * Created by Orion on 5/16/2017.
 */

public class PaymentProcessAPIResponseModel implements Parcelable {
    private ArrayList<PaymentNameValueModel> NameValueCollection;
    private int URLId;
    private String APIUrl;

    public PaymentProcessAPIResponseModel() {
    }

    protected PaymentProcessAPIResponseModel(Parcel in) {
        NameValueCollection = in.createTypedArrayList(PaymentNameValueModel.CREATOR);
        URLId = in.readInt();
        APIUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(NameValueCollection);
        dest.writeInt(URLId);
        dest.writeString(APIUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PaymentProcessAPIResponseModel> CREATOR = new Creator<PaymentProcessAPIResponseModel>() {
        @Override
        public PaymentProcessAPIResponseModel createFromParcel(Parcel in) {
            return new PaymentProcessAPIResponseModel(in);
        }

        @Override
        public PaymentProcessAPIResponseModel[] newArray(int size) {
            return new PaymentProcessAPIResponseModel[size];
        }
    };

    public ArrayList<PaymentNameValueModel> getNameValueCollection() {
        return NameValueCollection;
    }

    public void setNameValueCollection(ArrayList<PaymentNameValueModel> nameValueCollection) {
        NameValueCollection = nameValueCollection;
    }

    public int getURLId() {
        return URLId;
    }

    public void setURLId(int URLId) {
        this.URLId = URLId;
    }

    public String getAPIUrl() {
        return APIUrl;
    }

    public void setAPIUrl(String APIUrl) {
        this.APIUrl = APIUrl;
    }
}
