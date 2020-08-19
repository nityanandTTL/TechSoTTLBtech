package com.thyrocare.btechapp.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by e5233@thyrocare.com on 26/3/18.
 */

public class AccessUserCodeModel extends BaseModel implements Parcelable {
    private String AccessCode;

    protected AccessUserCodeModel(Parcel in) {
        super(in);
        AccessCode = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(AccessCode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AccessUserCodeModel> CREATOR = new Creator<AccessUserCodeModel>() {
        @Override
        public AccessUserCodeModel createFromParcel(Parcel in) {
            return new AccessUserCodeModel(in);
        }

        @Override
        public AccessUserCodeModel[] newArray(int size) {
            return new AccessUserCodeModel[size];
        }
    };

    public String getAccessCode() {
        return AccessCode;
    }

    public void setAccessCode(String accessCode) {
        AccessCode = accessCode;
    }
}
