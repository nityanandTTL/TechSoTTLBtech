package com.thyrocare.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.thyrocare.R;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.utils.api.NetworkUtils;
import com.thyrocare.utils.app.AppConstants;
import com.thyrocare.utils.app.AppPreferenceManager;
import com.thyrocare.utils.app.BundleConstants;

import org.json.JSONException;

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
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
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
        ApiCallAsyncTask recheckPaymentResponseAsyncTask = new AsyncTaskForRequest(getApplicationContext()).getRecheckPaymentResponseRequestAsyncTask(jsonRequest,recheckResponseURL);
        recheckPaymentResponseAsyncTask.setApiCallAsyncTaskDelegate(new RecheckPaymentResponseAsyncTaskDelegateResult());
        if(NetworkUtils.isNetworkAvailable(getApplicationContext())){
            recheckPaymentResponseAsyncTask.execute(recheckPaymentResponseAsyncTask);
        }
        else{
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.internet_connetion_error),Toast.LENGTH_SHORT).show();
        }
    }

    private class RecheckPaymentResponseAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            Toast.makeText(context,""+json,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onApiCancelled() {

        }
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
