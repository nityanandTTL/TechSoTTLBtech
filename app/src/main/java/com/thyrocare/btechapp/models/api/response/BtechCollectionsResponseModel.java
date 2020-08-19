package com.thyrocare.btechapp.models.api.response;




import com.thyrocare.btechapp.models.data.HubBarcodeModel;

import java.util.ArrayList;

/**
 * Created by Orion on 5/2/2017.
 */

public class BtechCollectionsResponseModel {
    private int Btech;
    private ArrayList<HubBarcodeModel> Barcode;

    public int getBtech() {
        return Btech;
    }

    public void setBtech(int btech) {
        Btech = btech;
    }

    public ArrayList<HubBarcodeModel> getBarcode() {
        return Barcode;
    }

    public void setBarcode(ArrayList<HubBarcodeModel> barcode) {
        Barcode = barcode;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    private String Response;
}
