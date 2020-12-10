package com.thyrocare.btechapp.models.api.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QrcodeBasedPatientDetailsResponseModel implements Serializable {


    private String Response;
    private String ResponseId;
    private ArrayList<PatientDataBean> PatientData;

    public String getResponse() {
        return Response;
    }

    public void setResponse(String Response) {
        this.Response = Response;
    }

    public String getResponseId() {
        return ResponseId;
    }

    public void setResponseId(String ResponseId) {
        this.ResponseId = ResponseId;
    }

    public ArrayList<PatientDataBean> getPatientData() {
        return PatientData;
    }

    public void setPatientData(ArrayList<PatientDataBean> PatientData) {
        this.PatientData = PatientData;
    }

    public static class PatientDataBean {

        private String Address;
        private String Age;
        private String Email;
        private String Gender;
        private String Id;
        private String Mobile;
        private String Name;
        private String OrderBy;
        private String OrderDate;
        private String OrderNo;
        private String Pincode;
        private String Product;
        private String Rate;
        private String Remarks;
        private String Status;
        private String Tsp;
        private ArrayList<TestDataBean> TestData;

        public String getAddress() {
            return Address;
        }

        public void setAddress(String Address) {
            this.Address = Address;
        }

        public String getAge() {
            return Age;
        }

        public void setAge(String Age) {
            this.Age = Age;
        }

        public String getEmail() {
            return Email;
        }

        public void setEmail(String Email) {
            this.Email = Email;
        }

        public String getGender() {
            return Gender;
        }

        public void setGender(String Gender) {
            this.Gender = Gender;
        }

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getMobile() {
            return Mobile;
        }

        public void setMobile(String Mobile) {
            this.Mobile = Mobile;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getOrderBy() {
            return OrderBy;
        }

        public void setOrderBy(String OrderBy) {
            this.OrderBy = OrderBy;
        }

        public String getOrderDate() {
            return OrderDate;
        }

        public void setOrderDate(String OrderDate) {
            this.OrderDate = OrderDate;
        }

        public String getOrderNo() {
            return OrderNo;
        }

        public void setOrderNo(String OrderNo) {
            this.OrderNo = OrderNo;
        }

        public String getPincode() {
            return Pincode;
        }

        public void setPincode(String Pincode) {
            this.Pincode = Pincode;
        }

        public String getProduct() {
            return Product;
        }

        public void setProduct(String Product) {
            this.Product = Product;
        }

        public String getRate() {
            return Rate;
        }

        public void setRate(String Rate) {
            this.Rate = Rate;
        }

        public String getRemarks() {
            return Remarks;
        }

        public void setRemarks(String Remarks) {
            this.Remarks = Remarks;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String Status) {
            this.Status = Status;
        }

        public String getTsp() {
            return Tsp;
        }

        public void setTsp(String Tsp) {
            this.Tsp = Tsp;
        }

        public ArrayList<TestDataBean> getTestData() {
            return TestData;
        }

        public void setTestData(ArrayList<TestDataBean> TestData) {
            this.TestData = TestData;
        }

        public static class TestDataBean {

            private String SampleType;
            private String Test;
            private String Barcode;

            public String getSampleType() {
                return SampleType;
            }

            public void setSampleType(String SampleType) {
                this.SampleType = SampleType;
            }

            public String getTest() {
                return Test;
            }

            public void setTest(String Test) {
                this.Test = Test;
            }

            public String getBarcode() {
                return Barcode;
            }

            public void setBarcode(String barcode) {
                Barcode = barcode;
            }
        }
    }
}
