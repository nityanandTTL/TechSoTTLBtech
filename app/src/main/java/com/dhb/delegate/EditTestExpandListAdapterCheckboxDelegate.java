package com.dhb.delegate;

import com.dhb.models.data.TestRateMasterModel;

import java.util.ArrayList;

/**
 * Created by vendor1 on 5/5/2017.
 */

public interface EditTestExpandListAdapterCheckboxDelegate {
    void onCheckChange(ArrayList<TestRateMasterModel> selectedTests);
}
