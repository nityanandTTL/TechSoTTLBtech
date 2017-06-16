package com.dhb.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.dao.DhbDao;
import com.dhb.dao.models.BeneficiaryDetailsDao;
import com.dhb.dao.models.LabAlertMasterDao;
import com.dhb.dao.models.OrderDetailsDao;
import com.dhb.dao.models.TestRateMasterDao;
import com.dhb.delegate.AddSampleBarcodeDialogDelegate;
import com.dhb.delegate.SelectClinicalHistoryCheckboxDelegate;
import com.dhb.delegate.SelectLabAlertsCheckboxDelegate;
import com.dhb.dialog.AddSampleBarcodeDialog;
import com.dhb.dialog.ClinicalHistorySelectorDialog;
import com.dhb.dialog.LabAlertSelectorDialog;
import com.dhb.models.api.request.OrderBookingRequestModel;
import com.dhb.models.api.response.OrderBookingResponseBeneficiaryModel;
import com.dhb.models.api.response.OrderBookingResponseOrderModel;
import com.dhb.models.api.response.OrderBookingResponseVisitModel;
import com.dhb.models.data.BeneficiaryBarcodeDetailsModel;
import com.dhb.models.data.BeneficiaryDetailsModel;
import com.dhb.models.data.BeneficiaryLabAlertsModel;
import com.dhb.models.data.BeneficiarySampleTypeDetailsModel;
import com.dhb.models.data.BeneficiaryTestWiseClinicalHistoryModel;
import com.dhb.models.data.LabAlertMasterModel;
import com.dhb.models.data.OrderBookingDetailsModel;
import com.dhb.models.data.OrderDetailsModel;
import com.dhb.models.data.OrderVisitDetailsModel;
import com.dhb.models.data.TestRateMasterModel;
import com.dhb.models.data.TestSampleTypeModel;
import com.dhb.network.ApiCallAsyncTask;
import com.dhb.network.ApiCallAsyncTaskDelegate;
import com.dhb.network.AsyncTaskForRequest;
import com.dhb.network.ResponseParser;
import com.dhb.uiutils.AbstractActivity;
import com.dhb.utils.app.AppPreferenceManager;
import com.dhb.utils.app.BundleConstants;
import com.dhb.utils.app.DeviceUtils;
import com.dhb.utils.app.InputUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;

import static com.dhb.utils.app.CommonUtils.encodeImage;

/**
 * Created by ISRO on 5/4/2017.
 */

