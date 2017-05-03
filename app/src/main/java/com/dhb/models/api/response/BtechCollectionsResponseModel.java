package com.dhb.models.api.response;




import com.dhb.models.data.BarcodeModel;

import java.util.ArrayList;

/**
 * Created by vendor1 on 5/2/2017.
 */

public class BtechCollectionsResponseModel {
    private int Btech;
    private ArrayList<BarcodeModel> Barcode;

    public int getBtech() {
        return Btech;
    }

    public void setBtech(int btech) {
        Btech = btech;
    }

    public ArrayList<BarcodeModel> getBarcode() {
        return Barcode;
    }

    public void setBarcode(ArrayList<BarcodeModel> barcode) {
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
