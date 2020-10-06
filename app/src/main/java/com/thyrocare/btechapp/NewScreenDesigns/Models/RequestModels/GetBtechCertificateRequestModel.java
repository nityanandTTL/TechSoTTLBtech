package com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels;

import java.io.Serializable;

public class GetBtechCertificateRequestModel implements Serializable {

    String BtechId;
    String DTLType;

    public String getBtechId() {
        return BtechId;
    }

    public void setBtechId(String btechId) {
        BtechId = btechId;
    }

    public String getDTLType() {
        return DTLType;
    }

    public void setDTLType(String DTLType) {
        this.DTLType = DTLType;
    }
}
