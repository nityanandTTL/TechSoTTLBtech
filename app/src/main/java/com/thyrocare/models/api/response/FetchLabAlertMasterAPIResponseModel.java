package com.thyrocare.models.api.response;

import com.thyrocare.models.data.LabAlertMasterModel;

import java.util.ArrayList;

/**
 * Created by Orion on 5/5/2017.
 */

public class FetchLabAlertMasterAPIResponseModel {
    private ArrayList<LabAlertMasterModel> testLabAlerts;

    public ArrayList<LabAlertMasterModel> getTestLabAlerts() {
        return testLabAlerts;
    }

    public void setTestLabAlerts(ArrayList<LabAlertMasterModel> testLabAlerts) {
        this.testLabAlerts = testLabAlerts;
    }
}
