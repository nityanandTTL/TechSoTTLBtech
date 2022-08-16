package com.thyrocare.btechapp.NewScreenDesigns.Activities;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.Alreadycontains10BenMsg;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.CHECK_INTERNET_CONN;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.CheckInternetConnectionMsg;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.PSAandFPSAforMaleMsg;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;
import static com.thyrocare.btechapp.utils.app.AppConstants.MSG_SERVER_EXCEPTION;
import static com.thyrocare.btechapp.utils.app.BundleConstants.HARDCOPY_CHARGES;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.btechapp.BtechInterfaces.AppInterfaces;
import com.thyrocare.btechapp.Controller.GetDSAProductsController;
import com.thyrocare.btechapp.Controller.PEAddOnController;
import com.thyrocare.btechapp.Controller.PEAuthorizationController;
import com.thyrocare.btechapp.Controller.PEOrderEditController;
import com.thyrocare.btechapp.FireBaseService.BtechFirebaseController;
import com.thyrocare.btechapp.NewScreenDesigns.Adapters.CouponCodeAdapter;
import com.thyrocare.btechapp.NewScreenDesigns.Adapters.DisplaySelectedTestsListForCancellationAdapter_new;
import com.thyrocare.btechapp.NewScreenDesigns.Adapters.ExpandableTestMasterListDisplayAdapter_new;
import com.thyrocare.btechapp.NewScreenDesigns.AddRemoveTestProfileActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Controllers.AddEditBenificaryController;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.OrderUpdateDetailsModel;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.LogUserActivityTagging;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.HomeScreenActivity;
import com.thyrocare.btechapp.dao.utils.ConnectionDetector;
import com.thyrocare.btechapp.delegate.RemoveSelectedTestFromListDelegate_new;
import com.thyrocare.btechapp.models.api.request.AddONRequestModel;
import com.thyrocare.btechapp.models.api.request.CartAPIRequestModel;
import com.thyrocare.btechapp.models.api.request.OrderBookingRequestModel;
import com.thyrocare.btechapp.models.api.request.OrderPassRequestModel;
import com.thyrocare.btechapp.models.api.request.PEOrderEditRequestModel;
import com.thyrocare.btechapp.models.api.request.PEUpdatePatientRequestModel;
import com.thyrocare.btechapp.models.api.request.RemoveBeneficiaryAPIRequestModel;
import com.thyrocare.btechapp.models.api.request.SendOTPRequestModel;
import com.thyrocare.btechapp.models.api.response.CartAPIResponseModel;
import com.thyrocare.btechapp.models.api.response.CommonResponseModel2;
import com.thyrocare.btechapp.models.api.response.CouponCodeResponseModel;
import com.thyrocare.btechapp.models.api.response.DSAProductsResponseModel;
import com.thyrocare.btechapp.models.api.response.GetPETestResponseModel;
import com.thyrocare.btechapp.models.api.response.OrderBookingResponseVisitModel;
import com.thyrocare.btechapp.models.api.response.VerifyCouponResponseModel;
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
import com.thyrocare.btechapp.utils.app.DateUtils;
import com.thyrocare.btechapp.utils.app.GPSTracker;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditBenificaryActivity extends AppCompatActivity {
    LinearLayout ll_amt;
    TextView txtAmountPayable, btnclose, txtTestsList, txt_header_title, txtActPrice, txtHardCopyCharge;
    Button btnOrderSubmit;
    ArrayList<TestRateMasterModel> selectedTestsList = new ArrayList<>();
    ArrayList<TestRateMasterModel> edit_selectedTestsList = new ArrayList<>();
    ArrayList<GetPETestResponseModel.DataDTO> edit_selectedTestsListPE = new ArrayList<>();
    Spinner spn_purpose;
    CartAPIResponseModel cartAPIResponseModelforsubmitFirtsTime;
    DSAProductsResponseModel dsaProductsResponseModelfinal;
    ArrayList<GetPETestResponseModel.DataDTO> peTestArraylist;
    ArrayList<GetPETestResponseModel.DataDTO> peselectedTestsList = new ArrayList<>();
    String peTestRates = "";
    CardView cv_RHC, cv_mob_no, cv_couponmain;
    TextView tv_rhcMessage, tv_mobile, txt_ViewRemove_switch, txt_coupons;
    int peAddben = 0;
    String str_benProduct;
    boolean callPEOrderAPI = false;
    CouponCodeResponseModel couponCodeResponseModel;
    BottomSheetDialog bottomSheet_Coupon_Dialog;
    TextView tv_cartPricetoshow, tv_couponDiscounttoshow, tv_orderTotaltoshow;
    RelativeLayout rl_priceView;
    int SelectedTest_TCPrice = 0;
    int testCouponDiscountedRate = 0;
    int orderAmountDue = 0;
    private Activity mActivity;
    private Global globalclass;
    private ConnectionDetector cd;
    private GPSTracker gpsTracker;
    private AppPreferenceManager appPreferenceManager;
    private boolean FlagADDEditBen = true;
    private EditText edtBenAge, edtBenName;
    private ImageView imgBenGenderF, imgBenGenderM, imgReportHC, imgBenAddTests;
    private boolean isM = false;
    private boolean isRHC = false;
    private String SelectedTestCode = "";
    private int CallCartAPIFlag = 0;
    private int AddRemoveTestAPIFlag = 0;
    private boolean testListFlag;
    private int PSelected_position;
    private OrderBookingRequestModel FinalSubmitDataModel;
    private int AddBenCartFlag = 0;
    private int benId = 0;
    private ExpandableTestMasterListDisplayAdapter_new expAdapter;
    private DisplaySelectedTestsListForCancellationAdapter_new displayAdapter;
    private boolean isTestEdit = false;
    private boolean test_edit_PE = false;
    private Dialog CustomDialogforOTPValidation;
    private RemoveBeneficiaryAPIRequestModel removebenModel;
    private OrderVisitDetailsModel orderVisitDetailsModel;
    private BeneficiaryDetailsModel selectedbeneficiaryDetailsModel;
    private String orderNo;
    private RelativeLayout relHardCopyCharge;
    private String SelectedCoupon;
    private int finalOrderAmount = 0;
    private int discountAmount = 0;

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
        peAddben = getIntent().getIntExtra(Constants.PEAddBen, 0);
        orderVisitDetailsModel = getIntent().getExtras().getParcelable(BundleConstants.VISIT_ORDER_DETAILS_MODEL);
        selectedbeneficiaryDetailsModel = getIntent().getExtras().getParcelable(BundleConstants.BENEFICIARY_DETAILS_MODEL);
        str_benProduct = getIntent().getStringExtra(BundleConstants.BENPRODUCT);
        FlagADDEditBen = getIntent().getBooleanExtra("IsAddBen", true);
        PSelected_position = getIntent().getIntExtra("SelectedBenPosition", 0);
        if (!FlagADDEditBen) {
            if (orderVisitDetailsModel == null || selectedbeneficiaryDetailsModel == null) {
                finish();
            }
        } else {
            if (orderVisitDetailsModel == null) {
                finish();
            }
        }
        orderNo = orderVisitDetailsModel.getVisitId();
        /*if (!Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName()) || orderVisitDetailsModel.getAllOrderdetails().get(0).isPEPartner()) {
            callAPIDSAProducts();
        }*/

       /* if (!Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())) {
            if (orderVisitDetailsModel.getAllOrderdetails().get(0).isPEPartner()) {
                if (cd.isConnectingToInternet()) {
                    PEAuthorizationController peAuthorizationController = new PEAuthorizationController(this);
                    peAuthorizationController.getAuthorizationToken(1, orderVisitDetailsModel.getAllOrderdetails().get(0).getPincode(), orderVisitDetailsModel.getAllOrderdetails().get(0).getVisitId());
                } else {
                    globalclass.showCustomToast(mActivity, CheckInternetConnectionMsg);
                }

            } else {
                callAPIDSAProducts();
            }
        } else {
            CallAPIFORPETESTLIST();
        }*/

        //fungible
//        if (BundleConstants.isPEPartner) {
        if (appPreferenceManager.isPEPartner()) {
            if (cd.isConnectingToInternet()) {
                PEAuthorizationController peAuthorizationController = new PEAuthorizationController(this);
                peAuthorizationController.getAuthorizationToken(1, orderVisitDetailsModel.getAllOrderdetails().get(0).getPincode(), orderVisitDetailsModel.getAllOrderdetails().get(0).getVisitId());

//                CallAPIFORPETESTLIST();
            } else {
                globalclass.showCustomToast(mActivity, CheckInternetConnectionMsg);
            }
        } else {
            callAPIDSAProducts();
        }

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
        btnOrderSubmit = findViewById(R.id.btn_order_submit);
        imgBenAddTests = (ImageView) findViewById(R.id.img_beneficiary_action_edit_tests);
        spn_purpose = findViewById(R.id.spn_purpose);
        cv_RHC = findViewById(R.id.cv_RHC);
        tv_rhcMessage = findViewById(R.id.tv_rhcMessage);
        tv_mobile = findViewById(R.id.tv_mobile);
        cv_mob_no = findViewById(R.id.cv_mob_no);

        cv_couponmain = findViewById(R.id.cv_couponmain);
        txt_ViewRemove_switch = findViewById(R.id.txt_ViewRemove_switch);
        txt_coupons = findViewById(R.id.txt_coupons);

        rl_priceView = findViewById(R.id.rl_priceView);
        tv_orderTotaltoshow = findViewById(R.id.tv_orderTotaltoshow);
        tv_couponDiscounttoshow = findViewById(R.id.tv_couponDiscounttoshow);
        tv_cartPricetoshow = findViewById(R.id.tv_cartPricetoshow);

//fungible
//        if (BundleConstants.isPEPartner) {
        if (appPreferenceManager.isPEPartner()) {
//        if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName()) || orderVisitDetailsModel.getAllOrderdetails().get(0).isPEPartner()) {
            cv_RHC.setVisibility(View.GONE);
            tv_rhcMessage.setVisibility(View.GONE);
            cv_mob_no.setVisibility(View.VISIBLE);
            tv_mobile.setText("Mobile No- " + orderVisitDetailsModel.getAllOrderdetails().get(0).getMobile());
        } else {
            cv_RHC.setVisibility(View.VISIBLE);
            tv_rhcMessage.setVisibility(View.VISIBLE);
            cv_mob_no.setVisibility(View.GONE);

        }

//        ll_amt = (LinearLayout) findViewById(R.id.ll_amt);

        //fungible
//        if (BundleConstants.isPEPartner && BundleConstants.PEDSAOrder) { //to check PEDSA order
        if (appPreferenceManager.isPEPartner() && appPreferenceManager.PEDSAOrder()) { //to check PEDSA order
            imgBenAddTests.setVisibility(View.GONE);
//        } else if (BundleConstants.isPEPartner) {//to check pe order
        } else if (appPreferenceManager.isPEPartner()) {//to check pe order
            imgBenAddTests.setVisibility(View.VISIBLE);
        } else {//for tc order
            imgBenAddTests.setVisibility(View.VISIBLE);
        }


        /*// TODO TEST EDIT not allowed for PE-Btech time being  GG Sir's instruction
        if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())) {
//            if (FlagADDEditBen) {
            imgBenAddTests.setVisibility(View.VISIBLE);
           *//* } else {
                imgBenAddTests.setVisibility(View.GONE);
            }*//*
        } else {
            if (orderVisitDetailsModel.getAllOrderdetails().get(0).isPEPartner() ) {
                //Not allow edit for PE dsa order
                imgBenAddTests.setVisibility(View.GONE);
            } else {
                imgBenAddTests.setVisibility(View.VISIBLE);
            }

        }*/

        /*txtActPrice = (TextView) findViewById(R.id.txtActPrice);
        txtHardCopyCharge = (TextView) findViewById(R.id.txtHardCopyCharge);
        relHardCopyCharge = (RelativeLayout) findViewById(R.id.relHardCopyCharge);*/
    }

    private void initData() {

//        ll_amt.setVisibility(View.GONE);
        txtAmountPayable.setVisibility(View.INVISIBLE);

        if (FlagADDEditBen) {
            benId = (int) (Math.random() * 999);
            if (selectedTestsList != null) {
                selectedTestsList = null;
            }
            selectedTestsList = new ArrayList<>();
        }


        edtBenName.setVisibility(View.VISIBLE);
        if (FlagADDEditBen) {
            txt_header_title.setText("Add Beneficiary");
            isM = true;
            imgBenGenderM.setImageDrawable(getResources().getDrawable(R.drawable.male_icon_orange));
            imgBenGenderF.setImageDrawable(getResources().getDrawable(R.drawable.female));
            isRHC = true;
            imgReportHC.setImageDrawable(getResources().getDrawable(R.drawable.check_mark));

//
        } else {
            txt_header_title.setText("Edit Beneficiary");
            edtBenName.setText(selectedbeneficiaryDetailsModel.getName());

            if (orderVisitDetailsModel.getAllOrderdetails().get(0).getReportHC() == 0) {
//                relHardCopyCharge.setVisibility(View.GONE);
                isRHC = false;
                imgReportHC.setImageDrawable(getResources().getDrawable(R.drawable.tick_icon));
//                txtActPrice.setText("Rs. " + orderVisitDetailsModel.getAllOrderdetails().get(0).getAmountDue() + "/-");
            } else {
//                relHardCopyCharge.setVisibility(View.VISIBLE);
                int PriceWithoutHardCopy = orderVisitDetailsModel.getAllOrderdetails().get(0).getAmountDue() - HARDCOPY_CHARGES;
//                txtHardCopyCharge.setText("Rs. " + HARDCOPY_CHARGES + "/-");
//                txtActPrice.setText("Rs. " + PriceWithoutHardCopy + "/-");
                isRHC = true;
                imgReportHC.setImageDrawable(getResources().getDrawable(R.drawable.check_mark));
            }


//            ll_amt.setVisibility(View.VISIBLE);
            txtAmountPayable.setVisibility(View.VISIBLE);


            SelectedTestCode = selectedbeneficiaryDetailsModel.getTestsCode();

//            if (BundleConstants.isPEPartner || BundleConstants.PEDSAOrder) {
            if (appPreferenceManager.isPEPartner() || appPreferenceManager.PEDSAOrder()) {
                if (InputUtils.isNull(SelectedTestCode)) {
                    txtAmountPayable.setVisibility(View.VISIBLE);
                    txtAmountPayable.setText(mActivity.getResources().getString(R.string.rupee_symbol) + "0" + "/-");
                    txtTestsList.setText(getResources().getString(R.string.add_edit_test_list_message));
                } else {
                    txtAmountPayable.setText(mActivity.getResources().getString(R.string.rupee_symbol) + orderVisitDetailsModel.getAllOrderdetails().get(0).getAmountDue() + "/-");
                    txtTestsList.setError(null);
                    txtTestsList.setText(SelectedTestCode.toString().trim());
                    txtTestsList.setSelected(true);
                    txtTestsList.setError(null);
                }
            } else {
                txtAmountPayable.setText(mActivity.getResources().getString(R.string.rupee_symbol) + orderVisitDetailsModel.getAllOrderdetails().get(0).getAmountDue() + "/-");
                txtTestsList.setError(null);
                txtTestsList.setText(SelectedTestCode.toString().trim());
                txtTestsList.setSelected(true);
                txtTestsList.setError(null);
            }

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

            if (FlagADDEditBen) {
                spn_purpose.setSelection(0);
            } else {
                if (selectedbeneficiaryDetailsModel.getGender().equalsIgnoreCase("M")) {
                    spn_purpose.setSelection(1);
                } else if (selectedbeneficiaryDetailsModel.getGender().equalsIgnoreCase("F")) {
                    spn_purpose.setSelection(2);
                }
            }

            /*if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())) {
                CallAPIFORPETESTLIST();
                //TODO No API call for PE login
            }*//* else {
                CallAPIFORTESTLIST(FlagADDEditBen);
            }*/
        }
    }

    private void callAPIDSAProducts() {
        GetDSAProductsController getDSAProductsController = new GetDSAProductsController(this);
        getDSAProductsController.getDSAProducts(orderNo);
    }

    private void CallAPIFORPETESTLIST() {
        PEAuthorizationController peAC = new PEAuthorizationController(this);
        peAC.getAuthorizationToken(1, "" + orderVisitDetailsModel.getAllOrderdetails().get(0).getPincode(), orderVisitDetailsModel.getAllOrderdetails().get(0).getVisitId());
    }

    private void initListener() {

        spn_purpose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    isM = true;
                } else if (position == 2) {
                    isM = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (peAddben == 1) {
                    Intent intent = new Intent(mActivity, HomeScreenActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    finish();
                }

            }
        });

        imgBenAddTests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (validateforAddTest())
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
//                        globalclass.showalert_OK(mActivity.getResources().getString(R.string.hardcopycharges), mActivity);
                    }

                    if (selectedTestsList != null) {
                        if (selectedTestsList.size() != 0) {
                            onRemoveCoupon();
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

//                        globalclass.showalert_OK(mActivity.getResources().getString(R.string.hardcopycharges), mActivity);
                    }

                    CallCartAPIFlag = 1;
                    AddRemoveTestAPIFlag = 0;
                    CallSubmitAPIforEditBen(orderNo);

                }
            }
        });


        final int finalBenId = benId;
        btnOrderSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fungible
