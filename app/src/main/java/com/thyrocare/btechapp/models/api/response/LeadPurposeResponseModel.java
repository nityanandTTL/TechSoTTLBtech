package com.thyrocare.btechapp.models.api.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LeadPurposeResponseModel {
    /**
     * response : Success
     * respId : RES00001
     * purposeList : [{"data":"Order","remarks":null},{"data":"Leggy","remarks":null},{"data":"TSP","remarks":null},{"data":"HVC","remarks":null}]
     */

    private String response;
    private String respId;
    private ArrayList<PurposeListDTO> purposeList;

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

    public ArrayList<PurposeListDTO> getPurposeList() {
        return purposeList;
    }

    public void setPurposeList(ArrayList<PurposeListDTO> purposeList) {
        this.purposeList = purposeList;
    }

    public static class PurposeListDTO {
        /**
         * data : Order
         * remarks : null
         */

        private String data;
        private String remarks;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }
    }


    /*private String response;
    private String respId;
    private List<PurposeListBean> purposeList;

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

    public List<PurposeListBean> getPurposeList() {
        return purposeList;
    }

    public void setPurposeList(List<PurposeListBean> purposeList) {
        this.purposeList = purposeList;
    }

    public static class PurposeListBean {

        private String data;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        @NonNull
        @Override
        public String toString() {
            return data;
        }
    }*/
}
