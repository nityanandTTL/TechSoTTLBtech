package com.thyrocare.btechapp.delegate;

import com.thyrocare.btechapp.models.data.TestRateMasterModel;

import java.util.ArrayList;

public interface CloseTestsDisplayDialogButtonDialogDelegate {
    void onItemClick(ArrayList<TestRateMasterModel> selectedTestsList, boolean isTestEdit);
}