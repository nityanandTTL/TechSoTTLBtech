package com.thyrocare.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.thyrocare.R;
import com.thyrocare.activity.AddEditBeneficiaryDetailsActivity;
import com.thyrocare.adapter.DisplaySelectedTestsListForCancellationAdapter;
import com.thyrocare.delegate.AddTestListDialogDelegate;
import com.thyrocare.delegate.CloseTestsDisplayDialogButtonDialogDelegate;
import com.thyrocare.delegate.RemoveSelectedTestFromListDelegate;
import com.thyrocare.fragment.BeneficiaryDetailsScanBarcodeFragment;
import com.thyrocare.models.data.BeneficiaryTestDetailsModel;
import com.thyrocare.models.data.TestRateMasterModel;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.AppConstants;
import com.thyrocare.utils.app.InputUtils;

import java.util.ArrayList;

/**
 * Created by Orion on 6/15/2017.
 */

public class DisplaySelectedTestsListForCancellationDialog extends Dialog {
    private static final String TAG = DisplaySelectedTestsListForCancellationDialog.class.getSimpleName();
    private Activity activity;
    private Button btn_addtest, btn_close;
    private ArrayList<BeneficiaryTestDetailsModel> selectedTestsListArr;
    private ListView lvTestsDisplay;
    private CloseTestsDisplayDialogButtonDialogDelegate closeTestsDisplayDialogButtonDialogDelegate;
    private DisplaySelectedTestsListForCancellationAdapter displayAdapter;
    private AddTestListDialogDelegate addTestListDialogDelegate;
    private boolean isTestEdit = false;
    private boolean isFBSpresent = false;
    private boolean isPPBSpresent = false;
    private boolean isRBSpresent = false;
    private int FastingCount = 0;
    private boolean isFBSpresentInSuperSet = false;

    private boolean isINSFApresent = false;
    private boolean isINSFApresentInSuperSet = false;
    private boolean isINSPPpresent = false;


