package com.thyrocare.models.api.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.thyrocare.models.data.BeneficiaryBarcodeDetailsModel;
import com.thyrocare.models.data.BeneficiaryDetailsModel;
import com.thyrocare.models.data.BeneficiaryLabAlertsModel;
import com.thyrocare.models.data.BeneficiarySampleTypeDetailsModel;
import com.thyrocare.models.data.BeneficiaryTestWiseClinicalHistoryModel;
import com.thyrocare.models.data.OrderBookingDetailsModel;
import com.thyrocare.models.data.OrderDetailsModel;

import java.util.ArrayList;

/**
 * Created by Orion on 5/15/2017.<br/>
 * /OrderBooking<br/>
 */

public class OrderBookingRequestModel implements Parcelable {
    private OrderBookingDetailsModel ordbooking;
    private ArrayList<OrderDetailsModel> orddtl;
    private ArrayList<BeneficiaryDetailsModel> bendtl;
    private ArrayList<BeneficiaryBarcodeDetailsModel> barcodedtl;
    private ArrayList<BeneficiarySampleTypeDetailsModel> smpldtl;
    private ArrayList<BeneficiaryLabAlertsModel> labAlert;
    private ArrayList<BeneficiaryTestWiseClinicalHistoryModel> clHistory;

    public OrderBookingRequestModel() {
    }

    public OrderBookingDetailsModel getOrdbooking() {
        return ordbooking;
    }

    public void setOrdbooking(OrderBookingDetailsModel ordbooking) {
        this.ordbooking = ordbooking;
    }

    public ArrayList<OrderDetailsModel> getOrddtl() {
        return orddtl;
    }

    public void setOrddtl(ArrayList<OrderDetailsModel> orddtl) {
        this.orddtl = orddtl;
    }

    public ArrayList<BeneficiaryDetailsModel> getBendtl() {
        return bendtl;
    }

    public void setBendtl(ArrayList<BeneficiaryDetailsModel> bendtl) {
        this.bendtl = bendtl;
    }

    public ArrayList<BeneficiaryBarcodeDetailsModel> getBarcodedtl() {
        return barcodedtl;
    }

    public void setBarcodedtl(ArrayList<BeneficiaryBarcodeDetailsModel> barcodedtl) {
        this.barcodedtl = barcodedtl;
    }

    public ArrayList<BeneficiarySampleTypeDetailsModel> getSmpldtl() {
        return smpldtl;
    }

    public void setSmpldtl(ArrayList<BeneficiarySampleTypeDetailsModel> smpldtl) {
        this.smpldtl = smpldtl;
    }

    public ArrayList<BeneficiaryLabAlertsModel> getLabAlert() {
        return labAlert;
    }

    public void setLabAlert(ArrayList<BeneficiaryLabAlertsModel> labAlert) {
        this.labAlert = labAlert;
    }

    public ArrayList<BeneficiaryTestWiseClinicalHistoryModel> getClHistory() {
        return clHistory;
    }

    public void setClHistory(ArrayList<BeneficiaryTestWiseClinicalHistoryModel> clHistory) {
        this.clHistory = clHistory;
    }

    protected OrderBookingRequestModel(Parcel in) {
        ordbooking = in.readParcelable(OrderBookingDetailsModel.class.getClassLoader());
        orddtl = in.createTypedArrayList(OrderDetailsModel.CREATOR);
        bendtl = in.createTypedArrayList(BeneficiaryDetailsModel.CREATOR);
        barcodedtl = in.createTypedArrayList(BeneficiaryBarcodeDetailsModel.CREATOR);
        smpldtl = in.createTypedArrayList(BeneficiarySampleTypeDetailsModel.CREATOR);
        labAlert = in.createTypedArrayList(BeneficiaryLabAlertsModel.CREATOR);
        clHistory = in.createTypedArrayList(BeneficiaryTestWiseClinicalHistoryModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(ordbooking, flags);
        dest.writeTypedList(orddtl);
        dest.writeTypedList(bendtl);
        dest.writeTypedList(barcodedtl);
        dest.writeTypedList(smpldtl);
        dest.writeTypedList(labAlert);
        dest.writeTypedList(clHistory);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderBookingRequestModel> CREATOR = new Creator<OrderBookingRequestModel>() {
        @Override
        public OrderBookingRequestModel createFromParcel(Parcel in) {
            return new OrderBookingRequestModel(in);
        }

        @Override
        public OrderBookingRequestModel[] newArray(int size) {
            return new OrderBookingRequestModel[size];
        }
    };
}
