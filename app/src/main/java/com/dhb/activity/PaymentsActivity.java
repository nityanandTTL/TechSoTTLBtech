package com.dhb.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.models.api.response.PaymentProcessAPIResponseModel;
import com.dhb.models.data.NarrationMasterModel;
import com.dhb.models.data.PaymentNameValueModel;
import com.dhb.network.ApiCallAsyncTask;
import com.dhb.network.ApiCallAsyncTaskDelegate;
import com.dhb.network.AsyncTaskForRequest;
import com.dhb.network.ResponseParser;
import com.dhb.uiutils.AbstractActivity;
import com.dhb.utils.app.AppPreferenceManager;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        activity = this;
        appPreferenceManager = new AppPreferenceManager(activity);
        asyncTaskForRequest = new AsyncTaskForRequest(activity);
        responseParser = new ResponseParser(activity);
        fetchNarrationMaster();
        initUI();
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
            fetchPaymentModes(nmm);
        }
    }

    private void fetchPaymentModes(NarrationMasterModel nmm) {
        fetchPaymentModesAsyncTask = asyncTaskForRequest.getPaymentModesFromNarrationIdRequestAsyncTask(nmm);
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
                llPaymentModes.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                llPaymentModes.setOrientation(LinearLayout.VERTICAL);
                llPaymentModes.setGravity(Gravity.CENTER);
                for (PaymentNameValueModel pnvm:
                     pparm.getNameValueCollection()) {
                    if(pnvm.getKey().equals("ModeName")){
                        TextView txtPaymentMode = new TextView(activity);
                        LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                        txtParams.setMargins(10,10,10,10);
                        txtPaymentMode.setLayoutParams(txtParams);
                        txtPaymentMode.setGravity(Gravity.CENTER);
                        txtPaymentMode.setMinEms(10);
                        txtPaymentMode.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_purple_bg_gradient));
                        txtPaymentMode.setText(pnvm.getValue());
                        llPaymentModeDetails.setTag(pparm);
                        llPaymentModeDetails.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                fetchPaymentPassInputs((PaymentProcessAPIResponseModel) v.getTag());
                            }
                        });
                        llPaymentModeDetails.addView(txtPaymentMode);
                    }
                }
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
            LinearLayout llPaymentPassInputs = new LinearLayout(activity);
            LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            llParams.setMargins(10,10,10,10);
            llPaymentPassInputs.setLayoutParams(llParams);
            llPaymentPassInputs.setOrientation(LinearLayout.VERTICAL);
            llPaymentPassInputs.setGravity(Gravity.CENTER);
            for (int i=0;i<paymentPassInputsModel.getNameValueCollection().size();i++) {
                final int currentPosition = i;
                if(paymentPassInputsModel.getNameValueCollection().get(i).getRequired().equals("User")){
                    EditText edtPaymentInputs = new EditText(activity);
                    LinearLayout.LayoutParams edtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    edtParams.setMargins(10,5,5,10);
                    edtPaymentInputs.setLayoutParams(edtParams);
                    edtPaymentInputs.setGravity(Gravity.CENTER);
                    edtPaymentInputs.setMinEms(10);
                    edtPaymentInputs.setHint(paymentPassInputsModel.getNameValueCollection().get(i).getKey());
                    edtPaymentInputs.addTextChangedListener(new TextWatcher() {
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
                    llPaymentPassInputs.addView(edtPaymentInputs);
                }
            }
            TextView txtPaymentInputsSubmit = new TextView(activity);
            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            btnParams.setMargins(10,5,5,10);
            txtPaymentInputsSubmit.setLayoutParams(btnParams);
            txtPaymentInputsSubmit.setGravity(Gravity.CENTER);
            txtPaymentInputsSubmit.setMinEms(10);
            txtPaymentInputsSubmit.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_purple_bg_gradient));
            txtPaymentInputsSubmit.setText(getResources().getString(R.string.submit));
            txtPaymentInputsSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fetchTransactionResponse();
                }
            });
            llPaymentPassInputs.addView(txtPaymentInputsSubmit);
            flPayments.addView(llPaymentPassInputs);
        }
    }

    private void fetchTransactionResponse() {

    }
}