    public DisplaySelectedTestsListForCancellationDialog(Activity activity,
                                                         ArrayList<BeneficiaryTestDetailsModel> selectedTestsListArr,
                                                         CloseTestsDisplayDialogButtonDialogDelegate closeTestsDisplayDialogButtonDialogDelegate,
                                                         AddTestListDialogDelegate addTestListDialogDelegate) {
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
                FastingCount = 0;
                isFBSpresent = false;
                isPPBSpresent = false;
                isRBSpresent = false;
                isFBSpresentInSuperSet = false;

                isINSFApresent = false;
                isINSFApresentInSuperSet = false;
                isINSPPpresent = false;

                ArrayList<TestRateMasterModel> trmmArr = new ArrayList<TestRateMasterModel>();
                try {
                    for (BeneficiaryTestDetailsModel btdm :
                            selectedTestsListArr) {

                        if (btdm.getTests().equalsIgnoreCase(AppConstants.FBS)) {
                            isFBSpresent = true;
                        }
                        if (btdm.getTests().equalsIgnoreCase(AppConstants.INSFA)) {
                            isINSFApresent = true;
                        }

                        try {
                            if (btdm.getChldtests() != null) {
                                if (btdm.getChldtests().size() != 0) {
                                    for (int i = 0; i < btdm.getChldtests().size(); i++) {
                                        if (btdm.getChldtests().get(i).getChildTestCode() != null) {
                                            if (btdm.getChldtests().get(i).getChildTestCode().toString().trim().equalsIgnoreCase(AppConstants.FBS)) {
                                                isFBSpresent = true;
                                                isFBSpresentInSuperSet = true;
                                            }
                                            if (btdm.getChldtests().get(i).getChildTestCode().toString().trim().equalsIgnoreCase(AppConstants.INSFA)) {
                                                isINSFApresent = true;
                                                isINSFApresentInSuperSet=true;
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (btdm.getTests().equalsIgnoreCase(AppConstants.INSPP)) {
                            isINSPPpresent = true;

                        }
                        if (btdm.getTests().equalsIgnoreCase(AppConstants.PPBS)) {
                            isPPBSpresent = true;
                        }

                        if (btdm.getTests().equalsIgnoreCase(AppConstants.RBS)) {
                            isRBSpresent = true;
                        }

                        if (btdm.getFasting().equalsIgnoreCase("Fasting")) {
                            FastingCount = FastingCount + 1;
                            Logger.error("FastingCount " + FastingCount);
                        }


                       /* if( !btdm.getTests().equalsIgnoreCase(AppConstants.FBS)
                                && isFBSpresent==true && FastingCount==2 &&
                                btdm.getFasting().equalsIgnoreCase("Fasting") )
                        {
                            Toast.makeText(activity, " testing ??.", Toast.LENGTH_SHORT).show();
                        }*/


                        TestRateMasterModel trmm = new TestRateMasterModel();
                        trmm.setSampltype(btdm.getSampleType());
                        trmm.setTstClinicalHistory(btdm.getTstClinicalHistory());
                        trmm.setFasting(btdm.getFasting());
                        trmm.setTestType(btdm.getTestType());
                        trmm.setTestCode(InputUtils.isNull(btdm.getProjId()) ? btdm.getTests() : btdm.getProjId());
                        if (!InputUtils.isNull(btdm.getProjId())) {
                            trmm.setTestType("OFFER");
                        }
                        trmm.setDescription(btdm.getTests());
                        trmm.setChldtests(btdm.getChldtests());
                        trmmArr.add(trmm);
                    }

                } catch (Exception e) {
                    dismiss();
                    e.printStackTrace();
                }
                Log.e(TAG, "isFBSpresent: " + isFBSpresent);
                Log.e(TAG, "FastingCount: " + FastingCount);

                if (isFBSpresent == true && FastingCount == 1 && !isFBSpresentInSuperSet) {
                    Log.e(TAG, "onClick: 11");
                    Toast.makeText(activity, "With FBS atleast one fasting test should be there", Toast.LENGTH_SHORT).show();
                } else if (isPPBSpresent && !isFBSpresent /*&& !isFBSpresentInSuperSet*/) {
                    Toast.makeText(activity, "To Avail PPBS You have to select FBS test", Toast.LENGTH_SHORT).show();
                } else if (isRBSpresent && !isFBSpresent /*&& !isFBSpresentInSuperSet*/) {
                    Toast.makeText(activity, "To Avail RBS You have to select FBS test", Toast.LENGTH_SHORT).show();
                } else if (isINSPPpresent && !isINSFApresent) {
                    Toast.makeText(activity, "To Avail INSPP You have to select INSFA test", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "onClick: 222");
                    dismiss();
                    if (isTestEdit) {
                        AddEditBeneficiaryDetailsActivity.testEdit = "yes";
                        Logger.error("test edited");
                        BeneficiaryDetailsScanBarcodeFragment.isTestListEdited = "yes";
                    } else {
                        AddEditBeneficiaryDetailsActivity.testEdit = "no";
                        Logger.error("test not edited");
                        BeneficiaryDetailsScanBarcodeFragment.isTestListEdited = "no";
                    }

                    closeTestsDisplayDialogButtonDialogDelegate.onItemClick(trmmArr, isTestEdit);


                }

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
                BeneficiaryDetailsScanBarcodeFragment.isTestListEdited = "yes";
                AddEditBeneficiaryDetailsActivity.testEdit = "yes";
                isTestEdit = true;
                selectedTestsListArr = selectedTests;
                displayAdapter.notifyDataSetChanged();
            }
        });
        lvTestsDisplay.setAdapter(displayAdapter);
    }
}

/*
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.thyrocare.R;
import com.thyrocare.activity.AddEditBeneficiaryDetailsActivity;
import com.thyrocare.adapter.DisplaySelectedTestsListForCancellationAdapter;
import com.thyrocare.delegate.AddTestListDialogDelegate;
import com.thyrocare.delegate.CloseTestsDisplayDialogButtonDialogDelegate;
import com.thyrocare.delegate.RemoveSelectedTestFromListDelegate;
import com.thyrocare.fragment.BeneficiaryDetailsScanBarcodeFragment;
import com.thyrocare.models.data.BeneficiaryTestDetailsModel;
import com.thyrocare.models.data.TestRateMasterModel;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.AppConstants;
import com.thyrocare.utils.app.InputUtils;

import java.util.ArrayList;

*/
/**
 * Created by Orion on 6/15/2017.
 *//*


public class DisplaySelectedTestsListForCancellationDialog extends Dialog {
    private static final String TAG = DisplaySelectedTestsListForCancellationDialog.class.getSimpleName();
    private Activity activity;
    private Button btn_addtest, btn_close;
    private ArrayList<BeneficiaryTestDetailsModel> selectedTestsListArr;
    private ListView lvTestsDisplay;
    private CloseTestsDisplayDialogButtonDialogDelegate closeTestsDisplayDialogButtonDialogDelegate;
    private DisplaySelectedTestsListForCancellationAdapter displayAdapter;
    private AddTestListDialogDelegate addTestListDialogDelegate;
    private boolean isTestEdit = false;
    private boolean isFBSpresent = false;
    private boolean isPPBSpresent = false;
    private boolean isRBSpresent = false;
    private int FastingCount = 0;

    public DisplaySelectedTestsListForCancellationDialog(Activity activity,
                                                         ArrayList<BeneficiaryTestDetailsModel> selectedTestsListArr,
                                                         CloseTestsDisplayDialogButtonDialogDelegate closeTestsDisplayDialogButtonDialogDelegate,
                                                         AddTestListDialogDelegate addTestListDialogDelegate) {
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
                FastingCount = 0;
                isFBSpresent = false;
                isPPBSpresent = false;
                isRBSpresent = false;
                ArrayList<TestRateMasterModel> trmmArr = new ArrayList<TestRateMasterModel>();
                try {
                    for (BeneficiaryTestDetailsModel btdm :
                            selectedTestsListArr) {

                        if (btdm.getTests().equalsIgnoreCase(AppConstants.FBS)) {
                            isFBSpresent = true;
                        }

                        try {
                            if (btdm.getChldtests() != null) {
                                if (btdm.getChldtests().size() != 0) {
                                    for (int i = 0; i < btdm.getChldtests().size(); i++) {
                                        if (btdm.getChldtests().get(i).getChildTestCode() != null) {
                                            if (btdm.getChldtests().get(i).getChildTestCode().toString().trim().equalsIgnoreCase(AppConstants.FBS)) {
                                                isFBSpresent = true;
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (btdm.getTests().equalsIgnoreCase(AppConstants.PPBS)) {
                            isPPBSpresent = true;
                        }

                        if (btdm.getTests().equalsIgnoreCase(AppConstants.RBS)) {
                            isRBSpresent = true;
                        }

                        if (btdm.getFasting().equalsIgnoreCase("Fasting")) {
                            FastingCount = FastingCount + 1;
                            Logger.error("FastingCount " + FastingCount);
                        }


                       */
/* if( !btdm.getTests().equalsIgnoreCase(AppConstants.FBS)
                                && isFBSpresent==true && FastingCount==2 &&
                                btdm.getFasting().equalsIgnoreCase("Fasting") )
                        {
                            Toast.makeText(activity, " testing ??.", Toast.LENGTH_SHORT).show();
                        }*//*



                        TestRateMasterModel trmm = new TestRateMasterModel();
                        trmm.setSampltype(btdm.getSampleType());
                        trmm.setTstClinicalHistory(btdm.getTstClinicalHistory());
                        trmm.setFasting(btdm.getFasting());
                        trmm.setTestType(btdm.getTestType());
                        trmm.setTestCode(InputUtils.isNull(btdm.getProjId()) ? btdm.getTests() : btdm.getProjId());
                        if (!InputUtils.isNull(btdm.getProjId())) {
                            trmm.setTestType("OFFER");
                        }
                        trmm.setDescription(btdm.getTests());
                        trmm.setChldtests(btdm.getChldtests());
                        trmmArr.add(trmm);
                    }

                } catch (Exception e) {
                    dismiss();
                    e.printStackTrace();
                }
                Log.e(TAG, "isFBSpresent: " + isFBSpresent);
                Log.e(TAG, "FastingCount: " + FastingCount);

                if (isFBSpresent == true && FastingCount == 1) {
                    Log.e(TAG, "onClick: 11");
                    Toast.makeText(activity, "With FBS atleast one fasting test should be there", Toast.LENGTH_SHORT).show();
                } else if (isPPBSpresent && !isFBSpresent) {
                    Toast.makeText(activity, "To Avail PPBS You have to select FBS test", Toast.LENGTH_SHORT).show();
                } else if (isRBSpresent && !isFBSpresent) {
                    Toast.makeText(activity, "To Avail RBS You have to select FBS test", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "onClick: 222");
                    dismiss();
                    if (isTestEdit) {
                        AddEditBeneficiaryDetailsActivity.testEdit = "yes";
                        Logger.error("test edited");
                        BeneficiaryDetailsScanBarcodeFragment.isTestListEdited = "yes";
                    } else {
                        AddEditBeneficiaryDetailsActivity.testEdit = "no";
                        Logger.error("test not edited");
                        BeneficiaryDetailsScanBarcodeFragment.isTestListEdited = "no";
                    }

                    closeTestsDisplayDialogButtonDialogDelegate.onItemClick(trmmArr, isTestEdit);


                }

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
                BeneficiaryDetailsScanBarcodeFragment.isTestListEdited = "yes";
                AddEditBeneficiaryDetailsActivity.testEdit = "yes";
                isTestEdit = true;
                selectedTestsListArr = selectedTests;
                displayAdapter.notifyDataSetChanged();
            }
        });
        lvTestsDisplay.setAdapter(displayAdapter);
    }
}
*/
