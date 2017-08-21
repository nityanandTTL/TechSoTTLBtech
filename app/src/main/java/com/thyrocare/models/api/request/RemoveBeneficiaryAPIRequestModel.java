package com.thyrocare.models.api.request;

/**
 * Created by Orion on 5/12/2017.
 */

public class RemoveBeneficiaryAPIRequestModel {
    String OrderNo;
    int BenId;
    String isAdded;

    public RemoveBeneficiaryAPIRequestModel() {
        setIsAdded("0");
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public int getBenId() {
        return BenId;
    }

    public void setBenId(int benId) {
        BenId = benId;
    }

    public String getIsAdded() {
        return isAdded;
    }

    public void setIsAdded(String isAdded) {
        this.isAdded = isAdded;
    }
}
