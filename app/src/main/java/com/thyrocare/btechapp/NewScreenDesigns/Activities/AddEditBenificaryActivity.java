package com.thyrocare.btechapp.NewScreenDesigns.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.thyrocare.btechapp.NewScreenDesigns.Adapters.DisplaySelectedTestsListForCancellationAdapter_new;
import com.thyrocare.btechapp.NewScreenDesigns.Adapters.ExpandableTestMasterListDisplayAdapter_new;
import com.thyrocare.btechapp.NewScreenDesigns.AddRemoveTestProfileActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.OrderUpdateDetailsModel;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.dao.utils.ConnectionDetector;
import com.thyrocare.btechapp.delegate.RemoveSelectedTestFromListDelegate_new;
import com.thyrocare.btechapp.models.api.request.CartAPIRequestModel;
import com.thyrocare.btechapp.models.api.request.OrderBookingRequestModel;
import com.thyrocare.btechapp.models.api.request.OrderPassRequestModel;
import com.thyrocare.btechapp.models.api.request.RemoveBeneficiaryAPIRequestModel;
import com.thyrocare.btechapp.models.api.request.SendOTPRequestModel;
import com.thyrocare.btechapp.models.api.response.CartAPIResponseModel;
import com.thyrocare.btechapp.models.api.response.CommonResponseModel2;
import com.thyrocare.btechapp.models.data.BeneficiaryBarcodeDetailsModel;
import com.thyrocare.btechapp.models.data.BeneficiaryDetailsModel;
import com.thyrocare.btechapp.models.data.BeneficiaryLabAlertsModel;
import com.thyrocare.btechapp.models.data.BeneficiarySampleTypeDetailsModel;
import com.thyrocare.btechapp.models.data.BeneficiaryTestDetailsModel;
import com.thyrocare.btechapp.models.data.BeneficiaryTestWiseClinicalHistoryModel;
import com.thyrocare.btechapp.models.data.BrandTestMasterModel;
import com.thyrocare.btechapp.models.data.CartRequestBeneficiaryModel;
import com.thyrocare.btechapp.models.data.CartRequestOrderModel;
import com.thyrocare.btechapp.models.data.ChildTestsModel;
import com.thyrocare.btechapp.models.data.KitsCountModel;
import com.thyrocare.btechapp.models.data.OrderBookingDetailsModel;
import com.thyrocare.btechapp.models.data.OrderDetailsModel;
import com.thyrocare.btechapp.models.data.OrderVisitDetailsModel;
import com.thyrocare.btechapp.models.data.TestClinicalHistoryModel;
import com.thyrocare.btechapp.models.data.TestRateMasterModel;
import com.thyrocare.btechapp.models.data.TestSampleTypeModel;
import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.GPSTracker;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import java.util.ArrayList;
import java.util.HashSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.Alreadycontains10BenMsg;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.CHECK_INTERNET_CONN;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.CheckInternetConnectionMsg;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.PSAandFPSAforMaleMsg;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;
import static com.thyrocare.btechapp.utils.app.AppConstants.MSG_SERVER_EXCEPTION;
import static com.thyrocare.btechapp.utils.app.BundleConstants.HARDCOPY_CHARGES;

public class AddEditBenificaryActivity extends AppCompatActivity {

    private Activity mActivity;
    private Global globalclass;
    private ConnectionDetector cd;
    private GPSTracker gpsTracker;
    private AppPreferenceManager appPreferenceManager;

    private boolean FlagADDEditBen = true;
    LinearLayout ll_amt;
    TextView txtAmountPayable, btnclose, txtTestsList,txt_header_title,txtActPrice,txtHardCopyCharge;
    Button btnOrderSubmit;
    private EditText edtBenAge, edtBenName;
    private ImageView imgBenGenderF, imgBenGenderM, imgReportHC, imgBenAddTests;
    private boolean isM = false;
    private boolean isRHC = false;
    ArrayList<TestRateMasterModel> selectedTestsList = new ArrayList<>();
    private String SelectedTestCode = "";
    private int CallCartAPIFlag = 0;
    private boolean testListFlag;
    ArrayList<TestRateMasterModel> edit_selectedTestsList = new ArrayList<>();
    private int PSelected_position;
    private OrderBookingRequestModel FinalSubmitDataModel;
    private int AddBenCartFlag = 0;
    private int benId = 0;
    private ExpandableTestMasterListDisplayAdapter_new expAdapter;
    private DisplaySelectedTestsListForCancellationAdapter_new displayAdapter;
    private boolean isTestEdit = false;
    private Dialog CustomDialogforOTPValidation;
    private RemoveBeneficiaryAPIRequestModel removebenModel;

