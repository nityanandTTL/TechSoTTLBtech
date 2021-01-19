package com.thyrocare.btechapp.models.api.response;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class LeadPurposeResponseModel implements Serializable {


    private String response;
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
    }
}
