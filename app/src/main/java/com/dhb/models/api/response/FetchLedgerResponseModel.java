package com.dhb.models.api.response;


import com.dhb.models.data.LedgerDetailsModeler;

import java.util.ArrayList;

/**
 * Created by E4904 on 4/26/2017.
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
