package com.thyrocare.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.thyrocare.R;
import com.thyrocare.activity.AddEditBeneficiaryDetailsActivity;
import com.thyrocare.activity.DisplayTestsMasterListActivity;
import com.thyrocare.activity.OrderBookingActivity;
import com.thyrocare.dao.DhbDao;
import com.thyrocare.dao.models.BeneficiaryDetailsDao;
import com.thyrocare.dao.models.LabAlertMasterDao;
import com.thyrocare.dao.models.OrderDetailsDao;
import com.thyrocare.delegate.AddSampleBarcodeDialogDelegate;
import com.thyrocare.delegate.AddTestListDialogDelegate;
import com.thyrocare.delegate.CloseTestsDisplayDialogButtonDialogDelegate;
import com.thyrocare.delegate.OrderCancelDialogButtonClickedDelegate;
import com.thyrocare.delegate.OrderRescheduleDialogButtonClickedDelegate;
import com.thyrocare.delegate.RefreshBeneficiariesSliderDelegate;
import com.thyrocare.delegate.SelectClinicalHistoryCheckboxDelegate;
import com.thyrocare.delegate.SelectLabAlertsCheckboxDelegate;
import com.thyrocare.dialog.AddSampleBarcodeDialog;
import com.thyrocare.dialog.CancelOrderDialog;
import com.thyrocare.dialog.ClinicalHistorySelectorDialog;
import com.thyrocare.dialog.DisplaySelectedTestsListForCancellationDialog;
import com.thyrocare.dialog.LabAlertSelectorDialog;
import com.thyrocare.dialog.RescheduleOrderDialog;
import com.thyrocare.models.api.request.OrderBookingRequestModel;
import com.thyrocare.models.api.request.OrderStatusChangeRequestModel;
import com.thyrocare.models.api.request.RemoveBeneficiaryAPIRequestModel;
import com.thyrocare.models.api.response.GetTestListResponseModel;
import com.thyrocare.models.api.response.OrderBookingResponseBeneficiaryModel;
import com.thyrocare.models.api.response.OrderBookingResponseOrderModel;
import com.thyrocare.models.api.response.OrderBookingResponseVisitModel;
import com.thyrocare.models.data.BeneficiaryBarcodeDetailsModel;
import com.thyrocare.models.data.BeneficiaryDetailsModel;
import com.thyrocare.models.data.BeneficiaryLabAlertsModel;
import com.thyrocare.models.data.BeneficiarySampleTypeDetailsModel;
import com.thyrocare.models.data.BeneficiaryTestDetailsModel;
import com.thyrocare.models.data.BeneficiaryTestWiseClinicalHistoryModel;
import com.thyrocare.models.data.ChildTestsModel;
import com.thyrocare.models.data.LabAlertMasterModel;
import com.thyrocare.models.data.OrderBookingDetailsModel;
import com.thyrocare.models.data.OrderDetailsModel;
import com.thyrocare.models.data.OrderVisitDetailsModel;
import com.thyrocare.models.data.REMOVEBENSMSPOSTDATAModel;
import com.thyrocare.models.data.TestClinicalHistoryModel;
import com.thyrocare.models.data.TestGroupListModel;
import com.thyrocare.models.data.TestRateMasterModel;
import com.thyrocare.models.data.TestSampleTypeModel;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.network.ResponseParser;
import com.thyrocare.uiutils.AbstractFragment;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.AppConstants;
import com.thyrocare.utils.app.AppPreferenceManager;
import com.thyrocare.utils.app.BundleConstants;
import com.thyrocare.utils.app.CommonUtils;
import com.thyrocare.utils.app.DeviceUtils;
import com.thyrocare.utils.app.InputUtils;

import org.json.JSONException;

import java.util.ArrayList;


public class BeneficiaryDetailsScanBarcodeFragment extends AbstractFragment {
    public static final String TAG_FRAGMENT = BeneficiaryDetailsScanBarcodeFragment.class.getSimpleName();
    private ImageView imgVenipuncture, imgHC;
    private TextView txtSrNo, tv_orderno;
    private TextView txtName;
    private TextView txtAge;
    private TextView txtAadharNo;
    private ImageView btnRelease, btnEdit;
    private TextView edtTests, textView3;
    ImageView img_view_test;
    private LinearLayout llBarcodes;
    private TextView edtCH, edtLA;
    private EditText edtRemarks;
    private BeneficiaryDetailsModel beneficiaryDetailsModel;
    private OrderBookingActivity activity;
    private String encodedVanipunctureImg;
    private static final int REQUEST_CAMERA = 100;
    private Bitmap thumbnail;
    private View rootview;
    private boolean isHC = false;
    private String userChoosenReleaseTask;
    private OrderDetailsModel orderDetailsModel;
    private String currentScanSampleType;
    private RescheduleOrderDialog cdd;
    private CancelOrderDialog cod;
    private boolean isCancelRequesGenereted = false;
    private DhbDao dhbDao;
    private ArrayList<BeneficiaryTestWiseClinicalHistoryModel> benCHArr;
    private ArrayList<BeneficiaryLabAlertsModel> benLAArr;
    private ArrayList<LabAlertMasterModel> labAlertsArr;
    private BeneficiaryDetailsDao beneficiaryDetailsDao;
    private OrderDetailsDao orderDetailsDao;
    private LabAlertMasterDao labAlertMasterDao;
    private TableLayout tlBarcodes;
    private IntentIntegrator intentIntegrator;
    private static RefreshBeneficiariesSliderDelegate refreshBeneficiariesSliderDelegateResult;
    private ImageView imgAadhar;
    public static String isTestListEdited = "no";
    public static String isTestListEditedFromAddBen = "no";
    ArrayList<TestGroupListModel> testGroupListModelArrayList;

    GetTestListResponseModel TestListResponseModel;
    CharSequence[] items;

    public BeneficiaryDetailsScanBarcodeFragment() {
        // Required empty public constructor
    }

    public static BeneficiaryDetailsScanBarcodeFragment newInstance(Bundle bundle, RefreshBeneficiariesSliderDelegate refreshBeneficiariesSliderDelegate) {
        BeneficiaryDetailsScanBarcodeFragment fragment = new BeneficiaryDetailsScanBarcodeFragment();
        fragment.setArguments(bundle);
        refreshBeneficiariesSliderDelegateResult = refreshBeneficiariesSliderDelegate;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_beneficiary_details_scan_barcode, container, false);
        activity = (OrderBookingActivity) getActivity();
        appPreferenceManager = new AppPreferenceManager(activity);

        dhbDao = new DhbDao(activity);
        beneficiaryDetailsDao = new BeneficiaryDetailsDao(dhbDao.getDb());
        orderDetailsDao = new OrderDetailsDao(dhbDao.getDb());
        labAlertMasterDao = new LabAlertMasterDao(dhbDao.getDb());

        labAlertsArr = labAlertMasterDao.getAllModels();
        benCHArr = new ArrayList<>();
        benLAArr = new ArrayList<>();
        Bundle bundle = getArguments();
        beneficiaryDetailsModel = bundle.getParcelable(BundleConstants.BENEFICIARY_DETAILS_MODEL);
        orderDetailsModel = bundle.getParcelable(BundleConstants.ORDER_DETAILS_MODEL);

