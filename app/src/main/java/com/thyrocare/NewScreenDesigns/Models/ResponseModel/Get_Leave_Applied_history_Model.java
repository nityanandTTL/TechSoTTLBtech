package com.thyrocare.NewScreenDesigns.Models.ResponseModel;

public class Get_Leave_Applied_history_Model {

    /**
     * BtechId : 884544334
     * Name : Tejas Telawane
     * LeaveDate : 2020-02-20T00:00:00
     * Remarks : NA
     */

    private int BtechId;
    private String Name;
    private String LeaveDate;
    private String Remarks;

    public int getBtechId() {
        return BtechId;
    }

    public void setBtechId(int BtechId) {
        this.BtechId = BtechId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getLeaveDate() {
        return LeaveDate;
    }

    public void setLeaveDate(String LeaveDate) {
        this.LeaveDate = LeaveDate;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String Remarks) {
        this.Remarks = Remarks;
    }
}
