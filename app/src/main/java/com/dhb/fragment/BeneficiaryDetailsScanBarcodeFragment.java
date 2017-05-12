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
import com.dhb.delegate.SelectClinicalHistoryCheckboxDelegate;
import com.dhb.delegate.SelectLabAlertsCheckboxDelegate;
import com.dhb.dialog.AddSampleBarcodeDialog;
import com.dhb.dialog.CancelOrderDialog;
import com.dhb.dialog.ClinicalHistorySelectorDialog;
import com.dhb.dialog.LabAlertSelectorDialog;
import com.dhb.dialog.RescheduleOrderDialog;
import com.dhb.models.api.request.OrderStatusChangeRequestModel;
import com.dhb.models.data.BarcodeDetailsModel;
import com.dhb.models.data.BeneficiaryDetailsModel;
import com.dhb.models.data.BeneficiaryLabAlertsModel;
import com.dhb.models.data.BeneficiarySampleTypeDetailsModel;
import com.dhb.models.data.LabAlertMasterModel;
import com.dhb.models.data.OrderDetailsModel;
import com.dhb.models.data.TestRateMasterModel;
import com.dhb.models.data.TestSampleTypeModel;
import com.dhb.models.data.TestWiseBeneficiaryClinicalHistoryModel;
import com.dhb.network.ApiCallAsyncTask;
import com.dhb.network.ApiCallAsyncTaskDelegate;
import com.dhb.network.AsyncTaskForRequest;
import com.dhb.uiutils.AbstractFragment;
import com.dhb.utils.app.AppPreferenceManager;
import com.dhb.utils.app.BundleConstants;
import com.dhb.utils.app.DeviceUtils;
import com.dhb.utils.app.InputUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static com.dhb.utils.app.CommonUtils.decodedImageBytes;
import static com.dhb.utils.app.CommonUtils.encodeImage;


public class BeneficiaryDetailsScanBarcodeFragment extends AbstractFragment {
    public static final String TAG_FRAGMENT = BeneficiaryDetailsScanBarcodeFragment.class.getSimpleName();
    private ImageView imgVenipuncture, imgHC;
    private TextView txtSrNo;
    private TextView txtName;
    private TextView txtAge;
    private TextView txtAadharNo;
    private ImageView btnRelease, btnEdit;
    private TextView edtTests;
    private LinearLayout llBarcodes;
    private TextView edtCH,edtLA;
    private EditText edtRemarks;
    private BeneficiaryDetailsModel beneficiaryDetailsModel;
    private OrderBookingActivity activity;
    private String userChoosenTask, encodedVanipunctureImg;
    private static final int REQUEST_CAMERA = 100;
    private Bitmap thumbnail;
    private View rootview;
    private boolean isHC = false;

    private String userChoosenReleaseTask;
    private OrderDetailsModel orderDetailsModel;
    private String currentScanSampleType;
    private RescheduleOrderDialog cdd;
    private CancelOrderDialog cod;
    private boolean isCancelRequesGenereted=false;
    private ArrayList<TestRateMasterModel> restOfTestsList;
    private DhbDao dhbDao;
    private ArrayList<TestWiseBeneficiaryClinicalHistoryModel> benCHArr;
    private ArrayList<BeneficiaryLabAlertsModel> benLAArr;
    private ArrayList<LabAlertMasterModel> labAlertsArr;
    private BeneficiaryDetailsDao beneficiaryDetailsDao;
    private OrderDetailsDao orderDetailsDao;
    private LabAlertMasterDao labAlertMasterDao;
    private TableLayout tlBarcodes;
    private IntentIntegrator intentIntegrator;

    public BeneficiaryDetailsScanBarcodeFragment() {
        // Required empty public constructor
    }

