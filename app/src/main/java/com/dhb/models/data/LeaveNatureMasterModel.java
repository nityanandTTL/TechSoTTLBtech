package com.dhb.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by E4904 on 5/6/2017.
 */

public class LeaveNatureMasterModel extends BaseModel implements Parcelable {

    Integer Id;
    String Nature;


    public LeaveNatureMasterModel() {
        super ();
    }

    protected LeaveNatureMasterModel(Parcel in) {
        super(in);
        Id = in.readInt();
        Nature = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(Id);
        dest.writeString(Nature);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LeaveNatureMasterModel> CREATOR = new Creator<LeaveNatureMasterModel>() {
        @Override
        public LeaveNatureMasterModel createFromParcel(Parcel in) {
            return new LeaveNatureMasterModel(in);
        }

        @Override
        public LeaveNatureMasterModel[] newArray(int size) {
            return new LeaveNatureMasterModel[size];
        }
    };

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getNature() {
        return Nature;
    }

    public void setNature(String nature) {
        Nature = nature;
    }
}
