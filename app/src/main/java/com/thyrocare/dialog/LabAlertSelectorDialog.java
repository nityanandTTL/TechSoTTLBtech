package com.thyrocare.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.thyrocare.R;
import com.thyrocare.adapter.SelectLabAlertsMasterDisplayAdapter;
import com.thyrocare.delegate.SelectLabAlertsCheckboxDelegate;
import com.thyrocare.models.data.BeneficiaryLabAlertsModel;
import com.thyrocare.models.data.LabAlertMasterModel;

import java.util.ArrayList;

/**
 * Created by Orion on 4/24/2017.
 */

public class LabAlertSelectorDialog extends Dialog{
    private Activity activity;
    private Button btnSave;
    private ArrayList<BeneficiaryLabAlertsModel> sLAArr;
    private ArrayList<LabAlertMasterModel> labAlertMasterModelsArr;
    private int benId;
    private SelectLabAlertsMasterDisplayAdapter sLAMDA;
    private SelectLabAlertsCheckboxDelegate selectLabAlertsCheckboxDelegate;
    private ListView lvLabAlerts;
    public LabAlertSelectorDialog(Activity activity, ArrayList<LabAlertMasterModel> labAlertMasterModelsArr, ArrayList<BeneficiaryLabAlertsModel> sLAArr, int benId, SelectLabAlertsCheckboxDelegate selectLabAlertsCheckboxDelegate) {
        super(activity);
        this.activity = activity;
        this.labAlertMasterModelsArr = labAlertMasterModelsArr;
        this.sLAArr = sLAArr;
        this.benId = benId;
        this.selectLabAlertsCheckboxDelegate = selectLabAlertsCheckboxDelegate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_lab_alert);
        setTitle("Select Lab Alerts");
        setCanceledOnTouchOutside(false);
        initUI();
        initData();
        setListners();
    }

    private void initData() {
        sLAMDA = new SelectLabAlertsMasterDisplayAdapter(activity, labAlertMasterModelsArr, sLAArr, benId, new SelectLabAlertsCheckboxDelegate() {
            @Override
            public void onCheckChange(ArrayList<BeneficiaryLabAlertsModel> chArr) {
                sLAArr = chArr;
                sLAMDA.notifyDataSetChanged();
            }
        });
        lvLabAlerts.setAdapter(sLAMDA);
    }

    private void setListners() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                selectLabAlertsCheckboxDelegate.onCheckChange(sLAArr);
            }
        });
    }

    private void initUI() {
        lvLabAlerts = (ListView) findViewById(R.id.lv_lab_alerts);
        btnSave = (Button) findViewById(R.id.btn_save);
    }
}
