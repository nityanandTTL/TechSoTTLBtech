package com.thyrocare.btechapp.models.api.request;

public class SendEventRequestModel {


    /**
     * id : 14965850
     * status : 60
     * remarks : test
     * orderno : VL60BB92
     * BtechId : 884543163
     */

    private Integer id;
    private Integer status;
    private String remarks;
    private String orderno;
    private String BtechId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getBtechId() {
        return BtechId;
    }

    public void setBtechId(String BtechId) {
        this.BtechId = BtechId;
    }
}
