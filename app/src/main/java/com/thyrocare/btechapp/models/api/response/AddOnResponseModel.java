package com.thyrocare.btechapp.models.api.response;

public class AddOnResponseModel {

    /**
     * status : true
     * data : {"parent_order_id":25913,"pe_order_id":"D25975","order_id":25975,"is_payment_done":false}
     * error : null
     */

    private Boolean status;
    private DataDTO data;
    private Object error;

    public Boolean isStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public static class DataDTO {
        /**
         * parent_order_id : 25913
         * pe_order_id : D25975
         * order_id : 25975
         * is_payment_done : false
         */

        private Integer parent_order_id;
        private String pe_order_id;
        private Integer order_id;
        private Boolean is_payment_done;

        public Integer getParent_order_id() {
            return parent_order_id;
        }

        public void setParent_order_id(Integer parent_order_id) {
            this.parent_order_id = parent_order_id;
        }

        public String getPe_order_id() {
            return pe_order_id;
        }

        public void setPe_order_id(String pe_order_id) {
            this.pe_order_id = pe_order_id;
        }

        public Integer getOrder_id() {
            return order_id;
        }

        public void setOrder_id(Integer order_id) {
            this.order_id = order_id;
        }

        public Boolean isIs_payment_done() {
            return is_payment_done;
        }

        public void setIs_payment_done(Boolean is_payment_done) {
            this.is_payment_done = is_payment_done;
        }
    }
}
