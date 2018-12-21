package com.thyrocare.models.data;

import java.util.ArrayList;

/**
 * Created by Orion on 6/16/2017.
 */

public class BtechOrderModel {
    private String OrderNo, OrderBy, Status, Mobile, Fasting;
    private int benCount, AmountCollected;
    private ArrayList<BtechBarcodeDetailModel> btchBracodeDtl;

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getFasting() {
        return Fasting;
    }

    public void setFasting(String fasting) {
        Fasting = fasting;
    }

    public String getOrderBy() {
        return OrderBy;
    }

    public void setOrderBy(String orderBy) {
        OrderBy = orderBy;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public int getBenCount() {
        return benCount;
    }

    public void setBenCount(int benCount) {
        this.benCount = benCount;
    }

    public int getAmountCollected() {
        return AmountCollected;
    }

    public void setAmountCollected(int amountCollected) {
        AmountCollected = amountCollected;
    }

    public ArrayList<BtechBarcodeDetailModel> getBtchBracodeDtl() {
        return btchBracodeDtl;
    }

    public void setBtchBracodeDtl(ArrayList<BtechBarcodeDetailModel> btchBracodeDtl) {
        this.btchBracodeDtl = btchBracodeDtl;
    }
}
