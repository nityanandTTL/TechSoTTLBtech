package com.dhb.delegate;

import com.dhb.models.data.BeneficiaryLabAlertsModel;

import java.util.ArrayList;

/**
 * Created by vendor1 on 5/5/2017.
 */

public interface SelectLabAlertsCheckboxDelegate {
    void onCheckChange(ArrayList<BeneficiaryLabAlertsModel> chArr);
}
