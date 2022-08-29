package com.thyrocare.btechapp.models.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Orion on 6/21/2017.
 */

public class VisitEarningsModel implements Parcelable {
    public static final Creator<VisitEarningsModel> CREATOR = new Creator<VisitEarningsModel>() {
        @Override
        public VisitEarningsModel createFromParcel(Parcel in) {
            return new VisitEarningsModel(in);
        }

        @Override
        public VisitEarningsModel[] newArray(int size) {
            return new VisitEarningsModel[size];
        }
    };
    ArrayList<KitsCountModel> kits;
    private int EstIncome;

    public VisitEarningsModel() {
    }

    protected VisitEarningsModel(Parcel in) {
        EstIncome = in.readInt();
        kits = in.createTypedArrayList(KitsCountModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(EstIncome);
        dest.writeTypedList(kits);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getEstIncome() {
        return EstIncome;
    }

    public void setEstIncome(int estIncome) {
        EstIncome = estIncome;
    }

    public ArrayList<KitsCountModel> getKits() {
        return kits;
    }

    public void setKits(ArrayList<KitsCountModel> kits) {
        this.kits = kits;
    }
}
