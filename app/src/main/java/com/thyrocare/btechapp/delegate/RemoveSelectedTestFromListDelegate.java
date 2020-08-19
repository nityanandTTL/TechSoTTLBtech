package com.thyrocare.btechapp.delegate;

import com.thyrocare.btechapp.models.data.BeneficiaryTestDetailsModel;

import java.util.ArrayList;

public interface RemoveSelectedTestFromListDelegate {
    void onRemoveButtonClicked(ArrayList<BeneficiaryTestDetailsModel> selectedTests);
}