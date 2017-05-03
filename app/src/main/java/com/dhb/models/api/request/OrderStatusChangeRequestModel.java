package com.dhb.models.api.request;

/**
 * Created by vendor1 on 4/24/2017.
 */

public class OrderStatusChangeRequestModel {
    String Remarks;

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    String Id;
    int Status;
}
