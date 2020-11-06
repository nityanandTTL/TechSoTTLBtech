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
import android.os.PowerManager;
import android.text.Editable;
import android.text.InputFilter;
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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.models.api.response.FetchLedgerResponseModel;
import com.thyrocare.btechapp.models.api.response.PaymentDoCaptureResponseAPIResponseModel;
import com.thyrocare.btechapp.models.api.response.PaymentProcessAPIResponseModel;
import com.thyrocare.btechapp.models.api.response.PaymentStartTransactionAPIResponseModel;
import com.thyrocare.btechapp.models.data.DepositRegisterModel;
import com.thyrocare.btechapp.models.data.Earning_NewRegisterModel;
import com.thyrocare.btechapp.models.data.LedgerDetailsModeler;
import com.thyrocare.btechapp.models.data.NarrationMasterModel;
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

    private Activity activity;
    private AppPreferenceManager appPreferenceManager;
    private ResponseParser responseParser;
    private FrameLayout flPayments;
    private ArrayList<NarrationMasterModel> narrationsArr;
    private ArrayList<PaymentProcessAPIResponseModel> paymentModesArr;
    private PaymentProcessAPIResponseModel paymentPassInputsModel;
    private int NarrationId = 0;
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
    private int Flagcnt = 0;
    EditText edtPaymentUserInputs;
    private Global global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        activity = this;
        global = new Global(activity);
        appPreferenceManager = new AppPreferenceManager(activity);
        responseParser = new ResponseParser(activity);

        if (getIntent().getExtras() != null) {
            NarrationId = getIntent().getExtras().getInt(BundleConstants.PAYMENTS_NARRATION_ID);
            OrderNo = getIntent().getExtras().getString(BundleConstants.PAYMENTS_ORDER_NO);
            Amount = getIntent().getExtras().getString(BundleConstants.PAYMENTS_AMOUNT);
            SourceCode = getIntent().getExtras().getInt(BundleConstants.PAYMENTS_SOURCE_CODE);
            BillingName = getIntent().getExtras().getString(BundleConstants.PAYMENTS_BILLING_NAME);
            BillingAddr = getIntent().getExtras().getString(BundleConstants.PAYMENTS_BILLING_ADDRESS);
            BillingPin = getIntent().getExtras().getString(BundleConstants.PAYMENTS_BILLING_PIN);
            BillingMob = getIntent().getExtras().getString(BundleConstants.PAYMENTS_BILLING_MOBILE);
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

        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.DecodeString64(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<FetchLedgerResponseModel> responseCall = apiInterface.CallgetFetchDepositDetailsApi(appPreferenceManager.getLoginResponseModel().getUserID(),fromdate, todate);
        global.showProgressDialog(activity, "Please wait..");
        responseCall.enqueue(new Callback< FetchLedgerResponseModel>() {
            @Override
            public void onResponse(Call< FetchLedgerResponseModel> call, retrofit2.Response< FetchLedgerResponseModel> response) {
                global.hideProgressDialog(activity);
                if (response.isSuccessful() && response.body() != null) {
                    FetchLedgerResponseModel fetchLedgerDetailsResponseModel = response.body();
                    fetchLedgerResponseModel = fetchLedgerDetailsResponseModel;

                    fetchEarningRegister();
                }else {
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
        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.DecodeString64(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<Earning_NewRegisterModel> responseCall = apiInterface.CallFetchEarningDetailsApi(appPreferenceManager.getLoginResponseModel().getUserID(),fromdate, todate);
        global.showProgressDialog(activity, "Please wait..");
        responseCall.enqueue(new Callback< Earning_NewRegisterModel>() {
            @Override
            public void onResponse(Call< Earning_NewRegisterModel> call, retrofit2.Response< Earning_NewRegisterModel> response) {
                global.hideProgressDialog(activity);
                if (response.isSuccessful() && response.body() != null) {
                    earning_newRegisterModelsArr = new ArrayList<>();
                    earning_newRegisterModel = response.body();
                    if (earning_newRegisterModelsArr != null && earning_newRegisterModelsArr.size() > 0) {

                    }
                    fetchDepositLedger();
                }else {
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
        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.DecodeString64(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<ArrayList<DepositRegisterModel>> responseCall = apiInterface.CallgetFetchDepositPaymentDetailsApi(appPreferenceManager.getLoginResponseModel().getUserID(),fromdate, todate);
        global.showProgressDialog(activity, "Please wait..");
        responseCall.enqueue(new Callback< ArrayList<DepositRegisterModel>>() {
            @Override
            public void onResponse(Call< ArrayList<DepositRegisterModel>> call, retrofit2.Response< ArrayList<DepositRegisterModel>> response) {
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
        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.DecodeString64(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<ArrayList<PaymentProcessAPIResponseModel>> responseCall = apiInterface.CallFetchPaymentModesApi(NarrationId);
        global.showProgressDialog(activity,"Please wait..");
        responseCall.enqueue(new Callback<ArrayList<PaymentProcessAPIResponseModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PaymentProcessAPIResponseModel>> call, retrofit2.Response<ArrayList<PaymentProcessAPIResponseModel>> response) {
                global.hideProgressDialog(activity);
                if (response.isSuccessful() && response.body() != null) {
                    paymentModesArr =response.body();
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
            flPayments.removeAllViews();
            LinearLayout llPaymentModes = new LinearLayout(activity);
            LinearLayout llBankModeDetails = new LinearLayout(activity);
            TextView tv_leger = new TextView(activity);
            tv_leger.setText("Ledger");
            tv_leger.setTypeface(Typeface.DEFAULT_BOLD);
            tv_leger.setTextSize(20);
            tv_leger.setTextColor(getResources().getColor(android.R.color.black));
            tv_leger.setGravity(Gravity.CENTER);
            llPaymentModes.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            llPaymentModes.setOrientation(LinearLayout.VERTICAL);
            llPaymentModes.setGravity(Gravity.CENTER);
            Flagcnt = 0;

            for (PaymentProcessAPIResponseModel pparm :
                    paymentModesArr) {

                Flagcnt++;
                LinearLayout llPaymentModeDetails = new LinearLayout(activity);
                llPaymentModeDetails.setLayoutParams(new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                llPaymentModeDetails.setOrientation(LinearLayout.VERTICAL);
                llPaymentModeDetails.setGravity(Gravity.LEFT);
                for (PaymentNameValueModel pnvm :
                        pparm.getNameValueCollection()) {
                    if (pnvm.getKey().equals("ModeName")) {
                        Button btnPaymentMode = new Button(activity);
                        LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        txtParams.setMargins(10, 10, 10, 10);
                        btnPaymentMode.setLayoutParams(txtParams);
                        btnPaymentMode.setGravity(Gravity.CENTER);
                        btnPaymentMode.setPadding(5, 5, 5, 5);
                        btnPaymentMode.setMinEms(10);
                        //jai2
                        btnPaymentMode.setTextColor(getResources().getColor(android.R.color.white));
                        btnPaymentMode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_background_filled_oranged));
                        btnPaymentMode.setText(pnvm.getValue());
                        btnPaymentMode.setTag(pparm);
                        btnPaymentMode.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                fetchPaymentPassInputs((PaymentProcessAPIResponseModel) v.getTag());
                            }
                        });
                        llPaymentModeDetails.addView(btnPaymentMode);
                        llPaymentModeDetails.setTag(pparm);
                    }
                }
                llPaymentModes.addView(llPaymentModeDetails);
//jai
                llBankModeDetails.setLayoutParams(new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                llBankModeDetails.setOrientation(LinearLayout.VERTICAL);
                llBankModeDetails.setGravity(Gravity.RIGHT);


                Button btnBankMode = new Button(activity);
                LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                txtParams.setMargins(10, 10, 10, 10);
                btnBankMode.setLayoutParams(txtParams);
                btnBankMode.setGravity(Gravity.CENTER);
                btnBankMode.setPadding(5, 5, 5, 5);
                btnBankMode.setMinEms(10);
                //jai2
                btnBankMode.setTextColor(getResources().getColor(android.R.color.white));
                btnBankMode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_background_filled_oranged));
                btnBankMode.setText("BANK");
                btnBankMode.setTag(pparm);
                btnBankMode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HomeScreenActivity.isFromPayment = true;
                        startActivity(new Intent(activity, HomeScreenActivity.class));
                    }
                });

                if (Flagcnt == 1) {

                    btnBankMode.setVisibility(View.VISIBLE);
                } else {
                    btnBankMode.setVisibility(View.INVISIBLE);
                }

                llBankModeDetails.addView(btnBankMode);
            }


            if (fetchLedgerResponseModel != null && fetchLedgerResponseModel.getLedgerDetails().size() > 0) {

                tlCR = new TableLayout(activity);
                TableRow trCrH = (TableRow) LayoutInflater.from(activity).inflate(R.layout.item_title_ledger_cash_register, null);
                tlCR.addView(trCrH);
                for (LedgerDetailsModeler ledgerDetailsModel :
                        fetchLedgerResponseModel.getLedgerDetails()) {
                        /*ledgerDetailsModel.setBTechId(ledgerDetailsModel.getBTechId());
                        ledgerDetailsModel.setBTechName(ledgerDetailsModel.getBTechName());*/
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

                    tlCR.addView(trCr);

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


                tlCR.addView(trCr);

                Toast.makeText(activity, "payment history not available", Toast.LENGTH_SHORT).show();
            }
            //jai
            llBankModeDetails.addView(tv_leger);
            llBankModeDetails.addView(tlCR);
            flPayments.addView(llPaymentModes);
            flPayments.addView(llBankModeDetails);
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

    private void CallgetTransactionInputsRequestApi(JsonObject jsonRequest){
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.DecodeString64(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<PaymentProcessAPIResponseModel> responseCall = apiInterface.CallgetTransactionInputsRequestApi(jsonRequest);
        global.showProgressDialog(activity,"Please wait..");
        responseCall.enqueue(new Callback<PaymentProcessAPIResponseModel>() {
            @Override
            public void onResponse(Call<PaymentProcessAPIResponseModel> call, Response<PaymentProcessAPIResponseModel> response) {
                global.hideProgressDialog(activity);
                if (response.isSuccessful()){
                    paymentPassInputsModel = response.body();
                    initPaymentPassInputsData();
                }else{
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
            flPayments.removeAllViews();
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

                        //changes_5june2017
                      /*  if (paymentPassInputsModel.getNameValueCollection().get(i).getHint().equals("Mobile")) {
                            String strMobile = String.format("%-9s", paymentPassInputsModel.getNameValueCollection().get(i).getHint());
                            txtPaymentUserInputss.setText(strMobile);
                        } else {
                            txtPaymentUserInputss.setText(paymentPassInputsModel.getNameValueCollection().get(i).getHint());
                        }*/

                        txtPaymentUserInputss.setText(paymentPassInputsModel.getNameValueCollection().get(i).getHint());
                        //changes_5june2017
                        //jai
                        Logger.error("name " + paymentPassInputsModel.getNameValueCollection().get(i).getHint());

                        //jai
                        edtPaymentUserInputs.setHint("Please Enter " + paymentPassInputsModel.getNameValueCollection().get(i).getHint());
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

                                //for REMARKS
                                if (edtPaymentUserInputs.getHint().equals("Please Enter Remarks")) {
                                    if (remarks.contains("\'")) {
                                        edtPaymentUserInputs.setError("Apostroph symbol ( ' ) is not allowed.");
                                        edtPaymentUserInputs.setText("");
                                    } else
                                        paymentPassInputsModel.getNameValueCollection().get(currentPosition).setValue(s.toString());
                                    //paymentPassInputsModel.getNameValueCollection().get(currentPosition).setValue(s.toString());
                                }

                                //Mobile
                                if (edtPaymentUserInputs.getHint().equals("Please Enter Mobile")) {
                                    Logger.error("Alloy barka");
                                    if (remarks.contains(" ")) {
                                        Logger.error("Adaklay");

                                        edtPaymentUserInputs.setError("Mobile No Cannot Be Empty");
                                        edtPaymentUserInputs.setText("");

                                    } else
                                        Logger.error("JHalay barka");
                                    paymentPassInputsModel.getNameValueCollection().get(currentPosition).setValue(s.toString());
                                    //paymentPassInputsModel.getNameValueCollection().get(currentPosition).setValue(s.toString());
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
                        llPaymentPassInputs.addView(v1);
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
                    } /*Zollar line aahe hi */ else if (paymentPassInputsModel.getNameValueCollection().get(i).getKey().equals("BillingMob")) {
                        paymentPassInputsModel.getNameValueCollection().get(i).setValue(BillingMob);
                    } else if (paymentPassInputsModel.getNameValueCollection().get(i).getKey().equals("BillingAddr")) {
                        paymentPassInputsModel.getNameValueCollection().get(i).setValue(BillingAddr + "");
                    } else if (paymentPassInputsModel.getNameValueCollection().get(i).getKey().equals("BillingEmail")) {
                        paymentPassInputsModel.getNameValueCollection().get(i).setValue(BillingEmail + "");

                    }

                    if (paymentPassInputsModel.getNameValueCollection().get(i).getKey().equals("Amount")) {
                        textAmount.setText(paymentPassInputsModel.getNameValueCollection().get(i).getValue());
                        textAmount.setVisibility(View.VISIBLE);
                        editAmount.setHint(paymentPassInputsModel.getNameValueCollection().get(i).getHint());


                        editAmount.setVisibility(View.GONE);
                    } else {
                        View v2 = activity.getLayoutInflater().inflate(R.layout.payment_textview, null);
                        TextView txtPaymentSystemInputsLabel = (TextView) v2.findViewById(R.id.payment_text1);
                        TextView txtPaymentSystemInputs = (TextView) v2.findViewById(R.id.payment_text2);

                        //changes_5june2017

                        //changes_5june2017

                        txtPaymentSystemInputsLabel.setText((paymentPassInputsModel.getNameValueCollection().get(i).getHint() + ":"));
                        Logger.error("HInt" + paymentPassInputsModel.getNameValueCollection().get(i).getHint());
                        txtPaymentSystemInputs.setText(paymentPassInputsModel.getNameValueCollection().get(i).getValue());

                        Logger.error("HInt" + paymentPassInputsModel.getNameValueCollection().get(i).getValue());
                        llPaymentPassInputs.addView(v2);
                    }
                }
            }
            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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
                    fetchTransactionResponseOnStartTransaction();
                }
            });
            llPaymentPassInputs.addView(btnPaymentInputsSubmit);
            flPayments.addView(v);
        }
    }

    private void fetchTransactionResponseOnStartTransaction() {
        JsonObject jsonRequest = new JsonObject();
        try {
            // TODO only for testing
//           /* paymentPassInputsModel.getNameValueCollection().get(2).setValue("1");*/

            jsonRequest.addProperty("URLId", paymentPassInputsModel.getURLId());
            for (PaymentNameValueModel pnvm :
                    paymentPassInputsModel.getNameValueCollection()) {
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

    private void CallFetchTransactionResponseOnStartTransactionApi(JsonObject jsonRequest){
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.DecodeString64(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<PaymentStartTransactionAPIResponseModel> responseCall = apiInterface.CallFetchTransactionResponseOnStartTransactionApi(jsonRequest);
        global.showProgressDialog(activity,"Please wait..");
        responseCall.enqueue(new Callback<PaymentStartTransactionAPIResponseModel>() {
            @Override
            public void onResponse(Call<PaymentStartTransactionAPIResponseModel> call, Response<PaymentStartTransactionAPIResponseModel> response) {
                global.hideProgressDialog(activity);
                if (response.isSuccessful()){
                    paymentStartTransactionAPIResponseModel = response.body();
                    if (paymentStartTransactionAPIResponseModel.getResponseCode().equals("RES000")) {
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
                        if (paymentStartTransactionAPIResponseModel.getTokenData() != null) {
                            Toast.makeText(activity, paymentStartTransactionAPIResponseModel.getTokenData(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(activity, paymentStartTransactionAPIResponseModel.getResponseMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
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
                CallgetDoCaptureResponseRequestApi(jsonRequest, paymentDoCaptureResponseAPIResponseModel.getReqParameters().getAPIUrl());;
            } else {
                Toast.makeText(activity, getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void CallgetDoCaptureResponseRequestApi(JsonObject jsonRequest, String URL){
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.DecodeString64(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<PaymentDoCaptureResponseAPIResponseModel> responseCall = apiInterface.CallgetDoCaptureResponseRequestApi(URL,jsonRequest);
        global.showProgressDialog(activity,"Please wait..");
        responseCall.enqueue(new Callback<PaymentDoCaptureResponseAPIResponseModel>() {
            @Override
            public void onResponse(Call<PaymentDoCaptureResponseAPIResponseModel> call, Response<PaymentDoCaptureResponseAPIResponseModel> response) {
                global.hideProgressDialog(activity);
                if (response.isSuccessful()){
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
                }else{
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
