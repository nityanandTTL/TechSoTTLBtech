package com.dhb.delegate;

import android.view.View;

import com.dhb.models.data.BarcodeModel;

/**
 * Created by vendor1 on 4/24/2017.
 */

public interface BtechCollectionsAdapterOnMasterBarcodeScanClickedDelegate {
    void onItemClicked(BarcodeModel barcodeModel, int position, View view);
}
