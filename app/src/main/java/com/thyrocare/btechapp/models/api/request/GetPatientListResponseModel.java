package com.thyrocare.btechapp.models.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class GetPatientListResponseModel {
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    private String error;

    private Data data;

    public static class Data {

        private Integer user_id;

        public Integer getUser_id() {
            return user_id;
        }

        public void setUser_id(Integer user_id) {
            this.user_id = user_id;
        }

        public ArrayList<Data.patients> getPatients() {
            return patients;
        }

        public void setPatients(ArrayList<Data.patients> patients) {
            this.patients = patients;
        }

        @JsonProperty("patients")
        private ArrayList<Data.patients> patients;

        public static class patients {

            private Integer id;
            private String name;
            private String gender;
            private String age;
            private Boolean isSelected;

            public Boolean getSelected() {
                return isSelected;
            }

            public void setSelected(Boolean selected) {
                isSelected = selected;
            }

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

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public String getAge() {
                return age;
            }

            public void setAge(String age) {
                this.age = age;
            }
        }
    }
}
