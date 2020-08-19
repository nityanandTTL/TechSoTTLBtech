package com.thyrocare.btechapp.models.api.response;

import com.thyrocare.btechapp.models.data.Numberofdaysdatamodel;

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
