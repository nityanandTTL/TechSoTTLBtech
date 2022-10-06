package com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ConfirmOrderRequestModel {

    public String orderId;
    public ArrayList<PatientsDTO> patients = new ArrayList<>();
    public int phleboId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public ArrayList<PatientsDTO> getPatients() {
        return patients;
    }

    public void setPatients(ArrayList<PatientsDTO> patients) {
        this.patients = patients;
    }

    public void addPatient(PatientsDTO singlePatient) {
        this.patients.add(singlePatient);
    }

    public int getPhleboId() {
        return phleboId;
    }

    public void setPhleboId(int phleboId) {
        this.phleboId = phleboId;
    }


    public static class PatientsDTO {
        public String Name;
        public String Age;
        public String Gender;
        public ArrayList<ProductsDTO> Products = new ArrayList<>();
        public String leadId;

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getAge() {
            return Age;
        }

        public void setAge(String age) {
            Age = age;
        }

        public String getGender() {
            return Gender;
        }

        public void setGender(String gender) {
            Gender = gender;
        }

        public ArrayList<ProductsDTO> getProducts() {
            return Products;
        }

        public void setProducts(ArrayList<ProductsDTO> products) {
            Products = products;
        }

        public void addProducts(ProductsDTO singleTest) {
            this.Products.add(singleTest);
        }

        public String getLeadId() {
            return leadId;
        }

        public void setLeadId(String leadId) {
            this.leadId = leadId;
        }

        public static class ProductsDTO {
            public String dos_code;
            public String type;
            public String lab_dos_name;
            public String packageId;

            public String getDos_code() {
                return dos_code;
            }

            public void setDos_code(String dos_code) {
                this.dos_code = dos_code;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getLab_dos_name() {
                return lab_dos_name;
            }

            public void setLab_dos_name(String lab_dos_name) {
                this.lab_dos_name = lab_dos_name;
            }

            public String getPackageId() {
                return packageId;
            }

            public void setPackageId(String packageId) {
                this.packageId = packageId;
            }
        }
    }
}
