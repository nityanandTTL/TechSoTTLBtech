package com.thyrocare.btechapp.models.api.response;

import java.util.List;


public class PickupOrderResponseModel {


    /**
     * pickuporders : [{"OrderNo":"VLD1542","Name":"Sachin Mejari","Mobile":"7738943013","Address":"Test entry please ignore","Pincode":"421202","ApptDay":0,"Appointment":"2021-06-04 10:00"},{"OrderNo":"VLD6547","Name":"Test Entry","Mobile":"7738943013","Address":"Test entry please ignore","Pincode":"421202","ApptDay":1,"Appointment":"2021-06-05 08:00"}]
     * RespId : RES000
     * Response : SUCCESS
     */

    private List<PickupordersDTO> pickuporders;
    private String RespId;
    private String Response;


    public List<PickupordersDTO> getPickuporders() {
        return pickuporders;
    }

    public void setPickuporders(List<PickupordersDTO> pickuporders) {
        this.pickuporders = pickuporders;
    }

    public String getRespId() {
        return RespId;
    }

    public void setRespId(String respId) {
        RespId = respId;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public static class PickupordersDTO {
        /**
         * OrderNo : VLD1542
         * Name : Sachin Mejari
         * Mobile : 7738943013
         * Address : Test entry please ignore
         * Pincode : 421202
         * ApptDay : 0
         * Appointment : 2021-06-04 10:00
         */

        private String OrderNo;
        private String Name;
        private String Mobile;
        private String Address;
        private String Pincode;
        private int ApptDay;
        private String Appointment;

        public String getOrderNo() {
            return OrderNo;
        }

        public void setOrderNo(String orderNo) {
            OrderNo = orderNo;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getMobile() {
            return Mobile;
        }

        public void setMobile(String mobile) {
            Mobile = mobile;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String address) {
            Address = address;
        }

        public String getPincode() {
            return Pincode;
        }

        public void setPincode(String pincode) {
            Pincode = pincode;
        }

        public int getApptDay() {
            return ApptDay;
        }

        public void setApptDay(int apptDay) {
            ApptDay = apptDay;
        }

        public String getAppointment() {
            return Appointment;
        }

        public void setAppointment(String appointment) {
            Appointment = appointment;
        }
    }
}
