package com.thyrocare.btechapp.delegate;

import com.thyrocare.btechapp.models.data.BeneficiaryTestWiseClinicalHistoryModel;

import java.util.ArrayList;

/**
 * Created by Orion on 5/5/2017.
 */

public interface SelectClinicalHistoryCheckboxDelegate {
    void onCheckChange(ArrayList<BeneficiaryTestWiseClinicalHistoryModel> chArr);
}
