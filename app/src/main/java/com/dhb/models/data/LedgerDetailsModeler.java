package com.dhb.models.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Orion on 4/26/2017.
 */

public class LedgerDetailsModeler extends BaseModel implements Parcelable {


    String Date;
    Integer OpeningBal;
    Integer Collection;
    Integer Credit;
    Integer Debit;
    Integer Payments;
    Integer ClosingBal;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public Integer getOpeningBal() {
        return OpeningBal;
    }

    public void setOpeningBal(Integer openingBal) {
        OpeningBal = openingBal;
    }

    public Integer getCredit() {
        return Credit;
    }

    public void setCredit(Integer credit) {
        Credit = credit;
    }

    public Integer getDebit() {
        return Debit;
    }

    public void setDebit(Integer debit) {
        Debit = debit;
    }

    public Integer getClosingBal() {
        return ClosingBal;
    }

    public void setClosingBal(Integer closingBal) {
        ClosingBal = closingBal;
    }



    public LedgerDetailsModeler() {
    super ();
    }

    protected LedgerDetailsModeler(Parcel in) {
        super(in);
        Date = in.readString();
        OpeningBal = in.readInt();
        Credit = in.readInt();
        Debit = in.readInt();
        ClosingBal = in.readInt();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(Date);
        dest.writeInt(OpeningBal);
        dest.writeInt(Credit);
        dest.writeInt(Debit);
        dest.writeInt(ClosingBal);




    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LedgerDetailsModeler> CREATOR = new Creator<LedgerDetailsModeler>() {
        @Override
        public LedgerDetailsModeler createFromParcel(Parcel in) {
            return new LedgerDetailsModeler(in);
        }

        @Override
        public LedgerDetailsModeler[] newArray(int size) {
            return new LedgerDetailsModeler[size];
        }
    };
}
