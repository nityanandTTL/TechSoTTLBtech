package com.thyrocare.btechapp.models.api.request;

import java.io.File;

public class BTechSelfieRequestModel {
    String Btechid, ORDERNO;
    File file;

    public String getBtechid() {
        return Btechid;
    }

    public void setBtechid(String btechid) {
        Btechid = btechid;
    }

    public String getORDERNO() {
        return ORDERNO;
    }

    public void setORDERNO(String ORDERNO) {
        this.ORDERNO = ORDERNO;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