//                if (BundleConstants.isPEPartner) {
                if (appPreferenceManager.isPEPartner()) {
//                if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())) {
                    if (FlagADDEditBen) {
                        if (validateforAddbenPE()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                            builder.setTitle("Add Beneficiary");
                            builder.setMessage("Are you sure you want Add beneficiary?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            try {
                                                if (cd.isConnectingToInternet()) {
                                                    ArrayList<AddONRequestModel.test> arrTest = new ArrayList<AddONRequestModel.test>();
                                                    addAddOnRequest(arrTest);
                                    /*if (!orderVisitDetailsModel.getAllOrderdetails().get(0).isOTP()) {
                                      ArrayList<AddONRequestModel.test> arrTest = new ArrayList<AddONRequestModel.test>();
                                        if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())) {
                                            addAddOnRequest(arrTest);
                                        } else {
                                            CallSubmitAPIforAddBen(orderNo, finalBenId);
                                        }
                                        CallSubmitAPIforAddBen(orderNo, finalBenId);
                                    } else {
                                        CallsendOTPAPIforOrderEdit("Add", orderVisitDetailsModel, orderNo, finalBenId);
                                    }*/
                                                } else {
                                                    globalclass.showCustomToast(mActivity, CHECK_INTERNET_CONN, Toast.LENGTH_LONG);
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            builder.create().
                                    show();

//                            CallsendOTPAPIforOrderEdit("Add", orderVisitDetailsModel, orderNo, finalBenId);
                        }
                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                        builder.setTitle("Edit/Update Beneficiary");
                        builder.setMessage("Are you sure you want to edit /Update beneficiary?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        try {
                                            if (cd.isConnectingToInternet()) {
                                                if (checkBeneficiaryDtls()) {
                                                    if (checkifBeneficiaryDetailsEdited() && callPEOrderAPI) {
                                                        CallSubmitAPIforPEEditBen(0);
                                                        callSubmitAPIforPEOrderEdit(0);
                                                    } else {
                                                        if (callPEOrderAPI) {
                                                            callSubmitAPIforPEOrderEdit(1);//TODO for order test edit
                                                        } else {
                                                            CallSubmitAPIforPEEditBen(1); //TODO for only order edit
                                                        }
                                                    }
                                /*if (!orderVisitDetailsModel.getAllOrderdetails().get(0).isOTP()) {
                                    if (checkifBeneficiaryDetailsEdited() && callPEOrderAPI) {
                                        CallSubmitAPIforPEEditBen(0);
                                        callSubmitAPIforPEOrderEdit(0);
                                    } else {
                                        if (callPEOrderAPI) {
                                            callSubmitAPIforPEOrderEdit(1);//TODO for order test edit
                                        } else {
                                            CallSubmitAPIforPEEditBen(1); //TODO for only order edit
                                        }
                                    }
                                } else {
                                    CallsendOTPAPIforOrderEdit("Edit", orderVisitDetailsModel, orderNo, finalBenId);
                                }*/
                                                }
                                            } else {
                                                globalclass.showCustomToast(mActivity, CHECK_INTERNET_CONN, Toast.LENGTH_LONG);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        builder.create().
                                show();


                    }
                } else {
                    int Bencount = orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().size();
                    if (FlagADDEditBen) {
                        if (Bencount < 10) {
                            if (validateforAddben()) {
                                if (cd.isConnectingToInternet()) {
                                    //fungible
//                                    if (BundleConstants.isPEPartner) {
                                    if (appPreferenceManager.isPEPartner()) {
//                                    if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())) {
                                        if (!orderVisitDetailsModel.getAllOrderdetails().get(0).isOTP()) {
                                            CallSubmitAPIforAddBen(orderNo, finalBenId);
                                        } else {
                                            CallsendOTPAPIforOrderEdit("Add", orderVisitDetailsModel, orderNo, finalBenId);
                                        }
                                    } else {
                                        CallsendOTPAPIforOrderEdit("Add", orderVisitDetailsModel, orderNo, finalBenId);
                                    }
                                } else {
                                    globalclass.showCustomToast(mActivity, CHECK_INTERNET_CONN, Toast.LENGTH_LONG);
                                }
                            }
                        } else {
                            globalclass.showalert_OK(Alreadycontains10BenMsg, mActivity);
                        }
                    } else {
                        if (validateforEditben()) {
                            if (CallCartAPIFlag == 1) {
                                CallSubmitAPIforEditBen(orderNo);
                            } else {
                                if (cd.isConnectingToInternet()) {
                                    //fungible
//                                    if (BundleConstants.isPEPartner) {
                                    if (appPreferenceManager.isPEPartner()) {
//                                    if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())) {
                                        if (checkBeneficiaryDtls()) {
                                            if (!orderVisitDetailsModel.getAllOrderdetails().get(0).isOTP()) {
                                                CallSubmitAPIforEditBen(orderNo);
                                            } else {
                                                CallsendOTPAPIforOrderEdit("Edit", orderVisitDetailsModel, orderNo, finalBenId);
                                            }
                                        }

                                    } else {
                                        if (checkBeneficiaryDtls()) {
                                            CallsendOTPAPIforOrderEdit("Edit", orderVisitDetailsModel, orderNo, finalBenId);
                                        }
                                    }
                                } else {
                                    globalclass.showCustomToast(mActivity, CHECK_INTERNET_CONN, Toast.LENGTH_LONG);
                                }
                            }
                        }
                    }
                }
            }
        });

        cv_couponmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCouponsAPI();
            }
        });

        txt_ViewRemove_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InputUtils.CheckEqualIgnoreCase(txt_ViewRemove_switch.getText().toString(), "View Offers")) {
                    cv_couponmain.setClickable(true);
                } else {
                    onRemoveCoupon();
                }
            }
        });


    }

    private boolean validateforAddTest() {
        if (StringUtils.isNull(edtBenName.getText().toString().trim())) {
            edtBenName.setError("Name is Required");
            return false;
        } else if (!StringUtils.isNull(edtBenName.getText().toString().trim()) && edtBenName.getText().toString().trim().length() < 2) {
            edtBenName.setError("Name should have minimum 2 characters");
            return false;
        } else if (StringUtils.isNull(edtBenAge.getText().toString().trim())) {
            edtBenAge.setError("Age is Required");
            return false;
        } else if (Integer.parseInt(edtBenAge.getText().toString().trim()) < 1 || Integer.parseInt(edtBenAge.getText().toString().trim()) > 120) {
            edtBenAge.setError("Age should be between 1 to 120");
            return false;
        } else if (spn_purpose.getSelectedItemPosition() == 0) {
            Toast.makeText(mActivity, "Kindly select gender", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void onRemoveCoupon() {
        txt_ViewRemove_switch.setText("View Offers");
        txt_coupons.setText("Apply Coupon");
        SelectedCoupon = null;
        if (appPreferenceManager.isPEPartner()) {
            txtAmountPayable.setText(mActivity.getString(R.string.rupee_symbol) + peTestRates + "/-");
        } else {
            txtAmountPayable.setText(mActivity.getString(R.string.rupee_symbol) + SelectedTest_TCPrice + "/-");

        }
        rl_priceView.setVisibility(View.GONE);
        cv_couponmain.setClickable(true);
    }

    private void callCouponsAPI() {

        //TODO API CALL----------
        if (cd.isConnectingToInternet()) {
            //TODO firebase firestore data receiver as per changes from PE ashok yogi

            if (appPreferenceManager.isPEPartner() && validateforAddbenPE()) {
                BtechFirebaseController firebaseController = new BtechFirebaseController(AddEditBenificaryActivity.this, Constants.PE_CouponCollection);
                firebaseController.getAllCoupons(Constants.PE_AllCouponsDocument, appPreferenceManager.isPEPartner());
            } else if (selectedTestsList != null) {
                BtechFirebaseController firebaseController = new BtechFirebaseController(AddEditBenificaryActivity.this, Constants.TC_CouponCollection);
                firebaseController.getAllCoupons(Constants.TC_AllCouponsDocument, appPreferenceManager.isPEPartner());
            }
            //TODO firebase firestore data receiver as per changes from PE ashok yogi

        }
        //TODO API CALL------------
    }

    private boolean validateEditCoupon(EditText editText) {
        if (InputUtils.isNull(editText.getText().toString())) {
            Global.showCustomStaticToast(mActivity, "Please enter coupon code");
            editText.requestFocus();
            return false;
        } else if (editText.getText().toString().startsWith("0")) {
            Global.showCustomStaticToast(mActivity, "Coupon code cannot start with 0");
            editText.requestFocus();
            return false;
        } else if (editText.getText().toString().contains(" ") || editText.getText().toString().contains("  ")) {
            Global.showCustomStaticToast(mActivity, "Coupon code cannot contain space");
            editText.requestFocus();
            return false;
        }
        return true;
    }

    private void bindCouponCodeAdapter(RecyclerView rcl_coupons, CouponCodeResponseModel couponCodeResponseModel) {
        CouponCodeAdapter couponCodeAdapter = new CouponCodeAdapter(AddEditBenificaryActivity.this, couponCodeResponseModel, new AppInterfaces.getClickedPECoupon() {
            @Override
            public void onApplyClick(String coupon) {
                if (cd.isConnectingToInternet()) {
                    if (appPreferenceManager.isPEPartner()) {
                        getClickedCoupon(coupon);
                    }
                }
            }

            @Override
            public void onTCCouponVerification(CouponCodeResponseModel.Coupons verifyTcCoupon) {
                if (SelectedTest_TCPrice <= verifyTcCoupon.getMaxRate() && SelectedTest_TCPrice > verifyTcCoupon.getMinRate()) {
                    if (bottomSheet_Coupon_Dialog.isShowing())
                        bottomSheet_Coupon_Dialog.dismiss();
                    int discountedPrice = 0;
                    discountAmount = verifyTcCoupon.getDiscount();
                    testCouponDiscountedRate = finalOrderAmount;
                    discountedPrice = SelectedTest_TCPrice - verifyTcCoupon.getDiscount();
                    rl_priceView.setVisibility(View.VISIBLE);
                    txt_coupons.setText(verifyTcCoupon.getCouponCode());
                    txt_ViewRemove_switch.setText("Remove");
                    SelectedCoupon = verifyTcCoupon.getCouponCode();
                    txtAmountPayable.setText(mActivity.getString(R.string.rupee_symbol) + discountedPrice + "/-");
                    tv_cartPricetoshow.setText(mActivity.getResources().getString(R.string.rupee_symbol) + SelectedTest_TCPrice + "/-");
                    tv_couponDiscounttoshow.setText(mActivity.getResources().getString(R.string.rupee_symbol) + verifyTcCoupon.getDiscount() + "/-");
                    tv_orderTotaltoshow.setText(mActivity.getResources().getString(R.string.rupee_symbol) + discountedPrice + "/-");
                } else {
                    Global.showCustomStaticToast(mActivity, "Coupon code is not applicable");
                    resetCouponView();
                }

            }
        });
        rcl_coupons.setAdapter(couponCodeAdapter);
    }

    private void resetCouponView() {
        txtAmountPayable.setText(mActivity.getString(R.string.rupee_symbol) + SelectedTest_TCPrice + "/-");
    }


    private boolean checkifBeneficiaryDetailsEdited() {
        String Gender = "";
        if (!selectedbeneficiaryDetailsModel.getName().toString().trim().equalsIgnoreCase(edtBenName.getText().toString().trim())) {
            return true;
        }
        if (selectedbeneficiaryDetailsModel.getAge() != Integer.parseInt(edtBenAge.getText().toString().trim())) {
            return true;
        }
        if (isM) {
            Gender = "M";
        } else {
            Gender = "F";
        }
        return !selectedbeneficiaryDetailsModel.getGender().equalsIgnoreCase(Gender);
    }

    private void callSubmitAPIforPEOrderEdit(int i) {
        ArrayList<PEOrderEditRequestModel.DataDTO.ExtraPayloadDTO.TestsDTO> dtoArrayList = new ArrayList<>();
        PEOrderEditRequestModel peOrderEditRequestModel = new PEOrderEditRequestModel();
        PEOrderEditRequestModel.DataDTO dataDTO = new PEOrderEditRequestModel.DataDTO();
        PEOrderEditRequestModel.DataDTO.ExtraPayloadDTO extraPayloadDTO;
        dataDTO.setPartner_order_id(orderNo);
        dataDTO.setEvent(BundleConstants.EDIT_TEST);
        dataDTO.setUserId(appPreferenceManager.getLoginResponseModel().getUserID());
        dataDTO.setChild_order_id("");

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy' 'HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        extraPayloadDTO = new PEOrderEditRequestModel.DataDTO.ExtraPayloadDTO();
        extraPayloadDTO.setEvent_timestamp(currentDateandTime);

        dataDTO.setExtra_payload(extraPayloadDTO);

        for (int j = 0; j < orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().size(); j++) {
            if (j == PSelected_position) {
                String testlist = txtTestsList.getText().toString().trim();
                ArrayList<String> stringArrayList = new ArrayList<>();
                String[] str = testlist.split(",");
                for (String s : str) {
                    String str1 = s.trim();
                    stringArrayList.add(str1);
                }
                PEOrderEditRequestModel.DataDTO.ExtraPayloadDTO.TestsDTO testsDTO;
                for (int k = 0; k < stringArrayList.size(); k++) {
                    for (int l = 0; l < peTestArraylist.size(); l++) {
                        if (stringArrayList.get(k).equalsIgnoreCase(peTestArraylist.get(l).getName()) ||
                                stringArrayList.get(k).equalsIgnoreCase(peTestArraylist.get(l).getDos_code()) ||
                                stringArrayList.get(k).equalsIgnoreCase(peTestArraylist.get(l).getPartner_lab_test_id())) {

                            testsDTO = new PEOrderEditRequestModel.DataDTO.ExtraPayloadDTO.TestsDTO();
                            testsDTO.setPartner_patient_id(orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(PSelected_position).getLeadId());
                            if (peTestArraylist.get(l).getType().equalsIgnoreCase("test") || peTestArraylist.get(l).getType().equalsIgnoreCase("Profile")) {
                                testsDTO.setDos_code(peTestArraylist.get(l).getPartner_lab_test_id());
                                testsDTO.setLab_dos_name(peTestArraylist.get(l).getPartner_lab_test_id());
                            } else if (peTestArraylist.get(l).getType().equalsIgnoreCase("package")) {
                                testsDTO.setDos_code(peTestArraylist.get(l).getPartner_lab_test_id());
                                testsDTO.setLab_dos_name(peTestArraylist.get(l).getName());
                            }
                            testsDTO.setPrice(Integer.parseInt(peTestArraylist.get(l).getPrice()));
                            testsDTO.setType(peTestArraylist.get(l).getType());
                            dtoArrayList.add(testsDTO);
                            break;
                        }
                    }
                }
            } else {
//                for (int k = 0; k < orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().size(); k++) {
                if (PSelected_position != j) {
                    ArrayList<String> strArr = new ArrayList<>();
                    String strTest = orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(j).getTestsCode();
                    if (!InputUtils.isNull(strTest)) {
                        String[] str = strTest.split(",");
                        for (String s : str) {
                            String str1 = s.trim();
                            strArr.add(str1);
                        }
                        for (int l = 0; l < strArr.size(); l++) {
                            for (int m = 0; m < peTestArraylist.size(); m++) {
                                if (strArr.get(l).equalsIgnoreCase(peTestArraylist.get(m).getName()) ||
                                        strArr.get(l).equalsIgnoreCase(peTestArraylist.get(m).getDos_code()) ||
                                        strArr.get(l).equalsIgnoreCase(peTestArraylist.get(m).getPartner_lab_test_id())) {
                                    PEOrderEditRequestModel.DataDTO.ExtraPayloadDTO.TestsDTO testsDTO = new PEOrderEditRequestModel.DataDTO.ExtraPayloadDTO.TestsDTO();
                                    if (peTestArraylist.get(m).getType().equalsIgnoreCase("test") || peTestArraylist.get(m).getType().equalsIgnoreCase("Profile")) {
                                        testsDTO.setDos_code(peTestArraylist.get(m).getPartner_lab_test_id());
                                        testsDTO.setLab_dos_name(peTestArraylist.get(m).getPartner_lab_test_id());
                                    } else if (peTestArraylist.get(m).getType().equalsIgnoreCase("package")) {
                                        testsDTO.setDos_code(peTestArraylist.get(m).getPartner_lab_test_id());
                                        testsDTO.setLab_dos_name(peTestArraylist.get(m).getName());
                                    }
                                    testsDTO.setPrice(Integer.parseInt(peTestArraylist.get(m).getPrice()));
                                    testsDTO.setPartner_patient_id(orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(j).getLeadId());
                                    testsDTO.setType(peTestArraylist.get(m).getType());
                                    dtoArrayList.add(testsDTO);
                                    break;
                                }
                            }
                        }
                    } else {
                        //TODO for HardBlocking
                        PEOrderEditRequestModel.DataDTO.ExtraPayloadDTO.TestsDTO testsDTO = new PEOrderEditRequestModel.DataDTO.ExtraPayloadDTO.TestsDTO();
                        testsDTO.setDos_code("");
                        testsDTO.setLab_dos_name("");
                        testsDTO.setPrice(0);
                        testsDTO.setPartner_patient_id(orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(j).getLeadId());
                        testsDTO.setType("");
                        dtoArrayList.add(testsDTO);
//                        break;
                    }
                }
//                }
            }
        }


        extraPayloadDTO.setTests(dtoArrayList);
        dataDTO.setExtra_payload(extraPayloadDTO);

        peOrderEditRequestModel.setData(dataDTO);

        callAPIFORPEORDEREDIT(peOrderEditRequestModel, i);
    }

    private void callAPIFORPEORDEREDIT(PEOrderEditRequestModel peOrderEditRequestModel, int i) {
        if (cd.isConnectingToInternet()) {
            PEOrderEditController peOrderEditController = new PEOrderEditController(this);
            peOrderEditController.postPEOrderEdit("EDIT", peOrderEditRequestModel, i);
        } else {
            globalclass.showCustomToast(mActivity, CHECK_INTERNET_CONN, Toast.LENGTH_SHORT);
        }
    }

    private void CallSubmitAPIforPEEditBen(int i) {
        PEUpdatePatientRequestModel peUpdatePatientRequestModel = new PEUpdatePatientRequestModel();
        ArrayList<PEUpdatePatientRequestModel.DataDTO> arraypeData = new ArrayList<>();
        ArrayList<PEUpdatePatientRequestModel.DataDTO.PatientDetailsDTO> arraypePatientDtl = new ArrayList<>();

        PEUpdatePatientRequestModel.DataDTO.PatientDetailsDTO pePatientDtl = new PEUpdatePatientRequestModel.DataDTO.PatientDetailsDTO();
        pePatientDtl.setPartner_patient_id(selectedbeneficiaryDetailsModel.getLeadId());
        pePatientDtl.setPatient_name(edtBenName.getText().toString());
        pePatientDtl.setPatient_age(edtBenAge.getText().toString());
        String gender;
        if (isM) {
            gender = "M";
        } else {
            gender = "F";
        }
        pePatientDtl.setPatient_gender(gender);
//        pePatientDtl.setPatient_contact(orderVisitDetailsModel.getAllOrderdetails().get(0).getMobile());
        arraypePatientDtl.add(pePatientDtl);

        PEUpdatePatientRequestModel.DataDTO peData = new PEUpdatePatientRequestModel.DataDTO();
        peData.setPartner_order_id(orderVisitDetailsModel.getVisitId());
        peData.setPatient_details(pePatientDtl);
        arraypeData.add(peData);

        peUpdatePatientRequestModel.setUserId(appPreferenceManager.getLoginResponseModel().getUserID());
        peUpdatePatientRequestModel.setData(peData);

        if (cd.isConnectingToInternet()) {
            PEOrderEditController peOrderEditController = new PEOrderEditController(this);
            peOrderEditController.postPEUpdatePatient(peUpdatePatientRequestModel, i);
        } else {
            globalclass.showCustomToast(mActivity, CHECK_INTERNET_CONN, Toast.LENGTH_LONG);
        }
    }

    private boolean checkBeneficiaryDtls() {
        if (edtBenName.getText().toString().trim().contains("Test_user") || edtBenName.getText().toString().trim().contains("TEST_USER") ||
                StringUtils.isNull(edtBenName.getText().toString().trim())) {
            Toast.makeText(mActivity, "Kindly edit beneficiary name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!StringUtils.isNull(edtBenName.getText().toString().trim()) && edtBenName.getText().toString().trim().length() < 2) {
            Toast.makeText(mActivity, "Name should have minimum 2 characters", Toast.LENGTH_SHORT).show();
            return false;
        } else if (edtBenAge.getText().toString().equalsIgnoreCase("150") || StringUtils.isNull(edtBenAge.getText().toString().trim())) {
            Toast.makeText(mActivity, "Kindly edit beneficiary age", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Integer.parseInt(edtBenAge.getText().toString().trim()) < 1 || Integer.parseInt(edtBenAge.getText().toString().trim()) > 120) {
            Toast.makeText(mActivity, "Age should be between 1 to 120", Toast.LENGTH_SHORT).show();
            return false;
        } else if (spn_purpose.getSelectedItemPosition() == 0) {
            Toast.makeText(mActivity, "Kindly select gender", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void CallsendOTPAPIforOrderEdit(final String Action, final OrderVisitDetailsModel orderVisitDetailsModel, final String orderNo, final int finalBenId) {

        SendOTPRequestModel model = new SendOTPRequestModel();
        model.setMobile(orderVisitDetailsModel.getAllOrderdetails().get(0).getMobile());
        model.setOrderno(orderVisitDetailsModel.getAllOrderdetails().get(0).getVisitId());
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<CommonResponseModel2> responseCall = apiInterface.CallSendOTPAPI(model);
        globalclass.showProgressDialog(mActivity, "Requesting for OTP. Please wait..");
        responseCall.enqueue(new Callback<CommonResponseModel2>() {
            @Override
            public void onResponse(Call<CommonResponseModel2> call, Response<CommonResponseModel2> response) {
                globalclass.hideProgressDialog(mActivity);
                if (response.isSuccessful() && response.body() != null) {
                    CommonResponseModel2 responseModel = response.body();
                    if (!TextUtils.isEmpty(responseModel.getRES_ID()) && responseModel.getRES_ID().equalsIgnoreCase("RES0000")) {
//                        globalclass.showCustomToast(mActivity, "OTP send successfully to mobile number mapped to this order.");
                        ShowDialogToVerifyOTP(Action, orderVisitDetailsModel, orderNo, finalBenId);
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

        /*final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mActivity, R.style.BottomSheetTheme);
        final View bottomSheet = LayoutInflater.from(mActivity).inflate(R.layout.bottomsheet_otp, (ViewGroup) mActivity.findViewById(R.id.bottom_sheet_dialog_parent));
        RelativeLayout rel_main = (RelativeLayout) bottomSheet.findViewById(R.id.rel_main);
        TextView tv_header = (TextView) bottomSheet.findViewById(R.id.tv_header);
        ImageView img_btn_validateOTP = (ImageView) bottomSheet.findViewById(R.id.img_btn_validateOTP);
        ImageView img_close = (ImageView) bottomSheet.findViewById(R.id.img_close);
        final EditText edt_OTP = (EditText) bottomSheet.findViewById(R.id.edt_OTP);

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });


        img_btn_validateOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strOTP = edt_OTP.getText().toString().trim();
                if (!InputUtils.isNull(strOTP) && strOTP.length() != 4) {
                    globalclass.showalert_OK("Please enter valid OTP. Length required : 4", mActivity);
                    edt_OTP.requestFocus();
                } else {
                    OrderPassRequestModel model = new OrderPassRequestModel();
                    model.setMobile(orderVisitDetailsModel.getAllOrderdetails().get(0).getMobile());
                    model.setOTP(strOTP);
                    model.setVisitId(orderVisitDetailsModel.getAllOrderdetails().get(0).getVisitId());
                    if (cd.isConnectingToInternet()) {
                        CallValidateOTPAPI(model, Action, orderNo, finalBenId);
                    } else {
                        globalclass.showCustomToast(mActivity, mActivity.getResources().getString(R.string.plz_chk_internet));
                    }
                }
            }
        });
        if (!mActivity.isFinishing()) {
            bottomSheetDialog.show();
        }
        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();*/

        CustomDialogforOTPValidation = new Dialog(mActivity);
        CustomDialogforOTPValidation.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        CustomDialogforOTPValidation.requestWindowFeature(Window.FEATURE_NO_TITLE);
        CustomDialogforOTPValidation.setContentView(R.layout.validate_otp_dialog);
        CustomDialogforOTPValidation.setCancelable(false);
        CustomDialogforOTPValidation.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        RelativeLayout rel_main = (RelativeLayout) CustomDialogforOTPValidation.findViewById(R.id.rel_main);
        TextView tv_header = (TextView) CustomDialogforOTPValidation.findViewById(R.id.tv_header);
        ImageView img_btn_validateOTP = (ImageView) CustomDialogforOTPValidation.findViewById(R.id.img_btn_validateOTP);
        ImageView img_close = (ImageView) CustomDialogforOTPValidation.findViewById(R.id.img_close);
        final EditText edt_OTP = (EditText) CustomDialogforOTPValidation.findViewById(R.id.edt_OTP);

        //fungible
//        if (BundleConstants.isPEPartner) {
        if (appPreferenceManager.isPEPartner()) {
//        if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())) {
            tv_header.setText(R.string.pe_otp_message);
        } else {
            tv_header.setText(R.string.tc_otp_message);
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

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

        if (!mActivity.isFinishing()) {
            CustomDialogforOTPValidation.show();
        }


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
                    globalclass.showalert_OK("Please enter valid OTP. Length required : 4", mActivity);
                    edt_OTP.requestFocus();
                } else {
                    OrderPassRequestModel model = new OrderPassRequestModel();
                    model.setMobile(orderVisitDetailsModel.getAllOrderdetails().get(0).getMobile());
                    model.setOTP(strOTP);
                    model.setVisitId(orderVisitDetailsModel.getAllOrderdetails().get(0).getVisitId());
                    if (cd.isConnectingToInternet()) {
                        CallValidateOTPAPI(model, Action, orderNo, finalBenId);
                    } else {
                        globalclass.showCustomToast(mActivity, mActivity.getResources().getString(R.string.plz_chk_internet));
                    }
                }

            }
        });

    }

    private void CallValidateOTPAPI(OrderPassRequestModel model, final String Action, final String orderNo, final int finalBenId) {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallValidateOTPAPI(model);
        globalclass.showProgressDialog(mActivity, "Validating OTP. Please wait..");
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                globalclass.hideProgressDialog(mActivity);
                if (response.isSuccessful() && response.body() != null) {
                    String strresponse = response.body();
                    if (!TextUtils.isEmpty(strresponse) && strresponse.toUpperCase().contains("SUCCESS")) {
                        Toast.makeText(mActivity, "OTP Validated Successfully.", Toast.LENGTH_SHORT).show();
//                        globalclass.showCustomToast(mActivity, "OTP Validated Successfully.");
                        if (!mActivity.isFinishing() && CustomDialogforOTPValidation != null && CustomDialogforOTPValidation.isShowing()) {
                            CustomDialogforOTPValidation.dismiss();
                        }
                        if (Action.equalsIgnoreCase("Add")) {
                            ArrayList<AddONRequestModel.test> arrTest = new ArrayList<AddONRequestModel.test>();
                            //fungible
//                            if (BundleConstants.isPEPartner) {
                            if (appPreferenceManager.isPEPartner()) {
//                            if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())) {
                                addAddOnRequest(arrTest);
                            } else {
                                CallSubmitAPIforAddBen(orderNo, finalBenId);
                            }

                        } else if (Action.equalsIgnoreCase("Edit")) {
                            //fungible
//                            if (BundleConstants.isPEPartner) {
                            if (appPreferenceManager.isPEPartner()) {
//                            if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())) {
                                if (callPEOrderAPI && checkifBeneficiaryDetailsEdited()) {
                                    CallSubmitAPIforPEEditBen(0);
                                    callSubmitAPIforPEOrderEdit(0);
                                } else {
                                    if (callPEOrderAPI) {
                                        callSubmitAPIforPEOrderEdit(1);//TODO for order test edit
                                    } else {
                                        CallSubmitAPIforPEEditBen(1); //TODO for only order edit
                                    }
                                }
                              /*  if (callPEOrderAPI) {
                                    callSubmitAPIforPEOrderEdit();
                                } else {
                                    CallSubmitAPIforPEEditBen();
                                }*/
                            } else {
                                CallSubmitAPIforEditBen(orderNo);
                            }

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

    private void addAddOnRequest(ArrayList<AddONRequestModel.test> arrTest) {
        try {
            AddONRequestModel.patient patient = new AddONRequestModel.patient();
//        patient.setId(DeviceUtils.randomInt(10000000, 99999999));
            patient.setId(0);
            System.out.println("Mith>>>>>" + patient.getId());
            patient.setName(edtBenName.getText().toString());
            patient.setAge(Integer.parseInt("" + edtBenAge.getText().toString()));
            patient.setGender(isM ? "Male" : "Female");

            for (int i = 0; i < peselectedTestsList.size(); i++) {
                AddONRequestModel.test test = new AddONRequestModel.test();
                test.setId(peselectedTestsList.get(i).getId());
                test.setTest_id(peselectedTestsList.get(i).getTest_dos_id());
                test.setType(peselectedTestsList.get(i).getType());
                arrTest.add(test);
            }
            AddONRequestModel addONRequestModel = new AddONRequestModel();
            addONRequestModel.setPatient(patient);
            addONRequestModel.setTest(arrTest);
            addONRequestModel.setPromocode(InputUtils.isNull(SelectedCoupon) ? "" : SelectedCoupon);
            PEAddOnController peAddOnController = new PEAddOnController(this);
            peAddOnController.getAddOnOrder(orderVisitDetailsModel.getVisitId(), addONRequestModel, peAddben);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
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
        OrderUpdateDetailsModel orderUpdate = new OrderUpdateDetailsModel();
        orderUpdate.setBtechId(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
        odModel.setOrdUpdateDetails(orderUpdate);
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
        ordbooking.setBtechId(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
        System.out.println(">>>>>>>>>>>>>>>>>>>>" + ordbooking.getBtechId());
        ordbooking.setVisitId(orderNo);
        ordbooking.setOrddtl(orddtl);

        /*OrderUpdateDetailsModel orderUpdate = new OrderUpdateDetailsModel();
        orderUpdate.setBtechId(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));

        OrderDetailsModel odm = new OrderDetailsModel();
        odm.setOrdUpdateDetails(orderUpdate);*/

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
        //TODO set the discount here to resolve the remove and edit ben issue-----------------------------------
        ordbooking.setCoupon(InputUtils.isNull(SelectedCoupon) ? 0 : discountAmount);
        //TODO set the discount here to resolve the remove and edit ben issue------------------------------------
        ordbooking.setBtechId(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>Mith" + ordbooking.getBtechId());
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

        if (StringUtils.isNull(edtBenName.getText().toString().trim())) {
            edtBenName.setError("Name is Required");
            return false;
        } else if (!StringUtils.isNull(edtBenName.getText().toString().trim()) && edtBenName.getText().toString().trim().length() < 2) {
            edtBenName.setError("Name should have minimum 2 characters");
            return false;
        } else if (StringUtils.isNull(edtBenAge.getText().toString().trim())) {
            edtBenAge.setError("Age is Required");
            return false;
        } else if (Integer.parseInt(edtBenAge.getText().toString().trim()) < 1 || Integer.parseInt(edtBenAge.getText().toString().trim()) > 120) {
            edtBenAge.setError("Age should be between 1 to 120");
            return false;
        } else if (StringUtils.isNull(txtTestsList.getText().toString().trim())) {
            txtTestsList.setError("Tests List is Required");
            return false;
        } else if (selectedTestsList.size() == 0) {
            Toast.makeText(mActivity, "Kindly select test to add", Toast.LENGTH_SHORT).show();
            return false;
        } else if ((str.contains("PSA") || str.contains("FPSA")) && !isM) {
            txtTestsList.setError(PSAandFPSAforMaleMsg);
            globalclass.showalert_OK(PSAandFPSAforMaleMsg, mActivity);
            return false;
        } else if (spn_purpose.getSelectedItemPosition() == 0) {
            Toast.makeText(mActivity, "Kindly select gender", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateforAddbenPE() {
        if (StringUtils.isNull(edtBenName.getText().toString().trim())) {
            edtBenName.setError("Name is Required");
            return false;
        } else if (!StringUtils.isNull(edtBenName.getText().toString().trim()) && edtBenName.getText().toString().trim().length() < 2) {
            edtBenName.setError("Name should have minimum 2 characters");
            return false;
        } else if (StringUtils.isNull(edtBenAge.getText().toString().trim())) {
            edtBenAge.setError("Age is Required");
            return false;
        } else if (Integer.parseInt(edtBenAge.getText().toString().trim()) < 1 || Integer.parseInt(edtBenAge.getText().toString().trim()) > 120) {
            edtBenAge.setError("Age should be between 1 to 120");
            return false;
        } else if (StringUtils.isNull(txtTestsList.getText().toString().trim())) {
            txtTestsList.setError("Tests List is Required");
            return false;
        } else if ((peselectedTestsList.size() == 0)) {
            Toast.makeText(mActivity, "Kindly select test to add", Toast.LENGTH_SHORT).show();
            return false;
        } else if (spn_purpose.getSelectedItemPosition() == 0) {
            Toast.makeText(mActivity, "Kindly select gender", Toast.LENGTH_SHORT).show();
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
        } else if (!StringUtils.isNull(edtBenName.getText().toString().trim()) && edtBenName.getText().toString().trim().length() < 2) {
            edtBenName.setError("Name should have minimum 2 characters");
            return false;
        } else if (StringUtils.isNull(edtBenAge.getText().toString().trim())) {
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

    /*private void CallAPIFORTESTLIST(boolean b_flag) {
        testListFlag = b_flag;
        if (cd.isConnectingToInternet()) {
            CallGetTechsoProductsAPI();
        } else {
            globalclass.showCustomToast(mActivity, CheckInternetConnectionMsg);
        }
    }*/

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
        if (!UpdateProduct()) {
            if (cd.isConnectingToInternet()) {
                CallGetTechsoProductsAPI();
            } else {
                globalclass.showCustomToast(mActivity, CheckInternetConnectionMsg);
            }
        } else {
            BrandTestMasterModel brandTestMasterModel = new Gson().fromJson(appPreferenceManager.getCacheProduct(), BrandTestMasterModel.class);
            // CallTestData(getBrandTestMaster(brandTestMasterModel));
            CallTestData(getBrandTestMaster1(brandTestMasterModel, dsaProductsResponseModelfinal));
        }
    }


   /* private void CallGetTechsoProductsAPI() {

        try {
            GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
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
    }*/

    private boolean UpdateProduct() {
        String getPreviouseMillis = appPreferenceManager.getCashingTime();
        if (getPreviouseMillis.equalsIgnoreCase(DateUtils.getCurrentdateWithFormat("yyyy-MM-dd"))) {
            return true;
        } else {
            return false;
        }
    }

    private void CallGetTechsoProductsAPI() {
        try {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>" + "Bearer " + appPreferenceManager.getAccess_Token());
            GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
            Call<BrandTestMasterModel> responseCall = apiInterface.CallGetTechsoPRoductsAPI("Bearer " + appPreferenceManager.getAccess_Token());
            globalclass.showProgressDialog(mActivity, "Fetching products. Please wait..");
            responseCall.enqueue(new Callback<BrandTestMasterModel>() {
                @Override
                public void onResponse(Call<BrandTestMasterModel> call, retrofit2.Response<BrandTestMasterModel> response) {
                    globalclass.hideProgressDialog(mActivity);
                    if (response.isSuccessful() && response.body() != null) {
                        Gson gson22 = new Gson();
                        String json22 = gson22.toJson(response.body());
                        appPreferenceManager.setCacheProduct(json22);
                        appPreferenceManager.setCashingTime(DateUtils.getCurrentdateWithFormat("yyyy-MM-dd"));

                        CallTestData(getBrandTestMaster1(response.body(), dsaProductsResponseModelfinal));

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

    private BrandTestMasterModel getBrandTestMaster1(BrandTestMasterModel brandTestMasterModel, DSAProductsResponseModel dsaProductResponseModel) {
        BrandTestMasterModel brandTestMasterModel1 = new BrandTestMasterModel();
        ArrayList<TestRateMasterModel> testRateMasterModels = new ArrayList<>();
        ArrayList<TestRateMasterModel> testRateMasterModelsNew = new ArrayList<>();
        ArrayList<TestRateMasterModel> testRateMasterModelsFinal = new ArrayList<>();

        if (brandTestMasterModel != null && brandTestMasterModel.getTstratemaster() != null && brandTestMasterModel.getTstratemaster().size() > 0) {
            for (int i = 0; i < brandTestMasterModel.getTstratemaster().size(); i++) {
                String st = "ADVANCED RENAL PROFILE";
                String st1 = brandTestMasterModel.getTstratemaster().get(i).getTestCode();
                String st2 = brandTestMasterModel.getTstratemaster().get(i).getDescription();

                System.out.println("----");
                if (brandTestMasterModel.getTstratemaster().get(i).getAccessUserCode() != null && brandTestMasterModel.getTstratemaster().get(i).getAccessUserCode().size() > 0) {
                    if (brandTestMasterModel.getTstratemaster().get(i).getAccessUserCode().isEmpty()) {
                        testRateMasterModels.add(brandTestMasterModel.getTstratemaster().get(i));
                    }
                    for (int j = 0; j < brandTestMasterModel.getTstratemaster().get(i).getAccessUserCode().size(); j++) {
                        try {
                            System.out.println(st);
                            System.out.println(st1);
                            System.out.println(st2);

                            if (st.equalsIgnoreCase(st1) || st.equalsIgnoreCase(st2)) {
                                System.out.println(st);
                            }

                            if (Integer.parseInt(brandTestMasterModel.getTstratemaster().get(i).getAccessUserCode().get(j).getAccessCode()) == orderVisitDetailsModel.getAllOrderdetails().get(0).getUserAccessCode()) {
                                testRateMasterModels.add(brandTestMasterModel.getTstratemaster().get(i));
                                break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

//        testRateMasterModelsNew.addAll(testRateMasterModels);
        if (dsaProductResponseModel != null && dsaProductResponseModel.getTstratemaster() != null && dsaProductResponseModel.getTstratemaster().size() > 0) {
            testRateMasterModelsNew.addAll(dsaProductResponseModel.getTstratemaster());
        }
      /*  if (dsaProductResponseModel != null && dsaProductResponseModel.getTstratemaster() != null && dsaProductResponseModel.getTstratemaster().size() > 0) {
            testRateMasterModels.addAll(dsaProductResponseModel.getTstratemaster());
        }

        java.util.List<TestRateMasterModel> testRateMasterModelList1 = new ArrayList<>();
        testRateMasterModelList1.addAll(testRateMasterModels);

        java.util.List<TestRateMasterModel> testRateMasterModelList = testRateMasterModels.stream().distinct().collect(Collectors.toList());*/
        testRateMasterModels.addAll(testRateMasterModelsNew);

        ArrayList<TestRateMasterModel> tempArr = new ArrayList<>();

        for (TestRateMasterModel data : testRateMasterModels) {
            if (!tempArr.contains(data)) {
                tempArr.add(data);
            }
        }
        System.out.println("1Arraylist>>>>>>>>>>>>>>>" + tempArr.size());
        testRateMasterModels.clear();
        testRateMasterModels.addAll(tempArr);

        /*for (int i = 0; i <testRateMasterModels.size(); i++) {
            for (int j = 0; j < testRateMasterModelsNew.size(); j++) {
                if (testRateMasterModels.get(i).getDescription().toString().trim().equalsIgnoreCase(testRateMasterModelsNew.get(j).getDescription().toString().trim()) ||
                        testRateMasterModels.get(i).getTestId()==(testRateMasterModelsNew.get(j).getTestId())){
                    tempArr.add(testRateMasterModelsNew.get(j));
                    break;
                }
            }
        }

        testRateMasterModels.removeAll(tempArr);
        testRateMasterModels.addAll(testRateMasterModelsNew);

        for (int i = 0; i <testRateMasterModels.size() ; i++) {
            System.out.println(testRateMasterModels.get(i).getDescription());
        }
*/
        /*int position = 0;

        for (TestRateMasterModel test2 : testRateMasterModelsNew) {
            boolean found = false;
            for (TestRateMasterModel test1 : testRateMasterModels) {
                if (test2.getDescription().equalsIgnoreCase(test1.getDescription())) {
                    found = true;
                }
            }
            position++;

            if (!found) {
                testRateMasterModelsFinal.add(testRateMasterModelsNew.get(position));
            }
        }*/

/*        if (dsaProductResponseModel != null && dsaProductResponseModel.getTstratemaster() != null && dsaProductResponseModel.getTstratemaster().size() > 0) {
            for (int i = 0; i < testRateMasterModels.size(); i++) {
                for (int j = 0; j < dsaProductResponseModel.getTstratemaster().size(); j++) {
                    if (!testRateMasterModels.get(i).getDescription().contains(dsaProductResponseModel.getTstratemaster().get(j).getDescription())) {
                        if (testRateMasterModelsNew.size() == 0) {
                            testRateMasterModelsNew.add(dsaProductResponseModel.getTstratemaster().get(j));
                        } else {
                            for (int k = 0; k < testRateMasterModelsNew.size(); k++) {
                                if (!testRateMasterModels.get(i).getDescription().contains(dsaProductResponseModel.getTstratemaster().get(j).getDescription())) {

                                }

                            }
                        }
                        testRateMasterModelsNew.add(dsaProductResponseModel.getTstratemaster().get(j));
//                        break;
                    }
                }
            }
//            testRateMasterModels.addAll(dsaProductResponseModel.getTstratemaster());
        }*/


/*
        Set<TestRateMasterModel>toRetain = new TreeSet<TestRateMasterModel>( TEst.CASE_INSENSITIVE_ORDER);
        toRetain.addAll(testRateMasterModels);
        Set<TestRateMasterModel>newList = new LinkedHashSet<>(testRateMasterModels);
        newList.retainAll(new LinkedHashSet<TestRateMasterModel>(toRetain));
        testRateMasterModels = new ArrayList<TestRateMasterModel>(newList);*/

        /*testRateMasterModelsFinal.addAll(testRateMasterModels);
        for (int i = 0; i < testRateMasterModels.size(); i++) {
            for (int j = 0; j < testRateMasterModelsNew.size(); j++) {
                if (!testRateMasterModels.get(i).getDescription().equalsIgnoreCase(testRateMasterModelsNew.get(j).getDescription())){
                    testRateMasterModelsFinal.add(testRateMasterModelsNew.get(j));
                }
                break;
            }
        }*/



        /*ArrayList<TestRateMasterModel> testRateMasterModelsFinal = new ArrayList<>();
        for (int i = 0; i < testRateMasterModels.size(); i++) {
            if (testRateMasterModelsFinal.size() > 0) {
                for (int j = 0; j < testRateMasterModelsFinal.size(); j++) {
                    if (!testRateMasterModels.get(i).getDescription().equalsIgnoreCase(testRateMasterModelsFinal.get(j).getDescription())) {
                        testRateMasterModelsFinal.add(testRateMasterModels.get(i));
                    }
                }
            } else {
                testRateMasterModelsFinal.add(testRateMasterModels.get(i));
            }
        }
        System.out.println("2Arraylist>>>>>>>>>>>>>>>"+testRateMasterModelsFinal.size());
        testRateMasterModels.clear();
        testRateMasterModels.addAll(testRateMasterModelsFinal);*/
        // brandTestMasterModel1.setTstratemaster(tempArr);
        brandTestMasterModel1.setTstratemaster(testRateMasterModels);
        brandTestMasterModel1.setBrandId(brandTestMasterModel.getBrandId());
        brandTestMasterModel1.setBrandName(brandTestMasterModel.getBrandName());

        String json = new Gson().toJson(brandTestMasterModel1);
        System.out.println(";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;" + json);

        return brandTestMasterModel1;
    }

    public BrandTestMasterModel getBrandTestMaster(BrandTestMasterModel brandTestMasterModel) {
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
                        //Mitanshu only in case of offer
                        //TODO  if there is a test in offer with same description but different testcode - not to add
                        if (edit_selectedTestsList.size() != 0) {
                            for (int k = 0; k < edit_selectedTestsList.size(); k++) {
                                if (edit_selectedTestsList.get(k).getDescription().equalsIgnoreCase(result.getTstratemaster().get(j).getDescription())
                                        || edit_selectedTestsList.get(k).getTestCode().equalsIgnoreCase(result.getTstratemaster().get(j).getDescription())) {

                                } else if (str[i].equalsIgnoreCase(result.getTstratemaster().get(j).getTestCode()) || str[i].trim().equalsIgnoreCase(result.getTstratemaster().get(j).getDescription())) {
                                    edit_selectedTestsList.add(result.getTstratemaster().get(j));
                                }
                            }
                        } else {
                            if (selectedbeneficiaryDetailsModel.getProjId().equalsIgnoreCase(result.getTstratemaster().get(j).getTestCode()) || str[i].trim().equalsIgnoreCase(result.getTstratemaster().get(j).getDescription())) {
                                edit_selectedTestsList.add(result.getTstratemaster().get(j));
                            }
                        }
                    }
                } else {
                    if (str[i].trim().equalsIgnoreCase(result.getTstratemaster().get(j).getTestCode().trim())) {
                        if (result.getTstratemaster().get(j).getTestType().equalsIgnoreCase("POP")) {
                            for (int k = 0; k < selectedbeneficiaryDetailsModel.getTestSampleType().size(); k++) {
                                if (str[i].trim().equalsIgnoreCase(result.getTstratemaster().get(j).getTestCode())) {
                                    if (selectedbeneficiaryDetailsModel.getTestSampleType().get(k).getTestType().equalsIgnoreCase(result.getTstratemaster().get(j).getTestType())) {
                                        edit_selectedTestsList.add(result.getTstratemaster().get(j));
                                        break;
                                    }
                                }
                            }
                        } else {
                            edit_selectedTestsList.add(result.getTstratemaster().get(j));
                            break;
                        }
                    }
                }

            }
        }
    }

    private void CallEditTestList(final boolean isAddBen) {
        isM = spn_purpose.getSelectedItemPosition() == 1;

        if (!InputUtils.isNull(edit_selectedTestsList)) {
            addEditTest(isAddBen);
            //fungible
//        } else if (isAddBen && BundleConstants.isPEPartner) {
        } else if (isAddBen && appPreferenceManager.isPEPartner()) {
//        } else if (isAddBen && Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())) {
            if (peselectedTestsList != null && peselectedTestsList.size() > 0) {
                addEditTest(isAddBen);
            } else {
                Intent intent = new Intent(mActivity, AddRemoveTestProfileActivity.class);
                intent.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL, orderVisitDetailsModel);
                intent.putParcelableArrayListExtra(BundleConstants.PE_TEST_LIST_MODEL, peTestArraylist);
                intent.putExtra("IsAddBen", FlagADDEditBen);
                startActivityForResult(intent, BundleConstants.ADDEDITTESTREQUESTCODE);
            }
//fungible
//        } else if (!isAddBen && BundleConstants.isPEPartner) {
        } else if (!isAddBen && appPreferenceManager.isPEPartner()) {
//        } else if (!isAddBen && Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())) {
            if (peselectedTestsList != null && peselectedTestsList.size() > 0) {
                addEditTest(isAddBen);
            } else {
                test_edit_PE = true;
                Intent intent = new Intent(mActivity, AddRemoveTestProfileActivity.class);
                intent.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL, orderVisitDetailsModel);
                intent.putParcelableArrayListExtra(BundleConstants.PE_TEST_LIST_MODEL, peTestArraylist);
                /*intent.putParcelableArrayListExtra(BundleConstants.ADD_BEN_SELECTED_TESTLIST, selectedTestsList);
                intent.putParcelableArrayListExtra(BundleConstants.EDIT_BEN_SELECTED_TESTLIST, edit_selectedTestsList);*/
                intent.putExtra("IsAddBen", FlagADDEditBen);
                intent.putExtra(BundleConstants.TEST_EDIT_PE, test_edit_PE);
                intent.putExtra("IS_MALE", isM);
                startActivityForResult(intent, BundleConstants.ADDEDITTESTREQUESTCODE);
            }
        } else if (isAddBen && InputUtils.isNull(selectedTestsList)) {
            Intent intent = new Intent(mActivity, AddRemoveTestProfileActivity.class);
            intent.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL, orderVisitDetailsModel);
            intent.putParcelableArrayListExtra(BundleConstants.ADD_BEN_SELECTED_TESTLIST, selectedTestsList);
            intent.putParcelableArrayListExtra(BundleConstants.EDIT_BEN_SELECTED_TESTLIST, edit_selectedTestsList);
            intent.putExtra("IsAddBen", FlagADDEditBen);
            intent.putExtra("IS_MALE", isM);
            startActivityForResult(intent, BundleConstants.ADDEDITTESTREQUESTCODE);
        } else {
            addEditTest(isAddBen);

        }

    }

    private void addEditTest(final boolean isAddBen) {
        ArrayList<String> DisTest = new ArrayList<>();
        final int ArraySize;
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mActivity, R.style.BottomSheetTheme);
        View bottomSheet = LayoutInflater.from(mActivity).inflate(R.layout.addtestbottomsheet, (ViewGroup) mActivity.findViewById(R.id.bottom_sheet_dialog_parent));
        ListView lvTestsDisplay = (ListView) bottomSheet.findViewById(R.id.test_names);
        TextView close_btn = (TextView) bottomSheet.findViewById(R.id.close_btn);
        Button btn_addtest = bottomSheet.findViewById(R.id.btn_addtest);

        /*if (edit_selectedTestsList.size()==0 && !InputUtils.isNull(SelectedTestCode)){
            DisTest = new ArrayList<>();
            String[] str = SelectedTestCode.split(",");
            for (int i = 0; i < str.length; i++) {
                String str1 = str[i].trim();
                DisTest.add(str1);
            }
        }

        ArraySize = DisTest.size();*/

        try {
            if (isAddBen) {
                //fungible
//                if (BundleConstants.isPEPartner) {
                if (appPreferenceManager.isPEPartner()) {
//                if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())) {
                    displayAdapter = new DisplaySelectedTestsListForCancellationAdapter_new(mActivity, true, selectedTestsList, peselectedTestsList, SelectedTestCode, new RemoveSelectedTestFromListDelegate_new() {
                        @Override
                        public void onRemoveButtonClicked(ArrayList<TestRateMasterModel> selectedTests, String newDistest) {
                        }

                        @Override
                        public void onRemovePEButtonClicked(ArrayList<GetPETestResponseModel.DataDTO> peselectedTest) {
                            isTestEdit = true;
                            peselectedTestsList = peselectedTest;
                            displayAdapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    displayAdapter = new DisplaySelectedTestsListForCancellationAdapter_new(mActivity, true, selectedTestsList, peselectedTestsList, SelectedTestCode, new RemoveSelectedTestFromListDelegate_new() {
                        @Override
                        public void onRemoveButtonClicked(ArrayList<TestRateMasterModel> selectedTests, String newDistest) {
                            isTestEdit = true;
                            selectedTestsList = selectedTests;
                            displayAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onRemovePEButtonClicked(ArrayList<GetPETestResponseModel.DataDTO> peselectedTests) {

                        }
                    });

                }
                lvTestsDisplay.setAdapter(displayAdapter);

            } else {
                //fungible
//                if (BundleConstants.isPEPartner) {
                if (appPreferenceManager.isPEPartner()) {
//                if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())) {
                    displayAdapter = new DisplaySelectedTestsListForCancellationAdapter_new(mActivity, false, edit_selectedTestsList, peselectedTestsList, SelectedTestCode, new RemoveSelectedTestFromListDelegate_new() {
                        @Override
                        public void onRemoveButtonClicked(ArrayList<TestRateMasterModel> selectedTests, String newDistest) {

                        }

                        @Override
                        public void onRemovePEButtonClicked(ArrayList<GetPETestResponseModel.DataDTO> peselectedTests) {
                            isTestEdit = true;
                            peselectedTestsList = peselectedTests;
                            displayAdapter.notifyDataSetChanged();
                        }
                    });
                    lvTestsDisplay.setAdapter(displayAdapter);

                } else {
                    if (displayAdapter != null) {
                        displayAdapter = null;
                    }

                    ArrayList<String> newEdit = new ArrayList<>();
                    String test = "";
                    if (edit_selectedTestsList != null && edit_selectedTestsList.size() > 0) {
                        for (int i = 0; i < edit_selectedTestsList.size(); i++) {
                            if (!StringUtils.isNull(edit_selectedTestsList.get(i).getDescription())) {
                                test = edit_selectedTestsList.get(i).getDescription();
                            } else {
                                test = edit_selectedTestsList.get(i).getTestCode();
                            }
                            newEdit.add(test);
                        }
                        SelectedTestCode = TextUtils.join(",", newEdit);
                    }
                    displayAdapter = new DisplaySelectedTestsListForCancellationAdapter_new(mActivity, false, edit_selectedTestsList, peselectedTestsList, SelectedTestCode, new RemoveSelectedTestFromListDelegate_new() {
                        @Override
                        public void onRemoveButtonClicked(ArrayList<TestRateMasterModel> selectedTests, String newDistest) {
                            SelectedTestCode = newDistest;
                            isTestEdit = true;
                            edit_selectedTestsList = selectedTests;
                            displayAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onRemovePEButtonClicked(ArrayList<GetPETestResponseModel.DataDTO> peselectedTests) {

                        }
                    });
                    lvTestsDisplay.setAdapter(displayAdapter);
                }
            }
            Button btn_Save = (Button) bottomSheet.findViewById(R.id.btn_save);
            //fungible
//            if (BundleConstants.isPEPartner) {
            if (appPreferenceManager.isPEPartner()) {
//            if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())) {
                if (isAddBen) {
                    if (peselectedTestsList.size() != 0) {
                        btn_Save.setVisibility(View.VISIBLE);
                    } else {
                        btn_Save.setVisibility(View.GONE);
                    }
                }
            } else {
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
            }

            btn_Save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    editdialog.dismiss();
                    //fungible
                    if (appPreferenceManager.isPEPartner()) {
//                    if (BundleConstants.isPEPartner) {
//                    if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())) {
                        if (isAddBen) {
                            ToDisplayTest(bottomSheetDialog);
                        } else {
                            callPEOrderAPI = true;
                            ToDisplayTest(bottomSheetDialog);
                        }
                    } else {
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
                                    SelectedTest_TCPrice = 0;
                                    for (TestRateMasterModel btdm :
                                            selectedTestsList) {
                                        MessageLogger.PrintMsg("test:" + btdm.getTestCode());

                                        SelectedTest_TCPrice += btdm.getRate();
                                        txtAmountPayable.setText(mActivity.getString(R.string.rupee_symbol) + SelectedTest_TCPrice + "/-");

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
                                    System.out.println("Price to check>>>>>>>>>>: " + SelectedTest_TCPrice);

                                    //TODO Remove logic
                                /*if (isFBSpresent == true && FastingCount == 1 && !isFBSpresentInSuperSet) {

                                    Toast.makeText(mActivity, "With FBS atleast one fasting test should be there", Toast.LENGTH_SHORT).show();
                                } else if (isPPBSpresent && !isFBSpresent) {
                                    Toast.makeText(mActivity, "To Avail PPBS You have to select FBS test", Toast.LENGTH_SHORT).show();
                                } else if (isRBSpresent && !isFBSpresent) {
                                    Toast.makeText(mActivity, "To Avail RBS You have to select FBS test", Toast.LENGTH_SHORT).show();
                                } else */
                                    if (isINSPPpresent && !isINSFApresent) {
                                        Toast.makeText(mActivity, "To Avail INSPP You have to select INSFA test", Toast.LENGTH_SHORT).show();
                                    } else {

                                        bottomSheetDialog.dismiss();
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
                                        txtTestsList.setSelected(true);
                                        txtTestsList.setError(null);
                                        CallCartAPIForAdd(selectedTestsList);
                                        cv_couponmain.setVisibility(View.VISIBLE);
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

                                /*if (isFBSpresent == true && FastingCount == 1 && !isFBSpresentInSuperSet) {
                                    Toast.makeText(mActivity, "With FBS atleast one fasting test should be there", Toast.LENGTH_SHORT).show();
                                } else if (isPPBSpresent && !isFBSpresent) {
                                    Toast.makeText(mActivity, "To Avail PPBS You have to select FBS test", Toast.LENGTH_SHORT).show();
                                } else if (isRBSpresent && !isFBSpresent) {
                                    Toast.makeText(mActivity, "To Avail RBS You have to select FBS test", Toast.LENGTH_SHORT).show();
                                } else*/
                                    if (isINSPPpresent && !isINSFApresent) {
                                        Toast.makeText(mActivity, "To Avail INSPP You have to select INSFA test", Toast.LENGTH_SHORT).show();
                                    } else {
                                        bottomSheetDialog.dismiss();
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
                                        txtTestsList.setSelected(true);
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
                                        AddRemoveTestAPIFlag = 1;
                                        btnOrderSubmit.performClick();
                                    }
                                } else {
                                    globalclass.showCustomToast(mActivity, "Please select atleast one test");
                                }
                            }
                        }
                    }
                }
            });

            btn_addtest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //fungible
//                    if (BundleConstants.isPEPartner) {
                    if (appPreferenceManager.isPEPartner()) {
//                    if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())) {
                        bottomSheetDialog.dismiss();
                        Intent intent = new Intent(mActivity, AddRemoveTestProfileActivity.class);
                        intent.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL, orderVisitDetailsModel);
                        intent.putParcelableArrayListExtra(BundleConstants.ADD_BEN_SELECTED_TESTLISTPE, peselectedTestsList);
//                        intent.putParcelableArrayListExtra(BundleConstants.EDIT_BEN_SELECTED_TESTLIST, edit_selectedTestsList);
                        intent.putExtra("IsAddBen", FlagADDEditBen);
                        intent.putExtra("IS_MALE", isM);
                        startActivityForResult(intent, BundleConstants.ADDEDITTESTREQUESTCODE);
                    } else {
                        bottomSheetDialog.dismiss();
                        Intent intent = new Intent(mActivity, AddRemoveTestProfileActivity.class);
                        intent.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL, orderVisitDetailsModel);
                        intent.putParcelableArrayListExtra(BundleConstants.ADD_BEN_SELECTED_TESTLIST, selectedTestsList);
                        intent.putParcelableArrayListExtra(BundleConstants.EDIT_BEN_SELECTED_TESTLIST, edit_selectedTestsList);
                        intent.putExtra(BundleConstants.EDITSELECTEDTEST, SelectedTestCode);
                        //  intent.putExtra("ArraySize", ArraySize);
                        intent.putExtra("IsAddBen", FlagADDEditBen);
                        intent.putExtra("IS_MALE", isM);
                        startActivityForResult(intent, BundleConstants.ADDEDITTESTREQUESTCODE);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
    }

    private void ToDisplayTest(BottomSheetDialog bottomSheetDialog) {
        onRemoveCoupon();
        if (peselectedTestsList.size() != 0) {
            bottomSheetDialog.dismiss();
            String str = "";
            for (int i = 0; i < peselectedTestsList.size(); i++) {
                str = str + peselectedTestsList.get(i).getPartner_lab_test_id() + ",";
            }
            str = str.substring(0, str.length() - 1);
            txtTestsList.setError(null);
            txtTestsList.setText("" + str);
            txtTestsList.setSelected(true);
            txtTestsList.setError(null);
            showAmount(peselectedTestsList);
//        CallCartAPIForAdd(peselectedTestsList);
        } else {
            bottomSheetDialog.dismiss();
            txtTestsList.setText(R.string.add_edit_test_list_message);
            cv_couponmain.setVisibility(View.GONE);
        }
    }

    private void showAmount(ArrayList<GetPETestResponseModel.DataDTO> peselectedTestsList) {
        int orderAmountPayable = 0;
        orderAmountDue = 0;
        int tempAmount = 0;
        orderAmountPayable = orderAmountPayable + orderVisitDetailsModel.getAllOrderdetails().get(0).getAmountDue();

        for (int j = 0; j < peselectedTestsList.size(); j++) {
            orderAmountDue = orderAmountDue + Integer.parseInt(peselectedTestsList.get(j).getPrice());
        }
        /*orderAmountDue =  orderAmountPayable-orderAmountDue;
        tempAmount = orderAmountDue+orderVisitDetailsModel.getAllOrderdetails().get(0).getAmountDue();
        orderAmountDue=tempAmount;*/
//        orderAmountDue = orderAmountDue + orderAmountPayable;
//            ll_amt.setVisibility(View.VISIBLE);
        txtAmountPayable.setVisibility(View.VISIBLE);

        txtAmountPayable.setText(mActivity.getResources().getString(R.string.rupee_symbol) + orderAmountDue + "/-");
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
            CallTechsoCartAPI(cartAPIRequestModel, null);
        } else {
            globalclass.showCustomToast(mActivity, CheckInternetConnectionMsg);
        }
    }

    private void CallCartAPI(OrderBookingRequestModel selectedTestsList) {
        CartAPIRequestModel cartAPIRequestModel = new CartAPIRequestModel();
        ArrayList<CartRequestOrderModel> ordersArr = new ArrayList<>();
        ArrayList<CartRequestBeneficiaryModel> beneficiariesArr = new ArrayList<>();
        cartAPIRequestModel.setVisitId(selectedTestsList.getOrdbooking().getVisitId());
        cartAPIRequestModel.setDisAmtDue(InputUtils.isNull(SelectedCoupon) ? 0 : discountAmount);


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
            if (FlagADDEditBen) {
                CallApiForSubMitBeforeCartAPI(cartAPIResponseModelforsubmitFirtsTime, cartAPIRequestModel);
            } else {
//                CallApiForSubMitBeforeCartAPI(cartAPIResponseModelforsubmitFirtsTime, cartAPIRequestModel);
                CallTechsoCartAPI(cartAPIRequestModel, null);
            }

//

        } else {
            globalclass.showCustomToast(mActivity, CheckInternetConnectionMsg);
        }
    }

    private void CallTechsoOrderbookingAPIFirst(OrderBookingRequestModel finalSubmitDataModel, final CartAPIRequestModel cartAPIRequestModel) {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<OrderBookingResponseVisitModel> responseCall = apiInterface.CallTechsoOrderBookingAPIFirst("Bearer " + appPreferenceManager.getLoginResponseModel().getAccess_token(), finalSubmitDataModel);
        globalclass.showProgressDialog(mActivity, "Please wait..");
        responseCall.enqueue(new Callback<OrderBookingResponseVisitModel>() {
            @Override
            public void onResponse(Call<OrderBookingResponseVisitModel> call, retrofit2.Response<OrderBookingResponseVisitModel> response) {
                globalclass.hideProgressDialog(mActivity);
                if (response.code() == 200) {
                    OrderBookingResponseVisitModel orderBookingResponseVisitModel;
                    orderBookingResponseVisitModel = response.body();

                    globalclass.showCustomToast(mActivity, "SUCCESS");
                    // TODo code to redirect to Arrive Screen
//                    setResult(Activity.RESULT_OK);
//                    finish();

                    CallTechsoCartAPI(cartAPIRequestModel, orderBookingResponseVisitModel);
                } else {

                    globalclass.showCustomToast(mActivity, SomethingWentwrngMsg);
                }
            }

            @Override
            public void onFailure(Call<OrderBookingResponseVisitModel> call, Throwable t) {
                globalclass.hideProgressDialog(mActivity);
                globalclass.showCustomToast(mActivity, SomethingWentwrngMsg);
            }
        });
    }

    private void CallTechsoCartAPI(final CartAPIRequestModel cartAPIRequestModel, final OrderBookingResponseVisitModel orderBookingResponseVisitModel) {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<CartAPIResponseModel> responseCall = apiInterface.CallTechsoCartAPI("Bearer " + appPreferenceManager.getLoginResponseModel().getAccess_token(), cartAPIRequestModel);
        globalclass.showProgressDialog(mActivity, "Please wait..");
        responseCall.enqueue(new Callback<CartAPIResponseModel>() {
            @Override
            public void onResponse(Call<CartAPIResponseModel> call, retrofit2.Response<CartAPIResponseModel> response) {
                globalclass.hideProgressDialog(mActivity);
                if (response.isSuccessful() && response.body() != null) {
                    CartAPIResponseModel cartAPIResponseModel = response.body();
                    cartAPIResponseModelforsubmitFirtsTime = response.body();
                    onTechsoCartAPIResponseReceived(cartAPIResponseModel, orderBookingResponseVisitModel);
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

    private void onTechsoCartAPIResponseReceived(CartAPIResponseModel cartAPIResponseModel, OrderBookingResponseVisitModel orderBookingResponseVisitModel) {

        try {
            if (AddBenCartFlag == 1) {
                AddBenCartFlag = 0;
                ShowdatainAmount(cartAPIResponseModel);
            } else {
                if (AddRemoveTestAPIFlag == 0 && CallCartAPIFlag == 1) {
//                    CallCartAPIFlag = 0;
                    ShowdatainAmount(cartAPIResponseModel);
                    CallApiForSubMitAfterCartAPI(cartAPIResponseModel, orderBookingResponseVisitModel);
                } else if (CallCartAPIFlag == 1 && AddRemoveTestAPIFlag == 1) {
                    CallCartAPIFlag = 0;
                    AddRemoveTestAPIFlag = 0;
                    ShowdatainAmount(cartAPIResponseModel);
                } else {
                    CallApiForSubMitAfterCartAPI(cartAPIResponseModel, orderBookingResponseVisitModel);
                }

               /*if (!FlagADDEditBen && CallCartAPIFlag ==1){
                   CallCartAPIFlag = 0;
                   ShowdatainAmount(cartAPIResponseModel);
                   CallApiForSubMitAfterCartAPI(cartAPIResponseModel);
               } else if (CallCartAPIFlag == 1) {
                    CallCartAPIFlag = 0;
                    ShowdatainAmount(cartAPIResponseModel);
                }else {
                    CallApiForSubMitAfterCartAPI(cartAPIResponseModel);
                }*/
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
            finalOrderAmount = orderAmountDue;
//            ll_amt.setVisibility(View.VISIBLE);
            txtAmountPayable.setVisibility(View.VISIBLE);

            txtAmountPayable.setText(FlagADDEditBen ? mActivity.getResources().getString(R.string.rupee_symbol) + SelectedTest_TCPrice + "/-" : mActivity.getResources().getString(R.string.rupee_symbol) + orderAmountDue + "/-");
            if (cartAPIResponseModel.getOrders() != null && cartAPIResponseModel.getOrders().size() > 0 && cartAPIResponseModel.getOrders().get(0).isHC()) {
//                relHardCopyCharge.setVisibility(View.VISIBLE);
                int PriceWithoutHardCopy = orderAmountDue - HARDCOPY_CHARGES;
//                txtHardCopyCharge.setText("Rs. " + HARDCOPY_CHARGES + "/-");
//                txtActPrice.setText("Rs. " + PriceWithoutHardCopy + "/-");
            } else {
//                relHardCopyCharge.setVisibility(View.GONE);
//                txtActPrice.setText("Rs. " + orderAmountDue + "/-");
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

    private void CallSubMitAPIAdd(OrderBookingRequestModel nm) {
        if (FinalSubmitDataModel != null) {
            FinalSubmitDataModel = null;
        }
        FinalSubmitDataModel = nm;
        if (cd.isConnectingToInternet()) {
//            CallTechsoOrderbookingAPIFirst(FinalSubmitDataModel);
        } else {
            globalclass.showCustomToast(mActivity, CheckInternetConnectionMsg);
        }
//        CallCartAPI(nm);
    }

    private void callSubmitAPIAfterBookingOrder(final String orderNo, final int finalBenId) {
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
        odModel.setAddBen(false);
        odModel.setTestEdit(false);
        odModel.setEditOrder(false);

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

//        CallSubMitAPIAdd(nm, orderNo, finalBenId);
        CallSubMitAPI(nm);
    }

    private void CallApiForSubMitAfterCartAPI(CartAPIResponseModel cartAPIResponseModel, OrderBookingResponseVisitModel orderBookingResponseVisitModel) {

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

            //TODO Mith for rate calculation issues

            if (orderBookingResponseVisitModel != null) {
                for (int i = 0; i < FinalSubmitDataModel.getBendtl().size(); i++) {
                    for (int k = 0; k < orderBookingResponseVisitModel.getOrderids().size(); k++) {
                        for (int l = 0; l < orderBookingResponseVisitModel.getOrderids().get(k).getBenfids().size(); l++) {
                            if (FinalSubmitDataModel.getBendtl().get(i).isAddBen() && Integer.parseInt(orderBookingResponseVisitModel.getOrderids().get(k).getBenfids().get(l).getOldBenIds()) != Integer.parseInt(orderBookingResponseVisitModel.getOrderids().get(k).getBenfids().get(l).getNewBenIds())) {
                                FinalSubmitDataModel.getBendtl().get(i).setBenId(Integer.parseInt(orderBookingResponseVisitModel.getOrderids().get(k).getBenfids().get(l).getNewBenIds()));
                                for (int j = 0; j < FinalSubmitDataModel.getBendtl().get(i).getSampleType().size(); j++) {
                                    FinalSubmitDataModel.getBendtl().get(i).getSampleType().get(j).setBenId(Integer.parseInt(orderBookingResponseVisitModel.getOrderids().get(k).getBenfids().get(l).getNewBenIds()));
                                }
                            }
                        }
                    }
                }

                for (int i = 0; i < FinalSubmitDataModel.getBendtl().size(); i++) {
                    //obrom.getBendtl().get(i).setAddBen(false);
                    FinalSubmitDataModel.getBendtl().get(i).setAddBen(false);
                    FinalSubmitDataModel.getBendtl().get(i).setTestEdit(true);
                    //Mith
                }
            } else {
                for (int j = 0; j < FinalSubmitDataModel.getBendtl().size(); j++) {
                    FinalSubmitDataModel.getBendtl().get(j).setAddBen(false);
                }
            }


        }

        if (cd.isConnectingToInternet()) {
            CallTechsoOrderbookingAPI(FinalSubmitDataModel);

        } else {
            globalclass.showCustomToast(mActivity, CheckInternetConnectionMsg);
        }
    }

    private void CallApiForSubMitBeforeCartAPI(CartAPIResponseModel cartAPIResponseModel, CartAPIRequestModel cartAPIRequestModel) {

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
//            CallTechsoOrderbookingAPI(FinalSubmitDataModel);
            CallTechsoOrderbookingAPIFirst(FinalSubmitDataModel, cartAPIRequestModel);

        } else {
            globalclass.showCustomToast(mActivity, CheckInternetConnectionMsg);
        }
    }

    private void CallTechsoOrderbookingAPI(OrderBookingRequestModel benModelaschc) {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallTechsoOrderBookingAPI("Bearer " + appPreferenceManager.getLoginResponseModel().getAccess_token(), benModelaschc);
        globalclass.showProgressDialog(mActivity, "Please wait..");
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                globalclass.hideProgressDialog(mActivity);
                if (response.code() == 200) {
                    Toast.makeText(mActivity, "SUCCESS", Toast.LENGTH_SHORT).show();
                    new LogUserActivityTagging(mActivity, BundleConstants.WOE, "Order Booking");
                    if (FlagADDEditBen) {
                        Global.callAPIEditOrder = true;
                        setResult(Activity.RESULT_OK);
                        finish();
                    } else if (CallCartAPIFlag == 1) {
                        CallCartAPIFlag = 0;
                    } else if (CallCartAPIFlag == 0) {
                        Global.callAPIEditOrder = true;
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
//                    globalclass.showCustomToast(mActivity, "SUCCESS");
                    // TODo code to redirect to Arrive Screen

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
                            peTestRates = data.getStringExtra(BundleConstants.PERATES);
                            peTestRates = data.getExtras().getString(BundleConstants.PERATES);
                            System.out.println("Mith>>>>>>" + data.getExtras().getString(BundleConstants.PERATES));
                            System.out.println("Mith>>>>>>" + data);
                            txtAmountPayable.setVisibility(View.VISIBLE);
                            if (!StringUtils.isNull(peTestRates)) {
                                txtAmountPayable.setText(mActivity.getResources().getString(R.string.rupee_symbol) + peTestRates + "/-");
                            }
                            peselectedTestsList = data.getParcelableArrayListExtra(BundleConstants.ADD_BEN_SELECTED_TESTLISTPE);
                            selectedTestsList = data.getParcelableArrayListExtra(BundleConstants.ADD_BEN_SELECTED_TESTLIST);
                            edit_selectedTestsList = data.getParcelableArrayListExtra(BundleConstants.EDIT_BEN_SELECTED_TESTLIST);

                            if ((peselectedTestsList.size() > 0 && peselectedTestsList != null)
                                    && FlagADDEditBen //TODO checks if we are editing order..
                            ) {
                                cv_couponmain.setVisibility(View.VISIBLE);
                            } else {
                                cv_couponmain.setVisibility(View.GONE);

                            }
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

    public void getTestList(GetPETestResponseModel getPETestResponseModel) {

        if (getPETestResponseModel != null) {
            ArrayList<GetPETestResponseModel.DataDTO> test = new ArrayList<>();
            peTestArraylist = new ArrayList<GetPETestResponseModel.DataDTO>();
            test = getPETestResponseModel.getData();
            for (int i = 0; i < test.size(); i++) {
                if (InputUtils.isNull(test.get(i).getPartner_lab_test_id())) {
                    test.remove(i);
                }
            }
            peTestArraylist = test;
        } else {
            TastyToast.makeText(mActivity, !InputUtils.isNull(getPETestResponseModel.getError()) ? getPETestResponseModel.getError() : SomethingWentwrngMsg, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
        }



        /*if (getPETestResponseModel != null) {
            if (getPETestResponseModel.getStatus() == 1) {
                peTestArraylist = getPETestResponseModel.getData();
            }
        }
*/
        CallTestDataPE(peTestArraylist);
        /*if (FlagADDEditBen && peselectedTestsList!=null || peselectedTestsList!=null){

        }*/
    }

    private void CallTestDataPE(ArrayList<GetPETestResponseModel.DataDTO> data) {
        if (data != null) {
            if (!InputUtils.isNull(SelectedTestCode)) {
                String[] str = SelectedTestCode.split(",");

                //edit_selectedTestsListPE = new ArrayList<>();
                peselectedTestsList = new ArrayList<>();

                for (String s : str) {
                    for (int j = 0; j < data.size(); j++) {
                        if (s.trim().equalsIgnoreCase(data.get(j).getName().trim()) ||
                                s.equalsIgnoreCase(data.get(j).getPartner_lab_test_id().trim()) ||
                                s.equalsIgnoreCase(data.get(j).getLab_dos_name().trim())) {
                            peselectedTestsList.add(data.get(j));
                            break;
                        }
                    }
                }
            }

        }
    }

    public void getDSAProducts(DSAProductsResponseModel dsaProductsResponseModel) {
        dsaProductsResponseModelfinal = new DSAProductsResponseModel();
        dsaProductsResponseModelfinal = dsaProductsResponseModel;
        CallAPIFORTESTLIST(FlagADDEditBen);
    }

    public void pePatientDetailsUpdated() {
        BundleConstants.PE_HARD_BLOCKING = true;
        setResult(Activity.RESULT_OK);
        finish();
    }

    public void getCouponsResponse(CouponCodeResponseModel responseModel) {
        if (responseModel != null) {
            couponCodeResponseModel = responseModel;
        } else {
            couponCodeResponseModel = null;
        }
        bottomSheet_Coupon_Dialog = new BottomSheetDialog(mActivity, R.style.BottomSheetTheme);
        final View bottomSheet = LayoutInflater.from(mActivity).inflate(R.layout.layout_btms_coupon, null);
        bottomSheet_Coupon_Dialog.setContentView(bottomSheet);
        bottomSheet_Coupon_Dialog.setCancelable(false);

        ImageView img_close = bottomSheet.findViewById(R.id.img_close);
        final EditText edt_couponcode = bottomSheet.findViewById(R.id.edt_couponcode);
        TextView tv_applycoupon = bottomSheet.findViewById(R.id.tv_applycoupon);
        TextView tv_nocoupon = bottomSheet.findViewById(R.id.tv_nocoupon);
        RecyclerView rcl_coupons = bottomSheet.findViewById(R.id.rcl_coupons);
        LinearLayout ll_enterCoupon = bottomSheet.findViewById(R.id.ll_enterCoupon);
        edt_couponcode.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        if (!appPreferenceManager.isPEPartner()) {
            ll_enterCoupon.setVisibility(View.GONE);
        }
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        manager.setOrientation(RecyclerView.VERTICAL);
        rcl_coupons.setLayoutManager(manager);

        if (couponCodeResponseModel.getData().size() > 0) {
            rcl_coupons.setVisibility(View.VISIBLE);
            tv_nocoupon.setVisibility(View.GONE);
            bindCouponCodeAdapter(rcl_coupons, couponCodeResponseModel);
        } else {
            rcl_coupons.setVisibility(View.GONE);
            tv_nocoupon.setVisibility(View.VISIBLE);
        }


        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet_Coupon_Dialog.dismiss();
            }
        });

        tv_applycoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateEditCoupon(edt_couponcode)) {
                    //TODO verfiy entered coupon
                    getClickedCoupon(edt_couponcode.getText().toString());
                } else {
                    edt_couponcode.requestFocus();
                    Global.showCustomStaticToast(mActivity, "Please enter coupon code");

                }
            }
        });

        bottomSheet_Coupon_Dialog.show();

    }

    public void getClickedCoupon(String selectedCouponCode) {
        //TODO validate couponcode api call
        if (appPreferenceManager.isPEPartner()) {

            ArrayList<AddONRequestModel.test> arrTest = new ArrayList<>();
            AddONRequestModel.patient patient = new AddONRequestModel.patient();
            patient.setId(0);
            patient.setName(edtBenName.getText().toString());
            patient.setAge(Integer.parseInt("" + edtBenAge.getText().toString()));
            patient.setGender(isM ? "Male" : "Female");

            for (int i = 0; i < peselectedTestsList.size(); i++) {
                AddONRequestModel.test test = new AddONRequestModel.test();
                test.setId(peselectedTestsList.get(i).getTest_dos_id());
                test.setType(peselectedTestsList.get(i).getType());
                //TODO added as per PE request for coupon task.
                test.setTest_id(peselectedTestsList.get(i).getId());
                arrTest.add(test);
            }
            AddONRequestModel addONRequestModel = new AddONRequestModel();
            addONRequestModel.setPatient(patient);
            addONRequestModel.setTest(arrTest);
            addONRequestModel.setPromocode(selectedCouponCode);
            addONRequestModel.setDos_order_id(orderNo);

            AddEditBenificaryController controller = new AddEditBenificaryController(AddEditBenificaryActivity.this, appPreferenceManager.isPEPartner(), addONRequestModel, orderNo);
            controller.callValidatePECouponCodeApi();
        }

    }

    public void getCouponValidateResponse(VerifyCouponResponseModel response) {

        if (response.getIs_coupon_applied()) {
            if (bottomSheet_Coupon_Dialog.isShowing())
                bottomSheet_Coupon_Dialog.dismiss();
            SelectedCoupon = response.getCode();
            txt_coupons.setText(response.getCode());
            rl_priceView.setVisibility(View.VISIBLE);
            txt_ViewRemove_switch.setText("Remove");
            cv_couponmain.setClickable(false);
            double finalprice = response.getData().getTests_pe_selling_price() - response.getData().getTests_discount();

            tv_couponDiscounttoshow.setText(mActivity.getResources().getString(R.string.rupee_symbol) + response.getData().getTests_discount().toString() + "/-");
            tv_orderTotaltoshow.setText(mActivity.getResources().getString(R.string.rupee_symbol) + finalprice + "/-");
            tv_cartPricetoshow.setText(mActivity.getResources().getString(R.string.rupee_symbol) + response.getData().getTests_pe_selling_price().toString() + "/-");
            txtAmountPayable.setText("");
            TastyToast.makeText(mActivity, response.getMessage() + SelectedCoupon, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

        } else {
            txt_coupons.setText("Apply Coupon");
            txt_ViewRemove_switch.setText("View Offers");
            rl_priceView.setVisibility(View.GONE);
            SelectedCoupon = null;
            txtAmountPayable.setText(mActivity.getString(R.string.rupee_symbol) + peTestRates + "/-");
            TastyToast.makeText(mActivity, response.getError(), TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
        }

    }
}