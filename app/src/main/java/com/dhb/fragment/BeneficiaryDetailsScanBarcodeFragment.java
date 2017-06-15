package com.dhb.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.activity.AddEditBeneficiaryDetailsActivity;
import com.dhb.activity.EditTestListActivity;
import com.dhb.activity.OrderBookingActivity;
import com.dhb.dao.DhbDao;
import com.dhb.dao.models.BeneficiaryDetailsDao;
import com.dhb.dao.models.LabAlertMasterDao;
import com.dhb.dao.models.OrderDetailsDao;
import com.dhb.dao.models.TestRateMasterDao;
import com.dhb.delegate.AddSampleBarcodeDialogDelegate;
import com.dhb.delegate.OrderCancelDialogButtonClickedDelegate;
import com.dhb.delegate.OrderRescheduleDialogButtonClickedDelegate;
import com.dhb.delegate.RefreshBeneficiariesSliderDelegate;
import com.dhb.delegate.SelectClinicalHistoryCheckboxDelegate;
import com.dhb.delegate.SelectLabAlertsCheckboxDelegate;
import com.dhb.dialog.AddSampleBarcodeDialog;
import com.dhb.dialog.CancelOrderDialog;
import com.dhb.dialog.ClinicalHistorySelectorDialog;
import com.dhb.dialog.LabAlertSelectorDialog;
import com.dhb.dialog.RescheduleOrderDialog;
import com.dhb.models.api.request.OrderStatusChangeRequestModel;
import com.dhb.models.api.request.RemoveBeneficiaryAPIRequestModel;
import com.dhb.models.data.BeneficiaryBarcodeDetailsModel;
import com.dhb.models.data.BeneficiaryDetailsModel;
import com.dhb.models.data.BeneficiaryLabAlertsModel;
import com.dhb.models.data.BeneficiarySampleTypeDetailsModel;
import com.dhb.models.data.LabAlertMasterModel;
import com.dhb.models.data.OrderDetailsModel;
import com.dhb.models.data.OrderVisitDetailsModel;
import com.dhb.models.data.TestRateMasterModel;
import com.dhb.models.data.TestSampleTypeModel;
import com.dhb.models.data.BeneficiaryTestWiseClinicalHistoryModel;
import com.dhb.network.ApiCallAsyncTask;
import com.dhb.network.ApiCallAsyncTaskDelegate;
import com.dhb.network.AsyncTaskForRequest;
import com.dhb.network.ResponseParser;
import com.dhb.uiutils.AbstractFragment;
import com.dhb.utils.api.Logger;
import com.dhb.utils.app.AppPreferenceManager;
import com.dhb.utils.app.BundleConstants;
import com.dhb.utils.app.DeviceUtils;
import com.dhb.utils.app.InputUtils;
import com.dhb.utils.app.StringUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;

import static com.dhb.utils.app.CommonUtils.decodedImageBytes;
import static com.dhb.utils.app.CommonUtils.encodeImage;


public class BeneficiaryDetailsScanBarcodeFragment extends AbstractFragment {
    public static final String TAG_FRAGMENT = BeneficiaryDetailsScanBarcodeFragment.class.getSimpleName();
    private ImageView imgVenipuncture, imgHC;
    private TextView txtSrNo,tv_orderno;
    private TextView txtName;
    private TextView txtAge;
    private TextView txtAadharNo;
    private ImageView btnRelease, btnEdit;
    private TextView edtTests;
    private LinearLayout llBarcodes;
    private TextView edtCH, edtLA;
    private EditText edtRemarks;
    private BeneficiaryDetailsModel beneficiaryDetailsModel;
    private OrderBookingActivity activity;
    private String userChoosenTask, encodedVanipunctureImg;
    private static final int REQUEST_CAMERA = 100;
    private Bitmap thumbnail;
    private View rootview;
      private boolean isHC = false;
//    private int isHC = 0;

