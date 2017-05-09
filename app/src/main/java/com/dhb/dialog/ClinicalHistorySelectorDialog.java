package com.dhb.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.dhb.R;
import com.dhb.adapter.SelectClinicalHistoryExpandableListAdapter;
import com.dhb.delegate.SelectClinicalHistoryCheckboxDelegate;
import com.dhb.models.data.TestRateMasterModel;
import com.dhb.models.data.TestWiseBeneficiaryClinicalHistoryModel;

import java.util.ArrayList;

/**
 * Created by vendor1 on 4/24/2017.
 */

public class ClinicalHistorySelectorDialog extends Dialog{
    private Activity activity;
    private Button btnSave;
    private ExpandableListView elvClinicalHistory;
    private ArrayList<TestRateMasterModel> testsRateMasterModelsArr;
    private ArrayList<TestWiseBeneficiaryClinicalHistoryModel> chArr;
    private int benId;
    private SelectClinicalHistoryExpandableListAdapter sCHELA;
    private SelectClinicalHistoryCheckboxDelegate selectClinicalHistoryCheckboxDelegate;
    public ClinicalHistorySelectorDialog(Activity activity, ArrayList<TestRateMasterModel> testsRateMasterModelsArr,ArrayList<TestWiseBeneficiaryClinicalHistoryModel> chArr,int benId,SelectClinicalHistoryCheckboxDelegate selectClinicalHistoryCheckboxDelegate) {
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
            public void onCheckChange(ArrayList<TestWiseBeneficiaryClinicalHistoryModel> chModels) {
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
