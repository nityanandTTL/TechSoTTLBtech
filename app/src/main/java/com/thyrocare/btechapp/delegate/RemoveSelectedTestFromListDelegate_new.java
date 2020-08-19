package com.thyrocare.btechapp.delegate;



import com.thyrocare.btechapp.models.data.TestRateMasterModel;

import java.util.ArrayList;

public interface RemoveSelectedTestFromListDelegate_new {
    void onRemoveButtonClicked(ArrayList<TestRateMasterModel> selectedTests);
}