public class AddEditBeneficiaryDetailsActivity extends AbstractActivity {
    private Activity activity;
    private AppPreferenceManager appPreferenceManager;
    private BeneficiaryDetailsModel beneficiaryDetailsModel;
    private EditText edtBenName;
    private EditText edtAge;
    private EditText edtAadhar;
    private ImageView imgMale,imgFemale;
    private TextView edtTests;
    private LinearLayout llBarcodes;
    private TextView edtCH,edtLA;
    private EditText edtRemarks;
    private ImageView imgVenipuncture, imgHC;
    private Button btnSave;
    private boolean isHC = false;
    private boolean isEdit = false;
    private static final int REQUEST_CAMERA = 100;
    private Bitmap thumbnail;
    private String encodedVanipunctureImg;
    private boolean isM = false;
    private TableLayout tlBarcodes;
    private OrderDetailsModel orderDetailsModel;
    private String currentScanSampleType;
    private boolean isCancelRequestGenereted=false;
    private ArrayList<TestRateMasterModel> restOfTestsList;
    private DhbDao dhbDao;
    private ArrayList<BeneficiaryTestWiseClinicalHistoryModel> benCHArr;
    private ArrayList<BeneficiaryLabAlertsModel> benLAArr;
    private ArrayList<LabAlertMasterModel> labAlertsArr;
    private BeneficiaryDetailsDao beneficiaryDetailsDao;
    private OrderDetailsDao orderDetailsDao;
    private LabAlertMasterDao labAlertMasterDao;
    private IntentIntegrator intentIntegrator;
    private boolean isAdd = false;
    private boolean isFasting = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_beneficiary);
        activity = this;
        appPreferenceManager = new AppPreferenceManager(activity);
        dhbDao = new DhbDao(activity);
        beneficiaryDetailsDao = new BeneficiaryDetailsDao(dhbDao.getDb());
        orderDetailsDao = new OrderDetailsDao(dhbDao.getDb());
        labAlertMasterDao = new LabAlertMasterDao(dhbDao.getDb());

        labAlertsArr = labAlertMasterDao.getAllModels();
        benCHArr = new ArrayList<>();
        benLAArr = new ArrayList<>();
        beneficiaryDetailsModel = getIntent().getExtras().getParcelable(BundleConstants.BENEFICIARY_DETAILS_MODEL);
        orderDetailsModel = getIntent().getExtras().getParcelable(BundleConstants.ORDER_DETAILS_MODEL);

        isEdit = getIntent().getExtras().getBoolean(BundleConstants.IS_BENEFICIARY_EDIT,false);
        isAdd = getIntent().getExtras().getBoolean(BundleConstants.IS_BENEFICIARY_ADD,false);
        initUI();
        initData();
        initListeners();
    }

    private void initScanBarcodeView() {
        if(beneficiaryDetailsModel!=null
                && beneficiaryDetailsModel.getBarcodedtl()!=null
                && beneficiaryDetailsModel.getBarcodedtl().size()>0) {
            tlBarcodes.removeAllViews();
            for (final BeneficiaryBarcodeDetailsModel beneficiaryBarcodeDetailsModel :
                    beneficiaryDetailsModel.getBarcodedtl()) {
                TableRow tr = (TableRow) activity.getLayoutInflater().inflate(R.layout.item_scan_barcode,null);
                TextView txtSampleType = (TextView)tr.findViewById(R.id.txt_sample_type);
                TextView edtBarcode = (TextView)tr.findViewById(R.id.edt_barcode);
                ImageView imgScan = (ImageView) tr.findViewById(R.id.scan_barcode_button);
                txtSampleType.setText(beneficiaryBarcodeDetailsModel.getSamplType());
                if(beneficiaryBarcodeDetailsModel.getSamplType().equals("SERUM")){
                    txtSampleType.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_sample_type_serum));
                }
                else if(beneficiaryBarcodeDetailsModel.getSamplType().equals("EDTA")){
                    txtSampleType.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_sample_type_edta));
                }
                else if(beneficiaryBarcodeDetailsModel.getSamplType().equals("FLUORIDE")){
                    txtSampleType.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_sample_type_fluoride));
                }
                else if(beneficiaryBarcodeDetailsModel.getSamplType().equals("HEPARIN")){
                    txtSampleType.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_sample_type_heparin));
                }
                else if(beneficiaryBarcodeDetailsModel.getSamplType().equals("URINE")){
                    txtSampleType.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_sample_type_urine));
                }
                edtBarcode.setText(beneficiaryBarcodeDetailsModel.getBarcode());
                imgScan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentScanSampleType = beneficiaryBarcodeDetailsModel.getSamplType();
                        intentIntegrator = new IntentIntegrator(activity){
                            @Override
                            protected void startActivityForResult(Intent intent, int code) {
                                AddEditBeneficiaryDetailsActivity.this.startActivityForResult(intent,BundleConstants.START_BARCODE_SCAN); // REQUEST_CODE override
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
                                if(beneficiaryDetailsModel.getBarcodedtl()!=null) {
                                    for (int i = 0; i < beneficiaryDetailsModel.getBarcodedtl().size(); i++) {
                                        if (!InputUtils.isNull(beneficiaryDetailsModel.getBarcodedtl().get(i).getSamplType())
                                                && currentScanSampleType.equals(beneficiaryDetailsModel.getBarcodedtl().get(i).getSamplType())) {
                                            //CHECK for duplicate barcode scanned for the same visit
                                            OrderVisitDetailsModel orderVisitDetailsModel = orderDetailsDao.getOrderVisitModel(orderDetailsModel.getVisitId());
                                            for (OrderDetailsModel odm :
                                                    orderVisitDetailsModel.getAllOrderdetails()) {
                                                for (BeneficiaryDetailsModel bdm :
                                                        odm.getBenMaster()) {
                                                    if(bdm.getBarcodedtl()!=null && bdm.getBarcodedtl().size()>0) {
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
                                }
                                else{
                                    Toast.makeText(activity,"Failed to Update Barcode Value",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(activity,"Failed to Update Barcode Value",Toast.LENGTH_SHORT).show();
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

    private class AddBeneficiaryOrderBookingAPIAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {

        private OrderBookingResponseVisitModel orderBookingResponseVisitModel = new OrderBookingResponseVisitModel();
        private ArrayList<OrderBookingResponseBeneficiaryModel> orderBookingResponseBeneficiaryModelArr = new ArrayList<>();
        private OrderVisitDetailsModel orderVisitDetailsModel;

        public AddBeneficiaryOrderBookingAPIAsyncTaskDelegateResult(OrderVisitDetailsModel orderVisitModel) {
            this.orderVisitDetailsModel = orderVisitModel;
        }

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if(statusCode==200) {
                orderBookingResponseVisitModel = new ResponseParser(activity).getOrderBookingAPIResponse(json, statusCode);
                for (OrderBookingResponseOrderModel obrom :
                        orderBookingResponseVisitModel.getOrderids()) {
                    orderBookingResponseBeneficiaryModelArr.addAll(obrom.getBenfids());
                }

                OrderVisitDetailsModel orderVisitDetails = orderDetailsDao.getOrderVisitModel(orderVisitDetailsModel.getVisitId());

                //UPDATE old Order No and Benficiary Id with New Order No and Beneficiary Id
                for (OrderDetailsModel odm :
                        orderVisitDetails.getAllOrderdetails()) {
                    for (OrderBookingResponseOrderModel obrom:
                            orderBookingResponseVisitModel.getOrderids()) {
                        //CHECK if old ORDER NO from API response equals order no of local Order Detail Model
                        // AND API response old order Id not equals new order Id
                        if(odm.getOrderNo().equals(obrom.getOldOrderId()) && !obrom.getOldOrderId().equals(obrom.getNewOrderId())){
                            odm.setOrderNo(obrom.getNewOrderId());
                            //UPDATE old order no with new order no
                            orderDetailsDao.updateOrderNo(obrom.getOldOrderId(),odm);
                            for (BeneficiaryDetailsModel bdm:
                                    odm.getBenMaster()) {
                                for (OrderBookingResponseBeneficiaryModel obrbm:
                                        orderBookingResponseBeneficiaryModelArr) {
                                    //CHECK if old beneficiary id from API response equals beneficiary Id of local Order Detail Model
                                    // AND API response old beneficiary Id not equals new beneficiary Id
                                    if((bdm.getBenId()+"").equals(obrbm.getOldBenIds())){
                                        bdm.setOrderNo(obrom.getNewOrderId());
                                        bdm.setBenId(Integer.parseInt(obrbm.getNewBenIds()));
                                        //UPDATE old beneficiary Id with new Beneficiary Id
                                        beneficiaryDetailsDao.updateBeneficiaryId(Integer.parseInt(obrbm.getNewBenIds()),bdm);
                                    }
                                }
                            }
                        }
                    }
                }
                //END of UPDATE old Order No and Benficiary Id with New Order No and Beneficiary Id

                Intent intentFinish = new Intent();
                intentFinish.putExtra(BundleConstants.BENEFICIARY_DETAILS_MODEL,beneficiaryDetailsModel);
                intentFinish.putExtra(BundleConstants.ORDER_DETAILS_MODEL,orderDetailsModel);
                if(isEdit) {
                    setResult(BundleConstants.EDIT_FINISH, intentFinish);
                }
                else{
                    setResult(BundleConstants.ADD_FINISH, intentFinish);
                }
                finish();
            }
        }

        @Override
        public void onApiCancelled() {

        }
    }

    private void initListeners() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){

                    beneficiaryDetailsModel.setName(edtBenName.getText().toString().trim());
                    beneficiaryDetailsModel.setAge(Integer.parseInt(edtAge.getText().toString().trim()));
                    beneficiaryDetailsModel.setGender(isM?"M":"F");
                    beneficiaryDetailsModel.setVenepuncture(encodedVanipunctureImg);
                    beneficiaryDetailsModel.setTestsCode(edtTests.getText().toString());
                    beneficiaryDetailsModel.setTests(edtTests.getText().toString());
                    beneficiaryDetailsDao.insertOrUpdate(beneficiaryDetailsModel);
                    orderDetailsModel.setReportHC(isHC?1:0);
                    orderDetailsModel.setAddBen(isAdd);

                    /********************/

                    /********************/

                    orderDetailsDao.insertOrUpdate(orderDetailsModel);
                    OrderBookingRequestModel obrm = generateOrderBookingRequestModel(orderDetailsDao.getOrderVisitModel(orderDetailsModel.getVisitId()));
//            if(validate(obrm)) {
                    ApiCallAsyncTask orderBookingAPIAsyncTask = new AsyncTaskForRequest(activity).getOrderBookingRequestAsyncTask(obrm);
                    orderBookingAPIAsyncTask.setApiCallAsyncTaskDelegate(new AddBeneficiaryOrderBookingAPIAsyncTaskDelegateResult(orderDetailsDao.getOrderVisitModel(orderDetailsModel.getVisitId())));
                    if (isNetworkAvailable(activity)) {
                        orderBookingAPIAsyncTask.execute(orderBookingAPIAsyncTask);
                    } else {
                        Toast.makeText(activity, activity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
                    }
//            }
                }
            }
        });
        imgMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isM = true;
                imgMale.setImageDrawable(activity.getResources().getDrawable(R.drawable.m_selected));
                imgFemale.setImageDrawable(activity.getResources().getDrawable(R.drawable.female));
                beneficiaryDetailsModel.setGender("M");
                beneficiaryDetailsDao.insertOrUpdate(beneficiaryDetailsModel);
            }
        });
        imgFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isM = false;
                imgMale.setImageDrawable(activity.getResources().getDrawable(R.drawable.male));
                imgFemale.setImageDrawable(activity.getResources().getDrawable(R.drawable.f_selected));
                beneficiaryDetailsModel.setGender("F");
                beneficiaryDetailsDao.insertOrUpdate(beneficiaryDetailsModel);
            }
        });
        imgVenipuncture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraIntent();

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
            }
        });
        edtTests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tests = beneficiaryDetailsModel.getTestsCode();
                String[] testsList = new String[]{};
                if(InputUtils.isNull(tests)){
                    Intent intentEdit = new Intent(activity, EditTestListActivity.class);
                    intentEdit.putExtra(BundleConstants.REST_BEN_TESTS_LIST,restOfTestsList);
                    intentEdit.putExtra(BundleConstants.SELECTED_TESTS_LIST, beneficiaryDetailsModel.getTestsList());
                    intentEdit.putExtra(BundleConstants.ORDER_DETAILS_MODEL, orderDetailsModel);
                    startActivityForResult(intentEdit, BundleConstants.EDIT_TESTS_START);
                }
                else{
                    final ArrayList<String> testCodesList = new ArrayList<String>();
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
                            dialog.dismiss();
                        }
                    }).setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            Toast.makeText(activity, "Feature Coming Soon..", Toast.LENGTH_SHORT).show();
