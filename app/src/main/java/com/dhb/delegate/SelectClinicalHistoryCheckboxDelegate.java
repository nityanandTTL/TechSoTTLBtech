package com.dhb.delegate;

import com.dhb.models.data.TestWiseBeneficiaryClinicalHistoryModel;

import java.util.ArrayList;

/**
 * Created by vendor1 on 5/5/2017.
 */

public interface SelectClinicalHistoryCheckboxDelegate {
    void onCheckChange(ArrayList<TestWiseBeneficiaryClinicalHistoryModel> chArr);
}