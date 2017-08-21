package com.thyrocare.models.api.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.thyrocare.models.data.LocationModel;

/**
 * Created by Orion on 5/25/2017.
 */

public class LocusPushLocationRequestModel implements Parcelable {
    private LocationModel location;

    public LocusPushLocationRequestModel() {
    }

    public LocationModel getLocation() {
        return location;
    }

    public void setLocation(LocationModel location) {
        this.location = location;
    }

    protected LocusPushLocationRequestModel(Parcel in) {
        location = in.readParcelable(LocationModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(location, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LocusPushLocationRequestModel> CREATOR = new Creator<LocusPushLocationRequestModel>() {
        @Override
        public LocusPushLocationRequestModel createFromParcel(Parcel in) {
            return new LocusPushLocationRequestModel(in);
        }

        @Override
        public LocusPushLocationRequestModel[] newArray(int size) {
            return new LocusPushLocationRequestModel[size];
        }
    };
}
