package com.thyrocare.btechapp.models.api.request;

/**
 * Created by Orion on 5/12/2017.
 */

public class RemoveBeneficiaryAPIRequestModel {
    String OrderNo;
    int BenId;
    String isAdded;
    String UserId;

    public RemoveBeneficiaryAPIRequestModel() {
        setIsAdded("0");
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
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
