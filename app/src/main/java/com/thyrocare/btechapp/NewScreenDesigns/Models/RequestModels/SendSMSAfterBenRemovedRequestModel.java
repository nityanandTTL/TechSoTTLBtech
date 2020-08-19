package com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels;

import java.io.Serializable;

public class SendSMSAfterBenRemovedRequestModel implements Serializable {

    int BenId;
    String OrderNo;
    String Rate1;

    public int getBenId() {
        return BenId;
    }

    public void setBenId(int benId) {
        BenId = benId;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getRate1() {
        return Rate1;
    }

    public void setRate1(String rate1) {
        Rate1 = rate1;
    }
}
