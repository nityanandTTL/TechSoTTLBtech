package com.thyrocare.btechapp.delegate;

import com.thyrocare.btechapp.models.data.BeneficiaryLabAlertsModel;

import java.util.ArrayList;

/**
 * Created by Orion on 5/5/2017.
 */

public interface SelectLabAlertsCheckboxDelegate {
    void onCheckChange(ArrayList<BeneficiaryLabAlertsModel> chArr);
}
