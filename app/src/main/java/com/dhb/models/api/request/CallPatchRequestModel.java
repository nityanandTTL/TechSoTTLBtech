package com.dhb.models.api.request;

/**
 * Created by Orion on 4/20/2017.
 */

public class CallPatchRequestModel {
    String Srcnumber;
    String VisitID;
    String DestNumber;

    public String getSrcnumber() {
        return Srcnumber;
    }

    public void setSrcnumber(String srcnumber) {
        Srcnumber = srcnumber;
    }

    public String getVisitID() {
        return VisitID;
    }

    public void setVisitID(String visitID) {
        VisitID = visitID;
    }

    public String getDestNumber() {
        return DestNumber;
    }

    public void setDestNumber(String destNumber) {
        DestNumber = destNumber;
    }
}
