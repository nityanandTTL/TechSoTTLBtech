package com.thyrocare.models.api.response;

import com.thyrocare.models.data.BtechClientsModel;

import java.util.ArrayList;

/**
 * Created by Orion on 5/8/2017.<br/>
 *  http://bts.dxscloud.com/btsapi/api/BtechClients/884543107<br/>
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
