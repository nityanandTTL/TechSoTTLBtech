package com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel;

import androidx.annotation.NonNull;

import java.util.ArrayList;


public class ServedOrderResponseModel {

    ArrayList<btchOrd> btchOrd;
    String Response;

    public ArrayList<ServedOrderResponseModel.btchOrd> getBtchOrd() {
        return btchOrd;
    }

    public void setBtchOrd(ArrayList<ServedOrderResponseModel.btchOrd> btchOrd) {
        this.btchOrd = btchOrd;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public class btchOrd {

        String OrderNo;
        String OrderBy;
        String Status;
        String Mobile;

        int benCount;
        int AmountCollected;

        String Fasting;
        ArrayList<btchBracodeDtl> btchBracodeDtl;

        public String getOrderNo() {
            return OrderNo;
        }

        public void setOrderNo(String orderNo) {
            OrderNo = orderNo;
        }

        public String getOrderBy() {
            return OrderBy;
        }

        public void setOrderBy(String orderBy) {
            OrderBy = orderBy;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            Status = status;
        }

        public String getMobile() {
            return Mobile;
        }

        public void setMobile(String mobile) {
            Mobile = mobile;
        }

        public int getBenCount() {
            return benCount;
        }

        public void setBenCount(int benCount) {
            this.benCount = benCount;
        }

        public int getAmountCollected() {
            return AmountCollected;
        }

        public void setAmountCollected(int amountCollected) {
            AmountCollected = amountCollected;
        }

        public String getFasting() {
            return Fasting;
        }

        public void setFasting(String fasting) {
            Fasting = fasting;
        }

        public ArrayList<ServedOrderResponseModel.btchBracodeDtl> getBtchBracodeDtl() {
            return btchBracodeDtl;
        }

        public void setBtchBracodeDtl(ArrayList<ServedOrderResponseModel.btchBracodeDtl> btchBracodeDtl) {
            this.btchBracodeDtl = btchBracodeDtl;
        }
    }

    public class btchBracodeDtl{

        String Barcode;

        public String getBarcode() {
            return Barcode;
        }

        public void setBarcode(String barcode) {
            Barcode = barcode;
        }

        @NonNull
        @Override
        public String toString() {
            return Barcode;
        }
    }
}
