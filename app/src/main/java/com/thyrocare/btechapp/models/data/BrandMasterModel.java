package com.thyrocare.btechapp.models.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Orion on 4/28/2017.
 */

public class BrandMasterModel extends BaseModel implements Parcelable {
    private int BrandId;
    private String BrandName;
    private String BrandAddress;
    private int cancellationFee;
    private int bookingInterval;
    private int collectionInterval;
    private boolean isAuthentication;
    private boolean Wallet;
    private boolean multiplePayments;
    private ArrayList<BrandCurrencyModel> crncydetls;
    private String Response;

    public BrandMasterModel() {
    }

    public int getBrandId() {
        return BrandId;
    }

    public void setBrandId(int brandId) {
        BrandId = brandId;
    }

    public String getBrandName() {
        return BrandName;
    }

    public void setBrandName(String brandName) {
        BrandName = brandName;
    }

    public String getBrandAddress() {
        return BrandAddress;
    }

    public void setBrandAddress(String brandAddress) {
        BrandAddress = brandAddress;
    }

    public int getCancellationFee() {
        return cancellationFee;
    }

    public void setCancellationFee(int cancellationFee) {
        this.cancellationFee = cancellationFee;
    }

    public int getBookingInterval() {
        return bookingInterval;
    }

    public void setBookingInterval(int bookingInterval) {
        this.bookingInterval = bookingInterval;
    }

    public int getCollectionInterval() {
        return collectionInterval;
    }

    public void setCollectionInterval(int collectionInterval) {
        this.collectionInterval = collectionInterval;
    }

    public boolean isAuthentication() {
        return isAuthentication;
    }

    public void setAuthentication(boolean authentication) {
        isAuthentication = authentication;
    }

    public boolean isWallet() {
        return Wallet;
    }

    public void setWallet(boolean wallet) {
        Wallet = wallet;
    }

    public boolean isMultiplePayments() {
        return multiplePayments;
    }

    public void setMultiplePayments(boolean multiplePayments) {
        this.multiplePayments = multiplePayments;
    }

    public ArrayList<BrandCurrencyModel> getCrncydetls() {
        return crncydetls;
    }

    public void setCrncydetls(ArrayList<BrandCurrencyModel> crncydetls) {
        this.crncydetls = crncydetls;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    protected BrandMasterModel(Parcel in) {
        super(in);
        BrandId = in.readInt();
        BrandName = in.readString();
        BrandAddress = in.readString();
        cancellationFee = in.readInt();
        bookingInterval = in.readInt();
        collectionInterval = in.readInt();
        isAuthentication = in.readByte() != 0;
        Wallet = in.readByte() != 0;
        multiplePayments = in.readByte() != 0;
        crncydetls = in.createTypedArrayList(BrandCurrencyModel.CREATOR);
        Response = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(BrandId);
        dest.writeString(BrandName);
        dest.writeString(BrandAddress);
        dest.writeInt(cancellationFee);
        dest.writeInt(bookingInterval);
        dest.writeInt(collectionInterval);
        dest.writeByte((byte) (isAuthentication ? 1 : 0));
        dest.writeByte((byte) (Wallet ? 1 : 0));
        dest.writeByte((byte) (multiplePayments ? 1 : 0));
        dest.writeTypedList(crncydetls);
        dest.writeString(Response);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BrandMasterModel> CREATOR = new Creator<BrandMasterModel>() {
        @Override
        public BrandMasterModel createFromParcel(Parcel in) {
            return new BrandMasterModel(in);
        }

        @Override
        public BrandMasterModel[] newArray(int size) {
            return new BrandMasterModel[size];
        }
    };

    @Override
    public String toString() {
        return  BrandName;
    }
}
