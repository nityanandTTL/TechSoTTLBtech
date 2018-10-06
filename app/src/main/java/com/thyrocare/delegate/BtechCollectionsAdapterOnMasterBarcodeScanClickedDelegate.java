package com.thyrocare.delegate;

import android.view.View;

import com.thyrocare.models.data.HubBarcodeModel;

/**
 * Created by Orion on 4/24/2017.
 */

public interface BtechCollectionsAdapterOnMasterBarcodeScanClickedDelegate {
    void onItemClicked(HubBarcodeModel barcodeModel, int position, View view);
}
