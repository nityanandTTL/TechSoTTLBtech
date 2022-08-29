package com.thyrocare.btechapp.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by e5233@thyrocare.com on 26/6/18.
 */

public class DispositionDetailsModel implements Parcelable {

    public static final Creator<DispositionDetailsModel> CREATOR = new Creator<DispositionDetailsModel>() {
        @Override
        public DispositionDetailsModel createFromParcel(Parcel in) {
            return new DispositionDetailsModel(in);
        }

        @Override
        public DispositionDetailsModel[] newArray(int size) {
            return new DispositionDetailsModel[size];
        }
    };
    private int dispId;
    private String Disposition;

    public DispositionDetailsModel() {

    }

    protected DispositionDetailsModel(Parcel in) {
        dispId = in.readInt();
        Disposition = in.readString();
    }

    public int getDispId() {
        return dispId;
    }

    public void setDispId(int dispId) {
        this.dispId = dispId;
    }

    public String getDisposition() {
        return Disposition;
    }

    public void setDisposition(String disposition) {
        Disposition = disposition;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(dispId);
        dest.writeString(Disposition);
    }
}
