package com.dhb.models.api.request;

import android.os.Parcel;
import android.os.Parcelable;
import com.dhb.models.data.Tsp_ScanBarcodeDataModel;

import java.util.ArrayList;

/**
 * Created by E4991 on 7/4/2017.
 */

public class Tsp_Send_RequestModel implements Parcelable{

    private String ConsignId;
    private int TSP;
    private String RoutingMode;
    private String Mode;
    private String ConsignTime;
    //private String Barcode;
    private ArrayList<Tsp_ScanBarcodeDataModel> tstBarcode;
    private int CPL;
    private int RPL;
    private String Instructions;
    private String Remarks;


    public Tsp_Send_RequestModel(Parcel in) {
        ConsignId = in.readString();
        TSP = in.readInt();
        RoutingMode = in.readString();
        Mode = in.readString();
        ConsignTime = in.readString();
        tstBarcode = in.createTypedArrayList(Tsp_ScanBarcodeDataModel.CREATOR);
        CPL = in.readInt();
        RPL = in.readInt();
        Instructions = in.readString();
        Remarks = in.readString();
    }

    public static final Creator<Tsp_Send_RequestModel> CREATOR = new Creator<Tsp_Send_RequestModel>() {
        @Override
        public Tsp_Send_RequestModel createFromParcel(Parcel in) {
            return new Tsp_Send_RequestModel(in);
        }

        @Override
        public Tsp_Send_RequestModel[] newArray(int size) {
            return new Tsp_Send_RequestModel[size];
        }
    };

    public Tsp_Send_RequestModel() {

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

    public ArrayList<Tsp_ScanBarcodeDataModel> getTstBarcode() {
        return tstBarcode;
    }

    public void setTstBarcode(ArrayList<Tsp_ScanBarcodeDataModel> tstBarcode) {
        this.tstBarcode = tstBarcode;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ConsignId);
        dest.writeInt(TSP);
        dest.writeString(RoutingMode);
        dest.writeString(Mode);
        dest.writeString(ConsignTime);
        dest.writeTypedList(tstBarcode);
        dest.writeInt(CPL);
        dest.writeInt(RPL);
        dest.writeString(Instructions);
        dest.writeString(Remarks);
    }
}
