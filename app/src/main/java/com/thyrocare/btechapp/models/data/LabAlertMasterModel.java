package com.thyrocare.btechapp.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 4/28/2017.
 */

public class LabAlertMasterModel extends BaseModel implements Parcelable {
    private int LabAlertId;
    private String LabAlert;

    public LabAlertMasterModel() {
        super();
    }

    protected LabAlertMasterModel(Parcel in) {
        super(in);
        LabAlertId = in.readInt();
        LabAlert = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(LabAlertId);
        dest.writeString(LabAlert);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LabAlertMasterModel> CREATOR = new Creator<LabAlertMasterModel>() {
        @Override
        public LabAlertMasterModel createFromParcel(Parcel in) {
            return new LabAlertMasterModel(in);
        }

        @Override
        public LabAlertMasterModel[] newArray(int size) {
            return new LabAlertMasterModel[size];
        }
    };

    public int getLabAlertId() {
        return LabAlertId;
    }

    public void setLabAlertId(int labAlertId) {
        LabAlertId = labAlertId;
    }

    public String getLabAlert() {
        return LabAlert;
    }

    public void setLabAlert(String labAlert) {
        LabAlert = labAlert;
    }
}
