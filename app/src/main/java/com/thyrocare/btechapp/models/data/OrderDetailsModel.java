package com.thyrocare.btechapp.models.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.OrderUpdateDetailsModel;

import java.util.ArrayList;

/**
 * Created by Orion on 4/19/2017.
 */

public class OrderDetailsModel extends BaseModel implements Parcelable {
    private int BrandId;//
    private String OrderNo;//
    private String Timestamp;
    private String CancelSMSSentTime;
    private String Address;
    private String Pincode;
    private String Mobile;
    private String Email;
    private int BenCount;
    private String BillingCode;
    private String Delcode;
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
    private String SecondVisitTest;
    private String YNC;
    private boolean VipOrder;
    private OrderUpdateDetailsModel ordUpdateDetails;
    private ArrayList<KitsCountModel> kits;
    private String OrderMode;
    private boolean isDigital;
    private int CHC;
    private boolean displayProduct;
    private boolean IsPPE;
    private String PPE_AlertMsg;
    private String timeSlot;
    private boolean isKCF;
    private boolean ISHclOrder;
    private boolean IsOTP;
    private boolean isdisabledelete;
    private boolean isPEPartner;
    private boolean isPEDSAOrder;
    private boolean IsPEDSATCPhlebo;


    protected OrderDetailsModel(Parcel in) {
        super(in);
        BrandId = in.readInt();
        OrderNo = in.readString();
        Address = in.readString();
        Pincode = in.readString();
        Mobile = in.readString();
        Email = in.readString();
        BenCount = in.readInt();
        BillingCode = in.readString();
        Delcode = in.readString();
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
        SecondVisitTest = in.readString();
        YNC = in.readString();
        VipOrder = in.readByte() != 0;
        isDigital = in.readByte() != 0;
        ordUpdateDetails = in.readParcelable(OrderUpdateDetailsModel.class.getClassLoader());
        kits = in.createTypedArrayList(KitsCountModel.CREATOR);
        OrderMode = in.readString();
        CHC = in.readInt();
        displayProduct = in.readByte() != 0;
        IsPPE = in.readByte() != 0;
        isKCF = in.readByte() != 0;
        ISHclOrder = in.readByte() != 0;
        IsOTP = in.readByte() != 0;
        isdisabledelete = in.readByte() != 0;
        isPEPartner = in.readByte() != 0;
        isPEDSAOrder = in.readByte() != 0;
        IsPEDSATCPhlebo = in.readByte() != 0;
        PPE_AlertMsg = in.readString();
        timeSlot = in.readString();
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
        dest.writeInt(BenCount);
        dest.writeString(BillingCode);
        dest.writeString(Delcode);
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
        dest.writeString(SecondVisitTest);
        dest.writeString(YNC);
        dest.writeByte((byte) (VipOrder ? 1 : 0));
        dest.writeByte((byte) (isDigital ? 1 : 0));
        dest.writeParcelable(ordUpdateDetails, flags);
        dest.writeTypedList(kits);
        dest.writeString(OrderMode);
        dest.writeInt(CHC);
        dest.writeByte((byte) (displayProduct ? 1 : 0));
        dest.writeByte((byte) (IsPPE ? 1 : 0));
        dest.writeByte((byte) (isKCF ? 1 : 0));
        dest.writeByte((byte) (ISHclOrder ? 1 : 0));
        dest.writeByte((byte) (IsOTP ? 1 : 0));
        dest.writeByte((byte) (isdisabledelete ? 1 : 0));
        dest.writeByte((byte) (isPEPartner ? 1 : 0));
        dest.writeByte((byte) (isPEDSAOrder ? 1 : 0));
        dest.writeByte((byte) (IsPEDSATCPhlebo ? 1 : 0));
        dest.writeString(PPE_AlertMsg);
        dest.writeString(timeSlot);
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

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }

    public String getCancelSMSSentTime() {
        return CancelSMSSentTime;
    }

    public void setCancelSMSSentTime(String cancelSMSSentTime) {
        CancelSMSSentTime = cancelSMSSentTime;
    }

    public boolean isKCF() {
        return isKCF;
    }

    public boolean isISHclOrder() {
        return ISHclOrder;
    }

    public void setISHclOrder(boolean ISHclOrder) {
        this.ISHclOrder = ISHclOrder;
    }

    public boolean isOTP() {
        return IsOTP;
    }

    public void setOTP(boolean OTP) {
        IsOTP = OTP;
    }

    public boolean isIsdisabledelete() {
        return isdisabledelete;
    }

    public void setIsdisabledelete(boolean isdisabledelete) {
        this.isdisabledelete = isdisabledelete;
    }

    public boolean isPEPartner() {
        return isPEPartner;
    }

    public void setPEPartner(boolean PEPartner) {
        isPEPartner = PEPartner;
    }

    public boolean isPEDSAOrder() {
        return isPEDSAOrder;
    }

    public void setPEDSAOrder(boolean PEDSAOrder) {
        isPEDSAOrder = PEDSAOrder;
    }

    public boolean IsPEDSATCPhlebo() {
        return IsPEDSATCPhlebo;
    }

    public void setTCB2BOrder(boolean IsPEDSATCPhlebo) {
        IsPEDSATCPhlebo = IsPEDSATCPhlebo;
    }

    public void setKCF(boolean KCF) {
        isKCF = KCF;
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

    public OrderDetailsModel() {
        super();
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

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
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


    public String getSecondVisitTest() {
        return SecondVisitTest;
    }

    public void setSecondVisitTest(String secondVisitTest) {
        SecondVisitTest = secondVisitTest;
    }

    public int getBenCount() {
        return BenCount;
    }

    public void setBenCount(int benCount) {
        BenCount = benCount;
    }

    public String getBillingCode() {
        return BillingCode;
    }

    public void setBillingCode(String billingCode) {
        BillingCode = billingCode;
    }

    public String getDelcode() {
        return Delcode;
    }

    public void setDelcode(String delcode) {
        Delcode = delcode;
    }

    public String getYNC() {
        return YNC;
    }

    public void setYNC(String YNC) {
        this.YNC = YNC;
    }

    public boolean isVipOrder() {
        return VipOrder;
    }

    public void setVipOrder(boolean vipOrder) {
        VipOrder = vipOrder;
    }

    public boolean isDigital() {
        return isDigital;
    }

    public void setDigital(boolean digital) {
        isDigital = digital;
    }

    public OrderUpdateDetailsModel getOrdUpdateDetails() {
        return ordUpdateDetails;
    }

    public void setOrdUpdateDetails(OrderUpdateDetailsModel ordUpdateDetails) {
        this.ordUpdateDetails = ordUpdateDetails;
    }

    public String getOrderMode() {
        return OrderMode;
    }

    public void setOrderMode(String orderMode) {
        OrderMode = orderMode;
    }

    public int getCHC() {
        return CHC;
    }

    public void setCHC(int CHC) {
        this.CHC = CHC;
    }

    public boolean isDisplayProduct() {
        return displayProduct;
    }

    public void setDisplayProduct(boolean displayProduct) {
        this.displayProduct = displayProduct;
    }

    public boolean isPPE() {
        return IsPPE;
    }

    public void setPPE(boolean PPE) {
        IsPPE = PPE;
    }

    public String getPPE_AlertMsg() {
        return PPE_AlertMsg;
    }

    public void setPPE_AlertMsg(String PPE_AlertMsg) {
        this.PPE_AlertMsg = PPE_AlertMsg;
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
