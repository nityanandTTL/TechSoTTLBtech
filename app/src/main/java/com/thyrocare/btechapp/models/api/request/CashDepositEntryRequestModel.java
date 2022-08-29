package com.thyrocare.btechapp.models.api.request;

/**
 * Created by Orion on 6/22/2017.<br/>
 * /CashDeposit/CashDepositEntry<br/>
 */

public class CashDepositEntryRequestModel {
    private int Id, BTechId, BankId, ModeId, Amount;//
    private String TransactionId, ChequeNo, Remarks, Image;

    public CashDepositEntryRequestModel() {
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getBTechId() {
        return BTechId;
    }

    public void setBTechId(int BTechId) {
        this.BTechId = BTechId;
    }

    public int getBankId() {
        return BankId;
    }

    public void setBankId(int bankId) {
        BankId = bankId;
    }

    public int getModeId() {
        return ModeId;
    }

    public void setModeId(int modeId) {
        ModeId = modeId;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public String getTransactionId() {
        return TransactionId;
    }

    public void setTransactionId(String transactionId) {
        TransactionId = transactionId;
    }

    public String getChequeNo() {
        return ChequeNo;
    }

    public void setChequeNo(String chequeNo) {
        ChequeNo = chequeNo;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }
}
