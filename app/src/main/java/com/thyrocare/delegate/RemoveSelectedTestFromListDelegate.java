package com.thyrocare.delegate;

import com.thyrocare.models.data.BeneficiaryTestDetailsModel;

import java.util.ArrayList;

public interface RemoveSelectedTestFromListDelegate {
    void onRemoveButtonClicked(ArrayList<BeneficiaryTestDetailsModel> selectedTests);
}