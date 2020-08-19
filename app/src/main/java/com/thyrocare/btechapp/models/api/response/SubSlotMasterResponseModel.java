package com.thyrocare.btechapp.models.api.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by E4904 on 9/2/2017.
 */

public class SubSlotMasterResponseModel implements Parcelable {

    private int Id;
    private String SlotMasterId;
    private String Slot;

    protected SubSlotMasterResponseModel(Parcel in) {
        Id = in.readInt();
        SlotMasterId = in.readString();
        Slot = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(SlotMasterId);
        dest.writeString(Slot);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SubSlotMasterResponseModel> CREATOR = new Creator<SubSlotMasterResponseModel>() {
        @Override
        public SubSlotMasterResponseModel createFromParcel(Parcel in) {
            return new SubSlotMasterResponseModel(in);
        }

        @Override
        public SubSlotMasterResponseModel[] newArray(int size) {
            return new SubSlotMasterResponseModel[size];
        }
    };

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getSlotMasterId() {
        return SlotMasterId;
    }

    public void setSlotMasterId(String slotMasterId) {
        SlotMasterId = slotMasterId;
    }

    public String getSlot() {
        return Slot;
    }

    public void setSlot(String slot) {
        Slot = slot;
    }

    public SubSlotMasterResponseModel() {
    }
}
