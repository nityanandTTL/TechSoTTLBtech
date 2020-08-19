package com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel;

public class Get_BtechEarning_Model {

    /**
     * BtechId : 884544334
     * BtechName : Tejas Telawane
     * FastingOrders : 4
     * NonFastingOrders : 1
     * TotalOrders : 0
     * TotalEarning : 1085
     * CreditDate : null
     * CreditedAmount : 0
     */

    private int BtechId;
    private String BtechName;
    private int FastingOrders;
    private int NonFastingOrders;
    private int TotalOrders;
    private int TotalEarning;
    private String CreditDate;
    private int CreditedAmount;

    public int getBtechId() {
        return BtechId;
    }

    public void setBtechId(int BtechId) {
        this.BtechId = BtechId;
    }

    public String getBtechName() {
        return BtechName;
    }

    public void setBtechName(String BtechName) {
        this.BtechName = BtechName;
    }

    public int getFastingOrders() {
        return FastingOrders;
    }

    public void setFastingOrders(int FastingOrders) {
        this.FastingOrders = FastingOrders;
    }

    public int getNonFastingOrders() {
        return NonFastingOrders;
    }

    public void setNonFastingOrders(int NonFastingOrders) {
        this.NonFastingOrders = NonFastingOrders;
    }

    public int getTotalOrders() {
        return TotalOrders;
    }

    public void setTotalOrders(int TotalOrders) {
        this.TotalOrders = TotalOrders;
    }

    public int getTotalEarning() {
        return TotalEarning;
    }

    public void setTotalEarning(int TotalEarning) {
        this.TotalEarning = TotalEarning;
    }

    public String getCreditDate() {
        return CreditDate;
    }

    public void setCreditDate(String creditDate) {
        CreditDate = creditDate;
    }

    public int getCreditedAmount() {
        return CreditedAmount;
    }

    public void setCreditedAmount(int CreditedAmount) {
        this.CreditedAmount = CreditedAmount;
    }
}
