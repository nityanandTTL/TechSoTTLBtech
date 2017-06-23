package com.dhb.models.api.response;

/**
 * Created by vendor1 on 6/22/2017.
 */

public class BankMasterResponseModel {
    private String BankName;
    private int BankId;

    public BankMasterResponseModel() {
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public int getBankId() {
        return BankId;
    }

    public void setBankId(int bankId) {
        BankId = bankId;
    }
}
