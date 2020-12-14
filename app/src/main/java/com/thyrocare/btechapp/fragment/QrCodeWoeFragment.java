package com.thyrocare.btechapp.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mindorks.paracamera.Camera;
import com.thyrocare.btechapp.Controller.AccessTokenAndOTPAPIController;
import com.thyrocare.btechapp.NewScreenDesigns.Controllers.GetAcessTokenAndOTPAPIController;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.RequestOTPModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.SubmitB2BWoeRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.WOEOtpValidationRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.B2BWoeResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.CommonPOSTResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.DateUtil;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.Validator;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.PostAPI_SingletonClass;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.HomeScreenActivity;
import com.thyrocare.btechapp.adapter.QrCodeBasedBarcodeScanAdapter;
import com.thyrocare.btechapp.dao.utils.ConnectionDetector;
import com.thyrocare.btechapp.models.api.request.GetPatientDetailsRequestModel;
import com.thyrocare.btechapp.models.api.request.OrderPassRequestModel;
import com.thyrocare.btechapp.models.api.request.SendOTPRequestModel;
import com.thyrocare.btechapp.models.api.response.CommonResponseModel2;
import com.thyrocare.btechapp.models.api.response.QrcodeBasedPatientDetailsResponseModel;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import application.ApplicationController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.NO_DATA_FOUND;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SOMETHING_WENT_WRONG;
import static com.thyrocare.btechapp.utils.app.AppConstants.MSG_SERVER_EXCEPTION;
import static com.thyrocare.btechapp.utils.app.BundleConstants.API_FOR_OTP;
import static com.thyrocare.btechapp.utils.app.BundleConstants.Apikey_WOE;

/**
 * A simple {@link Fragment} subclass.
 */
public class QrCodeWoeFragment extends Fragment {

    public static final String TAG_FRAGMENT = QrCodeWoeFragment.class.getSimpleName();
    Activity mActivity;
    HomeScreenActivity activity;
    AppPreferenceManager appPreferenceManager;
    ConnectionDetector cd;
    Global global;

    private static QrCodeWoeFragment fragment;
    private CardView cdView_ScanQRCode, cdView_Form, cdView_UploadImageAndBarcode;
    private RelativeLayout rel_buttons;
    private EditText edt_Product, edt_Name, edt_Age, edt_mobileNumber, edt_EmailId, edt_Address, edt_Pincode;
    private RadioButton rb_male, rb_female;
    private RecyclerView recyle_barcode;
    private ImageView img_uploadBenVail, imgDelete;
    private TextView txt_captureBenBarcodePic;
    private Button btn_Edit, btn_Finish;
    private Camera camera;
    private IntentIntegrator intentIntegrator;
    private File VialImageFile;
    private QrcodeBasedPatientDetailsResponseModel.PatientDataBean patientDetailsModel;
    private Dialog CustomDialogforOTPValidation;
    private QrCodeBasedBarcodeScanAdapter qrCodeBasedBarcodeScanAdapter;
    private Boolean isEditOptionEnabled = false;
    private RadioGroup rb_group;
    private String strGender = "";
    private String barcodeSampleTypeToScan;
    private int barcodePositionToScan = 0;
    private boolean isBarcodeScan = false;


    public QrCodeWoeFragment() {
        // Required empty public constructor
    }

    public static QrCodeWoeFragment newInstance() {
        fragment = new QrCodeWoeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContext();
    }

    private void initContext() {
        mActivity = getActivity();
        activity = (HomeScreenActivity) getActivity();
        cd = new ConnectionDetector(mActivity);
        appPreferenceManager = new AppPreferenceManager(mActivity);
        global = new Global(mActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_qr_code_woe, container, false);

        initView(v);
        initListeners();
        initData();

        return v;
    }

