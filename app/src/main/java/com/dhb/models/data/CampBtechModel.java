package com.dhb.models.data;

/**
 * Created by vendor1 on 5/10/2017.
 */

public class CampBtechModel {
    private int BtechId;
    private String Mobile,Status;

    public int getBtechId() {
        return BtechId;
    }

    public void setBtechId(int btechId) {
        BtechId = btechId;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