    public static BeneficiaryDetailsScanBarcodeFragment newInstance(Bundle bundle) {
        BeneficiaryDetailsScanBarcodeFragment fragment = new BeneficiaryDetailsScanBarcodeFragment();
        fragment.setArguments(bundle);
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
        benCHArr = beneficiaryDetailsModel.getClHistory();
        benLAArr = beneficiaryDetailsModel.getLabAlert();

        encodedVanipunctureImg = encodeImage(beneficiaryDetailsModel.getVenepuncture());
        String chS = "";
        if(benCHArr!=null && benCHArr.size()>0) {
            for (TestWiseBeneficiaryClinicalHistoryModel chm :
                    benCHArr) {
                if (InputUtils.isNull(chS))
                    chS = "" + chm.getClinicalHistoryId();
                else
                    chS = chS + ", " + chm.getClinicalHistoryId();
            }
        }
        edtCH.setText(chS);

        String laS = "";
        if(benLAArr!=null && benLAArr.size()>0) {
            for (BeneficiaryLabAlertsModel lam :
                    benLAArr) {
                LabAlertMasterModel labAlertMasterModel = labAlertMasterDao.getModelFromId(lam.getLabAlertId()+"");
                if(labAlertMasterModel!=null) {
                    if (InputUtils.isNull(laS))
                        laS = "" + labAlertMasterModel.getLabAlert();
                    else
                        laS = laS + ", " + labAlertMasterModel.getLabAlert();
                }
            }
        }
        edtLA.setText(laS);

        txtName.setText(beneficiaryDetailsModel.getName());
        txtAge.setText(beneficiaryDetailsModel.getAge() + " | " + beneficiaryDetailsModel.getGender());
        txtAadharNo.setVisibility(View.GONE);
        edtTests.setText(beneficiaryDetailsModel.getTestsCode());
        txtSrNo.setText(beneficiaryDetailsModel.getBenId() + "");
        if (orderDetailsModel != null && orderDetailsModel.getReportHC() == 0) {
            imgHC.setImageDrawable(getResources().getDrawable(R.drawable.tick_icon));
        } else {
            imgHC.setImageDrawable(getResources().getDrawable(R.drawable.check_mark));
        }
        if (beneficiaryDetailsModel != null
                && beneficiaryDetailsModel.getBarcodedtl() != null
                && beneficiaryDetailsModel.getSampleType() != null
                && beneficiaryDetailsModel.getBarcodedtl().size() == beneficiaryDetailsModel.getSampleType().size()) {

        } else {
            if(beneficiaryDetailsModel.getBarcodedtl()==null){
                beneficiaryDetailsModel.setBarcodedtl(new ArrayList<BarcodeDetailsModel>());
            }
            else{
                beneficiaryDetailsModel.getBarcodedtl().clear();
            }
            for (BeneficiarySampleTypeDetailsModel sampleTypes :
                    beneficiaryDetailsModel.getSampleType()) {
                BarcodeDetailsModel barcodeDetailsModel = new BarcodeDetailsModel();
                barcodeDetailsModel.setBenId(sampleTypes.getBenId());
                barcodeDetailsModel.setId(DeviceUtils.getRandomUUID());
                barcodeDetailsModel.setSamplType(sampleTypes.getSampleType());
                barcodeDetailsModel.setOrderNo(beneficiaryDetailsModel.getOrderNo());
                beneficiaryDetailsModel.getBarcodedtl().add(barcodeDetailsModel);
            }
            beneficiaryDetailsDao.insertOrUpdate(beneficiaryDetailsModel);
        }
        restOfTestsList = new ArrayList<>();
        TestRateMasterDao testRateMasterDao = new TestRateMasterDao(dhbDao.getDb());
        for (BeneficiaryDetailsModel beneficiaryModel:
                orderDetailsModel.getBenMaster()) {
            if(beneficiaryDetailsModel.getBenId()!=beneficiaryModel.getBenId()){
                restOfTestsList.addAll(testRateMasterDao.getModelsFromTestCodes(beneficiaryModel.getTestsCode()));
            }
        }
        initScanBarcodeView();
    }

