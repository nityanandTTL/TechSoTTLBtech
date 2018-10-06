package com.thyrocare.models.api.request;

import com.thyrocare.models.data.BtechIdModel;
import com.thyrocare.models.data.MaterialsStocksModel;

import java.util.ArrayList;

/**
 * Created by Orion on 5/3/2017.
 */

public class BtechsRequestModel {
    private BtechIdModel BTechs;
    private ArrayList<MaterialsStocksModel> Stocks;

    public BtechIdModel getBTechs() {
        return BTechs;
    }

    public void setBTechs(BtechIdModel BTechs) {
        this.BTechs = BTechs;
    }

    public ArrayList<MaterialsStocksModel> getStocks() {
        return Stocks;
    }

    public void setStocks(ArrayList<MaterialsStocksModel> stocks) {
        Stocks = stocks;
    }
}
