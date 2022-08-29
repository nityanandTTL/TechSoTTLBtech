package com.thyrocare.btechapp.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 5/2/2017.
 */

public class AcceptOrderNotfiDetailsModel implements Parcelable {

    public static final Creator<AcceptOrderNotfiDetailsModel> CREATOR = new Creator<AcceptOrderNotfiDetailsModel>() {
        @Override
        public AcceptOrderNotfiDetailsModel createFromParcel(Parcel in) {
            return new AcceptOrderNotfiDetailsModel(in);
        }

        @Override
        public AcceptOrderNotfiDetailsModel[] newArray(int size) {
            return new AcceptOrderNotfiDetailsModel[size];
        }
    };
    private String VisitId;
    private String SlotId;

    protected AcceptOrderNotfiDetailsModel(Parcel in) {
        VisitId = in.readString();
    }

    public String getSlotId() {
        return SlotId;
    }

    public void setSlotId(String slotId) {
        SlotId = slotId;
    }

    public String getVisitId() {
        return VisitId;
    }

    public void setVisitId(String visitId) {
        VisitId = visitId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(VisitId);
        dest.writeString(SlotId);
    }
}