    private void setListeners() {
        imgVenipuncture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPhoto();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEdit = new Intent(activity, AddEditBeneficiaryDetailsActivity.class);
                intentEdit.putExtra(BundleConstants.BENEFICIARY_DETAILS_MODEL, beneficiaryDetailsModel);
                intentEdit.putExtra(BundleConstants.ORDER_DETAILS_MODEL, orderDetailsModel);
                startActivityForResult(intentEdit, BundleConstants.ADD_EDIT_START);
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
                    imgHC.setImageDrawable(activity.getResources().getDrawable(R.drawable.check_mark));
                    orderDetailsModel.setReportHC(1);
                }
                orderDetailsDao.insertOrUpdate(orderDetailsModel);
            }
        });
        btnRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"Order Reschedule",
                        "Order Cancellation","Remove Beneficiary"};
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Select Action");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Order Reschedule")) {
                            userChoosenReleaseTask = "Order Reschedule";
                            cdd = new RescheduleOrderDialog(activity, new OrderRescheduleDialogButtonClickedDelegateResult(), orderDetailsModel);
                            cdd.show();
                        } else if (items[item].equals("Order Cancellation")) {
                            userChoosenReleaseTask = "Order Cancellation";
                            cod = new CancelOrderDialog(activity, new OrderCancelDialogButtonClickedDelegateResult(), orderDetailsModel);
                            cod.show();
                        } else if (items[item].equals("Remove Beneficiary")) {
                            userChoosenReleaseTask = "Remove Beneficiary";
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
                final String[] testsList = tests.split(",");
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Tests List");
                builder.setItems(testsList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intentEdit = new Intent(activity, EditTestListActivity.class);
                        intentEdit.putExtra(BundleConstants.REST_BEN_TESTS_LIST,restOfTestsList);
                        intentEdit.putExtra(BundleConstants.SELECTED_TESTS_LIST, beneficiaryDetailsModel.getTestsList());
                        intentEdit.putExtra(BundleConstants.ORDER_DETAILS_MODEL, orderDetailsModel);
                        startActivityForResult(intentEdit, BundleConstants.EDIT_TESTS_START);
                    }
                });
                builder.show();
            }
        });
        edtCH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<TestRateMasterModel> forCHSelection = new ArrayList<>();
                if(beneficiaryDetailsModel.getTestsList()!=null) {
                    for (TestRateMasterModel testRateMasterModel :
                            beneficiaryDetailsModel.getTestsList()) {
                        if(testRateMasterModel.getTstClinicalHistory()!=null && testRateMasterModel.getTstClinicalHistory().size()>0){
                            forCHSelection.add(testRateMasterModel);
                        }
                    }
                }
                if(forCHSelection.size()>0) {
                    ClinicalHistorySelectorDialog clinicalHistorySelectorDialog = new ClinicalHistorySelectorDialog(activity, forCHSelection, benCHArr, beneficiaryDetailsModel.getBenId(), new SelectClinicalHistoryCheckboxDelegate() {
                        @Override
                        public void onCheckChange(ArrayList<TestWiseBeneficiaryClinicalHistoryModel> chArr) {
                            benCHArr = chArr;
                            beneficiaryDetailsModel.setClHistory(benCHArr);
                            beneficiaryDetailsDao.insertOrUpdate(beneficiaryDetailsModel);
                            initData();
                        }
                    });
                    clinicalHistorySelectorDialog.show();
                }
                else{
                    Toast.makeText(activity,"Clinical History Not Required",Toast.LENGTH_SHORT).show();
                }
            }
        });
        edtLA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(labAlertsArr.size()>0) {
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
                }
                else{
                    Toast.makeText(activity,"Lab Alerts Master Not Available",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void initUI() {
        imgVenipuncture = (ImageView) rootview.findViewById(R.id.img_venipuncture);
        imgHC = (ImageView) rootview.findViewById(R.id.hard_copy_check);
        txtName = (TextView) rootview.findViewById(R.id.txt_name);
        txtAge = (TextView) rootview.findViewById(R.id.txt_age);
        txtAadharNo = (TextView) rootview.findViewById(R.id.txt_aadhar_no);
        txtSrNo = (TextView) rootview.findViewById(R.id.txt_sr_no);
        btnEdit = (ImageView) rootview.findViewById(R.id.img_edit);
        btnRelease = (ImageView) rootview.findViewById(R.id.img_release);
        edtTests = (TextView) rootview.findViewById(R.id.edt_test);
        edtCH = (TextView) rootview.findViewById(R.id.clinical_history);
        edtLA = (TextView) rootview.findViewById(R.id.edt_lab_alerts);
        edtRemarks = (EditText) rootview.findViewById(R.id.customer_sign);
        llBarcodes = (LinearLayout) rootview.findViewById(R.id.ll_barcodes);
        tlBarcodes = (TableLayout) rootview.findViewById(R.id.tl_barcodes);
        btnEdit.setVisibility(View.VISIBLE);
    }

    private void initScanBarcodeView() {
//        View scanBarcodeView = activity.getLayoutInflater().inflate(R.layout.item_list_view, null);
//        ListView lv = (ListView) scanBarcodeView.findViewById(R.id.lv_barcodes);
//        displayScanBarcodeItemListAdapter = new DisplayScanBarcodeItemListAdapter(activity, beneficiaryDetailsModel.getBarcodedtl(), new ScanBarcodeIconClickedDelegateResult());
//        lv.setAdapter(displayScanBarcodeItemListAdapter);
        if(beneficiaryDetailsModel!=null
                && beneficiaryDetailsModel.getBarcodedtl()!=null
                && beneficiaryDetailsModel.getBarcodedtl().size()>0) {
            tlBarcodes.removeAllViews();
            for (final BarcodeDetailsModel barcodeDetailsModel :
                    beneficiaryDetailsModel.getBarcodedtl()) {
                TableRow tr = (TableRow) activity.getLayoutInflater().inflate(R.layout.item_scan_barcode,null);
                TextView txtSampleType = (TextView)tr.findViewById(R.id.txt_sample_type);
                TextView edtBarcode = (TextView)tr.findViewById(R.id.edt_barcode);
                ImageView imgScan = (ImageView) tr.findViewById(R.id.scan_barcode_button);
                txtSampleType.setText(barcodeDetailsModel.getSamplType());
                edtBarcode.setText(barcodeDetailsModel.getBarcode());
                imgScan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentScanSampleType = barcodeDetailsModel.getSamplType();
                        intentIntegrator = new IntentIntegrator(activity){
                            @Override
                            protected void startActivityForResult(Intent intent, int code) {
                                BeneficiaryDetailsScanBarcodeFragment.this.startActivityForResult(intent,BundleConstants.START_BARCODE_SCAN); // REQUEST_CODE override
                            }
                        };
                        intentIntegrator.initiateScan();
                    }
                });
                edtBarcode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentScanSampleType = barcodeDetailsModel.getSamplType();
                        AddSampleBarcodeDialog sampleBarcodeDialog = new AddSampleBarcodeDialog(activity, new AddSampleBarcodeDialogDelegate() {
                            @Override
                            public void onSampleBarcodeAdded(String scanned_barcode) {
                                if (!InputUtils.isNull(scanned_barcode) && scanned_barcode.length() == 8) {
                                    for(int i=0;i<beneficiaryDetailsModel.getBarcodedtl().size();i++){
                                        if(currentScanSampleType.equals(beneficiaryDetailsModel.getBarcodedtl().get(i).getSamplType())){
                                            beneficiaryDetailsModel.getBarcodedtl().get(i).setBarcode(scanned_barcode);
                                            beneficiaryDetailsDao.insertOrUpdate(beneficiaryDetailsModel);
                                            break;
                                        }
                                    }
                                    initData();
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

    private void clickPhoto() {
        final CharSequence[] items = {"Take Photo"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Choose Action");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = DeviceUtils.checkPermission(activity);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==BundleConstants.START_BARCODE_SCAN) {
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if ((scanningResult != null) && (scanningResult.getContents() != null)) {
                String scanned_barcode = scanningResult.getContents();
                if (!InputUtils.isNull(scanned_barcode) && scanned_barcode.length() == 8) {
                    for (int i = 0; i < beneficiaryDetailsModel.getBarcodedtl().size(); i++) {
                        if (currentScanSampleType.equals(beneficiaryDetailsModel.getBarcodedtl().get(i).getSamplType())) {
                            beneficiaryDetailsModel.getBarcodedtl().get(i).setBarcode(scanned_barcode);
                            beneficiaryDetailsDao.insertOrUpdate(beneficiaryDetailsModel);
                            break;
                        }
                    }
                    initData();
                }
            }
        }
        if (requestCode==REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            onCaptureImageResult(data);
        }
        if (requestCode == BundleConstants.EDIT_TESTS_START && resultCode == BundleConstants.EDIT_TESTS_FINISH) {
            String testsCode = "";
            ArrayList<TestRateMasterModel> selectedTests = new ArrayList<>();
            selectedTests = data.getExtras().getParcelableArrayList(BundleConstants.SELECTED_TESTS_LIST);

            int selectedTestsTotalCost = data.getExtras().getInt(BundleConstants.SELECTED_TESTS_TOTAL_COST);
            int selectedTestsDiscount = data.getExtras().getInt(BundleConstants.SELECTED_TESTS_DISCOUNT);
            int selectedTestsIncentive = data.getExtras().getInt(BundleConstants.SELECTED_TESTS_INCENTIVE);

            orderDetailsModel.setAmountDue(selectedTestsTotalCost);
            orderDetailsModel.setDiscount(selectedTestsDiscount);
            orderDetailsModel.setMargin(selectedTestsIncentive);
            orderDetailsDao.insertOrUpdate(orderDetailsModel);

            if(selectedTests!=null) {
                for (TestRateMasterModel testRateMasterModel :
                        selectedTests) {
                    if (InputUtils.isNull(testsCode)) {
                        testsCode = testRateMasterModel.getTestCode();
                    } else {
                        testsCode = testsCode + "," + testRateMasterModel.getTestCode();
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
                        if(!samples.contains(bstdm)){
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
            initData();
        }
        if (requestCode == BundleConstants.ADD_EDIT_START && resultCode == BundleConstants.ADD_EDIT_FINISH) {
            beneficiaryDetailsModel = data.getExtras().getParcelable(BundleConstants.BENEFICIARY_DETAILS_MODEL);
            orderDetailsModel = data.getExtras().getParcelable(BundleConstants.ORDER_DETAILS_MODEL);
            initData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void onCaptureImageResult(Intent data) {
        thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
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
        beneficiaryDetailsModel.setVenepuncture(decodedImageBytes(encodedVanipunctureImg));
        beneficiaryDetailsDao.insertOrUpdate(beneficiaryDetailsModel);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private class OrderRescheduleDialogButtonClickedDelegateResult implements OrderRescheduleDialogButtonClickedDelegate {
        @Override
        public void onOkButtonClicked(OrderDetailsModel orderDetailsModel, String remark) {
            AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
            OrderStatusChangeRequestModel orderStatusChangeRequestModel = new OrderStatusChangeRequestModel();
            orderStatusChangeRequestModel.setId(orderDetailsModel.getSlotId() + "");
            orderStatusChangeRequestModel.setRemarks(remark);
            orderStatusChangeRequestModel.setStatus(11);
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
                if(userChoosenReleaseTask.equals("Order Cancellation")){
                    if(!isCancelRequesGenereted) {
                        isCancelRequesGenereted = true;
                        orderDetailsModel.setStatus("Cancellation Request");
                        orderDetailsDao.insertOrUpdate(orderDetailsModel);
                        Toast.makeText(activity, "Order cancellation request generated successfully", Toast.LENGTH_SHORT).show();
                        CancelOrderDialog.ll_reason_for_cancel.setVisibility(View.GONE);
                        CancelOrderDialog.ll_enter_otp.setVisibility(View.VISIBLE);
                        cod.show();
                    }
                    else{
                        orderDetailsModel.setStatus("CANCELLED");
                        orderDetailsDao.insertOrUpdate(orderDetailsModel);
                        Toast.makeText(activity, "Order cancelled Successfully", Toast.LENGTH_SHORT).show();
                        activity.finish();
                    }
                }else {
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
        public void onOkButtonClicked(OrderDetailsModel orderVisitDetailsModel, String remark) {
            AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
            OrderStatusChangeRequestModel orderStatusChangeRequestModel = new OrderStatusChangeRequestModel();
            orderStatusChangeRequestModel.setId(orderDetailsModel.getSlotId() + "");
            orderStatusChangeRequestModel.setRemarks(remark);
            orderStatusChangeRequestModel.setStatus(12);
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
