package com.thyrocare.btechapp.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 4/20/2017.
 */

public class SlotModel extends BaseModel implements Parcelable {
    public static final Creator<SlotModel> CREATOR = new Creator<SlotModel>() {
        @Override
        public SlotModel createFromParcel(Parcel in) {
            return new SlotModel(in);
        }

        @Override
        public SlotModel[] newArray(int size) {
            return new SlotModel[size];
        }
    };
    private String Slot;
    private int Id;
    private boolean isSelected;
    private boolean MandatorySlot;

    public SlotModel() {
        super();
    }

    protected SlotModel(Parcel in) {
        super(in);
        Slot = in.readString();
        Id = in.readInt();
        isSelected = in.readByte() != 0;
        MandatorySlot = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(Slot);
        dest.writeInt(Id);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeByte((byte) (MandatorySlot ? 1 : 0));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SlotModel) {
            return ((SlotModel) obj).getId() == this.getId();
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getSlot() {
        return Slot;
    }

    public void setSlot(String slot) {
        Slot = slot;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isMandatorySlot() {
        return MandatorySlot;
    }

    public void setMandatorySlot(boolean mandatorySlot) {
        MandatorySlot = mandatorySlot;
    }
}
