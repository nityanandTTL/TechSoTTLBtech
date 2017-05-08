package com.dhb.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.dhb.R;
import com.dhb.dao.DhbDao;
import com.dhb.models.data.BeneficiaryDetailsModel;
import com.dhb.models.data.OrderDetailsModel;
import com.dhb.uiutils.AbstractActivity;
import com.dhb.utils.app.AppPreferenceManager;
import com.dhb.utils.app.BundleConstants;
import com.dhb.utils.app.InputUtils;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_beneficiary);
        activity = this;
        appPreferenceManager = new AppPreferenceManager(activity);
        dhbDao = new DhbDao(activity);
        beneficiaryDetailsModel = getIntent().getExtras().getParcelable(BundleConstants.BENEFICIARY_DETAILS_MODEL);
        orderDetailsModel = getIntent().getExtras().getParcelable(BundleConstants.ORDER_DETAILS_MODEL);
        initUI();
        initData();
        initListeners();
    }

    private void initListeners() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(BundleConstants.ADD_EDIT_FINISH);
                finish();
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
    }

    private void initData() {
        if(beneficiaryDetailsModel!=null){
            edtBenName.setText(!InputUtils.isNull(beneficiaryDetailsModel.getName()) ? beneficiaryDetailsModel.getName() : "");
            edtAge.setText(!InputUtils.isNull(beneficiaryDetailsModel.getAge()+"") ? beneficiaryDetailsModel.getAge()+"" : "");
            edtTests.setText(!InputUtils.isNull(beneficiaryDetailsModel.getTests()) ? beneficiaryDetailsModel.getTests() : "");
            edtCH.setText(!InputUtils.isNull(beneficiaryDetailsModel.getTests()) ? beneficiaryDetailsModel.getTests() : "");
            if(!InputUtils.isNull(beneficiaryDetailsModel.getGender())){
                if(beneficiaryDetailsModel.getGender().equals("M")) {
                    imgMale.setBackgroundColor(activity.getResources().getColor(R.color.colorSecondaryDark));
                    imgFemale.setBackgroundColor(activity.getResources().getColor(android.R.color.white));
                }
                else{
                    imgFemale.setBackgroundColor(activity.getResources().getColor(R.color.colorSecondaryDark));
                    imgMale.setBackgroundColor(activity.getResources().getColor(android.R.color.white));
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
}
