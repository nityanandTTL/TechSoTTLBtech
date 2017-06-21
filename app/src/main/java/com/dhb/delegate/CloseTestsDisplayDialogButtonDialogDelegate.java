package com.dhb.delegate;

import com.dhb.models.data.TestRateMasterModel;

import java.util.ArrayList;

public interface CloseTestsDisplayDialogButtonDialogDelegate {
    void onItemClick(ArrayList<TestRateMasterModel> selectedTestsList,boolean isTestEdit);
}