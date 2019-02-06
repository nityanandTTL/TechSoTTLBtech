package com.thyrocare.models.api.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.thyrocare.models.data.Numberofdaysdatamodel;

/**
 * Created by Orion on 6/22/2017.
 */

public class NewBtechAvaliabilityResponseModel  {
    private Numberofdaysdatamodel NumberOfDays;

    public NewBtechAvaliabilityResponseModel() {
    }


    public Numberofdaysdatamodel getNumberOfDays() {
        return NumberOfDays;
    }

    public void setNumberOfDays(Numberofdaysdatamodel numberOfDays) {
        NumberOfDays = numberOfDays;
    }
}
