package com.dhb.models.api.response;

import com.dhb.models.data.BTMaterialsModel;
import com.dhb.models.data.LedgerDetailsModeler;

import java.util.ArrayList;

/**
 * Created by E4904 on 5/2/2017.
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
