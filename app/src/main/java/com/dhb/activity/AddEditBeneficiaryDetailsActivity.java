package com.dhb.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.dhb.R;
import com.dhb.dao.DhbDao;
import com.dhb.dao.models.TestRateMasterDao;
import com.dhb.delegate.SelectClinicalHistoryCheckboxDelegate;
import com.dhb.dialog.ClinicalHistorySelectorDialog;
import com.dhb.models.data.BeneficiaryDetailsModel;
import com.dhb.models.data.OrderDetailsModel;
import com.dhb.models.data.TestRateMasterModel;
import com.dhb.models.data.TestWiseBeneficiaryClinicalHistoryModel;
import com.dhb.uiutils.AbstractActivity;
import com.dhb.utils.app.AppPreferenceManager;
import com.dhb.utils.app.BundleConstants;
import com.dhb.utils.app.CommonUtils;
import com.dhb.utils.app.DeviceUtils;
import com.dhb.utils.app.InputUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static com.dhb.utils.app.CommonUtils.encodeImage;

/**
 * Created by ISRO on 5/4/2017.
 */

public class AddEditBeneficiaryDetailsActivity extends AbstractActivity {
    private Activity activity;
    private AppPreferenceManager appPreferenceManager;
    private DhbDao dhbDao;
    private BeneficiaryDetailsModel beneficiaryDetailsModel;
    private OrderDetailsModel orderDetailsModel;
    private EditText edtBenName;
    private EditText edtAge;
    private EditText edtAadhar;
    private ImageView imgMale,imgFemale;
    private EditText edtTests;
    private ImageView imgVenipuncture;
    private ImageView imgHC;
    private EditText edtCH;
    private EditText edtCS;
    private Button btnSave;
    private boolean isHC = false;
    private boolean isEdit = false;
    private String userChoosenTask;
    private static final int REQUEST_CAMERA = 100;
    private Bitmap thumbnail;
    private String encodedVanipunctureImg;
    private boolean isM = false;
    private ArrayList<TestRateMasterModel> restOfTestsList;
    private ArrayList<TestWiseBeneficiaryClinicalHistoryModel> benCHArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_beneficiary);
        activity = this;
        appPreferenceManager = new AppPreferenceManager(activity);
        dhbDao = new DhbDao(activity);
        beneficiaryDetailsModel = getIntent().getExtras().getParcelable(BundleConstants.BENEFICIARY_DETAILS_MODEL);
        orderDetailsModel = getIntent().getExtras().getParcelable(BundleConstants.ORDER_DETAILS_MODEL);
        isEdit = getIntent().getExtras().getBoolean(BundleConstants.IS_BENEFICIARY_EDIT);
        initUI();
        initData();
        initListeners();
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
                    orderDetailsModel.setAddBen(isEdit);
                    orderDetailsModel.setReportHC(isHC?1:0);
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
            }
        });
        imgFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isM = false;
                imgFemale.setBackgroundColor(activity.getResources().getColor(R.color.colorSecondaryDark));
                imgMale.setBackgroundColor(activity.getResources().getColor(android.R.color.white));
            }
        });
        imgHC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isHC){
                    isHC = false;
                    imgHC.setImageDrawable(activity.getResources().getDrawable(R.drawable.tick_icon));
                }else{
                    isHC = true;
                    imgHC.setImageDrawable(activity.getResources().getDrawable(R.drawable.check_mark));
                }
            }
        });
        imgVenipuncture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPhoto();
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
                        intentEdit.putExtra(BundleConstants.BENEFICIARY_TEST_LIST, beneficiaryDetailsModel.getTestsCode());
                        startActivityForResult(intentEdit, BundleConstants.EDIT_TESTS_START);
                    }
                });
                builder.show();
            }
        });

        edtCH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClinicalHistorySelectorDialog clinicalHistorySelectorDialog = new ClinicalHistorySelectorDialog(activity, beneficiaryDetailsModel.getTestsList(), benCHArr, beneficiaryDetailsModel.getBenId(), new SelectClinicalHistoryCheckboxDelegate() {
                    @Override
                    public void onCheckChange(ArrayList<TestWiseBeneficiaryClinicalHistoryModel> chArr) {
                        benCHArr = chArr;
                    }
                });
                clinicalHistorySelectorDialog.show();
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
            return false;
        }
        else if(InputUtils.isNull(edtAge.getText().toString())){
            return false;
        }
        else if(InputUtils.isNull(edtTests.getText().toString())){
            return false;
        }
        else if(InputUtils.isNull(encodedVanipunctureImg)){
            return false;
        }
        return true;
    }

    private void initData() {
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
            TestRateMasterDao testRateMasterDao = new TestRateMasterDao(dhbDao.getDb());
            for (BeneficiaryDetailsModel beneficiaryModel:
                    orderDetailsModel.getBenMaster()) {
                if(beneficiaryDetailsModel.getBenId()!=beneficiaryModel.getBenId()){
                    restOfTestsList.addAll(testRateMasterDao.getModelsFromTestCodes(beneficiaryModel.getTestsCode()));
                }
            }
        }
    }

    @Override
    public void initUI() {
        super.initUI();
        edtBenName = (EditText) findViewById(R.id.edt_ben_name);
        edtAge = (EditText) findViewById(R.id.edt_age);
        edtTests = (EditText) findViewById(R.id.edt_add_test);
        edtAadhar = (EditText) findViewById(R.id.title_aadhar_no);
        edtCH = (EditText) findViewById(R.id.edt_clinical_history);
        edtCS = (EditText) findViewById(R.id.edt_customer_sign);
        imgMale = (ImageView) findViewById(R.id.img_male);
        imgFemale = (ImageView) findViewById(R.id.img_female);
        imgVenipuncture = (ImageView) findViewById(R.id.img_venipuncture);
        imgHC = (ImageView) findViewById(R.id.hard_copy_check);
        btnSave = (Button) findViewById(R.id.btn_save);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        encodedVanipunctureImg = encodeImage(thumbnail);
    }

}