//                            dialog.dismiss();
                            if(testCodesList.size()==beneficiaryDetailsModel.getTestsList().size()) {
                                Intent intentEdit = new Intent(activity, EditTestListActivity.class);
                                intentEdit.putExtra(BundleConstants.REST_BEN_TESTS_LIST, restOfTestsList);
                                intentEdit.putExtra(BundleConstants.SELECTED_TESTS_LIST, beneficiaryDetailsModel.getTestsList());
                                intentEdit.putExtra(BundleConstants.ORDER_DETAILS_MODEL, orderDetailsModel);
                                startActivityForResult(intentEdit, BundleConstants.EDIT_TESTS_START);
                                dialog.dismiss();
                            }
                            else{
                                Toast.makeText(activity,"Tests & Profiles cannot be edited for DSA Orders",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.show();
                }
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
                        public void onCheckChange(ArrayList<BeneficiaryTestWiseClinicalHistoryModel> chArr) {
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
        edtBenName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String benName = s.toString();
                if(InputUtils.isNull(benName)){
                    beneficiaryDetailsModel.setName("");
                }
                else{
                    beneficiaryDetailsModel.setName(benName);
                }
                beneficiaryDetailsDao.insertOrUpdate(beneficiaryDetailsModel);
            }
        });
        edtAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String benAge = s.toString();
                if(InputUtils.isNull(benAge)){
                    beneficiaryDetailsModel.setAge(0);
                }
                else{
                    beneficiaryDetailsModel.setAge(Integer.parseInt(benAge));
                }
                beneficiaryDetailsDao.insertOrUpdate(beneficiaryDetailsModel);
            }
        });
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private boolean validate() {
        if(InputUtils.isNull(edtBenName.getText().toString())){
            Toast.makeText(activity, "Beneficiary Name is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(InputUtils.isNull(edtAge.getText().toString())){
            Toast.makeText(activity, "Beneficiary Age is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(InputUtils.isNull(edtTests.getText().toString())){
            Toast.makeText(activity, "Beneficiary Tests List is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(InputUtils.isNull(encodedVanipunctureImg)){
            Toast.makeText(activity, "Venepuncture Image is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(beneficiaryDetailsModel.getBarcodedtl()!=null && beneficiaryDetailsModel.getBarcodedtl().size()>0){
            for (BeneficiaryBarcodeDetailsModel benBarcode:
                 beneficiaryDetailsModel.getBarcodedtl()) {
                if(InputUtils.isNull(benBarcode.getBarcode())){
                    Toast.makeText(activity, "Please scan Barcode for Sample Type "+benBarcode.getSamplType(), Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        return true;
    }

    private void initData() {
        beneficiaryDetailsModel = beneficiaryDetailsDao.getModelFromId(beneficiaryDetailsModel.getBenId());
        orderDetailsModel = orderDetailsDao.getModelFromId(orderDetailsModel.getOrderNo());

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
            if(beneficiaryDetailsModel!=null && beneficiaryDetailsModel.getBarcodedtl()==null){
                beneficiaryDetailsModel.setBarcodedtl(new ArrayList<BeneficiaryBarcodeDetailsModel>());
            }
            if(beneficiaryDetailsModel!=null && beneficiaryDetailsModel.getSampleType()!=null) {
                beneficiaryDetailsModel.getBarcodedtl().clear();
                for (BeneficiarySampleTypeDetailsModel sampleTypes :
                        beneficiaryDetailsModel.getSampleType()) {
                    BeneficiaryBarcodeDetailsModel beneficiaryBarcodeDetailsModel = new BeneficiaryBarcodeDetailsModel();
                    beneficiaryBarcodeDetailsModel.setBenId(sampleTypes.getBenId());
                    beneficiaryBarcodeDetailsModel.setId(DeviceUtils.getRandomUUID());
                    beneficiaryBarcodeDetailsModel.setSamplType(sampleTypes.getSampleType());
                    beneficiaryBarcodeDetailsModel.setOrderNo(beneficiaryDetailsModel.getOrderNo());
                    beneficiaryDetailsModel.getBarcodedtl().add(beneficiaryBarcodeDetailsModel);
                }
                beneficiaryDetailsDao.insertOrUpdate(beneficiaryDetailsModel);
            }
        }
        restOfTestsList = new ArrayList<>();
        TestRateMasterDao testRateMasterDao = new TestRateMasterDao(dhbDao.getDb());
        for (BeneficiaryDetailsModel beneficiaryModel:
                orderDetailsModel.getBenMaster()) {
            if(beneficiaryDetailsModel.getBenId()!=beneficiaryModel.getBenId()){
                restOfTestsList.addAll(testRateMasterDao.getModelsFromTestCodes(beneficiaryModel.getTestsCode()));
            }
        }
        if(beneficiaryDetailsModel!=null){
            edtBenName.setText(!InputUtils.isNull(beneficiaryDetailsModel.getName()) ? beneficiaryDetailsModel.getName() : "");
            edtAge.setText(!InputUtils.isNull(beneficiaryDetailsModel.getAge()+"") ? beneficiaryDetailsModel.getAge()+"" : "");
            edtTests.setText(!InputUtils.isNull(beneficiaryDetailsModel.getTestsCode()) ? beneficiaryDetailsModel.getTestsCode() : "");
            //TODO get clinical History and set it in edtCH
            //TODO on click of edtCH open a Dialog box and allow multi selection of clinical histories
            if(!InputUtils.isNull(beneficiaryDetailsModel.getGender())){
                if(beneficiaryDetailsModel.getGender().equals("M")||beneficiaryDetailsModel.getGender().equalsIgnoreCase("Male")) {
                    isM = true;
                    imgFemale.setImageDrawable(getResources().getDrawable(R.drawable.female));
                    imgMale.setImageDrawable(getResources().getDrawable(R.drawable.m_selected));
                }
                else{
                    isM = false;
                    imgFemale.setImageDrawable(getResources().getDrawable(R.drawable.f_selected));
                    imgMale.setImageDrawable(getResources().getDrawable(R.drawable.male));
                }
            }
            if(!InputUtils.isNull(beneficiaryDetailsModel.getVenepuncture()))
            {
                imgVenipuncture.setImageDrawable(activity.getResources().getDrawable(R.drawable.camera_blue));
                encodedVanipunctureImg = beneficiaryDetailsModel.getVenepuncture();
            }
            benCHArr = beneficiaryDetailsModel.getClHistory();
            benLAArr = beneficiaryDetailsModel.getLabAlert();
            String chS = "";
            if(benCHArr!=null && benCHArr.size()>0) {
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
        }
        if(orderDetailsModel!=null){
            if(orderDetailsModel.getReportHC()==0) {
                imgHC.setImageDrawable(activity.getResources().getDrawable(R.drawable.tick_icon));
                isHC = false;
            }else{
                imgHC.setImageDrawable(activity.getResources().getDrawable(R.drawable.green_tick_icon));
                isHC = true;
            }
            restOfTestsList = new ArrayList<>();
            for (BeneficiaryDetailsModel beneficiaryModel:
                    orderDetailsModel.getBenMaster()) {
                if(beneficiaryDetailsModel.getBenId()!=beneficiaryModel.getBenId()){
                    restOfTestsList.addAll(testRateMasterDao.getModelsFromTestCodes(beneficiaryModel.getTestsCode()));
                }
            }
        }
        initScanBarcodeView();
    }

    @Override
    public void initUI() {
        super.initUI();
        edtBenName = (EditText) findViewById(R.id.edt_ben_name);
        edtAge = (EditText) findViewById(R.id.edt_age);
        edtTests = (TextView) findViewById(R.id.edt_test);
        edtAadhar = (EditText) findViewById(R.id.title_aadhar_no);
        edtCH = (TextView) findViewById(R.id.edt_clinical_history);
        edtLA = (TextView) findViewById(R.id.edt_lab_alerts);
        edtRemarks = (EditText) findViewById(R.id.edt_customer_sign);
        imgMale = (ImageView) findViewById(R.id.img_male);
        imgFemale = (ImageView) findViewById(R.id.img_female);
        imgVenipuncture = (ImageView) findViewById(R.id.img_venipuncture);
        llBarcodes = (LinearLayout) findViewById(R.id.ll_barcodes);
        tlBarcodes = (TableLayout) findViewById(R.id.tl_barcodes);
        imgHC = (ImageView) findViewById(R.id.hard_copy_check);
        btnSave = (Button) findViewById(R.id.btn_save);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if ((scanningResult != null) && (scanningResult.getContents() != null)) {
            String scanned_barcode = scanningResult.getContents();
            if (!InputUtils.isNull(scanned_barcode) && scanned_barcode.length() == 8) {
                if(beneficiaryDetailsModel.getBarcodedtl()!=null) {
                    for (int i = 0; i < beneficiaryDetailsModel.getBarcodedtl().size(); i++) {
                        if (!InputUtils.isNull(beneficiaryDetailsModel.getBarcodedtl().get(i).getSamplType())
                                && currentScanSampleType.equals(beneficiaryDetailsModel.getBarcodedtl().get(i).getSamplType())) {
                            //CHECK for duplicate barcode scanned for the same visit
                            OrderVisitDetailsModel orderVisitDetailsModel = orderDetailsDao.getOrderVisitModel(orderDetailsModel.getVisitId());
                            for (OrderDetailsModel odm :
                                    orderVisitDetailsModel.getAllOrderdetails()) {
                                for (BeneficiaryDetailsModel bdm :
                                        odm.getBenMaster()) {
                                    if(bdm.getBarcodedtl()!=null && bdm.getBarcodedtl().size()>0) {
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
                }
                else{
                    Toast.makeText(activity,"Failed to Update Scanned Barcode Value",Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(activity,"Failed to Scan Barcode",Toast.LENGTH_SHORT).show();
            }
        }
        if (resultCode == Activity.RESULT_OK) {
            imgVenipuncture.setImageDrawable(activity.getResources().getDrawable(R.drawable.camera_blue));
            if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }
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
                        if(testRateMasterModel.getTestType().equals("OFFER")) {
                            testsCode = testRateMasterModel.getDescription();
                            beneficiaryDetailsModel.setProjId(testRateMasterModel.getTestCode());
                        }
                        else{
                            testsCode = testRateMasterModel.getTestCode();
                        }
                    } else {
                        if(testRateMasterModel.getTestType().equals("OFFER")) {
                            testsCode = testsCode + "," + testRateMasterModel.getDescription();
                            beneficiaryDetailsModel.setProjId(testRateMasterModel.getTestCode());
                        }
                        else{
                            testsCode = testsCode + "," + testRateMasterModel.getTestCode();

                        }
                    }
                }
                if(InputUtils.isNull(beneficiaryDetailsModel.getProjId())){
                    beneficiaryDetailsModel.setProjId("");
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
                for (TestRateMasterModel trmm:
                     selectedTests) {
                    if(!trmm.getFasting().toLowerCase().contains("non")){
                        isFasting = true;
                        break;
                    }
                }
            }
            beneficiaryDetailsModel.setTestsCode(testsCode);
            beneficiaryDetailsModel.setTests(testsCode);
            edtTests.setText(testsCode);
            if(isFasting) {
                beneficiaryDetailsModel.setFasting("Fasting");
            }
            else{
                beneficiaryDetailsModel.setFasting("Non-Fasting");
            }
            beneficiaryDetailsDao.insertOrUpdate(beneficiaryDetailsModel);
            initData();
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
        beneficiaryDetailsDao.insertOrUpdate(beneficiaryDetailsModel);
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
        for (OrderDetailsModel orderDetailsModel:
                orderDetailsDao.getModelsFromVisitId(orderVisitDetailsModel.getVisitId())) {
            ArrayList<BeneficiaryDetailsModel> tempBenArr = new ArrayList<>();
            tempBenArr = beneficiaryDetailsDao.getModelsFromOrderNo(orderDetailsModel.getOrderNo());
            if(tempBenArr!=null) {
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

        for (BeneficiaryDetailsModel beneficiaryDetailsModel:
                benArr) {
            if(beneficiaryDetailsModel.getBarcodedtl()!=null) {
                benBarcodeArr.addAll(beneficiaryDetailsModel.getBarcodedtl());
            }
            if(beneficiaryDetailsModel.getSampleType()!=null) {
                benSTArr.addAll(beneficiaryDetailsModel.getSampleType());
            }
            if(beneficiaryDetailsModel.getClHistory()!=null) {
                benCHArr.addAll(beneficiaryDetailsModel.getClHistory());
            }
            if(beneficiaryDetailsModel.getLabAlert()!=null) {
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

}
