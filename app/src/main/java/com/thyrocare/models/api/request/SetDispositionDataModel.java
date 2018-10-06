package com.thyrocare.models.api.request;

/**
 * Created by e5233@thyrocare.com on 27/6/18.
 */

public class SetDispositionDataModel {

    private String OrderNo;
    private String FrmNo;
    private String ToNo;
    private String UserId;
    private int DispId;
    private int AppId;
    private String Remarks;

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getFrmNo() {
        return FrmNo;
    }

    public void setFrmNo(String frmNo) {
        FrmNo = frmNo;
    }

    public String getToNo() {
        return ToNo;
    }

    public void setToNo(String toNo) {
        ToNo = toNo;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public int getDispId() {
        return DispId;
    }

    public void setDispId(int dispId) {
        DispId = dispId;
    }

    public int getAppId() {
        return AppId;
    }

    public void setAppId(int appId) {
        AppId = appId;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }
}
