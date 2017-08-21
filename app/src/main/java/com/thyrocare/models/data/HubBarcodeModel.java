package com.thyrocare.models.data;

/**
 * Created by Orion on 5/2/2017.
 */

public class HubBarcodeModel {
    private String Barcode;
    private String SampleType,SCT;
    private boolean IsScanned;

    public HubBarcodeModel() {
        IsScanned = false;
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String barcode) {
        Barcode = barcode;
    }

    public String getSampleType() {
        return SampleType;
    }

    public void setSampleType(String sampleType) {
        SampleType = sampleType;
    }

    public String getSCT() {
        return SCT;
    }

    public void setSCT(String SCT) {
        this.SCT = SCT;
    }

    public boolean isScanned() {
        return IsScanned;
    }

    public void setScanned(boolean scanned) {
        IsScanned = scanned;
    }
}