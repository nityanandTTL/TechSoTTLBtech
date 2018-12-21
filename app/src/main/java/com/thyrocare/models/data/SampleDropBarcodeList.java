package com.thyrocare.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by e5233@thyrocare.com on 13/8/18.
 */

public class SampleDropBarcodeList  implements Parcelable {
    private int SampleDropId;
    private String MasterBarcode;
    private String ScannedBarcode;
    private int SampleCount;
    private int Batch;
    private int Status;
    private String EntryDate;
    private String LastUpdated;
    private String StartedAt;
    private String CompletedAt;
    private String ArrivedAt;
    private boolean IsScanned;

    protected SampleDropBarcodeList(Parcel in) {
        SampleDropId = in.readInt();
        MasterBarcode = in.readString();
        ScannedBarcode = in.readString();
        SampleCount = in.readInt();
        Batch = in.readInt();
        Status = in.readInt();
        EntryDate = in.readString();
        LastUpdated = in.readString();
        StartedAt = in.readString();
        CompletedAt = in.readString();
        ArrivedAt = in.readString();
        IsScanned = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(SampleDropId);
        dest.writeString(MasterBarcode);
        dest.writeString(ScannedBarcode);
        dest.writeInt(SampleCount);
        dest.writeInt(Batch);
        dest.writeInt(Status);
        dest.writeString(EntryDate);
        dest.writeString(LastUpdated);
        dest.writeString(StartedAt);
        dest.writeString(CompletedAt);
        dest.writeString(ArrivedAt);
        dest.writeByte((byte) (IsScanned ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SampleDropBarcodeList> CREATOR = new Creator<SampleDropBarcodeList>() {
        @Override
        public SampleDropBarcodeList createFromParcel(Parcel in) {
            return new SampleDropBarcodeList(in);
        }

        @Override
        public SampleDropBarcodeList[] newArray(int size) {
            return new SampleDropBarcodeList[size];
        }
    };

    public int getSampleDropId() {
        return SampleDropId;
    }

    public void setSampleDropId(int sampleDropId) {
        SampleDropId = sampleDropId;
    }

    public String getMasterBarcode() {
        return MasterBarcode;
    }

    public void setMasterBarcode(String masterBarcode) {
        MasterBarcode = masterBarcode;
    }

    public String getScannedBarcode() {
        return ScannedBarcode;
    }

    public void setScannedBarcode(String scannedBarcode) {
        ScannedBarcode = scannedBarcode;
    }

    public int getSampleCount() {
        return SampleCount;
    }

    public void setSampleCount(int sampleCount) {
        SampleCount = sampleCount;
    }

    public int getBatch() {
        return Batch;
    }

    public void setBatch(int batch) {
        Batch = batch;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getEntryDate() {
        return EntryDate;
    }

    public void setEntryDate(String entryDate) {
        EntryDate = entryDate;
    }

    public String getLastUpdated() {
        return LastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        LastUpdated = lastUpdated;
    }

    public String getStartedAt() {
        return StartedAt;
    }

    public void setStartedAt(String startedAt) {
        StartedAt = startedAt;
    }

    public String getCompletedAt() {
        return CompletedAt;
    }

    public void setCompletedAt(String completedAt) {
        CompletedAt = completedAt;
    }

    public String getArrivedAt() {
        return ArrivedAt;
    }

    public void setArrivedAt(String arrivedAt) {
        ArrivedAt = arrivedAt;
    }

    public boolean isScanned() {
        return IsScanned;
    }

    public void setScanned(boolean scanned) {
        IsScanned = scanned;
    }
}
