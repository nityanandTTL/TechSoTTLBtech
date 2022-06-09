package com.thyrocare.btechapp.NewScreenDesigns.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mindorks.paracamera.Camera;
import com.thyrocare.btechapp.Controller.BottomSheetController;
import com.thyrocare.btechapp.Controller.GetCollectionCenterController;
import com.thyrocare.btechapp.Controller.GetTestController;
import com.thyrocare.btechapp.Controller.PayTypeController;
import com.thyrocare.btechapp.Controller.SendLatLongforOrderController;
import com.thyrocare.btechapp.Controller.UploadSelfieWOEController;
import com.thyrocare.btechapp.Controller.WOEController;
import com.thyrocare.btechapp.NewScreenDesigns.Adapters.CheckoutWoeAdapter;
import com.thyrocare.btechapp.NewScreenDesigns.Controllers.GetAcessTokenAndOTPAPIController;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.WOEOtpValidationRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.CommonPOSTResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.LogUserActivityTagging;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.HomeScreenActivity;
import com.thyrocare.btechapp.activity.KIOSK_Scanner_Activity;
import com.thyrocare.btechapp.activity.PaymentsActivity;

import application.ApplicationController;

import com.thyrocare.btechapp.dao.utils.ConnectionDetector;
import com.thyrocare.btechapp.models.api.request.BTechSelfieRequestModel;
import com.thyrocare.btechapp.models.api.request.GetTestCodeRequestModel;
import com.thyrocare.btechapp.models.api.request.OrderBookingRequestModel;
import com.thyrocare.btechapp.models.api.request.PaytypeRequestModel;
import com.thyrocare.btechapp.models.api.response.FetchOrderDetailsResponseModel;
import com.thyrocare.btechapp.models.api.response.GetCollectionReqModel;
import com.thyrocare.btechapp.models.api.response.GetCollectionRespModel;
import com.thyrocare.btechapp.models.api.response.GetTestResponseModel;
import com.thyrocare.btechapp.models.api.response.OrderBookingResponseBeneficiaryModel;
import com.thyrocare.btechapp.models.api.response.OrderBookingResponseOrderModel;
import com.thyrocare.btechapp.models.api.response.OrderBookingResponseVisitModel;
import com.thyrocare.btechapp.models.api.response.PaytypeResponseModel;
import com.thyrocare.btechapp.models.data.BeneficiaryBarcodeDetailsModel;
import com.thyrocare.btechapp.models.data.BeneficiaryDetailsModel;
import com.thyrocare.btechapp.models.data.BeneficiaryLabAlertsModel;
import com.thyrocare.btechapp.models.data.BeneficiarySampleTypeDetailsModel;
import com.thyrocare.btechapp.models.data.BeneficiaryTestWiseClinicalHistoryModel;
import com.thyrocare.btechapp.models.data.OrderBookingDetailsModel;
import com.thyrocare.btechapp.models.data.OrderDetailsModel;
import com.thyrocare.btechapp.models.data.OrderVisitDetailsModel;


import com.thyrocare.btechapp.network.MyBroadcastReceiver;
import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.AppConstants;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.CommonUtils;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import org.json.JSONObject;

import java.io.File;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.LENGTH_SHORT;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.CheckInternetConnectionMsg;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.PLEASE_WAIT;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SOMETHING_WENT_WRONG;


public class CheckoutWoeActivity extends AppCompatActivity {

    Activity mActivity;
    Global globalclass;
    ConnectionDetector cd;
    AppPreferenceManager appPreferenceManager;
    private OrderVisitDetailsModel orderVisitDetailsModel;
    ArrayList<BeneficiaryDetailsModel> beneficaryWiseArylst;
    private LinearLayout lin_bottom;
    private TextView txt_amount;
    private TextView txtNoRecord;
    private TextView btn_Pay;
    private RecyclerView recyle_OrderDetailWithBarcode;
    private CheckoutWoeAdapter checkoutWoeAdapter;
    private int PaymentMode;
    private String datefrom_model = "", newTimeaddTwoHrs = "", newTimeaddTwoHalfHrs = "";
    private String test = "";
    private OrderBookingResponseVisitModel orderBookingResponseVisitModel = new OrderBookingResponseVisitModel();
    private ArrayList<OrderBookingResponseBeneficiaryModel> orderBookingResponseBeneficiaryModelArr = new ArrayList<>();
    private int totalAmountPayable = 0;
    private String[] paymentItems;
    private String OrderMode = "";
    private boolean isOnlyWOE = false;
    private boolean isOnlyDigital = false;
    private Dialog CustomDialogfor_WOE_OTPValidation;
    private Button btn_MobileGetOTP, btn_MobileVerifyOTP, btn_MobileVerified, btn_EmailGetOTP, btn_EmailVerifyOTP, btn_EmailVerified;
    private boolean isMobilenoOTPVerfied = false, isEmailIDOTPVerfied = false;
    private EditText edt_mobileOTP, edt_EmailOTP;
    private TextView tv_reSendMobileOTP, tv_reSendEmailOTP;
    private CountDownTimer MobileResendOTPcdTimer, EmailResendOTPcdTimer;
    Spinner spn_collection;
    private ArrayList<GetCollectionRespModel.CenterListDTO> getcollearray;
    private String address;
    private Boolean canSubmit = false;
    private Camera camera;
    private File selfie_photo;
    private TextView btn_capture_photo;
    private TextView tv_view_photo;
    private Uri uri;
    ArrayList<String> testList;
    TextView tv_note;
    LinearLayout ll_Note;
    String processLocation;
    TextView tv_pay;
    TextView tv_toolbar;
    ImageView iv_back, iv_home;
    String strPaytype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_wow);
        mActivity = CheckoutWoeActivity.this;
        globalclass = new Global(mActivity);
        cd = new ConnectionDetector(mActivity);
        appPreferenceManager = new AppPreferenceManager(mActivity);
        orderVisitDetailsModel = getIntent().getExtras().getParcelable(BundleConstants.VISIT_ORDER_DETAILS_MODEL);
        beneficaryWiseArylst = getIntent().getExtras().getParcelableArrayList(BundleConstants.BENEFICIARY_DETAILS_MODEL);


        TrimTheNameOfCustomers();
        insertBase64BarcodeImageInMainModel();
        initView();
        initData();
        initListener();


        //fungible
        if (BundleConstants.isPEPartner) {
//        if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())) {
            if (BundleConstants.setPEOrderEdit) {
                fetchData();
            }
        }
//        initToolBar();

//        if (!BuildConfig.DEBUG) {
        CallTestColorAPI();
