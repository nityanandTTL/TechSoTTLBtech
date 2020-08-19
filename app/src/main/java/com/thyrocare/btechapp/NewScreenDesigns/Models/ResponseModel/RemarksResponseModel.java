package com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel;

/**
 * Created by E4904 on 8/10/2017.
 */

public class RemarksResponseModel /*implements Parcelable*/ {
    /**
     * Id : 21
     * Reason : Bad Weather - Can Not Serve Today
     */

    private int Id;
    private String Reason;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String Reason) {
        this.Reason = Reason;
    }

    /*private int Id;
    private String Reason;

    protected RemarksResponseModel(Parcel in) {
        Id = in.readInt();
        Reason = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(Reason);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RemarksResponseModel> CREATOR = new Creator<RemarksResponseModel>() {
        @Override
        public RemarksResponseModel createFromParcel(Parcel in) {
            return new RemarksResponseModel(in);
        }

        @Override
        public RemarksResponseModel[] newArray(int size) {
            return new RemarksResponseModel[size];
        }
    };

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public RemarksResponseModel() {
    }*/



}