    private void initView(View v) {

        cdView_ScanQRCode = (CardView) v.findViewById(R.id.cdView_ScanQRCode);
        cdView_Form = (CardView) v.findViewById(R.id.cdView_Form);
        cdView_UploadImageAndBarcode = (CardView) v.findViewById(R.id.cdView_UploadImageAndBarcode);

        rel_buttons = (RelativeLayout) v.findViewById(R.id.rel_buttons);

        edt_Product = (EditText) v.findViewById(R.id.edt_Product);
        edt_Name = (EditText) v.findViewById(R.id.edt_Name);
        rb_male = (RadioButton) v.findViewById(R.id.rb_male);
        rb_female = (RadioButton) v.findViewById(R.id.rb_female);
        rb_group = (RadioGroup) v.findViewById(R.id.rb_group);
        edt_Age = (EditText) v.findViewById(R.id.edt_Age);
        edt_mobileNumber = (EditText) v.findViewById(R.id.edt_mobileNumber);
        edt_EmailId = (EditText) v.findViewById(R.id.edt_EmailId);
        edt_Address = (EditText) v.findViewById(R.id.edt_Address);
        edt_Pincode = (EditText) v.findViewById(R.id.edt_Pincode);

        recyle_barcode = (RecyclerView) v.findViewById(R.id.recyle_barcode);
        img_uploadBenVail = (ImageView) v.findViewById(R.id.img_uploadBenVail);
        imgDelete = (ImageView) v.findViewById(R.id.imgDelete);
        txt_captureBenBarcodePic = (TextView) v.findViewById(R.id.txt_captureBenBarcodePic);


        btn_Edit = (Button) v.findViewById(R.id.btn_Edit);
        btn_Finish = (Button) v.findViewById(R.id.btn_Finish);

    }


