package com.thyrocare.btechapp.models.api.request;

public class PEUpdatePatientRequestModel {

    /**
     * userId : 876567483
     * data : {"partner_order_id":"VL422096","patient_details":{"partner_patient_id":"SP71123500","patient_name":"TEST EJ","patient_age":"23","patient_gender":"M","patient_contact":""}}
     */

    private String userId;
    private DataDTO data;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public static class DataDTO {
        /**
         * partner_order_id : VL422096
         * patient_details : {"partner_patient_id":"SP71123500","patient_name":"TEST EJ","patient_age":"23","patient_gender":"M","patient_contact":""}
         */

        private String partner_order_id;
        private PatientDetailsDTO patient_details;

        public String getPartner_order_id() {
            return partner_order_id;
        }

        public void setPartner_order_id(String partner_order_id) {
            this.partner_order_id = partner_order_id;
        }

        public PatientDetailsDTO getPatient_details() {
            return patient_details;
        }

        public void setPatient_details(PatientDetailsDTO patient_details) {
            this.patient_details = patient_details;
        }

        public static class PatientDetailsDTO {
            /**
             * partner_patient_id : SP71123500
             * patient_name : TEST EJ
             * patient_age : 23
             * patient_gender : M
             * patient_contact :
             */

            private String partner_patient_id;
            private String patient_name;
            private String patient_age;
            private String patient_gender;
            private String patient_contact;

            public String getPartner_patient_id() {
                return partner_patient_id;
            }

            public void setPartner_patient_id(String partner_patient_id) {
                this.partner_patient_id = partner_patient_id;
            }

            public String getPatient_name() {
                return patient_name;
            }

            public void setPatient_name(String patient_name) {
                this.patient_name = patient_name;
            }

            public String getPatient_age() {
                return patient_age;
            }

            public void setPatient_age(String patient_age) {
                this.patient_age = patient_age;
            }

            public String getPatient_gender() {
                return patient_gender;
            }

            public void setPatient_gender(String patient_gender) {
                this.patient_gender = patient_gender;
            }

            public String getPatient_contact() {
                return patient_contact;
            }

            public void setPatient_contact(String patient_contact) {
                this.patient_contact = patient_contact;
            }
        }
    }
}
