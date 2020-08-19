package com.thyrocare.btechapp.models.api.request;

/**
 * Created by Orion on 5/6/2017.
 */

public class ApplyLeaveRequestModel {
    public Integer getBtechId() {
        return BtechId;
    }

    public void setBtechId(Integer btechId) {
        BtechId = btechId;
    }

    public String getLeaveType() {
        return LeaveType;
    }

    public void setLeaveType(String leaveType) {
        LeaveType = leaveType;
    }



    public Integer getNature() {
        return Nature;
    }

    public void setNature(Integer nature) {
        Nature = nature;
    }



    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getFromdate() {
        return Fromdate;
    }

    public void setFromdate(String fromdate) {
        Fromdate = fromdate;
    }

    public String getTodate() {
        return Todate;
    }

    public void setTodate(String todate) {
        Todate = todate;
    }

    public Integer getEnteredBy() {
        return EnteredBy;
    }

    public void setEnteredBy(Integer enteredBy) {
        EnteredBy = enteredBy;
    }

    Integer BtechId;
    String LeaveType;
    String Fromdate;
    String Todate;
    Integer Nature;
    long days;

    public long getDays() {
        return days;
    }

    public void setDays(long days) {
        this.days = days;
    }

    String Remarks;
  Integer EnteredBy;


}
