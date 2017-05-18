package com.dhb.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
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
import com.dhb.dialog.CancelOrderDialog;
import com.dhb.dialog.ClinicalHistorySelectorDialog;
import com.dhb.dialog.LabAlertSelectorDialog;
import com.dhb.dialog.RescheduleOrderDialog;
import com.dhb.models.data.BeneficiaryBarcodeDetailsModel;
import com.dhb.models.data.BeneficiaryDetailsModel;
import com.dhb.models.data.BeneficiaryLabAlertsModel;
import com.dhb.models.data.BeneficiarySampleTypeDetailsModel;
import com.dhb.models.data.LabAlertMasterModel;
import com.dhb.models.data.OrderDetailsModel;
import com.dhb.models.data.TestRateMasterModel;
import com.dhb.models.data.TestSampleTypeModel;
import com.dhb.models.data.BeneficiaryTestWiseClinicalHistoryModel;
import com.dhb.uiutils.AbstractActivity;
import com.dhb.utils.app.AppPreferenceManager;
import com.dhb.utils.app.BundleConstants;
import com.dhb.utils.app.CommonUtils;
import com.dhb.utils.app.DeviceUtils;
import com.dhb.utils.app.InputUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static com.dhb.utils.app.CommonUtils.decodedImageBytes;
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
    private String userChoosenTask;
    private static final int REQUEST_CAMERA = 100;
    private Bitmap thumbnail;
    private String encodedVanipunctureImg;
    private boolean isM = false;
    private TableLayout tlBarcodes;
    private String userChoosenReleaseTask;
    private OrderDetailsModel orderDetailsModel;
    private String currentScanSampleType;
    private RescheduleOrderDialog cdd;
    private CancelOrderDialog cod;
    private boolean isCancelRequesGenereted=false;
    private ArrayList<TestRateMasterModel> restOfTestsList;
    private DhbDao dhbDao;
    private ArrayList<BeneficiaryTestWiseClinicalHistoryModel> benCHArr;
    private ArrayList<BeneficiaryLabAlertsModel> benLAArr;
    private ArrayList<LabAlertMasterModel> labAlertsArr;
    private BeneficiaryDetailsDao beneficiaryDetailsDao;
    private OrderDetailsDao orderDetailsDao;
    private LabAlertMasterDao labAlertMasterDao;
    private IntentIntegrator intentIntegrator;

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

        isEdit = getIntent().getExtras().getBoolean(BundleConstants.IS_BENEFICIARY_EDIT);
        initUI();
        initData();
        initListeners();
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
            for (final BeneficiaryBarcodeDetailsModel beneficiaryBarcodeDetailsModel :
                    beneficiaryDetailsModel.getBarcodedtl()) {
                TableRow tr = (TableRow) activity.getLayoutInflater().inflate(R.layout.item_scan_barcode,null);
                TextView txtSampleType = (TextView)tr.findViewById(R.id.txt_sample_type);
                TextView edtBarcode = (TextView)tr.findViewById(R.id.edt_barcode);
                ImageView imgScan = (ImageView) tr.findViewById(R.id.scan_barcode_button);
                txtSampleType.setText(beneficiaryBarcodeDetailsModel.getSamplType());
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

    private void initListeners() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    beneficiaryDetailsModel.setName(edtBenName.getText().toString().trim());
                    beneficiaryDetailsModel.setAge(Integer.parseInt(edtAge.getText().toString().trim()));
                    beneficiaryDetailsModel.setGender(isM?"F":"M");
                    beneficiaryDetailsModel.setVenepuncture(CommonUtils.decodedImageBytes(encodedVanipunctureImg));
                    beneficiaryDetailsModel.setTestsCode(edtTests.getText().toString());
                    beneficiaryDetailsModel.setTests(edtTests.getText().toString());
                    beneficiaryDetailsDao.insertOrUpdate(beneficiaryDetailsModel);
                    orderDetailsModel.setAddBen(isEdit);
                    orderDetailsModel.setReportHC(isHC?1:0);
                    orderDetailsDao.insertOrUpdate(orderDetailsModel);
                    Intent intentFinish = new Intent();
                    intentFinish.putExtra(BundleConstants.BENEFICIARY_DETAILS_MODEL,beneficiaryDetailsModel);
                    intentFinish.putExtra(BundleConstants.ORDER_DETAILS_MODEL,orderDetailsModel);
                    setResult(BundleConstants.ADD_EDIT_FINISH,intentFinish);
                    finish();
                }
            }
        });
        imgMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isM = true;
                imgMale.setBackgroundColor(activity.getResources().getColor(R.color.colorSecondaryDark));
                imgFemale.setBackgroundColor(activity.getResources().getColor(android.R.color.white));
                beneficiaryDetailsModel.setGender("M");
                beneficiaryDetailsDao.insertOrUpdate(beneficiaryDetailsModel);
            }
        });
        imgFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isM = false;
                imgFemale.setBackgroundColor(activity.getResources().getColor(R.color.colorSecondaryDark));
                imgMale.setBackgroundColor(activity.getResources().getColor(android.R.color.white));
                beneficiaryDetailsModel.setGender("F");
                beneficiaryDetailsDao.insertOrUpdate(beneficiaryDetailsModel);
            }
        });
        imgVenipuncture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPhoto();
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
        return true;
    }

    private void initData() {
        beneficiaryDetailsModel = beneficiaryDetailsDao.getModelFromId(beneficiaryDetailsModel.getBenId());
        orderDetailsModel = orderDetailsDao.getModelFromId(orderDetailsModel.getOrderNo());

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
            if(beneficiaryDetailsModel!=null && beneficiaryDetailsModel.getBarcodedtl()==null){
                beneficiaryDetailsModel.setBarcodedtl(new ArrayList<BeneficiaryBarcodeDetailsModel>());
            }
            if(beneficiaryDetailsModel!=null && beneficiaryDetailsModel.getSampleType()!=null) {
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
                    imgMale.setBackgroundColor(activity.getResources().getColor(R.color.colorSecondaryDark));
                    imgFemale.setBackgroundColor(activity.getResources().getColor(android.R.color.white));
                }
                else{
                    isM = false;
                    imgFemale.setBackgroundColor(activity.getResources().getColor(R.color.colorSecondaryDark));
                    imgMale.setBackgroundColor(activity.getResources().getColor(android.R.color.white));
                }
            }
            if(beneficiaryDetailsModel.getVenepuncture()!=null) {
                encodedVanipunctureImg = encodeImage(beneficiaryDetailsModel.getVenepuncture());
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
                imgHC.setImageDrawable(activity.getResources().getDrawable(R.drawable.check_mark));
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
        if (resultCode == Activity.RESULT_OK) {
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
            beneficiaryDetailsModel.setTestsCode(testsCode);
            beneficiaryDetailsModel.setTests(testsCode);
            edtTests.setText(testsCode);
            beneficiaryDetailsDao.insertOrUpdate(beneficiaryDetailsModel);
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
}
