package com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel;

import java.util.List;

public class Get_cash_register_details_Model {

    /**
     * BTechId : 884544334
     * BTechCode : LNINMH4334
     * BTechName : Tejas Telawane
     * OutstandingBalance : 575361
     * LedgerDetails : [{"Date":"13-02-2020","OpeningBal":-560480,"Credit":0,"Debit":0,"ClosingBal":-560480,"Color":null},{"Date":"14-02-2020","OpeningBal":-560480,"Credit":0,"Debit":0,"ClosingBal":-560480,"Color":null},{"Date":"20-02-2020","OpeningBal":-575011,"Credit":0,"Debit":350,"ClosingBal":-575361,"Color":null},{"Date":"15-02-2020","OpeningBal":-560480,"Credit":0,"Debit":0,"ClosingBal":-560480,"Color":null},{"Date":"16-02-2020","OpeningBal":-560480,"Credit":0,"Debit":0,"ClosingBal":-560480,"Color":null},{"Date":"17-02-2020","OpeningBal":-560480,"Credit":0,"Debit":7335,"ClosingBal":-567815,"Color":null},{"Date":"18-02-2020","OpeningBal":-567815,"Credit":0,"Debit":7196,"ClosingBal":-575011,"Color":null},{"Date":"19-02-2020","OpeningBal":-575011,"Credit":0,"Debit":0,"ClosingBal":-575011,"Color":null}]
     */

    private int BTechId;
    private String BTechCode;
    private String BTechName;
    private int OutstandingBalance;
    private List<LedgerDetailsBean> LedgerDetails;

    public int getBTechId() {
        return BTechId;
    }

    public void setBTechId(int BTechId) {
        this.BTechId = BTechId;
    }

    public String getBTechCode() {
        return BTechCode;
    }

    public void setBTechCode(String BTechCode) {
        this.BTechCode = BTechCode;
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

    public void setOutstandingBalance(int OutstandingBalance) {
        this.OutstandingBalance = OutstandingBalance;
    }

    public List<LedgerDetailsBean> getLedgerDetails() {
        return LedgerDetails;
    }

    public void setLedgerDetails(List<LedgerDetailsBean> LedgerDetails) {
        this.LedgerDetails = LedgerDetails;
    }

    public static class LedgerDetailsBean {
        /**
         * Date : 13-02-2020
         * OpeningBal : -560480
         * Credit : 0
         * Debit : 0
         * ClosingBal : -560480
         * Color : null
         */

        private String Date;
        private long OpeningBal;
        private long Credit;
        private long Debit;
        private long ClosingBal;
        private Object Color;

        public String getDate() {
            return Date;
        }

        public void setDate(String date) {
            Date = date;
        }

        public long getOpeningBal() {
            return OpeningBal;
        }

        public void setOpeningBal(long openingBal) {
            OpeningBal = openingBal;
        }

        public long getCredit() {
            return Credit;
        }

        public void setCredit(long credit) {
            Credit = credit;
        }

        public long getDebit() {
            return Debit;
        }

        public void setDebit(long debit) {
            Debit = debit;
        }

        public long getClosingBal() {
            return ClosingBal;
        }

        public void setClosingBal(long closingBal) {
            ClosingBal = closingBal;
        }

        public Object getColor() {
            return Color;
        }

        public void setColor(Object color) {
            Color = color;
        }
    }
}
