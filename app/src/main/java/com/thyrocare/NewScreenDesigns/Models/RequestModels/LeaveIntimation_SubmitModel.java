package com.thyrocare.NewScreenDesigns.Models.RequestModels;

public class LeaveIntimation_SubmitModel {

    /**
     * BtechId : 884544334
     * EnteredBy : 884544334
     * Fromdate : 2020-02-20
     * LeaveType : NA
     * Nature : 3
     * Remarks : test entry kindly ignore this
     * Todate : 2020-02-18
     * days : 1
     */

    private int BtechId;
    private int EnteredBy;
    private String Fromdate;
    private String LeaveType;
    private int Nature;
    private String Remarks;
    private String Todate;
    private int days;

    public int getBtechId() {
        return BtechId;
    }

    public void setBtechId(int BtechId) {
        this.BtechId = BtechId;
    }

    public int getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(int EnteredBy) {
        this.EnteredBy = EnteredBy;
    }

    public String getFromdate() {
        return Fromdate;
    }

    public void setFromdate(String Fromdate) {
        this.Fromdate = Fromdate;
    }

    public String getLeaveType() {
        return LeaveType;
    }

    public void setLeaveType(String LeaveType) {
        this.LeaveType = LeaveType;
    }

    public int getNature() {
        return Nature;
    }

    public void setNature(int Nature) {
        this.Nature = Nature;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String Remarks) {
        this.Remarks = Remarks;
    }

    public String getTodate() {
        return Todate;
    }

    public void setTodate(String Todate) {
        this.Todate = Todate;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }
}
