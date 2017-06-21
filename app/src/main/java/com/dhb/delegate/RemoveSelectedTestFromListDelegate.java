package com.dhb.delegate;

import com.dhb.models.data.BeneficiaryTestDetailsModel;

import java.util.ArrayList;

public interface RemoveSelectedTestFromListDelegate {
    void onRemoveButtonClicked(ArrayList<BeneficiaryTestDetailsModel> selectedTests);
}