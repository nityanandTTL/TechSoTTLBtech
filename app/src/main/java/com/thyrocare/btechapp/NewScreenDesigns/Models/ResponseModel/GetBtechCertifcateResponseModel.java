package com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel;

import java.io.Serializable;
import java.util.ArrayList;

public class GetBtechCertifcateResponseModel implements Serializable {

    String BtechId;
    String CRAT;
    String Response;
    String RespId;

    ArrayList<Certificatess> Certificates;

    public String getBtechId() {
        return BtechId;
    }

    public void setBtechId(String btechId) {
        BtechId = btechId;
    }

    public String getCRAT() {
        return CRAT;
    }

    public void setCRAT(String CRAT) {
        this.CRAT = CRAT;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public String getRespId() {
        return RespId;
    }

    public void setRespId(String respId) {
        RespId = respId;
    }

    public ArrayList<Certificatess> getCertificates() {
        return Certificates;
    }

    public void setCertificates(ArrayList<Certificatess> certificates) {
        Certificates = certificates;
    }

    public static class Certificatess {
        String Certificate;

        public String getCertificate() {
            return Certificate;
        }

        public void setCertificate(String certificate) {
            Certificate = certificate;
        }
    }
}
