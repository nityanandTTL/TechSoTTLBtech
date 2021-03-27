package com.thyrocare.btechapp.models.data;

public class HCWResponseModel {

    /**
     * Status : RESP0000
     * Msg : Successful
     * Result : Link
     */

    private String Status;
    private String Msg;
    private String Result;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String Result) {
        this.Result = Result;
    }
}
