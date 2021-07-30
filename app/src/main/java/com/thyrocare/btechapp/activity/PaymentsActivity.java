package com.thyrocare.btechapp.activity;

import android.annotation.SuppressLint;
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
import android.os.PowerManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
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
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.thyrocare.btechapp.Controller.PayTMController;
import com.thyrocare.btechapp.Controller.PayTMVerifyController;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.ScanBarcodeWoeActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConnectionDetector;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.models.api.request.PayTMRequestModel;
import com.thyrocare.btechapp.models.api.request.PayTMVerifyRequestModel;
import com.thyrocare.btechapp.models.api.response.FetchLedgerResponseModel;
import com.thyrocare.btechapp.models.api.response.PayTMResponseModel;
import com.thyrocare.btechapp.models.api.response.PayTMVerifyResponseModel;
import com.thyrocare.btechapp.models.api.response.PaymentDoCaptureResponseAPIResponseModel;
import com.thyrocare.btechapp.models.api.response.PaymentProcessAPIResponseModel;
import com.thyrocare.btechapp.models.api.response.PaymentStartTransactionAPIResponseModel;
import com.thyrocare.btechapp.models.data.DepositRegisterModel;
import com.thyrocare.btechapp.models.data.Earning_NewRegisterModel;
import com.thyrocare.btechapp.models.data.LedgerDetailsModeler;
import com.thyrocare.btechapp.models.data.NarrationMasterModel;
import com.thyrocare.btechapp.models.data.OrderVisitDetailsModel;
import com.thyrocare.btechapp.models.data.PaymentNameValueModel;


import com.thyrocare.btechapp.network.ResponseParser;
import com.thyrocare.btechapp.service.CheckPaymentResponseService;
import com.thyrocare.btechapp.uiutils.AbstractActivity;
import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.AppConstants;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;


import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SOMETHING_WENT_WRONG;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;

/**
 * Created by Orion on 5/16/2017.
 */

public class PaymentsActivity extends AbstractActivity {
    private static final String TAG_ACTIVITY = "PaymentsActivity";

    LinearLayout llPayWithCashTitle, llPayWithCash, llPaymentModes, llPaymentGateway;
    private OrderVisitDetailsModel orderDetailsModel;
    private Activity activity;
    private AppPreferenceManager appPreferenceManager;
    private ResponseParser responseParser;
    private FrameLayout flPayments;
    private ArrayList<NarrationMasterModel> narrationsArr;
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
    private int flag = 0;
    private PaymentStartTransactionAPIResponseModel paymentStartTransactionAPIResponseModel;
    private PaymentDoCaptureResponseAPIResponseModel paymentDoCaptureResponseAPIResponseModel;
    private boolean doubleBackToExitPressedOnce = false;
    private PowerManager.WakeLock wakeLock;
    private long startRecheckMillis;
    //TODO tejas - 7738185400 for airtel money
    //TODO tejas - testthyrocare@axis for UPI
    int buttonDecider = 0;
    private int ModeId = 0;
    String TAG = PaymentsActivity.class.getSimpleName();
    private FetchLedgerResponseModel fetchLedgerResponseModel;
    private ArrayList<Earning_NewRegisterModel> earning_newRegisterModelsArr;
    Earning_NewRegisterModel earning_newRegisterModel;
    private ArrayList<DepositRegisterModel> depositRegisterModels;
    private String fromdate = "", todate = "";
    private TableLayout tlCR;
    EditText edtPaymentUserInputs;
    private Global global;
    ConnectionDetector cd;

//    private String mobile;
//    private int mobileflag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        activity = this;
        cd = new ConnectionDetector(activity);
        global = new Global(activity);
        appPreferenceManager = new AppPreferenceManager(activity);
        responseParser = new ResponseParser(activity);

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

            orderDetailsModel = getIntent().getExtras().getParcelable(BundleConstants.VISIT_ORDER_DETAILS_MODEL);
            Logger.error("BillingMob " + BillingMob);
            //  Toast.makeText(getApplicationContext(), BillingMob, Toast.LENGTH_SHORT).show();
            BillingEmail = getIntent().getExtras().getString(BundleConstants.PAYMENTS_BILLING_EMAIL);
        }
        initUI();
        //today date
        todate = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
        //previous 7  days
        fromdate = getCalculatedDate("yyyy-MM-dd", -7);
