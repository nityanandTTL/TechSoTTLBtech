package com.thyrocare.delegate;

import com.thyrocare.models.data.TestRateMasterModel;

import java.util.ArrayList;

public interface CloseTestsDisplayDialogButtonDialogDelegate {
    void onItemClick(ArrayList<TestRateMasterModel> selectedTestsList, boolean isTestEdit);
}