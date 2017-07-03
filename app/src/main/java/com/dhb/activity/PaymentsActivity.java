package com.dhb.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.models.api.response.PaymentDoCaptureResponseAPIResponseModel;
import com.dhb.models.api.response.PaymentProcessAPIResponseModel;
import com.dhb.models.api.response.PaymentStartTransactionAPIResponseModel;
import com.dhb.models.data.NarrationMasterModel;
import com.dhb.models.data.PaymentNameValueModel;
import com.dhb.network.ApiCallAsyncTask;
import com.dhb.network.ApiCallAsyncTaskDelegate;
import com.dhb.network.AsyncTaskForRequest;
import com.dhb.network.ResponseParser;
import com.dhb.service.CheckPaymentResponseService;
import com.dhb.uiutils.AbstractActivity;
import com.dhb.utils.api.Logger;
import com.dhb.utils.app.AppConstants;
import com.dhb.utils.app.AppPreferenceManager;
import com.dhb.utils.app.BundleConstants;
import com.dhb.utils.app.InputUtils;
import com.google.zxing.qrcode.decoder.Mode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Vendor3 on 5/16/2017.
 */

public class PaymentsActivity extends AbstractActivity {
    private Activity activity;
    private AppPreferenceManager appPreferenceManager;
    private AsyncTaskForRequest asyncTaskForRequest;
    private ResponseParser responseParser;
    private ApiCallAsyncTask fetchNarrationMasterAsyncTask;
    private ApiCallAsyncTask fetchPaymentModesAsyncTask;
    private FrameLayout flPayments;
    private ArrayList<NarrationMasterModel> narrationsArr;
    private ArrayList<PaymentProcessAPIResponseModel> paymentModesArr;
    private ApiCallAsyncTask fetchPaymentPassInputsAsyncTask;
    private PaymentProcessAPIResponseModel paymentPassInputsModel;
    private ApiCallAsyncTask startTransactionAsyncTask;
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
    private ApiCallAsyncTask doCaptureResponseAsyncTask;
    private ApiCallAsyncTask recheckResponseAsyncTask;
    private boolean doubleBackToExitPressedOnce = false;
    private PowerManager.WakeLock wakeLock;
    private long startRecheckMillis;
    //TODO tejas - 7738185400 for airtel money
    //TODO tejas - testthyrocare@axis for UPI
    int buttonDecider = 0;
    private int ModeId = 0;
    String TAG = PaymentsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        activity = this;
        appPreferenceManager = new AppPreferenceManager(activity);
        asyncTaskForRequest = new AsyncTaskForRequest(activity);
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
            BillingEmail = getIntent().getExtras().getString(BundleConstants.PAYMENTS_BILLING_EMAIL);
        }
        initUI();

