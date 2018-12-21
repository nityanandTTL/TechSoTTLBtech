package com.thyrocare.models.api.request;

import com.thyrocare.models.data.BtechwithHub_BarcodeDataModel;

import java.util.ArrayList;

/**
 * Created by Orion on 5/2/2017.
 */

public class BtechwithHub_MasterBarcodeMappingRequestModel {
    private String MasterBarcode,BtechId, HubId;
    private ArrayList<BtechwithHub_BarcodeDataModel> barcodes;

    public String getMasterBarcode() {
        return MasterBarcode;
    }

    public void setMasterBarcode(String masterBarcode) {
        MasterBarcode = masterBarcode;
    }

    public String getBtechId() {
        return BtechId;
    }

    public void setBtechId(String btechId) {
        BtechId = btechId;
    }

    public String getHubId() {
        return HubId;
    }

    public void setHubId(String hubId) {
        HubId = hubId;
    }

    public ArrayList<BtechwithHub_BarcodeDataModel> getBarcodes() {
        return barcodes;
    }

    public void setBarcodes(ArrayList<BtechwithHub_BarcodeDataModel> barcodes) {
        this.barcodes = barcodes;
    }
}