//        fetchNarrationMaster();

        fetchLedgerDetails();
    }

    public static String getCalculatedDate(String dateFormat, int days) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        cal.add(Calendar.DAY_OF_YEAR, days);
        String previous_date = s.format(new Date(cal.getTimeInMillis()));
        return previous_date;
    }

    private void fetchLedgerDetails() {
        if (isNetworkAvailable(activity)) {
            CallgetFetchDepositDetailsApi();
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchEarningRegister() {

        if (isNetworkAvailable(activity)) {
            CallFetchEarningDetailsApi();
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchDepositLedger() {
        if (isNetworkAvailable(activity)) {
            CallgetFetchDepositPaymentDetailsApi();
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }


    private void CallgetFetchDepositDetailsApi() {

        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<FetchLedgerResponseModel> responseCall = apiInterface.CallgetFetchDepositDetailsApi(appPreferenceManager.getLoginResponseModel().getUserID(), fromdate, todate);
        global.showProgressDialog(activity, "Please wait..");
        responseCall.enqueue(new Callback<FetchLedgerResponseModel>() {
            @Override
            public void onResponse(Call<FetchLedgerResponseModel> call, retrofit2.Response<FetchLedgerResponseModel> response) {
                global.hideProgressDialog(activity);
                if (response.isSuccessful() && response.body() != null) {
                    FetchLedgerResponseModel fetchLedgerDetailsResponseModel = response.body();
                    fetchLedgerResponseModel = fetchLedgerDetailsResponseModel;

                    fetchEarningRegister();
                } else {
                    Toast.makeText(activity, SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchLedgerResponseModel> call, Throwable t) {
                global.hideProgressDialog(activity);
                global.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });
    }

    private void CallFetchEarningDetailsApi() {
        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<Earning_NewRegisterModel> responseCall = apiInterface.CallFetchEarningDetailsApi(appPreferenceManager.getLoginResponseModel().getUserID(), fromdate, todate);
        global.showProgressDialog(activity, "Please wait..");
        responseCall.enqueue(new Callback<Earning_NewRegisterModel>() {
            @Override
            public void onResponse(Call<Earning_NewRegisterModel> call, retrofit2.Response<Earning_NewRegisterModel> response) {
                global.hideProgressDialog(activity);
                if (response.isSuccessful() && response.body() != null) {
                    earning_newRegisterModelsArr = new ArrayList<>();
                    earning_newRegisterModel = response.body();
                    if (earning_newRegisterModelsArr != null && earning_newRegisterModelsArr.size() > 0) {

                    }
                    fetchDepositLedger();
                } else {
                    Toast.makeText(activity, SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Earning_NewRegisterModel> call, Throwable t) {
                global.hideProgressDialog(activity);
                global.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });
    }


    private void CallgetFetchDepositPaymentDetailsApi() {
        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<ArrayList<DepositRegisterModel>> responseCall = apiInterface.CallgetFetchDepositPaymentDetailsApi(appPreferenceManager.getLoginResponseModel().getUserID(), fromdate, todate);
        global.showProgressDialog(activity, "Please wait..");
        responseCall.enqueue(new Callback<ArrayList<DepositRegisterModel>>() {
            @Override
            public void onResponse(Call<ArrayList<DepositRegisterModel>> call, retrofit2.Response<ArrayList<DepositRegisterModel>> response) {
                global.hideProgressDialog(activity);
                if (response.isSuccessful() && response.body() != null) {
                    depositRegisterModels = new ArrayList<>();
                    depositRegisterModels = response.body();
                    if (depositRegisterModels != null && depositRegisterModels.size() > 0) {
                    }
                }
                fetchPaymentModes();
            }

            @Override
            public void onFailure(Call<ArrayList<DepositRegisterModel>> call, Throwable t) {
                global.hideProgressDialog(activity);
                global.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage("You are trying to close the payments")
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("CONTINUE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.finish();
                            return;
                        }
                    })
                    .show();
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
    }


    private void fetchPaymentModes() {
        if (isNetworkAvailable(activity)) {
            CallFetchPaymentModesApi(NarrationId);
        } else {
            Toast.makeText(activity, getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
        }
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
            PayableAmount.setText("Payable Amount : " + Amount);
            PayableAmount.setTextSize(16);
            PayableAmount.setTextColor(getResources().getColor(R.color.black));
            PayableAmount.setLayoutParams(params);
            llPaymentModesFinal.addView(PayableAmount);

            //TODO Payment Gateway Title
            llPaymentGateway = new LinearLayout(activity);
            llPaymentGateway.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            llPaymentGateway.setBackgroundResource(R.drawable.grey_rect_grey_stroke_bg);

            TextView tv_payment_gateway_title = new TextView(activity);
            tv_payment_gateway_title.setLayoutParams(params);
            tv_payment_gateway_title.setText(R.string.PaymentGateway);
            tv_payment_gateway_title.setTextColor(getResources().getColor(R.color.colorFAB3));
            tv_payment_gateway_title.setTextSize(18);
            tv_payment_gateway_title.setLayoutParams(params);
            tv_payment_gateway_title.setLayoutParams(params);
            llPaymentGateway.addView(tv_payment_gateway_title);
            llPaymentModesFinal.addView(llPaymentGateway);

            //TODO Payment icons
            llPaymentModes = new LinearLayout(activity);
            llPaymentModes.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            llPaymentModes.setOrientation(LinearLayout.VERTICAL);


            //TODO Payment Gateways view
            AddingPaymentGateways();

            //TODO Adding Bank Icon
            LinearLayout llBankMode = new LinearLayout(activity);
            llBankMode.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            llBankMode.setOrientation(LinearLayout.HORIZONTAL);

            ImageView btnPaymentMode = new ImageView(activity);
            int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.12);
            LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(width, width);
            txtParams.setMargins(10, 10, 10, 10);
            btnPaymentMode.setLayoutParams(txtParams);
            btnPaymentMode.setScaleType(ImageView.ScaleType.FIT_CENTER);
            btnPaymentMode.setBackground(getResources().getDrawable(R.drawable.ic_bank));

            //TODO Names of Payment Gateway
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params1.setMargins(30, 20, 10, 20);
            TextView PaymentNames = new TextView(activity);
            PaymentNames.setText("Bank");
            PaymentNames.setTextSize(12);
            PaymentNames.setGravity(Gravity.CENTER_VERTICAL);
            PaymentNames.setTextColor(getResources().getColor(R.color.black));
            PaymentNames.setLayoutParams(params1);

/*            //TODO Bottom Line
            final LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params2.setMargins(20, 0, 20, 15);
            View view = new View(activity);
            view.setLayoutParams(params2);
            view.setBackgroundColor(getResources().getColor(R.color.black));*/


            //TODO Adding arrows at right
            LinearLayout llArrowIcons = new LinearLayout(activity);
            llArrowIcons.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            llArrowIcons.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
            int size = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.04);
            LinearLayout.LayoutParams arrowParams = new LinearLayout.LayoutParams(size, size);
            arrowParams.setMarginEnd(20);
            ImageView ArrowButtons = new ImageView(activity);
            ArrowButtons.setImageDrawable(getResources().getDrawable(R.drawable.right_arrow_orange));
            ArrowButtons.setLayoutParams(arrowParams);
            llArrowIcons.addView(ArrowButtons);

            llBankMode.addView(btnPaymentMode);
            llBankMode.addView(PaymentNames);
            llBankMode.addView(llArrowIcons);
//            llPaymentModes.addView(view);
            llPaymentModes.addView(llBankMode);

            llPaymentModesFinal.addView(llPaymentModes);


            llBankMode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomeScreenActivity.isFromPayment = true;
                    startActivity(new Intent(activity, HomeScreenActivity.class));
                }
            });


            //TODO Ledger History
            LinearLayout.LayoutParams txtParams234 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
            txtParams234.setMargins(0, 100, 0, 0);
            TextView tv_leger = new TextView(activity);
            tv_leger.setLayoutParams(txtParams234);
            tv_leger.setText("Ledger");
            tv_leger.setTypeface(Typeface.DEFAULT_BOLD);
            tv_leger.setTextSize(20);
            tv_leger.setTextColor(getResources().getColor(android.R.color.black));
            tv_leger.setGravity(Gravity.CENTER);

//            llPaymentModesFinal.addView(tv_leger);

            if (fetchLedgerResponseModel != null && fetchLedgerResponseModel.getLedgerDetails() != null && fetchLedgerResponseModel.getLedgerDetails().size() > 0) {

                tlCR = new TableLayout(activity);
                TableRow trCrH = (TableRow) LayoutInflater.from(activity).inflate(R.layout.item_title_ledger_cash_register, null);
                tlCR.addView(trCrH);
                for (LedgerDetailsModeler ledgerDetailsModel :
                        fetchLedgerResponseModel.getLedgerDetails()) {
                    TableRow trCr = (TableRow) LayoutInflater.from(activity).inflate(R.layout.item_titlesub_ledger_cash_register, null);
                    TextView txtDate = (TextView) trCr.findViewById(R.id.txt_date);
                    TextView txtopeningbal = (TextView) trCr.findViewById(R.id.txt_openingbalance);
                    TextView txtCredit = (TextView) trCr.findViewById(R.id.txt_credit);
                    TextView txtDebit = (TextView) trCr.findViewById(R.id.txt_debit);
                    TextView txtclosingbal = (TextView) trCr.findViewById(R.id.txt_closingBalance);

                    txtDate.setText(ledgerDetailsModel.getDate() + "");
                    txtopeningbal.setText(ledgerDetailsModel.getOpeningBal() + "");
                    txtCredit.setText(ledgerDetailsModel.getCredit() + "");
                    txtDebit.setText(ledgerDetailsModel.getDebit() + "");
                    txtclosingbal.setText(ledgerDetailsModel.getClosingBal() + "");

//                    tlCR.addView(trCr);

                }

            } else {
                tlCR = new TableLayout(activity);
                TableRow trCrH = (TableRow) LayoutInflater.from(activity).inflate(R.layout.item_title_ledger_cash_register, null);
                tlCR.addView(trCrH);
                TableRow trCr = (TableRow) LayoutInflater.from(activity).inflate(R.layout.item_titlesub_ledger_cash_register, null);

                TextView txtDate = (TextView) trCr.findViewById(R.id.txt_date);
                TextView txtopeningbal = (TextView) trCr.findViewById(R.id.txt_openingbalance);
                TextView txtCredit = (TextView) trCr.findViewById(R.id.txt_credit);
                TextView txtDebit = (TextView) trCr.findViewById(R.id.txt_debit);
                TextView txtclosingbal = (TextView) trCr.findViewById(R.id.txt_closingBalance);

                txtDate.setText("-");
                txtopeningbal.setText("-");
                txtCredit.setText("-");
                txtDebit.setText("-");
                txtclosingbal.setText("-");

//                tlCR.addView(trCr);

//                Toast.makeText(activity, "payment history not available", Toast.LENGTH_SHORT).show();
            }

//            llPaymentModesFinal.addView(tlCR);

            llPayWithCashTitle = new LinearLayout(activity);
            llPayWithCashTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            llPayWithCashTitle.setBackgroundResource(R.drawable.grey_rect_grey_stroke_bg);


            TextView tvPayWithCash = new TextView(activity);
            tvPayWithCash.setLayoutParams(params);
            tvPayWithCash.setText(R.string.PayWithCash);
            tvPayWithCash.setTextColor(getResources().getColor(R.color.colorFAB3));
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
            int ic_width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.12);
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
            TvCashDesc.setTextSize(12);
            TvCashDesc.setGravity(Gravity.CENTER_VERTICAL);
            TvCashDesc.setTextColor(getResources().getColor(R.color.colorFAB3));
            TvCashDesc.setLayoutParams(llCashDesc);

            //Sub description
            LinearLayout.LayoutParams lpSubDesc = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lpSubDesc.setMargins(30, 0, 10, 0);

            TextView TVDescCash = new TextView(activity);
            TVDescCash.setText("Online payment recommended to reduce contact between you and technician");
            TVDescCash.setTextSize(12);
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

    private void AddingPaymentGateways() {

        for (PaymentProcessAPIResponseModel pparm : paymentModesArr) {
            final LinearLayout llPaymentModesasd = new LinearLayout(activity);
            llPaymentModesasd.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            llPaymentModesasd.setOrientation(LinearLayout.HORIZONTAL);


            for (final PaymentNameValueModel pnvm : pparm.getNameValueCollection()) {
                if (pnvm.getKey().equals("ModeName")) {


                    ImageView btnPaymentMode = new ImageView(activity);
                    int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.12);
                    LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(width, width);
                    txtParams.setMargins(10, 10, 10, 10);
                    txtParams.gravity = Gravity.CENTER;
                    btnPaymentMode.setLayoutParams(txtParams);
                    btnPaymentMode.setScaleType(ImageView.ScaleType.FIT_CENTER);

                    if (pnvm.getValue().equalsIgnoreCase("AIRTEL")) {
                        btnPaymentMode.setBackground(getResources().getDrawable(R.drawable.ic_airtel));
                    } else if (pnvm.getValue().equalsIgnoreCase("MobiKwik")) {
                        btnPaymentMode.setBackground(getResources().getDrawable(R.drawable.ic_bobikwik));
                    } else if (pnvm.getValue().equalsIgnoreCase("PayU")) {
                        btnPaymentMode.setBackground(getResources().getDrawable(R.drawable.ic_payu));
                    } else if (pnvm.getValue().equalsIgnoreCase("Paytm")) {
                        btnPaymentMode.setBackground(getResources().getDrawable(R.drawable.ic_paytm));
                    } else {
                        btnPaymentMode.setBackground(getResources().getDrawable(R.drawable.ic_bank));
                    }

                    LinearLayout llNameAndEdt = new LinearLayout(activity);
                    llNameAndEdt.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    llNameAndEdt.setOrientation(LinearLayout.VERTICAL);

                    LinearLayout linearLayout = new LinearLayout(activity);
                    linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    linearLayout.setGravity(Gravity.CENTER);

                    //TODO Names of Payment Gateway

                    final LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params1.setMargins(30, 5, 10, 5);

                    TextView PaymentNames = new TextView(activity);
                    PaymentNames.setText(pnvm.getValue());
                    PaymentNames.setTextSize(16);
                    PaymentNames.setGravity(Gravity.CENTER_VERTICAL);
                    PaymentNames.setTextColor(getResources().getColor(R.color.black));
                    PaymentNames.setLayoutParams(params1);


                    //TODO Bottom Line
                    final LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
                    params2.setMargins(20, 15, 20, 15);
                    View view = new View(activity);
                    view.setLayoutParams(params2);
                    view.setBackgroundColor(getResources().getColor(R.color.gray));

                    //TODO Adding Tick image
                    int imgsize = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.05);
                    LinearLayout.LayoutParams tickParams = new LinearLayout.LayoutParams(imgsize, imgsize);
                    tickParams.setMarginEnd(20);
                    tickParams.gravity = Gravity.CENTER;
                    final ImageView imgTick = new ImageView(activity);
                    imgTick.setImageDrawable(getResources().getDrawable(R.drawable.tick_icon_green));
                    imgTick.setLayoutParams(tickParams);
                    imgTick.setVisibility(View.GONE);


                    //TODO Adding Edit Text
                    final EditText editText = new EditText(activity);
                    editText.setHint("Mobile Number");
                    editText.setGravity(Gravity.CENTER_VERTICAL);
                    editText.setTextSize(14);
                    editText.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                    editText.setLayoutParams(params1);
                    editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                    editText.setBackgroundResource(R.drawable.editextborder);
                    editText.setPadding(50, 10, 50, 10);
                    editText.setVisibility(View.GONE);


                    if (pnvm.getValue().equalsIgnoreCase("AIRTEL") || pnvm.getValue().equalsIgnoreCase("MobiKwik")) {
                        editText.setVisibility(View.VISIBLE);
                    }

                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (editText.getText().toString().length() == 10) {
                                if (getMobileValidate(s.toString())) {
                                    imgTick.setVisibility(View.VISIBLE);
                                }
                            } else {
                                imgTick.setVisibility(View.GONE);
                            }
                        }
                    });

                   /* if (pnvm.isClicked()) {
                        editText.setVisibility(View.VISIBLE);
                    } else {
                        editText.setVisibility(View.GONE);
                    }*/

                    LinearLayout LLPayTM = new LinearLayout(activity);
                    LLPayTM.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f));
                    LLPayTM.setOrientation(LinearLayout.HORIZONTAL);
                    LLPayTM.setWeightSum(2);
                    LLPayTM.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
                    Button btnQR = new Button(activity);
                    TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1);
                    btnQR.setLayoutParams(params);
                    btnQR.setText("Scan QR");
                    btnQR.setGravity(Gravity.CENTER_HORIZONTAL);
                    btnQR.setBackground(getResources().getDrawable(R.drawable.editextborder));
                    btnQR.setTextColor(getResources().getColor(R.color.colorOrange));
//                    btnQR.setBackgroundColor(getResources().getColor(R.color.colorOrange));
                    btnQR.setVisibility(View.GONE);
                    btnQR.setTextSize(10);
                    btnQR.setTag(pparm);
                    Button btnLink = new Button(activity);
                    TableRow.LayoutParams paramsl = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1);
                    btnLink.setLayoutParams(paramsl);
                    btnLink.setText("Send Link");
                    btnLink.setGravity(Gravity.CENTER_HORIZONTAL);
                    btnLink.setBackground(getResources().getDrawable(R.drawable.editextborder));
                    btnLink.setTextColor(getResources().getColor(R.color.colorOrange));
//                    btnLink.setBackgroundColor(getResources().getColor(R.color.colorOrange));
                    btnLink.setVisibility(View.GONE);
                    btnLink.setTextSize(10);
                    if (pnvm.getValue().equalsIgnoreCase("Paytm")) {
                        LLPayTM.setVisibility(View.VISIBLE);
                        btnQR.setVisibility(View.VISIBLE);
                        btnLink.setVisibility(View.VISIBLE);
                    } else {
                        LLPayTM.setVisibility(View.GONE);
                        btnQR.setVisibility(View.GONE);
                        btnLink.setVisibility(View.GONE);
                    }

                    btnQR.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fetchPaymentPassInputs((PaymentProcessAPIResponseModel) v.getTag());
                        }
                    });

                    btnLink.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            callPaytmLinkAPI();
                        }
                    });


                    //TODO Adding arrows at right

                    LinearLayout llArrowIcons = new LinearLayout(activity);
                    llArrowIcons.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    llArrowIcons.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                    int size = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.04);
                    LinearLayout.LayoutParams arrowParams = new LinearLayout.LayoutParams(size, size);
                    arrowParams.setMarginEnd(20);
                    ImageView ArrowButtons = new ImageView(activity);
                    ArrowButtons.setImageDrawable(getResources().getDrawable(R.drawable.right_arrow_orange));
                    ArrowButtons.setLayoutParams(arrowParams);
                    llArrowIcons.addView(ArrowButtons);

                    //TODO Handling onclicks
           /*         llNameAndEdt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (pnvm.getValue().equalsIgnoreCase("AIRTEL")) {
                                pnvm.setClicked(true);

                            } else if (pnvm.getValue().equalsIgnoreCase("MobiKwik")) {
                                pnvm.setClicked(true);
                            }
                            flag = 1;
                            llPaymentModes.removeAllViews();
                            AddingPaymentGateways();

                        }
                    });*/
                    if (pnvm.getValue().equalsIgnoreCase("PayTm")) {
                        ArrowButtons.setVisibility(View.GONE);
                    }
                    ArrowButtons.setTag(pparm);
                    ArrowButtons.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (editText.getVisibility() == View.VISIBLE) {
                                if (getMobileValidate(editText.getText().toString())) {
                                    BillingMob = editText.getText().toString();
                                    fetchPaymentPassInputs((PaymentProcessAPIResponseModel) v.getTag());
                                }
                            } else {
                                fetchPaymentPassInputs((PaymentProcessAPIResponseModel) v.getTag());
                            }
                        }
                    });

                    //TODO Adding All views
                    linearLayout.addView(editText);
                    LLPayTM.addView(btnQR);
                    LLPayTM.addView(btnLink);
                    linearLayout.addView(imgTick);
                    linearLayout.addView(LLPayTM);
                    llPaymentModesasd.addView(btnPaymentMode);
                    llNameAndEdt.addView(PaymentNames);
                    llNameAndEdt.addView(linearLayout);
                    llPaymentModesasd.addView(llNameAndEdt);
                    llPaymentModesasd.addView(llArrowIcons);
                    llPaymentModes.addView(llPaymentModesasd);
                    llPaymentModes.addView(view);
                }
            }
        }
    }

    private void callPaytmLinkAPI() {
        if (cd.isConnectingToInternet()) {
            PayTMRequestModel payTMRequestModel = new PayTMRequestModel();
            payTMRequestModel.setEntryBy(appPreferenceManager.getLoginResponseModel().getUserID());
            payTMRequestModel.setOrderno(OrderNo);

            PayTMController payTMController = new PayTMController(this);
            payTMController.payTM(payTMRequestModel);
        } else {
            Global.showCustomStaticToast(activity, SOMETHING_WENT_WRONG);
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

//            flPayments.removeAllViews();
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

    /*                if (paymentPassInputsModel.getNameValueCollection().get(i).getKey().equals("Amount")) {
                        textAmount.setText(paymentPassInputsModel.getNameValueCollection().get(i).getValue());
                        textAmount.setVisibility(View.VISIBLE);
                        if (paymentPassInputsModel.getNameValueCollection().get(i).getHint().equalsIgnoreCase("Mobile")) {
                            edtPaymentUserInputs.setHint("Please Enter " + paymentPassInputsModel.getNameValueCollection().get(i).getHint() + "*");
                        } else {
                            editAmount.setHint(paymentPassInputsModel.getNameValueCollection().get(i).getHint());
                        }

                        editAmount.setVisibility(View.GONE);
                    } else {
                        View v2 = activity.getLayoutInflater().inflate(R.layout.payment_textview, null);
                        TextView txtPaymentSystemInputsLabel = (TextView) v2.findViewById(R.id.payment_text1);
                        TextView txtPaymentSystemInputs = (TextView) v2.findViewById(R.id.payment_text2);


                        txtPaymentSystemInputsLabel.setText((paymentPassInputsModel.getNameValueCollection().get(i).getHint() + ":"));
                        Logger.error("HInt" + paymentPassInputsModel.getNameValueCollection().get(i).getHint());
                        txtPaymentSystemInputs.setText(paymentPassInputsModel.getNameValueCollection().get(i).getValue());

                        Logger.error("HInt" + paymentPassInputsModel.getNameValueCollection().get(i).getValue());
//                        llPaymentPassInputs.addView(v2);
                    }*/
                }
            }


            fetchTransactionResponseOnStartTransaction();

