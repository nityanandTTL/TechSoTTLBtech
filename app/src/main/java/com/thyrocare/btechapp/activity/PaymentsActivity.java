package com.thyrocare.btechapp.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;
import com.thyrocare.btechapp.Controller.PayTMController;
import com.thyrocare.btechapp.Controller.PayTMVerifyController;
import com.thyrocare.btechapp.Controller.WOEController;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.CheckoutWoeActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.ScanBarcodeWoeActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConnectionDetector;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.adapter.PaymentDetailsAdapter;
import com.thyrocare.btechapp.models.api.request.OrderBookingRequestModel;
import com.thyrocare.btechapp.models.api.request.PEPaymentRequestModel;
import com.thyrocare.btechapp.models.api.request.PayTMRequestModel;
import com.thyrocare.btechapp.models.api.request.PayTMVerifyRequestModel;
import com.thyrocare.btechapp.models.api.response.PEPaymentResponseModel;
import com.thyrocare.btechapp.models.api.response.PayTMResponseModel;
import com.thyrocare.btechapp.models.api.response.PayTMVerifyResponseModel;
import com.thyrocare.btechapp.models.api.response.PaymentDoCaptureResponseAPIResponseModel;
import com.thyrocare.btechapp.models.api.response.PaymentProcessAPIResponseModel;
import com.thyrocare.btechapp.models.api.response.PaymentStartTransactionAPIResponseModel;
import com.thyrocare.btechapp.models.data.BeneficiaryBarcodeDetailsModel;
import com.thyrocare.btechapp.models.data.BeneficiaryDetailsModel;
import com.thyrocare.btechapp.models.data.BeneficiaryLabAlertsModel;
import com.thyrocare.btechapp.models.data.BeneficiarySampleTypeDetailsModel;
import com.thyrocare.btechapp.models.data.BeneficiaryTestWiseClinicalHistoryModel;
import com.thyrocare.btechapp.models.data.OrderBookingDetailsModel;
import com.thyrocare.btechapp.models.data.OrderDetailsModel;
import com.thyrocare.btechapp.models.data.OrderVisitDetailsModel;
import com.thyrocare.btechapp.models.data.PaymentNameValueModel;
import com.thyrocare.btechapp.service.CheckPaymentResponseService;
import com.thyrocare.btechapp.uiutils.AbstractActivity;
import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.AppConstants;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.CommonUtils;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.RES0000;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SOMETHING_WENT_WRONG;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;

/**
 * Created by Orion on 5/16/2017.
 */

public class PaymentsActivity extends AbstractActivity {
    LinearLayout llPayWithCashTitle, llPayWithCash, llPaymentModes, llPaymentGateway;
    private OrderVisitDetailsModel orderDetailsModel;
    private Activity activity;
    private AppPreferenceManager appPreferenceManager;
    private FrameLayout flPayments;
    private ArrayList<PaymentProcessAPIResponseModel> paymentModesArr;
    private PaymentProcessAPIResponseModel paymentPassInputsModel;
    private int NarrationId = 0;
    private int PaymentModesFlag = 2;
    private String OrderNo = "";
    private String Amount = "";
    private int SourceCode = 0;
    private String BillingName = "";
    private String BillingAddr = "";
    private String BillingPin = "";
    private String BillingMob = "";
    private String BillingEmail = "";
    private PaymentStartTransactionAPIResponseModel paymentStartTransactionAPIResponseModel;
    private PaymentDoCaptureResponseAPIResponseModel paymentDoCaptureResponseAPIResponseModel;
    private boolean doubleBackToExitPressedOnce = false;
    int buttonDecider = 0;
    private int ModeId = 0;
    EditText edtPaymentUserInputs;
    private Global global;
    ConnectionDetector cd;
    TextView tv_toolbar;
    ImageView iv_back, iv_home;
    CheckoutWoeActivity checkoutWoeActivity;
    ArrayList<BeneficiaryDetailsModel> beneficaryWiseArylst;
    private RecyclerView recy_paymentmode;
    private int PaymentMode;
    private int totalAmountPayable = 0;
    String test, location, address, newTimeaddTwoHrs = "", newTimeaddTwoHalfHrs = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        activity = this;
        cd = new ConnectionDetector(activity);
        global = new Global(activity);
        appPreferenceManager = new AppPreferenceManager(activity);

        if (getIntent().getExtras() != null) {
            PaymentModesFlag = getIntent().getExtras().getInt(BundleConstants.PAYMENTS_OPTION_FLAG);
            NarrationId = getIntent().getExtras().getInt(BundleConstants.PAYMENTS_NARRATION_ID);
            OrderNo = getIntent().getExtras().getString(BundleConstants.PAYMENTS_ORDER_NO);
            Amount = getIntent().getExtras().getString(BundleConstants.PAYMENTS_AMOUNT);
            SourceCode = getIntent().getExtras().getInt(BundleConstants.PAYMENTS_SOURCE_CODE);
            BillingName = getIntent().getExtras().getString(BundleConstants.PAYMENTS_BILLING_NAME);
            BillingAddr = getIntent().getExtras().getString(BundleConstants.PAYMENTS_BILLING_ADDRESS);
            BillingPin = getIntent().getExtras().getString(BundleConstants.PAYMENTS_BILLING_PIN);
            BillingMob = getIntent().getExtras().getString(BundleConstants.PAYMENTS_BILLING_MOBILE);
            location = getIntent().getExtras().getString(BundleConstants.PROCESSING_LOCATION);
            address = getIntent().getExtras().getString(BundleConstants.ADDRESS);
            newTimeaddTwoHrs = getIntent().getExtras().getString(BundleConstants.newTimeTWO);
            newTimeaddTwoHalfHrs = getIntent().getExtras().getString(BundleConstants.newTimeTWOHalf);

            orderDetailsModel = getIntent().getExtras().getParcelable(BundleConstants.VISIT_ORDER_DETAILS_MODEL);
            beneficaryWiseArylst = getIntent().getExtras().getParcelableArrayList(BundleConstants.BENEFICIARY_DETAILS_MODEL);

            Logger.error("BillingMob " + BillingMob);
            BillingEmail = getIntent().getExtras().getString(BundleConstants.PAYMENTS_BILLING_EMAIL);
        }
        initUI();
        fetchPaymentModes();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {

            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetTheme);

