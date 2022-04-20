package com.thyrocare.btechapp.models.api.request;

import java.util.ArrayList;

public class PEOrderEditRequestModel {


    /**
     * data : {"event":"EDIT_TESTS","partner_order_id":"VL8943FF","child_order_id":"","extra_payload":{"event_timestamp":"20/10/2021 14: 00","tests":[{"partner_patient_id":"SP93242222","dos_code":"TSH","type":"test","lab_dos_name":"TSH"},{"partner_patient_id":"SP93242223","dos_code":"PROJ1234","type":"package","lab_dos_name":"Urine Sample"}]}}
     */

    private DataDTO data;

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public static class DataDTO {
        /**
         * event : EDIT_TESTS
         * partner_order_id : VL8943FF
         * child_order_id :
         * extra_payload : {"event_timestamp":"20/10/2021 14: 00","tests":[{"partner_patient_id":"SP93242222","dos_code":"TSH","type":"test","lab_dos_name":"TSH"},{"partner_patient_id":"SP93242223","dos_code":"PROJ1234","type":"package","lab_dos_name":"Urine Sample"}]}
         */

        private String event;
        private String partner_order_id;
        private String child_order_id;
        private String UserId;
        private ExtraPayloadDTO extra_payload;

        public String getEvent() {
            return event;
        }

        public void setEvent(String event) {
            this.event = event;
        }

        public String getPartner_order_id() {
            return partner_order_id;
        }

        public void setPartner_order_id(String partner_order_id) {
            this.partner_order_id = partner_order_id;
        }

        public String getUserId() {
            return UserId;
        }

        public void setUserId(String userId) {
            UserId = userId;
        }

        public String getChild_order_id() {
            return child_order_id;
        }

        public void setChild_order_id(String child_order_id) {
            this.child_order_id = child_order_id;
        }

        public ExtraPayloadDTO getExtra_payload() {
            return extra_payload;
        }

        public void setExtra_payload(ExtraPayloadDTO extra_payload) {
            this.extra_payload = extra_payload;
        }

        public static class ExtraPayloadDTO {
            /**
             * event_timestamp : 20/10/2021 14: 00
             * tests : [{"partner_patient_id":"SP93242222","dos_code":"TSH","type":"test","lab_dos_name":"TSH"},{"partner_patient_id":"SP93242223","dos_code":"PROJ1234","type":"package","lab_dos_name":"Urine Sample"}]
             */

            private String event_timestamp;
            private ArrayList<TestsDTO> tests;

            public String getEvent_timestamp() {
                return event_timestamp;
            }

            public void setEvent_timestamp(String event_timestamp) {
                this.event_timestamp = event_timestamp;
            }

            public ArrayList<TestsDTO> getTests() {
                return tests;
            }

            public void setTests(ArrayList<TestsDTO> tests) {
                this.tests = tests;
            }

            public static class TestsDTO {
                /**
                 * partner_patient_id : SP93242222
                 * dos_code : TSH
                 * type : test
                 * lab_dos_name : TSH
                 */

                private String partner_patient_id;
                private String dos_code;
                private String type;
                private String lab_dos_name;
                private int price;

                public int getPrice() {
                    return price;
                }

                public void setPrice(int price) {
                    this.price = price;
                }

                public String getPartner_patient_id() {
                    return partner_patient_id;
                }

                public void setPartner_patient_id(String partner_patient_id) {
                    this.partner_patient_id = partner_patient_id;
                }

                public String getDos_code() {
                    return dos_code;
                }

                public void setDos_code(String dos_code) {
                    this.dos_code = dos_code;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getLab_dos_name() {
                    return lab_dos_name;
                }

                public void setLab_dos_name(String lab_dos_name) {
                    this.lab_dos_name = lab_dos_name;
                }
            }
        }
    }
}