/*            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            //btnParams.setMargins(10, 5, 5, 10);
            //changes_7june2017
            btnParams.setMargins(200, 20, 200, 5);
            //change_7june2017...
            btnPaymentInputsSubmit.setLayoutParams(btnParams);
            btnPaymentInputsSubmit.setGravity(Gravity.CENTER);
            btnPaymentInputsSubmit.setMinEms(10);
            btnPaymentInputsSubmit.setBackgroundDrawable(getResources().getDrawable(R.drawable.purple_btn_bg));
            btnPaymentInputsSubmit.setText(getResources().getString(R.string.submit));
            btnPaymentInputsSubmit.setTextColor(getResources().getColor(android.R.color.white));
            btnPaymentInputsSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    fetchTransactionResponseOnStartTransaction();
                   */
            /* if (mobileflag == 1) {
                        if (getMobileValidate(mobile)) {
                            fetchTransactionResponseOnStartTransaction();
                        }
                    } else {
                        fetchTransactionResponseOnStartTransaction();
                    }*/
            /*

                }
            });
//            llPaymentPassInputs.addView(btnPaymentInputsSubmit);
//            flPayments.addView(v);*/
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
//           /* paymentPassInputsModel.getNameValueCollection().get(2).setValue("1");*/

            jsonRequest.addProperty("URLId", paymentPassInputsModel.getURLId());
            for (PaymentNameValueModel pnvm : paymentPassInputsModel.getNameValueCollection()) {
                if (pnvm.getKey().equals("SourceCode")) {
                    jsonRequest.addProperty(pnvm.getKey(), appPreferenceManager.getLoginResponseModel().getUserID());
                } else {
                    Logger.error("Key" + pnvm.getKey() + "Value" + pnvm.getValue());
                    jsonRequest.addProperty(pnvm.getKey(), pnvm.getValue());

                   /* if (pnvm.getRequired().equals("User") && InputUtils.isNull(pnvm.getValue())) {
                        Toast.makeText(activity, "All input fields are necessary"+pnvm.getKey().toString(), Toast.LENGTH_SHORT).show();
                        return;
                    }*//*----Changes done Due to blocking */
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

    private void CallFetchTransactionResponseOnStartTransactionApi(JsonObject jsonRequest) {
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
                        } else if (NarrationId == 2 && (ModeId == 1 || ModeId == 10)) {
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
    }

    private void initDoCaptureResponseData() {
        if (paymentStartTransactionAPIResponseModel != null
                && paymentStartTransactionAPIResponseModel.getReqParameters() != null
                && paymentStartTransactionAPIResponseModel.getReqParameters().getNameValueCollection() != null
                && paymentStartTransactionAPIResponseModel.getReqParameters().getNameValueCollection().size() > 0) {
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
                startRecheckMillis = Calendar.getInstance().getTimeInMillis();
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
                ;
            } else {
                Toast.makeText(activity, getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void CallgetDoCaptureResponseRequestApi(JsonObject jsonRequest, String URL) {
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
                                                Intent intent = new Intent();
                                                intent.putExtra(BundleConstants.PAYMENT_STATUS, false);
                                                setResult(BundleConstants.PAYMENTS_FINISH, intent);
                                                finish();
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
    }


    /******************************AUTO RECHECK RESPONSE UNTIL RESULT RECEIVE FUNCTIONALITY START************************************/

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

    private void callCheckPaymentResponseService(String jsonRequest) {
        Intent intent = new Intent(activity, CheckPaymentResponseService.class);
        intent.putExtra(BundleConstants.CHECK_PAYMENT_RESPONSE_JSON_REQUEST, jsonRequest);
        startService(intent);
        acquirewakeLock();
    }

    public void stopCheckPaymentResponseService() {
        stopService(new Intent(activity, CheckPaymentResponseService.class));
        releaseWakeLock();
    }

    public void acquirewakeLock() {

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Login Wakelock");
        wakeLock.acquire();
    }

    public void releaseWakeLock() {
        wakeLock.release();
    }

    public void getSubmitDataResponse(PayTMResponseModel payTMResponseModel, final String orderNo) {
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
                    Intent intent = new Intent();
                    intent.putExtra(BundleConstants.PAYMENT_STATUS, false);
                    setResult(BundleConstants.PAYMENTS_FINISH, intent);
                    finish();
                }
            }).show();
        }
    }

    private void callVerifyAPI(String orderNo) {
        if(cd.isConnectingToInternet()){
            PayTMVerifyRequestModel payTMVerifyRequestModel = new PayTMVerifyRequestModel();
            payTMVerifyRequestModel.setOrderNo(orderNo);

            PayTMVerifyController ptC = new PayTMVerifyController(this);
            ptC.payTMVerify(payTMVerifyRequestModel);

        }else {
            Global.showCustomStaticToast(this, SOMETHING_WENT_WRONG);
        }
    }

    public void getSubmitVerifyResponse() {


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

    /******************************AUTO RECHECK RESPONSE UNTIL RESULT RECEIVE FUNCTIONALITY END************************************/
}
