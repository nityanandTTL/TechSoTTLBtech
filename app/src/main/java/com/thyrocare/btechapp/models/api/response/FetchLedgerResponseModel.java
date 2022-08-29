package com.thyrocare.btechapp.models.api.response;


import com.thyrocare.btechapp.models.data.LedgerDetailsModeler;

import java.util.ArrayList;

/**
 * Created by Orion on 4/26/2017.<br/>
 * /Ledger/CashRegister<br/>
 */

public class FetchLedgerResponseModel {
    private int BTechId;
    private String BTechName;
    private int OutstandingBalance;
    private ArrayList<LedgerDetailsModeler> LedgerDetails;

    public ArrayList<LedgerDetailsModeler> getLedgerDetails() {
        return LedgerDetails;
    }

    public void setLedgerDetails(ArrayList<LedgerDetailsModeler> Ledgerdetails) {
        this.LedgerDetails = Ledgerdetails;
    }

    public int getBTechId() {
        return BTechId;
    }

    public void setBTechId(int BTechId) {
        this.BTechId = BTechId;
    }

    public String getBTechName() {
        return BTechName;
    }

    public void setBTechName(String BTechName) {
        this.BTechName = BTechName;
    }

    public int getOutstandingBalance() {
        return OutstandingBalance;
    }

    public void setOutstandingBalance(int outstandingBalance) {
        OutstandingBalance = outstandingBalance;
    }
}
