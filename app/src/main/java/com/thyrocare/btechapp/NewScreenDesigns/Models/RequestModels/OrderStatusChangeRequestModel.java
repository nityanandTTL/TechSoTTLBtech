package com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 4/24/2017.
 */

public class OrderStatusChangeRequestModel implements Parcelable {
    String Remarks;
    String Id;
    int Status;
    String AppointmentDate;

    public OrderStatusChangeRequestModel() {
    }

    protected OrderStatusChangeRequestModel(Parcel in) {
        Remarks = in.readString();
        Id = in.readString();
        Status = in.readInt();
        AppointmentDate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Remarks);
        dest.writeString(Id);
        dest.writeInt(Status);
        dest.writeString(AppointmentDate);
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
}
