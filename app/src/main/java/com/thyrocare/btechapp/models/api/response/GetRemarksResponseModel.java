package com.thyrocare.btechapp.models.api.response;

public class GetRemarksResponseModel {


    /**
     * Id : 108
     * Remarks : Customer asked to reschedule
     * ReCallRemarksId : 0
     */

    private Integer Id;
    private String Remarks;
    private String ReCallRemarksId;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer Id) {
        this.Id = Id;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String Remarks) {
        this.Remarks = Remarks;
    }

    public String getReCallRemarksId() {
        return ReCallRemarksId;
    }

    public void setReCallRemarksId(String ReCallRemarksId) {
        this.ReCallRemarksId = ReCallRemarksId;
    }
}
