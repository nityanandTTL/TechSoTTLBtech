package com.thyrocare.btechapp.models.api.request;

import java.util.ArrayList;
import java.util.List;

public class GetPatientListResponseModel {

    public String error;
    public ArrayList<DataDTO> data;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public ArrayList<DataDTO> getData() {
        return data;
    }

    public void setData(ArrayList<DataDTO> data) {
        this.data = data;
    }

    public void addData(DataDTO data) {
        this.data.add(data);
    }

    public static class DataDTO {
        public int id;
        public String name;
        public int gender;
        public int age;
        public boolean isSelected;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }


    }
}
