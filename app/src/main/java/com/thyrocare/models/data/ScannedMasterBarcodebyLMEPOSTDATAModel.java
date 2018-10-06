package com.thyrocare.models.data;

/**
 * Created by e5233@thyrocare.com on 29/3/18.
 */

public class ScannedMasterBarcodebyLMEPOSTDATAModel {

    private String MasterBarcode;
    private String SampleDropIds;
    private String Status;
    private String Latitude;
    private String Longitude;

    public String getMasterBarcode() {
        return MasterBarcode;
    }

    public void setMasterBarcode(String masterBarcode) {
        MasterBarcode = masterBarcode;
    }

    public String getSampleDropIds() {
        return SampleDropIds;
    }

    public void setSampleDropIds(String sampleDropIds) {
        SampleDropIds = sampleDropIds;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }
}