//        }
    }

    private void fetchData() {
        if (cd.isConnectingToInternet()) {
            CallOrderDetailAPI();
        } else {
            globalclass.showCustomToast(mActivity, CheckInternetConnectionMsg, Toast.LENGTH_LONG);
        }
    }

    private void CallOrderDetailAPI() {

        try {
            GetAPIInterface getAPIInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
//                    Call<FetchOrderDetailsResponseModel> fetchOrderDetailsResponseModelCall = getAPIInterface.getAllVisitDetails(appPreferenceManager.getLoginResponseModel().getUserID());
            Call<FetchOrderDetailsResponseModel> fetchOrderDetailsResponseModelCall = getAPIInterface.getOrderDetail(BundleConstants.setSelectedOrder.trim());
            globalclass.showProgressDialog(mActivity, mActivity.getResources().getString(R.string.fetchingOrders), false);
            fetchOrderDetailsResponseModelCall.enqueue(new Callback<FetchOrderDetailsResponseModel>() {
                @Override
                public void onResponse(Call<FetchOrderDetailsResponseModel> call, Response<FetchOrderDetailsResponseModel> response) {
                    globalclass.hideProgressDialog(mActivity);
                    FetchOrderDetailsResponseModel fetchOrderDetailsResponseModel = response.body();
                    if (fetchOrderDetailsResponseModel != null && fetchOrderDetailsResponseModel.getOrderVisitDetails() != null && fetchOrderDetailsResponseModel.getOrderVisitDetails().size() > 0) {
                        totalAmountPayable = 0;
                        orderVisitDetailsModel.getAllOrderdetails().get(0).setAmountDue(fetchOrderDetailsResponseModel.getOrderVisitDetails().get(0).getAllOrderdetails().get(0).getAmountDue());
                        orderVisitDetailsModel.getAllOrderdetails().get(0).setAmountPayable(fetchOrderDetailsResponseModel.getOrderVisitDetails().get(0).getAllOrderdetails().get(0).getAmountPayable());
                        initData();
                        /*orderVisitDetailsModel = fetchOrderDetailsResponseModel.getOrderVisitDetails().get(0);
                        if ( !InputUtils.isNull(orderVisitDetailsModel.getAllOrderdetails().get(0).getAmountDue()) && orderVisitDetailsModel.getAllOrderdetails().get(0).getAmountDue() != 0) {
                            totalAmountPayable = orderVisitDetailsModel.getAllOrderdetails().get(0).getAmountDue();
                            tv_pay.setText(mActivity.getResources().getString(R.string.rupee_symbol) + " " + totalAmountPayable + "/-");
                        } else {
                            tv_pay.setText("Paid");
                        }*/
                    }
                }

                @Override
                public void onFailure(Call<FetchOrderDetailsResponseModel> call, Throwable t) {
                    globalclass.hideProgressDialog(mActivity);
                    globalclass.showCustomToast(mActivity, SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT);
                }
            });
        } catch (Exception e) {
            globalclass.hideProgressDialog(mActivity);
            e.printStackTrace();
            globalclass.showCustomToast(mActivity, SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT);
        }
    }

    private void CallTestColorAPI() {
        try {
            if (cd.isConnectingToInternet()) {

                testList = new ArrayList<>();
                HashSet<String> removediplicate = new HashSet<>();
                if (!InputUtils.isNull(beneficaryWiseArylst)) {
                    for (int i = 0; i < beneficaryWiseArylst.size(); i++) {
                        if (!InputUtils.isNull(beneficaryWiseArylst.get(i).getTestsCode())) {
                            testList.add(beneficaryWiseArylst.get(i).getTestsCode());
                        }
                    }
                }
                removediplicate.addAll(testList);
                testList.clear();
                testList.addAll(removediplicate);

                String Tests = TextUtils.join(",", testList);

                GetTestCodeRequestModel requestModel = new GetTestCodeRequestModel();
                requestModel.setTest(Tests);
                requestModel.setTSP("" + appPreferenceManager.getLoginResponseModel().getUserID());
                GetTestController getTestController = new GetTestController(this);
                getTestController.getTest(requestModel);
            } else {
                Global.showCustomStaticToast(mActivity, SOMETHING_WENT_WRONG);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void TrimTheNameOfCustomers() {
        if (orderVisitDetailsModel != null && orderVisitDetailsModel.getAllOrderdetails() != null && orderVisitDetailsModel.getAllOrderdetails().size() > 0 && orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster() != null) {
            for (int i = 0; i < orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().size(); i++) {
                String strname = !InputUtils.isNull(orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(i).getName()) ? orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(i).getName().trim() : "";
                orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(i).setName(strname);
            }
        }

        if (beneficaryWiseArylst != null && beneficaryWiseArylst.size() > 0) {
            for (int i = 0; i < beneficaryWiseArylst.size(); i++) {
                String strname = !InputUtils.isNull(beneficaryWiseArylst.get(i).getName()) ? beneficaryWiseArylst.get(i).getName().trim() : "";
                beneficaryWiseArylst.get(i).setName(strname);
            }
        }
    }

    private void insertBase64BarcodeImageInMainModel() {
        if (orderVisitDetailsModel != null && orderVisitDetailsModel.getAllOrderdetails() != null && orderVisitDetailsModel.getAllOrderdetails().size() > 0) {
            if (orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster() != null && orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().size() > 0) {
                for (int i = 0; i < orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().size(); i++) {
                    test = test + orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(i).getTestsCode();
                    String VenupuntureFilePath = orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(i).getVenepuncture();
                    Bitmap bitmap = BitmapFactory.decodeFile(VenupuntureFilePath);
                    String encodedVanipunctureImg = CommonUtils.encodeImage(bitmap);
//                    orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(i).setVenepuncture(encodedVanipunctureImg);
                }
            }
        }
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarStockAvailablity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.blank_menu_screen, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(mActivity).create();
        alertDialog.setMessage("All changes made will be reset.\nAre you sure, you want to go back ?");
        alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        backpressed();
                        //finish();
                    }
                });
        alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

    }

    private void initView() {

        lin_bottom = (LinearLayout) findViewById(R.id.lin_bottom);
        txt_amount = (TextView) findViewById(R.id.txt_amount);
        txtNoRecord = (TextView) findViewById(R.id.txtNoRecord);
        btn_Pay = findViewById(R.id.btn_Pay);
        spn_collection = (Spinner) findViewById(R.id.spn_collection);
        btn_capture_photo = findViewById(R.id.btn_capture_photo);
        tv_view_photo = (TextView) findViewById(R.id.tv_view_photo);
        tv_note = (TextView) findViewById(R.id.tv_note);
        tv_pay = (TextView) findViewById(R.id.tv_pay);
        ll_Note = (LinearLayout) findViewById(R.id.ll_Note);

        recyle_OrderDetailWithBarcode = (RecyclerView) findViewById(R.id.recyle_OrderDetailWithBarcode);

        tv_toolbar = findViewById(R.id.tv_toolbar);
        iv_back = findViewById(R.id.iv_back);
        iv_home = findViewById(R.id.iv_home);

        tv_toolbar.setText("Checkout");
        tv_toolbar.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        tv_toolbar.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);

        iv_home.setVisibility(View.GONE);


        if (BundleConstants.isKIOSKOrder && !beneficaryWiseArylst.get(0).isCovidOrder()) {
            spn_collection.setVisibility(View.VISIBLE);
            CallCollectionAPI();
        } else {
            spn_collection.setVisibility(View.GONE);
        }

        //TO orderwise details API

    }

    private void CallCollectionAPI() {
        if (cd.isConnectingToInternet()) {
            GetCollectionReqModel getCollectionReqModel = new GetCollectionReqModel();
            getCollectionReqModel.setApiKey("cWPte2TY@MX)UB21JvANihDcVJ8vZjhC.XeJMNKD9Lo4=");
            getCollectionReqModel.setPincode("" + orderVisitDetailsModel.getAllOrderdetails().get(0).getPincode());
            GetCollectionCenterController getCollectionCenterController = new GetCollectionCenterController(this);
            getCollectionCenterController.CallAPI(getCollectionReqModel);
        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void initData() {


        if (!InputUtils.isNull(orderVisitDetailsModel.getAllOrderdetails().get(0).getAmountDue()) && orderVisitDetailsModel.getAllOrderdetails().get(0).getAmountDue() != 0) {
            tv_pay.setText(mActivity.getResources().getString(R.string.rupee_symbol) + " " + orderVisitDetailsModel.getAllOrderdetails().get(0).getAmountDue() + "/-");
            btn_Pay.setText("PAY");
        } else if (orderVisitDetailsModel.getAllOrderdetails().get(0).getAmountDue() == 0) {
            tv_pay.setText("Paid");
        }

        OrderMode = !StringUtils.isNull(orderVisitDetailsModel.getAllOrderdetails().get(0).getOrderMode()) ? orderVisitDetailsModel.getAllOrderdetails().get(0).getOrderMode() : "";
        isOnlyDigital = orderVisitDetailsModel.getAllOrderdetails().get(0).isDigital();
        /*for (OrderDetailsModel orderDetailsModel : orderVisitDetailsModel.getAllOrderdetails()) {
            totalAmountPayable = totalAmountPayable + orderDetailsModel.getAmountPayable();
        }*/

        for (OrderDetailsModel orderDetailsModel : orderVisitDetailsModel.getAllOrderdetails()) {
            if (orderDetailsModel.getAmountDue() != 0) {
                totalAmountPayable = totalAmountPayable + orderDetailsModel.getAmountDue();
            } else {
                totalAmountPayable = totalAmountPayable + orderDetailsModel.getAmountPayable();
            }

        }

        if (BundleConstants.addPaymentFlag == 1) {
            btn_Pay.setText("Submit");
        } else {
            if (totalAmountPayable == 0) {
                btn_Pay.setText("Submit");
            } else {
                btn_Pay.setText("PAY");
            }
        }

        if (orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster() != null && orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().size() > 0) {
            if (orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode() != null) {
                if (CommonUtils.isValidForEditing(orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode())) {
                    isOnlyWOE = true;
                }
            }
        }
        if (isOnlyWOE) {
            txt_amount.setText(mActivity.getResources().getString(R.string.rupee_symbol) + " 0 /-");
        } else {
            txt_amount.setText(mActivity.getResources().getString(R.string.rupee_symbol) + " " + totalAmountPayable + "/-");
        }

        if (beneficaryWiseArylst != null && beneficaryWiseArylst.size() > 0) {
            checkoutWoeAdapter = new CheckoutWoeAdapter(mActivity, beneficaryWiseArylst, orderVisitDetailsModel.getAllOrderdetails().get(0).isDisplayProduct());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
            recyle_OrderDetailWithBarcode.setLayoutManager(mLayoutManager);
            recyle_OrderDetailWithBarcode.setAdapter(checkoutWoeAdapter);
          /*  for (int i = 0; i < beneficaryWiseArylst.size(); i++) {
            }*/
            btn_Pay.setEnabled(true);
            txtNoRecord.setVisibility(View.GONE);
            recyle_OrderDetailWithBarcode.setVisibility(View.VISIBLE);
        } else {
            btn_Pay.setEnabled(false);
            txtNoRecord.setVisibility(View.VISIBLE);
            recyle_OrderDetailWithBarcode.setVisibility(View.GONE);
        }
    }

    private void initListener() {

        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, HomeScreenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(mActivity).create();
                alertDialog.setMessage("All changes made will be reset.\nAre you sure, you want to go back ?");
                alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE, "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                backpressed();
                                //finish();
                            }
                        });
                alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE, "NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                //   onBackPressed();
                //  backpressed();
            }
        });

        btn_capture_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                selectImage();
                TedPermission.with(mActivity)
                        .setPermissions(Manifest.permission.CAMERA)
                        .setRationaleMessage("We need permission to capture photo from your camera to Upload Vail Photo.")
                        .setRationaleConfirmText("OK")
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > Permission > Camera")
                        .setPermissionListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted() {
                                openCamera();
                            }

                            @Override
                            public void onPermissionDenied(List<String> deniedPermissions) {
                                Toast.makeText(mActivity, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }).check();
            }
        });
        tv_view_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selfie_photo.exists()) {
                    ShowSelfieDialog();
                }
            }
        });

        spn_collection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!spn_collection.getSelectedItem().toString().equalsIgnoreCase("Select Collection Center")) {
                    address = "" + getcollearray.get(position).getAddress();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_Pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (canSubmit) {
                    SharedPreferences prefVersionControlFags = getSharedPreferences("VersionControlFlags", 0);
                    if (prefVersionControlFags.getInt("OTPEnabled", 0) == 1) {
                        OrderBookingRequestModel orderBookingRequestModel = generateOrderBookingRequestModel("Button_proceed_payment");
                        if (!CommonUtils.isValidForEditing((orderBookingRequestModel.getBendtl().get(0).getTests()))) {
                            ShowDialogToVerifyOTP();
                        } else {
                            ProceedWOEonSubmit();
                        }
                    } else {
                        ProceedWOEonSubmit();
                    }
                } else {
                    Toast.makeText(mActivity, "Please upload selfie first", LENGTH_SHORT).show();
                }
            }
        });
    }

    private void backpressed() {
        Intent intent = new Intent(mActivity, ScanBarcodeWoeActivity.class);
        intent.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL, orderVisitDetailsModel);
        intent.putExtra(BundleConstants.BENEFICIARY_DETAILS_MODEL, beneficaryWiseArylst);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void ProceedWOEonSubmit() {

        try {
            if (BundleConstants.isKIOSKOrder && !beneficaryWiseArylst.get(0).isCovidOrder()) {
                if (spn_collection != null && spn_collection.getSelectedItem() != null) {
                    if (!InputUtils.isNull(spn_collection.getSelectedItem().toString())) {
                        if (spn_collection.getSelectedItem().toString().equalsIgnoreCase("Select Collection Center")) {
                            globalclass.showCustomToast(mActivity, "Select Collection Center");
                        } else {
                            WOE();
                        }
                    }
                } else {
//                    WOE();
                    globalclass.showCustomToast(mActivity, SOMETHING_WENT_WRONG);
                }
            } else {
                //fungible
                if (BundleConstants.isPEPartner && InputUtils.CheckEqualIgnoreCase(btn_Pay.getText().toString(), "PAY")) {
                    CallPayTypeAPI();
                } else {
                    WOE();
                }
                /*if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName()) && InputUtils.CheckEqualIgnoreCase(btn_Pay.getText().toString(), "PAY")) {
                    CallPayTypeAPI();
                } else {
                    if (orderVisitDetailsModel.getAllOrderdetails().get(0).isPEPartner() && InputUtils.CheckEqualIgnoreCase(btn_Pay.getText().toString(), "PAY")) {
                        CallPayTypeAPI();
                    } else {
                        WOE();
                    }
                }*/
            }
        } catch (Exception e) {
            e.printStackTrace();
//            WOE();
        }
    }

    public void WOE() {
        try {
            OrderBookingRequestModel orderBookingRequestModel = generateOrderBookingRequestModel("Button_proceed_payment");
            Logger.error("Selcted testssssssss" + orderBookingRequestModel.getBendtl().get(0).getTestsCode());
            boolean isEditMobile_email = orderVisitDetailsModel.getAllOrderdetails().get(0).isEditME();

            if (orderBookingRequestModel.getOrddtl().get(0).getStatus().toString().trim().equalsIgnoreCase("PARTIAL SERVICED")) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setMessage("Payment already received. Please proceed")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PaymentMode = 1;
                                OrderBookingRequestModel orderBookingRequestModel = generateOrderBookingRequestModel("work_order_entry_cash");
                                if (cd.isConnectingToInternet()) {
                                    CallWOEAPI(orderBookingRequestModel);

                                    //                                CallWorkOrderEntryAPI(orderBookingRequestModel);
                                } else {
                                    Toast.makeText(mActivity, mActivity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();

                //neha g -----------------------------------
                CheckDelay();

                if (BundleConstants.delay != 0) {
                    MessageLogger.PrintMsg("notify enter");
                    showNotiication();
                    //neha g---------------------
                }
            } else {

               /* if (CommonUtils.isValidForEditing(orderBookingRequestModel.getBendtl().get(0).getTestsCode())) {
                    Logger.error("for PPBS");
                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                    builder.setMessage("Payment already received. Please proceed")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    PaymentMode = 1;
                                    OrderBookingRequestModel orderBookingRequestModel = generateOrderBookingRequestModel("work_order_entry_cash");
                                    if (cd.isConnectingToInternet()) {
                                        CallWOEAPI(orderBookingRequestModel);

                                        //                                CallWorkOrderEntryAPI(orderBookingRequestModel);
                                    } else {
                                        Toast.makeText(mActivity, mActivity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();

                    //neha g -----------------------------------
                    CheckDelay();

                    if (BundleConstants.delay != 0) {
                        MessageLogger.PrintMsg("notify enter");
                        showNotiication();
                        //neha g---------------------
                    }
                } else*/
                if (!isEditMobile_email) {
                    // TODO code to Add again the Venupunture images stored in global array in MainbookingRequestModel
                    OrderBookingRequestModel orderBookingRequestModel1 = generateOrderBookingRequestModel("work_order_entry_prepaid");
                    if (cd.isConnectingToInternet()) {
                        CallWOEAPI(orderBookingRequestModel1);
                        //                CallWorkOrderEntryAPI(orderBookingRequestModel1);
                    } else {
                        Toast.makeText(mActivity, mActivity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
                    }
                } /*else if (InputUtils.isNull(orderBookingRequestModel.getBendtl().get(0).getTestsCode()) && isEditMobile_email) {
    //TODO Done if testcode is null and isEDITME is true // for testing
                OrderBookingRequestModel orderBookingRequestModel1 = generateOrderBookingRequestModel("work_order_entry_prepaid");
                if (cd.isConnectingToInternet()) {
                    CallWOEAPI(orderBookingRequestModel1);
    //                CallWorkOrderEntryAPI(orderBookingRequestModel1);
                } else {
                    Toast.makeText(mActivity, mActivity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
                }
            }*/ else {
                    Logger.error("Other than PPBS");
                    if (cd.isConnectingToInternet()) {
                        CallOrderBookingApi(orderBookingRequestModel);
                    } else {
                        Toast.makeText(mActivity, mActivity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void CallWOEAPI(OrderBookingRequestModel orderBookingRequestModel) {
        if (cd.isConnectingToInternet()) {
            WOEController wc = new WOEController(this);
            wc.CallWorkOrderEntryAPI(orderBookingRequestModel, orderVisitDetailsModel, test, newTimeaddTwoHrs, newTimeaddTwoHalfHrs, btn_Pay);
        } else {
            globalclass.showCustomToast(mActivity, CheckInternetConnectionMsg);
        }

    }

    private OrderBookingRequestModel generateOrderBookingRequestModel(String from) {

        OrderBookingRequestModel orderBookingRequestModel = new OrderBookingRequestModel();
        //SET Order Booking Details Model - START
        OrderBookingDetailsModel orderBookingDetailsModel = new OrderBookingDetailsModel();
        orderBookingDetailsModel.setPaymentMode(PaymentMode);
        orderBookingDetailsModel.setBtechId(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
        orderBookingDetailsModel.setVisitId(orderVisitDetailsModel.getVisitId());
        orderBookingDetailsModel.setProcessLocation(getProcessLocationFromString());
        ArrayList<OrderDetailsModel> ordtl = new ArrayList<>();
        ordtl = orderVisitDetailsModel.getAllOrderdetails();
        String Slot = orderVisitDetailsModel.getSlot();
        Logger.error("Slot" + Slot);
        dateCheck();
        if (from.equals("Button_proceed_payment")) {
            for (int i = 0; i < ordtl.size(); i++) {
                ordtl.get(i).setAddBen(false);
            }
        }
        Logger.error("Amount when order booking " + orderVisitDetailsModel.getAllOrderdetails().get(0).getAmountPayable());
        for (int i = 0; i < ordtl.size(); i++) {
            ordtl.get(i).setAmountDue(totalAmountPayable);
            ordtl.get(i).setAmountPayable(totalAmountPayable);
            if (BundleConstants.isKIOSKOrder && !beneficaryWiseArylst.get(0).isCovidOrder()) {
                ordtl.get(i).setAddress("" + address);
            } else if (BundleConstants.isKIOSKOrder && (beneficaryWiseArylst.get(0).isCovidOrder())) {
                if (!InputUtils.isNull(orderVisitDetailsModel.getAllOrderdetails().get(0).getAddress())) {
                    ordtl.get(i).setAddress("" + orderVisitDetailsModel.getAllOrderdetails().get(0).getAddress());
                }
            }
        }

        //fungible
        if (BundleConstants.isPEPartner && InputUtils.CheckEqualIgnoreCase(btn_Pay.getText().toString(), "PAY")) {
//        if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName()) && btn_Pay.getText().toString().equalsIgnoreCase("Pay")) {
            for (int i = 0; i < ordtl.size(); i++) {
                ordtl.get(i).setPayType(strPaytype);
            }
        }
//        orderBookingDetailsModel.getOrddtl().get(0).setPayType(strPaytype);
        orderBookingDetailsModel.setOrddtl(ordtl);
        orderBookingRequestModel.setOrdbooking(orderBookingDetailsModel);
        //SET Order Booking Details Model - END
        //SET Order Details Models Array - START
        orderBookingRequestModel.setOrddtl(ordtl);
        //SET Order Details Models Array - END
        //SET BENEFICIARY Details Models Array - START
        ArrayList<BeneficiaryDetailsModel> benArr = orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster();
        if (from.equals("Button_proceed_payment")) {
            for (int i = 0; i < benArr.size(); i++) {
                benArr.get(i).setAddBen(false);
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

    private String getProcessLocationFromString() {
        if (processLocation != null) {
            return processLocation;
        }
        return "";
    }

    public void CallOrderBookingApi(OrderBookingRequestModel orderBookingRequestModel) {

        for (int i = 0; i < orderBookingRequestModel.getBendtl().size(); i++) {
            if (!InputUtils.isNull(orderBookingRequestModel.getBendtl().get(i).getVenepuncture())) {
                orderBookingRequestModel.getBendtl().get(i).setVenepuncture("");
            }
        }

        for (int i = 0; i < orderBookingRequestModel.getOrdbooking().getOrddtl().size(); i++) {
            for (int j = 0; j < orderBookingRequestModel.getOrdbooking().getOrddtl().get(i).getBenMaster().size(); j++) {
                if (!InputUtils.isNull(orderBookingRequestModel.getOrdbooking().getOrddtl().get(i).getBenMaster().get(j).getVenepuncture())) {
                    orderBookingRequestModel.getOrdbooking().getOrddtl().get(i).getBenMaster().get(j).setVenepuncture("");
                }
            }

        }

        for (int i = 0; i < orderBookingRequestModel.getOrddtl().size(); i++) {
            for (int j = 0; j < orderBookingRequestModel.getOrddtl().get(i).getBenMaster().size(); j++) {
                if (!InputUtils.isNull(orderBookingRequestModel.getOrddtl().get(i).getBenMaster().get(j).getVenepuncture())) {
                    orderBookingRequestModel.getOrddtl().get(i).getBenMaster().get(j).setVenepuncture("");
                }
            }
        }

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(mActivity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        String postrequest = new Gson().toJson(orderBookingRequestModel);
        System.out.println(postrequest);
        Call<OrderBookingResponseVisitModel> responseCall = apiInterface.CallOrderBookingApi(orderBookingRequestModel);
        globalclass.showProgressDialog(mActivity, mActivity.getResources().getString(R.string.progress_message_uploading_order_details_please_wait), false);
        responseCall.enqueue(new Callback<OrderBookingResponseVisitModel>() {
            @Override
            public void onResponse(Call<OrderBookingResponseVisitModel> call, retrofit2.Response<OrderBookingResponseVisitModel> response) {
                globalclass.hideProgressDialog(mActivity);
                if (response.isSuccessful() && response.body() != null) {
                    SendinglatlongOrderAllocation(5);
                    orderBookingResponseVisitModel = response.body();

                    BundleConstants.setSecondOrderFlag = orderBookingResponseVisitModel.getSecondVisitGenerated();


                    if (orderBookingResponseVisitModel.getOrderids() != null) {
                        for (OrderBookingResponseOrderModel obrom :
                                orderBookingResponseVisitModel.getOrderids()) {
                            orderBookingResponseBeneficiaryModelArr.addAll(obrom.getBenfids());
                        }
                    }
                    if (!btn_Pay.getText().equals("Submit")) {
                        if (btn_Pay.getText().equals("PAY")) {
                            if (isOnlyDigital && BundleConstants.isPEPartner && BundleConstants.PEDSAOrder) {
//fungible
//                                if (BundleConstants.companyOrderFlag) {
//                                    if (orderVisitDetailsModel.getAllOrderdetails().get(0).isPEPartner()) {
                                if (strPaytype.equalsIgnoreCase(Global.Prepaid)) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                                    builder.setMessage("Payment already received. Please proceed")
                                            .setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    PaymentMode = 2;
                                                    OrderBookingRequestModel orderBookingRequestModel = generateOrderBookingRequestModel("work_order_entry_prepaid");
                                                    if (cd.isConnectingToInternet()) {
                                                        CallWOEAPI(orderBookingRequestModel);
                                                    } else {
                                                        Toast.makeText(mActivity, mActivity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            })
                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).show();

                                } else {
                                    redirectToPaymentScreen();
                                }
                                    /*} else {
                                        redirectToPaymentScreen();
                                    }*/
                            } else if (BundleConstants.isPEPartner) {
                                if (strPaytype.equalsIgnoreCase(Global.Prepaid)) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                                    builder.setMessage("Payment already received. Please proceed")
                                            .setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    PaymentMode = 2;
                                                    OrderBookingRequestModel orderBookingRequestModel = generateOrderBookingRequestModel("work_order_entry_prepaid");
                                                    if (cd.isConnectingToInternet()) {
                                                        CallWOEAPI(orderBookingRequestModel);

//                                CallWorkOrderEntryAPI(orderBookingRequestModel);
                                                    } else {
                                                        Toast.makeText(mActivity, mActivity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            })
                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).show();

                                 /*   OrderBookingRequestModel orderBookingRequestModel = generateOrderBookingRequestModel("work_order_entry_prepaid");
                                    if (cd.isConnectingToInternet()) {
                                        CallWOEAPI(orderBookingRequestModel);
//                                                CallWorkOrderEntryAPI(orderBookingRequestModel);
                                    } else {
                                        Toast.makeText(mActivity, mActivity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
                                    }*/
                                } else {
                                    if (!Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName()) && BundleConstants.isPEPartner) {
                                        redirectToPaymentScreen();
                                    } else {
                                        if (OrderMode.equalsIgnoreCase("LTD-BLD") || OrderMode.equalsIgnoreCase("LTD-NBLD")) {
                                            paymentItems = new String[]{"Cash"};
                                        } else {
                                            paymentItems = new String[]{"Cash", "Digital"};
                                        }
                                        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                                        builder.setTitle("Choose payment mode")
                                                .setItems(paymentItems, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        if (paymentItems[which].equals("Cash")) {
                                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(mActivity);
                                                            builder1.setMessage("Confirm amount received ₹ " + totalAmountPayable + "")
                                                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                            PaymentMode = 1;
                                                                            // TODO code to Add again the Venupunture images stored in global array in MainbookingRequestModel
                                                                            OrderBookingRequestModel orderBookingRequestModel = generateOrderBookingRequestModel("work_order_entry_cash");
                                                                            if (cd.isConnectingToInternet()) {
                                                                                CallWOEAPI(orderBookingRequestModel);
//                                                                                        CallWorkOrderEntryAPI(orderBookingRequestModel);
                                                                            } else {
                                                                                Toast.makeText(mActivity, mActivity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    })
                                                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                            dialog.dismiss();
                                                                        }
                                                                    })
                                                                    .show();
                                                        } else {
                                                            PaymentMode = 2;
                                                            Intent intentPayments = new Intent(mActivity, PaymentsActivity.class);
                                                            Logger.error("tejastotalAmountPayableatsending " + totalAmountPayable);
                                                            intentPayments.putExtra(BundleConstants.PAYMENTS_AMOUNT, totalAmountPayable + "");
                                                            intentPayments.putExtra(BundleConstants.PAYMENTS_NARRATION_ID, 2);
                                                            intentPayments.putExtra(BundleConstants.BENEFICIARY_DETAILS_MODEL, beneficaryWiseArylst);
                                                            intentPayments.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL, orderVisitDetailsModel);
                                                            intentPayments.putExtra(BundleConstants.PAYMENTS_ORDER_NO, orderVisitDetailsModel.getVisitId());
                                                            intentPayments.putExtra(BundleConstants.PAYMENTS_SOURCE_CODE, Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
                                                            intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_NAME, orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(0).getName());
                                                            intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_ADDRESS, orderVisitDetailsModel.getAllOrderdetails().get(0).getAddress());
                                                            intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_PIN, orderVisitDetailsModel.getAllOrderdetails().get(0).getPincode());
                                                            intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_MOBILE, orderVisitDetailsModel.getAllOrderdetails().get(0).getMobile());
                                                            intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_EMAIL, orderVisitDetailsModel.getAllOrderdetails().get(0).getEmail());
                                                            intentPayments.putExtra(BundleConstants.PROCESSING_LOCATION, processLocation);
                                                            intentPayments.putExtra(BundleConstants.ADDRESS, address);
                                                            intentPayments.putExtra(BundleConstants.newTimeTWO, newTimeaddTwoHrs);
                                                            intentPayments.putExtra(BundleConstants.newTimeTWOHalf, newTimeaddTwoHalfHrs);
                                                            startActivityForResult(intentPayments, BundleConstants.PAYMENTS_START);
                                                        }
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                    }

                                }
                            } /*else if (!BundleConstants.isPEPartner && !BundleConstants.PEDSAOrder) {

                                PaymentMode = 2;
                                Intent intentPayments = new Intent(mActivity, PaymentsActivity.class);
                                Logger.error("tejastotalAmountPayableatsending " + totalAmountPayable);
                                intentPayments.putExtra(BundleConstants.PAYMENTS_AMOUNT, totalAmountPayable + "");
                                intentPayments.putExtra(BundleConstants.PAYMENTS_NARRATION_ID, 2);
                                intentPayments.putExtra(BundleConstants.BENEFICIARY_DETAILS_MODEL, beneficaryWiseArylst);
                                intentPayments.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL, orderVisitDetailsModel);
                                intentPayments.putExtra(BundleConstants.PAYMENTS_ORDER_NO, orderVisitDetailsModel.getVisitId());
                                intentPayments.putExtra(BundleConstants.PAYMENTS_SOURCE_CODE, Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
                                intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_NAME, orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(0).getName());
                                intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_ADDRESS, orderVisitDetailsModel.getAllOrderdetails().get(0).getAddress());
                                intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_PIN, orderVisitDetailsModel.getAllOrderdetails().get(0).getPincode());
                                intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_MOBILE, orderVisitDetailsModel.getAllOrderdetails().get(0).getMobile());
                                intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_EMAIL, orderVisitDetailsModel.getAllOrderdetails().get(0).getEmail());
                                intentPayments.putExtra(BundleConstants.PROCESSING_LOCATION, processLocation);
                                intentPayments.putExtra(BundleConstants.ADDRESS, address);
                                intentPayments.putExtra(BundleConstants.newTimeTWO, newTimeaddTwoHrs);
                                intentPayments.putExtra(BundleConstants.newTimeTWOHalf, newTimeaddTwoHalfHrs);
                                startActivityForResult(intentPayments, BundleConstants.PAYMENTS_START);

                            }*/
                        } else if (btn_Pay.getText().equals("Submit")) {
                            // TODO code to Add again the Venupunture images stored in global array in MainbookingRequestModel

                            if (BundleConstants.addPaymentFlag == 1) {
                                PaymentMode = 1;
                            }
                            OrderBookingRequestModel orderBookingRequestModel = generateOrderBookingRequestModel("work_order_entry_prepaid");
                            if (cd.isConnectingToInternet()) {
                                CallWOEAPI(orderBookingRequestModel);
//                                                CallWorkOrderEntryAPI(orderBookingRequestModel);
                            } else {
                                Toast.makeText(mActivity, mActivity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
                            }
                        }
                        /*AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                        builder.setMessage("Amount payable ₹ " + totalAmountPayable + "/-")
                                .setPositiveButton("Collect", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (btn_Pay.getText().equals("PAY")) {
                                            CallPayTypeAPI();

                                            if (isOnlyDigital && !InputUtils.CheckEqualIgnoreCase(appPreferenceManager.getLoginResponseModel().getCompanyName(), Global.PE_BTech)) {
                                                PaymentMode = 2;
                                                Intent intentPayments = new Intent(mActivity, PaymentsActivity.class);
                                                Logger.error("tejastotalAmountPayableatsending " + totalAmountPayable);
                                                intentPayments.putExtra(BundleConstants.PAYMENTS_AMOUNT, totalAmountPayable + "");
                                                intentPayments.putExtra(BundleConstants.PAYMENTS_NARRATION_ID, 2);
                                                intentPayments.putExtra(BundleConstants.PAYMENTS_ORDER_NO, orderVisitDetailsModel.getVisitId());
                                                intentPayments.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL, orderVisitDetailsModel);
                                                intentPayments.putExtra(BundleConstants.BENEFICIARY_DETAILS_MODEL, beneficaryWiseArylst);
                                                intentPayments.putExtra(BundleConstants.PAYMENTS_SOURCE_CODE, Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
                                                intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_NAME, orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(0).getName());
                                                intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_ADDRESS, orderVisitDetailsModel.getAllOrderdetails().get(0).getAddress());
                                                intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_PIN, orderVisitDetailsModel.getAllOrderdetails().get(0).getPincode());
                                                intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_MOBILE, orderVisitDetailsModel.getAllOrderdetails().get(0).getMobile());
                                                intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_EMAIL, orderVisitDetailsModel.getAllOrderdetails().get(0).getEmail());
                                                intentPayments.putExtra(BundleConstants.PROCESSING_LOCATION, processLocation);
                                                intentPayments.putExtra(BundleConstants.ADDRESS, address);
                                                intentPayments.putExtra(BundleConstants.newTimeTWO, newTimeaddTwoHrs);
                                                intentPayments.putExtra(BundleConstants.newTimeTWOHalf, newTimeaddTwoHalfHrs);
                                                startActivityForResult(intentPayments, BundleConstants.PAYMENTS_START);
                                            } else {
                                                if (OrderMode.equalsIgnoreCase("LTD-BLD") || OrderMode.equalsIgnoreCase("LTD-NBLD")) {
                                                    paymentItems = new String[]{"Cash"};
                                                } else {
                                                    paymentItems = new String[]{"Cash", "Digital"};
                                                }
                                                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                                                builder.setTitle("Choose payment mode")
                                                        .setItems(paymentItems, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                if (paymentItems[which].equals("Cash")) {

                                                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(mActivity);
                                                                    builder1.setMessage("Confirm amount received ₹ " + totalAmountPayable + "")
                                                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                    PaymentMode = 1;
                                                                                    // TODO code to Add again the Venupunture images stored in global array in MainbookingRequestModel
                                                                                    OrderBookingRequestModel orderBookingRequestModel = generateOrderBookingRequestModel("work_order_entry_cash");
                                                                                    if (cd.isConnectingToInternet()) {
                                                                                        CallWOEAPI(orderBookingRequestModel);
//                                                                                        CallWorkOrderEntryAPI(orderBookingRequestModel);
                                                                                    } else {
                                                                                        Toast.makeText(mActivity, mActivity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                }
                                                                            })
                                                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                    dialog.dismiss();
                                                                                }
                                                                            })
                                                                            .show();
                                                                } else {
                                                                    PaymentMode = 2;
                                                                    Intent intentPayments = new Intent(mActivity, PaymentsActivity.class);
                                                                    Logger.error("tejastotalAmountPayableatsending " + totalAmountPayable);
                                                                    intentPayments.putExtra(BundleConstants.PAYMENTS_AMOUNT, totalAmountPayable + "");
                                                                    intentPayments.putExtra(BundleConstants.PAYMENTS_NARRATION_ID, 2);
                                                                    intentPayments.putExtra(BundleConstants.BENEFICIARY_DETAILS_MODEL, beneficaryWiseArylst);
                                                                    intentPayments.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL, orderVisitDetailsModel);
                                                                    intentPayments.putExtra(BundleConstants.PAYMENTS_ORDER_NO, orderVisitDetailsModel.getVisitId());
                                                                    intentPayments.putExtra(BundleConstants.PAYMENTS_SOURCE_CODE, Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
                                                                    intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_NAME, orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(0).getName());
                                                                    intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_ADDRESS, orderVisitDetailsModel.getAllOrderdetails().get(0).getAddress());
                                                                    intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_PIN, orderVisitDetailsModel.getAllOrderdetails().get(0).getPincode());
                                                                    intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_MOBILE, orderVisitDetailsModel.getAllOrderdetails().get(0).getMobile());
                                                                    intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_EMAIL, orderVisitDetailsModel.getAllOrderdetails().get(0).getEmail());
                                                                    intentPayments.putExtra(BundleConstants.PROCESSING_LOCATION, processLocation);
                                                                    intentPayments.putExtra(BundleConstants.ADDRESS, address);
                                                                    intentPayments.putExtra(BundleConstants.newTimeTWO, newTimeaddTwoHrs);
                                                                    intentPayments.putExtra(BundleConstants.newTimeTWOHalf, newTimeaddTwoHalfHrs);
                                                                    startActivityForResult(intentPayments, BundleConstants.PAYMENTS_START);
                                                                }
                                                                dialog.dismiss();
                                                            }
                                                        })
                                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                            }
                                                        }).show();
                                            }
                                        } else if (btn_Pay.getText().equals("Submit")) {
                                            // TODO code to Add again the Venupunture images stored in global array in MainbookingRequestModel

                                            if (BundleConstants.addPaymentFlag == 1) {
                                                PaymentMode = 1;
                                            }
                                            OrderBookingRequestModel orderBookingRequestModel = generateOrderBookingRequestModel("work_order_entry_prepaid");
                                            if (cd.isConnectingToInternet()) {
                                                CallWOEAPI(orderBookingRequestModel);
//                                                CallWorkOrderEntryAPI(orderBookingRequestModel);
                                            } else {
                                                Toast.makeText(mActivity, mActivity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();*/
                    } else {
                        if (BundleConstants.addPaymentFlag == 1) {
                            PaymentMode = 1;
                        }
                        OrderBookingRequestModel orderBookingRequestModel = generateOrderBookingRequestModel("work_order_entry_prepaid");
                        if (cd.isConnectingToInternet()) {
                            CallWOEAPI(orderBookingRequestModel);
//                            CallWorkOrderEntryAPI(orderBookingRequestModel);
                        } else {
                            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    try {
                        if (response.errorBody() != null) {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            globalclass.showCustomToast(mActivity, jObjError.optString("Message", SOMETHING_WENT_WRONG));
                        } else {
                            globalclass.showCustomToast(mActivity, SOMETHING_WENT_WRONG);
                        }
                    } catch (Exception e) {
                        globalclass.showCustomToast(mActivity, SOMETHING_WENT_WRONG);
                    }

                    BundleConstants.setSecondOrderFlag = false;
                }
            }

            @Override
            public void onFailure(Call<OrderBookingResponseVisitModel> call, Throwable t) {
                globalclass.hideProgressDialog(mActivity);
                BundleConstants.setSecondOrderFlag = false;
                globalclass.showCustomToast(mActivity, ConstantsMessages.UNABLE_TO_CONNECT, Toast.LENGTH_LONG);
                MessageLogger.LogDebug("Errror", t.getMessage());
            }
        });
    }

    private void redirectToPaymentScreen() {
        try {
            PaymentMode = 2;
            Intent intentPayments = new Intent(mActivity, PaymentsActivity.class);
            Logger.error("tejastotalAmountPayableatsending " + totalAmountPayable);
            intentPayments.putExtra(BundleConstants.PAYMENTS_AMOUNT, totalAmountPayable + "");
            intentPayments.putExtra(BundleConstants.PAYMENTS_NARRATION_ID, 2);
            intentPayments.putExtra(BundleConstants.PAYMENTS_ORDER_NO, orderVisitDetailsModel.getVisitId());
            intentPayments.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL, orderVisitDetailsModel);
            intentPayments.putExtra(BundleConstants.BENEFICIARY_DETAILS_MODEL, beneficaryWiseArylst);
            intentPayments.putExtra(BundleConstants.PAYMENTS_SOURCE_CODE, Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
            intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_NAME, orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(0).getName());
            intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_ADDRESS, orderVisitDetailsModel.getAllOrderdetails().get(0).getAddress());
            intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_PIN, orderVisitDetailsModel.getAllOrderdetails().get(0).getPincode());
            intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_MOBILE, orderVisitDetailsModel.getAllOrderdetails().get(0).getMobile());
            intentPayments.putExtra(BundleConstants.PAYMENTS_BILLING_EMAIL, orderVisitDetailsModel.getAllOrderdetails().get(0).getEmail());
            intentPayments.putExtra(BundleConstants.PROCESSING_LOCATION, processLocation);
            intentPayments.putExtra(BundleConstants.ADDRESS, address);
            intentPayments.putExtra(BundleConstants.newTimeTWO, newTimeaddTwoHrs);
            intentPayments.putExtra(BundleConstants.newTimeTWOHalf, newTimeaddTwoHalfHrs);
            startActivityForResult(intentPayments, BundleConstants.PAYMENTS_START);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void CallPayTypeAPI() {
        PaytypeRequestModel ptrm = new PaytypeRequestModel();
        ptrm.setOrderno("" + orderVisitDetailsModel.getVisitId());
        PayTypeController pTC = new PayTypeController(this);
        pTC.getPayType(ptrm);
    }

    public void CallWorkOrderEntryAPI(OrderBookingRequestModel orderBookingRequestModel) {

        for (int i = 0; i < orderBookingRequestModel.getBendtl().size(); i++) {
            if (!InputUtils.isNull(orderBookingRequestModel.getBendtl().get(i).getVenepuncture())) {
                orderBookingRequestModel.getBendtl().get(i).setVenepuncture("");
            }
        }

        for (int i = 0; i < orderBookingRequestModel.getOrdbooking().getOrddtl().size(); i++) {
            for (int j = 0; j < orderBookingRequestModel.getOrdbooking().getOrddtl().get(i).getBenMaster().size(); j++) {
                if (!InputUtils.isNull(orderBookingRequestModel.getOrdbooking().getOrddtl().get(i).getBenMaster().get(j).getVenepuncture())) {
                    orderBookingRequestModel.getOrdbooking().getOrddtl().get(i).getBenMaster().get(j).setVenepuncture("");
                }
            }

        }

        for (int i = 0; i < orderBookingRequestModel.getOrddtl().size(); i++) {
            for (int j = 0; j < orderBookingRequestModel.getOrddtl().get(i).getBenMaster().size(); j++) {
                if (!InputUtils.isNull(orderBookingRequestModel.getOrddtl().get(i).getBenMaster().get(j).getVenepuncture())) {
                    orderBookingRequestModel.getOrddtl().get(i).getBenMaster().get(j).setVenepuncture("");
                }
            }
        }

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(mActivity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallWorkOrderEntryAPI(orderBookingRequestModel);
        String abc = new Gson().toJson(orderBookingRequestModel);
        System.out.println("" + abc);
        globalclass.showProgressDialog(mActivity, PLEASE_WAIT);
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> res) {
                globalclass.hideProgressDialog(mActivity);
                if (res.isSuccessful() && res.body() != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                    builder.setTitle("Order Status")
                            .setCancelable(false)
                            .setMessage("Work order entry successful!\nPlease note ref id - " + orderVisitDetailsModel.getVisitId() + " for future references.")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String remark = orderVisitDetailsModel.getVisitId();
                                    new LogUserActivityTagging(mActivity, BundleConstants.WOE, remark);
                                    if (test.toUpperCase().contains(AppConstants.PPBS) && test.toUpperCase().contains(AppConstants.FBS)
                                            && test.toUpperCase().contains("INSPP") && test.toUpperCase().contains("INSFA")
                                            && test.toUpperCase().contains(AppConstants.RBS) && test.toUpperCase().contains(AppConstants.FBS)) {
                                        Logger.error("should print revisit dialog for both: ");
                                        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                                        builder.setMessage("Please note you have to revisit at customer place to collect sample for PPBS/RBS and INSPP in between " + newTimeaddTwoHrs + " to " + newTimeaddTwoHalfHrs)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Constants.Finish_barcodeScanAcitivty = true;
                                                        Constants.isWOEDone = true;
                                                        if (BundleConstants.isKIOSKOrder) {
                                                            finish();
                                                            Intent intent = new Intent(mActivity, KIOSK_Scanner_Activity.class);
                                                            startActivity(intent);
                                                        } else {
                                                            startActivity(new Intent(mActivity, HomeScreenActivity.class));
//                                                            mActivity.finish();
                                                        }
                                                    }
                                                })
                                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .setCancelable(false)
                                                .show();

                                    } else if (test.toUpperCase().contains(AppConstants.PPBS)
                                            && test.toUpperCase().contains(AppConstants.RBS)
                                            && test.toUpperCase().contains(AppConstants.FBS)) {

                                        Logger.error("should print revisit dialog for ppbs and rbs: ");

                                        Logger.error("for PPBS and RBS");
                                        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                                        builder.setMessage("Please note you have to revisit at customer place to collect sample for PPBS and RBS in between " + newTimeaddTwoHrs + " to " + newTimeaddTwoHalfHrs)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Constants.Finish_barcodeScanAcitivty = true;
                                                        Constants.isWOEDone = true;
                                                        if (BundleConstants.isKIOSKOrder) {
                                                            finish();
                                                            Intent intent = new Intent(mActivity, KIOSK_Scanner_Activity.class);
                                                            startActivity(intent);
                                                        } else {
                                                            mActivity.finish();
                                                        }
                                                    }
                                                })
                                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .setCancelable(false)
                                                .show();

                                    } else if (test.toUpperCase().contains(AppConstants.INSPP) && test.toUpperCase().contains(AppConstants.INSFA)) {
                                        Logger.error("should print revisit dialog for insfa: ");
                                        Logger.error("for INSPP");
                                        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                                        builder.setMessage("Please note you have to revisit at customer place to collect sample for INSPP in between " + newTimeaddTwoHrs + " to " + newTimeaddTwoHalfHrs)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Constants.Finish_barcodeScanAcitivty = true;
                                                        Constants.isWOEDone = true;
                                                        if (BundleConstants.isKIOSKOrder) {
                                                            finish();
                                                            Intent intent = new Intent(mActivity, HomeScreenActivity.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            startActivity(intent);
                                                        } else {
                                                            mActivity.finish();
                                                        }
                                                    }
                                                })
                                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .setCancelable(false)
                                                .show();

                                    } else if (test.toUpperCase().contains(AppConstants.RBS) && test.toUpperCase().contains(AppConstants.FBS)) {
                                        Logger.error("should print revisit dialog for rbs: ");
                                        Logger.error("for rbs");
                                        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                                        builder.setMessage("Please note you have to revisit at customer place to collect sample for RBS in between " + newTimeaddTwoHrs + " to " + newTimeaddTwoHalfHrs)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Constants.Finish_barcodeScanAcitivty = true;
                                                        Constants.isWOEDone = true;
                                                        if (BundleConstants.isKIOSKOrder) {
                                                            finish();
                                                            Intent intent = new Intent(mActivity, HomeScreenActivity.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            startActivity(intent);
                                                        } else {
                                                            mActivity.finish();
                                                        }
                                                    }
                                                })
                                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .setCancelable(false)
                                                .show();

                                    } else if (test.toUpperCase().contains(AppConstants.PPBS) && test.toUpperCase().contains(AppConstants.FBS)) {
                                        Logger.error("should print revisit dialog for rbs: ");
                                        Logger.error("for ppbs");
                                        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                                        builder.setMessage("Please note you have to revisit at customer place to collect sample for PPBS in between " + newTimeaddTwoHrs + " to " + newTimeaddTwoHalfHrs)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Constants.Finish_barcodeScanAcitivty = true;
                                                        Constants.isWOEDone = true;
                                                        if (BundleConstants.isKIOSKOrder) {
                                                            finish();
                                                            Intent intent = new Intent(mActivity, HomeScreenActivity.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            startActivity(intent);
                                                        } else {
                                                            mActivity.finish();
                                                        }
                                                    }
                                                })
                                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .setCancelable(false)
                                                .show();
                                    } else {
                                        Logger.error("testcode in else " + test.toUpperCase());
                                        Constants.Finish_barcodeScanAcitivty = true;
                                        Constants.isWOEDone = true;
                                        if (BundleConstants.isKIOSKOrder) {
                                            finish();
                                            Intent intent = new Intent(mActivity, HomeScreenActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                        } else {
                                            mActivity.finish();
                                        }
                                    }
                                }
                            })
                            .create();

                    if (!mActivity.isFinishing()) {
                        builder.show();
                    }
                } else {
                    try {
                        if (res.errorBody() != null) {
                            JSONObject jObjError = new JSONObject(res.errorBody().string());
                            globalclass.showCustomToast(mActivity, jObjError.optString("Message", SOMETHING_WENT_WRONG));
                        } else {
                            globalclass.showCustomToast(mActivity, SOMETHING_WENT_WRONG);
                        }
                    } catch (Exception e) {
                        globalclass.showCustomToast(mActivity, SOMETHING_WENT_WRONG);
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                    builder.setTitle("Order Status")
                            .setMessage("Work Order Entry Failed!")
                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Constants.Finish_barcodeScanAcitivty = true;
                                    Constants.isWOEDone = true;
                                    mActivity.finish();
                                }
                            })
                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    btn_Pay.performClick();
                                }
                            })
                            .create();

                    if (!mActivity.isFinishing()) {
                        builder.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                globalclass.hideProgressDialog(mActivity);
                Toast.makeText(mActivity, SOMETHING_WENT_WRONG, LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BundleConstants.PAYMENTS_START && resultCode == BundleConstants.PAYMENTS_FINISH) {
            boolean isPaymentSuccess = data.getBooleanExtra(BundleConstants.PAYMENT_STATUS, false);
            if (isPaymentSuccess) {
                // TODO code to reduce the size of Json by temporary storing Venupunture in global array
                OrderBookingRequestModel orderBookingRequestModel = generateOrderBookingRequestModel("work_order_entry_digital");
                orderBookingRequestModel = fixForAddBeneficiary(orderBookingRequestModel);
                if (cd.isConnectingToInternet()) {
                    CallWOEAPI(orderBookingRequestModel);
//                    CallWorkOrderEntryAPI(orderBookingRequestModel);
                } else {
                    Toast.makeText(mActivity, mActivity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mActivity, "Payment failed", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == ImagePicker.REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                String imageurl = null;
                Uri selectedImageUri = data.getData();
                imageurl = selectedImageUri.getPath();

                selfie_photo = new File(imageurl);
                uri = Uri.fromFile(selfie_photo);
                if (uri != null) {
                    tv_view_photo.setVisibility(View.VISIBLE);
                    UploadSelfieAPI();
                } else {
                    Toast.makeText(mActivity, "Failed to upload selfie", LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
       /* else if (requestCode == Camera.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            try {

                String imageurl = camera.getCameraBitmapPath();
                selfie_photo = new File(imageurl);
                uri = Uri.fromFile(selfie_photo);
                if (uri != null) {
                    tv_view_photo.setVisibility(View.VISIBLE);
                    UploadSelfieAPI();
                } else {
                    Toast.makeText(mActivity, "Failed to upload selfie", LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void UploadSelfieAPI() {
        String BtechID = appPreferenceManager.getLoginResponseModel().getUserID();
        final String OrderNo = orderVisitDetailsModel.getVisitId().trim();

        BTechSelfieRequestModel bTechSelfieRequestModel = new BTechSelfieRequestModel();
        bTechSelfieRequestModel.setBtechid(BtechID);
        bTechSelfieRequestModel.setORDERNO(OrderNo);

        if (selfie_photo.exists()) {
            canSubmit = true;
            bTechSelfieRequestModel.setFile(selfie_photo);
            UploadSelfieWOEController uploadSelfieWOEController = new UploadSelfieWOEController(CheckoutWoeActivity.this, mActivity);
            uploadSelfieWOEController.CallAPI(appPreferenceManager.getLoginResponseModel().getAccess_token(), bTechSelfieRequestModel);
        }
    }

    private OrderBookingRequestModel fixForAddBeneficiary(OrderBookingRequestModel
                                                                  orderBookingRequestModel) {
        //Update Visit ID in OrdBooking Model
        try {
            if (orderBookingRequestModel.getOrdbooking().getVisitId().equalsIgnoreCase(orderBookingResponseVisitModel.getOldVisitId())) {
                orderBookingRequestModel.getOrdbooking().setVisitId(orderBookingResponseVisitModel.getNewVisitId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Update Order No and Visit ID in Order Dtl Arr
        try {
            for (int i = 0; i < orderBookingRequestModel.getOrddtl().size(); i++) {
                if (orderBookingRequestModel.getOrddtl().get(i).getVisitId().equalsIgnoreCase(orderBookingResponseVisitModel.getOldVisitId())) {
                    orderBookingRequestModel.getOrddtl().get(i).setVisitId(orderBookingResponseVisitModel.getNewVisitId());
                }
                for (int j = 0; j < orderBookingResponseVisitModel.getOrderids().size(); j++) {
                    if (orderBookingRequestModel.getOrddtl().get(i).getOrderNo().equalsIgnoreCase(orderBookingResponseVisitModel.getOrderids().get(j).getOldOrderId())) {
                        orderBookingRequestModel.getOrddtl().get(i).setOrderNo(orderBookingResponseVisitModel.getOrderids().get(j).getNewOrderId());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Update Order No and BenId in Bendtl Arr
        try {
            if (orderBookingRequestModel.getBendtl().size() != 0) {
                for (int i = 0; i < orderBookingRequestModel.getBendtl().size(); i++) {
                    for (int j = 0; j < orderBookingResponseVisitModel.getOrderids().size(); j++) {
                        if (orderBookingRequestModel.getBendtl().get(i).getOrderNo().equalsIgnoreCase(orderBookingResponseVisitModel.getOrderids().get(j).getOldOrderId())) {
                            orderBookingRequestModel.getBendtl().get(i).setOrderNo(orderBookingResponseVisitModel.getOrderids().get(j).getNewOrderId());
                        }
                    }
                    for (int j = 0; j < orderBookingResponseBeneficiaryModelArr.size(); j++) {
                        if ((orderBookingRequestModel.getBendtl().get(i).getBenId() + "").equalsIgnoreCase(orderBookingResponseBeneficiaryModelArr.get(j).getOldBenIds())) {
                            orderBookingRequestModel.getBendtl().get(i).setBenId(Integer.parseInt(orderBookingResponseBeneficiaryModelArr.get(j).getNewBenIds()));
                        }
                    }
                }
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        //Update orderNo and BenId in BarcodeDtl Arr
        try {
            for (int i = 0; i < orderBookingRequestModel.getBarcodedtl().size(); i++) {
                for (int j = 0; j < orderBookingResponseVisitModel.getOrderids().size(); j++) {
                    if (!InputUtils.isNull(orderBookingRequestModel.getBarcodedtl().get(i).getOrderNo()) && !InputUtils.isNull(orderBookingResponseVisitModel.getOrderids().get(j).getOldOrderId()))
                        if (orderBookingRequestModel.getBarcodedtl().get(i).getOrderNo().equalsIgnoreCase(orderBookingResponseVisitModel.getOrderids().get(j).getOldOrderId())) {
                            orderBookingRequestModel.getBarcodedtl().get(i).setOrderNo(orderBookingResponseVisitModel.getOrderids().get(j).getNewOrderId());
                        }
                }
                for (int j = 0; j < orderBookingResponseBeneficiaryModelArr.size(); j++) {
                    if ((orderBookingRequestModel.getBarcodedtl().get(i).getBenId() + "").equalsIgnoreCase(orderBookingResponseBeneficiaryModelArr.get(j).getOldBenIds())) {
                        orderBookingRequestModel.getBarcodedtl().get(i).setBenId(Integer.parseInt(orderBookingResponseBeneficiaryModelArr.get(j).getNewBenIds()));
                    }
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        //Update BenId in SmplDtl Arr
        try {
            for (int i = 0; i < orderBookingRequestModel.getSmpldtl().size(); i++) {
                for (int j = 0; j < orderBookingResponseBeneficiaryModelArr.size(); j++) {
                    if ((orderBookingRequestModel.getSmpldtl().get(i).getBenId() + "").equalsIgnoreCase(orderBookingResponseBeneficiaryModelArr.get(j).getOldBenIds())) {
                        orderBookingRequestModel.getSmpldtl().get(i).setBenId(Integer.parseInt(orderBookingResponseBeneficiaryModelArr.get(j).getNewBenIds()));
                    }
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        //Update BenId in ClHistory Arr
        try {
            for (int i = 0; i < orderBookingRequestModel.getClHistory().size(); i++) {
                for (int j = 0; j < orderBookingResponseBeneficiaryModelArr.size(); j++) {
                    if ((orderBookingRequestModel.getClHistory().get(i).getBenId() + "").equalsIgnoreCase(orderBookingResponseBeneficiaryModelArr.get(j).getOldBenIds())) {
                        orderBookingRequestModel.getClHistory().get(i).setBenId(Integer.parseInt(orderBookingResponseBeneficiaryModelArr.get(j).getNewBenIds()));
                    }
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        //Update BenId in LabAlert Arr
        try {
            for (int i = 0; i < orderBookingRequestModel.getLabAlert().size(); i++) {
                for (int j = 0; j < orderBookingResponseBeneficiaryModelArr.size(); j++) {
                    if ((orderBookingRequestModel.getLabAlert().get(i).getBenId() + "").equalsIgnoreCase(orderBookingResponseBeneficiaryModelArr.get(j).getOldBenIds())) {
                        orderBookingRequestModel.getLabAlert().get(i).setBenId(Integer.parseInt(orderBookingResponseBeneficiaryModelArr.get(j).getNewBenIds()));
                    }
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return orderBookingRequestModel;
    }


    private void SendinglatlongOrderAllocation(int status) {

        if (ApplicationController.sendLatLongforOrderController != null) {
            ApplicationController.sendLatLongforOrderController = null;
        }
        ApplicationController.sendLatLongforOrderController = new SendLatLongforOrderController(mActivity);
        ApplicationController.sendLatLongforOrderController.SendLatlongToToServer(orderVisitDetailsModel.getVisitId(), status);
        ApplicationController.sendLatLongforOrderController.setOnResponseListener(new SendLatLongforOrderController.OnResponseListener() {
            @Override
            public void onSuccess(String response) {

            }

            @Override
            public void onfailure(String msg) {

            }
        });

    }

    private void CheckDelay() {
        int apptime = 0;
        int hours = new Time(System.currentTimeMillis()).getHours();
        int min = new Time(System.currentTimeMillis()).getMinutes();
        String currTime = hours + ":" + min;
        MessageLogger.PrintMsg("currtime" + currTime); //17:39
        datefrom_model = BundleConstants.ShowTimeInNotificatn; //17:30
        try {
            String[] timesplit = datefrom_model.split(":");

            int slothr = Integer.parseInt(timesplit[0]);
            int slotmin = Integer.parseInt(timesplit[1]);

            int subhr = hours - slothr;
            int submin = min - slotmin;
            if (slotmin == 00) {
                subhr = subhr - 1;
                submin = 30;
            }


            MessageLogger.PrintMsg("sub min" + submin);
            BundleConstants.delay = subhr + submin;
            BundleConstants.DoneworkOrder = 1;
            appPreferenceManager.setDelay(subhr + submin);


            MessageLogger.PrintMsg("dealy in order" + BundleConstants.delay);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //neha g ---------------------

    private void dateCheck() {
        //jai
        //minus 30 min
        Date strDate = null;
        try {
            SimpleDateFormat df = new SimpleDateFormat("hh:mm:a");
            Calendar cal = Calendar.getInstance();
            Date currentTime = cal.getTime();
            Logger.error(">> " + currentTime);
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(currentTime);
            cal1.add(Calendar.HOUR, +2);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(currentTime);
            cal2.add(Calendar.MINUTE, +150);

            //TODO Logic for rounding-off time for PPBS Sushil-------------
            int min = 0;
            if (cal1.get(Calendar.MINUTE) != 0 && cal1.get(Calendar.MINUTE) <= 30) {
                min = 30 - cal1.get(Calendar.MINUTE);
            } else if (cal1.get(Calendar.MINUTE) > 30 && cal1.get(Calendar.MINUTE) < 60) {
                min = 60 - cal1.get(Calendar.MINUTE);
            }
            cal1.add(Calendar.MINUTE, min);
             min = 0;
            if (cal2.get(Calendar.MINUTE) != 0 && cal2.get(Calendar.MINUTE) <= 30) {
                min = 30 - cal2.get(Calendar.MINUTE);
            } else if (cal2.get(Calendar.MINUTE) > 30 && cal2.get(Calendar.MINUTE) < 60) {
                min = 60 - cal2.get(Calendar.MINUTE);
            }
            cal2.add(Calendar.MINUTE, min);


            newTimeaddTwoHrs = df.format(cal1.getTime());
            newTimeaddTwoHalfHrs = df.format(cal2.getTime());
            Logger.error(">> ....." + newTimeaddTwoHrs);
            Logger.error(">> ....." + newTimeaddTwoHalfHrs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showNotiication() { //TODO CHANGE MSG
        AlarmManager alarmMgr = (AlarmManager) mActivity.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mActivity, MyBroadcastReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(mActivity, 0, intent, 0);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), alarmIntent);
    }


    private void ShowDialogToVerifyOTP() {
        isMobilenoOTPVerfied = false;
        isEmailIDOTPVerfied = false;

        CustomDialogfor_WOE_OTPValidation = new Dialog(mActivity);
        CustomDialogfor_WOE_OTPValidation.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        CustomDialogfor_WOE_OTPValidation.requestWindowFeature(Window.FEATURE_NO_TITLE);
        CustomDialogfor_WOE_OTPValidation.setContentView(R.layout.woe_otp_validation_dialog);
        CustomDialogfor_WOE_OTPValidation.setCancelable(false);

        RelativeLayout rel_main = (RelativeLayout) CustomDialogfor_WOE_OTPValidation.findViewById(R.id.rel_main);
        ImageView img_close = (ImageView) CustomDialogfor_WOE_OTPValidation.findViewById(R.id.img_close);

        edt_mobileOTP = (EditText) CustomDialogfor_WOE_OTPValidation.findViewById(R.id.edt_mobileOTP);
        edt_EmailOTP = (EditText) CustomDialogfor_WOE_OTPValidation.findViewById(R.id.edt_EmailOTP);
        btn_MobileGetOTP = (Button) CustomDialogfor_WOE_OTPValidation.findViewById(R.id.btn_MobileGetOTP);
        btn_MobileVerifyOTP = (Button) CustomDialogfor_WOE_OTPValidation.findViewById(R.id.btn_MobileVerifyOTP);
        btn_MobileVerified = (Button) CustomDialogfor_WOE_OTPValidation.findViewById(R.id.btn_MobileVerified);
        btn_EmailGetOTP = (Button) CustomDialogfor_WOE_OTPValidation.findViewById(R.id.btn_EmailGetOTP);
        btn_EmailVerifyOTP = (Button) CustomDialogfor_WOE_OTPValidation.findViewById(R.id.btn_EmailVerifyOTP);
        btn_EmailVerified = (Button) CustomDialogfor_WOE_OTPValidation.findViewById(R.id.btn_EmailVerified);
        tv_reSendMobileOTP = (TextView) CustomDialogfor_WOE_OTPValidation.findViewById(R.id.tv_reSendMobileOTP);
        tv_reSendEmailOTP = (TextView) CustomDialogfor_WOE_OTPValidation.findViewById(R.id.tv_reSendEmailOTP);
        Button btn_proceed_afterOTP = (Button) CustomDialogfor_WOE_OTPValidation.findViewById(R.id.btn_proceed_afterOTP);


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

        final String mobileNo = orderVisitDetailsModel.getAllOrderdetails().get(0).getMobile();
        final String OrderNo = orderVisitDetailsModel.getVisitId();

        CustomDialogfor_WOE_OTPValidation.show();

        MobileResendOTPcdTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                tv_reSendMobileOTP.setText("Didn't receive OTP? \nPlease Wait: " + millisUntilFinished / 1000);
                tv_reSendMobileOTP.setTextColor(getResources().getColor(R.color.black));

                tv_reSendMobileOTP.setEnabled(false);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                tv_reSendMobileOTP.setEnabled(true);
                tv_reSendMobileOTP.setTextColor(getResources().getColor(R.color.colorPrimary));
                String mystring = new String("Resend OTP");
                SpannableString content = new SpannableString(mystring);
                content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
                tv_reSendMobileOTP.setText(content);
            }
        };

        EmailResendOTPcdTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                tv_reSendEmailOTP.setText("Didn't receive OTP? \nPlease Wait: " + millisUntilFinished / 1000);
                tv_reSendEmailOTP.setTextColor(getResources().getColor(R.color.black));

                tv_reSendEmailOTP.setEnabled(false);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                tv_reSendEmailOTP.setEnabled(true);
                tv_reSendEmailOTP.setTextColor(getResources().getColor(R.color.colorPrimary));
                String mystring = new String("Resend OTP");
                SpannableString content = new SpannableString(mystring);
                content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
                tv_reSendEmailOTP.setText(content);
            }
        };

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialogfor_WOE_OTPValidation.dismiss();
            }
        });

        btn_MobileGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
                    CallGenerateOTPApi(mobileNo, "SENDOTPALL", "Mobile", OrderNo);
                } else {
                    globalclass.showCustomToast(mActivity, getResources().getString(R.string.plz_chk_internet));
                }
            }
        });

        tv_reSendMobileOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
                    CallGenerateOTPApi(mobileNo, "SENDOTPALL", "Mobile", OrderNo);
                } else {
                    globalclass.showCustomToast(mActivity, getResources().getString(R.string.plz_chk_internet));
                }
            }
        });


        btn_MobileVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strOTP = edt_mobileOTP.getText().toString().trim();
                if (InputUtils.isNull(strOTP)) {
                    globalclass.showalert_OK("Please enter OTP", mActivity);
                    edt_mobileOTP.requestFocus();
                } else if (strOTP.length() != 4) {
                    globalclass.showalert_OK("Please enter valid OTP. Length required : 4", mActivity);
                    edt_mobileOTP.requestFocus();
                } else {
                    WOEOtpValidationRequestModel model = new WOEOtpValidationRequestModel();
                    model.setOrderno(OrderNo);
                    model.setOtp(strOTP);
                    model.setOtpTo("Mobile");
                    if (cd.isConnectingToInternet()) {
                        CallValidateOTPAPI(model);
                    } else {
                        globalclass.showCustomToast(mActivity, getResources().getString(R.string.plz_chk_internet));
                    }
                }

            }
        });

        btn_EmailGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
                    CallGenerateOTPApi(mobileNo, "SENDOTPALL", "Email", OrderNo);
                } else {
                    globalclass.showCustomToast(mActivity, getResources().getString(R.string.plz_chk_internet));
                }
            }
        });

        tv_reSendEmailOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
                    CallGenerateOTPApi(mobileNo, "SENDOTPALL", "Email", OrderNo);
                } else {
                    globalclass.showCustomToast(mActivity, getResources().getString(R.string.plz_chk_internet));
                }
            }
        });


        btn_EmailVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strOTP = edt_EmailOTP.getText().toString().trim();
                if (InputUtils.isNull(strOTP)) {
                    globalclass.showalert_OK("Please enter OTP", mActivity);
                    edt_EmailOTP.requestFocus();
                } else if (strOTP.length() != 4) {
                    globalclass.showalert_OK("Please enter valid OTP. Length required : 4", mActivity);
                    edt_EmailOTP.requestFocus();
                } else {
                    WOEOtpValidationRequestModel model = new WOEOtpValidationRequestModel();
                    model.setOrderno(OrderNo);
                    model.setOtp(strOTP);
                    model.setOtpTo("Email");
                    if (cd.isConnectingToInternet()) {
                        CallValidateOTPAPI(model);
                    } else {
                        globalclass.showCustomToast(mActivity, getResources().getString(R.string.plz_chk_internet));
                    }
                }

            }
        });

        btn_proceed_afterOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMobilenoOTPVerfied) {
                    globalclass.showalert_OK("Please verify OTP for Mobile number.", mActivity);
                    edt_mobileOTP.requestFocus();
                } else if (!isEmailIDOTPVerfied) {
                    androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder;
                    alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(mActivity);
                    alertDialogBuilder
                            .setMessage("Do you want to proceed without verifying Email-ID OTP ?")
                            .setCancelable(true)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    ProceedWOEonSubmit();
                                    if (CustomDialogfor_WOE_OTPValidation != null && CustomDialogfor_WOE_OTPValidation.isShowing()) {
                                        CustomDialogfor_WOE_OTPValidation.dismiss();
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
                } else {

                    if (CustomDialogfor_WOE_OTPValidation != null && CustomDialogfor_WOE_OTPValidation.isShowing()) {
                        CustomDialogfor_WOE_OTPValidation.dismiss();
                    }
                    ProceedWOEonSubmit();
                }
            }
        });

    }

    private void CallGenerateOTPApi(String mobileNumber, String Purpose,
                                    final String OTPVia, String orderno) {

        if (ApplicationController.getAcessTokenAndOTPAPIController != null) {
            ApplicationController.getAcessTokenAndOTPAPIController = null;
        }

        ApplicationController.getAcessTokenAndOTPAPIController = new GetAcessTokenAndOTPAPIController(mActivity);
        ApplicationController.getAcessTokenAndOTPAPIController.CallGetTokenAPIForOTP(mobileNumber, Purpose, OTPVia, orderno);
        ApplicationController.getAcessTokenAndOTPAPIController.setOnResponseListener(new GetAcessTokenAndOTPAPIController.OnResponseListener() {
            @Override
            public void onSuccess(CommonPOSTResponseModel commonPOSTResponseModel) {
                onGetOTPResponseReceived(commonPOSTResponseModel, OTPVia);
            }

            @Override
            public void onfailure(CommonPOSTResponseModel commonPOSTResponseModel) {
                onGetOTPResponseReceived(commonPOSTResponseModel, OTPVia);
            }
        });
    }

    private void onGetOTPResponseReceived(CommonPOSTResponseModel model1, String OtpVia) {

        if (!InputUtils.isNull(model1.getResponse1()) && model1.getResponse1().equalsIgnoreCase("SUCCESS")) {
            if (OtpVia.equalsIgnoreCase("Mobile")) {
                btn_MobileGetOTP.setVisibility(View.GONE);
                btn_MobileVerified.setVisibility(View.GONE);
                btn_MobileVerifyOTP.setVisibility(View.VISIBLE);
                edt_mobileOTP.setVisibility(View.VISIBLE);
            } else if (OtpVia.equalsIgnoreCase("Email")) {
                btn_EmailGetOTP.setVisibility(View.GONE);
                btn_EmailVerified.setVisibility(View.GONE);
                btn_EmailVerifyOTP.setVisibility(View.VISIBLE);
                edt_EmailOTP.setVisibility(View.VISIBLE);
            }
        } else if (!InputUtils.isNull(model1.getResponse1()) && (model1.getResponse1().contains("Mailbox unavailable") || model1.getResponse1().contains("Failure sending mail"))) {
            if (OtpVia.equalsIgnoreCase("Mobile")) {
                btn_MobileGetOTP.setVisibility(View.GONE);
                btn_MobileVerified.setVisibility(View.GONE);
                btn_MobileVerifyOTP.setVisibility(View.VISIBLE);
                edt_mobileOTP.setVisibility(View.VISIBLE);

            } else if (OtpVia.equalsIgnoreCase("Email")) {
                btn_EmailGetOTP.setVisibility(View.GONE);
                btn_EmailVerified.setVisibility(View.GONE);
                btn_EmailVerifyOTP.setVisibility(View.VISIBLE);
                edt_EmailOTP.setVisibility(View.VISIBLE);
            }
        } else {
            globalclass.showalert_OK(!InputUtils.isNull(model1.getResponse1()) ? model1.getResponse1() : "Sorry we are facing issue while sending OTP. Please try again later.", mActivity);
            if (OtpVia.equalsIgnoreCase("Mobile")) {
                btn_MobileGetOTP.setVisibility(View.GONE);
                btn_MobileVerified.setVisibility(View.GONE);
                btn_MobileVerifyOTP.setVisibility(View.VISIBLE);
                edt_mobileOTP.setVisibility(View.VISIBLE);

            } else if (OtpVia.equalsIgnoreCase("Email")) {
                btn_EmailGetOTP.setVisibility(View.GONE);
                btn_EmailVerified.setVisibility(View.GONE);
                btn_EmailVerifyOTP.setVisibility(View.VISIBLE);
                edt_EmailOTP.setVisibility(View.VISIBLE);
            }
        }

        if (OtpVia.equalsIgnoreCase("Mobile")) {
            MobileResendOTPcdTimer.start();
            tv_reSendMobileOTP.setVisibility(View.VISIBLE);
        } else if (OtpVia.equalsIgnoreCase("Email")) {
            EmailResendOTPcdTimer.start();
            tv_reSendMobileOTP.setVisibility(View.VISIBLE);
        }

    }

    private void openCamera() {

        Global.cropImageFullScreenActivity(mActivity, 3);

       /* buildCamera();
        try {
            camera.takePicture();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    private void buildCamera() {
        camera = new Camera.Builder()
                .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(1)
                .setDirectory("pics")
                .setName("img" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_PNG)
                .setCompression(AppConstants.setcompression)
                .setImageHeight(AppConstants.setheight)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                .build(this);
    }

    private void selectImage() {
        try {
            final CharSequence[] items = {"Take Photo", "Cancel"};
            final String[] userChoosenTask = {""};
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mActivity);
            builder.setTitle("Add Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {

                    if (items[item].equals("Take Photo")) {
                        userChoosenTask[0] = "Take Photo";
                        TedPermission.with(mActivity)
                                .setPermissions(Manifest.permission.CAMERA)
                                .setRationaleMessage("We need permission to capture photo from your camera to Upload Vail Photo.")
                                .setRationaleConfirmText("OK")
                                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > Permission > Camera")
                                .setPermissionListener(new PermissionListener() {
                                    @Override
                                    public void onPermissionGranted() {
                                        openCamera();
                                    }

                                    @Override
                                    public void onPermissionDenied(List<String> deniedPermissions) {
                                        Toast.makeText(mActivity, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }).check();

                    } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CallValidateOTPAPI(final WOEOtpValidationRequestModel model) {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<CommonPOSTResponseModel> responseCall = apiInterface.ValidateWoeOTPAPI(model);
        globalclass.showProgressDialog(mActivity, "Validating OTP. Please wait..");
        responseCall.enqueue(new Callback<CommonPOSTResponseModel>() {
            @Override
            public void onResponse(Call<CommonPOSTResponseModel> call, Response<CommonPOSTResponseModel> response) {
                globalclass.hideProgressDialog(mActivity);
                if (response.isSuccessful() && response.body() != null) {
                    CommonPOSTResponseModel ResponseModel = response.body();
                    if (ResponseModel != null && !InputUtils.isNull(ResponseModel.getResponse1()) && ResponseModel.getResponse1().equalsIgnoreCase("SUCCESS")) {

                        if (model.getOtpTo().equalsIgnoreCase("Mobile")) {
                            btn_MobileVerified.setVisibility(View.VISIBLE);
                            btn_MobileVerifyOTP.setVisibility(View.GONE);
                            btn_MobileGetOTP.setVisibility(View.GONE);
                            edt_mobileOTP.setEnabled(false);
                            isMobilenoOTPVerfied = true;
                        } else if (model.getOtpTo().equalsIgnoreCase("Email")) {
                            btn_EmailVerified.setVisibility(View.VISIBLE);
                            btn_EmailVerifyOTP.setVisibility(View.GONE);
                            btn_EmailGetOTP.setVisibility(View.GONE);
                            edt_EmailOTP.setEnabled(false);
                            isEmailIDOTPVerfied = true;
                        }

                    } else {
                        globalclass.showCustomToast(mActivity, "OTP Validation Failed. Please enter Valid OTP.");
                    }
                } else {
                    globalclass.showCustomToast(mActivity, "Unable to connect to the server. Please try after sometime.");

                }
            }

            @Override
            public void onFailure(Call<CommonPOSTResponseModel> call, Throwable t) {
                globalclass.hideProgressDialog(mActivity);
                globalclass.showCustomToast(mActivity, "Something went wrong. Please try after sometime.");
            }
        });

    }

    public void getCollectioncenters(GetCollectionRespModel body) {
        if (!InputUtils.isNull(body.getRespId()) && body.getRespId().equalsIgnoreCase("RES00001")) {
            GetCollectionRespModel.CenterListDTO centerListDTO = new GetCollectionRespModel.CenterListDTO();
            centerListDTO.setCenterName("Select Collection Center");
            body.getCenterList().add(0, centerListDTO);

            getcollearray = body.getCenterList();
            if (!InputUtils.isNull(body.getCenterList())) {
                ArrayAdapter<GetCollectionRespModel.CenterListDTO> spinnerArrayAdapter = new ArrayAdapter<>(mActivity, android.R.layout.simple_spinner_item, body.getCenterList());
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_collection.setAdapter(spinnerArrayAdapter);
            }

        } else {
            globalclass.showCustomToast(mActivity, body.getResponse());
        }
    }

    private void ShowSelfieDialog() {
        try {

            BottomSheetController bottomSheetController = new BottomSheetController(mActivity, CheckoutWoeActivity.this, uri);
            bottomSheetController.SetBottomSheet(mActivity);

//            final Dialog maindialog = new Dialog(mActivity);
//            maindialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//            maindialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            maindialog.setContentView(R.layout.preview_dialog);
//            maindialog.getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//            ImageView imageView = maindialog.findViewById(R.id.viewPager);
//            imageView.setImageURI(uri);
//            ImageView ic_close = (ImageView) maindialog.findViewById(R.id.img_close);
//            ic_close.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    maindialog.dismiss();
//                }
//            });
//
//
//            maindialog.setCancelable(true);
//            maindialog.show();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    public void getSelfieResponse() {
        canSubmit = true;
    }

    public void getSubmitDataResponse(GetTestResponseModel responseModel) {
        if (responseModel != null && responseModel.getResponse() != null) {
            if (responseModel.getProcessAt() != null) {
                ll_Note.setVisibility(View.VISIBLE);
                displayNote(responseModel);
            } else {
                ll_Note.setVisibility(View.GONE);
                tv_note.setText("");
            }
        } else {
            globalclass.showCustomToast(this, SOMETHING_WENT_WRONG);
        }
    }

    private void displayNote(GetTestResponseModel responseModel) {
        if (InputUtils.CheckEqualIgnoreCase(responseModel.getProcessAt(), ConstantsMessages.RPL)) {
            tv_note.setText("Your order will be processed at " + "" + responseModel.getProcessAt());
//            tv_note.setTextColor(getResources().getColor(R.color.blue_shade));
            processLocation = "" + responseModel.getProcessAt().toString().trim();
        } else if (InputUtils.CheckEqualIgnoreCase(responseModel.getProcessAt(), ConstantsMessages.CPL)) {
            tv_note.setText("Your order will be processed at " + "" + responseModel.getProcessAt());
//            tv_note.setTextColor(getResources().getColor(R.color.highlight_color));
            processLocation = "" + responseModel.getProcessAt().toString().trim();
        } else if (InputUtils.CheckEqualIgnoreCase(responseModel.getProcessAt(), ConstantsMessages.ZPL)) {
            tv_note.setText("Your order will be processed at " + "" + responseModel.getProcessAt());
//            tv_note.setTextColor(getResources().getColor(R.color.sample_type_serum));
            processLocation = "" + responseModel.getProcessAt().toString().trim();
        } else {
            ll_Note.setVisibility(View.GONE);
            tv_note.setText("");
            processLocation = "";
        }
    }

    public void getbottomsheetresponse(BottomSheetDialog bottomSheetDialog) {
        bottomSheetDialog.dismiss();
    }

    public void getPayTypeResponse(PaytypeResponseModel responseModel) {
        if (responseModel.getPaytype() != null) {
            strPaytype = responseModel.getPaytype();
            WOE();
        }
    }
}