//        fetchNarrationMaster();
        fetchPaymentModes();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {

            final AlertDialog.Builder builder=new AlertDialog.Builder(activity);
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
        }
        else{
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit Payments", Toast.LENGTH_SHORT).show();
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

    private void fetchNarrationMaster() {
        fetchNarrationMasterAsyncTask = asyncTaskForRequest.getNarrationMasterRequestAsyncTask();
        fetchNarrationMasterAsyncTask.setApiCallAsyncTaskDelegate(new FetchNarrationMasterAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            fetchNarrationMasterAsyncTask.execute(fetchNarrationMasterAsyncTask);
        } else {
            Toast.makeText(activity, getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
        }
    }

    private class FetchNarrationMasterAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                narrationsArr = responseParser.getNarrationMasterResponse(json, statusCode);
                initNarrationsData();
            } else {
                Toast.makeText(activity, json + "", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, "No Response", Toast.LENGTH_SHORT).show();
        }
    }

    private void initNarrationsData() {
        if (narrationsArr != null && narrationsArr.size() > 0) {
            flPayments.removeAllViews();
            LinearLayout llNarrations = new LinearLayout(activity);
            llNarrations.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            llNarrations.setOrientation(LinearLayout.VERTICAL);
            llNarrations.setGravity(Gravity.CENTER);
            for (NarrationMasterModel nmm :
                    narrationsArr) {
                Button btnNarration = new Button(activity);
                btnNarration.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                btnNarration.setText(nmm.getNarration());
                btnNarration.setTextColor(getResources().getColor(android.R.color.white));
                btnNarration.setPadding(5, 5, 5, 5);
                btnNarration.setBackgroundDrawable(getResources().getDrawable(R.drawable.purple_btn_bg));
                btnNarration.setOnClickListener(new NarrationsButtonOnClickListener(nmm));
                btnNarration.setGravity(Gravity.CENTER);
                llNarrations.addView(btnNarration);
            }
            flPayments.addView(llNarrations);
        }
    }

    private class NarrationsButtonOnClickListener implements View.OnClickListener {
        private NarrationMasterModel nmm;

        public NarrationsButtonOnClickListener(NarrationMasterModel nmm) {
            this.nmm = nmm;
        }

        @Override
        public void onClick(View v) {
            fetchPaymentModes();
        }
    }

    private void fetchPaymentModes() {
        fetchPaymentModesAsyncTask = asyncTaskForRequest.getPaymentModesFromNarrationIdRequestAsyncTask(NarrationId);
        fetchPaymentModesAsyncTask.setApiCallAsyncTaskDelegate(new FetchPaymentModesAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            fetchPaymentModesAsyncTask.execute(fetchPaymentModesAsyncTask);
        } else {
            Toast.makeText(activity, getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
        }
    }

    private class FetchPaymentModesAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                paymentModesArr = responseParser.getPaymentModesResponse(json, statusCode);
                initPaymentModesData();
            } else {
                Toast.makeText(activity, json + "", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, "No Response", Toast.LENGTH_SHORT).show();
        }
    }

    private void initPaymentModesData() {
        if (paymentModesArr != null && paymentModesArr.size() > 0) {
            flPayments.removeAllViews();
            LinearLayout llPaymentModes = new LinearLayout(activity);
            llPaymentModes.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            llPaymentModes.setOrientation(LinearLayout.VERTICAL);
            llPaymentModes.setGravity(Gravity.CENTER);
            for (PaymentProcessAPIResponseModel pparm :
                    paymentModesArr) {
                LinearLayout llPaymentModeDetails = new LinearLayout(activity);
                llPaymentModeDetails.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                llPaymentModeDetails.setOrientation(LinearLayout.VERTICAL);
                llPaymentModeDetails.setGravity(Gravity.CENTER);
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
                        btnPaymentMode.setTextColor(getResources().getColor(android.R.color.white));
                        btnPaymentMode.setBackgroundDrawable(getResources().getDrawable(R.drawable.purple_btn_bg));
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
            }
            flPayments.addView(llPaymentModes);
        }
    }

    private void fetchPaymentPassInputs(PaymentProcessAPIResponseModel pparm) {
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("URLId", pparm.getURLId());
            for (PaymentNameValueModel pnvm :
                    pparm.getNameValueCollection()) {
                if (pnvm.getKey().equals("ModeId")) {
                    ModeId = Integer.parseInt(pnvm.getValue());
                }
                jsonRequest.put(pnvm.getKey(), pnvm.getValue());
            }
            fetchPaymentPassInputsAsyncTask = asyncTaskForRequest.getTransactionInputsRequestAsyncTask(jsonRequest);
            fetchPaymentPassInputsAsyncTask.setApiCallAsyncTaskDelegate(new FetchPaymentPassInputsAsyncTaskDelegateResult());
            if (isNetworkAvailable(activity)) {
                fetchPaymentPassInputsAsyncTask.execute(fetchPaymentPassInputsAsyncTask);
            } else {
                Toast.makeText(activity, getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class FetchPaymentPassInputsAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                paymentPassInputsModel = responseParser.getPaymentPassInputsResponse(json, statusCode);
                initPaymentPassInputsData();
            } else {
                Toast.makeText(activity, json + "", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, "No Response", Toast.LENGTH_SHORT).show();
        }
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
                         final  EditText edtPaymentUserInputs = (EditText) v1.findViewById(R.id.edit_payment);
                        TextView txtPaymentUserInputss = (TextView) v1.findViewById(R.id.payment_text);


                        //changes_5june2017
                        /*if (paymentPassInputsModel.getNameValueCollection().get(i).getHint().equals("Mobile")) {
                            String strMobile = String.format("%-9s", paymentPassInputsModel.getNameValueCollection().get(i).getHint());
                            txtPaymentUserInputss.setText(strMobile);
                        } else {
                            txtPaymentUserInputss.setText(paymentPassInputsModel.getNameValueCollection().get(i).getHint());
                        }*/

                        txtPaymentUserInputss.setText(paymentPassInputsModel.getNameValueCollection().get(i).getHint());
                        //changes_5june2017

                        edtPaymentUserInputs.setHint(paymentPassInputsModel.getNameValueCollection().get(i).getHint());
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
                                if (edtPaymentUserInputs.getHint().equals("Remarks")) {
                                    if (remarks.contains("\'")) {
                                        edtPaymentUserInputs.setError("Apostroph symbol ( ' ) is not allowed.");
                                        edtPaymentUserInputs.setText("");
                                    } else
                                        paymentPassInputsModel.getNameValueCollection().get(currentPosition).setValue(s.toString());
                                    //paymentPassInputsModel.getNameValueCollection().get(currentPosition).setValue(s.toString());
                                }


                                //for VPA
                                if (edtPaymentUserInputs.getHint().equals("VPA")) {
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
                                            if(NarrationId == 3 && amountPayable<2000){//amounPayable<2000
                                                editAmount.requestFocus();
                                                editAmount.setError("Amount Should be greater than Rs 2000/-");
                                            }
                                            else {
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
                        paymentPassInputsModel.getNameValueCollection().get(i).setValue(BillingMob + "");
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
                        /*if(paymentPassInputsModel.getNameValueCollection().get(i).getHint().equals("Name"))
                        {
                            String strName = String.format("%-9s", paymentPassInputsModel.getNameValueCollection().get(i).getHint() + ":");
                            txtPaymentSystemInputsLabel.setText(strName);
                        } else if (paymentPassInputsModel.getNameValueCollection().get(i).getHint().equals("Mobile")) {
                            String strMobile = String.format("%-9s", paymentPassInputsModel.getNameValueCollection().get(i).getHint() + ":");
                            txtPaymentSystemInputsLabel.setText(strMobile);
                        } else if (paymentPassInputsModel.getNameValueCollection().get(i).getHint().equals("Email")) {
                            String strEmail = String.format("%-9s", paymentPassInputsModel.getNameValueCollection().get(i).getHint() + ":");
                            txtPaymentSystemInputsLabel.setText(strEmail);
                        } else {
                            txtPaymentSystemInputsLabel.setText((paymentPassInputsModel.getNameValueCollection().get(i).getHint() + ":"));
                        }*/
                        //changes_5june2017

                        txtPaymentSystemInputsLabel.setText((paymentPassInputsModel.getNameValueCollection().get(i).getHint() + ":"));
                        txtPaymentSystemInputs.setText(paymentPassInputsModel.getNameValueCollection().get(i).getValue());
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
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("URLId", paymentPassInputsModel.getURLId());
            for (PaymentNameValueModel pnvm :
                    paymentPassInputsModel.getNameValueCollection()) {
                if (pnvm.getKey().equals("SourceCode")) {
                    jsonRequest.put(pnvm.getKey(), appPreferenceManager.getLoginResponseModel().getUserID());
                } else {
                    jsonRequest.put(pnvm.getKey(), pnvm.getValue());
                    if (pnvm.getRequired().equals("User") && InputUtils.isNull(pnvm.getValue())) {
                        Toast.makeText(activity, "All input fields are necessary", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
            startTransactionAsyncTask = asyncTaskForRequest.getStartTransactionRequestAsyncTask(jsonRequest);
            startTransactionAsyncTask.setApiCallAsyncTaskDelegate(new FetchTransactionResponseOnStartTransactionAsyncTaskDelegateResult());
            if (isNetworkAvailable(activity)) {
                startTransactionAsyncTask.execute(startTransactionAsyncTask);
            } else {
                Toast.makeText(activity, getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class FetchTransactionResponseOnStartTransactionAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                paymentStartTransactionAPIResponseModel = responseParser.getPaymentStartTransactionResponse(json, statusCode);
                if (paymentStartTransactionAPIResponseModel.getResponseCode().equals("RES000")) {
                    if(NarrationId == 1 || NarrationId == 3 ){
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
                    }
                    else if (NarrationId == 2 && ModeId == 1) {
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
                    Toast.makeText(activity, paymentStartTransactionAPIResponseModel.getTokenData(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(activity, json + "", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, "No Response", Toast.LENGTH_SHORT).show();
        }
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
                    EditText edtPaymentUserInputs = new EditText(activity);
                    LinearLayout.LayoutParams edtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    edtParams.setMargins(10, 5, 5, 10);
                    edtPaymentUserInputs.setLayoutParams(edtParams);
                    edtPaymentUserInputs.setGravity(Gravity.CENTER);
                    edtPaymentUserInputs.setMinEms(10);
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
                        fetchDoCaptureResponse(true);
                    }
                });
                llPaymentStartTransaction.addView(btnPaymentStartTransactionSubmit);
                flPayments.addView(llPaymentStartTransaction);
            }
        }
    }

    private void fetchDoCaptureResponse(boolean showProgressDialog) {
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("URLId", paymentStartTransactionAPIResponseModel.getReqParameters().getURLId());
            for (PaymentNameValueModel pnvm :
                    paymentStartTransactionAPIResponseModel.getReqParameters().getNameValueCollection()) {
                jsonRequest.put(pnvm.getKey(), pnvm.getValue());
            }
            doCaptureResponseAsyncTask = asyncTaskForRequest.getDoCaptureResponseRequestAsyncTask(jsonRequest, paymentStartTransactionAPIResponseModel.getReqParameters().getAPIUrl());
            doCaptureResponseAsyncTask.setApiCallAsyncTaskDelegate(new DoCaptureResponseAsyncTaskDelegateResult(showProgressDialog));
            doCaptureResponseAsyncTask.setProgressBarVisible(showProgressDialog);
            if (isNetworkAvailable(activity)) {
                startRecheckMillis = Calendar.getInstance().getTimeInMillis();
                doCaptureResponseAsyncTask.execute(doCaptureResponseAsyncTask);
            } else {
                Toast.makeText(activity, getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class DoCaptureResponseAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        private boolean showProgressDialog;

        public DoCaptureResponseAsyncTaskDelegateResult(boolean showProgressDialog) {
            this.showProgressDialog = showProgressDialog;
        }

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                PaymentDoCaptureResponseAPIResponseModel tempPDCRAPRM = responseParser.getPaymentDoCaptureAPIResponse(json, statusCode);
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
                                    fetchRecheckResponseData(showProgressDialog);
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
                                            fetchRecheckResponseData(showProgressDialog);
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
                Toast.makeText(activity, json + "", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, "No Response", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchRecheckResponseData(boolean showProgressDialog) {
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("URLId", paymentDoCaptureResponseAPIResponseModel.getReqParameters().getURLId());
            for (PaymentNameValueModel pnvm :
                    paymentDoCaptureResponseAPIResponseModel.getReqParameters().getNameValueCollection()) {
                jsonRequest.put(pnvm.getKey(), pnvm.getValue());
            }
            recheckResponseAsyncTask = asyncTaskForRequest.getDoCaptureResponseRequestAsyncTask(jsonRequest, paymentDoCaptureResponseAPIResponseModel.getReqParameters().getAPIUrl());
            recheckResponseAsyncTask.setApiCallAsyncTaskDelegate(new DoCaptureResponseAsyncTaskDelegateResult(showProgressDialog));
            recheckResponseAsyncTask.setProgressBarVisible(showProgressDialog);
            if (isNetworkAvailable(activity)) {
                recheckResponseAsyncTask.execute(recheckResponseAsyncTask);
            } else {
                Toast.makeText(activity, getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
