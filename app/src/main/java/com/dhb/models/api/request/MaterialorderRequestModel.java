package com.dhb.models.api.request;

import com.dhb.models.data.HubBarcodeModel;
import com.dhb.models.data.MaterialOrderDataModel;

import java.util.ArrayList;

/**
 * Created by Orion on 5/24/2017.
 */

public class MaterialorderRequestModel {

String BTechId;
    String BTTransactionCode;
    String OrderId;
    String FinalStatus;

    public String getBTechId() {
        return BTechId;
    }

    public void setBTechId(String BTechId) {
        this.BTechId = BTechId;
    }

    public String getBTTransactionCode() {
        return BTTransactionCode;
    }

    public void setBTTransactionCode(String BTTransactionCode) {
        this.BTTransactionCode = BTTransactionCode;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getFinalStatus() {
        return FinalStatus;
    }

    public void setFinalStatus(String finalStatus) {
        FinalStatus = finalStatus;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public ArrayList<MaterialOrderDataModel> getMaterialDetails() {
        return MaterialDetails;
    }

    public void setMaterialDetails(ArrayList<MaterialOrderDataModel> materialDetails) {
        MaterialDetails = materialDetails;
    }

    String Remarks;
    private ArrayList<MaterialOrderDataModel> MaterialDetails;

}
