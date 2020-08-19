package com.thyrocare.btechapp.delegate;

import com.thyrocare.btechapp.models.data.BeneficiaryTestDetailsModel;

import java.util.ArrayList;

public interface AddTestListDialogDelegate {
    void onItemClick(ArrayList<BeneficiaryTestDetailsModel> selectedTestsList);
}