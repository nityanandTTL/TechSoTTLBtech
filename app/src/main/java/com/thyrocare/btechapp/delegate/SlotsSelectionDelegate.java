package com.thyrocare.btechapp.delegate;

import com.thyrocare.btechapp.models.data.SlotModel;

import java.util.ArrayList;

/**
 * Created by Orion on 5/2/2017.
 */
public interface SlotsSelectionDelegate {
    void onSlotSelected(ArrayList<SlotModel> selectedSlotModels);
}
