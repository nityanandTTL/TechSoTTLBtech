package com.dhb.delegate;

import com.dhb.models.data.BarcodeDetailsModel;
import com.dhb.models.data.BeneficiarySampleTypeDetailsModel;

import java.util.ArrayList;

/**
 * Created by ISRO on 5/2/2017.
 */
public interface ScanBarcodeIconClickedDelegate {
    void onClicked(ArrayList<BarcodeDetailsModel> scannedBarcodes, ArrayList<BeneficiarySampleTypeDetailsModel> scannedSampleTypes);
}
