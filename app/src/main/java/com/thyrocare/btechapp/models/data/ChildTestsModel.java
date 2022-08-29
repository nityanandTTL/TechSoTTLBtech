package com.thyrocare.btechapp.models.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Orion on 4/19/2017.
 */

public class ChildTestsModel extends BaseModel implements Parcelable {
    public static final Creator<ChildTestsModel> CREATOR = new Creator<ChildTestsModel>() {
        @Override
        public ChildTestsModel createFromParcel(Parcel in) {
            return new ChildTestsModel(in);
        }

        @Override
        public ChildTestsModel[] newArray(int size) {
            return new ChildTestsModel[size];
        }
    };
    private int ChildTestId;
    private String ChildTestCode;
    private ArrayList<TestSkillsModel> tstSkills;

    public ChildTestsModel() {
        super();
    }

    protected ChildTestsModel(Parcel in) {
        super(in);
        ChildTestId = in.readInt();
        ChildTestCode = in.readString();
        tstSkills = in.createTypedArrayList(TestSkillsModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(ChildTestId);
        dest.writeString(ChildTestCode);
        dest.writeTypedList(tstSkills);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getChildTestId() {
        return ChildTestId;
    }

    public void setChildTestId(int childTestId) {
        ChildTestId = childTestId;
    }

    public String getChildTestCode() {
        return ChildTestCode;
    }

    public void setChildTestCode(String childTestCode) {
        ChildTestCode = childTestCode;
    }

    public ArrayList<TestSkillsModel> getTstSkills() {
        return tstSkills;
    }

    public void setTstSkills(ArrayList<TestSkillsModel> tstSkills) {
        this.tstSkills = tstSkills;
    }
}
