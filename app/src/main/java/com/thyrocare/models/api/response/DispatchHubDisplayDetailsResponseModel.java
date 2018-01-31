package com.thyrocare.models.api.response;

import com.thyrocare.models.data.HUBBTechModel;

import java.util.ArrayList;

/**
 * Created by Orion on 4/27/2017.
 */

public class DispatchHubDisplayDetailsResponseModel {
    private int BtechId;
    private ArrayList<HUBBTechModel> HubMaster;
    private String Response;

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }


    public int getBtechId() {
        return BtechId;
    }

    public void setBtechId(int btechId) {
        BtechId = btechId;
    }

    public ArrayList<HUBBTechModel> getHubMaster() {
        return HubMaster;
    }

    public void setHubMaster(ArrayList<HUBBTechModel> hubMaster) {
        HubMaster = hubMaster;
    }
}
