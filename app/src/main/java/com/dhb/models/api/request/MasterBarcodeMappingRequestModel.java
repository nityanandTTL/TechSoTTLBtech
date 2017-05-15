package com.dhb.models.api.request;

import com.dhb.models.data.HubBarcodeModel;

import java.util.ArrayList;

/**
 * Created by vendor1 on 5/2/2017.
 */

public class MasterBarcodeMappingRequestModel {
    private int BtechId, HubId;
    private String MasterBarcode;
    private ArrayList<HubBarcodeModel> barcodeModels;

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

    public ArrayList<HubBarcodeModel> getBarcodeModels() {
        return barcodeModels;
    }

    public void setBarcodeModels(ArrayList<HubBarcodeModel> barcodeModels) {
        this.barcodeModels = barcodeModels;
    }
}
