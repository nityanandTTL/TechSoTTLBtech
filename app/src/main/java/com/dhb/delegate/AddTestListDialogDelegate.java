package com.dhb.delegate;

import com.dhb.dao.models.TestRateMasterDao;
import com.dhb.models.data.BeneficiaryTestDetailsModel;
import com.dhb.models.data.TestRateMasterModel;

import java.util.ArrayList;

public interface AddTestListDialogDelegate {
    void onItemClick(ArrayList<BeneficiaryTestDetailsModel> selectedTestsList);
}