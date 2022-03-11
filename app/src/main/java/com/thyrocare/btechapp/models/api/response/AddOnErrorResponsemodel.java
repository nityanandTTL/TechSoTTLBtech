package com.thyrocare.btechapp.models.api.response;

public class AddOnErrorResponsemodel {

    /**
     * status : false
     * data : null
     * error : Unable to create an add on order due to error: Something went wrong. - The parent order id: 1490421 for partner_order_id: VL566673 was not in phlebo assigned state hence not processing the request further.
     */

    private Boolean status;
    private Object data;
    private String error;

    public Boolean isStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
