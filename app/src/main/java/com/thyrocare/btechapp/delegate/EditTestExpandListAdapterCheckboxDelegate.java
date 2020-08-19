package com.thyrocare.btechapp.delegate;

import com.thyrocare.btechapp.models.data.TestRateMasterModel;

import java.util.ArrayList;

/**
 * Created by Orion on 5/5/2017.
 */

public interface EditTestExpandListAdapterCheckboxDelegate {
    void onCheckChange(ArrayList<TestRateMasterModel> selectedTests);
}
