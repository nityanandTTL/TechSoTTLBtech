package com.thyrocare.btechapp.models.api.request;

import com.thyrocare.btechapp.models.data.HubBarcodeModel;

import java.util.ArrayList;

/**
 * Created by Orion on 5/2/2017.
 */

public class MasterBarcodeMappingRequestModel {
    private int BtechId, HubId;
    private String MasterBarcode;
    private ArrayList<HubBarcodeModel> barcodes;

    public int getBtechId() {
        return BtechId;
    }

    public void setBtechId(int btechId) {
        BtechId = btechId;
    }

    public int getHubId() {
        return HubId;
    }

    public void setHubId(int hubId) {
        HubId = hubId;
    }

    public String getMasterBarcode() {
        return MasterBarcode;
    }

    public void setMasterBarcode(String masterBarcode) {
        MasterBarcode = masterBarcode;
    }

    public ArrayList<HubBarcodeModel> getBarcodes() {
        return barcodes;
    }

    public void setBarcodes(ArrayList<HubBarcodeModel> barcodes) {
        this.barcodes = barcodes;
    }
}
