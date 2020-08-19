package com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel;

public class Get_deposite_details_model {

    /**
     * Date : 01-02-2020
     * Amount : 13869
     * Remarks : BTECH PAYMENTS
     */

    private String Date;
    private int Amount;
    private String Remarks;

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int Amount) {
        this.Amount = Amount;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String Remarks) {
        this.Remarks = Remarks;
    }
}
