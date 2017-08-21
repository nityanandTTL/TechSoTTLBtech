package com.thyrocare.models.api.request;

/**
 * Created by Orion on 5/9/2017.
 */

public class OlcScanPickUpRequestModel {
    String Barcode,BarcodeType;
    int BtechId,ClientId;

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String barcode) {
        Barcode = barcode;
    }

    public String getBarcodeType() {
        return BarcodeType;
    }

    public void setBarcodeType(String barcodeType) {
        BarcodeType = barcodeType;
    }

    public int getBtechId() {
        return BtechId;
    }

    public void setBtechId(int btechId) {
        BtechId = btechId;
    }

    public int getClientId() {
        return ClientId;
    }

    public void setClientId(int clientId) {
        ClientId = clientId;
    }
}
