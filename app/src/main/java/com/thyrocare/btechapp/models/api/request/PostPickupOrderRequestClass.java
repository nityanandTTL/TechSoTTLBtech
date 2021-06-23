package com.thyrocare.btechapp.models.api.request;

public class PostPickupOrderRequestClass {

    String Orderno;
    String Btechid;

    public String getOrderno() {
        return Orderno;
    }

    public void setOrderno(String orderno) {
        Orderno = orderno;
    }

    public String getBtechid() {
        return Btechid;
    }

    public void setBtechid(String btechid) {
        Btechid = btechid;
    }
}