    private String userChoosenReleaseTask;
    private OrderDetailsModel orderDetailsModel;
    private String currentScanSampleType;
    private String currentScanBarcode;
    private RescheduleOrderDialog cdd;
    private CancelOrderDialog cod;
    private boolean isCancelRequesGenereted = false;
    private ArrayList<TestRateMasterModel> restOfTestsList;
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
            txtAadharNo.setVisibility(View.GONE);
            edtTests.setText(beneficiaryDetailsModel.getTestsCode());
            txtSrNo.setText(beneficiaryDetailsModel.getBenId() + "");
            if (orderDetailsModel != null && orderDetailsModel.getReportHC() == 0) {
                imgHC.setImageDrawable(getResources().getDrawable(R.drawable.tick_icon));
                isHC = false;
            } else {
                imgHC.setImageDrawable(getResources().getDrawable(R.drawable.green_tick_icon));
                isHC = true;
            }
            if (beneficiaryDetailsModel != null
                    && beneficiaryDetailsModel.getBarcodedtl() != null
                    && beneficiaryDetailsModel.getSampleType() != null
                    && beneficiaryDetailsModel.getBarcodedtl().size() == beneficiaryDetailsModel.getSampleType().size()) {

            } else {
                if (beneficiaryDetailsModel.getBarcodedtl() == null) {
                    beneficiaryDetailsModel.setBarcodedtl(new ArrayList<BeneficiaryBarcodeDetailsModel>());
                } else {
                    beneficiaryDetailsModel.getBarcodedtl().clear();
                }
                for (BeneficiarySampleTypeDetailsModel sampleTypes :
                        beneficiaryDetailsModel.getSampleType()) {
                    Logger.error("sample type: "+beneficiaryDetailsModel.getSampleType());
                    BeneficiaryBarcodeDetailsModel beneficiaryBarcodeDetailsModel = new BeneficiaryBarcodeDetailsModel();
                    beneficiaryBarcodeDetailsModel.setBenId(beneficiaryDetailsModel.getBenId());
                    beneficiaryBarcodeDetailsModel.setId(DeviceUtils.getRandomUUID());
                    beneficiaryBarcodeDetailsModel.setSamplType(sampleTypes.getSampleType());
                    beneficiaryBarcodeDetailsModel.setOrderNo(beneficiaryDetailsModel.getOrderNo());
                    beneficiaryDetailsModel.getBarcodedtl().add(beneficiaryBarcodeDetailsModel);
                }
                beneficiaryDetailsDao.insertOrUpdate(beneficiaryDetailsModel);
            }
            restOfTestsList = new ArrayList<>();
            TestRateMasterDao testRateMasterDao = new TestRateMasterDao(dhbDao.getDb());
            for (BeneficiaryDetailsModel beneficiaryModel :
                    orderDetailsModel.getBenMaster()) {
                if (beneficiaryDetailsModel.getBenId() != beneficiaryModel.getBenId()) {
                    restOfTestsList.addAll(testRateMasterDao.getModelsFromTestCodes(beneficiaryModel.getTestsCode()));
                }
            }
            if(!InputUtils.isNull(beneficiaryDetailsModel.getVenepuncture()))
            {
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
                final CharSequence[] items = {"Order Reschedule",
                        "Visit Cancellation", "Remove Beneficiary"};
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
                                            ApiCallAsyncTask removeBeneficiaryAsyncTask = new AsyncTaskForRequest(activity).getRemoveBeneficiaryRequestAsyncTask(rb);
                                            removeBeneficiaryAsyncTask.setApiCallAsyncTaskDelegate(new ApiCallAsyncTaskDelegate() {
                                                @Override
                                                public void apiCallResult(String json, int statusCode) throws JSONException {
                                                    if (statusCode == 200) {
                                                        ResponseParser responseParser = new ResponseParser(activity);
                                                        OrderVisitDetailsModel orderVisitDetailsModel = responseParser.getRemoveBeneficiaryAPIResponseModel(json, statusCode);
                                                        if (orderVisitDetailsModel.getAllOrderdetails() != null && orderVisitDetailsModel.getAllOrderdetails().size() > 0) {
                                                            beneficiaryDetailsDao.deleteByBenId(beneficiaryDetailsModel.getBenId() + "");
                                                            for (OrderDetailsModel orderDetailsModel :
                                                                    orderVisitDetailsModel.getAllOrderdetails()) {
                                                                orderDetailsModel.setVisitId(orderVisitDetailsModel.getVisitId());
                                                                orderDetailsModel.setResponse(orderVisitDetailsModel.getResponse());
                                                                orderDetailsModel.setSlot(orderVisitDetailsModel.getSlot());
                                                                orderDetailsModel.setSlotId(orderVisitDetailsModel.getSlotId());
                                                                if (orderDetailsModel.getBenMaster() != null && orderDetailsModel.getBenMaster().size() > 0) {
                                                                    for (BeneficiaryDetailsModel beneficiaryDetailsModel :
                                                                            orderDetailsModel.getBenMaster()) {
                                                                        beneficiaryDetailsModel.setOrderNo(orderDetailsModel.getOrderNo());
                                                                        beneficiaryDetailsDao.insertOrUpdate(beneficiaryDetailsModel);
                                                                    }
                                                                    orderDetailsDao.insertOrUpdate(orderDetailsModel);
                                                                }
                                                            }
                                                            Toast.makeText(activity, "Beneficiary Removed Successfully", Toast.LENGTH_SHORT).show();
                                                            refreshBeneficiariesSliderDelegateResult.onRefreshActionCallbackReceived(orderVisitDetailsModel);
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
        edtTests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tests = beneficiaryDetailsModel.getTestsCode();
                String projId = beneficiaryDetailsModel.getProjId();
                final ArrayList<String> testCodesList = new ArrayList<>();
                Collections.addAll(testCodesList, tests.split(","));
                beneficiaryDetailsModel.setTestsList(new TestRateMasterDao(dhbDao.getDb()).getModelsFromTestCodes(tests));
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Tests List");
                builder.setItems(testCodesList.toArray(new String[testCodesList.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("closing Dialog");

                        dialog.dismiss();
                    }
                }).setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (testCodesList.size() == beneficiaryDetailsModel.getTestsList().size()) {
                            Intent intentEdit = new Intent(activity, EditTestListActivity.class);
                            intentEdit.putExtra(BundleConstants.REST_BEN_TESTS_LIST, restOfTestsList);
                            intentEdit.putExtra(BundleConstants.SELECTED_TESTS_LIST, beneficiaryDetailsModel.getTestsList());
                            intentEdit.putExtra(BundleConstants.ORDER_DETAILS_MODEL, orderDetailsModel);
                            startActivityForResult(intentEdit, BundleConstants.EDIT_TESTS_START);
                            dialog.dismiss();
                        } else {
                            Toast.makeText(activity, "Tests & Profiles cannot be edited for DSA Orders", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.show();
            }
        });
        edtCH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<TestRateMasterModel> forCHSelection = new ArrayList<>();
                if (beneficiaryDetailsModel.getTestsList() != null) {
                    for (TestRateMasterModel testRateMasterModel :
                            beneficiaryDetailsModel.getTestsList()) {
                        if (testRateMasterModel.getTstClinicalHistory() != null && testRateMasterModel.getTstClinicalHistory().size() > 0) {
                            forCHSelection.add(testRateMasterModel);
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

    @Override
    public void initUI() {
        imgVenipuncture = (ImageView) rootview.findViewById(R.id.img_venipuncture);
        tv_orderno = (TextView)rootview.findViewById(R.id.tv_orderno);
        tv_orderno.setSelected(true);
        imgHC = (ImageView) rootview.findViewById(R.id.hard_copy_check);
        txtName = (TextView) rootview.findViewById(R.id.txt_name);
        txtName.setSelected(true);
        txtAge = (TextView) rootview.findViewById(R.id.txt_age);
        txtAadharNo = (TextView) rootview.findViewById(R.id.txt_aadhar_no);
        txtSrNo = (TextView) rootview.findViewById(R.id.txt_sr_no);
        btnEdit = (ImageView) rootview.findViewById(R.id.img_edit);
        btnRelease = (ImageView) rootview.findViewById(R.id.img_release2);
        edtTests = (TextView) rootview.findViewById(R.id.edt_test);
        edtCH = (TextView) rootview.findViewById(R.id.clinical_history);
        edtLA = (TextView) rootview.findViewById(R.id.edt_lab_alerts);
        edtRemarks = (EditText) rootview.findViewById(R.id.customer_sign);
        llBarcodes = (LinearLayout) rootview.findViewById(R.id.ll_barcodes);
        tlBarcodes = (TableLayout) rootview.findViewById(R.id.tl_barcodes);
        btnEdit.setVisibility(View.VISIBLE);
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
                }
                else if (beneficiaryBarcodeDetailsModel.getSamplType().equals("FLUORIDE-F")) {

                    txtSampleType.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_sample_type_fluoride));
                }
                else if (beneficiaryBarcodeDetailsModel.getSamplType().equals("FLUORIDE-PP")) {

                    txtSampleType.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_sample_type_fluoride));
                }
                Logger.error("beneficiaryBarcodeDetailsModel.getBarcode() " + beneficiaryBarcodeDetailsModel.getBarcode());
                Logger.error("barcode value: " + beneficiaryBarcodeDetailsModel.getBarcode());

                edtBarcode.setText(beneficiaryBarcodeDetailsModel.getBarcode());

                imgScan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Logger.error("sample type img scan click "+beneficiaryBarcodeDetailsModel.getSamplType());
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
                        AddSampleBarcodeDialog sampleBarcodeDialog = new AddSampleBarcodeDialog(activity, new AddSampleBarcodeDialogDelegate() {
                            @Override
                            public void onSampleBarcodeAdded(String scanned_barcode) {
                                if (!InputUtils.isNull(scanned_barcode) && scanned_barcode.length() == 8) {
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
                String scanned_barcode = scanningResult.getContents();
                if (!InputUtils.isNull(scanned_barcode) && scanned_barcode.length() == 8) {
                    currentScanBarcode=scanned_barcode;
                    if (beneficiaryDetailsModel.getBarcodedtl() != null) {
                        for (int i = 0; i < beneficiaryDetailsModel.getBarcodedtl().size(); i++) {
                            //size 4
                            if (!InputUtils.isNull(beneficiaryDetailsModel.getBarcodedtl().get(i).getSamplType())
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
        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            onCaptureImageResult(data);

        }
        if (requestCode == BundleConstants.EDIT_TESTS_START && resultCode == BundleConstants.EDIT_TESTS_FINISH) {
            String testsCode = "";
            ArrayList<TestRateMasterModel> selectedTests;
            selectedTests = data.getExtras().getParcelableArrayList(BundleConstants.SELECTED_TESTS_LIST);

            int selectedTestsTotalCost = data.getExtras().getInt(BundleConstants.SELECTED_TESTS_TOTAL_COST);
            int selectedTestsDiscount = data.getExtras().getInt(BundleConstants.SELECTED_TESTS_DISCOUNT);
            int selectedTestsIncentive = data.getExtras().getInt(BundleConstants.SELECTED_TESTS_INCENTIVE);

            orderDetailsModel.setAmountDue(selectedTestsTotalCost);
            orderDetailsModel.setDiscount(selectedTestsDiscount);
            orderDetailsModel.setMargin(selectedTestsIncentive);
            orderDetailsDao.insertOrUpdate(orderDetailsModel);

            if (selectedTests != null) {
                for (TestRateMasterModel testRateMasterModel :
                        selectedTests) {
                    if (InputUtils.isNull(testsCode)) {
                        if (testRateMasterModel.getTestType().equals("OFFER")) {
                            testsCode = testRateMasterModel.getDescription();
                            beneficiaryDetailsModel.setProjId(testRateMasterModel.getTestCode());
                        } else {
                            testsCode = testRateMasterModel.getTestCode();
                        }
                    } else {
                        if (testRateMasterModel.getTestType().equals("OFFER")) {
                            testsCode = testsCode + "," + testRateMasterModel.getDescription();
                        } else {
                            testsCode = testsCode + "," + testRateMasterModel.getTestCode();
                            beneficiaryDetailsModel.setProjId(testRateMasterModel.getTestCode());
                        }
                    }
                }
                ArrayList<BeneficiarySampleTypeDetailsModel> samples = new ArrayList<>();
                for (TestRateMasterModel trmm :
                        selectedTests) {
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
            }
            beneficiaryDetailsModel.setTestsList(selectedTests);
            beneficiaryDetailsModel.setTestsCode(testsCode);
            beneficiaryDetailsModel.setTests(testsCode);
            edtTests.setText(testsCode);
            beneficiaryDetailsDao.insertOrUpdate(beneficiaryDetailsModel);
            refreshBeneficiariesSliderDelegateResult.onRefreshActionCallbackReceived(orderDetailsDao.getOrderVisitModel(orderDetailsModel.getVisitId()));
        }
        if (requestCode == BundleConstants.EDIT_START && resultCode == BundleConstants.EDIT_FINISH) {
            beneficiaryDetailsModel = data.getExtras().getParcelable(BundleConstants.BENEFICIARY_DETAILS_MODEL);
            orderDetailsModel = data.getExtras().getParcelable(BundleConstants.ORDER_DETAILS_MODEL);
            refreshBeneficiariesSliderDelegateResult.onRefreshActionCallbackReceived(orderDetailsDao.getOrderVisitModel(orderDetailsModel.getVisitId()));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void onCaptureImageResult(Intent data) {
        thumbnail = (Bitmap) data.getExtras().get("data");
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        /*File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        encodedVanipunctureImg = encodeImage(thumbnail);
        beneficiaryDetailsModel.setVenepuncture(encodedVanipunctureImg);
        imgVenipuncture.setImageDrawable(activity.getResources().getDrawable(R.drawable.camera_blue));
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
                if (IS_DEBUG)
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
}
