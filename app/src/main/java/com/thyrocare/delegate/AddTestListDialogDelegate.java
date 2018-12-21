package com.thyrocare.delegate;

import com.thyrocare.models.data.BeneficiaryTestDetailsModel;

import java.util.ArrayList;

public interface AddTestListDialogDelegate {
    void onItemClick(ArrayList<BeneficiaryTestDetailsModel> selectedTestsList);
}