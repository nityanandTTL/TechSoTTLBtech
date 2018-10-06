package com.thyrocare.models.api.response;

/**
 * Created by e5209@thyrocare.com on 1/1/18.
 */

public class LeaveAppliedResponseModel {
    int BtechId;
    String Name;
    String LeaveDate;
    String Remarks;

    public LeaveAppliedResponseModel() {
    }

    public int getBtechId() {
        return BtechId;
    }

    public void setBtechId(int btechId) {
        BtechId = btechId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLeaveDate() {
        return LeaveDate;
    }

    public void setLeaveDate(String leaveDate) {
        LeaveDate = leaveDate;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }
}
