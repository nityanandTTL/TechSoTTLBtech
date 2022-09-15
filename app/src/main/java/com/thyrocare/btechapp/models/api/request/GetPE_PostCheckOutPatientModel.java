package com.thyrocare.btechapp.models.api.request;

import java.util.ArrayList;

public class GetPE_PostCheckOutPatientModel {
    ArrayList<patients> PatientDetailsList = new ArrayList<patients>();

    public ArrayList<patients> getPatientDetailsList() {
        return PatientDetailsList;
    }

    public void setPatientDetailsList(ArrayList<patients> patientDetailsList) {
        PatientDetailsList = patientDetailsList;
    }

    public class patients {
        String id;
        String name;
        String age;
        String gender;
        boolean isSelected;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }
    }
}
