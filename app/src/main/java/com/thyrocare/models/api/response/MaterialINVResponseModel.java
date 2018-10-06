package com.thyrocare.models.api.response;

import com.thyrocare.models.data.BTMaterialsModel;

import java.util.ArrayList;

/**
 * Created by Orion on 5/2/2017.
 */

public class MaterialINVResponseModel {

    private int BTechID;
    private ArrayList<BTMaterialsModel> BTMaterials;

    public int getBTechID() {
        return BTechID;
    }

    public void setBTechID(int BTechID) {
        this.BTechID = BTechID;
    }

    public ArrayList<BTMaterialsModel> getBTMaterials() {
        return BTMaterials;
    }

    public void setBTMaterials(ArrayList<BTMaterialsModel> BTMaterials) {
        this.BTMaterials = BTMaterials;
    }


}
