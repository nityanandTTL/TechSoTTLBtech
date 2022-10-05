package com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel;

import com.thyrocare.btechapp.models.api.request.GetPatientListResponseModel;

import java.util.ArrayList;

public class Get_PEPostCheckoutOrderResponseModel {
    /*TODO get the response of post checkout order*/
    private String testName;
    private Integer patientCount;
    private boolean dataAdded;
    private String patientDetails;
    private ArrayList<GetPatientListResponseModel.Data.patients> addedPatients = new ArrayList<>();

    public String getPatientDetails() {
        StringBuilder addDetailsString = new StringBuilder();
        for (int i = 0; i < addedPatients.size(); i++) {
            if (addedPatients.get(i).getSelected())
                addDetailsString.append(addedPatients.get(i).getName()).append("(").append(addedPatients.get(i).getGender()).append("|").append(addedPatients.get(i).getAge()).append(")\n");
        }
        return addDetailsString.toString();
    }

    public ArrayList<GetPatientListResponseModel.Data.patients> getAddedPatients() {
        return addedPatients;
    }

    public void setAddedPatients(ArrayList<GetPatientListResponseModel.Data.patients> addedPatients) {
        this.addedPatients = addedPatients;
    }

    public boolean isDataAdded() {
        return dataAdded;
    }

    public void setDataAdded(boolean dataAdded) {
        this.dataAdded = dataAdded;
    }


    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public int getPatientCount() {
        return patientCount;
    }

    public void setPatientCount(Integer patientCount) {
        this.patientCount = patientCount;
    }

    private class AddedPatients {
        String benName, benGender;
        int patientID, age;

        public String getBenName() {
            return benName;
        }

        public void setBenName(String benName) {
            this.benName = benName;
        }

        public String getBenGender() {
            return benGender;
        }

        public void setBenGender(String benGender) {
            this.benGender = benGender;
        }

        public int getPatientID() {
            return patientID;
        }

        public void setPatientID(int patientID) {
            this.patientID = patientID;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