            View bottomSheet = LayoutInflater.from(this).inflate(R.layout.logout_bottomsheet, (ViewGroup) this.findViewById(R.id.bottom_sheet_dialog_parent));
            TextView tv_text = bottomSheet.findViewById(R.id.tv_text);
            tv_text.setText("You are trying to close the payments");
            Button btn_yes = bottomSheet.findViewById(R.id.btn_yes);
            btn_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                    finish();

                }
            });

            Button btn_no = bottomSheet.findViewById(R.id.btn_no);
            btn_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();

                }
            });
            bottomSheetDialog.setContentView(bottomSheet);
            bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.show();
        } else {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit payments", Toast.LENGTH_SHORT).show();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public void initUI() {
        super.initUI();
        flPayments = (FrameLayout) findViewById(R.id.fl_payments);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        iv_back = findViewById(R.id.iv_back);
        iv_home = findViewById(R.id.iv_home);
        tv_toolbar.setText("Payment");
        tv_toolbar.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        iv_home.setVisibility(View.GONE);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheetTheme);

                View bottomSheet = LayoutInflater.from(activity).inflate(R.layout.logout_bottomsheet, (ViewGroup) activity.findViewById(R.id.bottom_sheet_dialog_parent));
                TextView tv_text = bottomSheet.findViewById(R.id.tv_text);
                tv_text.setText("You are trying to close the payments");
                Button btn_yes = bottomSheet.findViewById(R.id.btn_yes);
                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                        finish();

                    }
                });

                Button btn_no = bottomSheet.findViewById(R.id.btn_no);
                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();

                    }
                });
                bottomSheetDialog.setContentView(bottomSheet);
                bottomSheetDialog.setCancelable(false);
                bottomSheetDialog.show();
            }
        });

        for (OrderDetailsModel orderDetailsModel : orderDetailsModel.getAllOrderdetails()) {
            totalAmountPayable = totalAmountPayable + orderDetailsModel.getAmountPayable();
        }
    }


    private void fetchPaymentModes() {
        if (isNetworkAvailable(activity)) {
            CallFetchPaymentModesApi(NarrationId);
        } else {
            Toast.makeText(activity, getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void callPEPaymentModeAPI(final PEPaymentRequestModel pePaymentRequestModel) {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<PEPaymentResponseModel> responseCall = apiInterface.PEVerifyPayment(pePaymentRequestModel);
        global.showProgressDialog(activity, "Please wait..");
        responseCall.enqueue(new Callback<PEPaymentResponseModel>() {
            @Override
            public void onResponse(Call<PEPaymentResponseModel> call, retrofit2.Response<PEPaymentResponseModel> response) {
                global.hideProgressDialog(activity);
                if (response.isSuccessful() && response.body() != null) {
                    if (InputUtils.CheckEqualIgnoreCase(response.body().getResponseCode(), Constants.RES000)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle("Payment Status")
                                .setCancelable(false)
                                .setMessage(response.body().getResponseMessage())
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent();
                                        intent.putExtra(BundleConstants.PAYMENT_STATUS, true);
                                        setResult(BundleConstants.PAYMENTS_FINISH, intent);
                                        finish();
                                    }
                                }).show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle("Verify Payment")
                                .setMessage(response.body().getResponseMessage())
                                .setCancelable(false)
                                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        callPEPaymentModeAPI(pePaymentRequestModel);
                                    }
                                }).setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent();
                                intent.putExtra(BundleConstants.PAYMENT_STATUS, false);
                                setResult(BundleConstants.PAYMENTS_FINISH, intent);
                                finish();
                            }
                        }).show();
                    }
                } else {
                    Toast.makeText(activity, SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PEPaymentResponseModel> call, Throwable t) {
                global.hideProgressDialog(activity);
                global.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });
    }


    private void CallFetchPaymentModesApi(int NarrationId) {
        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<ArrayList<PaymentProcessAPIResponseModel>> responseCall = apiInterface.CallFetchPaymentModesApi(NarrationId);
        global.showProgressDialog(activity, "Please wait..");
        responseCall.enqueue(new Callback<ArrayList<PaymentProcessAPIResponseModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PaymentProcessAPIResponseModel>> call, retrofit2.Response<ArrayList<PaymentProcessAPIResponseModel>> response) {
                global.hideProgressDialog(activity);
                if (response.isSuccessful() && response.body() != null) {
                    paymentModesArr = response.body();
                    initPaymentModesData();
                } else {
                    Toast.makeText(activity, SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PaymentProcessAPIResponseModel>> call, Throwable t) {
                global.hideProgressDialog(activity);
                global.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });
    }

    private void initPaymentModesData() {
        if (paymentModesArr != null && paymentModesArr.size() > 0) {

            ScrollView scroll = new ScrollView(activity);
            scroll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

            LinearLayout llPaymentModesFinal = new LinearLayout(activity);
            llPaymentModesFinal.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            llPaymentModesFinal.setOrientation(LinearLayout.VERTICAL);

            //TODO Payable amount Textview
            final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(20, 25, 10, 25);
            TextView PayableAmount = new TextView(activity);
            PayableAmount.setText("Payable Amount : " + activity.getResources().getString(R.string.rupee_symbol) + " " + Amount + "/-");
            PayableAmount.setTextSize(18);
            PayableAmount.setTextColor(getResources().getColor(R.color.bg_new_color));
            PayableAmount.setLayoutParams(params);
            llPaymentModesFinal.addView(PayableAmount);

            //TODO Payment Gateway Title
            llPaymentGateway = new LinearLayout(activity);
            llPaymentGateway.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            llPaymentGateway.setBackgroundResource(R.drawable.grey_rect_grey_stroke_bg);

            TextView tv_payment_gateway_title = new TextView(activity);
            tv_payment_gateway_title.setLayoutParams(params);
            tv_payment_gateway_title.setText(R.string.PaymentGateway);
            tv_payment_gateway_title.setTextColor(getResources().getColor(R.color.black));
            tv_payment_gateway_title.setTextSize(18);
            tv_payment_gateway_title.setLayoutParams(params);
            llPaymentGateway.addView(tv_payment_gateway_title);
            llPaymentModesFinal.addView(llPaymentGateway);

            //TODO Payment icons
            llPaymentModes = new LinearLayout(activity);
            llPaymentModes.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            llPaymentModes.setOrientation(LinearLayout.VERTICAL);

            //TODO Payment Gateways view
            SetpaymentGateways(paymentModesArr);

            llPaymentModesFinal.addView(llPaymentModes);

            llPayWithCashTitle = new LinearLayout(activity);
            llPayWithCashTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            llPayWithCashTitle.setBackgroundResource(R.drawable.grey_rect_grey_stroke_bg);


            TextView tvPayWithCash = new TextView(activity);
            tvPayWithCash.setLayoutParams(params);
            tvPayWithCash.setText(R.string.PayWithCash);
            tvPayWithCash.setTextColor(getResources().getColor(R.color.bg_new_color));
            tvPayWithCash.setTextSize(18);
            tvPayWithCash.setLayoutParams(params);
            tvPayWithCash.setLayoutParams(params);
            llPayWithCashTitle.addView(tvPayWithCash);
            llPaymentModesFinal.addView(llPayWithCashTitle);

            //TODO Pay with Cash Icon

            llPayWithCash = new LinearLayout(activity);
            llPayWithCash.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            llPayWithCash.setOrientation(LinearLayout.HORIZONTAL);

            ImageView IVPayWithCash = new ImageView(activity);
            int ic_width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.25);
            LinearLayout.LayoutParams iv_params = new LinearLayout.LayoutParams(ic_width, ic_width);
            iv_params.setMargins(10, 10, 10, 10);
            IVPayWithCash.setLayoutParams(iv_params);
            IVPayWithCash.setScaleType(ImageView.ScaleType.FIT_CENTER);
            IVPayWithCash.setBackground(getResources().getDrawable(R.drawable.ic_payment_via_cash));
            llPayWithCash.addView(IVPayWithCash);

            //TODO Pay with Cash Caption
            LinearLayout llPayCashDesc = new LinearLayout(activity);
            llPayCashDesc.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            llPayCashDesc.setOrientation(LinearLayout.VERTICAL);


            LinearLayout.LayoutParams llCashDesc = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            llCashDesc.setMargins(30, 20, 10, 0);

            TextView TvCashDesc = new TextView(activity);
            TvCashDesc.setText("Cash");
            TvCashDesc.setTypeface(TvCashDesc.getTypeface(), Typeface.BOLD);
            TvCashDesc.setTextSize(16);
            TvCashDesc.setGravity(Gravity.CENTER_VERTICAL);
            TvCashDesc.setTextColor(getResources().getColor(R.color.bg_new_color));
            TvCashDesc.setLayoutParams(llCashDesc);

            //Sub description
            LinearLayout.LayoutParams lpSubDesc = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lpSubDesc.setMargins(30, 0, 10, 0);

            TextView TVDescCash = new TextView(activity);
            TVDescCash.setText("Online payment recommended to reduce contact between you and technician");
            TVDescCash.setTextSize(14);
            TVDescCash.setGravity(Gravity.CENTER_VERTICAL);
            TVDescCash.setTextColor(getResources().getColor(R.color.black));
            TVDescCash.setLayoutParams(lpSubDesc);

            llPayCashDesc.addView(TvCashDesc);
            llPayCashDesc.addView(TVDescCash);
            llPayWithCash.addView(llPayCashDesc);

            llPaymentModesFinal.addView(llPayWithCash);

            scroll.addView(llPaymentModesFinal);
            flPayments.addView(scroll);

            //TODO onclick For cash Option

            llPayWithCash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                    builder1.setMessage("Confirm amount received ₹ " + Amount + "")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    BundleConstants.addPaymentFlag = 1;
                                    Intent intentOrderBooking = new Intent(activity, ScanBarcodeWoeActivity.class);
                                    intentOrderBooking.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL, orderDetailsModel);
                                    startActivity(intentOrderBooking);
                                    finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            });

            ShowingViewsAsPerFlag();
        }
    }

    private void SetpaymentGateways(ArrayList<PaymentProcessAPIResponseModel> paymentModesArr) {
        try {

            if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName()) || orderDetailsModel.getAllOrderdetails().get(0).isPEPartner()) {
                /*ArrayList<PaymentProcessAPIResponseModel> NewPaymentModesArr = new ArrayList<PaymentProcessAPIResponseModel>();
                for (int i = 0; i <paymentModesArr.size(); i++) {
                    for (int j = 0; j < paymentModesArr.get(i).getNameValueCollection().size(); j++) {
                        if (paymentModesArr.get(i).getNameValueCollection().get(j).getValue().equalsIgnoreCase("Paytm")){
                            NewPaymentModesArr.add(paymentModesArr.get(i));
                        }
                    }
                }
                paymentModesArr.clear();
                paymentModesArr = NewPaymentModesArr;*/

                View ViewPaytmLink = activity.getLayoutInflater().inflate(R.layout.paymentsgateway_row_items, null);
                TextView txt_paymentModename = (TextView) ViewPaytmLink.findViewById(R.id.txt_paymentModename);
                ImageView img_ident = (ImageView) ViewPaytmLink.findViewById(R.id.img_ident);
                LinearLayout ll_rowmainelmnt = (LinearLayout) ViewPaytmLink.findViewById(R.id.ll_rowmainelmnt);

                img_ident.setBackground(activity.getResources().getDrawable(R.drawable.ic_olpay));
                txt_paymentModename.setText("Send payment link");

                ll_rowmainelmnt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fetchTransactionResponsePEonStartTransaction();
//                        fetchTransactionResponseOnStartTransaction();
//                        callPaytmLinkAPI();
                    }
                });

                llPaymentModes.addView(ViewPaytmLink);
            } else {
                View dynamicListview = activity.getLayoutInflater().inflate(R.layout.payment_recy, null);
                recy_paymentmode = (RecyclerView) dynamicListview.findViewById(R.id.recy_paymentmode);

                LinearLayoutManager lm1 = new LinearLayoutManager(activity);
                recy_paymentmode.setLayoutManager(lm1);
                recy_paymentmode.setHasFixedSize(true);
                PaymentDetailsAdapter adpter_inc = new PaymentDetailsAdapter(PaymentsActivity.this, paymentModesArr);
                recy_paymentmode.setAdapter(adpter_inc);
                llPaymentModes.addView(dynamicListview);

                View ViewPaytmLink = activity.getLayoutInflater().inflate(R.layout.paymentsgateway_row_items, null);
                TextView txt_paymentModename = (TextView) ViewPaytmLink.findViewById(R.id.txt_paymentModename);
                ImageView img_ident = (ImageView) ViewPaytmLink.findViewById(R.id.img_ident);
                LinearLayout ll_rowmainelmnt = (LinearLayout) ViewPaytmLink.findViewById(R.id.ll_rowmainelmnt);
                img_ident.setBackground(activity.getResources().getDrawable(R.drawable.ic_paytm_logo_n));
                txt_paymentModename.setText("Send link on SMS");

                ll_rowmainelmnt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callPaytmLinkAPI();
                    }
                });

                llPaymentModes.addView(ViewPaytmLink);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchTransactionResponsePEonStartTransaction() {
        JsonObject jsonRequest = new JsonObject();
        try {
            jsonRequest.addProperty("SourceCode", appPreferenceManager.getLoginResponseModel().getUserID());
            jsonRequest.addProperty("UserId", appPreferenceManager.getLoginResponseModel().getUserID());
            jsonRequest.addProperty("Amount", totalAmountPayable);
            jsonRequest.addProperty("OrderNo", OrderNo);
            jsonRequest.addProperty("TransactionDtls", "");
            jsonRequest.addProperty("ACCode", "");
            jsonRequest.addProperty("ModeId", 3);

//            {"URLId":9,"NarrationId":"2","ModeId":"3","Amount":"1","OrderNo":"VLF532DC","SourceCode":"884543141","ACCode":"","TransactionDtls":"","UserId":"884543141"}

            if (isNetworkAvailable(activity)) {
                CallFetchTransactionResponseOnStartTransactionApi(jsonRequest);
            } else {
                Toast.makeText(activity, getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callPaytmLinkAPI() {
        try {
            if (cd.isConnectingToInternet()) {
                PayTMRequestModel payTMRequestModel = new PayTMRequestModel();
                payTMRequestModel.setEntryBy(appPreferenceManager.getLoginResponseModel().getUserID());
                payTMRequestModel.setOrderno(OrderNo);
                PayTMController payTMController = new PayTMController(this);
                payTMController.payTM(payTMRequestModel);
            } else {
                Global.showCustomStaticToast(activity, SOMETHING_WENT_WRONG);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ShowingViewsAsPerFlag() {
        if (PaymentModesFlag == 0) {
            llPayWithCashTitle.setVisibility(View.GONE);
            llPayWithCash.setVisibility(View.GONE);
        } else if (PaymentModesFlag == 1) {
            //Shows only cash option
            llPaymentModes.setVisibility(View.GONE);
            llPaymentGateway.setVisibility(View.GONE);
        }

    }

    private void fetchPaymentPassInputs(PaymentProcessAPIResponseModel pparm) {
        JsonObject jsonRequest = new JsonObject();
        try {
            jsonRequest.addProperty("URLId", pparm.getURLId());
            for (PaymentNameValueModel pnvm :
                    pparm.getNameValueCollection()) {
                if (pnvm.getKey().equals("ModeId")) {
                    ModeId = Integer.parseInt(pnvm.getValue());
                }
                jsonRequest.addProperty(pnvm.getKey(), pnvm.getValue());
            }
            if (isNetworkAvailable(activity)) {
                CallgetTransactionInputsRequestApi(jsonRequest);
            } else {
                Toast.makeText(activity, getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchPaymentClickDetails(PaymentProcessAPIResponseModel pparm) {
        fetchPaymentPassInputs(pparm);
    }

    private void CallgetTransactionInputsRequestApi(JsonObject jsonRequest) {
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<PaymentProcessAPIResponseModel> responseCall = apiInterface.CallgetTransactionInputsRequestApi(jsonRequest);
        global.showProgressDialog(activity, "Please wait..");
        responseCall.enqueue(new Callback<PaymentProcessAPIResponseModel>() {
            @Override
            public void onResponse(Call<PaymentProcessAPIResponseModel> call, Response<PaymentProcessAPIResponseModel> response) {
                global.hideProgressDialog(activity);
                if (response.isSuccessful()) {
                    paymentPassInputsModel = response.body();
                    initPaymentPassInputsData();
                } else {
                    Toast.makeText(activity, ConstantsMessages.SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PaymentProcessAPIResponseModel> call, Throwable t) {
                global.hideProgressDialog(activity);
            }
        });
    }


    private void initPaymentPassInputsData() {
        if (paymentPassInputsModel != null
                && paymentPassInputsModel.getNameValueCollection() != null
                && paymentPassInputsModel.getNameValueCollection().size() > 0) {

            View v = activity.getLayoutInflater().inflate(R.layout.paymentsdesign, null);
            LinearLayout llPaymentPassInputs = (LinearLayout) v.findViewById(R.id.ll_payments_pass_inputs_data);

            final EditText editAmount = (EditText) v.findViewById(R.id.amount1);
            TextView textAmount = (TextView) v.findViewById(R.id.amount);
            final Button btnPaymentInputsSubmit = new Button(activity);

            for (int i = 0; i < paymentPassInputsModel.getNameValueCollection().size(); i++) {
                final int currentPosition = i;
                if (paymentPassInputsModel.getNameValueCollection().get(i).getRequired().equals("User")) {
                    if (!paymentPassInputsModel.getNameValueCollection().get(i).getKey().equals("Amount")) {
                        View v1 = activity.getLayoutInflater().inflate(R.layout.payment_edit_text, null);
                        final EditText edtPaymentUserInputs = (EditText) v1.findViewById(R.id.edit_payment);
                        TextView txtPaymentUserInputss = (TextView) v1.findViewById(R.id.payment_text);
                        txtPaymentUserInputss.setText(paymentPassInputsModel.getNameValueCollection().get(i).getHint());
                        Logger.error("name " + paymentPassInputsModel.getNameValueCollection().get(i).getHint());

                        if (paymentPassInputsModel.getNameValueCollection().get(i).getHint().equalsIgnoreCase("Mobile")) {
                            paymentPassInputsModel.getNameValueCollection().get(i).setValue(BillingMob);
                            edtPaymentUserInputs.setHint("Please Enter " + paymentPassInputsModel.getNameValueCollection().get(i).getHint() + "*");
                        } else {
                            editAmount.setHint(paymentPassInputsModel.getNameValueCollection().get(i).getHint());

                        }


                        edtPaymentUserInputs.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                //paymentPassInputsModel.getNameValueCollection().get(currentPosition).setValue(s.toString());

                                String remarks = s.toString();

                                if (edtPaymentUserInputs.getHint().equals("Please Enter Remarks")) {
                                    if (remarks.contains("\'")) {
                                        edtPaymentUserInputs.setError("Apostroph symbol ( ' ) is not allowed.");
                                        edtPaymentUserInputs.setText("");
                                    } else
                                        paymentPassInputsModel.getNameValueCollection().get(currentPosition).setValue(s.toString());
                                }


                                if (edtPaymentUserInputs.getHint().toString().equalsIgnoreCase("Please Enter Mobile*")) {

                                    if (remarks.contains(" ")) {
                                        edtPaymentUserInputs.setError("Mobile No Cannot Be Empty");
                                        edtPaymentUserInputs.setText("");

                                    } else if (remarks.length() != 10) {
                                        edtPaymentUserInputs.setError("Mobile Should be 10 digits");
                                        edtPaymentUserInputs.setText("");

                                    } else {
                                        paymentPassInputsModel.getNameValueCollection().get(currentPosition).setValue(s.toString());
                                    }

                                }
                                //for VPA
                                if (edtPaymentUserInputs.getHint().equals("Please Enter VPA")) {
                                    if (remarks.contains(" ")) {
                                        edtPaymentUserInputs.setError("Space is not allowed.");
                                        edtPaymentUserInputs.setText("");
                                    } else
                                        paymentPassInputsModel.getNameValueCollection().get(currentPosition).setValue(s.toString());
                                }
                            }
                        });

//                        llPaymentPassInputs.addView(v1);
                    } else {

                        textAmount.setText(paymentPassInputsModel.getNameValueCollection().get(i).getHint());
                        textAmount.setVisibility(View.GONE);
                        editAmount.setHint(paymentPassInputsModel.getNameValueCollection().get(i).getHint());
                        editAmount.setVisibility(View.VISIBLE);
                        editAmount.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                if (!InputUtils.isNull(s.toString())) {
                                    try {
                                        btnPaymentInputsSubmit.setEnabled(false);
                                        int amountPayable = Integer.parseInt(s.toString());
                                        if (amountPayable > 0) {
                                            if (NarrationId == 3 && amountPayable < 2000) {//amounPayable<2000
                                                editAmount.requestFocus();
                                                editAmount.setError("Amount Should be greater than Rs 2000/-");
                                            } else {
                                                btnPaymentInputsSubmit.setEnabled(true);
                                                paymentPassInputsModel.getNameValueCollection().get(currentPosition).setValue(amountPayable + "");
                                            }
                                        } else {
                                            editAmount.requestFocus();
                                            editAmount.setError("Please enter Valid Amount");
                                        }
                                    } catch (Exception e) {
                                        editAmount.requestFocus();
                                        editAmount.setError("Please enter Valid Amount");
                                        e.printStackTrace();
                                    }
                                } else {
                                    editAmount.requestFocus();
                                    editAmount.setError("Amount Cannot be Empty");
                                }
                            }
                        });
                    }
                    // llPaymentPassInputs.addView(edtPaymentUserInputs);
                } else if (paymentPassInputsModel.getNameValueCollection().get(i).getRequired().equals("System")) {
                    if (paymentPassInputsModel.getNameValueCollection().get(i).getKey().equals("NarrationId")) {
                        paymentPassInputsModel.getNameValueCollection().get(i).setValue(NarrationId + "");
                    } else if (paymentPassInputsModel.getNameValueCollection().get(i).getKey().equals("OrderNo")) {
                        paymentPassInputsModel.getNameValueCollection().get(i).setValue(OrderNo);
                    } else if (paymentPassInputsModel.getNameValueCollection().get(i).getKey().equals("SourceCode")) {
                        paymentPassInputsModel.getNameValueCollection().get(i).setValue(SourceCode + "");
                    } else if (paymentPassInputsModel.getNameValueCollection().get(i).getKey().equals("Amount")) {
                        paymentPassInputsModel.getNameValueCollection().get(i).setValue(Amount + "");
                    } else if (paymentPassInputsModel.getNameValueCollection().get(i).getKey().equals("BillingName")) {
                        paymentPassInputsModel.getNameValueCollection().get(i).setValue(BillingName);
                    } else if (paymentPassInputsModel.getNameValueCollection().get(i).getKey().equals("BillingPin")) {
                        paymentPassInputsModel.getNameValueCollection().get(i).setValue(BillingPin + "");
                    } else if (paymentPassInputsModel.getNameValueCollection().get(i).getKey().equals("BillingMob")) {
                        paymentPassInputsModel.getNameValueCollection().get(i).setValue(BillingMob);
                    } else if (paymentPassInputsModel.getNameValueCollection().get(i).getKey().equals("BillingAddr")) {
                        paymentPassInputsModel.getNameValueCollection().get(i).setValue(BillingAddr + "");
                    } else if (paymentPassInputsModel.getNameValueCollection().get(i).getKey().equals("BillingEmail")) {
                        paymentPassInputsModel.getNameValueCollection().get(i).setValue(BillingEmail + "");

                    }
                }
            }


            fetchTransactionResponseOnStartTransaction();

        }
    }

    private boolean getMobileValidate(String mobile) {


        if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(activity, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (mobile.length() != 10) {
            Toast.makeText(activity, "Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (mobile.startsWith("0") || mobile.startsWith("1") || mobile.startsWith("2") || mobile.startsWith("3") || mobile.startsWith("4") || mobile.startsWith("5")) {
            Toast.makeText(activity, "Mobile Number Cannot start with 0,1,2,3,4,5", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void fetchTransactionResponseOnStartTransaction() {
        JsonObject jsonRequest = new JsonObject();
        try {
            // TODO only for testing

            jsonRequest.addProperty("URLId", paymentPassInputsModel.getURLId());
            for (PaymentNameValueModel pnvm : paymentPassInputsModel.getNameValueCollection()) {
                if (pnvm.getKey().equals("SourceCode")) {
                    jsonRequest.addProperty(pnvm.getKey(), appPreferenceManager.getLoginResponseModel().getUserID());
                } else {
                    Logger.error("Key" + pnvm.getKey() + "Value" + pnvm.getValue());
                    jsonRequest.addProperty(pnvm.getKey(), pnvm.getValue());
                }
            }
            jsonRequest.addProperty("UserId", appPreferenceManager.getLoginResponseModel().getUserID());

            if (isNetworkAvailable(activity)) {
                CallFetchTransactionResponseOnStartTransactionApi(jsonRequest);
            } else {
                Toast.makeText(activity, getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CallFetchTransactionResponseOnStartTransactionApi(final JsonObject jsonRequest) {
        try {
            PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
            Call<PaymentStartTransactionAPIResponseModel> responseCall = apiInterface.CallFetchTransactionResponseOnStartTransactionApi(jsonRequest);
            global.showProgressDialog(activity, "Please wait..");
            responseCall.enqueue(new Callback<PaymentStartTransactionAPIResponseModel>() {
                @Override
                public void onResponse(Call<PaymentStartTransactionAPIResponseModel> call, Response<PaymentStartTransactionAPIResponseModel> response) {
                    global.hideProgressDialog(activity);
                    if (response.isSuccessful()) {
                        paymentStartTransactionAPIResponseModel = response.body();
                        if (paymentStartTransactionAPIResponseModel.getResponseCode().equals("RES000")) {
                            // mobileflag = 0;
                            if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName()) || orderDetailsModel.getAllOrderdetails().get(0).isPEPartner()) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                builder.setTitle("Verify Payment")
                                        .setMessage("Please Click 'Verify Payment' after payment is done by customer!")
                                        .setCancelable(false)
                                        .setPositiveButton("Verify Payment", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                PEPaymentRequestModel pePaymentRequestModel = new PEPaymentRequestModel();
                                                pePaymentRequestModel.setOrderNo(OrderNo);
                                                pePaymentRequestModel.setTransactionId(paymentStartTransactionAPIResponseModel.getTransactionId());
                                                callPEPaymentModeAPI(pePaymentRequestModel);
                                            }
                                        }).setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        CallFetchTransactionResponseOnReStartTransactionApi(jsonRequest);
                                        /*Intent intent = new Intent();
                                        intent.putExtra(BundleConstants.PAYMENT_STATUS, false);
                                        setResult(BundleConstants.PAYMENTS_FINISH, intent);
                                        finish();*/
                               /*     }
                                }).setNeutralButton("Retry", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                CallFetchTransactionResponseOnReStartTransactionApi(jsonRequest);*/
                                    }
                                }).show();
                            } else {
                                // mobileflag = 0;
                                if (NarrationId == 1 || NarrationId == 3) {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                    builder.setTitle("UPI Payment")
                                            .setMessage("Your Payment request has been initiated. Please access your UPI banking app to complete the process.")
                                            .setCancelable(false)
                                            .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent();
                                                    intent.putExtra(BundleConstants.PAYMENT_STATUS, false);
                                                    setResult(BundleConstants.PAYMENTS_FINISH, intent);
                                                    finish();
                                                }
                                            }).show();
                                } else if ((NarrationId == 2 && (ModeId == 1 || ModeId == 10))) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                    builder.setTitle("Verify Payment")
                                            .setMessage("Please Click 'Verify Payment' after payment is done by customer!")
                                            .setCancelable(false)
                                            .setPositiveButton("Verify Payment", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    fetchDoCaptureResponse(true);
                                                }
                                            }).setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent();
                                            intent.putExtra(BundleConstants.PAYMENT_STATUS, false);
                                            setResult(BundleConstants.PAYMENTS_FINISH, intent);
                                            finish();
                                        }
                                    }).show();
                                } else {
                                    initDoCaptureResponseData();
                                }
                            }
                        } else {
                            if (paymentStartTransactionAPIResponseModel.getResponseMessage() != null) {
                                if (paymentStartTransactionAPIResponseModel.getResponseMessage().equalsIgnoreCase("MOBILE_NUMBER_NOT_REGISTERED")) {
                                    Toast.makeText(activity, "Mobile Number is not Registered", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(activity, paymentStartTransactionAPIResponseModel.getResponseMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(activity, ConstantsMessages.SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<PaymentStartTransactionAPIResponseModel> call, Throwable t) {
                    global.hideProgressDialog(activity);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CallFetchTransactionResponseOnReStartTransactionApi(final JsonObject jsonRequest) {
        try {
            PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
            Call<PaymentStartTransactionAPIResponseModel> responseCall = apiInterface.CallFetchTransactionResponseOnReStartTransactionApi(jsonRequest);
            global.showProgressDialog(activity, "Please wait..");
            responseCall.enqueue(new Callback<PaymentStartTransactionAPIResponseModel>() {
                @Override
                public void onResponse(Call<PaymentStartTransactionAPIResponseModel> call, Response<PaymentStartTransactionAPIResponseModel> response) {
                    global.hideProgressDialog(activity);
                    if (response.isSuccessful()) {
                        paymentStartTransactionAPIResponseModel = response.body();
                        if (paymentStartTransactionAPIResponseModel.getResponseCode().equals("RES000")) {
                            Toast.makeText(activity,""+response.body().getResponseMessage(),Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setTitle("Verify Payment")
                                    .setMessage("Please Click 'Verify Payment' after payment is done by customer!")
                                    .setCancelable(false)
                                    .setPositiveButton("Verify Payment", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            PEPaymentRequestModel pePaymentRequestModel = new PEPaymentRequestModel();
                                            pePaymentRequestModel.setOrderNo(OrderNo);
                                            pePaymentRequestModel.setTransactionId(paymentStartTransactionAPIResponseModel.getTransactionId());
                                            callPEPaymentModeAPI(pePaymentRequestModel);
                                        }
                                    }).setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    CallFetchTransactionResponseOnReStartTransactionApi(jsonRequest);
                                    /*Intent intent = new Intent();
                                    intent.putExtra(BundleConstants.PAYMENT_STATUS, false);
                                    setResult(BundleConstants.PAYMENTS_FINISH, intent);
                                    finish();*/
                                }
                            })/*.setNeutralButton("Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    CallFetchTransactionResponseOnReStartTransactionApi(jsonRequest);
                                }
                            })*/.show();
                        } else {
                            Toast.makeText(activity, ""+response.body().getResponseMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<PaymentStartTransactionAPIResponseModel> call, Throwable t) {
                    global.hideProgressDialog(activity);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initDoCaptureResponseData() {
        if (paymentStartTransactionAPIResponseModel != null
                && paymentStartTransactionAPIResponseModel.getReqParameters() != null
                && paymentStartTransactionAPIResponseModel.getReqParameters().getNameValueCollection() != null
                && paymentStartTransactionAPIResponseModel.getReqParameters().getNameValueCollection().size() > 0) {

          /*  flPayments.setVisibility(View.VISIBLE);
            recy_paymentmode.setVisibility(View.GONE);*/
            flPayments.removeAllViews();


            View vpp = getLayoutInflater().inflate(R.layout.pay_ccna, null);
            LinearLayout llPaymentStartTransaction = (LinearLayout) vpp.findViewById(R.id.lineapay);
            String btnSubmitText = "Submit";
            for (int i = 0; i < paymentStartTransactionAPIResponseModel.getReqParameters().getNameValueCollection().size(); i++) {
                final int currentPosition = i;
                if (paymentStartTransactionAPIResponseModel.getReqParameters().getNameValueCollection().get(i).getRequired().equals("User")) {

                    int maxLength = 6;
                    InputFilter[] fArray = new InputFilter[1];
                    fArray[0] = new InputFilter.LengthFilter(maxLength);

                    edtPaymentUserInputs = new EditText(activity);
                    LinearLayout.LayoutParams edtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    edtParams.setMargins(10, 5, 5, 10);
                    edtPaymentUserInputs.setLayoutParams(edtParams);
                    edtPaymentUserInputs.setGravity(Gravity.CENTER);
                    edtPaymentUserInputs.setMinEms(10);
                    edtPaymentUserInputs.setFilters(fArray);
                    edtPaymentUserInputs.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                    edtPaymentUserInputs.setHint(paymentStartTransactionAPIResponseModel.getReqParameters().getNameValueCollection().get(i).getHint());
                    edtPaymentUserInputs.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            paymentStartTransactionAPIResponseModel.getReqParameters().getNameValueCollection().get(currentPosition).setValue(s.toString());
                        }
                    });
                    llPaymentStartTransaction.addView(edtPaymentUserInputs);
                }
            }
            for (PaymentNameValueModel paymentNameValueModel :
                    paymentStartTransactionAPIResponseModel.getReqParameters().getNameValueCollection()) {
                if (paymentNameValueModel.getKey().equals("ModeId") && paymentNameValueModel.getValue().equals("3")) {
                    buttonDecider = 1;
                    WebView wvQRDisplay = new WebView(activity);
                    wvQRDisplay.loadUrl("about:blank");
                    wvQRDisplay.canGoBack();
                    wvQRDisplay.setWebChromeClient(new WebChromeClient() {
                        public void onProgressChanged(WebView view, int progress) {
                            activity.setProgress(progress * 1000);
                        }
                    });
                    wvQRDisplay.setWebViewClient(new WebViewClient() {
                        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                            Toast.makeText(activity, "Error while loading the QR Code!" + description, Toast.LENGTH_SHORT).show();
                            initPaymentPassInputsData();
                        }
                    });
                    WebSettings settings = wvQRDisplay.getSettings();
                    settings.setUserAgentString(Global.getHeaderValue(activity));
                    settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
                    wvQRDisplay.setVerticalScrollBarEnabled(false);
                    wvQRDisplay.setHorizontalScrollBarEnabled(false);
                    LinearLayout.LayoutParams llwvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    llwvParams.gravity = Gravity.CENTER;
                    llwvParams.setMargins(10, 300, 10, 0);
                    wvQRDisplay.setLayoutParams(llwvParams);
                    wvQRDisplay.setInitialScale(getScale());
                    wvQRDisplay.loadDataWithBaseURL(null, paymentStartTransactionAPIResponseModel.getTokenData(), "text/html", "UTF-8", null);
                    llPaymentStartTransaction.addView(wvQRDisplay);
                    Toast.makeText(activity, "Please wait while the customer scans the QR Code", Toast.LENGTH_LONG).show();
                    break;
                }
            }
            //for paytm & cc avenue
            if (buttonDecider == 1) {
                ImageView imgPaymentStartTransactionSubmit = new ImageView(activity);
                Logger.debug("counter" + String.valueOf(buttonDecider));
                LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(100, 100);
                btnParams.setMargins(10, 5, 5, 10);
                btnParams.gravity = Gravity.CENTER_HORIZONTAL;
                imgPaymentStartTransactionSubmit.setLayoutParams(btnParams);
                imgPaymentStartTransactionSubmit.setImageResource(R.drawable.refresh_icon);
                imgPaymentStartTransactionSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fetchDoCaptureResponse(true);
                    }
                });
                llPaymentStartTransaction.addView(imgPaymentStartTransactionSubmit);
                flPayments.addView(llPaymentStartTransaction);
            }
            //for airtel
            else if (buttonDecider == 0) {
                Button btnPaymentStartTransactionSubmit = new Button(activity);
                Logger.debug("counter" + String.valueOf(buttonDecider));
                LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                btnParams.setMargins(10, 5, 5, 10);
                btnParams.gravity = Gravity.CENTER_HORIZONTAL;
                btnPaymentStartTransactionSubmit.setLayoutParams(btnParams);
                btnPaymentStartTransactionSubmit.setText(btnSubmitText);

                btnPaymentStartTransactionSubmit.setBackgroundResource(R.drawable.purple_btn_bg);
                btnPaymentStartTransactionSubmit.setTextColor(activity.getResources().getColor(R.color.loginbg));
                btnPaymentStartTransactionSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edtPaymentUserInputs != null) {
                            if (edtPaymentUserInputs.getText().length() == 6) {
                                fetchDoCaptureResponse(true);
                            } else {
                                Toast.makeText(activity, "Enter Valid OTP.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                llPaymentStartTransaction.addView(btnPaymentStartTransactionSubmit);
                flPayments.addView(llPaymentStartTransaction);
            }
        }
    }

    private void fetchDoCaptureResponse(boolean showProgressDialog) {
        JsonObject jsonRequest = new JsonObject();
        try {

            jsonRequest.addProperty("URLId", paymentStartTransactionAPIResponseModel.getReqParameters().getURLId());
            for (PaymentNameValueModel pnvm :
                    paymentStartTransactionAPIResponseModel.getReqParameters().getNameValueCollection()) {
                jsonRequest.addProperty(pnvm.getKey(), pnvm.getValue());
            }

            if (NarrationId == 2 && (ModeId == 1 || ModeId == 10)) {
                jsonRequest.addProperty("OrderNo", OrderNo);
            }

            if (isNetworkAvailable(activity)) {
                CallgetDoCaptureResponseRequestApi(jsonRequest, paymentStartTransactionAPIResponseModel.getReqParameters().getAPIUrl());
            } else {
                Toast.makeText(activity, getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchRecheckResponseData() {
        JsonObject jsonRequest = new JsonObject();
        try {
            jsonRequest.addProperty("URLId", paymentDoCaptureResponseAPIResponseModel.getReqParameters().getURLId());
            for (PaymentNameValueModel pnvm :
                    paymentDoCaptureResponseAPIResponseModel.getReqParameters().getNameValueCollection()) {
                jsonRequest.addProperty(pnvm.getKey(), pnvm.getValue());
            }

            if (NarrationId == 2 && (ModeId == 1 || ModeId == 10)) {
                jsonRequest.addProperty("OrderNo", OrderNo);
            }

            if (isNetworkAvailable(activity)) {
                CallgetDoCaptureResponseRequestApi(jsonRequest, paymentDoCaptureResponseAPIResponseModel.getReqParameters().getAPIUrl());

            } else {
                Toast.makeText(activity, getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void CallgetDoCaptureResponseRequestApi(JsonObject jsonRequest, String URL) {
        try {
            PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
            Call<PaymentDoCaptureResponseAPIResponseModel> responseCall = apiInterface.CallgetDoCaptureResponseRequestApi(URL, jsonRequest);
            global.showProgressDialog(activity, "Please wait..");
            responseCall.enqueue(new Callback<PaymentDoCaptureResponseAPIResponseModel>() {
                @Override
                public void onResponse(Call<PaymentDoCaptureResponseAPIResponseModel> call, Response<PaymentDoCaptureResponseAPIResponseModel> response) {
                    global.hideProgressDialog(activity);
                    if (response.isSuccessful()) {
                        PaymentDoCaptureResponseAPIResponseModel tempPDCRAPRM = response.body();
                        if (tempPDCRAPRM.getStatus() != null) {
                            paymentDoCaptureResponseAPIResponseModel = tempPDCRAPRM;
                            switch (paymentDoCaptureResponseAPIResponseModel.getStatus()) {
                                case "PAYMENT SUCCESS": {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                    builder.setTitle("Payment Status")
                                            .setMessage(paymentDoCaptureResponseAPIResponseModel.getResponseMessage())
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent();
                                                    intent.putExtra(BundleConstants.PAYMENT_STATUS, true);
                                                    setResult(BundleConstants.PAYMENTS_FINISH, intent);
                                                    finish();
                                                }
                                            }).show();
                                    break;
                                }
                                case "PAYMENT FAILED": {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                    builder.setTitle("Payment Status")
                                            .setMessage(paymentDoCaptureResponseAPIResponseModel.getResponseMessage())
                                            .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())) {
                                                        WOEPEBtech();//TODO For PE-BTech as per GG Sir's remark
                                                    } else {
                                                        Intent intent = new Intent();
                                                        intent.putExtra(BundleConstants.PAYMENT_STATUS, false);
                                                        setResult(BundleConstants.PAYMENTS_FINISH, intent);
                                                        finish();
                                                    }
                                                }
                                            }).setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            fetchRecheckResponseData();
                                        }
                                    }).show();
                                    break;
                                }
                                default: {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                    builder.setTitle("Verify Payment")
                                            .setMessage("Verify Payment Status failed! Please click Retry to check payment status again.")
                                            .setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    fetchRecheckResponseData();
                                                }
                                            })
                                            .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent();
                                                    intent.putExtra(BundleConstants.PAYMENT_STATUS, false);
                                                    setResult(BundleConstants.PAYMENTS_FINISH, intent);
                                                    finish();
                                                }
                                            }).show();
                                    break;
                                }
                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setTitle("Verify Payment")
                                    .setMessage("Verify Payment Status failed! Please click Retry to check payment status again.")
                                    .setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            fetchDoCaptureResponse(true);
                                        }
                                    })
                                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent();
                                            intent.putExtra(BundleConstants.PAYMENT_STATUS, false);
                                            setResult(BundleConstants.PAYMENTS_FINISH, intent);
                                            finish();
                                        }
                                    }).show();
                        }
                    } else {
                        Toast.makeText(activity, ConstantsMessages.SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<PaymentDoCaptureResponseAPIResponseModel> call, Throwable t) {
                    global.hideProgressDialog(activity);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void WOEPEBtech() {
        BundleConstants.addPaymentFlag = 1;
        if (BundleConstants.addPaymentFlag == 1) {
            PaymentMode = 1;
        }

        OrderBookingRequestModel orderBookingRequestModel = generateOrderBookingRequestModel("work_order_entry_prepaid");
        if (cd.isConnectingToInternet()) {
            WOEController wc = new WOEController(this);
            wc.CallWorkOrderEntryAPI(orderBookingRequestModel, orderDetailsModel, test, newTimeaddTwoHrs, newTimeaddTwoHalfHrs, null);
//            CallWorkOrderEntryAPI(orderBookingRequestModel);
        } else {
            Toast.makeText(activity, activity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
        }


        Intent intent = new Intent();
        intent.putExtra(BundleConstants.PAYMENT_STATUS, false);
        setResult(BundleConstants.PAYMENTS_FINISH, intent);
        finish();
    }


    private OrderBookingRequestModel generateOrderBookingRequestModel(String from) {

        OrderBookingRequestModel orderBookingRequestModel = new OrderBookingRequestModel();
        //SET Order Booking Details Model - START
        OrderBookingDetailsModel orderBookingDetailsModel = new OrderBookingDetailsModel();
        orderBookingDetailsModel.setPaymentMode(PaymentMode);
        orderBookingDetailsModel.setBtechId(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
        orderBookingDetailsModel.setVisitId(orderDetailsModel.getVisitId());
        orderBookingDetailsModel.setProcessLocation(location);
        ArrayList<OrderDetailsModel> ordtl = new ArrayList<>();
        ordtl = orderDetailsModel.getAllOrderdetails();
        String Slot = orderDetailsModel.getSlot();
        Logger.error("Slot" + Slot);
        dateCheck();
        if (from.equals("Button_proceed_payment")) {
            for (int i = 0; i < ordtl.size(); i++) {
                ordtl.get(i).setAddBen(false);
            }
        }
        Logger.error("Amount when order booking " + orderDetailsModel.getAllOrderdetails().get(0).getAmountPayable());
        for (int i = 0; i < ordtl.size(); i++) {
            ordtl.get(i).setAmountDue(totalAmountPayable);
            ordtl.get(i).setAmountPayable(totalAmountPayable);
            if (BundleConstants.isKIOSKOrder && !beneficaryWiseArylst.get(0).isCovidOrder()) {
                ordtl.get(i).setAddress("" + address);
            } else if (BundleConstants.isKIOSKOrder && beneficaryWiseArylst.get(0).isCovidOrder()) {
                if (!InputUtils.isNull(orderDetailsModel.getAllOrderdetails().get(0).getAddress())) {
                    ordtl.get(i).setAddress("" + orderDetailsModel.getAllOrderdetails().get(0).getAddress());
                }
            }
        }
        orderBookingDetailsModel.setOrddtl(ordtl);
        orderBookingRequestModel.setOrdbooking(orderBookingDetailsModel);
        //SET Order Booking Details Model - END
        //SET Order Details Models Array - START
        orderBookingRequestModel.setOrddtl(ordtl);
        //SET Order Details Models Array - END
        //SET BENEFICIARY Details Models Array - START
        ArrayList<BeneficiaryDetailsModel> benArr = orderDetailsModel.getAllOrderdetails().get(0).getBenMaster();
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

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public boolean isCheckPaymentResponseServiceIsInProgress() {
        return isServiceRunning(CheckPaymentResponseService.class);
    }

    public void stopCheckPaymentResponseService() {
        stopService(new Intent(activity, CheckPaymentResponseService.class));
    }

    public void getSubmitDataResponse(PayTMResponseModel payTMResponseModel, final String orderNo) {
        try {
            if (payTMResponseModel != null && InputUtils.CheckEqualIgnoreCase(payTMResponseModel.getStatusCode(), Constants.RES200)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Verify Payment")
                        .setMessage("Please Click 'Verify Payment' after payment is done by customer!")
                        .setCancelable(false)
                        .setPositiveButton("Verify Payment", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                callVerifyAPI(orderNo);
                            }
                        }).setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callVerifyAPI(String orderNo) {
        try {
            if (cd.isConnectingToInternet()) {
                PayTMVerifyRequestModel payTMVerifyRequestModel = new PayTMVerifyRequestModel();
                payTMVerifyRequestModel.setOrderNo(orderNo);
                PayTMVerifyController ptC = new PayTMVerifyController(this);
                ptC.payTMVerify(payTMVerifyRequestModel);
            } else {
                Global.showCustomStaticToast(this, SOMETHING_WENT_WRONG);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getSubmitVerifyResponse(PayTMVerifyResponseModel payTMVerifyResponseModel, final String orderNo) {
        try {
            if (payTMVerifyResponseModel != null && !InputUtils.isNull(payTMVerifyResponseModel.getStatus()) && InputUtils.CheckEqualIgnoreCase(payTMVerifyResponseModel.getStatus(), RES0000)) {
                Intent intent = new Intent();
                intent.putExtra(BundleConstants.PAYMENT_STATUS, true);
                setResult(BundleConstants.PAYMENTS_FINISH, intent);
                finish();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Verify Payment")
                        .setMessage("Verify Payment Status failed! Please click Retry to check payment status again.")
                        .setCancelable(false)
                        .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                callVerifyAPI(orderNo);
                            }
                        }).setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent();
                        intent.putExtra(BundleConstants.PAYMENT_STATUS, false);
                        setResult(BundleConstants.PAYMENTS_FINISH, intent);
                        finish();
                    }
                }).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class CheckPaymentStatusResponseReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() != null) {
                if (intent.getAction().equals(AppConstants.CHECK_PAYMENT_RESPONSE_ACTION_IN_PROGRESS)) {
                    //TODO show indeterminate Progress circle
                } else if (intent.getAction().equals(AppConstants.CHECK_PAYMENT_RESPONSE_ACTION_DONE)) {
                    if (intent.getExtras() != null) {
                        boolean isIssueFoundInSync = false;
                        if (intent.getExtras().containsKey(AppConstants.CHECK_PAYMENT_RESPONSE_ISSUE_FOUND)) {
                            isIssueFoundInSync = intent.getExtras().getBoolean(AppConstants.CHECK_PAYMENT_RESPONSE_ISSUE_FOUND);
                        }

                        if (isCheckPaymentResponseServiceIsInProgress()) {
                            stopCheckPaymentResponseService();
                        }

                        if (isIssueFoundInSync) {
                            Toast.makeText(activity, getResources().getString(R.string.sync_master_error), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(activity, getResources().getString(R.string.sync_done_master_table), Toast.LENGTH_SHORT).show();
                        }
                    }

                } else if (intent.getAction().equals(AppConstants.CHECK_PAYMENT_RESPONSE_ACTION_NO_DATA)) {
                    Toast.makeText(activity, getResources().getString(R.string.sync_no_data), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

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
            newTimeaddTwoHrs = df.format(cal1.getTime());
            newTimeaddTwoHalfHrs = df.format(cal2.getTime());
            Logger.error(">> ....." + newTimeaddTwoHrs);
            Logger.error(">> ....." + newTimeaddTwoHalfHrs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /******************************AUTO RECHECK RESPONSE UNTIL RESULT RECEIVE FUNCTIONALITY END************************************/
}