    private OrderVisitDetailsModel orderVisitDetailsModel;
    private BeneficiaryDetailsModel selectedbeneficiaryDetailsModel;
    private String orderNo;
    private RelativeLayout relHardCopyCharge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_benificary);
        initContext();
        iniView();
        initData();
        initListener();
    }

    private void initContext() {
        mActivity = AddEditBenificaryActivity.this;
        globalclass = new Global(mActivity);
        cd = new ConnectionDetector(mActivity);
        gpsTracker = new GPSTracker(mActivity);
        appPreferenceManager = new AppPreferenceManager(mActivity);
        orderVisitDetailsModel = getIntent().getExtras().getParcelable(BundleConstants.VISIT_ORDER_DETAILS_MODEL);
        selectedbeneficiaryDetailsModel = getIntent().getExtras().getParcelable(BundleConstants.BENEFICIARY_DETAILS_MODEL);
        FlagADDEditBen = getIntent().getBooleanExtra("IsAddBen",true);
        PSelected_position = getIntent().getIntExtra("SelectedBenPosition",0);
        if (!FlagADDEditBen){
            if (orderVisitDetailsModel == null || selectedbeneficiaryDetailsModel == null ){
                finish();
            }
        }else{
            if (orderVisitDetailsModel == null ){
                finish();
            }
        }
        orderNo = orderVisitDetailsModel.getVisitId();
    }

    private void iniView() {
        txt_header_title = (TextView) findViewById(R.id.txt_header_title);
        edtBenAge = (EditText) findViewById(R.id.edt_beneficiary_age);
        edtBenName = (EditText) findViewById(R.id.edt_beneficiary_name);
        txtTestsList = (TextView) findViewById(R.id.txt_beneficiary_tests);
        txtAmountPayable = (TextView) findViewById(R.id.txt_amount_payable);
        btnclose = (TextView) findViewById(R.id.btnclose);
        imgBenGenderF = (ImageView) findViewById(R.id.img_gender_female);
        imgBenGenderM = (ImageView) findViewById(R.id.img_gender_male);
        imgReportHC = (ImageView) findViewById(R.id.img_report_hc_tick);
        btnOrderSubmit = (Button) findViewById(R.id.btn_order_submit);
        imgBenAddTests = (ImageView) findViewById(R.id.img_beneficiary_action_edit_tests);
        ll_amt = (LinearLayout) findViewById(R.id.ll_amt);

        txtActPrice = (TextView) findViewById(R.id.txtActPrice);
        txtHardCopyCharge = (TextView) findViewById(R.id.txtHardCopyCharge);
        relHardCopyCharge = (RelativeLayout) findViewById(R.id.relHardCopyCharge);
    }

    private void initData() {

        ll_amt.setVisibility(View.GONE);
        txtAmountPayable.setVisibility(View.GONE);


        if (FlagADDEditBen) {
            benId = (int) (Math.random() * 999);
            if (selectedTestsList != null) {
                selectedTestsList = null;
            }
            selectedTestsList = new ArrayList<>();
        }


        edtBenName.setVisibility(View.VISIBLE);
        if (FlagADDEditBen) {
            txt_header_title.setText("ADD BENEFICIARY");
            isM = true;
            imgBenGenderM.setImageDrawable(getResources().getDrawable(R.drawable.male_icon_orange));
            imgBenGenderF.setImageDrawable(getResources().getDrawable(R.drawable.female));
            isRHC = true;
            imgReportHC.setImageDrawable(getResources().getDrawable(R.drawable.check_mark));

        } else {
            txt_header_title.setText("EDIT BENEFICIARY");
            edtBenName.setText(selectedbeneficiaryDetailsModel.getName());

            if (orderVisitDetailsModel.getAllOrderdetails().get(0).getReportHC() == 0) {
                relHardCopyCharge.setVisibility(View.GONE);
                isRHC = false;
                imgReportHC.setImageDrawable(getResources().getDrawable(R.drawable.tick_icon));
                txtActPrice.setText("Rs. " + orderVisitDetailsModel.getAllOrderdetails().get(0).getAmountDue() +"/-");
            } else {
                relHardCopyCharge.setVisibility(View.VISIBLE);
                int PriceWithoutHardCopy  = orderVisitDetailsModel.getAllOrderdetails().get(0).getAmountDue() - HARDCOPY_CHARGES;
                txtHardCopyCharge.setText("Rs. " + HARDCOPY_CHARGES  +"/-");
                txtActPrice.setText("Rs. " + PriceWithoutHardCopy  +"/-");
                isRHC = true;
                imgReportHC.setImageDrawable(getResources().getDrawable(R.drawable.check_mark));
            }


            ll_amt.setVisibility(View.VISIBLE);
            txtAmountPayable.setVisibility(View.VISIBLE);
            txtAmountPayable.setText("Rs. " + orderVisitDetailsModel.getAllOrderdetails().get(0).getAmountDue() +"/-");


            SelectedTestCode = selectedbeneficiaryDetailsModel.getTestsCode();
            txtTestsList.setError(null);
            txtTestsList.setText(SelectedTestCode.toString().trim());
            txtTestsList.setError(null);


            edtBenAge.setEnabled(true);
            edtBenAge.setError(null);
            edtBenAge.setText("" + selectedbeneficiaryDetailsModel.getAge());

            if (selectedbeneficiaryDetailsModel.getGender().toString().trim().equalsIgnoreCase("M")) {
                isM = true;
                imgBenGenderM.setImageDrawable(getResources().getDrawable(R.drawable.male_icon_orange));
                imgBenGenderF.setImageDrawable(getResources().getDrawable(R.drawable.female));
            } else if (selectedbeneficiaryDetailsModel.getGender().toString().trim().equalsIgnoreCase("F")) {
                isM = false;
                imgBenGenderF.setImageDrawable(getResources().getDrawable(R.drawable.female_icon_orange));
                imgBenGenderM.setImageDrawable(getResources().getDrawable(R.drawable.male));
            }
            CallAPIFORTESTLIST(FlagADDEditBen);
        }

    }

    private void initListener() {

        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });

        imgBenAddTests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (FlagADDEditBen) {
                    CallEditTestList(true);
                } else {
                    CallEditTestList(false);
                }
            }
        });

        imgBenGenderF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isM = false;
                imgBenGenderF.setImageDrawable(getResources().getDrawable(R.drawable.female_icon_orange));
                imgBenGenderM.setImageDrawable(getResources().getDrawable(R.drawable.male));

            }
        });

        imgBenGenderM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isM = true;
                imgBenGenderM.setImageDrawable(getResources().getDrawable(R.drawable.male_icon_orange));
                imgBenGenderF.setImageDrawable(getResources().getDrawable(R.drawable.female));
            }
        });

        imgReportHC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (FlagADDEditBen) {

                    if (isRHC) {
                        isRHC = false;
                        imgReportHC.setImageDrawable(getResources().getDrawable(R.drawable.tick_icon));
                    } else {

                        isRHC = true;
                        imgReportHC.setImageDrawable(getResources().getDrawable(R.drawable.check_mark));
                        globalclass.showalert_OK(mActivity.getResources().getString(R.string.hardcopycharges), mActivity);

                    }

                    if (selectedTestsList != null) {
                        if (selectedTestsList.size() != 0) {
                            CallCartAPIForAdd(selectedTestsList);
                        }
                    }
                } else {
                    if (isRHC) {
                        isRHC = false;
                        imgReportHC.setImageDrawable(getResources().getDrawable(R.drawable.tick_icon));
                    } else {
                        isRHC = true;
                        imgReportHC.setImageDrawable(getResources().getDrawable(R.drawable.check_mark));

                        globalclass.showalert_OK(mActivity.getResources().getString(R.string.hardcopycharges), mActivity);
                    }

                    CallCartAPIFlag = 1;

                    CallSubmitAPIforEditBen(  orderNo);

                }
            }
        });


        final int finalBenId = benId;
        btnOrderSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Bencount = orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().size();
                if (FlagADDEditBen) {
                    if (Bencount < 10) {
                        if (validateforAddben()) {
                            if (cd.isConnectingToInternet()){
                                CallsendOTPAPIforOrderEdit("Add",orderVisitDetailsModel, orderNo ,finalBenId);
                            }else{
                                globalclass.showCustomToast(mActivity,CHECK_INTERNET_CONN, Toast.LENGTH_LONG);
                            }
                        }
                    } else {
                        globalclass.showalert_OK(Alreadycontains10BenMsg, mActivity);
                    }
                } else {

                    if (validateforEditben()) {

                        if (CallCartAPIFlag == 1){
                            CallSubmitAPIforEditBen(orderNo);
                        }else{
                            if (cd.isConnectingToInternet()){
                                CallsendOTPAPIforOrderEdit("Edit",  orderVisitDetailsModel, orderNo, finalBenId);
                            }else{
                                globalclass.showCustomToast(mActivity,CHECK_INTERNET_CONN,Toast.LENGTH_LONG);
                            }
                        }

                    }
                }
            }
        });
    }

    private void CallsendOTPAPIforOrderEdit(final String Action,  final OrderVisitDetailsModel orderVisitDetailsModel, final String orderNo, final int finalBenId) {

        SendOTPRequestModel model = new SendOTPRequestModel();
        model.setMobile(orderVisitDetailsModel.getAllOrderdetails().get(0).getMobile());
        model.setOrderno(orderVisitDetailsModel.getAllOrderdetails().get(0).getVisitId());
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.DecodeString64(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<CommonResponseModel2> responseCall = apiInterface.CallSendOTPAPI(model);
        globalclass.showProgressDialog(mActivity, "Requesting for OTP. Please wait..");
        responseCall.enqueue(new Callback<CommonResponseModel2>() {
            @Override
            public void onResponse(Call<CommonResponseModel2> call, Response<CommonResponseModel2> response) {
                globalclass.hideProgressDialog(mActivity);
                if (response.isSuccessful() && response.body() != null) {
                    CommonResponseModel2 responseModel = response.body();
                    if (!TextUtils.isEmpty(responseModel.getRES_ID()) && responseModel.getRES_ID().equalsIgnoreCase("RES0000")) {
                        globalclass.showCustomToast(mActivity, "OTP send successfully to mobile number mapped to this order.");
                        ShowDialogToVerifyOTP(Action,orderVisitDetailsModel, orderNo ,finalBenId);
                    } else {
                        globalclass.showCustomToast(mActivity, "OTP Generation Failed.");
                    }
                } else {
                    globalclass.showCustomToast(mActivity, MSG_SERVER_EXCEPTION);
                }
            }

            @Override
            public void onFailure(Call<CommonResponseModel2> call, Throwable t) {
                globalclass.hideProgressDialog(mActivity);
                globalclass.showCustomToast(mActivity, MSG_SERVER_EXCEPTION);
            }
        });
    }

    private void ShowDialogToVerifyOTP(final String Action, final OrderVisitDetailsModel orderVisitDetailsModel, final String orderNo, final int finalBenId) {
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

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int height = 0,width = 0;
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
                    globalclass.showalert_OK("Please enter valid OTP. Length required : 4",mActivity);
                    edt_OTP.requestFocus();
                } else {
                    OrderPassRequestModel model = new OrderPassRequestModel();

                    model.setMobile(orderVisitDetailsModel.getAllOrderdetails().get(0).getMobile());
                    model.setOTP(strOTP);
                    model.setVisitId(orderVisitDetailsModel.getAllOrderdetails().get(0).getVisitId());
                    if (cd.isConnectingToInternet()) {
                        CallValidateOTPAPI(model,Action, orderNo ,finalBenId);
                    } else {
                        globalclass.showCustomToast(mActivity, mActivity.getResources().getString(R.string.plz_chk_internet));
                    }
                }

            }
        });

    }

    private void CallValidateOTPAPI(OrderPassRequestModel model, final String Action, final String orderNo, final int finalBenId) {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.DecodeString64(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallValidateOTPAPI(model);
        globalclass.showProgressDialog(mActivity, "Requesting for OTP. Please wait..");
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                globalclass.hideProgressDialog(mActivity);
                if (response.isSuccessful() && response.body() != null) {
                    String strresponse = response.body();
                    if (!TextUtils.isEmpty(strresponse) && strresponse.toUpperCase().contains("SUCCESS")) {
                        globalclass.showCustomToast(mActivity, "OTP Validated Successfully.");
                        if (!mActivity.isFinishing() && CustomDialogforOTPValidation != null && CustomDialogforOTPValidation.isShowing()){
                            CustomDialogforOTPValidation.dismiss();
                        }
                        if (Action.equalsIgnoreCase("Add")){
                            CallSubmitAPIforAddBen( orderNo, finalBenId);
                        }else if(Action.equalsIgnoreCase("Edit")) {
                            CallSubmitAPIforEditBen(  orderNo);
                        }
                    } else {
                        globalclass.showCustomToast(mActivity, "Invalid OTP.");
                    }
                } else if (response.code() == 401) {
                    globalclass.showCustomToast(mActivity, "Invalid OTP.");
                } else {
                    globalclass.showCustomToast(mActivity, MSG_SERVER_EXCEPTION);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                globalclass.hideProgressDialog(mActivity);
                globalclass.showCustomToast(mActivity, MSG_SERVER_EXCEPTION);
            }
        });

    }

    private void CallSubmitAPIforEditBen(String orderNo) {
        ArrayList<BeneficiaryDetailsModel> arrayListData = new ArrayList<>();
        String fName = edtBenName.getText().toString().trim();
        int fAge = Integer.parseInt(edtBenAge.getText().toString().trim());

        orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(PSelected_position).setName(fName);
        orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(PSelected_position).setAge(fAge);

        if (isM) {
            orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(PSelected_position).setGender("M");
        } else {
            orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(PSelected_position).setGender("F");
        }

        if (arrayListData != null) {
            arrayListData = null;
        }
        arrayListData = EditSetBenmaster();

        OrderBookingRequestModel nm = new OrderBookingRequestModel();

        //TODO -> Add data in orddtl
        ArrayList<OrderDetailsModel> orddtl = new ArrayList<OrderDetailsModel>();
        OrderDetailsModel odModel = new OrderDetailsModel();
        OrderDetailsModel ent_data = orderVisitDetailsModel.getAllOrderdetails().get(0);
        odModel.setAddress(ent_data.getAddress());
        odModel.setAmountDue(ent_data.getAmountDue());
        odModel.setBenCount(ent_data.getBenCount());
        odModel.setBillingCode(ent_data.getBillingCode());
        odModel.setBrandId(ent_data.getBrandId());
        odModel.setDelcode(ent_data.getDelcode());
        odModel.setDiscount(ent_data.getDiscount());
        odModel.setDistance(ent_data.getDistance());
        odModel.setEmail(ent_data.getEmail());
        odModel.setEstIncome(ent_data.getEstIncome());
        odModel.setLatitude(ent_data.getLatitude());
        odModel.setLongitude(ent_data.getLongitude());
        odModel.setMargin(ent_data.getMargin());
        odModel.setMobile(ent_data.getMobile());
        odModel.setOrderNo(orderNo);
        odModel.setPayType(ent_data.getPayType());
        odModel.setPincode(ent_data.getPincode());
        odModel.setRefcode(ent_data.getRefcode());
        odModel.setReportHC(isRHC ? 1 : 0);
        odModel.setSlot(ent_data.getSlot());
        odModel.setStatus(ent_data.getStatus());
        odModel.setVipOrder(ent_data.isVipOrder());
        odModel.setYNC(ent_data.getYNC());
        odModel.setKits(new ArrayList<KitsCountModel>());
        odModel.setOrdUpdateDetails(new OrderUpdateDetailsModel());
        odModel.setBenMaster(arrayListData);
        odModel.setAddBen(false);
        odModel.setTestEdit(true);

        orddtl.add(odModel);
        nm.setOrddtl(orddtl);

        //TODO -> End data in orddtl

        ArrayList<BeneficiaryBarcodeDetailsModel> benBarcodeArr = new ArrayList<>();
        ArrayList<BeneficiarySampleTypeDetailsModel> benSTArr = new ArrayList<>();
        ArrayList<BeneficiaryTestWiseClinicalHistoryModel> benCHArr = new ArrayList<>();
        //SET BENEFICIARY Lab Alerts Models Array - START
        ArrayList<BeneficiaryLabAlertsModel> benLAArr = new ArrayList<>();
        for (BeneficiaryDetailsModel beneficiaryDetailsModel :
                arrayListData) {
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

        OrderBookingDetailsModel ordbooking = new OrderBookingDetailsModel();
        ordbooking.setVisitId(orderNo);
        ordbooking.setOrddtl(orddtl);

        nm.setSmpldtl(benSTArr);
        nm.setLabAlert(benLAArr);
        nm.setClHistory(benCHArr);
        nm.setBendtl(arrayListData);
        nm.setBarcodedtl(benBarcodeArr);
        nm.setOrdbooking(ordbooking);

        CallSubMitAPI(nm);
    }

    private void CallSubmitAPIforAddBen(String orderNo, int finalBenId) {
        ArrayList<BeneficiaryDetailsModel> arrayListData = new ArrayList<>();
        edtBenAge.setError(null);
        edtBenName.setError(null);
        txtTestsList.setError(null);

        int fAge = Integer.parseInt(edtBenAge.getText().toString().trim());
        String fName = edtBenName.getText().toString().trim();

        if (arrayListData != null) {
            arrayListData = null;
        }

        arrayListData = SetBenmaster(fAge, fName, orderNo, isM, finalBenId);
        OrderBookingRequestModel nm = new OrderBookingRequestModel();

        //TODO -> Add data in orddtl
        ArrayList<OrderDetailsModel> orddtl = new ArrayList<OrderDetailsModel>();
        OrderDetailsModel odModel = new OrderDetailsModel();
        OrderDetailsModel ent_data = orderVisitDetailsModel.getAllOrderdetails().get(0);
        odModel.setAddress(ent_data.getAddress());
        odModel.setAmountDue(ent_data.getAmountDue());
        odModel.setBenCount(ent_data.getBenCount());
        odModel.setBillingCode(ent_data.getBillingCode());
        odModel.setBrandId(ent_data.getBrandId());
        odModel.setDelcode(ent_data.getDelcode());
        odModel.setDiscount(ent_data.getDiscount());
        odModel.setDistance(ent_data.getDistance());
        odModel.setEmail(ent_data.getEmail());
        odModel.setEstIncome(ent_data.getEstIncome());
        odModel.setLatitude(ent_data.getLatitude());
        odModel.setLongitude(ent_data.getLongitude());
        odModel.setMargin(ent_data.getMargin());
        odModel.setMobile(ent_data.getMobile());
        odModel.setOrderNo(orderNo);
        odModel.setPayType(ent_data.getPayType());
        odModel.setPincode(ent_data.getPincode());
        odModel.setRefcode(ent_data.getRefcode());
        odModel.setReportHC(isRHC ? 1 : 0);
        odModel.setSlot(ent_data.getSlot());
        odModel.setStatus(ent_data.getStatus());
        odModel.setVipOrder(ent_data.isVipOrder());
        odModel.setYNC(ent_data.getYNC());
        odModel.setKits(new ArrayList<KitsCountModel>());
        odModel.setOrdUpdateDetails(new OrderUpdateDetailsModel());
        odModel.setBenMaster(arrayListData);
        odModel.setAddBen(true);
        odModel.setTestEdit(true);

        orddtl.add(odModel);
        nm.setOrddtl(orddtl);

        //TODO -> End data in orddtl

        ArrayList<BeneficiaryBarcodeDetailsModel> benBarcodeArr = new ArrayList<>();
        ArrayList<BeneficiarySampleTypeDetailsModel> benSTArr = new ArrayList<>();
        ArrayList<BeneficiaryTestWiseClinicalHistoryModel> benCHArr = new ArrayList<>();
        //SET BENEFICIARY Lab Alerts Models Array - START
        ArrayList<BeneficiaryLabAlertsModel> benLAArr = new ArrayList<>();
        for (BeneficiaryDetailsModel beneficiaryDetailsModel :
                arrayListData) {
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

        OrderBookingDetailsModel ordbooking = new OrderBookingDetailsModel();
        ordbooking.setVisitId(orderNo);
        ordbooking.setOrddtl(orddtl);

        nm.setSmpldtl(benSTArr);
        nm.setLabAlert(benLAArr);
        nm.setClHistory(benCHArr);
        nm.setBendtl(arrayListData);
        nm.setBarcodedtl(benBarcodeArr);
        nm.setOrdbooking(ordbooking);

        CallSubMitAPI(nm);
    }

    private boolean validateforAddben() {
        String str = "";
        for (int i = 0; i < selectedTestsList.size(); i++) {
            if (selectedTestsList.get(i).getTestType().equalsIgnoreCase("OFFER")) {
                str = str + selectedTestsList.get(i).getDescription() + ",";
            } else {
                str = str + selectedTestsList.get(i).getTestCode() + ",";
            }
        }
        MessageLogger.PrintMsg(" Selected Test : " + str);
        //Todo tejas t -----------

        if (StringUtils.isNull(edtBenAge.getText().toString().trim())) {
            edtBenAge.setError("Age is Required");
            return false;
        } else if (Integer.parseInt(edtBenAge.getText().toString().trim()) < 1 || Integer.parseInt(edtBenAge.getText().toString().trim()) > 120) {
            edtBenAge.setError("Age should be between 1 to 120");
            return false;
        } else if (StringUtils.isNull(edtBenName.getText().toString().trim())) {
            edtBenName.setError("Name is Required");
            return false;
        }else if (!StringUtils.isNull(edtBenName.getText().toString().trim()) && edtBenName.getText().toString().trim().length() < 2) {
            edtBenName.setError("Name should have minimum 2 characters");
            return false;
        } else if (StringUtils.isNull(txtTestsList.getText().toString().trim())) {
            txtTestsList.setError("Tests List is Required");
            return false;
        } else if ((str.contains("PSA") || str.contains("FPSA")) && !isM) {
            txtTestsList.setError(PSAandFPSAforMaleMsg);
            globalclass.showalert_OK(PSAandFPSAforMaleMsg, mActivity);
            return false;
        }
        return true;
    }

    private boolean validateforEditben() {
        //Todo tejas t -----------
        String str = "";
        for (int i = 0; i < edit_selectedTestsList.size(); i++) {
            if (edit_selectedTestsList.get(i).getTestType().equalsIgnoreCase("OFFER")) {
                str = str + edit_selectedTestsList.get(i).getDescription() + ",";
            } else {
                str = str + edit_selectedTestsList.get(i).getTestCode() + ",";
            }
        }
        MessageLogger.PrintMsg(" Selected Test : " + str);
        //Todo tejas t -----------

        if (StringUtils.isNull(edtBenName.getText().toString().trim())) {
            edtBenName.setError("Name is Required");
            return false;
        }else if (!StringUtils.isNull(edtBenName.getText().toString().trim()) && edtBenName.getText().toString().trim().length() < 2) {
            edtBenName.setError("Name should have minimum 2 characters");
            return false;
        }else if (StringUtils.isNull(edtBenAge.getText().toString().trim())) {
            edtBenAge.setError("Age is Required");
            return false;
        } else if (Integer.parseInt(edtBenAge.getText().toString().trim()) < 1 || Integer.parseInt(edtBenAge.getText().toString().trim()) > 120) {
            edtBenAge.setError("Age should be between 1 to 120");
            return false;
        } else if (StringUtils.isNull(txtTestsList.getText().toString().trim())) {
            txtTestsList.setError("Tests List is Required");
            return false;
            //Todo tejas t -----------
        } else if ((str.contains("PSA") || str.contains("FPSA")) && !isM) {
            txtTestsList.setError(PSAandFPSAforMaleMsg);
            globalclass.showalert_OK(PSAandFPSAforMaleMsg, mActivity);
            //Todo tejas t -----------
            return false;
        }
        return true;
    }

    private ArrayList<BeneficiaryDetailsModel> SetBenmaster(int fAge, String name, String orderNo, boolean isM, int benId) {
        ArrayList<BeneficiaryDetailsModel> entity;
        entity = new ArrayList<BeneficiaryDetailsModel>();

        BeneficiaryDetailsModel benmsdvbh = null;
        try {

            if (orderVisitDetailsModel.getAllOrderdetails() != null) {
                if (orderVisitDetailsModel.getAllOrderdetails().size() != 0) {
                    orderVisitDetailsModel.getAllOrderdetails().get(0).setReportHC(isRHC ? 1 : 0);
                    if (orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster() != null) {
                        if (orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().size() != 0) {
                            for (int q = 0; q < orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().size(); q++) {
                                if (orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(q).getTestsCode() != null) {
                                    orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(q).setTests(orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(q).getTestsCode());
                                }
                            }
                        }
                    }
                }
                if (orderVisitDetailsModel.getAllOrderdetails().size() != 0) {
                    entity = orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster();
                }
            }


            benmsdvbh = new BeneficiaryDetailsModel();
            benmsdvbh.setOrderNo(orderNo);

            String strfasting = "Non-Fasting";
            for (int i = 0; i < selectedTestsList.size(); i++) {
                if (selectedTestsList.get(i).getFasting().equalsIgnoreCase("Fasting")) {
                    strfasting = "Fasting";
                }
            }
            benmsdvbh.setFasting(strfasting);

            benmsdvbh.setProjId("");
            benmsdvbh.setAddBen(true);
            benmsdvbh.setAge(fAge);
            benmsdvbh.setName(name);
            benmsdvbh.setBenId(benId);

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
                if (!StringUtils.isNull(trmm.getTestType()) && trmm.getTestType().equalsIgnoreCase("offer")) {
                    btdm.setProjId(trmm.getTestCode());
                    btdm.setTests(trmm.getDescription());
                    benmsdvbh.setProjId(trmm.getTestCode());
                }
                selectedTestDetailsArr.add(btdm);
            }
            benmsdvbh.setTestSampleType(selectedTestDetailsArr);

            ArrayList<BeneficiarySampleTypeDetailsModel> samples = new ArrayList<>();
            for (TestRateMasterModel trmm :
                    selectedTestsList) {
                for (TestSampleTypeModel tstm :
                        trmm.getSampltype()) {
                    BeneficiarySampleTypeDetailsModel bstdm = new BeneficiarySampleTypeDetailsModel();
                    bstdm.setBenId(benmsdvbh.getBenId());
                    bstdm.setSampleType(tstm.getSampleType());
                    bstdm.setId(tstm.getId());
                    if (!samples.contains(bstdm)) {
                        samples.add(bstdm);
                    }
                }
            }
            benmsdvbh.setSampleType(samples);

            if (isM) {
                benmsdvbh.setGender("M");
            } else {
                benmsdvbh.setGender("F");
            }

            String testsCode = "";
            if (selectedTestsList != null) {
                for (TestRateMasterModel testRateMasterModel :
                        selectedTestsList) {
                    if (StringUtils.isNull(testsCode)) {
                        if (!StringUtils.isNull(testRateMasterModel.getTestType()) && testRateMasterModel.getTestType().equalsIgnoreCase("OFFER")) {
                            testsCode = testRateMasterModel.getDescription();
                            benmsdvbh.setProjId(testRateMasterModel.getTestCode());
                        } else {
                            testsCode = testRateMasterModel.getTestCode();
                        }
                    } else {
                        if (!StringUtils.isNull(testRateMasterModel.getTestType()) && testRateMasterModel.getTestType().equalsIgnoreCase("OFFER")) {
                            testsCode = testsCode + "," + testRateMasterModel.getDescription();
                            benmsdvbh.setProjId(testRateMasterModel.getTestCode());
                        } else {
                            testsCode = testsCode + "," + testRateMasterModel.getTestCode();

                        }
                    }
                }
            }
            benmsdvbh.setTestsCode(testsCode);
            benmsdvbh.setTests(testsCode);
        } catch (Exception e) {
            e.printStackTrace();
        }


        entity.add(benmsdvbh);


        return entity;
    }

    private ArrayList<BeneficiaryDetailsModel> EditSetBenmaster() {
        ArrayList<BeneficiaryDetailsModel> entity;
        entity = new ArrayList<BeneficiaryDetailsModel>();

        try {
            if (orderVisitDetailsModel.getAllOrderdetails() != null) {

                for (int q = 0; q < orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().size(); q++) {
                    if (orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(q).getTestSampleType().size() != 0) {
                        if (orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(q).getTestSampleType().size() != 0) {
                            String tescd = "";
                            for (int x = 0; x < orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(q).getTestSampleType().size(); x++) {
                                tescd = tescd + "" + orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(q).getTestSampleType().get(x).getTests() + ",";
                            }
                            tescd = tescd.substring(0, tescd.length() - 1);
                            orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(q).setTests(tescd);
                            orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(q).setTestsCode(tescd);
                        }
                    }
                }

                if (orderVisitDetailsModel.getAllOrderdetails().size() != 0) {
                    entity = orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return entity;
    }

    private void CallAPIFORTESTLIST(boolean b_flag) {
        testListFlag = b_flag;
        if (cd.isConnectingToInternet()) {
            CallGetTechsoProductsAPI();
        } else {
            globalclass.showCustomToast(mActivity, CheckInternetConnectionMsg);
        }
    }

    private void CallGetTechsoProductsAPI() {

        try {
            GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.DecodeString64(getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
            Call<BrandTestMasterModel> responseCall = apiInterface.CallGetTechsoPRoductsAPI("Bearer " + appPreferenceManager.getLoginResponseModel().getAccess_token());
            globalclass.showProgressDialog(mActivity, "Fetching products. Please wait..");
            responseCall.enqueue(new Callback<BrandTestMasterModel>() {
                @Override
                public void onResponse(Call<BrandTestMasterModel> call, retrofit2.Response<BrandTestMasterModel> response) {
                    globalclass.hideProgressDialog(mActivity);

                    if (response.isSuccessful() && response.body() != null) {
                        BrandTestMasterModel brandTestMasterModel = new BrandTestMasterModel();
                        brandTestMasterModel = getBrandTestMaster(response.body());
                        CallTestData(brandTestMasterModel);

                    } else {
                        globalclass.showcenterCustomToast(mActivity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
                    }
                }

                @Override
                public void onFailure(Call<BrandTestMasterModel> call, Throwable t) {
                    globalclass.hideProgressDialog(mActivity);
                    globalclass.showcenterCustomToast(mActivity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BrandTestMasterModel getBrandTestMaster(BrandTestMasterModel brandTestMasterModel) {
        Gson gson = new Gson();

        BrandTestMasterModel brandTestMasterModelFinal = new BrandTestMasterModel();
        ArrayList<TestRateMasterModel> tstratemaster = new ArrayList<>();

        if (brandTestMasterModel != null && brandTestMasterModel.getTstratemaster() != null && brandTestMasterModel.getTstratemaster().size() > 0) {
            for (int i = 0; i < brandTestMasterModel.getTstratemaster().size(); i++) {
                if (brandTestMasterModel.getTstratemaster().get(i).getAccessUserCode() != null && brandTestMasterModel.getTstratemaster().get(i).getAccessUserCode().size() > 0) {
                    for (int j = 0; j < brandTestMasterModel.getTstratemaster().get(i).getAccessUserCode().size(); j++) {
                        try {
                            if (Integer.parseInt(brandTestMasterModel.getTstratemaster().get(i).getAccessUserCode().get(j).getAccessCode()) == orderVisitDetailsModel.getAllOrderdetails().get(0).getUserAccessCode()) {
                                tstratemaster.add(brandTestMasterModel.getTstratemaster().get(i));
                                break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }


        brandTestMasterModelFinal.setTstratemaster(tstratemaster);
        brandTestMasterModelFinal.setBrandId(brandTestMasterModel.getBrandId());
        brandTestMasterModelFinal.setBrandName(brandTestMasterModel.getBrandName());

        return brandTestMasterModelFinal;
    }


    private void CallTestData(BrandTestMasterModel result) {

        String[] str = SelectedTestCode.split(",");

        if (edit_selectedTestsList != null) {
            edit_selectedTestsList = null;
        }

        edit_selectedTestsList = new ArrayList<>();

        for (int i = 0; i < str.length; i++) {
            for (int j = 0; j < result.getTstratemaster().size(); j++) {

                if (result.getTstratemaster().get(j).getTestType().trim().equalsIgnoreCase("OFFER")) {
                    if (str[i].trim().equalsIgnoreCase(result.getTstratemaster().get(j).getDescription().trim())) {
                        edit_selectedTestsList.add(result.getTstratemaster().get(j));
                    }
                } else {
                    if (str[i].trim().equalsIgnoreCase(result.getTstratemaster().get(j).getTestCode().trim())) {
                        edit_selectedTestsList.add(result.getTstratemaster().get(j));
                    }
                }
            }
        }
    }

    private void CallEditTestList(final boolean isAddBen) {
        final Dialog editdialog = new Dialog(mActivity);
        editdialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        editdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        editdialog.setContentView(R.layout.dailog_edit_order1);
        ListView lvTestsDisplay = (ListView) editdialog.findViewById(R.id.test_names);
        TextView close_btn = (TextView) editdialog.findViewById(R.id.close_btn);

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editdialog.dismiss();
            }
        });

        try {
            if (isAddBen) {
                displayAdapter = new DisplaySelectedTestsListForCancellationAdapter_new(mActivity, selectedTestsList, new RemoveSelectedTestFromListDelegate_new() {
                    @Override
                    public void onRemoveButtonClicked(ArrayList<TestRateMasterModel> selectedTests) {
                        isTestEdit = true;
                        selectedTestsList = selectedTests;
                        displayAdapter.notifyDataSetChanged();
                    }
                });
                lvTestsDisplay.setAdapter(displayAdapter);
            } else {

                if (displayAdapter != null) {
                    displayAdapter = null;
                }

                displayAdapter = new DisplaySelectedTestsListForCancellationAdapter_new(mActivity, edit_selectedTestsList, new RemoveSelectedTestFromListDelegate_new() {
                    @Override
                    public void onRemoveButtonClicked(ArrayList<TestRateMasterModel> selectedTests) {
                        isTestEdit = true;
                        edit_selectedTestsList = selectedTests;
                        displayAdapter.notifyDataSetChanged();
                    }
                });
                lvTestsDisplay.setAdapter(displayAdapter);

            }
            Button btn_Save = (Button) editdialog.findViewById(R.id.btn_save);
            if (isAddBen) {
                if (selectedTestsList.size() != 0) {
                    btn_Save.setVisibility(View.VISIBLE);
                } else {
                    btn_Save.setVisibility(View.GONE);
                }
            } else {
                if (edit_selectedTestsList.size() != 0) {
                    btn_Save.setVisibility(View.VISIBLE);
                } else {
                    btn_Save.setVisibility(View.GONE);
                }
            }

            btn_Save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    editdialog.dismiss();
                    if (isAddBen) {
                        if (selectedTestsList.size() != 0) {

                            int FastingCount = 0;
                            boolean isFBSpresent = false;
                            boolean isPPBSpresent = false;
                            boolean isRBSpresent = false;
                            boolean isFBSpresentInSuperSet = false;
                            boolean isINSFApresent = false;
                            boolean isINSFApresentInSuperSet = false;
                            boolean isINSPPpresent = false;

                            if (selectedTestsList != null && selectedTestsList.size() > 0) {
                                ArrayList<TestRateMasterModel> trmmArr = new ArrayList<TestRateMasterModel>();
                                for (TestRateMasterModel btdm :
                                        selectedTestsList) {
                                    MessageLogger.PrintMsg("test:" + btdm.getTestCode());

                                    if (btdm.getTestCode().equalsIgnoreCase(Constants.FBS)) {
                                        isFBSpresent = true;
                                    }
                                    if (btdm.getTestCode().equalsIgnoreCase(Constants.INSFA)) {
                                        isINSFApresent = true;
                                    }

                                    if (btdm.getChldtests() != null) {
                                        if (btdm.getChldtests().size() != 0) {
                                            for (int i = 0; i < btdm.getChldtests().size(); i++) {
                                                if (btdm.getChldtests().get(i).getChildTestCode() != null) {

                                                    if (btdm.getChldtests().get(i).getChildTestCode().toString().trim().equalsIgnoreCase(Constants.FBS)) {
                                                        isFBSpresent = true;
                                                        isFBSpresentInSuperSet = true;
                                                    }
                                                    if (btdm.getChldtests().get(i).getChildTestCode().toString().trim().equalsIgnoreCase(Constants.INSFA)) {
                                                        isINSFApresent = true;
                                                        isINSFApresentInSuperSet = true;
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    if (btdm.getTestCode().equalsIgnoreCase(Constants.INSPP)) {
                                        isINSPPpresent = true;

                                    }
                                    if (btdm.getTestCode().equalsIgnoreCase(Constants.PPBS)) {
                                        isPPBSpresent = true;

                                    }
                                    if (btdm.getTestCode().equalsIgnoreCase(Constants.RBS)) {
                                        isRBSpresent = true;

                                    }
                                    if (btdm.getFasting().equalsIgnoreCase("Fasting")) {
                                        FastingCount = FastingCount + 1;
                                        Logger.error("FastingCount " + FastingCount);
                                    }

                                }

                                MessageLogger.PrintMsg("isFBSpresent: " + isFBSpresent);
                                MessageLogger.PrintMsg("FastingCount: " + FastingCount);


                                if (isFBSpresent == true && FastingCount == 1 && !isFBSpresentInSuperSet) {

                                    Toast.makeText(mActivity, "With FBS atleast one fasting test should be there", Toast.LENGTH_SHORT).show();
                                } else if (isPPBSpresent && !isFBSpresent) {
                                    Toast.makeText(mActivity, "To Avail PPBS You have to select FBS test", Toast.LENGTH_SHORT).show();
                                } else if (isRBSpresent && !isFBSpresent) {
                                    Toast.makeText(mActivity, "To Avail RBS You have to select FBS test", Toast.LENGTH_SHORT).show();
                                } else if (isINSPPpresent && !isINSFApresent) {
                                    Toast.makeText(mActivity, "To Avail INSPP You have to select INSFA test", Toast.LENGTH_SHORT).show();
                                } else {

                                    editdialog.dismiss();
                                    String str = "";
                                    for (int i = 0; i < selectedTestsList.size(); i++) {

                                        if (selectedTestsList.get(i).getTestType().equalsIgnoreCase("OFFER")) {
                                            str = str + selectedTestsList.get(i).getDescription() + ",";
                                        } else {
                                            str = str + selectedTestsList.get(i).getTestCode() + ",";
                                        }
//                                str = str + selectedTestsList.get(i).getDescription() + ",";
                                    }
                                    str = str.substring(0, str.length() - 1);
                                    txtTestsList.setError(null);
                                    txtTestsList.setText("" + str);
                                    txtTestsList.setError(null);
                                    CallCartAPIForAdd(selectedTestsList);
                                }

                            } else {

                                globalclass.showCustomToast(mActivity, "Please select atleast one test");

                            }


                        }
                    } else {
                        if (edit_selectedTestsList.size() != 0) {


                            int FastingCount = 0;
                            boolean isFBSpresent = false;
                            boolean isPPBSpresent = false;
                            boolean isRBSpresent = false;
                            boolean isFBSpresentInSuperSet = false;
                            boolean isINSFApresent = false;
                            boolean isINSFApresentInSuperSet = false;
                            boolean isINSPPpresent = false;

                            if (edit_selectedTestsList != null && edit_selectedTestsList.size() > 0) {
                                ArrayList<TestRateMasterModel> trmmArr = new ArrayList<TestRateMasterModel>();
                                for (TestRateMasterModel btdm :
                                        edit_selectedTestsList) {
                                    MessageLogger.PrintMsg("test:" + btdm.getTestCode());

                                    if (btdm.getTestCode().equalsIgnoreCase(Constants.FBS)) {
                                        isFBSpresent = true;
                                    }
                                    if (btdm.getTestCode().equalsIgnoreCase(Constants.INSFA)) {
                                        isINSFApresent = true;
                                    }

                                    if (btdm.getChldtests() != null) {
                                        if (btdm.getChldtests().size() != 0) {
                                            for (int i = 0; i < btdm.getChldtests().size(); i++) {
                                                if (btdm.getChldtests().get(i).getChildTestCode() != null) {

                                                    if (btdm.getChldtests().get(i).getChildTestCode().toString().trim().equalsIgnoreCase(Constants.FBS)) {
                                                        isFBSpresent = true;
                                                        isFBSpresentInSuperSet = true;
                                                    }
                                                    if (btdm.getChldtests().get(i).getChildTestCode().toString().trim().equalsIgnoreCase(Constants.INSFA)) {
                                                        isINSFApresent = true;
                                                        isINSFApresentInSuperSet = true;
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    if (btdm.getTestCode().equalsIgnoreCase(Constants.INSPP)) {
                                        isINSPPpresent = true;

                                    }
                                    if (btdm.getTestCode().equalsIgnoreCase(Constants.PPBS)) {
                                        isPPBSpresent = true;

                                    }
                                    if (btdm.getTestCode().equalsIgnoreCase(Constants.RBS)) {
                                        isRBSpresent = true;

                                    }
                                    if (btdm.getFasting().equalsIgnoreCase("Fasting")) {
                                        FastingCount = FastingCount + 1;
                                        Logger.error("FastingCount " + FastingCount);
                                    }

                                }

                                MessageLogger.PrintMsg("isFBSpresent: " + isFBSpresent);
                                MessageLogger.PrintMsg("FastingCount: " + FastingCount);


                                if (isFBSpresent == true && FastingCount == 1 && !isFBSpresentInSuperSet) {

                                    Toast.makeText(mActivity, "With FBS atleast one fasting test should be there", Toast.LENGTH_SHORT).show();
                                } else if (isPPBSpresent && !isFBSpresent) {
                                    Toast.makeText(mActivity, "To Avail PPBS You have to select FBS test", Toast.LENGTH_SHORT).show();
                                } else if (isRBSpresent && !isFBSpresent) {
                                    Toast.makeText(mActivity, "To Avail RBS You have to select FBS test", Toast.LENGTH_SHORT).show();
                                } else if (isINSPPpresent && !isINSFApresent) {
                                    Toast.makeText(mActivity, "To Avail INSPP You have to select INSFA test", Toast.LENGTH_SHORT).show();
                                } else {

                                    editdialog.dismiss();

                                    String str = "";
                                    for (int i = 0; i < edit_selectedTestsList.size(); i++) {

                                        if (edit_selectedTestsList.get(i).getTestType().equalsIgnoreCase("OFFER")) {
                                            str = str + edit_selectedTestsList.get(i).getDescription() + ",";
                                        } else {
                                            str = str + edit_selectedTestsList.get(i).getTestCode() + ",";
                                        }
                                    }
                                    str = str.substring(0, str.length() - 1);
                                    txtTestsList.setText("" + str);
                                    txtTestsList.setError(null);

                                    String testsCode = "";
                                    ArrayList<BeneficiarySampleTypeDetailsModel> samples = new ArrayList<>();
                                    ArrayList<BeneficiaryTestDetailsModel> selectedTestDetailsArr = new ArrayList<BeneficiaryTestDetailsModel>();
                                    for (TestRateMasterModel trmm :
                                            edit_selectedTestsList) {
                                        BeneficiaryTestDetailsModel btdm = new BeneficiaryTestDetailsModel();
                                        btdm.setFasting(trmm.getFasting());
                                        btdm.setChldtests(trmm.getChldtests() != null ? trmm.getChldtests() : new ArrayList<ChildTestsModel>());
                                        btdm.setTests(trmm.getTestCode());
                                        btdm.setTestType(trmm.getTestType());
                                        btdm.setProjId("");
                                        btdm.setSampleType(trmm.getSampltype() != null ? trmm.getSampltype() : new ArrayList<TestSampleTypeModel>());
                                        btdm.setTstClinicalHistory(trmm.getTstClinicalHistory() != null ? trmm.getTstClinicalHistory() : new ArrayList<TestClinicalHistoryModel>());
                                        if (!StringUtils.isNull(trmm.getTestType()) && trmm.getTestType().equalsIgnoreCase("offer")) {
                                            btdm.setProjId(trmm.getTestCode());
                                            btdm.setTests(trmm.getDescription());
                                            orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(PSelected_position).setProjId(trmm.getTestCode());
                                        }
                                        selectedTestDetailsArr.add(btdm);


                                        if (StringUtils.isNull(testsCode)) {
                                            if (!StringUtils.isNull(trmm.getTestType()) && trmm.getTestType().equalsIgnoreCase("OFFER")) {
                                                testsCode = trmm.getDescription();
                                                orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(PSelected_position).setProjId(trmm.getTestCode());
                                            } else {
                                                testsCode = trmm.getTestCode();
                                            }
                                        } else {
                                            if (!StringUtils.isNull(trmm.getTestType()) && trmm.getTestType().equalsIgnoreCase("OFFER")) {
                                                testsCode = testsCode + "," + trmm.getDescription();
                                                orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(PSelected_position).setProjId(trmm.getTestCode());
                                            } else {
                                                testsCode = testsCode + "," + trmm.getTestCode();

                                            }
                                        }

                                        for (TestSampleTypeModel tstm :
                                                trmm.getSampltype()) {
                                            BeneficiarySampleTypeDetailsModel bstdm = new BeneficiarySampleTypeDetailsModel();
                                            bstdm.setBenId(orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(PSelected_position).getBenId());
                                            bstdm.setSampleType(tstm.getSampleType());
                                            bstdm.setId(tstm.getId());
                                            if (!samples.contains(bstdm)) {
                                                samples.add(bstdm);
                                            }
                                        }
                                    }
                                    orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(PSelected_position).setTestEdit(true);
                                    orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(PSelected_position).setTestSampleType(selectedTestDetailsArr);
                                    orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(PSelected_position).setTestsCode(testsCode);
                                    orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(PSelected_position).setTests(testsCode);
                                    orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(PSelected_position).setSampleType(samples);

                                    CallCartAPIFlag = 1;
                                    btnOrderSubmit.performClick();
                                }

                            } else {

                                globalclass.showCustomToast(mActivity, "Please select atleast one test");

                            }


                        }
                    }
                }
            });

            Button btn_addtest = (Button) editdialog.findViewById(R.id.btn_addtest);
            btn_addtest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editdialog.dismiss();
                    Intent intent = new Intent(mActivity, AddRemoveTestProfileActivity.class);
                    intent.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL, orderVisitDetailsModel);
                    intent.putParcelableArrayListExtra(BundleConstants.ADD_BEN_SELECTED_TESTLIST, selectedTestsList);
                    intent.putParcelableArrayListExtra(BundleConstants.EDIT_BEN_SELECTED_TESTLIST, edit_selectedTestsList);
                    intent.putExtra("IsAddBen", FlagADDEditBen);
                    startActivityForResult(intent, BundleConstants.ADDEDITTESTREQUESTCODE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        editdialog.show();
    }

    static ArrayList<String> removeDuplicates(ArrayList<String> list) {

        // Store unique items in result.
        ArrayList<String> result = new ArrayList<>();

        // Record encountered Strings in HashSet.
        HashSet<String> set = new HashSet<>();

        // Loop over argument list.
        for (String item : list) {

            // If String is not in set, add it to the list and the set.
            if (!set.contains(item)) {
                result.add(item);
                set.add(item);
            }
        }
        return result;
    }

    private void CallCartAPIForAdd(ArrayList<TestRateMasterModel> selectedTests_List) {

        AddBenCartFlag = 1;

        CartAPIRequestModel cartAPIRequestModel = new CartAPIRequestModel();
        ArrayList<CartRequestOrderModel> ordersArr = new ArrayList<>();
        ArrayList<CartRequestBeneficiaryModel> beneficiariesArr = new ArrayList<>();
        cartAPIRequestModel.setVisitId(orderVisitDetailsModel.getVisitId());

        CartRequestOrderModel crom = new CartRequestOrderModel();
        crom.setOrderNo(orderVisitDetailsModel.getVisitId());
        crom.setHC(isRHC ? 1 : 0);
        crom.setBrandId("" + orderVisitDetailsModel.getAllOrderdetails().get(0).getBrandId());
        ordersArr.add(crom);

        for (int j = 0; j < orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().size(); j++) {
            CartRequestBeneficiaryModel crbm = new CartRequestBeneficiaryModel();

            crbm.setOrderNo(orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(j).getOrderNo());
            crbm.setAddben(orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(j).isAddBen());
            crbm.setTestEdit(orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(j).isTestEdit());
//            crbm.setTests(selectedTestsList.getOrdbooking().getOrddtl().get(0).getBenMaster().get(j).getTestsCode());


            if (!StringUtils.isNull(orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(j).getProjId())) {
                crbm.setTests(orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(j).getProjId() + "," + orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(j).getTestsCode());
                crbm.setProjId(orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(j).getProjId());
                crbm.setBenId(String.valueOf(orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(j).getBenId()));
            } else {
                crbm.setTests(orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(j).getTestsCode());
                crbm.setBenId(String.valueOf(orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(j).getBenId()));
            }


            beneficiariesArr.add(crbm);
        }

        if (selectedTests_List != null) {
            if (selectedTests_List.size() != 0) {
                String testsCode = "";
                CartRequestBeneficiaryModel cr = new CartRequestBeneficiaryModel();
                cr.setOrderNo(orderVisitDetailsModel.getAllOrderdetails().get(0).getOrderNo());
                cr.setAddben(true);
                cr.setTestEdit(true);
                cr.setBenId("" + benId);
                cr.setProjId("");


                for (int m = 0; m < selectedTests_List.size(); m++) {
                    if (selectedTests_List.get(m).getTestType().toString().toString().equalsIgnoreCase("OFFER")) {
                        cr.setProjId(selectedTests_List.get(m).getTestCode());
                    }


                    if (StringUtils.isNull(testsCode)) {
                        if (!StringUtils.isNull(selectedTests_List.get(m).getTestType()) && selectedTests_List.get(m).getTestType().equalsIgnoreCase("OFFER")) {
                            testsCode = selectedTests_List.get(m).getDescription();
                        } else {
                            testsCode = selectedTests_List.get(m).getTestCode();
                        }
                    } else {
                        if (!StringUtils.isNull(selectedTests_List.get(m).getTestType()) && selectedTests_List.get(m).getTestType().equalsIgnoreCase("OFFER")) {
                            testsCode = testsCode + "," + selectedTests_List.get(m).getDescription();
                        } else {
                            testsCode = testsCode + "," + selectedTests_List.get(m).getTestCode();

                        }
                    }
                }
                cr.setTests(testsCode);

                beneficiariesArr.add(cr);
            }
        }


        cartAPIRequestModel.setOrders(ordersArr);
        cartAPIRequestModel.setBeneficiaries(beneficiariesArr);

        //CAllAPIforCart
        if (cd.isConnectingToInternet()) {
            CallTechsoCartAPI(cartAPIRequestModel);
        } else {
            globalclass.showCustomToast(mActivity, CheckInternetConnectionMsg);
        }
    }

    private void CallCartAPI(OrderBookingRequestModel selectedTestsList) {
        CartAPIRequestModel cartAPIRequestModel = new CartAPIRequestModel();
        ArrayList<CartRequestOrderModel> ordersArr = new ArrayList<>();
        ArrayList<CartRequestBeneficiaryModel> beneficiariesArr = new ArrayList<>();
        cartAPIRequestModel.setVisitId(selectedTestsList.getOrdbooking().getVisitId());


        CartRequestOrderModel crom = new CartRequestOrderModel();
        crom.setOrderNo(selectedTestsList.getOrdbooking().getVisitId());
        crom.setHC(isRHC ? 1 : 0);
        crom.setBrandId("" + selectedTestsList.getOrdbooking().getOrddtl().get(0).getBrandId());
        ordersArr.add(crom);

        for (int j = 0; j < selectedTestsList.getOrdbooking().getOrddtl().get(0).getBenMaster().size(); j++) {
            CartRequestBeneficiaryModel crbm = new CartRequestBeneficiaryModel();

            //  crbm.setOrderNo(selectedTestsList.getOrdbooking().getOrddtl().get(0).getBenMaster().get(j).getOrderNo());
            crbm.setOrderNo(selectedTestsList.getOrdbooking().getVisitId());//TODO tejas t---
            crbm.setAddben(selectedTestsList.getOrdbooking().getOrddtl().get(0).getBenMaster().get(j).isAddBen());
            crbm.setTestEdit(selectedTestsList.getOrdbooking().getOrddtl().get(0).getBenMaster().get(j).isTestEdit());
//            crbm.setTests(selectedTestsList.getOrdbooking().getOrddtl().get(0).getBenMaster().get(j).getTestsCode());


            if (!StringUtils.isNull(selectedTestsList.getOrdbooking().getOrddtl().get(0).getBenMaster().get(j).getProjId())) {
                crbm.setTests(selectedTestsList.getOrdbooking().getOrddtl().get(0).getBenMaster().get(j).getProjId() + "," + selectedTestsList.getOrdbooking().getOrddtl().get(0).getBenMaster().get(j).getTestsCode());
                crbm.setProjId(selectedTestsList.getOrdbooking().getOrddtl().get(0).getBenMaster().get(j).getProjId());
                crbm.setBenId(String.valueOf(selectedTestsList.getOrdbooking().getOrddtl().get(0).getBenMaster().get(j).getBenId()));
            } else {
                crbm.setTests(selectedTestsList.getOrdbooking().getOrddtl().get(0).getBenMaster().get(j).getTestsCode());
                crbm.setBenId(String.valueOf(selectedTestsList.getOrdbooking().getOrddtl().get(0).getBenMaster().get(j).getBenId()));
            }


            beneficiariesArr.add(crbm);
        }


        cartAPIRequestModel.setOrders(ordersArr);
        cartAPIRequestModel.setBeneficiaries(beneficiariesArr);

        //CAllAPIforCart
        if (cd.isConnectingToInternet()) {
            CallTechsoCartAPI(cartAPIRequestModel);

        } else {
            globalclass.showCustomToast(mActivity, CheckInternetConnectionMsg);
        }
    }

    private void CallTechsoCartAPI(CartAPIRequestModel cartAPIRequestModel) {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.DecodeString64(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<CartAPIResponseModel> responseCall = apiInterface.CallTechsoCartAPI("Bearer "+ appPreferenceManager.getLoginResponseModel().getAccess_token(), cartAPIRequestModel);
        globalclass.showProgressDialog(mActivity, "Please wait..");
        responseCall.enqueue(new Callback<CartAPIResponseModel>() {
            @Override
            public void onResponse(Call<CartAPIResponseModel> call, retrofit2.Response<CartAPIResponseModel> response) {
                globalclass.hideProgressDialog(mActivity);
                if (response.isSuccessful() && response.body() != null) {
                    CartAPIResponseModel cartAPIResponseModel = response.body();
                    onTechsoCartAPIResponseReceived(cartAPIResponseModel);
                } else {

                    globalclass.showCustomToast(mActivity, SomethingWentwrngMsg);
                }
            }

            @Override
            public void onFailure(Call<CartAPIResponseModel> call, Throwable t) {
                globalclass.hideProgressDialog(mActivity);
                globalclass.showCustomToast(mActivity, SomethingWentwrngMsg);
            }
        });
    }

    private void onTechsoCartAPIResponseReceived(CartAPIResponseModel cartAPIResponseModel) {

        try {
            if (AddBenCartFlag == 1) {
                AddBenCartFlag = 0;
                ShowdatainAmount(cartAPIResponseModel);
            } else {
                if (CallCartAPIFlag == 1) {
                    CallCartAPIFlag = 0;
                    ShowdatainAmount(cartAPIResponseModel);
                } else {
                    CallApiForSubMitAfterCartAPI(cartAPIResponseModel);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ShowdatainAmount(CartAPIResponseModel cartAPIResponseModel) {

        if (cartAPIResponseModel != null
                && !StringUtils.isNull(cartAPIResponseModel.getResponse())
                && cartAPIResponseModel.getResponse().equals("SUCCESS")
                && cartAPIResponseModel.getOrders() != null
                && cartAPIResponseModel.getOrders().size() > 0) {

            int orderAmountPayable = 0;
            int orderAmountDue = 0;
            for (int j = 0; j < cartAPIResponseModel.getOrders().size(); j++) {
                orderAmountPayable = orderAmountPayable + cartAPIResponseModel.getOrders().get(j).getAmountDue();
                orderAmountDue = orderAmountDue + cartAPIResponseModel.getOrders().get(j).getTestCharges() + cartAPIResponseModel.getOrders().get(j).getServiceCharge();
            }
            ll_amt.setVisibility(View.VISIBLE);
            txtAmountPayable.setVisibility(View.VISIBLE);
            txtAmountPayable.setText("Rs. " + orderAmountDue  +"/-");
            if (cartAPIResponseModel.getOrders() != null && cartAPIResponseModel.getOrders().size() > 0 && cartAPIResponseModel.getOrders().get(0).isHC()){
                relHardCopyCharge.setVisibility(View.VISIBLE);
                int PriceWithoutHardCopy  = orderAmountDue - HARDCOPY_CHARGES;
                txtHardCopyCharge.setText("Rs. " + HARDCOPY_CHARGES  +"/-");
                txtActPrice.setText("Rs. " + PriceWithoutHardCopy  +"/-");
            }else{
                relHardCopyCharge.setVisibility(View.GONE);
                txtActPrice.setText("Rs. " + orderAmountDue  +"/-");
            }


        }
    }

    private void CallSubMitAPI(OrderBookingRequestModel nm) {
        if (FinalSubmitDataModel != null) {
            FinalSubmitDataModel = null;
        }

        FinalSubmitDataModel = nm;
        CallCartAPI(nm);
    }

    private void CallApiForSubMitAfterCartAPI(CartAPIResponseModel cartAPIResponseModel) {

        if (cartAPIResponseModel != null
                && !StringUtils.isNull(cartAPIResponseModel.getResponse())
                && cartAPIResponseModel.getResponse().equals("SUCCESS")
                && cartAPIResponseModel.getOrders() != null
                && cartAPIResponseModel.getOrders().size() > 0) {

            for (int i = 0; i < FinalSubmitDataModel.getOrdbooking().getOrddtl().size(); i++) {
                int orderAmountPayable = 0;
                int orderAmountDue = 0;
                for (int j = 0; j < cartAPIResponseModel.getOrders().size(); j++) {
                    if (FinalSubmitDataModel.getOrdbooking().getOrddtl().get(i).getOrderNo().equals(cartAPIResponseModel.getOrders().get(j).getOrderNo())) {
                        orderAmountPayable = orderAmountPayable + cartAPIResponseModel.getOrders().get(j).getAmountDue();
                        orderAmountDue = orderAmountDue + cartAPIResponseModel.getOrders().get(j).getTestCharges() + cartAPIResponseModel.getOrders().get(j).getServiceCharge();
                        FinalSubmitDataModel.getOrdbooking().getOrddtl().get(i).getOrdUpdateDetails().setPaymentDue(orderAmountPayable);
                        FinalSubmitDataModel.getOrdbooking().getOrddtl().get(i).setAmountDue(orderAmountDue);
                        FinalSubmitDataModel.getOrdbooking().getOrddtl().get(i).setReportHC(isRHC ? 1 : 0);
                        break;
                    }
                }
            }
        }

        if (cd.isConnectingToInternet()) {
            CallTechsoOrderbookingAPI(FinalSubmitDataModel);

        } else {
            globalclass.showCustomToast(mActivity, CheckInternetConnectionMsg);
        }
    }

    private void CallTechsoOrderbookingAPI(OrderBookingRequestModel benModelaschc) {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.DecodeString64(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallTechsoOrderBookingAPI("Bearer "+ appPreferenceManager.getLoginResponseModel().getAccess_token(), benModelaschc);
        globalclass.showProgressDialog(mActivity, "Please wait..");
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                globalclass.hideProgressDialog(mActivity);
                if (response.code() == 200) {
                    globalclass.showCustomToast(mActivity, "SUCCESS");
                    // TODo code to redirect to Arrive Screen
                    setResult(Activity.RESULT_OK);
                    finish();
                } else {

                    globalclass.showCustomToast(mActivity, SomethingWentwrngMsg);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                globalclass.hideProgressDialog(mActivity);
                globalclass.showCustomToast(mActivity, SomethingWentwrngMsg);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case BundleConstants.ADDEDITTESTREQUESTCODE:
                if (resultCode == RESULT_OK) {
                    if (cd.isConnectingToInternet()) {
                        try {
                            selectedTestsList = data.getParcelableArrayListExtra(BundleConstants.ADD_BEN_SELECTED_TESTLIST);
                            edit_selectedTestsList = data.getParcelableArrayListExtra(BundleConstants.EDIT_BEN_SELECTED_TESTLIST);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        CallEditTestList(FlagADDEditBen);
                    } else {
                        globalclass.showCustomToast(mActivity, CheckInternetConnectionMsg, Toast.LENGTH_LONG);
                    }
                }
                break;
            default:
                break;
        }

    }
}
