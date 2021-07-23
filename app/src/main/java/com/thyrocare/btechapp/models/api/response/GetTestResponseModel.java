package com.thyrocare.btechapp.models.api.response;

public class GetTestResponseModel {


    /**
     * ProcessAt : CPL
     * Response : Success
     * ResID : RES0000
     */

    private String ProcessAt;
    private String Response;
    private String ResID;

    public String getProcessAt() {
        return ProcessAt;
    }

    public void setProcessAt(String ProcessAt) {
        this.ProcessAt = ProcessAt;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String Response) {
        this.Response = Response;
    }

    public String getResID() {
        return ResID;
    }

    public void setResID(String ResID) {
        this.ResID = ResID;
    }
}
