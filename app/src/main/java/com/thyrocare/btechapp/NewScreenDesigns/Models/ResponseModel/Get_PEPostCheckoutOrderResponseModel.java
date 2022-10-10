package com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel;

import com.thyrocare.btechapp.models.api.request.GetPatientListResponseModel;
import com.thyrocare.btechapp.utils.app.Global;

import java.util.ArrayList;

public class Get_PEPostCheckoutOrderResponseModel {
    /*TODO get the response of post checkout order*/
    private String testName;
    private Integer patientCount;
    private boolean dataAdded;
    private String testtype;
    private String projectID;
    private ArrayList<GetPatientListResponseModel.DataDTO> addedPatients = new ArrayList<>();

    public String getTesttype() {
        return testtype;
    }

    public void setTesttype(String testtype) {
        this.testtype = testtype;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getPatientDetails() {
        String addDetailsString = "";
        try {
            for (int i = 0; i < addedPatients.size(); i++) {
                String Gender = addedPatients.get(i).getGender() == 1 ? "Male" : "Female";
                if (addedPatients.get(i).isSelected())
                    addDetailsString = addDetailsString + addedPatients.get(i).getName() + " ( " + Gender + " | " + addedPatients.get(i).getAge() + " ) \n";
            }
            Global.sout("added patient string", addDetailsString.toString());
        } catch (Exception e) {
            Global.sout("adddetailsString exception", e.getLocalizedMessage());
        }
        return addDetailsString.trim();
    }

    public ArrayList<GetPatientListResponseModel.DataDTO> getAddedPatients() {
        return addedPatients;
    }

    public void setAddedPatients(ArrayList<GetPatientListResponseModel.DataDTO> addedPatients) {
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
