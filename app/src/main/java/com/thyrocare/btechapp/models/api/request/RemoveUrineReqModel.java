package com.thyrocare.btechapp.models.api.request;

public class RemoveUrineReqModel {


    /**
     * OrderNo : VL762471
     * Remarks :
     * BtechID : 894563248
     * SampleType : URINE
     * benID : 35403373
     */

    private String OrderNo;
    private String Remarks;
    private String BtechID;
    private String SampleType;
    private Integer benID;

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String OrderNo) {
        this.OrderNo = OrderNo;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String Remarks) {
        this.Remarks = Remarks;
    }

    public String getBtechID() {
        return BtechID;
    }

    public void setBtechID(String BtechID) {
        this.BtechID = BtechID;
    }

    public String getSampleType() {
        return SampleType;
    }

    public void setSampleType(String SampleType) {
        this.SampleType = SampleType;
    }

    public Integer getBenID() {
        return benID;
    }

    public void setBenID(Integer benID) {
        this.benID = benID;
    }
}
