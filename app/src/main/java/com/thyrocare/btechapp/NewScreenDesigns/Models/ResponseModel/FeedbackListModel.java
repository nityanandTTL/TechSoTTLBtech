package com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel;

import java.util.List;

public class FeedbackListModel {

    /**
     * MASTER : [{"VALUE":"Service"},{"VALUE":"Mobile Application"},{"VALUE":"Cost"},{"VALUE":"Quality"},{"VALUE":"Speed"},{"VALUE":"Over All"}]
     * RESPONSE : SUCCESS
     * RES_ID : RES0000
     */

    private String RESPONSE;
    private String RES_ID;
    private List<MASTERBean> MASTER;

    public String getRESPONSE() {
        return RESPONSE;
    }

    public void setRESPONSE(String RESPONSE) {
        this.RESPONSE = RESPONSE;
    }

    public String getRES_ID() {
        return RES_ID;
    }

    public void setRES_ID(String RES_ID) {
        this.RES_ID = RES_ID;
    }

    public List<MASTERBean> getMASTER() {
        return MASTER;
    }

    public void setMASTER(List<MASTERBean> MASTER) {
        this.MASTER = MASTER;
    }

    public static class MASTERBean {
        /**
         * VALUE : Service
         */

        private String VALUE;

        public String getVALUE() {
            return VALUE;
        }

        public void setVALUE(String VALUE) {
            this.VALUE = VALUE;
        }
    }
}
