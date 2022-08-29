package com.thyrocare.btechapp.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.adapter.SelectClinicalHistoryExpandableListAdapter;
import com.thyrocare.btechapp.delegate.SelectClinicalHistoryCheckboxDelegate;
import com.thyrocare.btechapp.models.data.BeneficiaryTestDetailsModel;
import com.thyrocare.btechapp.models.data.BeneficiaryTestWiseClinicalHistoryModel;

import java.util.ArrayList;

/**
 * Created by Orion on 4/24/2017.
 */

public class ClinicalHistorySelectorDialog extends Dialog {
    private Activity activity;
    private Button btnSave;
    private ExpandableListView elvClinicalHistory;
    private ArrayList<BeneficiaryTestDetailsModel> testsRateMasterModelsArr;
    private ArrayList<BeneficiaryTestWiseClinicalHistoryModel> chArr;
    private int benId;
    private SelectClinicalHistoryExpandableListAdapter sCHELA;
    private SelectClinicalHistoryCheckboxDelegate selectClinicalHistoryCheckboxDelegate;

    public ClinicalHistorySelectorDialog(Activity activity, ArrayList<BeneficiaryTestDetailsModel> testsRateMasterModelsArr, ArrayList<BeneficiaryTestWiseClinicalHistoryModel> chArr, int benId, SelectClinicalHistoryCheckboxDelegate selectClinicalHistoryCheckboxDelegate) {
        super(activity);
        this.activity = activity;
        this.testsRateMasterModelsArr = testsRateMasterModelsArr;
        this.chArr = chArr;
        this.benId = benId;
        this.selectClinicalHistoryCheckboxDelegate = selectClinicalHistoryCheckboxDelegate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_clinical_history);
        setTitle("Select Clinical History");
        setCanceledOnTouchOutside(false);
        initUI();
        initData();
        setListners();
    }

    private void initData() {
        sCHELA = new SelectClinicalHistoryExpandableListAdapter(activity, testsRateMasterModelsArr, chArr, benId, new SelectClinicalHistoryCheckboxDelegate() {
            @Override
            public void onCheckChange(ArrayList<BeneficiaryTestWiseClinicalHistoryModel> chModels) {
                chArr = chModels;
                sCHELA.notifyDataSetChanged();
            }
        });
        elvClinicalHistory.setAdapter(sCHELA);
    }

    private void setListners() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                selectClinicalHistoryCheckboxDelegate.onCheckChange(chArr);
            }
        });
    }

    private void initUI() {
        elvClinicalHistory = (ExpandableListView) findViewById(R.id.elv_ch);
        btnSave = (Button) findViewById(R.id.btn_save);
    }
}
