package com.dhb.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

import com.dhb.R;
import com.dhb.adapter.DisplaySelectedTestsListForCancellationAdapter;
import com.dhb.delegate.AddTestListDialogDelegate;
import com.dhb.delegate.CloseTestsDisplayDialogButtonDialogDelegate;
import com.dhb.delegate.RemoveSelectedTestFromListDelegate;
import com.dhb.models.data.BeneficiaryTestDetailsModel;
import com.dhb.models.data.TestRateMasterModel;
import com.dhb.utils.app.InputUtils;

import java.util.ArrayList;

/**
 * Created by E4904 on 6/15/2017.
 */

public class DisplaySelectedTestsListForCancellationDialog extends Dialog {
    private Activity activity;
    private Button btn_addtest, btn_close;
    private ArrayList<BeneficiaryTestDetailsModel> selectedTestsListArr;
    private ListView lvTestsDisplay;
    private CloseTestsDisplayDialogButtonDialogDelegate closeTestsDisplayDialogButtonDialogDelegate;
    private DisplaySelectedTestsListForCancellationAdapter displayAdapter;
    private AddTestListDialogDelegate addTestListDialogDelegate;
    private boolean isTestEdit = false;

    public DisplaySelectedTestsListForCancellationDialog(Activity activity, ArrayList<BeneficiaryTestDetailsModel> selectedTestsListArr, CloseTestsDisplayDialogButtonDialogDelegate closeTestsDisplayDialogButtonDialogDelegate, AddTestListDialogDelegate addTestListDialogDelegate) {
        super(activity);
        this.activity = activity;
        this.setCanceledOnTouchOutside(false);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.selectedTestsListArr = selectedTestsListArr;
        this.closeTestsDisplayDialogButtonDialogDelegate = closeTestsDisplayDialogButtonDialogDelegate;
        this.addTestListDialogDelegate = addTestListDialogDelegate;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dailog_edit_order);
        initUI();
        setListners();
    }

    @Override
    public void onBackPressed() {
        btn_close.performClick();
        super.onBackPressed();
    }

    private void setListners() {
        btn_addtest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                addTestListDialogDelegate.onItemClick(selectedTestsListArr);
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<TestRateMasterModel> trmmArr =new ArrayList<TestRateMasterModel>();
                for (BeneficiaryTestDetailsModel btdm:
                     selectedTestsListArr) {
                    TestRateMasterModel trmm = new TestRateMasterModel();
                    trmm.setSampltype(btdm.getSampleType());
                    trmm.setTstClinicalHistory(btdm.getTstClinicalHistory());
                    trmm.setFasting(btdm.getFasting());
                    trmm.setTestType(btdm.getTestType());
                    trmm.setTestCode(InputUtils.isNull(btdm.getProjId())?btdm.getTests():btdm.getProjId());
                    if(!InputUtils.isNull(btdm.getProjId())){
                        trmm.setTestType("OFFER");
                    }
                    trmm.setDescription(btdm.getTests());
                    trmm.setChldtests(btdm.getChldtests());
                    trmmArr.add(trmm);
                }
                dismiss();
                closeTestsDisplayDialogButtonDialogDelegate.onItemClick(trmmArr,isTestEdit);
            }
        });
    }

    private void initUI() {
        btn_addtest = (Button) findViewById(R.id.btn_addtest);
        btn_close = (Button) findViewById(R.id.btn_cancel);
        lvTestsDisplay = (ListView) findViewById(R.id.test_names);

        displayAdapter = new DisplaySelectedTestsListForCancellationAdapter(activity, selectedTestsListArr, new RemoveSelectedTestFromListDelegate() {
            @Override
            public void onRemoveButtonClicked(ArrayList<BeneficiaryTestDetailsModel> selectedTests) {
                isTestEdit = true;
                selectedTestsListArr = selectedTests;
                displayAdapter.notifyDataSetChanged();
            }
        });
        lvTestsDisplay.setAdapter(displayAdapter);
    }
}
