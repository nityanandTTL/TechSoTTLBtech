package com.dhb.models.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Pratik Ambhore on 4/19/2017.
 */

public class OrderDetailsModel extends BaseModel implements Parcelable {
    private int BrandId;
    private String OrderNo;
    private String Address;
    private String Pincode;
    private String Mobile;
    private String Email;
    private String PayType;
    private int AmountDue;
    private int Margin;
    private int Discount;
    private String Refcode;
    private String ProjId;
    private int ReportHC;
    private boolean isTestEdit;
    private boolean isAddBen;
    private ArrayList<BeneficiaryDetailsModel> benMaster;
    private String VisitId;
    private String Slot;
    private String Latitude;
    private String Longitude;
    private String Response;
    private int SlotId;
    private int Distance;
    private String Latitude;
    private String Longitude;
    private String Status;

    private ArrayList<KitsCountModel> kits;

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

    public OrderDetailsModel() {
        super();
    }

    protected OrderDetailsModel(Parcel in) {
        super(in);
        BrandId = in.readInt();
        OrderNo = in.readString();
        Address = in.readString();
        Pincode = in.readString();
        Mobile = in.readString();
        Email = in.readString();
        PayType = in.readString();
        AmountDue = in.readInt();
        Margin = in.readInt();
        Discount = in.readInt();
        Refcode = in.readString();
        ProjId = in.readString();
        ReportHC = in.readInt();
        isTestEdit = in.readByte() != 0;
        isAddBen = in.readByte() != 0;
        benMaster = in.createTypedArrayList(BeneficiaryDetailsModel.CREATOR);
        VisitId = in.readString();
        Slot = in.readString();
        Response = in.readString();
        SlotId = in.readInt();
        Distance = in.readInt();
        Latitude = in.readString();
        Longitude = in.readString();
        Status = in.readString();
        kits = in.createTypedArrayList(KitsCountModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(BrandId);
        dest.writeString(OrderNo);
        dest.writeString(Address);
        dest.writeString(Pincode);
        dest.writeString(Mobile);
        dest.writeString(Email);
        dest.writeString(PayType);
        dest.writeInt(AmountDue);
        dest.writeInt(Margin);
        dest.writeInt(Discount);
        dest.writeString(Refcode);
        dest.writeString(ProjId);
        dest.writeInt(ReportHC);
        dest.writeByte((byte) (isTestEdit ? 1 : 0));
        dest.writeByte((byte) (isAddBen ? 1 : 0));
        dest.writeTypedList(benMaster);
        dest.writeString(VisitId);
        dest.writeString(Slot);
        dest.writeString(Response);
        dest.writeInt(SlotId);
        dest.writeInt(Distance);
        dest.writeString(Latitude);
        dest.writeString(Longitude);
        dest.writeString(Status);
        dest.writeTypedList(kits);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderDetailsModel> CREATOR = new Creator<OrderDetailsModel>() {
        @Override
        public OrderDetailsModel createFromParcel(Parcel in) {
            return new OrderDetailsModel(in);
        }

        @Override
        public OrderDetailsModel[] newArray(int size) {
            return new OrderDetailsModel[size];
        }
    };

    public String getVisitId() {
        return VisitId;
    }

    public void setVisitId(String visitId) {
        VisitId = visitId;
    }

    public String getSlot() {
        return Slot;
    }

    public void setSlot(String slot) {
        Slot = slot;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public int getSlotId() {
        return SlotId;
    }

    public void setSlotId(int slotId) {
        SlotId = slotId;
    }

    public int getBrandId() {
        return BrandId;
    }

    public void setBrandId(int brandId) {
        BrandId = brandId;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPayType() {
        return PayType;
    }

    public void setPayType(String payType) {
        PayType = payType;
    }

    public int getAmountDue() {
        return AmountDue;
    }

    public void setAmountDue(int amountDue) {
        AmountDue = amountDue;
    }

    public int getMargin() {
        return Margin;
    }

    public void setMargin(int margin) {
        Margin = margin;
    }

    public int getDiscount() {
        return Discount;
    }

    public void setDiscount(int discount) {
        Discount = discount;
    }

    public String getRefcode() {
        return Refcode;
    }

    public void setRefcode(String refcode) {
        Refcode = refcode;
    }

    public String getProjId() {
        return ProjId;
    }

    public void setProjId(String projId) {
        ProjId = projId;
    }

    public int getReportHC() {
        return ReportHC;
    }

    public void setReportHC(int reportHC) {
        ReportHC = reportHC;
    }

    public boolean isTestEdit() {
        return isTestEdit;
    }

    public void setTestEdit(boolean testEdit) {
        isTestEdit = testEdit;
    }

    public boolean isAddBen() {
        return isAddBen;
    }

    public void setAddBen(boolean addBen) {
        isAddBen = addBen;
    }

    public ArrayList<BeneficiaryDetailsModel> getBenMaster() {
        return benMaster;
    }

    public void setBenMaster(ArrayList<BeneficiaryDetailsModel> benMaster) {
        this.benMaster = benMaster;
    }

    public ArrayList<KitsCountModel> getKits() {
        return kits;
    }

    public void setKits(ArrayList<KitsCountModel> kits) {
        this.kits = kits;
    }

    public int getDistance() {
        return Distance;
    }

    public void setDistance(int distance) {
        Distance = distance;
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

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
