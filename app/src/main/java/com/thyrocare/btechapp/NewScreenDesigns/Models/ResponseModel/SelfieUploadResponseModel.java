package com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel;

public class SelfieUploadResponseModel {

    private String BtechId;
    private String Pic;
    private long timeUploaded;
    private int Flag;
    private String FromDate;
    private String ToDate;

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

    public int getFlag() {
        return Flag;
    }

    public void setFlag(int flag) {
        Flag = flag;
    }

    public String getFromDate() {
        return FromDate;
    }

    public void setFromDate(String fromDate) {
        FromDate = fromDate;
    }

    public String getToDate() {
        return ToDate;
    }

    public void setToDate(String toDate) {
        ToDate = toDate;
    }
}
