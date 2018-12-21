package com.thyrocare.models.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Orion on 5/25/2017.
 */

public class CartAPIResponseOrderModel implements Parcelable {
    private String OrderNo;
    private int BrandId;
    private boolean HC;
    private int AmountDue;
    private int TestCharges;
    private int ServiceCharge;
    private int Fasting;
    private int NonFasting;
    private int CostPerKM;
    private int FixedPerOrder;
    private int PercentageOfOrder;
    private int ExtraBen;
    private ArrayList<CartAPIResponseBeneficiariesModel> beneficiaries;

    public CartAPIResponseOrderModel() {
    }

    protected CartAPIResponseOrderModel(Parcel in) {
        OrderNo = in.readString();
        BrandId = in.readInt();
        HC = in.readByte() != 0;
        AmountDue = in.readInt();
        TestCharges = in.readInt();
        ServiceCharge = in.readInt();
        Fasting = in.readInt();
        NonFasting = in.readInt();
        CostPerKM = in.readInt();
        FixedPerOrder = in.readInt();
        PercentageOfOrder = in.readInt();
        ExtraBen = in.readInt();
        beneficiaries = in.createTypedArrayList(CartAPIResponseBeneficiariesModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(OrderNo);
        dest.writeInt(BrandId);
        dest.writeByte((byte) (HC ? 1 : 0));
        dest.writeInt(AmountDue);
        dest.writeInt(TestCharges);
        dest.writeInt(ServiceCharge);
        dest.writeInt(Fasting);
        dest.writeInt(NonFasting);
        dest.writeInt(CostPerKM);
        dest.writeInt(FixedPerOrder);
        dest.writeInt(PercentageOfOrder);
        dest.writeInt(ExtraBen);
        dest.writeTypedList(beneficiaries);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CartAPIResponseOrderModel> CREATOR = new Creator<CartAPIResponseOrderModel>() {
        @Override
        public CartAPIResponseOrderModel createFromParcel(Parcel in) {
            return new CartAPIResponseOrderModel(in);
        }

        @Override
        public CartAPIResponseOrderModel[] newArray(int size) {
            return new CartAPIResponseOrderModel[size];
        }
    };

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public int getBrandId() {
        return BrandId;
    }

    public void setBrandId(int brandId) {
        BrandId = brandId;
    }

    public boolean isHC() {
        return HC;
    }

    public void setHC(boolean HC) {
        this.HC = HC;
    }

    public ArrayList<CartAPIResponseBeneficiariesModel> getBeneficiaries() {
        return beneficiaries;
    }

    public void setBeneficiaries(ArrayList<CartAPIResponseBeneficiariesModel> beneficiaries) {
        this.beneficiaries = beneficiaries;
    }

    public int getTestCharges() {
        return TestCharges;
    }

    public void setTestCharges(int testCharges) {
        TestCharges = testCharges;
    }

    public int getServiceCharge() {
        return ServiceCharge;
    }

    public void setServiceCharge(int serviceCharge) {
        ServiceCharge = serviceCharge;
    }

    public int getFasting() {
        return Fasting;
    }

    public void setFasting(int fasting) {
        Fasting = fasting;
    }

    public int getNonFasting() {
        return NonFasting;
    }

    public void setNonFasting(int nonFasting) {
        NonFasting = nonFasting;
    }

    public int getCostPerKM() {
        return CostPerKM;
    }

    public void setCostPerKM(int costPerKM) {
        CostPerKM = costPerKM;
    }

    public int getFixedPerOrder() {
        return FixedPerOrder;
    }

    public void setFixedPerOrder(int fixedPerOrder) {
        FixedPerOrder = fixedPerOrder;
    }

    public int getPercentageOfOrder() {
        return PercentageOfOrder;
    }

    public void setPercentageOfOrder(int percentageOfOrder) {
        PercentageOfOrder = percentageOfOrder;
    }

    public int getAmountDue() {
        return AmountDue;
    }

    public void setAmountDue(int amountDue) {
        AmountDue = amountDue;
    }

    public int getExtraBen() {
        return ExtraBen;
    }

    public void setExtraBen(int extraBen) {
        ExtraBen = extraBen;
    }
}
