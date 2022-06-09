package com.thyrocare.btechapp.models.api.response;

import java.util.ArrayList;

public class GetBenWiseBtechListResponseModel {


    /**
     * respId : RES00001
     * response : Success
     * btechs : [{"btechId":"884543153","btechName":"MAHENDRA BATRA","mobile":"9076021070","id":"19","slotMasterId":"16","slot":"13:30 - 14:30"},{"btechId":"884543153","btechName":"MAHENDRA BATRA","mobile":"9076021070","id":"20","slotMasterId":"16","slot":"14:00 - 15:00"},{"btechId":"884543153","btechName":"MAHENDRA BATRA","mobile":"9076021070","id":"21","slotMasterId":"17","slot":"14:30 - 15:30"},{"btechId":"884543153","btechName":"MAHENDRA BATRA","mobile":"9076021070","id":"22","slotMasterId":"17","slot":"15:00 - 16:00"},{"btechId":"884543153","btechName":"MAHENDRA BATRA","mobile":"9076021070","id":"23","slotMasterId":"18","slot":"15:30 - 16:30"},{"btechId":"884543153","btechName":"MAHENDRA BATRA","mobile":"9076021070","id":"24","slotMasterId":"18","slot":"16:00 - 17:00"},{"btechId":"884543153","btechName":"MAHENDRA BATRA","mobile":"9076021070","id":"25","slotMasterId":"19","slot":"16:30 - 17:30"},{"btechId":"884543153","btechName":"MAHENDRA BATRA","mobile":"9076021070","id":"26","slotMasterId":"19","slot":"17:00 - 18:00"}]
     */

    private String respId;
    private String response;
    private ArrayList<BtechsDTO> btechs;

    public String getRespId() {
        return respId;
    }

    public void setRespId(String respId) {
        this.respId = respId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public ArrayList<BtechsDTO> getBtechs() {
        return btechs;
    }

    public void setBtechs(ArrayList<BtechsDTO> btechs) {
        this.btechs = btechs;
    }

    public static class BtechsDTO {
        /**
         * btechId : 884543153
         * btechName : MAHENDRA BATRA
         * mobile : 9076021070
         * id : 19
         * slotMasterId : 16
         * slot : 13:30 - 14:30
         */

        private String btechId;
        private String btechName;
        private String mobile;
        private String id;
        private String slotMasterId;
        private String slot;

        public String getBtechId() {
            return btechId;
        }

        public void setBtechId(String btechId) {
            this.btechId = btechId;
        }

        public String getBtechName() {
            return btechName;
        }

        public void setBtechName(String btechName) {
            this.btechName = btechName;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSlotMasterId() {
            return slotMasterId;
        }

        public void setSlotMasterId(String slotMasterId) {
            this.slotMasterId = slotMasterId;
        }

        public String getSlot() {
            return slot;
        }

        public void setSlot(String slot) {
            this.slot = slot;
        }
    }
}
