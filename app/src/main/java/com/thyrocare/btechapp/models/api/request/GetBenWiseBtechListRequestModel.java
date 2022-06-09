package com.thyrocare.btechapp.models.api.request;

public class GetBenWiseBtechListRequestModel {


    /**
     * apptDate : 2022-05-16
     * ecode : 0396E
     * pincode : 400702
     * visitId : VL98765434
     * product : TSH
     * projectId : NA
     * benCount : 2
     */

    private String apptDate;
    private String ecode;
    private String pincode;
    private String visitId;
    private String product;
    private String projectId;
    private Integer benCount;

    public String getApptDate() {
        return apptDate;
    }

    public void setApptDate(String apptDate) {
        this.apptDate = apptDate;
    }

    public String getEcode() {
        return ecode;
    }

    public void setEcode(String ecode) {
        this.ecode = ecode;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getVisitId() {
        return visitId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Integer getBenCount() {
        return benCount;
    }

    public void setBenCount(Integer benCount) {
        this.benCount = benCount;
    }
}
