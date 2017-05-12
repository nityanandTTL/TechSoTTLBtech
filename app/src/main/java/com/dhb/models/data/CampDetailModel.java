package com.dhb.models.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by vendor1 on 5/10/2017.
 */

public class CampDetailModel implements Parcelable {
    private int Id, Amount, ExpectedCrowd, ExpectedBtech, Leader, LeaderContactNo;
    private String CampId, VisitId, CampDate, Location, Status;
    private boolean InventoryAssign, isStarted;
    private String CampDateTime, BookedBy, Product, QRCode;
    private ArrayList<CampBtechModel> btechs;
    private ExecutionTrack executionTrack;
    private boolean IsAccepted;

    protected CampDetailModel(Parcel in) {
        Id = in.readInt();
        Amount = in.readInt();
        ExpectedCrowd = in.readInt();
        ExpectedBtech = in.readInt();
        Leader = in.readInt();
        LeaderContactNo = in.readInt();
        CampId = in.readString();
        VisitId = in.readString();
        CampDate = in.readString();
        Location = in.readString();
        Status = in.readString();
        InventoryAssign = in.readByte() != 0;
        isStarted = in.readByte() != 0;
        CampDateTime = in.readString();
        BookedBy = in.readString();
        Product = in.readString();
        QRCode = in.readString();
        executionTrack = in.readParcelable(ExecutionTrack.class.getClassLoader());
        IsAccepted = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeInt(Amount);
        dest.writeInt(ExpectedCrowd);
        dest.writeInt(ExpectedBtech);
        dest.writeInt(Leader);
        dest.writeInt(LeaderContactNo);
        dest.writeString(CampId);
        dest.writeString(VisitId);
        dest.writeString(CampDate);
        dest.writeString(Location);
        dest.writeString(Status);
        dest.writeByte((byte) (InventoryAssign ? 1 : 0));
        dest.writeByte((byte) (isStarted ? 1 : 0));
        dest.writeString(CampDateTime);
        dest.writeString(BookedBy);
        dest.writeString(Product);
        dest.writeString(QRCode);
        dest.writeParcelable(executionTrack, flags);
        dest.writeByte((byte) (IsAccepted ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CampDetailModel> CREATOR = new Creator<CampDetailModel>() {
        @Override
        public CampDetailModel createFromParcel(Parcel in) {
            return new CampDetailModel(in);
        }

        @Override
        public CampDetailModel[] newArray(int size) {
            return new CampDetailModel[size];
        }
    };

    public boolean isAccepted() {
        return IsAccepted;
    }

    public void setAccepted(boolean accepted) {
        IsAccepted = accepted;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }

    public CampDetailModel() {

    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public int getExpectedCrowd() {
        return ExpectedCrowd;
    }

    public void setExpectedCrowd(int expectedCrowd) {
        ExpectedCrowd = expectedCrowd;
    }

    public int getExpectedBtech() {
        return ExpectedBtech;
    }

    public void setExpectedBtech(int expectedBtech) {
        ExpectedBtech = expectedBtech;
    }

    public int getLeader() {
        return Leader;
    }

    public void setLeader(int leader) {
        Leader = leader;
    }

    public int getLeaderContactNo() {
        return LeaderContactNo;
    }

    public void setLeaderContactNo(int leaderContactNo) {
        LeaderContactNo = leaderContactNo;
    }

    public String getCampDateTime() {
        return CampDateTime;
    }

    public void setCampDateTime(String campDateTime) {
        CampDateTime = campDateTime;
    }

    public String getBookedBy() {
        return BookedBy;
    }

    public void setBookedBy(String bookedBy) {
        BookedBy = bookedBy;
    }

    public String getProduct() {
        return Product;
    }

    public void setProduct(String product) {
        Product = product;
    }

    public String getQRCode() {
        return QRCode;
    }

    public void setQRCode(String QRCode) {
        this.QRCode = QRCode;
    }

    public ArrayList<CampBtechModel> getBtechs() {
        return btechs;
    }

    public void setBtechs(ArrayList<CampBtechModel> btechs) {
        this.btechs = btechs;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getCampId() {
        return CampId;
    }

    public void setCampId(String campId) {
        CampId = campId;
    }

    public String getVisitId() {
        return VisitId;
    }

    public void setVisitId(String visitId) {
        VisitId = visitId;
    }

    public String getCampDate() {
        return CampDate;
    }

    public void setCampDate(String campDate) {
        CampDate = campDate;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }


    public ExecutionTrack getExecutionTrack() {
        return executionTrack;
    }

    public void setExecutionTrack(ExecutionTrack executionTrack) {
        this.executionTrack = executionTrack;
    }

    public boolean isInventoryAssign() {
        return InventoryAssign;
    }

    public void setInventoryAssign(boolean inventoryAssign) {
        InventoryAssign = inventoryAssign;
    }
}
