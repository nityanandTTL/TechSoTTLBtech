package com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel;

import java.io.Serializable;
import java.util.ArrayList;

public class GetSSLKeyResponseModel implements Serializable {

    String AppId;
    String RespId;
    String Response;
    ArrayList<lstKeys> lstKeys;

    public String getAppId() {
        return AppId;
    }

    public void setAppId(String appId) {
        AppId = appId;
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

    public ArrayList<GetSSLKeyResponseModel.lstKeys> getLstKeys() {
        return lstKeys;
    }

    public void setLstKeys(ArrayList<GetSSLKeyResponseModel.lstKeys> lstKeys) {
        this.lstKeys = lstKeys;
    }

    public static class lstKeys {

        String Domain;
        String EncodeKey;
        String EncryptPass;
        boolean ForcefullyAllow;
        String SSL_Key;

        public String getDomain() {
            return Domain;
        }

        public void setDomain(String domain) {
            Domain = domain;
        }

        public String getEncodeKey() {
            return EncodeKey;
        }

        public void setEncodeKey(String encodeKey) {
            EncodeKey = encodeKey;
        }

        public String getEncryptPass() {
            return EncryptPass;
        }

        public void setEncryptPass(String encryptPass) {
            EncryptPass = encryptPass;
        }


        public boolean isForcefullyAllow() {
            return ForcefullyAllow;
        }

        public void setForcefullyAllow(boolean forcefullyAllow) {
            ForcefullyAllow = forcefullyAllow;
        }

        public String getSSL_Key() {
            return SSL_Key;
        }

        public void setSSL_Key(String SSL_Key) {
            this.SSL_Key = SSL_Key;
        }
    }
}
