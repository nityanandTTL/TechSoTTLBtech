package com.thyrocare.models.data;

import java.io.Serializable;

public class Venupunture_Temporary_ImageModel implements Serializable {

    String VenupuntureBase64string ;
    int BenID;
    String Benname;
    String BenGender;
    String BenAge;
    String TRFImagePath = "";

    public String getVenupuntureBase64string() {
        return VenupuntureBase64string;
    }

    public void setVenupuntureBase64string(String venupuntureBase64string) {
        VenupuntureBase64string = venupuntureBase64string;
    }


    public int getBenID() {
        return BenID;
    }

    public void setBenID(int benID) {
        BenID = benID;
    }

    public String getBenname() {
        return Benname;
    }

    public void setBenname(String benname) {
        Benname = benname;
    }

    public String getBenGender() {
        return BenGender;
    }

    public void setBenGender(String benGender) {
        BenGender = benGender;
    }

    public String getBenAge() {
        return BenAge;
    }

    public void setBenAge(String benAge) {
        BenAge = benAge;
    }

    public String getTRFImagePath() {
        return TRFImagePath;
    }

    public void setTRFImagePath(String TRFImagePath) {
        this.TRFImagePath = TRFImagePath;
    }
}
