package com.thyrocare.btechapp.models.api.response;

import java.util.ArrayList;
import java.util.List;

public class PEBenWiseApptSlotResponseModel {


    /**
     * lSlotDataRes : [{"id":"string","slotMasterId":"string","slot":"string"}]
     * response : string
     * respId : string
     */

    private String response;
    private String respId;
    private ArrayList<LSlotDataResDTO> lSlotDataRes;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getRespId() {
        return respId;
    }

    public void setRespId(String respId) {
        this.respId = respId;
    }

    public ArrayList<LSlotDataResDTO> getLSlotDataRes() {
        return lSlotDataRes;
    }

    public void setLSlotDataRes(ArrayList<LSlotDataResDTO> lSlotDataRes) {
        this.lSlotDataRes = lSlotDataRes;
    }

    public static class LSlotDataResDTO {
        /**
         * id : string
         * slotMasterId : string
         * slot : string
         */

        private String id;
        private String slotMasterId;
        private String slot;

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
