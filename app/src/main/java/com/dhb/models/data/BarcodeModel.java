package com.dhb.models.data;

/**
 * Created by vendor1 on 5/2/2017.
 */

public class BarcodeModel {
    private int Barcode;
    private String SampleType,SCT;

    public int getBarcode() {
        return Barcode;
    }

    public void setBarcode(int barcode) {
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
}
