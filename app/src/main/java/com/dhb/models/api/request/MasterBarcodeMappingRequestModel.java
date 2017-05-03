package com.dhb.models.api.request;

import com.dhb.models.data.BarcodeModel;

import java.util.ArrayList;

/**
 * Created by vendor1 on 5/2/2017.
 */

public class MasterBarcodeMappingRequestModel {
    private int BtechId, HubId;
    private String MasterBarcode;
    private ArrayList<BarcodeModel> barcodeModels;

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

    public ArrayList<BarcodeModel> getBarcodeModels() {
        return barcodeModels;
    }

    public void setBarcodeModels(ArrayList<BarcodeModel> barcodeModels) {
        this.barcodeModels = barcodeModels;
    }
}
