package com.dhb.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.dhb.service.MasterTablesSyncService;
import com.dhb.uiutils.AbstractActivity;
import com.dhb.utils.app.AppConstants;
import com.dhb.utils.app.AppPreferenceManager;
import com.dhb.utils.app.BundleConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
    private int Amount = 0;
    private int SourceCode = 0;
    private String BillingName = "";
    private String BillingAddr = "";
    private String BillingPin = "";
    private String BillingMob = "";
    private String BillingEmail = "";
    private PaymentStartTransactionAPIResponseModel paymentStartTransactionAPIResponseModel;
    private PaymentDoCaptureResponseAPIResponseModel paymentDoCaptureResponseAPIResponseModel;
    private int checkPaymentSuccessResponseRetryCount = 0;
    private ApiCallAsyncTask doCaptureResponseAsyncTask;
    private ApiCallAsyncTask recheckResponseAsyncTask;
    private PowerManager.WakeLock wakeLock;

    //TODO tejas - 7738185400 for airtel money
    //TODO tejas - tejaspatil92axisbank@axis for UPI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        activity = this;
        appPreferenceManager = new AppPreferenceManager(activity);
        asyncTaskForRequest = new AsyncTaskForRequest(activity);
        responseParser = new ResponseParser(activity);
        if(getIntent().getExtras()!=null){
            NarrationId = getIntent().getExtras().getInt(BundleConstants.PAYMENTS_NARRATION_ID);
            OrderNo = getIntent().getExtras().getString(BundleConstants.PAYMENTS_ORDER_NO);
            Amount = getIntent().getExtras().getInt(BundleConstants.PAYMENTS_AMOUNT);
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
    public void initUI() {
        super.initUI();
        flPayments = (FrameLayout) findViewById(R.id.fl_payments);
    }

    private void fetchNarrationMaster() {
        fetchNarrationMasterAsyncTask = asyncTaskForRequest.getNarrationMasterRequestAsyncTask();
        fetchNarrationMasterAsyncTask.setApiCallAsyncTaskDelegate(new FetchNarrationMasterAsyncTaskDelegateResult());
        if(isNetworkAvailable(activity)){
            fetchNarrationMasterAsyncTask.execute(fetchNarrationMasterAsyncTask);
        }
        else{
            Toast.makeText(activity,getResources().getString(R.string.internet_connetion_error),Toast.LENGTH_SHORT).show();
        }
    }

    private class FetchNarrationMasterAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if(statusCode==200){
                narrationsArr = responseParser.getNarrationMasterResponse(json,statusCode);
                initNarrationsData();
            }
            else{
                Toast.makeText(activity,json+"",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity,"No Response",Toast.LENGTH_SHORT).show();
        }
    }

    private void initNarrationsData() {
        if(narrationsArr!=null && narrationsArr.size()>0){
            flPayments.removeAllViews();
            LinearLayout llNarrations = new LinearLayout(activity);
            llNarrations.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            llNarrations.setOrientation(LinearLayout.VERTICAL);
            llNarrations.setGravity(Gravity.CENTER);
            for (NarrationMasterModel nmm:
                 narrationsArr) {
                Button btnNarration = new Button(activity);
                btnNarration.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                btnNarration.setText(nmm.getNarration());
                btnNarration.setTextColor(getResources().getColor(android.R.color.white));
                btnNarration.setPadding(5,5,5,5);
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
        if(isNetworkAvailable(activity)){
            fetchPaymentModesAsyncTask.execute(fetchPaymentModesAsyncTask);
        }
        else{
            Toast.makeText(activity,getResources().getString(R.string.internet_connetion_error),Toast.LENGTH_SHORT).show();
        }
    }
    private void fetchPaymentPassInputs(PaymentProcessAPIResponseModel pparm) {
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("URLId", pparm.getURLId());
            for (PaymentNameValueModel pnvm:
                 pparm.getNameValueCollection()) {
                 jsonRequest.put(pnvm.getKey(),pnvm.getValue());
            }
            fetchPaymentPassInputsAsyncTask = asyncTaskForRequest.getTransactionInputsRequestAsyncTask(jsonRequest);
            fetchPaymentPassInputsAsyncTask.setApiCallAsyncTaskDelegate(new FetchPaymentPassInputsAsyncTaskDelegateResult());
            if (isNetworkAvailable(activity)) {
                fetchPaymentPassInputsAsyncTask.execute(fetchPaymentPassInputsAsyncTask);
            } else {
                Toast.makeText(activity, getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private class FetchPaymentModesAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if(statusCode==200){
                paymentModesArr = responseParser.getPaymentModesResponse(json,statusCode);
                initPaymentModesData();
            }
            else{
                Toast.makeText(activity,json+"",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity,"No Response",Toast.LENGTH_SHORT).show();
        }
    }

    private void initPaymentModesData() {
        if(paymentModesArr!=null && paymentModesArr.size()>0){
            flPayments.removeAllViews();
            LinearLayout llPaymentModes = new LinearLayout(activity);
            llPaymentModes.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            llPaymentModes.setOrientation(LinearLayout.VERTICAL);
            llPaymentModes.setGravity(Gravity.CENTER);
            for (PaymentProcessAPIResponseModel pparm:
                    paymentModesArr) {
                LinearLayout llPaymentModeDetails = new LinearLayout(activity);
                llPaymentModeDetails.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                llPaymentModeDetails.setOrientation(LinearLayout.VERTICAL);
                llPaymentModeDetails.setGravity(Gravity.CENTER);
                for (PaymentNameValueModel pnvm:
                     pparm.getNameValueCollection()) {
                    if(pnvm.getKey().equals("ModeName")){
                        Button btnPaymentMode = new Button(activity);
                        LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                        txtParams.setMargins(10,10,10,10);
                        btnPaymentMode.setLayoutParams(txtParams);
                        btnPaymentMode.setGravity(Gravity.CENTER);
                        btnPaymentMode.setPadding(5,5,5,5);
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

    private class FetchPaymentPassInputsAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if(statusCode==200){
                paymentPassInputsModel = responseParser.getPaymentPassInputsResponse(json,statusCode);
                initPaymentPassInputsData();
            }
            else{
                Toast.makeText(activity,json+"",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity,"No Response",Toast.LENGTH_SHORT).show();
        }
    }

    private void initPaymentPassInputsData() {
        if(paymentPassInputsModel!=null
                && paymentPassInputsModel.getNameValueCollection()!=null
                && paymentPassInputsModel.getNameValueCollection().size()>0){
            flPayments.removeAllViews();
            ScrollView svPaymentPassInputs = new ScrollView(activity);
            ScrollView.LayoutParams svParams = new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT,ScrollView.LayoutParams.MATCH_PARENT);
            svPaymentPassInputs.setLayoutParams(svParams);

            LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout llPaymentPassInputs = new LinearLayout(activity);
            llParams.setMargins(10,10,10,10);
            llPaymentPassInputs.setLayoutParams(llParams);
            llPaymentPassInputs.setOrientation(LinearLayout.VERTICAL);
            llPaymentPassInputs.setGravity(Gravity.CENTER);
            for (int i=0;i<paymentPassInputsModel.getNameValueCollection().size();i++) {
                final int currentPosition = i;
                if(paymentPassInputsModel.getNameValueCollection().get(i).getRequired().equals("User")){
                    EditText edtPaymentUserInputs = new EditText(activity);
                    LinearLayout.LayoutParams edtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    edtParams.setMargins(10,5,5,10);
                    edtPaymentUserInputs.setLayoutParams(edtParams);
                    edtPaymentUserInputs.setGravity(Gravity.CENTER);
                    edtPaymentUserInputs.setMinEms(10);
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
                            paymentPassInputsModel.getNameValueCollection().get(currentPosition).setValue(s.toString());
                        }
                    });
                    if(paymentPassInputsModel.getNameValueCollection().get(i).getKey().equals("Amount") && NarrationId!=3){
                        paymentPassInputsModel.getNameValueCollection().get(i).setValue(Amount+"");
                        edtPaymentUserInputs.setText(paymentPassInputsModel.getNameValueCollection().get(i).getValue());
                    }
                    llPaymentPassInputs.addView(edtPaymentUserInputs);
                }
                else if(paymentPassInputsModel.getNameValueCollection().get(i).getRequired().equals("System")){
                    if(paymentPassInputsModel.getNameValueCollection().get(i).getKey().equals("NarrationId")){
                        paymentPassInputsModel.getNameValueCollection().get(i).setValue(NarrationId+"");
                    }
                    else if(paymentPassInputsModel.getNameValueCollection().get(i).getKey().equals("OrderNo")){
                        paymentPassInputsModel.getNameValueCollection().get(i).setValue(OrderNo);
                    }
                    else if(paymentPassInputsModel.getNameValueCollection().get(i).getKey().equals("SourceCode")){
                        paymentPassInputsModel.getNameValueCollection().get(i).setValue(SourceCode+"");
                    }
                    else if(paymentPassInputsModel.getNameValueCollection().get(i).getKey().equals("Amount")){
                        paymentPassInputsModel.getNameValueCollection().get(i).setValue(Amount+"");
                    }
                    else if(paymentPassInputsModel.getNameValueCollection().get(i).getKey().equals("BillingName")){
                        paymentPassInputsModel.getNameValueCollection().get(i).setValue(BillingName);
                    }
                    else if(paymentPassInputsModel.getNameValueCollection().get(i).getKey().equals("BillingPin")){
                        paymentPassInputsModel.getNameValueCollection().get(i).setValue(BillingPin+"");
                    }
                    else if(paymentPassInputsModel.getNameValueCollection().get(i).getKey().equals("BillingMob")){
                        paymentPassInputsModel.getNameValueCollection().get(i).setValue(BillingMob+"");
                    }
                    else if(paymentPassInputsModel.getNameValueCollection().get(i).getKey().equals("BillingAddr")){
                        paymentPassInputsModel.getNameValueCollection().get(i).setValue(BillingAddr+"");
                    }
                    else if(paymentPassInputsModel.getNameValueCollection().get(i).getKey().equals("BillingEmail")){
                        paymentPassInputsModel.getNameValueCollection().get(i).setValue(BillingEmail+"");
                    }

                    LinearLayout llPaymentPassSystemInputs = new LinearLayout(activity);
                    LinearLayout.LayoutParams llSystemInputsParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    llSystemInputsParams.setMargins(10,10,10,10);
                    llPaymentPassSystemInputs.setLayoutParams(llSystemInputsParams);
                    llPaymentPassSystemInputs.setOrientation(LinearLayout.VERTICAL);
                    llPaymentPassSystemInputs.setGravity(Gravity.CENTER);

                    TextView txtPaymentSystemInputsLabel = new TextView(activity);
                    LinearLayout.LayoutParams txtLabelParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    txtLabelParams.setMargins(10,5,5,10);
                    txtPaymentSystemInputsLabel.setLayoutParams(txtLabelParams);
                    txtPaymentSystemInputsLabel.setGravity(Gravity.START);
                    txtPaymentSystemInputsLabel.setMinEms(10);
                    txtPaymentSystemInputsLabel.setText(paymentPassInputsModel.getNameValueCollection().get(i).getHint()+":");

                    TextView txtPaymentSystemInputs = new TextView(activity);
                    LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    txtParams.setMargins(10,5,5,10);
                    txtPaymentSystemInputs.setLayoutParams(txtParams);
                    txtPaymentSystemInputs.setGravity(Gravity.CENTER);
                    txtPaymentSystemInputs.setMinEms(10);
                    txtPaymentSystemInputs.setHint(paymentPassInputsModel.getNameValueCollection().get(i).getValue());

                    llPaymentPassSystemInputs.addView(txtPaymentSystemInputsLabel);
                    llPaymentPassSystemInputs.addView(txtPaymentSystemInputs);

                    llPaymentPassInputs.addView(llPaymentPassSystemInputs);
                }
            }
            Button btnPaymentInputsSubmit = new Button(activity);
            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            btnParams.setMargins(10,5,5,10);
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
            svPaymentPassInputs.addView(llPaymentPassInputs);
            flPayments.addView(svPaymentPassInputs);
        }
    }

    private void fetchTransactionResponseOnStartTransaction() {
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("URLId", paymentPassInputsModel.getURLId());
            for (PaymentNameValueModel pnvm:
                paymentPassInputsModel.getNameValueCollection()) {
                if (pnvm.getKey().equals("SourceCode")) {
                    jsonRequest.put(pnvm.getKey(), appPreferenceManager.getLoginResponseModel().getUserID());
                } else {
                    jsonRequest.put(pnvm.getKey(), pnvm.getValue());
                }
            }
            startTransactionAsyncTask = asyncTaskForRequest.getStartTransactionRequestAsyncTask(jsonRequest);
            startTransactionAsyncTask.setApiCallAsyncTaskDelegate(new FetchTransactionResponseOnStartTransactionAsyncTaskDelegateResult());
            if (isNetworkAvailable(activity)) {
                startTransactionAsyncTask.execute(startTransactionAsyncTask);
            } else {
                Toast.makeText(activity, getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private class FetchTransactionResponseOnStartTransactionAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if(statusCode==200){
                paymentStartTransactionAPIResponseModel = responseParser.getPaymentStartTransactionResponse(json,statusCode);
                initDoCaptureResponseData();
            }
            else{
                Toast.makeText(activity,json+"",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity,"No Response",Toast.LENGTH_SHORT).show();
        }
    }

    private void initDoCaptureResponseData() {
        if(paymentStartTransactionAPIResponseModel!=null
                && paymentStartTransactionAPIResponseModel.getReqParameters()!=null
                && paymentStartTransactionAPIResponseModel.getReqParameters().getNameValueCollection()!=null
                && paymentStartTransactionAPIResponseModel.getReqParameters().getNameValueCollection().size()>0){
            flPayments.removeAllViews();
            LinearLayout llPaymentStartTransaction = new LinearLayout(activity);
            LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            llParams.setMargins(10,10,10,10);
            llPaymentStartTransaction.setLayoutParams(llParams);
            llPaymentStartTransaction.setOrientation(LinearLayout.VERTICAL);
            llPaymentStartTransaction.setGravity(Gravity.CENTER);
            for (int i=0;i<paymentStartTransactionAPIResponseModel.getReqParameters().getNameValueCollection().size();i++) {
                final int currentPosition = i;
                if (paymentStartTransactionAPIResponseModel.getReqParameters().getNameValueCollection().get(i).getRequired().equals("User")) {
                    EditText edtPaymentUserInputs = new EditText(activity);
                    LinearLayout.LayoutParams edtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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
            for (PaymentNameValueModel paymentNameValueModel:
                    paymentStartTransactionAPIResponseModel.getReqParameters().getNameValueCollection()) {
                if(paymentNameValueModel.getKey().equals("ModeId")&&paymentNameValueModel.getValue().equals("3")){
                    WebView wvQRDisplay = new WebView(activity);
                    WebSettings settings = wvQRDisplay.getSettings();
                    settings.setUseWideViewPort(true);
                    settings.setLoadWithOverviewMode(true);
                    settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
                    wvQRDisplay.setVerticalScrollBarEnabled(false);
                    wvQRDisplay.setHorizontalScrollBarEnabled(false);
                    LinearLayout.LayoutParams llwvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    llwvParams.setMargins(20,20,20,20);
                    wvQRDisplay.setLayoutParams(llwvParams);
                    wvQRDisplay.setPadding(0, 0, 0, 0);
                    wvQRDisplay.setInitialScale(getScale());
                    wvQRDisplay.loadDataWithBaseURL(null, paymentStartTransactionAPIResponseModel.getTokenData(), "text/html", "UTF-8", null);
                    llPaymentStartTransaction.addView(wvQRDisplay);
                    break;
                }
                else if(paymentNameValueModel.getKey().equals("ModeId")&&paymentNameValueModel.getValue().equals("1")){
                    WebView wvCCADisplay = new WebView(activity);
                    WebSettings settings = wvCCADisplay.getSettings();
                    settings.setJavaScriptEnabled(true);
                    settings.setBuiltInZoomControls(true);
//                    wvCCADisplay.setVerticalScrollBarEnabled(false);
//                    wvCCADisplay.setHorizontalScrollBarEnabled(false);
                    LinearLayout.LayoutParams llwvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    wvCCADisplay.setLayoutParams(llwvParams);
                    wvCCADisplay.setInitialScale(getScale());
                    wvCCADisplay.loadDataWithBaseURL(null, paymentStartTransactionAPIResponseModel.getTokenData(), "text/html", "UTF-8", null);
                    llPaymentStartTransaction.addView(wvCCADisplay);
                    break;
                }
            }
            Button btnPaymentStartTransactionSubmit = new Button(activity);
            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            btnParams.setMargins(10,5,5,10);
            btnPaymentStartTransactionSubmit.setLayoutParams(btnParams);
            btnPaymentStartTransactionSubmit.setGravity(Gravity.CENTER);
            btnPaymentStartTransactionSubmit.setMinEms(10);
            btnPaymentStartTransactionSubmit.setBackgroundDrawable(getResources().getDrawable(R.drawable.purple_btn_bg));
            btnPaymentStartTransactionSubmit.setText(getResources().getString(R.string.submit));
            btnPaymentStartTransactionSubmit.setTextColor(getResources().getColor(android.R.color.white));
            btnPaymentStartTransactionSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fetchDoCaptureResponse();
                }
            });
            llPaymentStartTransaction.addView(btnPaymentStartTransactionSubmit);
            flPayments.addView(llPaymentStartTransaction);
        }
    }

    private void fetchDoCaptureResponse() {
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("URLId", paymentStartTransactionAPIResponseModel.getReqParameters().getURLId());
            for (PaymentNameValueModel pnvm:
                    paymentStartTransactionAPIResponseModel.getReqParameters().getNameValueCollection()) {
                    jsonRequest.put(pnvm.getKey(), pnvm.getValue());
            }
            doCaptureResponseAsyncTask = asyncTaskForRequest.getDoCaptureResponseRequestAsyncTask(jsonRequest,paymentStartTransactionAPIResponseModel.getReqParameters().getAPIUrl());
            doCaptureResponseAsyncTask.setApiCallAsyncTaskDelegate(new DoCaptureResponseAsyncTaskDelegateResult());
            if (isNetworkAvailable(activity)) {
                doCaptureResponseAsyncTask.execute(doCaptureResponseAsyncTask);
            } else {
                Toast.makeText(activity, getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private class DoCaptureResponseAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if(statusCode==200){
                paymentDoCaptureResponseAPIResponseModel = responseParser.getPaymentDoCaptureAPIResponse(json,statusCode);
                if(paymentDoCaptureResponseAPIResponseModel.getStatus().equals("PAYMENT SUCCESS")){
                    Toast.makeText(activity,paymentDoCaptureResponseAPIResponseModel.getResponseMessage(),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra(BundleConstants.PAYMENT_STATUS,true);
                    setResult(BundleConstants.PAYMENTS_FINISH,intent);
                    finish();
                }
                else if(paymentDoCaptureResponseAPIResponseModel.getStatus().equals("PAYMENT FAILED")){
                    Toast.makeText(activity,paymentDoCaptureResponseAPIResponseModel.getResponseMessage(),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra(BundleConstants.PAYMENT_STATUS,false);
                    setResult(BundleConstants.PAYMENTS_FINISH,intent);
                    finish();
                }
                else{
                    checkPaymentSuccessResponseRetryCount++;
                    if(checkPaymentSuccessResponseRetryCount<=5) {
                        fetchRecheckResponseData();
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle("Payment Status")
                                .setMessage("Failed to Verify Payment Status!")
                                .setNegativeButton("Recheck", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        fetchRecheckResponseData();
                                    }
                                })
                                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent();
                                        intent.putExtra(BundleConstants.PAYMENT_STATUS,false);
                                        setResult(BundleConstants.PAYMENTS_FINISH,intent);
                                        finish();
                                    }
                                }).show();
                    }
                }
            }
            else{
                Toast.makeText(activity,json+"",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity,"No Response",Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchRecheckResponseData() {
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("URLId", paymentDoCaptureResponseAPIResponseModel.getReqParameters().getURLId());
            for (PaymentNameValueModel pnvm:
                    paymentDoCaptureResponseAPIResponseModel.getReqParameters().getNameValueCollection()) {
                jsonRequest.put(pnvm.getKey(), pnvm.getValue());
            }
            recheckResponseAsyncTask = asyncTaskForRequest.getDoCaptureResponseRequestAsyncTask(jsonRequest,paymentDoCaptureResponseAPIResponseModel.getReqParameters().getAPIUrl());
            recheckResponseAsyncTask.setApiCallAsyncTaskDelegate(new DoCaptureResponseAsyncTaskDelegateResult());
            if (isNetworkAvailable(activity)) {
                recheckResponseAsyncTask.execute(recheckResponseAsyncTask);
            } else {
                Toast.makeText(activity, getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
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
        intent.putExtra(BundleConstants.CHECK_PAYMENT_RESPONSE_JSON_REQUEST,jsonRequest);
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

    public class CheckPaymentStatusResponseReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() != null) {
                if(intent.getAction().equals(AppConstants.CHECK_PAYMENT_RESPONSE_ACTION_IN_PROGRESS)){
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
