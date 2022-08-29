package com.thyrocare.btechapp.models.api.response;

public class VerifyCouponResponseModel {
    public Data data;
    String error, code, message;
    Integer status;
    Boolean is_coupon_applied;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getIs_coupon_applied() {
        return is_coupon_applied;
    }

    public void setIs_coupon_applied(Boolean is_coupon_applied) {
        this.is_coupon_applied = is_coupon_applied;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        public Double tests_discount, tests_pe_selling_price;

        public Double getTests_discount() {
            return tests_discount;
        }

        public void setTests_discount(Double tests_discount) {
            this.tests_discount = tests_discount;
        }

        public Double getTests_pe_selling_price() {
            return tests_pe_selling_price;
        }

        public void setTests_pe_selling_price(Double tests_pe_selling_price) {
            this.tests_pe_selling_price = tests_pe_selling_price;
        }
    }
}
