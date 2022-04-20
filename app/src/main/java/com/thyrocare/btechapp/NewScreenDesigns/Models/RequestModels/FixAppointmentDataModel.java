package com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels;

public class FixAppointmentDataModel {
    private String VisitId;
    private int Status;
    private int ReasonId;
    private int RemarksId;
    private int UserId;
    private int BTechId;
    private String AppointmentDate;
    private int AppointmentSlot;
    private String OrderNo;
    private String Address1;
    private String Address2;
    private int Pincode1;
    private int Pincode2;
    private String Lattitude;
    private String Longitude;
    private String Others;
    private boolean VipOrder;
    private String CallBackDate;

    public String getVisitId() {
        return VisitId;
    }

    public void setVisitId(String visitId) {
        VisitId = visitId;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getReasonId() {
        return ReasonId;
    }

    public void setReasonId(int reasonId) {
        ReasonId = reasonId;
    }

    public int getRemarksId() {
        return RemarksId;
    }

    public void setRemarksId(int remarksId) {
        RemarksId = remarksId;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getBTechId() {
        return BTechId;
    }

    public void setBTechId(int BTechId) {
        this.BTechId = BTechId;
    }

    public String getAppointmentDate() {
        return AppointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        AppointmentDate = appointmentDate;
    }

    public int getAppointmentSlot() {
        return AppointmentSlot;
    }

    public void setAppointmentSlot(int appointmentSlot) {
        AppointmentSlot = appointmentSlot;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getAddress1() {
        return Address1;
    }

    public void setAddress1(String address1) {
        Address1 = address1;
    }

    public String getAddress2() {
        return Address2;
    }

    public void setAddress2(String address2) {
        Address2 = address2;
    }

    public int getPincode1() {
        return Pincode1;
    }

    public void setPincode1(int pincode1) {
        Pincode1 = pincode1;
    }

    public int getPincode2() {
        return Pincode2;
    }

    public void setPincode2(int pincode2) {
        Pincode2 = pincode2;
    }

    public String getLattitude() {
        return Lattitude;
    }

    public void setLattitude(String lattitude) {
        Lattitude = lattitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getOthers() {
        return Others;
    }

    public void setOthers(String others) {
        Others = others;
    }

    public boolean isVipOrder() {
        return VipOrder;
    }

    public void setVipOrder(boolean vipOrder) {
        VipOrder = vipOrder;
    }

    public String getCallBackDate() {
        return CallBackDate;
    }

    public void setCallBackDate(String callBackDate) {
        CallBackDate = callBackDate;
    }
}
