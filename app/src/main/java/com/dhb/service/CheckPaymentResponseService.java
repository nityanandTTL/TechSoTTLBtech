package com.dhb.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.dhb.utils.api.NetworkUtils;
import com.dhb.utils.app.AppConstants;
import com.dhb.utils.app.AppPreferenceManager;

public class CheckPaymentResponseService extends Service {

    private Context context;
    private AppPreferenceManager appPreferenceManager;
    private int retryCount = 0;
    private static final int retryCountMax = 5;
    private boolean isIssueFound = false;
    private int serviceId;

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

        if (appPreferenceManager.getLoginResponseModel()==null){
            broadcastSyncIssue();
            stopSelf(serviceId);
        }

        if (NetworkUtils.isNetworkAvailable(getApplicationContext())){
            isIssueFound = false;
            broadcastSyncProgress(true);
            startUpdatingMasterTables();
        } else {
            broadcastSyncIssue();
            stopSelf(serviceId);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void startUpdatingMasterTables() {
        broadcastSyncDone();
        stopSelf(serviceId);
    }

    private void callMasterTableUpdateUploadAPI(int infoType, String brandId, int previousCount) {
        /*AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(context);
        ApiCallAsyncTask queueUploadApiCallAsyncTask = new ApiCallAsyncTask(context);
        queueUploadApiCallAsyncTask.setApiCallAsyncTaskDelegate(new MasterTableUpdateApiCallResult(infoType,previousCount));
        queueUploadApiCallAsyncTask.execute(queueUploadApiCallAsyncTask);*/
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
