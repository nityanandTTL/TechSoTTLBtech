package com.thyrocare.btechapp.models.api.response;

/**
 * Created by Orion on 6/22/2017.
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
