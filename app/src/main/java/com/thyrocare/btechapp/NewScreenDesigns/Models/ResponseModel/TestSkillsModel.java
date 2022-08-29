package com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 4/19/2017.
 */

public class TestSkillsModel extends BaseModel implements Parcelable {
    public static final Creator<TestSkillsModel> CREATOR = new Creator<TestSkillsModel>() {
        @Override
        public TestSkillsModel createFromParcel(Parcel in) {
            return new TestSkillsModel(in);
        }

        @Override
        public TestSkillsModel[] newArray(int size) {
            return new TestSkillsModel[size];
        }
    };
    private int SkillId;
    private String Skill;

    public TestSkillsModel() {
        super();
    }

    protected TestSkillsModel(Parcel in) {
        super(in);
        SkillId = in.readInt();
        Skill = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(SkillId);
        dest.writeString(Skill);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getSkillId() {
        return SkillId;
    }

    public void setSkillId(int skillId) {
        SkillId = skillId;
    }

    public String getSkill() {
        return Skill;
    }

    public void setSkill(String skill) {
        Skill = skill;
    }
}
