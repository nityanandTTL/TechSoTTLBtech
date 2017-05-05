package com.dhb.models.api.response;

/**
 * Created by vendor1 on 4/20/2017.
 */

public class SelfieUploadResponseModel {
    private String BtechId;
    private String Pic;
    private long timeUploaded;

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

    public long getTimeUploaded() {
        return timeUploaded;
    }

    public void setTimeUploaded(long timeUploaded) {
        this.timeUploaded = timeUploaded;
    }
}
