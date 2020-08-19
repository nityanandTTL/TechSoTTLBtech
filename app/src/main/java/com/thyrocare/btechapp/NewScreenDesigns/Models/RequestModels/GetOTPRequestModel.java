package com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels;

import org.parceler.Parcel;


@Parcel
public class GetOTPRequestModel  {

    String Mobile_no;

    public GetOTPRequestModel() {
    }

    public String getMobile_no() {
        return Mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        Mobile_no = mobile_no;
    }
}
