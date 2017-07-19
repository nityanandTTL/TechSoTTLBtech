package com.dhb.models.api.response;

import com.dhb.models.data.BtechClientsModel;

import java.util.ArrayList;

/**
 * Created by Orion on 5/8/2017.
 */

public class BtechClientsResponseModel {
    private int BtechId;
    private ArrayList<BtechClientsModel> btechClients;
    private String Response;

    public int getBtechId() {
        return BtechId;
    }

    public void setBtechId(int btechId) {
        BtechId = btechId;
    }

    public ArrayList<BtechClientsModel> getBtechClients() {
        return btechClients;
    }

    public void setBtechClients(ArrayList<BtechClientsModel> btechClients) {
        this.btechClients = btechClients;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }
}
