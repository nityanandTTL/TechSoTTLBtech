package com.thyrocare.btechapp.models.api.response;

/**
 * Created by Orion on 4/28/2017.
 */

public class EraningRegisterResponseModel {

    String Date;
    Integer Amount;
    String Remarks;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public Integer getAmount() {
        return Amount;
    }

    public void setAmount(Integer amount) {
        Amount = amount;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }


}
