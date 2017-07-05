package com.dhb.models.api.request;

/**
 * Created by E4991 on 7/4/2017.
 */

public class Tsp_Send_RequestModel {

    private String ConsignId;
    private int TSP;
    private String RoutingMode;
    private String Mode;
    private String ConsignTime;
    private String Barcode;
    private int CPL;
    private int RPL;
    private String Instructions;
    private String Remarks;


    @Override
    public String toString() {
        return "Tsp_Send_RequestModel{" +
                "ConsignId='" + ConsignId + '\'' +
                ", TSP=" + TSP +
                ", RoutingMode='" + RoutingMode + '\'' +
                ", Mode='" + Mode + '\'' +
                ", ConsignTime='" + ConsignTime + '\'' +
                ", Barcode='" + Barcode + '\'' +
                ", CPL=" + CPL +
                ", RPL=" + RPL +
                ", Instructions='" + Instructions + '\'' +
                ", Remarks='" + Remarks + '\'' +
                '}';
    }

    public String getConsignId() {
        return ConsignId;
    }

    public void setConsignId(String consignId) {
        ConsignId = consignId;
    }

    public int getTSP() {
        return TSP;
    }

    public void setTSP(int TSP) {
        this.TSP = TSP;
    }

    public String getRoutingMode() {
        return RoutingMode;
    }

    public void setRoutingMode(String routingMode) {
        RoutingMode = routingMode;
    }

    public String getMode() {
        return Mode;
    }

    public void setMode(String mode) {
        Mode = mode;
    }

    public String getConsignTime() {
        return ConsignTime;
    }

    public void setConsignTime(String consignTime) {
        ConsignTime = consignTime;
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String barcode) {
        Barcode = barcode;
    }

    public int getCPL() {
        return CPL;
    }

    public void setCPL(int CPL) {
        this.CPL = CPL;
    }

    public int getRPL() {
        return RPL;
    }

    public void setRPL(int RPL) {
        this.RPL = RPL;
    }

    public String getInstructions() {
        return Instructions;
    }

    public void setInstructions(String instructions) {
        Instructions = instructions;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }
}
