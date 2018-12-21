package com.thyrocare.models.api.request;

/**
 * Created by Orion on 4/20/2017.
 */

public class SelfieUploadRequestModel {
    private String BtechId;
    private String Pic;

    public String getBtechId() {
        return BtechId;
    }

    public void setBtechId(String btechId) {
        BtechId = btechId;
    }

    public String getPic() {
        return Pic;
    }

    public void setPic(String pic) {
        Pic = pic;
    }

}
