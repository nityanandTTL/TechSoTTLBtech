package com.thyrocare.btechapp.models.api.request;

public class PECutomerIntimationSMSRequestModel {

    /**
     * orderNo :
     * BtechId : 0
     * UserId : 0
     */

    private String orderNo;
    private Integer BtechId;
    private Integer UserId;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getBtechId() {
        return BtechId;
    }

    public void setBtechId(Integer BtechId) {
        this.BtechId = BtechId;
    }

    public Integer getUserId() {
        return UserId;
    }

    public void setUserId(Integer UserId) {
        this.UserId = UserId;
    }
}