        initUI();
        initData();
        setListeners();
        return rootview;
    }

    private void initData() {
        beneficiaryDetailsModel = beneficiaryDetailsDao.getModelFromId(beneficiaryDetailsModel.getBenId());
        orderDetailsModel = orderDetailsDao.getModelFromId(orderDetailsModel.getOrderNo());
        if (beneficiaryDetailsModel != null && orderDetailsModel != null) {
            if (beneficiaryDetailsModel.getClHistory() != null) {
                benCHArr = beneficiaryDetailsModel.getClHistory();
            }
            if (beneficiaryDetailsModel.getLabAlert() != null) {
                benLAArr = beneficiaryDetailsModel.getLabAlert();
            }
            if (beneficiaryDetailsModel.getVenepuncture() != null) {
                encodedVanipunctureImg = beneficiaryDetailsModel.getVenepuncture();
            }
            String chS = "";
            if (benCHArr != null && benCHArr.size() > 0) {
                for (BeneficiaryTestWiseClinicalHistoryModel chm :
                        benCHArr) {
                    if (InputUtils.isNull(chS))
                        chS = "" + chm.getClinicalHistoryId();
                    else
                        chS = chS + ", " + chm.getClinicalHistoryId();
                }
            }
            edtCH.setText(chS);

            String laS = "";
            if (benLAArr != null && benLAArr.size() > 0) {
                for (BeneficiaryLabAlertsModel lam :
                        benLAArr) {
                    LabAlertMasterModel labAlertMasterModel = labAlertMasterDao.getModelFromId(lam.getLabAlertId() + "");
                    if (labAlertMasterModel != null) {
                        if (InputUtils.isNull(laS))
                            laS = "" + labAlertMasterModel.getLabAlert();
                        else
                            laS = laS + ", " + labAlertMasterModel.getLabAlert();
                    }
                }
            }
            edtLA.setText(laS);

            txtName.setText(beneficiaryDetailsModel.getName());
            tv_orderno.setText(beneficiaryDetailsModel.getOrderNo());
            txtAge.setText(beneficiaryDetailsModel.getAge() + " | " + beneficiaryDetailsModel.getGender());
            if (InputUtils.isNull(beneficiaryDetailsModel.getAadhar())) {
                txtAadharNo.setVisibility(View.GONE);
                imgAadhar.setVisibility(View.GONE);
            } else {
                txtAadharNo.setVisibility(View.VISIBLE);
                imgAadhar.setVisibility(View.VISIBLE);
                txtAadharNo.setText(beneficiaryDetailsModel.getAadhar());
            }
            Logger.error("test code "+beneficiaryDetailsModel.getTestsCode());
            edtTests.setText(beneficiaryDetailsModel.getTestsCode());
            edtRemarks.setText(beneficiaryDetailsModel.getRemarks());

            if (beneficiaryDetailsModel.getTestsCode().equalsIgnoreCase(AppConstants.PPBS)
                    || beneficiaryDetailsModel.getTestsCode().equalsIgnoreCase(AppConstants.INSPP)
                    || beneficiaryDetailsModel.getTestsCode().equalsIgnoreCase(AppConstants.PPBS+","+AppConstants.INSPP)) {
Logger.error("");
                edtTests.setEnabled(false);
                imgHC.setEnabled(false);
                btnRelease.setVisibility(View.GONE);
                btnEdit.setVisibility(View.INVISIBLE);
            } else {
                edtTests.setEnabled(true);
                imgHC.setEnabled(true);
                btnRelease.setVisibility(View.VISIBLE);
                btnEdit.setVisibility(View.VISIBLE);
            }
            //here to add
            txtSrNo.setText("1");
           // txtSrNo.setText(beneficiaryDetailsModel.getBenId() + "");
            if (orderDetailsModel != null && orderDetailsModel.getReportHC() == 0) {

                imgHC.setImageDrawable(getResources().getDrawable(R.drawable.tick_icon));
                isHC = false;
            } else {
                imgHC.setImageDrawable(getResources().getDrawable(R.drawable.green_tick_icon));
                isHC = true;
            }

            if (orderDetailsModel.isEditHC()) {
                imgHC.setVisibility(View.VISIBLE);
                textView3.setVisibility(View.VISIBLE);
            } else {
                imgHC.setVisibility(View.GONE);
                textView3.setVisibility(View.GONE);
            }
//jai
            if(beneficiaryDetailsModel.getTestsCode().equalsIgnoreCase(AppConstants.PPBS)
                    || beneficiaryDetailsModel.getTestsCode().equalsIgnoreCase(AppConstants.INSPP)
                    || beneficiaryDetailsModel.getTestsCode().equalsIgnoreCase(AppConstants.PPBS+","+AppConstants.INSPP)){

            }else {
                Logger.error("not pp insp");
                if(orderDetailsModel.isEditOrder()){
                    edtTests.setEnabled(true);
                    Logger.error("isEditOrder scan "+orderDetailsModel.isEditOrder());
                }else {
                    edtTests.setEnabled(false);
                    Logger.error("isEditOrder scan "+orderDetailsModel.isEditOrder());
                }
            }

            //jai
            boolean isBarcodeAndSampleListSame = false;
            if (beneficiaryDetailsModel != null
                    && beneficiaryDetailsModel.getBarcodedtl() != null
                    && beneficiaryDetailsModel.getSampleType() != null
                    && beneficiaryDetailsModel.getBarcodedtl().size() == beneficiaryDetailsModel.getSampleType().size()) {
                int sameSampleTypeCnt = 0;
                for (BeneficiaryBarcodeDetailsModel bbdm :
                        beneficiaryDetailsModel.getBarcodedtl()) {
                    for (BeneficiarySampleTypeDetailsModel bstdm :
                            beneficiaryDetailsModel.getSampleType()) {
                        if (bbdm.getSamplType().equals(bstdm.getSampleType())) {
                            sameSampleTypeCnt++;
                        }
                    }
                }
                if (sameSampleTypeCnt == beneficiaryDetailsModel.getBarcodedtl().size() && sameSampleTypeCnt == beneficiaryDetailsModel.getSampleType().size()) {
                    isBarcodeAndSampleListSame = true;
                }
            }
            if (!isBarcodeAndSampleListSame) {
                if (beneficiaryDetailsModel.getBarcodedtl() == null) {
                    beneficiaryDetailsModel.setBarcodedtl(new ArrayList<BeneficiaryBarcodeDetailsModel>());
                } else {
                    beneficiaryDetailsModel.getBarcodedtl().clear();
                }
                for (BeneficiarySampleTypeDetailsModel sampleTypes :
                        beneficiaryDetailsModel.getSampleType()) {
                    Logger.error("sample type: " + beneficiaryDetailsModel.getSampleType());
                    BeneficiaryBarcodeDetailsModel beneficiaryBarcodeDetailsModel = new BeneficiaryBarcodeDetailsModel();
                    beneficiaryBarcodeDetailsModel.setBenId(beneficiaryDetailsModel.getBenId());
                    beneficiaryBarcodeDetailsModel.setId(DeviceUtils.getRandomUUID());
                    beneficiaryBarcodeDetailsModel.setSamplType(sampleTypes.getSampleType());
                    beneficiaryBarcodeDetailsModel.setOrderNo(beneficiaryDetailsModel.getOrderNo());
                    beneficiaryDetailsModel.getBarcodedtl().add(beneficiaryBarcodeDetailsModel);
                }
                beneficiaryDetailsDao.insertOrUpdate(beneficiaryDetailsModel);
            }
            if (!InputUtils.isNull(beneficiaryDetailsModel.getVenepuncture())) {
                imgVenipuncture.setImageDrawable(activity.getResources().getDrawable(R.drawable.camera_blue));
            }
            initScanBarcodeView();
        }
    }

    private void setListeners() {
        imgVenipuncture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraIntent();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEdit = new Intent(activity, AddEditBeneficiaryDetailsActivity.class);
                intentEdit.putExtra(BundleConstants.BENEFICIARY_DETAILS_MODEL, beneficiaryDetailsModel);
                intentEdit.putExtra(BundleConstants.ORDER_DETAILS_MODEL, orderDetailsModel);
                intentEdit.putExtra(BundleConstants.IS_BENEFICIARY_EDIT, true);
                intentEdit.putExtra(BundleConstants.IS_TEST_EDIT, true);
                startActivityForResult(intentEdit, BundleConstants.EDIT_START);
            }
        });
        imgHC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHC) {
                    isHC = false;
                    imgHC.setImageDrawable(activity.getResources().getDrawable(R.drawable.tick_icon));
                    orderDetailsModel.setReportHC(0);
                } else {
                    isHC = true;
                    imgHC.setImageDrawable(activity.getResources().getDrawable(R.drawable.green_tick_icon));
                    orderDetailsModel.setReportHC(1);
                }
                orderDetailsDao.insertOrUpdate(orderDetailsModel);
                refreshBeneficiariesSliderDelegateResult.onRefreshActionCallbackReceived(orderDetailsDao.getOrderVisitModel(orderDetailsModel.getVisitId()));
            }
        });
        btnRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appPreferenceManager.getLoginResponseModel().getRole().equals(AppConstants.NBTTSP_ROLE_ID)) {
//jai
                    if (orderDetailsModel.isEditOrder()){
                        items = new String[]{
                                "Remove Beneficiary"};
                        Logger.error("isEditOrder Remove Beneficiary "+orderDetailsModel.isEditOrder());
                    }else {
                        items = new String[]{
                                "Cannot Remove Beneficiary"};
                        Logger.error("isEditOrder Remove Beneficiary "+orderDetailsModel.isEditOrder());
                    }

//jai


                } else {

                    /*if(orderDetailsModel.isEditOrder()){
                        items = new String[]{"Order Reschedule",
                                "Visit Cancellation", "Remove Beneficiary"};
                    }else {
                        items = new String[]{"Order Reschedule",
                                "Visit Cancellation"};
                    }*/

                    //TODO Visit Cancellation Removed
                    if(orderDetailsModel.isEditOrder()){
                        items = new String[]{"Order Reschedule", "Remove Beneficiary"};
                    }else {
                        items = new String[]{"Order Reschedule"};
                    }

                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Select Action");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Order Reschedule")) {
                            userChoosenReleaseTask = "Order Reschedule";
                            cdd = new RescheduleOrderDialog(activity, new OrderRescheduleDialogButtonClickedDelegateResult(), orderDetailsModel);
                            cdd.show();
                        } else if (items[item].equals("Visit Cancellation")) {
                            userChoosenReleaseTask = "Visit Cancellation";
                            cod = new CancelOrderDialog(activity, new OrderCancelDialogButtonClickedDelegateResult(), orderDetailsModel);
                            cod.show();
                        } else if (items[item].equals("Remove Beneficiary")) {
                            userChoosenReleaseTask = "Remove Beneficiary";
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                            builder1.setTitle("Confirm Action")
                                    .setMessage("Do you really want to remove beneficiary?")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(final DialogInterface dialog, int which) {
                                            RemoveBeneficiaryAPIRequestModel rb = new RemoveBeneficiaryAPIRequestModel();
                                            rb.setBenId(beneficiaryDetailsModel.getBenId());
                                            rb.setOrderNo(beneficiaryDetailsModel.getOrderNo());
                                            rb.setIsAdded(orderDetailsModel.isAddBen() ? "1" : "0");

                                            //set id for Remove ben SMS
                                            BundleConstants.RemoveBenId = beneficiaryDetailsModel.getBenId();

                                            ApiCallAsyncTask removeBeneficiaryAsyncTask = new AsyncTaskForRequest(activity).getRemoveBeneficiaryRequestAsyncTask(rb);
                                            removeBeneficiaryAsyncTask.setApiCallAsyncTaskDelegate(new ApiCallAsyncTaskDelegate() {
                                                @Override
                                                public void apiCallResult(String json, int statusCode) throws JSONException {
                                                    if (statusCode == 200) {
                                                        ResponseParser responseParser = new ResponseParser(activity);
                                                        OrderVisitDetailsModel orderVisitDetailsModel = responseParser.getRemoveBeneficiaryAPIResponseModel(json, statusCode);
                                                        if (orderVisitDetailsModel.getAllOrderdetails() != null && orderVisitDetailsModel.getAllOrderdetails().size() > 0) {
                                                            orderDetailsDao.deleteByVisitId(orderVisitDetailsModel.getVisitId());
//                                                            beneficiaryDetailsDao.deleteByBenId(beneficiaryDetailsModel.getBenId() + "");
                                                            for (OrderDetailsModel orderDetailsModel :
                                                                    orderVisitDetailsModel.getAllOrderdetails()) {
                                                                orderDetailsModel.setVisitId(orderVisitDetailsModel.getVisitId());
                                                                orderDetailsModel.setResponse(orderVisitDetailsModel.getResponse());
                                                                orderDetailsModel.setSlot(orderVisitDetailsModel.getSlot());
                                                                orderDetailsModel.setSlotId(orderVisitDetailsModel.getSlotId());
                                                                orderDetailsModel.setAmountPayable(orderDetailsModel.getAmountDue());
                                                                orderDetailsModel.setEstIncome(orderVisitDetailsModel.getEstIncome());
                                                                //jai
                                                               // orderDetailsModel.setEditOrder(orderVisitDetailsModel.get());
                                                                // jai



                                                                if (orderDetailsModel.getBenMaster() != null && orderDetailsModel.getBenMaster().size() > 0) {
                                                                    for (BeneficiaryDetailsModel beneficiaryDetailsModel :
                                                                            orderDetailsModel.getBenMaster()) {
                                                                        beneficiaryDetailsModel.setOrderNo(orderDetailsModel.getOrderNo());
                                                                        beneficiaryDetailsModel.setTests(beneficiaryDetailsModel.getTestsCode());
                                                                        for (int i = 0; i < beneficiaryDetailsModel.getSampleType().size(); i++) {
                                                                            beneficiaryDetailsModel.getSampleType().get(i).setBenId(beneficiaryDetailsModel.getBenId());
                                                                        }
                                                                        beneficiaryDetailsDao.insertOrUpdate(beneficiaryDetailsModel);
                                                                    }
                                                                    orderDetailsDao.insertOrUpdate(orderDetailsModel);
                                                                }
                                                            }
                                                            //CallApiForRemoveBenSMS(BundleConstants.RemoveBenId, orderVisitDetailsModel.getAllOrderdetails().get(0).getOrderNo(), orderVisitDetailsModel.getAllOrderdetails().get(0).getAmountDue());
                                                            Toast.makeText(activity, "Beneficiary Removed Successfully", Toast.LENGTH_SHORT).show();
                                                            refreshBeneficiariesSliderDelegateResult.onRefreshActionCallbackReceived(orderDetailsDao.getOrderVisitModel(orderVisitDetailsModel.getVisitId()));
                                                        }
                                                    } else {
                                                        Toast.makeText(activity, "Failed to remove beneficiary", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onApiCancelled() {
                                                    Toast.makeText(activity, "Failed to remove beneficiary", Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();
                                                }
                                            });
                                            if (orderDetailsModel.getBenMaster().size() > 1) {
                                                removeBeneficiaryAsyncTask.execute(removeBeneficiaryAsyncTask);
                                            } else {
                                                Toast.makeText(activity, "Cannot remove if only 1 Beneficiary in Order!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                        }
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
        edtRemarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String remarks = s.toString();
                if (InputUtils.isNull(remarks)) {
                    beneficiaryDetailsModel.setRemarks("");
                } else {
                    beneficiaryDetailsModel.setRemarks(remarks);
                }
                beneficiaryDetailsDao.insertOrUpdate(beneficiaryDetailsModel);
            }
        });
        img_view_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "img_view_test1", Toast.LENGTH_SHORT).show();
                if (beneficiaryDetailsModel.getLeadId() == null) {
                    Logger.error("Lead ID null");
                   /* getviewTestData("SP33337413");*/
                } else {
                    Logger.error("Lead ID " + beneficiaryDetailsModel.getLeadId());
                    getviewTestData(beneficiaryDetailsModel.getLeadId());
                }
            }
        });
        edtTests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO show tests list for addition and removal
//                Toast.makeText(getActivity(),"Feature Coming Soon Stay Tunned", Toast.LENGTH_SHORT).show();
//                if(beneficiaryDetailsModel.getTestSampleType()!=null && beneficiaryDetailsModel.getTestSampleType().size()>0) {
                DisplaySelectedTestsListForCancellationDialog dstlfcd = new DisplaySelectedTestsListForCancellationDialog(activity,
                        beneficiaryDetailsModel.getTestSampleType(),
                        new CloseTestsDisplayDialogButtonDialogDelegate() {
                            @Override
                            public void onItemClick(ArrayList<TestRateMasterModel> selectedTestsList, boolean isTestEdit) {
                                //  beneficiaryDetailsModel.setTestEdit(true);
                                Logger.error("isTestEditedt " + isTestEdit);
                                //jai

                               /* Logger.error("list status " + isTestListEdited);
                                if (isTestListEdited.equals("yes")) {
                                    orderDetailsModel.setTestEdit(true);
                                    beneficiaryDetailsModel.setTestEdit(true);
                                } else {
                                    orderDetailsModel.setTestEdit(false);
                                    beneficiaryDetailsModel.setTestEdit(false);
                                }*/

                                //jai
                                if (isTestEdit) {
                                    orderDetailsModel.setTestEdit(isTestEdit);
                                    beneficiaryDetailsModel.setTestEdit(isTestEdit);
                                }


                                orderDetailsDao.insertOrUpdate(orderDetailsModel);
                                beneficiaryDetailsModel.setProjId("");
                                ArrayList<BeneficiaryTestDetailsModel> selectedTestDetailsArr = new ArrayList<BeneficiaryTestDetailsModel>();
                                for (TestRateMasterModel trmm :
                                        selectedTestsList) {
                                    BeneficiaryTestDetailsModel btdm = new BeneficiaryTestDetailsModel();
                                    btdm.setFasting(trmm.getFasting());
                                    btdm.setChldtests(trmm.getChldtests() != null ? trmm.getChldtests() : new ArrayList<ChildTestsModel>());
                                    btdm.setTests(trmm.getTestCode());
                                    btdm.setTestType(trmm.getTestType());
                                    btdm.setProjId("");
                                    btdm.setSampleType(trmm.getSampltype() != null ? trmm.getSampltype() : new ArrayList<TestSampleTypeModel>());
                                    btdm.setTstClinicalHistory(trmm.getTstClinicalHistory() != null ? trmm.getTstClinicalHistory() : new ArrayList<TestClinicalHistoryModel>());
                                    if (!InputUtils.isNull(trmm.getTestType()) && trmm.getTestType().equalsIgnoreCase("offer")) {
                                        btdm.setProjId(trmm.getTestCode());
                                        btdm.setTests(trmm.getDescription());
                                        beneficiaryDetailsModel.setProjId(trmm.getTestCode());
                                    }
                                    selectedTestDetailsArr.add(btdm);
                                }
                                beneficiaryDetailsModel.setTestSampleType(selectedTestDetailsArr);
                                boolean isFasting = false;
                                String testsCode = "";
                                if (selectedTestsList != null) {
                                    for (TestRateMasterModel testRateMasterModel :
                                            selectedTestsList) {
                                        if (InputUtils.isNull(testsCode)) {
                                            if (!InputUtils.isNull(testRateMasterModel.getTestType()) && testRateMasterModel.getTestType().equals("OFFER")) {
                                                testsCode = testRateMasterModel.getDescription();
                                                beneficiaryDetailsModel.setProjId(testRateMasterModel.getTestCode());
                                            } else {
                                                testsCode = testRateMasterModel.getTestCode();
                                            }
                                        } else {
                                            if (!InputUtils.isNull(testRateMasterModel.getTestType()) && testRateMasterModel.getTestType().equals("OFFER")) {
                                                testsCode = testsCode + "," + testRateMasterModel.getDescription();
                                                beneficiaryDetailsModel.setProjId(testRateMasterModel.getTestCode());
                                            } else {
                                                testsCode = testsCode + "," + testRateMasterModel.getTestCode();

                                            }
                                        }
                                    }
                                    if (InputUtils.isNull(beneficiaryDetailsModel.getProjId())) {
                                        beneficiaryDetailsModel.setProjId("");
                                    }
                                    ArrayList<BeneficiarySampleTypeDetailsModel> samples = new ArrayList<>();
                                    for (TestRateMasterModel trmm :
                                            selectedTestsList) {
                                        for (TestSampleTypeModel tstm :
                                                trmm.getSampltype()) {
                                            BeneficiarySampleTypeDetailsModel bstdm = new BeneficiarySampleTypeDetailsModel();
                                            bstdm.setBenId(beneficiaryDetailsModel.getBenId());
                                            bstdm.setSampleType(tstm.getSampleType());
                                            bstdm.setId(tstm.getId());
                                            if (!samples.contains(bstdm)) {
                                                samples.add(bstdm);
                                            }
                                        }
                                    }
                                    beneficiaryDetailsModel.setSampleType(samples);
                                    for (TestRateMasterModel trmm :
                                            selectedTestsList) {
                                        if (!trmm.getFasting().toLowerCase().contains("non")) {
                                            isFasting = true;
                                            break;
                                        }
                                    }
                                }
                                beneficiaryDetailsModel.setTestsCode(testsCode);
                                beneficiaryDetailsModel.setTests(testsCode);
                                if (isFasting) {
                                    beneficiaryDetailsModel.setFasting("Fasting");
                                } else {
                                    beneficiaryDetailsModel.setFasting("Non-Fasting");
                                }
                                beneficiaryDetailsDao.insertOrUpdate(beneficiaryDetailsModel);
                                refreshBeneficiariesSliderDelegateResult.onRefreshActionCallbackReceived(orderDetailsDao.getOrderVisitModel(orderDetailsModel.getVisitId()));
                            }
                        }, new AddTestListDialogDelegate() {
                    @Override
                    public void onItemClick(ArrayList<BeneficiaryTestDetailsModel> selectedTestsList) {
                        Intent intentAddTests = new Intent(activity, DisplayTestsMasterListActivity.class);
                        intentAddTests.putExtra(BundleConstants.SELECTED_TESTS_LIST, selectedTestsList);
                        intentAddTests.putExtra(BundleConstants.ORDER_DETAILS_MODEL, orderDetailsModel);
                        intentAddTests.putExtra(BundleConstants.BENEFICIARY_DETAILS_MODEL, beneficiaryDetailsModel);

                        // intentAddTests.putExtra(BundleConstants.IS_TEST_EDIT, true);

                        startActivityForResult(intentAddTests, BundleConstants.ADD_TESTS_START);
                    }
                });
                dstlfcd.show();
                /*}
                else{
                    Intent intentAddTests = new Intent(activity, DisplayTestsMasterListActivity.class);
                    intentAddTests.putExtra(BundleConstants.SELECTED_TESTS_LIST, new ArrayList<>());
                    intentAddTests.putExtra(BundleConstants.ORDER_DETAILS_MODEL, orderDetailsModel);
                    intentAddTests.putExtra(BundleConstants.BENEFICIARY_DETAILS_MODEL, beneficiaryDetailsModel);
                    intentAddTests.putExtra(BundleConstants.IS_TEST_EDIT, true);
                    startActivityForResult(intentAddTests, BundleConstants.ADD_TESTS_START);
                }*/
            }
        });
        edtCH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO calculate clinical history
                ArrayList<BeneficiaryTestDetailsModel> forCHSelection = new ArrayList<>();
                if (beneficiaryDetailsModel.getTestSampleType() != null) {
                    for (BeneficiaryTestDetailsModel btdm :
                            beneficiaryDetailsModel.getTestSampleType()) {
                        if (btdm.getTstClinicalHistory() != null && btdm.getTstClinicalHistory().size() > 0) {
                            forCHSelection.add(btdm);
                        }
                    }
                }
                if (forCHSelection.size() > 0) {
                    ClinicalHistorySelectorDialog clinicalHistorySelectorDialog = new ClinicalHistorySelectorDialog(activity, forCHSelection, benCHArr, beneficiaryDetailsModel.getBenId(), new SelectClinicalHistoryCheckboxDelegate() {
                        @Override
                        public void onCheckChange(ArrayList<BeneficiaryTestWiseClinicalHistoryModel> chArr) {
                            benCHArr = chArr;
                            beneficiaryDetailsModel.setClHistory(benCHArr);
                            beneficiaryDetailsDao.insertOrUpdate(beneficiaryDetailsModel);
                            initData();
                        }
                    });
                    clinicalHistorySelectorDialog.show();
                } else {
                    Toast.makeText(activity, "Clinical History Not Required", Toast.LENGTH_SHORT).show();
                }
            }
        });
        edtLA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (labAlertsArr.size() > 0) {
                    LabAlertSelectorDialog labAlertSelectorDialog = new LabAlertSelectorDialog(activity, labAlertsArr, benLAArr, beneficiaryDetailsModel.getBenId(), new SelectLabAlertsCheckboxDelegate() {
                        @Override
                        public void onCheckChange(ArrayList<BeneficiaryLabAlertsModel> chArr) {
                            benLAArr = chArr;
                            beneficiaryDetailsModel.setLabAlert(benLAArr);
                            beneficiaryDetailsDao.insertOrUpdate(beneficiaryDetailsModel);
                            initData();
                        }
                    });
                    labAlertSelectorDialog.show();
                } else {
                    Toast.makeText(activity, "Lab Alerts Master Not Available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getviewTestData(String leadId) {
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchAssignedDetailApiAsyncTask = asyncTaskForRequest.getTestListAsyncTask(leadId);
        fetchAssignedDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new GetTestListApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            fetchAssignedDetailApiAsyncTask.execute(fetchAssignedDetailApiAsyncTask);
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void initUI() {
        imgVenipuncture = (ImageView) rootview.findViewById(R.id.img_venipuncture);
        tv_orderno = (TextView) rootview.findViewById(R.id.tv_orderno);
        tv_orderno.setSelected(true);
        imgHC = (ImageView) rootview.findViewById(R.id.hard_copy_check);
        textView3 = (TextView) rootview.findViewById(R.id.textView3);
        txtName = (TextView) rootview.findViewById(R.id.txt_name);
        txtName.setSelected(true);
        txtAge = (TextView) rootview.findViewById(R.id.txt_age);
        txtAadharNo = (TextView) rootview.findViewById(R.id.txt_aadhar_no);
        txtSrNo = (TextView) rootview.findViewById(R.id.txt_sr_no);
        btnEdit = (ImageView) rootview.findViewById(R.id.img_edit);
        btnRelease = (ImageView) rootview.findViewById(R.id.img_release2);
        imgAadhar = (ImageView) rootview.findViewById(R.id.title_aadhar_icon);
        edtTests = (TextView) rootview.findViewById(R.id.edt_test);
        img_view_test = (ImageView) rootview.findViewById(R.id.img_view_test);
        edtCH = (TextView) rootview.findViewById(R.id.clinical_history);
        edtLA = (TextView) rootview.findViewById(R.id.edt_lab_alerts);
        edtRemarks = (EditText) rootview.findViewById(R.id.customer_sign);
        llBarcodes = (LinearLayout) rootview.findViewById(R.id.ll_barcodes);
        tlBarcodes = (TableLayout) rootview.findViewById(R.id.tl_barcodes);
        //
    }

    private void initScanBarcodeView() {
        if (beneficiaryDetailsModel != null
                && beneficiaryDetailsModel.getBarcodedtl() != null
                && beneficiaryDetailsModel.getBarcodedtl().size() > 0) {
            tlBarcodes.removeAllViews();
            for (final BeneficiaryBarcodeDetailsModel beneficiaryBarcodeDetailsModel :
                    beneficiaryDetailsModel.getBarcodedtl()) {
                TableRow tr = (TableRow) activity.getLayoutInflater().inflate(R.layout.item_scan_barcode, null);
                TextView txtSampleType = (TextView) tr.findViewById(R.id.txt_sample_type);
                txtSampleType.setSelected(true);
                TextView edtBarcode = (TextView) tr.findViewById(R.id.edt_barcode);
                ImageView imgScan = (ImageView) tr.findViewById(R.id.scan_barcode_button);
                txtSampleType.setText(beneficiaryBarcodeDetailsModel.getSamplType());
                if (beneficiaryBarcodeDetailsModel.getSamplType().equals("SERUM")) {
                    txtSampleType.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_sample_type_serum));
                } else if (beneficiaryBarcodeDetailsModel.getSamplType().equals("EDTA")) {
                    txtSampleType.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_sample_type_edta));
                } else if (beneficiaryBarcodeDetailsModel.getSamplType().equals("FLUORIDE")) {

                    txtSampleType.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_sample_type_fluoride));
                } else if (beneficiaryBarcodeDetailsModel.getSamplType().equals("HEPARIN")) {
                    txtSampleType.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_sample_type_heparin));
                } else if (beneficiaryBarcodeDetailsModel.getSamplType().equals("URINE")) {
                    txtSampleType.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_sample_type_urine));
                } else if (beneficiaryBarcodeDetailsModel.getSamplType().equals("FLUORIDE-F")) {

                    txtSampleType.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_sample_type_fluoride));
                } else if (beneficiaryBarcodeDetailsModel.getSamplType().equals("FLUORIDE-PP")) {

                    txtSampleType.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_sample_type_fluoride));
                }
                Logger.error("beneficiaryBarcodeDetailsModel.getBarcode() " + beneficiaryBarcodeDetailsModel.getBarcode());
                Logger.error("barcode value: " + beneficiaryBarcodeDetailsModel.getBarcode());

                edtBarcode.setText(beneficiaryBarcodeDetailsModel.getBarcode());

                imgScan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Logger.error("sample type img scan click " + beneficiaryBarcodeDetailsModel.getSamplType());
                        currentScanSampleType = beneficiaryBarcodeDetailsModel.getSamplType();

                        intentIntegrator = new IntentIntegrator(activity) {
                            @Override
                            protected void startActivityForResult(Intent intent, int code) {
                                BeneficiaryDetailsScanBarcodeFragment.this.startActivityForResult(intent, BundleConstants.START_BARCODE_SCAN); // REQUEST_CODE override
                            }
                        };
                        intentIntegrator.initiateScan();
                    }
                });
                edtBarcode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentScanSampleType = beneficiaryBarcodeDetailsModel.getSamplType();
                        Logger.error("currentScanSampleType "+currentScanSampleType);
                        //jai
                        AddSampleBarcodeDialog sampleBarcodeDialog = new AddSampleBarcodeDialog(currentScanSampleType,activity, new AddSampleBarcodeDialogDelegate() {
                            @Override
                            public void onSampleBarcodeAdded(String scanned_barcode) {
                                if (!InputUtils.isNull(scanned_barcode) && scanned_barcode.length() == 8) {
                                    if (scanned_barcode.startsWith("0") || scanned_barcode.startsWith("$")) {
                                        Toast.makeText(activity, "Invalid Barcode", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (beneficiaryDetailsModel.getBarcodedtl() != null) {
                                            for (int i = 0; i < beneficiaryDetailsModel.getBarcodedtl().size(); i++) {
                                                if (!InputUtils.isNull(beneficiaryDetailsModel.getBarcodedtl().get(i).getSamplType())
                                                        && currentScanSampleType.equals(beneficiaryDetailsModel.getBarcodedtl().get(i).getSamplType())) {
                                                    //CHECK for duplicate barcode scanned for the same visit
                                                    OrderVisitDetailsModel orderVisitDetailsModel = orderDetailsDao.getOrderVisitModel(orderDetailsModel.getVisitId());
                                                    for (OrderDetailsModel odm :
                                                            orderVisitDetailsModel.getAllOrderdetails()) {
                                                        for (BeneficiaryDetailsModel bdm :
                                                                odm.getBenMaster()) {
                                                            if (bdm.getBarcodedtl() != null && bdm.getBarcodedtl().size() > 0) {
                                                                for (BeneficiaryBarcodeDetailsModel bbdm :
                                                                        bdm.getBarcodedtl()) {
                                                                    if (!InputUtils.isNull(bbdm.getBarcode()) && bbdm.getBarcode().equals(scanned_barcode)) {
                                                                        if (bbdm.getSamplType().equals(currentScanSampleType) && bbdm.getBenId() == beneficiaryDetailsModel.getBenId()) {

                                                                        } else {
                                                                            Toast.makeText(activity, "Same Barcode Already Scanned for " + bdm.getName() + " - " + bbdm.getSamplType(), Toast.LENGTH_SHORT).show();
                                                                            return;
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                    beneficiaryDetailsModel.getBarcodedtl().get(i).setBarcode(scanned_barcode);
                                                    beneficiaryDetailsModel.getBarcodedtl().get(i).setBenId(beneficiaryDetailsModel.getBenId());
                                                    beneficiaryDetailsDao.insertOrUpdate(beneficiaryDetailsModel);
                                                    break;
                                                }
                                            }
                                            initData();
                                        } else {
                                            Toast.makeText(activity, "Failed to Update Barcode Value", Toast.LENGTH_SHORT).show();
                                        }
                                    }


                                } else {
                                    Toast.makeText(activity, "Failed to Update Barcode Value", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        sampleBarcodeDialog.show();
                    }
                });
                tlBarcodes.addView(tr);
            }
            llBarcodes.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

            if (requestCode == BundleConstants.START_BARCODE_SCAN) {
                IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                if ((scanningResult != null) && (scanningResult.getContents() != null)) {
                    final String scanned_barcode = scanningResult.getContents();


                    AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                    builder1.setTitle("Check the Barcode ")
                            .setMessage("Do you want to Proceed with this barcode entry "+scanned_barcode+"?")
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            if (scanned_barcode.startsWith("0") || scanned_barcode.startsWith("$")) {
                                Toast.makeText(activity, "Invalid Barcode", Toast.LENGTH_SHORT).show();
                            } else {
                                if (!InputUtils.isNull(scanned_barcode) && scanned_barcode.length() == 8) {
                                    if (beneficiaryDetailsModel.getBarcodedtl() != null) {
                                        for (int i = 0; i < beneficiaryDetailsModel.getBarcodedtl().size(); i++) {
                                            //size 4
                                            if (!InputUtils.isNull(beneficiaryDetailsModel.getBarcodedtl().get(i).getSamplType())
                                                    && !InputUtils.isNull(currentScanSampleType)
                                                    && currentScanSampleType.equals(beneficiaryDetailsModel.getBarcodedtl().get(i).getSamplType()) /*&& currentScanBarcode.equals(beneficiaryDetailsModel.getBarcodedtl().get(i).getBarcode())*/) {

                                                //CHECK for duplicate barcode scanned for the same visit
                                                OrderVisitDetailsModel orderVisitDetailsModel = orderDetailsDao.getOrderVisitModel(orderDetailsModel.getVisitId());
                                                for (OrderDetailsModel odm :
                                                        orderVisitDetailsModel.getAllOrderdetails()) {
                                                    for (BeneficiaryDetailsModel bdm :
                                                            odm.getBenMaster()) {
                                                        if (bdm.getBarcodedtl() != null && bdm.getBarcodedtl().size() > 0) {
                                                            for (BeneficiaryBarcodeDetailsModel bbdm :
                                                                    bdm.getBarcodedtl()) {

                                                                if (!InputUtils.isNull(bbdm.getBarcode()) && bbdm.getBarcode().equals(scanned_barcode)) {
                                                                    if (bbdm.getSamplType().equals(currentScanSampleType) && bbdm.getBenId() == beneficiaryDetailsModel.getBenId()) {

                                                                    } else {
                                                                        Toast.makeText(activity, "Same Barcode Already Scanned for " + bdm.getName() + " - " + bbdm.getSamplType(), Toast.LENGTH_SHORT).show();
                                                                        return;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                beneficiaryDetailsModel.getBarcodedtl().get(i).setBarcode(scanned_barcode);
                                                //   Logger.error("getBarcodedtl "+beneficiaryDetailsModel);
                                                beneficiaryDetailsModel.getBarcodedtl().get(i).setBenId(beneficiaryDetailsModel.getBenId());
                                                beneficiaryDetailsDao.insertOrUpdate(beneficiaryDetailsModel);
                                                break;
                                            }
                                        }
                                        initData();
                                    } else {
                                        Toast.makeText(activity, "Failed to Update Scanned Barcode Value", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(activity, "Failed to Scan Barcode", Toast.LENGTH_SHORT).show();
                                }
                            }



                        }
                    }).show();
            }

        }
        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            onCaptureImageResult(data);
        }
        if (requestCode == BundleConstants.ADD_TESTS_START && resultCode == BundleConstants.ADD_TESTS_FINISH) {
            initData();
            edtTests.performClick();
//            refreshBeneficiariesSliderDelegateResult.onRefreshActionCallbackReceived(orderDetailsDao.getOrderVisitModel(orderDetailsModel.getVisitId()));
        }
        if (requestCode == BundleConstants.EDIT_START && resultCode == BundleConstants.EDIT_FINISH) {
//            beneficiaryDetailsModel = data.getExtras().getParcelable(BundleConstants.BENEFICIARY_DETAILS_MODEL);
//            orderDetailsModel = data.getExtras().getParcelable(BundleConstants.ORDER_DETAILS_MODEL);
            try {
                refreshBeneficiariesSliderDelegateResult.onRefreshActionCallbackReceived(orderDetailsDao.getOrderVisitModel(orderDetailsModel.getVisitId()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void onCaptureImageResult(Intent data) {
        thumbnail = (Bitmap) data.getExtras().get("data");
        encodedVanipunctureImg = CommonUtils.encodeImage(thumbnail);
        if (!InputUtils.isNull(encodedVanipunctureImg)) {
            imgVenipuncture.setImageDrawable(activity.getResources().getDrawable(R.drawable.camera_blue));
        } else {
            imgVenipuncture.setImageDrawable(activity.getResources().getDrawable(R.drawable.cameraa));
        }
        beneficiaryDetailsModel.setVenepuncture(encodedVanipunctureImg);
        beneficiaryDetailsDao.insertOrUpdate(beneficiaryDetailsModel);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private class OrderRescheduleDialogButtonClickedDelegateResult implements OrderRescheduleDialogButtonClickedDelegate {

        @Override
        public void onOkButtonClicked(OrderDetailsModel orderVisitDetailsModel, String remark, String date) {
            AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
            OrderStatusChangeRequestModel orderStatusChangeRequestModel = new OrderStatusChangeRequestModel();
            orderStatusChangeRequestModel.setId(orderDetailsModel.getSlotId() + "");
            orderStatusChangeRequestModel.setRemarks(remark);
            orderStatusChangeRequestModel.setStatus(11);
            orderStatusChangeRequestModel.setAppointmentDate(date);
            ApiCallAsyncTask orderStatusChangeApiAsyncTask = asyncTaskForRequest.getOrderStatusChangeRequestAsyncTask(orderStatusChangeRequestModel);
            orderStatusChangeApiAsyncTask.setApiCallAsyncTaskDelegate(new OrderStatusChangeApiAsyncTaskDelegateResult(orderDetailsModel));
            if (isNetworkAvailable(activity)) {
                orderStatusChangeApiAsyncTask.execute(orderStatusChangeApiAsyncTask);
            } else {
                Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelButtonClicked() {

        }
    }

    private class OrderStatusChangeApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        OrderDetailsModel orderDetailsModel;

        public OrderStatusChangeApiAsyncTaskDelegateResult(OrderDetailsModel orderDetailsModel) {
            this.orderDetailsModel = orderDetailsModel;
        }

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                if (userChoosenReleaseTask.equals("Visit Cancellation")) {
                    if (!isCancelRequesGenereted) {
                        isCancelRequesGenereted = true;
                        orderDetailsModel.setStatus("Cancellation Request");
                        orderDetailsDao.insertOrUpdate(orderDetailsModel);
                        Toast.makeText(activity, "Visit cancellation request generated successfully", Toast.LENGTH_SHORT).show();
                        CancelOrderDialog.ll_reason_for_cancel.setVisibility(View.GONE);
                        CancelOrderDialog.ll_enter_otp.setVisibility(View.VISIBLE);
                        cod.show();
                    } else {
                        orderDetailsModel.setStatus("CANCELLED");
                        orderDetailsDao.insertOrUpdate(orderDetailsModel);
                        Toast.makeText(activity, "Visit cancelled Successfully", Toast.LENGTH_SHORT).show();
                        activity.finish();
                    }
                } else {
                    orderDetailsModel.setStatus("RESCHEDULED");
                    orderDetailsDao.insertOrUpdate(orderDetailsModel);
                    Toast.makeText(activity, "Order rescheduled successfully", Toast.LENGTH_SHORT).show();
                    activity.finish();
                }
            } else {
                Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }

    private class OrderCancelDialogButtonClickedDelegateResult implements OrderCancelDialogButtonClickedDelegate {

        @Override
        public void onOkButtonClicked(OrderDetailsModel orderVisitDetailsModel, String remark, int status) {
            AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
            OrderStatusChangeRequestModel orderStatusChangeRequestModel = new OrderStatusChangeRequestModel();
            orderStatusChangeRequestModel.setId(orderDetailsModel.getSlotId() + "");
            orderStatusChangeRequestModel.setRemarks(remark);


            orderStatusChangeRequestModel.setStatus(status);
            ApiCallAsyncTask orderStatusChangeApiAsyncTask = asyncTaskForRequest.getOrderStatusChangeRequestAsyncTask(orderStatusChangeRequestModel);
            orderStatusChangeApiAsyncTask.setApiCallAsyncTaskDelegate(new OrderStatusChangeApiAsyncTaskDelegateResult(orderDetailsModel));
            if (isNetworkAvailable(activity)) {
                orderStatusChangeApiAsyncTask.execute(orderStatusChangeApiAsyncTask);
            } else {
                Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelButtonClicked() {

        }
    }

    private class AddBeneficiaryOrderBookingAPIAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {

        private OrderBookingResponseVisitModel orderBookingResponseVisitModel = new OrderBookingResponseVisitModel();
        private ArrayList<OrderBookingResponseBeneficiaryModel> orderBookingResponseBeneficiaryModelArr = new ArrayList<>();
        private OrderVisitDetailsModel orderVisitDetailsModel;

        public AddBeneficiaryOrderBookingAPIAsyncTaskDelegateResult(OrderVisitDetailsModel orderVisitModel) {
            this.orderVisitDetailsModel = orderVisitModel;
        }

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                orderBookingResponseVisitModel = new ResponseParser(activity).getOrderBookingAPIResponse(json, statusCode);
                for (OrderBookingResponseOrderModel obrom :
                        orderBookingResponseVisitModel.getOrderids()) {
                    orderBookingResponseBeneficiaryModelArr.addAll(obrom.getBenfids());
                }

                OrderVisitDetailsModel orderVisitDetails = orderDetailsDao.getOrderVisitModel(orderVisitDetailsModel.getVisitId());

                //UPDATE old Order No and Benficiary Id with New Order No and Beneficiary Id
                for (OrderDetailsModel odm :
                        orderVisitDetails.getAllOrderdetails()) {
                    for (OrderBookingResponseOrderModel obrom :
                            orderBookingResponseVisitModel.getOrderids()) {
                        //CHECK if old ORDER NO from API response equals order no of local Order Detail Model
                        // AND API response old order Id not equals new order Id
                        if (odm.getOrderNo().equals(obrom.getOldOrderId()) && !obrom.getOldOrderId().equals(obrom.getNewOrderId())) {
                            odm.setOrderNo(obrom.getNewOrderId());
                            //UPDATE old order no with new order no
                            orderDetailsDao.updateOrderNo(obrom.getOldOrderId(), odm);
                            for (BeneficiaryDetailsModel bdm :
                                    odm.getBenMaster()) {
                                for (OrderBookingResponseBeneficiaryModel obrbm :
                                        orderBookingResponseBeneficiaryModelArr) {
                                    //CHECK if old beneficiary id from API response equals beneficiary Id of local Order Detail Model
                                    // AND API response old beneficiary Id not equals new beneficiary Id
                                    if ((bdm.getBenId() + "").equals(obrbm.getOldBenIds())) {
                                        bdm.setOrderNo(obrom.getNewOrderId());
                                        bdm.setBenId(Integer.parseInt(obrbm.getNewBenIds()));
                                        //UPDATE old beneficiary Id with new Beneficiary Id
                                        beneficiaryDetailsDao.updateBeneficiaryId(Integer.parseInt(obrbm.getNewBenIds()), bdm);
                                    }
                                }
                            }
                        }
                    }
                }
                //END of UPDATE old Order No and Benficiary Id with New Order No and Beneficiary Id

                Intent intentFinish = new Intent();
                intentFinish.putExtra(BundleConstants.BENEFICIARY_DETAILS_MODEL, beneficiaryDetailsModel);
                intentFinish.putExtra(BundleConstants.ORDER_DETAILS_MODEL, orderDetailsModel);

            } else {
                Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {

        }
    }

    private OrderBookingRequestModel generateOrderBookingRequestModel(OrderVisitDetailsModel orderVisitDetailsModel) {
        OrderBookingRequestModel orderBookingRequestModel = new OrderBookingRequestModel();

        //SET Order Booking Details Model - START
        OrderBookingDetailsModel orderBookingDetailsModel = new OrderBookingDetailsModel();
        orderBookingDetailsModel.setBtechId(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
        orderBookingDetailsModel.setVisitId(orderVisitDetailsModel.getVisitId());
        orderBookingDetailsModel.setOrddtl(orderDetailsDao.getModelsFromVisitId(orderVisitDetailsModel.getVisitId()));
        orderBookingRequestModel.setOrdbooking(orderBookingDetailsModel);
        //SET Order Booking Details Model - END

        //SET Order Details Models Array - START
        orderBookingRequestModel.setOrddtl(orderDetailsDao.getModelsFromVisitId(orderVisitDetailsModel.getVisitId()));
        //SET Order Details Models Array - END


        //SET BENEFICIARY Details Models Array - START
        ArrayList<BeneficiaryDetailsModel> benArr = new ArrayList<>();
        for (OrderDetailsModel orderDetailsModel :
                orderDetailsDao.getModelsFromVisitId(orderVisitDetailsModel.getVisitId())) {
            ArrayList<BeneficiaryDetailsModel> tempBenArr = new ArrayList<>();
            tempBenArr = beneficiaryDetailsDao.getModelsFromOrderNo(orderDetailsModel.getOrderNo());
            if (tempBenArr != null) {
                benArr.addAll(tempBenArr);
            }
        }
        orderBookingRequestModel.setBendtl(benArr);
        //SET BENEFICIARY Details Models Array - END

        //SET BENEFICIARY Barcode Details Models Array - START
        ArrayList<BeneficiaryBarcodeDetailsModel> benBarcodeArr = new ArrayList<>();

        //SET BENEFICIARY Sample Types Details Models Array - START
        ArrayList<BeneficiarySampleTypeDetailsModel> benSTArr = new ArrayList<>();

        //SET BENEFICIARY Test Wise Clinical History Models Array - START
        ArrayList<BeneficiaryTestWiseClinicalHistoryModel> benCHArr = new ArrayList<>();

        //SET BENEFICIARY Lab Alerts Models Array - START
        ArrayList<BeneficiaryLabAlertsModel> benLAArr = new ArrayList<>();

        for (BeneficiaryDetailsModel beneficiaryDetailsModel :
                benArr) {
            if (beneficiaryDetailsModel.getBarcodedtl() != null) {
                benBarcodeArr.addAll(beneficiaryDetailsModel.getBarcodedtl());
            }
            if (beneficiaryDetailsModel.getSampleType() != null) {
                benSTArr.addAll(beneficiaryDetailsModel.getSampleType());
            }
            if (beneficiaryDetailsModel.getClHistory() != null) {
                benCHArr.addAll(beneficiaryDetailsModel.getClHistory());
            }
            if (beneficiaryDetailsModel.getLabAlert() != null) {
                benLAArr.addAll(beneficiaryDetailsModel.getLabAlert());
            }
            //*******

        }
        orderBookingRequestModel.setBarcodedtl(benBarcodeArr);
        //SET BENEFICIARY Barcode Details Models Array - END

        orderBookingRequestModel.setSmpldtl(benSTArr);
        //SET BENEFICIARY Sample Type Details Models Array - END

        orderBookingRequestModel.setClHistory(benCHArr);
        //SET BENEFICIARY Test Wise Clinical History Models Array - END

        orderBookingRequestModel.setLabAlert(benLAArr);
        //SET BENEFICIARY Lab Alerts Models Array - END

        return orderBookingRequestModel;
    }

    private class GetTestListApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                Logger.error("" + json);

                ResponseParser responseParser = new ResponseParser(activity);
                testGroupListModelArrayList = new ArrayList<>();
                TestListResponseModel = new GetTestListResponseModel();
                TestListResponseModel = responseParser.getTestListResponseModel(json, statusCode);

                CustomDialogClass cdd = new CustomDialogClass(activity);
                cdd.show();

            } else {
                Logger.error("" + json);
            }

        }

        @Override
        public void onApiCancelled() {

        }
    }

    private class CustomDialogClass extends Dialog {

        private Activity c;
        private Dialog d;
        private Button yes, no;
        private TextView tv_test;
        private LinearLayout ll_tests;

        public CustomDialogClass(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.item_test_list_display);
            ll_tests = (LinearLayout) findViewById(R.id.ll_tests);
            iflateTestGroupName(ll_tests);


        }

    }

    private void iflateTestGroupName(LinearLayout ll_tests) {
        if (TestListResponseModel.getTestGroupList().size() > 0) {
            // Logger.error("if ");
            ll_tests.removeAllViews();
            for (int i = 0; i < TestListResponseModel.getTestGroupList().size(); i++) {
                LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = vi.inflate(R.layout.item_view_test, null);
                final TextView tv_test = (TextView) v.findViewById(R.id.tv_test);
                final LinearLayout ll_child = (LinearLayout) v.findViewById(R.id.ll_child);
                tv_test.setText("" + TestListResponseModel.getTestGroupList().get(i).getGroupName() + " (" + TestListResponseModel.getTestGroupList().get(i).getTestCount() + ")");
                tv_test.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_group_collapse_15, 0);

                tv_test.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ll_child.getVisibility() == View.VISIBLE) {
                            ll_child.setVisibility(View.GONE);
                            tv_test.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_group_collapse_15, 0);
                        } else {
                            ll_child.setVisibility(View.VISIBLE);
                            tv_test.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_group_expand_15, 0);

                        }
                    }
                });
                ll_child.removeAllViews();
                for (int j = 0; j < TestListResponseModel.getTestGroupList().get(i).getTestDetails().size(); j++) {
                    LayoutInflater vj = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v1 = vj.inflate(R.layout.item_view_test, null);
                    TextView tv_test1 = (TextView) v1.findViewById(R.id.tv_test);
                    tv_test1.setBackgroundColor(Color.parseColor("#ffffff"));
                    tv_test1.setTextColor(Color.parseColor("#000000"));
                    tv_test1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    tv_test1.setText(">> Description:  " + TestListResponseModel.getTestGroupList().get(i).getTestDetails().get(j).getDescription() + " \n     Test Code: " + TestListResponseModel.getTestGroupList().get(i).getTestDetails().get(j).getTestCode() + "\n     Unit: " + TestListResponseModel.getTestGroupList().get(i).getTestDetails().get(j).getUnit());

                    ll_child.addView(v1);

                    ll_child.invalidate();
                }

                ll_tests.addView(v);
                ll_tests.invalidate();
            }


        }
    }
}
