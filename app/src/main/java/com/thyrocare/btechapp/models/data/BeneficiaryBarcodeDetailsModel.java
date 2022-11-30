package com.thyrocare.btechapp.models.data;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

/**
 * Created by Orion on 4/19/2017.
 */

public class BeneficiaryBarcodeDetailsModel extends BaseModel implements Parcelable {

    public static final Creator<BeneficiaryBarcodeDetailsModel> CREATOR = new Creator<BeneficiaryBarcodeDetailsModel>() {
        @Override
        public BeneficiaryBarcodeDetailsModel createFromParcel(Parcel in) {
            return new BeneficiaryBarcodeDetailsModel(in);
        }

        @Override
        public BeneficiaryBarcodeDetailsModel[] newArray(int size) {
            return new BeneficiaryBarcodeDetailsModel[size];
        }
    };
    private String id;
    private int BenId;
    private String OrderNo;
    private String Barcode;
    private String SamplType;
    private String SCT;
    private String RescanBarcode;
    private int LabAlert;
    private String ClinicalHistory;
    private int ProcessAt;
    private boolean isRBS_PPBS;
    private String benCode;
    private Boolean isBencodeCorrect = null;

    public Boolean getIsBenCodeCorrect(){
        return isBencodeCorrect;
    }
    public void setIsBenCodeCorrect(Boolean isBencodeCorrect){
        this.isBencodeCorrect = isBencodeCorrect;
    }

    public String getBenCode() {
        return benCode;
    }

    public void setBenCode(String benCode) {
        this.benCode = benCode;
    }




    public BeneficiaryBarcodeDetailsModel() {
        super();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected BeneficiaryBarcodeDetailsModel(Parcel in) {
        super(in);
        id = in.readString();
        BenId = in.readInt();
        OrderNo = in.readString();
        Barcode = in.readString();
        SamplType = in.readString();
        SCT = in.readString();
        RescanBarcode = in.readString();
        LabAlert = in.readInt();
        ClinicalHistory = in.readString();
        benCode = in.readString();
        isBencodeCorrect = in.readBoolean();
        ProcessAt = in.readInt();
        isRBS_PPBS = in.readByte() != 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(id);
        dest.writeInt(BenId);
        dest.writeString(OrderNo);
        dest.writeString(Barcode);
        dest.writeString(SamplType);
        dest.writeString(SCT);
        dest.writeString(RescanBarcode);
        dest.writeInt(LabAlert);
        dest.writeString(ClinicalHistory);
        dest.writeString(benCode);
        dest.writeBoolean(isBencodeCorrect);
        dest.writeInt(ProcessAt);
        dest.writeByte((byte) (isRBS_PPBS ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getRescanBarcode() {
        return RescanBarcode;
    }

    public void setRescanBarcode(String rescanBarcode) {
        RescanBarcode = rescanBarcode;
    }

    @Override
    public String toString() {
        return /*SamplType*/Barcode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getBenId() {
        return BenId;
    }

    public void setBenId(int benId) {
        BenId = benId;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String barcode) {
        Barcode = barcode;
    }

    public String getSamplType() {
        return SamplType;
    }

    public void setSamplType(String samplType) {
        SamplType = samplType;
    }

    public String getSCT() {
        return SCT;
    }

    public void setSCT(String SCT) {
        this.SCT = SCT;
    }

    public int getLabAlert() {
        return LabAlert;
    }

    public void setLabAlert(int labAlert) {
        LabAlert = labAlert;
    }

    public String getClinicalHistory() {
        return ClinicalHistory;
    }

    public void setClinicalHistory(String clinicalHistory) {
        ClinicalHistory = clinicalHistory;
    }

    public int getProcessAt() {
        return ProcessAt;
    }

    public void setProcessAt(int processAt) {
        ProcessAt = processAt;
    }

    public boolean isRBS_PPBS() {
        return isRBS_PPBS;
    }

    public void setRBS_PPBS(boolean RBS_PPBS) {
        isRBS_PPBS = RBS_PPBS;
    }
}
