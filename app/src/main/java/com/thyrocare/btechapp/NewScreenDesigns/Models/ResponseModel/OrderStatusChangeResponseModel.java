package com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderStatusChangeResponseModel {

    @JsonProperty("Status")
    private String status;
    @JsonProperty("StatusId")
    private String statusId;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }
}
