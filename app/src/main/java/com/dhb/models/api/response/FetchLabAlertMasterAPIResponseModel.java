package com.dhb.models.api.response;

import com.dhb.models.data.LabAlertMasterModel;

import java.util.ArrayList;

/**
 * Created by ISRO on 5/5/2017.
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
