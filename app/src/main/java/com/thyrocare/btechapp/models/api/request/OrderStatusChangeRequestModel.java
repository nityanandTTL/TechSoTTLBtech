package com.thyrocare.btechapp.models.api.request;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 4/24/2017.
 */

public class OrderStatusChangeRequestModel implements Parcelable{
    String Remarks;
    String Id;
    int Status;
    int RemarksId;
    int ReasonId;
    String AppointmentDate;
    String phleboStatus;

    public OrderStatusChangeRequestModel() {
    }

    protected OrderStatusChangeRequestModel(Parcel in) {
        Remarks = in.readString();
        Id = in.readString();
        Status = in.readInt();
        RemarksId = in.readInt();
        ReasonId = in.readInt();
        AppointmentDate = in.readString();
        phleboStatus = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Remarks);
        dest.writeString(Id);
        dest.writeInt(Status);
        dest.writeInt(RemarksId);
        dest.writeInt(ReasonId);
        dest.writeString(AppointmentDate);
        dest.writeString(phleboStatus);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderStatusChangeRequestModel> CREATOR = new Creator<OrderStatusChangeRequestModel>() {
        @Override
        public OrderStatusChangeRequestModel createFromParcel(Parcel in) {
            return new OrderStatusChangeRequestModel(in);
        }

        @Override
        public OrderStatusChangeRequestModel[] newArray(int size) {
            return new OrderStatusChangeRequestModel[size];
        }
    };

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getAppointmentDate() {
        return AppointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        AppointmentDate = appointmentDate;
    }

    public int getRemarksId() {
        return RemarksId;
    }

    public void setRemarksId(int remarksId) {
        RemarksId = remarksId;
    }

    public int getReasonId() {
        return ReasonId;
    }

    public void setReasonId(int reasonId) {
        ReasonId = reasonId;
    }

    public String getPhleboStatus() {
        return phleboStatus;
    }

    public void setPhleboStatus(String phleboStatus) {
        this.phleboStatus = phleboStatus;
    }
}
