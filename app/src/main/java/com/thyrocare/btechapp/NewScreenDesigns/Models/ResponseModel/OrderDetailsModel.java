package com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Orion on 4/19/2017.
 */


public class OrderDetailsModel extends BaseModel implements Parcelable {
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
    private int BrandId;//
    private String OrderNo;//
    private String Address;
    private String Pincode;
    private String Mobile;
    private String Email;
    private String PayType;//
    private int AmountDue;//
    private int AmountPayable;
    private int Margin;//
    private String Location;
    private int Discount;//
    private String Refcode;
    private String ProjId;//
    private int ReportHC;
    private int UserAccessCode;
    private boolean isTestEdit;
    private boolean isAddBen;
    private ArrayList<BeneficiaryDetailsModel> benMaster;
    private String VisitId;
    private String Slot;
    private String CampId;
    private String Response;
    private int SlotId;
    private float Distance;
    private String Latitude;
    private String Longitude;
    private String Status;
    private String Servicetype;
    private float EstIncome;
    private String AppointmentDate;
    private String BtechName;
    private boolean EditHC;
    private boolean DirectVisit;
    private boolean EditOrder;
    private boolean EuOrders;
    private boolean EditME;
    private ArrayList<KitsCountModel> kits;

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
        AmountPayable = in.readInt();
        Margin = in.readInt();
        Location = in.readString();
        Discount = in.readInt();
        Refcode = in.readString();
        ProjId = in.readString();
        ReportHC = in.readInt();
        UserAccessCode = in.readInt();
        isTestEdit = in.readByte() != 0;
        isAddBen = in.readByte() != 0;
        benMaster = in.createTypedArrayList(BeneficiaryDetailsModel.CREATOR);
        VisitId = in.readString();
        Slot = in.readString();
        CampId = in.readString();
        Response = in.readString();
        SlotId = in.readInt();
        Distance = in.readFloat();
        Latitude = in.readString();
        Longitude = in.readString();
        Status = in.readString();
        Servicetype = in.readString();
        EstIncome = in.readFloat();
        AppointmentDate = in.readString();
        BtechName = in.readString();
        EditHC = in.readByte() != 0;
        DirectVisit = in.readByte() != 0;
        EditOrder = in.readByte() != 0;
        EuOrders = in.readByte() != 0;
        EditME = in.readByte() != 0;
//        kits = in.createTypedArrayList(KitsCountModel.CREATOR);
    }

    public OrderDetailsModel() {
        super();
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
        dest.writeInt(AmountPayable);
        dest.writeInt(Margin);
        dest.writeString(Location);
        dest.writeInt(Discount);
        dest.writeString(Refcode);
        dest.writeString(ProjId);
        dest.writeInt(ReportHC);
        dest.writeInt(UserAccessCode);
        dest.writeByte((byte) (isTestEdit ? 1 : 0));
        dest.writeByte((byte) (isAddBen ? 1 : 0));
        dest.writeTypedList(benMaster);
        dest.writeString(VisitId);
        dest.writeString(Slot);
        dest.writeString(CampId);
        dest.writeString(Response);
        dest.writeInt(SlotId);
        dest.writeFloat(Distance);
        dest.writeString(Latitude);
        dest.writeString(Longitude);
        dest.writeString(Status);
        dest.writeString(Servicetype);
        dest.writeFloat(EstIncome);
        dest.writeString(AppointmentDate);
        dest.writeString(BtechName);
        dest.writeByte((byte) (EditHC ? 1 : 0));
        dest.writeByte((byte) (DirectVisit ? 1 : 0));
        dest.writeByte((byte) (EditOrder ? 1 : 0));
        dest.writeByte((byte) (EuOrders ? 1 : 0));
        dest.writeByte((byte) (EditME ? 1 : 0));
//        dest.writeTypedList(kits);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public boolean isDirectVisit() {
        return DirectVisit;
    }

    public void setDirectVisit(boolean directVisit) {
        DirectVisit = directVisit;
    }

    public boolean isEditHC() {
        return EditHC;
    }

    public void setEditHC(boolean editHC) {
        EditHC = editHC;
    }

    public String getBtechName() {
        return BtechName;
    }

    public void setBtechName(String btechName) {
        BtechName = btechName;
    }

    public String getAppointmentDate() {
        return AppointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        AppointmentDate = appointmentDate;
    }

    public String getCampId() {
        return CampId;
    }

    public void setCampId(String campId) {
        CampId = campId;
    }

    public String getServicetype() {
        return Servicetype;
    }

    public void setServicetype(String servicetype) {
        Servicetype = servicetype;
    }

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

    public int getUserAccessCode() {
        return UserAccessCode;
    }

    public void setUserAccessCode(int userAccessCode) {
        UserAccessCode = userAccessCode;
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


    public int getDiscount() {
        return Discount;
    }

    public void setDiscount(int discount) {
        Discount = discount;
    }

    public float getDistance() {
        return Distance;
    }

    public void setDistance(float distance) {
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

    public float getEstIncome() {
        return EstIncome;
    }

    public void setEstIncome(float estIncome) {
        EstIncome = estIncome;
    }

    public int getAmountPayable() {
        return AmountPayable;
    }

    public void setAmountPayable(int amountPayable) {
        AmountPayable = amountPayable;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public boolean isEditOrder() {
        return EditOrder;
    }

    public void setEditOrder(boolean editOrder) {
        EditOrder = editOrder;
    }

    public boolean isEuOrders() {
        return EuOrders;
    }

    public void setEuOrders(boolean euOrders) {
        EuOrders = euOrders;
    }

    public boolean isEditME() {
        return EditME;
    }

    public void setEditME(boolean editME) {
        EditME = editME;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof OrderDetailsModel) {
            if (((OrderDetailsModel) obj).getOrderNo().equals(getOrderNo())) {
                return true;
            }
        }
        return false;
    }
}
