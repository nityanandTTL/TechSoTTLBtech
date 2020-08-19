package com.thyrocare.btechapp.service;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import android.widget.Toast;

import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.models.api.response.PaymentDoCaptureResponseAPIResponseModel;



import com.thyrocare.btechapp.utils.api.NetworkUtils;
import com.thyrocare.btechapp.utils.app.AppConstants;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.Global;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckPaymentResponseService extends Service {

    private Context context;
    private AppPreferenceManager appPreferenceManager;
    private int retryCount = 0;
    private static final int retryCountMax = 5;
    private boolean isIssueFound = false;
    private int serviceId;
    private String jsonRequest;
    private String recheckResponseURL;
    private boolean isRecheckComplete = false;
    private Global global;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        global = new Global(context);
        appPreferenceManager = new AppPreferenceManager(context);
    }

    public CheckPaymentResponseService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        /*// TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");*/
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        serviceId = startId;
        jsonRequest = intent.getExtras().getString(BundleConstants.PAYMENTS_RECHECK_REQUEST);
        recheckResponseURL = intent.getExtras().getString(BundleConstants.PAYMENTS_RECHECK_REQUEST_URL);
        if (appPreferenceManager.getLoginResponseModel()==null){
            broadcastSyncIssue();
            stopSelf(serviceId);
        }

        if (NetworkUtils.isNetworkAvailable(getApplicationContext())){
            isIssueFound = false;
            broadcastSyncProgress(true);
            startRecheckingPaymentResponse();
        } else {
            broadcastSyncIssue();
            stopSelf(serviceId);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void startRecheckingPaymentResponse() {
        if(!isRecheckComplete||retryCount<=retryCountMax){
            callRecheckPaymentResponseAPI(jsonRequest,recheckResponseURL);
        }
        broadcastSyncDone();
        stopSelf(serviceId);
    }

    private void callRecheckPaymentResponseAPI(String jsonRequest, String recheckResponseURL) {
        if(NetworkUtils.isNetworkAvailable(getApplicationContext())){
            CallgetRecheckPaymentResponseRequestApi(jsonRequest,recheckResponseURL);
        } else{
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.internet_connetion_error),Toast.LENGTH_SHORT).show();
        }
    }

    private void CallgetRecheckPaymentResponseRequestApi(String jsonRequest, String URL){
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(context, EncryptionUtils.DecodeString64(context.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallgetRecheckPaymentResponseRequestApi(URL,jsonRequest);
        global.showProgressDialog(context,"Please wait..");
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                global.hideProgressDialog();
                if (response.isSuccessful()){
                    Toast.makeText(context, !StringUtils.isNull(response.body()) ? response.body() : ConstantsMessages.SOMETHING_WENT_WRONG,Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, ConstantsMessages.SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                global.hideProgressDialog();
            }
        });
    }

    public void broadcastSyncProgress(boolean isInInitialMode) {
        Intent intent = new Intent();
        intent.setAction(AppConstants.CHECK_PAYMENT_RESPONSE_ACTION_IN_PROGRESS);
        /*if (isInInitialMode){
            intent.putExtra(AppConstants.MASTER_TABLE_UPDATE_TOTAL_COUNT, 0);
            intent.putExtra(AppConstants.MASTER_TABLE_UPDATE_COMPLETED_COUNT, 0);
        } else {
            intent.putExtra(AppConstants.MASTER_TABLE_UPDATE_TOTAL_COUNT, getTotalUploadCount());
            intent.putExtra(AppConstants.MASTER_TABLE_UPDATE_COMPLETED_COUNT, getUploadCount());
        }*/
        sendBroadcast(intent);
    }

    public void broadcastSyncDone() {
        Intent intent = new Intent();
        intent.setAction(AppConstants.CHECK_PAYMENT_RESPONSE_ACTION_DONE);
        intent.putExtra(AppConstants.CHECK_PAYMENT_RESPONSE_ISSUE_FOUND, isIssueFound);
        sendBroadcast(intent);
    }

    public void broadcastSyncNoData() {
        Intent intent = new Intent();
        intent.setAction(AppConstants.CHECK_PAYMENT_RESPONSE_ACTION_NO_DATA);
        sendBroadcast(intent);
    }

    public void broadcastSyncIssue() {
        Intent intent = new Intent();
        intent.setAction(AppConstants.CHECK_PAYMENT_RESPONSE_ACTION_ISSUE);
        sendBroadcast(intent);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

}
