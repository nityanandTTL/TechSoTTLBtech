package com.thyrocare.btechapp.models.api.response;

import com.thyrocare.btechapp.models.data.BtechOrderModel;

import java.util.ArrayList;

/**
 * Created by Orion on 6/16/2017.
 */

public class OrderServedResponseModel {
   private String Response;
    private ArrayList<BtechOrderModel> btchOrd;

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public ArrayList<BtechOrderModel> getBtchOrd() {
        return btchOrd;
    }

    public void setBtchOrd(ArrayList<BtechOrderModel> btchOrd) {
        this.btchOrd = btchOrd;
    }
}