    private void initListeners() {
        cdView_ScanQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cdView_Form.getVisibility() == View.VISIBLE){
                    androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder;
                    alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(mActivity);
                    alertDialogBuilder
                            .setMessage(ConstantsMessages.ThisWillResetAllPatientDetails)
                            .setCancelable(true)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    resetAllFields();
                                    OpenScanPatientQrCodeScreen();
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }else{
                    OpenScanPatientQrCodeScreen();
                }

            }
        });

        rb_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                strGender = "";
                if (checkedId == R.id.rb_male) {
                    strGender = "M";
                } else if (checkedId == R.id.rb_female) {
                    strGender = "F";
                }
            }
        });

        edt_Address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String startingchar = s.toString();
                if (!InputUtils.isNull(startingchar)){
                    if (startingchar.startsWith(",") ||
                            startingchar.startsWith(".") ||
                            startingchar.startsWith("/") ||
                            startingchar.startsWith("-") ){
                        edt_Address.setText("");
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        img_uploadBenVail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CaptureVailImagePic();
            }
        });

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetVialImageView();
            }
        });

        btn_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder;
                alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(mActivity);
                alertDialogBuilder
                        .setMessage(ConstantsMessages.AreYouSureYouWantToEditPatientDetails)
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (patientDetailsModel != null  && !InputUtils.isNull(patientDetailsModel.getOrderNo()) && !InputUtils.isNull(patientDetailsModel.getMobile())){
                                    CallGenerateOTPApi( patientDetailsModel.getMobile(),"SENDOTPALL");
                                }
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });


        btn_Finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    if (cd.isConnectingToInternet()) {
                        CallSubmitPatientDetailsAPI();
                    } else {
                        global.showCustomToast(mActivity, ConstantsMessages.CHECK_INTERNET_CONN);
                    }
                }
            }
        });
    }

    private void initData() {
        resetAllFields();
    }

    private void CallSubmitPatientDetailsAPI() {


        SubmitB2BWoeRequestModel submitB2BWoeRequestModel = new SubmitB2BWoeRequestModel();
        SubmitB2BWoeRequestModel.Woe woe = new SubmitB2BWoeRequestModel.Woe();
        woe.setAADHAR_NO("");
        woe.setADDRESS(edt_Address.getText().toString().trim());
        woe.setAGE(InputUtils.parseInt(edt_Age.getText().toString().trim()));
        woe.setAGE_TYPE("Y");
        woe.setALERT_MESSAGE("");
        woe.setAMOUNT_COLLECTED(patientDetailsModel.getRate());
        woe.setAMOUNT_DUE(0);
        String version = "";
        try {
            version = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        woe.setAPP_ID(version);
        woe.setBCT_ID(appPreferenceManager.getLoginResponseModel().getUserID());
        woe.setBRAND("TTL");
        woe.setCAMP_ID("");
        woe.setCONT_PERSON("");
        woe.setCONTACT_NO(edt_mobileNumber.getText().toString().trim());
        woe.setCUSTOMER_ID("");
        woe.setDELIVERY_MODE(2);
        woe.setEMAIL_ID(edt_EmailId.getText().toString().trim());
        woe.setENTERED_BY(appPreferenceManager.getLoginResponseModel().getUserID());
        woe.setGENDER(strGender);
        woe.setLAB_ADDRESS(edt_Address.getText().toString().trim().toUpperCase());
        woe.setLAB_ID("");
        woe.setLAB_NAME("");
        woe.setLEAD_ID(patientDetailsModel.getId());
        woe.setMAIN_SOURCE(patientDetailsModel.getTsp());
        woe.setORDER_NO(patientDetailsModel.getOrderNo());
        woe.setOS("");
        woe.setPATIENT_NAME(edt_Name.getText().toString().trim().toUpperCase());
        woe.setPINCODE(edt_Pincode.getText().toString().trim());
        woe.setPRODUCT(patientDetailsModel.getProduct());
        woe.setREF_DR_ID("");
        woe.setREF_DR_NAME("Self");
        woe.setREMARKS("MOBILE");
        woe.setSPECIMEN_COLLECTION_TIME(DateUtil.getDateFromLong(System.currentTimeMillis(),"yyy-MM-dd HH:mm"));
        woe.setSPECIMEN_SOURCE("");
        woe.setSR_NO(1);
        woe.setSTATUS("N");
        woe.setSUB_SOURCE_CODE(patientDetailsModel.getTsp());
        woe.setTOTAL_AMOUNT(InputUtils.parseInt(patientDetailsModel.getRate()));
        woe.setTYPE("CAMP");
        woe.setWATER_SOURCE("");
        woe.setWO_MODE("BTECHQRAPP");
        woe.setWO_STAGE(3);
        woe.setPurpose("");
        woe.setADDITIONAL3("EDIT");

        submitB2BWoeRequestModel.setWoe(woe);

        ArrayList<SubmitB2BWoeRequestModel.Barcodelist> barcodelist = new ArrayList<>();
        if (!InputUtils.isNull(patientDetailsModel.getTestData())){
            for (int i = 0; i < patientDetailsModel.getTestData().size(); i++) {
                SubmitB2BWoeRequestModel.Barcodelist barcodelistModel = new SubmitB2BWoeRequestModel.Barcodelist();
                barcodelistModel.setBARCODE(patientDetailsModel.getTestData().get(i).getBarcode());
                barcodelistModel.setSAMPLE_TYPE(patientDetailsModel.getTestData().get(i).getSampleType());
                barcodelistModel.setTESTS(patientDetailsModel.getTestData().get(i).getTest());
                barcodelist.add(barcodelistModel);
            }
        }

        submitB2BWoeRequestModel.setBarcodelist(barcodelist);
        submitB2BWoeRequestModel.setWoe_type("WOE");
        submitB2BWoeRequestModel.setApi_key(Apikey_WOE);//api_key

        CallSubmitWoeDetailsAPI(submitB2BWoeRequestModel);
    }

    private void CallSubmitWoeDetailsAPI(final SubmitB2BWoeRequestModel submitB2BWoeRequestModel) {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.DecodeString64(mActivity.getString(R.string.B2B_API_VERSION))).create(PostAPIInterface.class);
        Call<B2BWoeResponseModel> responseCall = apiInterface.CallQrCodeBasedSubmitWOEAPI(submitB2BWoeRequestModel);
        global.showProgressDialog(mActivity,"Please wait..");
        responseCall.enqueue(new Callback<B2BWoeResponseModel>() {
            @Override
            public void onResponse(Call<B2BWoeResponseModel> call, Response<B2BWoeResponseModel> response) {
                global.hideProgressDialog(mActivity);
                if (response.isSuccessful() && response.body() != null){
                    onSubmitWoeResponseReceived(response.body());
                }else{
                    Toast.makeText(mActivity, SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<B2BWoeResponseModel> call, Throwable t) {
                global.hideProgressDialog(mActivity);
            }
        });
    }

    private void onSubmitWoeResponseReceived(B2BWoeResponseModel b2BWoeResponseModel) {

        if (b2BWoeResponseModel.getRES_ID() != null && b2BWoeResponseModel.getRES_ID().equalsIgnoreCase(ConstantsMessages.RES0000)){
            androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder;
            alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(mActivity);
            alertDialogBuilder
                    .setMessage(ConstantsMessages.WOE_DoneSuccessfully)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            try {
                                resetAllFields();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
            androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }else{
            global.showCustomToast(mActivity,!StringUtils.isNull(b2BWoeResponseModel.getMessage()) ? b2BWoeResponseModel.getMessage() : ConstantsMessages.WOESubmitFailed);
        }

    }

    private boolean isValid() {

        if (isEditOptionEnabled) {

            if (!Validator.isValidUserName(edt_Name.getText().toString().trim())) {
                global.showCustomToast(mActivity, ConstantsMessages.EnterValidFullName);
                edt_Name.requestFocus();
                return false;
            }

            if (!Validator.isValidMobile(edt_mobileNumber.getText().toString().trim())) {
                global.showCustomToast(mActivity, ConstantsMessages.EnterValidMobileNumber);
                edt_mobileNumber.requestFocus();
                return false;
            }

            if (!Validator.isValidEmail(edt_EmailId.getText().toString().trim())) {
                global.showCustomToast(mActivity, ConstantsMessages.EnterValidEmailId);
                edt_EmailId.requestFocus();
                return false;
            }

            if (InputUtils.isNull(strGender)) {
                global.showCustomToast(mActivity, ConstantsMessages.PleaseSelectGender);
            }

            if (!Validator.isValidAge(InputUtils.parseInt(edt_Age.getText().toString().trim()))) {
                global.showCustomToast(mActivity, ConstantsMessages.EnterValidPatientAge);
                edt_Age.requestFocus();
                return false;
            }

            if (!Validator.isValidAddress(edt_Address.getText().toString().trim())) {
                global.showCustomToast(mActivity, ConstantsMessages.EnterValidAddress);
                edt_Address.requestFocus();
                return false;
            }

            if (!Validator.isValidPincode(edt_Pincode.getText().toString().trim())) {
                global.showCustomToast(mActivity, ConstantsMessages.EnterValidPincodeMsg);
                edt_Pincode.requestFocus();
                return false;
            }
        }


        if (!InputUtils.isNull(patientDetailsModel.getTestData())) {
            for (int i = 0; i < patientDetailsModel.getTestData().size(); i++) {
                if (InputUtils.isNull(patientDetailsModel.getTestData().get(i).getBarcode())) {
                    String sampletype = !InputUtils.isNull(patientDetailsModel.getTestData().get(i).getSampleType()) ? "for SampleType : " + patientDetailsModel.getTestData().get(i).getSampleType() : "";
                    global.showCustomToast(mActivity, ConstantsMessages.PleaseScanBarcode + sampletype);
                    return false;
                }
            }
        } else {
            global.showCustomToast(mActivity, ConstantsMessages.InvalidBarcodedetails);
            return false;
        }


        // Todo Vail Image option Hide as per input received from Sachin Sir.
       /* if (VialImageFile == null || !VialImageFile.exists()) {
            global.showCustomToast(mActivity, ConstantsMessages.UploadVailImage);
            return false;
        }*/

        return true;
    }

    private void CallGetPatientDetailAPI(String OrderID) {

        resetAllFields();
        GetPatientDetailsRequestModel model = new GetPatientDetailsRequestModel();
        model.setOrderNo(OrderID);

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.DecodeString64(mActivity.getString(R.string.B2B_API_VERSION))).create(PostAPIInterface.class);
        Call<QrcodeBasedPatientDetailsResponseModel> responseCall = apiInterface.CallGetQRCodeBasedPatientDetailsAPI(model);
        global.showProgressDialog(mActivity, ConstantsMessages.PLEASE_WAIT, false);
        responseCall.enqueue(new Callback<QrcodeBasedPatientDetailsResponseModel>() {
            @Override
            public void onResponse(Call<QrcodeBasedPatientDetailsResponseModel> call, retrofit2.Response<QrcodeBasedPatientDetailsResponseModel> response) {
                global.hideProgressDialog(mActivity);
                if (response.isSuccessful() && response.body() != null) {
                    QrcodeBasedPatientDetailsResponseModel responseModel = response.body();

                    if (InputUtils.CheckEqualIgnoreCase(responseModel.getResponseId(), ConstantsMessages.RES0000) && !InputUtils.isNull(responseModel.getPatientData()) && !InputUtils.isNull(responseModel.getPatientData().get(0).getOrderNo()) && !InputUtils.isNull(responseModel.getPatientData().get(0).getMobile()) && !InputUtils.isNull(responseModel.getPatientData().get(0).getTestData())) {
                        patientDetailsModel = responseModel.getPatientData().get(0);
                        onPatientDetailsReceived();
                    } else {
                        global.showCustomToast(mActivity,!InputUtils.isNull(responseModel.getResponse()) ? responseModel.getResponse() : ConstantsMessages.InvalidPatientDetails);
                    }
                } else {
                    global.showCustomToast(mActivity, NO_DATA_FOUND);
                }
            }

            @Override
            public void onFailure(Call<QrcodeBasedPatientDetailsResponseModel> call, Throwable t) {
                global.hideProgressDialog(mActivity);
                global.showCustomToast(mActivity, NO_DATA_FOUND);
            }
        });
    }

    private void onPatientDetailsReceived() {

        cdView_Form.setVisibility(View.VISIBLE);
        rel_buttons.setVisibility(View.GONE);

        EnableDisableAllEditFields(false);

        InputUtils.setTextToTextView(edt_Product, patientDetailsModel.getProduct());
        InputUtils.setTextToTextView(edt_Name, patientDetailsModel.getName());
        InputUtils.setTextToTextView(edt_Age, patientDetailsModel.getAge());
        InputUtils.setTextToTextView(edt_mobileNumber, patientDetailsModel.getMobile());
        InputUtils.setTextToTextView(edt_EmailId, patientDetailsModel.getEmail());
        InputUtils.setTextToTextView(edt_Address, patientDetailsModel.getAddress());
        InputUtils.setTextToTextView(edt_Pincode, patientDetailsModel.getPincode());

        if (InputUtils.CheckEqualIgnoreCase(patientDetailsModel.getGender(), "M") || InputUtils.CheckEqualIgnoreCase(patientDetailsModel.getGender(), "Male")) {
            rb_male.setChecked(true);
        } else {
            rb_female.setChecked(true);
        }

        initScanBarcodeListView();

        cdView_UploadImageAndBarcode.setVisibility(View.VISIBLE);
        rel_buttons.setVisibility(View.VISIBLE);

    }

    private void initScanBarcodeListView() {
        if (patientDetailsModel.getTestData() != null && patientDetailsModel.getTestData().size() > 0) {

            // TODO code to show Primary and secondary serum
            int serumCount = 0;
            for (int i = 0; i < patientDetailsModel.getTestData().size(); i++) {
                if (patientDetailsModel.getTestData().get(i).getSampleType().equalsIgnoreCase("SERUM")) {
                    serumCount++;
                }
            }

            recyle_barcode.setLayoutManager(new LinearLayoutManager(mActivity));
            qrCodeBasedBarcodeScanAdapter = new QrCodeBasedBarcodeScanAdapter(mActivity, serumCount, patientDetailsModel);
            qrCodeBasedBarcodeScanAdapter.setOnItemClickListener(new QrCodeBasedBarcodeScanAdapter.OnItemClickListener() {
                @Override
                public void onBarcodeScanClicked(String SampleType, int barcodePosition) {
                    barcodeSampleTypeToScan = SampleType;
                    barcodePositionToScan = barcodePosition;
                    OpenScanBarcodeScreen();
                }

                @Override
                public void onBarcodeDelete(String barcode) {
                    if (!InputUtils.isNull(patientDetailsModel.getTestData())) {
                        for (int i = 0; i < patientDetailsModel.getTestData().size(); i++) {
                            if (StringUtils.CheckEqualIgnoreCase(patientDetailsModel.getTestData().get(i).getBarcode(), barcode)) {
                                patientDetailsModel.getTestData().get(i).setBarcode("");
                                break;
                            }
                        }
                        initScanBarcodeListView();
                    }
                }
            });
            recyle_barcode.setAdapter(qrCodeBasedBarcodeScanAdapter);
            recyle_barcode.setVisibility(View.VISIBLE);
        } else {
            recyle_barcode.setVisibility(View.GONE);
            global.showCustomToast(mActivity, ConstantsMessages.SampleTypeNotFound);
        }
    }

    private void OpenBarcodeConfirmationDialog(final String scanned_barcode) {

        try {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(mActivity);
            builder1.setTitle(ConstantsMessages.CheckBarcode)
                    .setMessage(ConstantsMessages.DoYouWantToProceedWiththisBarcode + scanned_barcode + "?")
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (TextUtils.isEmpty(scanned_barcode) || scanned_barcode.startsWith("0") || scanned_barcode.startsWith("$") || scanned_barcode.startsWith("1") || scanned_barcode.startsWith(" ") /*|| Character.isDigit(scanned_barcode.charAt(0))*/) {
                        Toast.makeText(mActivity, "Invalid barcode", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!InputUtils.isNull(scanned_barcode) && scanned_barcode.length() == 8) {
                            if (!InputUtils.isNull(patientDetailsModel.getTestData())) {

                                for (int i = 0; i < patientDetailsModel.getTestData().size(); i++) {
                                    if (i == barcodePositionToScan && InputUtils.CheckEqualIgnoreCase(patientDetailsModel.getTestData().get(i).getSampleType(), barcodeSampleTypeToScan)) {
                                        patientDetailsModel.getTestData().get(i).setBarcode(scanned_barcode);
                                        initScanBarcodeListView();
                                        break;
                                    }
                                }
                            } else {
                                Toast.makeText(mActivity, ConstantsMessages.FailedToUpdateScanBarcodevalue, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(mActivity, ConstantsMessages.FailedToScanBarcode, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void CallGenerateOTPApi(final String mobileNumber, String Purpose) {

        AccessTokenAndOTPAPIController accessTokenAndOTPAPIController = new AccessTokenAndOTPAPIController(mActivity);
        accessTokenAndOTPAPIController.CallGetTokenAPIForOTP(mobileNumber, Purpose, null);
        accessTokenAndOTPAPIController.setOnResponseListener(new AccessTokenAndOTPAPIController.OnResponseListener() {
            @Override
            public void onSuccess(CommonPOSTResponseModel commonPOSTResponseModel) {
                if (!InputUtils.isNull(commonPOSTResponseModel) && InputUtils.CheckEqualIgnoreCase(commonPOSTResponseModel.getRES_ID(), ConstantsMessages.RES0000)) {
                    global.showCustomToast(mActivity, ConstantsMessages.OTPGeneratedSuccessfully);
                    ShowDialogToVerifyOTP(mobileNumber);
                } else {
                    if (!InputUtils.isNull(commonPOSTResponseModel)) {
                        Global.showCustomStaticToast(mActivity, !StringUtils.isNull(commonPOSTResponseModel.getRESPONSE()) ? commonPOSTResponseModel.getRESPONSE() : ConstantsMessages.OTP_GENERATION_FAILED);
                    } else {
                        Global.showCustomStaticToast(mActivity, ConstantsMessages.OTP_GENERATION_FAILED);
                    }
                }
            }

            @Override
            public void onfailure(CommonPOSTResponseModel commonPOSTResponseModel) {
                Global.showCustomStaticToast(mActivity, ConstantsMessages.OTP_GENERATION_FAILED);
            }
        });
    }

    private void ShowDialogToVerifyOTP( final String MobileNo) {
        CustomDialogforOTPValidation = new Dialog(mActivity);
        CustomDialogforOTPValidation.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        CustomDialogforOTPValidation.requestWindowFeature(Window.FEATURE_NO_TITLE);
        CustomDialogforOTPValidation.setContentView(R.layout.validate_otp_dialog);
        CustomDialogforOTPValidation.setCancelable(false);

        RelativeLayout rel_main = (RelativeLayout) CustomDialogforOTPValidation.findViewById(R.id.rel_main);
        TextView tv_header = (TextView) CustomDialogforOTPValidation.findViewById(R.id.tv_header);
        ImageView img_btn_validateOTP = (ImageView) CustomDialogforOTPValidation.findViewById(R.id.img_btn_validateOTP);
        ImageView img_close = (ImageView) CustomDialogforOTPValidation.findViewById(R.id.img_close);
        final EditText edt_OTP = (EditText) CustomDialogforOTPValidation.findViewById(R.id.edt_OTP);

        tv_header.setText("Please enter OTP send to customer mobile number.");

        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


        int height = 0, width = 0;
        if (displayMetrics != null) {
            try {
                height = displayMetrics.heightPixels;
                width = displayMetrics.widthPixels;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width - 150, FrameLayout.LayoutParams.WRAP_CONTENT);
        rel_main.setLayoutParams(lp);

        CustomDialogforOTPValidation.show();

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialogforOTPValidation.dismiss();
            }
        });


        img_btn_validateOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strOTP = edt_OTP.getText().toString().trim();
                if (!InputUtils.isNull(strOTP) && strOTP.length() != 4) {
                    global.showalert_OK(ConstantsMessages.EnterValidOTP, mActivity);
                    edt_OTP.requestFocus();
                } else {
                    RequestOTPModel model = new RequestOTPModel();
                    model.setApi_key(API_FOR_OTP);
                    model.setMobile(MobileNo);
                    model.setOtpno(edt_OTP.getText().toString().trim());
                    model.setType("VALIDATE_OTP");
                    if (cd.isConnectingToInternet()) {
                        CallAPItoVerifyMobileNumber(model);
                    } else {
                        global.showCustomToast(mActivity, ConstantsMessages.CHECK_INTERNET_CONN);
                    }
                }

            }
        });

    }

    private void CallAPItoVerifyMobileNumber(RequestOTPModel model) {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.DecodeString64(mActivity.getString(R.string.B2C_API_VERSION))).create(PostAPIInterface.class);
        Call<CommonPOSTResponseModel> responseCall = apiInterface.CallValidateOTPForQRcodeBasedWOEAPI(model);
        global.showProgressDialog(mActivity,  ConstantsMessages.PLEASE_WAIT);
        responseCall.enqueue(new Callback<CommonPOSTResponseModel>() {
            @Override
            public void onResponse(Call<CommonPOSTResponseModel> call, Response<CommonPOSTResponseModel> response) {
                global.hideProgressDialog(mActivity);

                if (response.isSuccessful() && response.body() != null) {
                    CommonPOSTResponseModel responseModel = response.body();
                    if (StringUtils.CheckEqualIgnoreCase(responseModel.getRES_ID(), ConstantsMessages.RES0000)) {
                        global.showCustomToast(mActivity, ConstantsMessages.OTP_VERIFIED_SUCCESSFULLY);
                        if (!mActivity.isFinishing() && CustomDialogforOTPValidation != null && CustomDialogforOTPValidation.isShowing()) {
                            CustomDialogforOTPValidation.dismiss();
                        }
                        EnableDisableAllEditFields(true);
                    }else{
                        global.showCustomToast(mActivity, ConstantsMessages.InvalidOTP);
                    }
                } else {
                    global.showCustomToast(mActivity, ConstantsMessages.UnableToConnectMsg);
                }
            }

            @Override
            public void onFailure(Call<CommonPOSTResponseModel> call, Throwable t) {
                global.hideProgressDialog(mActivity);
                global.showCustomToast(mActivity, ConstantsMessages.UnableToConnectMsg);
            }
        });
    }


    private void CaptureVailImagePic() {
        TedPermission.with(mActivity)
                .setPermissions(Manifest.permission.CAMERA)
                .setRationaleMessage(ConstantsMessages.PermissionRequestforVialImageUpload)
                .setRationaleConfirmText("OK")
                .setDeniedMessage(ConstantsMessages.PermissionRejectedMsg)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        selectImage();
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(mActivity, ConstantsMessages.PermissionDenied + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                .check();
    }

    private void selectImage() {
        camera = new Camera.Builder()
                .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(1)
                .setDirectory("BtechApp/BenBarcodePics")
                .setName(patientDetailsModel.getOrderNo() + "_" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_JPEG)
                .setCompression(40)
                .setImageHeight(480)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                .build(this);
        try {
            camera.takePicture();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void OpenScanBarcodeScreen() {

        IntentIntegrator.forSupportFragment(this).initiateScan();
        isBarcodeScan = true;
    }

    private void OpenScanPatientQrCodeScreen() {

        IntentIntegrator.forSupportFragment(this).initiateScan();
        isBarcodeScan = false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == BundleConstants.START_BARCODE_SCAN) {
            if (isBarcodeScan){
                IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                if ((scanningResult != null) && (scanningResult.getContents() != null)) {
                    final String scanned_barcode = scanningResult.getContents().trim();
                    if (!InputUtils.isNull(scanned_barcode)) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                OpenBarcodeConfirmationDialog(scanned_barcode);
                            }
                        }, 300);

                    } else {
                        Toast.makeText(mActivity, ConstantsMessages.RetryScanningbarcode, Toast.LENGTH_SHORT).show();
                    }

                }
            }else{
                IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                if ((scanningResult != null) && (scanningResult.getContents() != null)) {
                    final String scanned_barcode = scanningResult.getContents().trim();
                    if (!InputUtils.isNull(scanned_barcode)) {
                        CallGetPatientDetailAPI(scanned_barcode);
                    } else {
                        Toast.makeText(mActivity, ConstantsMessages.RetryScanningbarcode, Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }  else if (requestCode == Camera.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            String imageurl = "";
            try {
                imageurl = camera.getCameraBitmapPath();
                File BenBarcodePicimagefile = new File(imageurl);
                if (BenBarcodePicimagefile.exists()) {
                    VialImageFile = BenBarcodePicimagefile;
                    boolean ImageUpdated = true;

                    // Todo   Display Image is ImageView
                    txt_captureBenBarcodePic.setText("View Image");
                    txt_captureBenBarcodePic.setPaintFlags(txt_captureBenBarcodePic.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    imgDelete.setVisibility(View.VISIBLE);
                    img_uploadBenVail.setVisibility(View.GONE);

                } else {
                    Toast.makeText(mActivity, ConstantsMessages.FailedtoCapturePhoto, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void resetAllFields() {

        edt_Product.setText("");
        edt_Name.setText("");
        /*rb_male.setChecked(false);
        rb_female.setChecked(false);*/
        edt_Age.setText("");
        edt_mobileNumber.setText("");
        edt_EmailId.setText("");
        edt_Address.setText("");
        edt_Pincode.setText("");

        EnableDisableAllEditFields(false);


        patientDetailsModel = null;
        recyle_barcode.setAdapter(null);
        recyle_barcode.setVisibility(View.GONE);

        resetVialImageView();

        cdView_Form.setVisibility(View.GONE);
        cdView_UploadImageAndBarcode.setVisibility(View.GONE);
        rel_buttons.setVisibility(View.GONE);
        btn_Edit.setVisibility(View.VISIBLE);

    }

    private void resetVialImageView() {
        txt_captureBenBarcodePic.setText("Upload Image");
        txt_captureBenBarcodePic.setPaintFlags(txt_captureBenBarcodePic.getPaintFlags() | 0);
        imgDelete.setVisibility(View.GONE);
        img_uploadBenVail.setVisibility(View.VISIBLE);
        VialImageFile = null;
    }

    private void EnableDisableAllEditFields(Boolean action) {
        isEditOptionEnabled = action;
        edt_Product.setEnabled(false); // Product cannot be changed
        edt_Name.setEnabled(action);
        rb_male.setEnabled(action);
        rb_female.setEnabled(action);
        edt_Age.setEnabled(action);
        edt_mobileNumber.setEnabled(action);
        edt_EmailId.setEnabled(action);
        edt_Address.setEnabled(action);
        edt_Pincode.setEnabled(action);
        if (action){
            btn_Edit.setVisibility(View.INVISIBLE);
        }else{
            btn_Edit.setVisibility(View.VISIBLE);
        }

    }
}
