package com.thyrocare.btechapp.models.api.request;

import java.util.ArrayList;

public class AddONRequestModel {

    private patient patient;
    private ArrayList<test> test;

    public patient getPatient() {
        return patient;
    }

    public void setPatient(patient patientArrayList) {
        this.patient = patientArrayList;
    }

    public ArrayList<test> getTestArrayList() {
        return test;
    }

    public void setTest(ArrayList<test> testArrayList) {
        this.test = testArrayList;
    }

    public static class patient{
        private Integer id;
        private String name;
        private int age;
        private String gender;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }
    }

    public static class test{
        private Integer id;
        private String type;


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
