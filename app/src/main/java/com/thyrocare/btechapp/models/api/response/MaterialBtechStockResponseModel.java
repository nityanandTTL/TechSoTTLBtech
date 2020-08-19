package com.thyrocare.btechapp.models.api.response;

import com.thyrocare.btechapp.models.data.BTStockMaterialsModel;

import java.util.ArrayList;

/**
 * Created by Orion on 5/2/2017.
 */

public class MaterialBtechStockResponseModel {

    private int BtechId;
    private ArrayList<BTStockMaterialsModel> allMaterialStock;

    public int getBtechId() {
        return BtechId;
    }

    public void setBtechId(int btechId) {
        BtechId = btechId;
    }

    public ArrayList<BTStockMaterialsModel> getAllMaterialStock() {
        return allMaterialStock;
    }

    public void setAllMaterialStock(ArrayList<BTStockMaterialsModel> allMaterialStock) {
        this.allMaterialStock = allMaterialStock;
    }
}
