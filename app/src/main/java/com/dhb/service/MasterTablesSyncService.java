package com.dhb.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.dhb.dao.DhbDao;
import com.dhb.dao.models.BrandMasterDao;
import com.dhb.dao.models.TestRateMasterDao;
import com.dhb.models.data.BrandMasterModel;
import com.dhb.models.data.BrandTestMasterModel;
import com.dhb.models.data.TestRateMasterModel;
import com.dhb.network.ApiCallAsyncTask;
import com.dhb.network.ApiCallAsyncTaskDelegate;
import com.dhb.network.AsyncTaskForRequest;
import com.dhb.network.ResponseParser;
import com.dhb.utils.api.NetworkUtils;
import com.dhb.utils.app.AppConstants;
import com.dhb.utils.app.AppPreferenceManager;

import org.json.JSONException;

import java.util.ArrayList;

public class MasterTablesSyncService extends Service {
    private DhbDao dhbDao;
    private AppPreferenceManager appPreferenceManager;
    private Context context;
    private static final int MASTER_TABLE_BRANDS = 0;
    private static final int MASTER_TABLE_TESTS = 1;

    private int retryCount = 0;

    private static int uploadTotalCount = 1;

    private static final int retryCountMax = 5;

    private boolean isIssueFound = false;

    private int serviceId;

    private ArrayList<Integer> masterTableUploaded = new ArrayList<>();

    private ArrayList<Integer> masterTableTotalToBeUploaded = new ArrayList<>();

    private static int previousBrandCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        dhbDao = new DhbDao(getApplicationContext());
        appPreferenceManager = new AppPreferenceManager(getApplicationContext());
        masterTableUploaded = new ArrayList<>();
        masterTableTotalToBeUploaded = new ArrayList<>();
    }

    public MasterTablesSyncService() {
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
        for (int i = 0; i < uploadTotalCount; i++){
            masterTableUploaded.add(i, 0);
        }

        masterTableTotalToBeUploaded.add(0,1);
        /*for (int i = 0; i < uploadTotalCount; i++){
            masterTableTotalToBeUploaded.add(i, 0);
        }*/

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

        //BRAND MASTER
        if (masterTableUploaded != null && masterTableTotalToBeUploaded != null
                && masterTableUploaded.get(MASTER_TABLE_BRANDS) < masterTableTotalToBeUploaded.get(MASTER_TABLE_BRANDS)){
            callMasterTableUpdateUploadAPI(MASTER_TABLE_BRANDS,"");
            return;
        }

        BrandMasterDao brandMasterDao = new BrandMasterDao(dhbDao.getDb());
        ArrayList<BrandMasterModel> brandMasterModels = brandMasterDao.getAllModels();
        if (masterTableUploaded != null && masterTableTotalToBeUploaded != null && (masterTableUploaded.get(MASTER_TABLE_TESTS) < masterTableTotalToBeUploaded.get(MASTER_TABLE_TESTS)) && previousBrandCount<brandMasterModels.size()){
            callMasterTableUpdateUploadAPI(MASTER_TABLE_TESTS+previousBrandCount,brandMasterModels.get(previousBrandCount).getBrandId()+"");
            return;
        }

        broadcastSyncDone();
        stopSelf(serviceId);
    }

    private void callMasterTableUpdateUploadAPI(int infoType,String brandId) {
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(context);
        ApiCallAsyncTask queueUploadApiCallAsyncTask = new ApiCallAsyncTask(context);
        if(infoType==0) {
            queueUploadApiCallAsyncTask = asyncTaskForRequest.getFetchBrandMasterRequestAsyncTask();
        }
        else if(infoType==1) {
            queueUploadApiCallAsyncTask = asyncTaskForRequest.getFetchBrandwiseTestMasterRequestAsyncTask(brandId);
        }
        queueUploadApiCallAsyncTask.setApiCallAsyncTaskDelegate(new MasterTableUpdateApiCallResult(infoType));
        queueUploadApiCallAsyncTask.execute(queueUploadApiCallAsyncTask);
    }

    public void broadcastSyncProgress(boolean isInInitialMode) {
        Intent intent = new Intent();
        intent.setAction(AppConstants.MASTER_TABLE_UPDATE_ACTION_IN_PROGRESS);
        if (isInInitialMode){
            intent.putExtra(AppConstants.MASTER_TABLE_UPDATE_TOTAL_COUNT, 0);
            intent.putExtra(AppConstants.MASTER_TABLE_UPDATE_COMPLETED_COUNT, 0);
        } else {
            intent.putExtra(AppConstants.MASTER_TABLE_UPDATE_TOTAL_COUNT, getTotalUploadCount());
            intent.putExtra(AppConstants.MASTER_TABLE_UPDATE_COMPLETED_COUNT, getUploadCount());
        }
        sendBroadcast(intent);
    }

    public void broadcastSyncDone() {
        Intent intent = new Intent();
        intent.setAction(AppConstants.MASTER_TABLE_UPDATE_ACTION_DONE);
        intent.putExtra(AppConstants.MASTER_TABLE_UPDATE_ISSUE_FOUND, isIssueFound);
        sendBroadcast(intent);
    }

    public void broadcastSyncNoData() {
        Intent intent = new Intent();
        intent.setAction(AppConstants.MASTER_TABLE_UPDATE_ACTION_NO_DATA);
        sendBroadcast(intent);
    }

    public void broadcastSyncIssue() {
        Intent intent = new Intent();
        intent.setAction(AppConstants.MASTER_TABLE_UPDATE_ACTION_ISSUE);
        sendBroadcast(intent);
    }


    public int getTotalUploadCount() {
        int totalUploadCount = 0;
        for (int i = 0; i < masterTableUploaded.size(); i++){
            if (masterTableTotalToBeUploaded.get(i) != 0){
                totalUploadCount = totalUploadCount + masterTableTotalToBeUploaded.get(i);
            }
        }
        return totalUploadCount;
    }

    public int getUploadCount() {
        int uploadCount = 0;
        for (int i = 0; i < masterTableUploaded.size(); i++){
            if (masterTableUploaded.get(i) != 0){
                uploadCount = uploadCount + masterTableUploaded.get(i);
            }
        }
        return uploadCount;
    }


    private class MasterTableUpdateApiCallResult implements ApiCallAsyncTaskDelegate {
        private int requestType;
        public MasterTableUpdateApiCallResult(int infoType) {
            this.requestType = infoType;
        }

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {

            if(statusCode==200) {
                retryCount = 0;
                ResponseParser responseParser = new ResponseParser(getApplicationContext());
                responseParser.setToShowErrorDailog(false);
                responseParser.setToShowToast(false);
                switch (requestType) {
                    case 0: {
                        ArrayList<BrandMasterModel> brandMastersArr = new ArrayList<BrandMasterModel>();
                        brandMastersArr = responseParser.getBrandMaster(json, statusCode);
                        masterTableUploaded.set(0,1);
                        masterTableTotalToBeUploaded.set(0,0);
                        for (int i = uploadTotalCount; i < uploadTotalCount + brandMastersArr.size(); i++){
                            masterTableUploaded.add(i,0);
                            masterTableTotalToBeUploaded.add(i, 1);
                        }
                        uploadTotalCount = uploadTotalCount + brandMastersArr.size();

                        if (brandMastersArr.size() > 0) {
                            for (BrandMasterModel brandMaster : brandMastersArr) {
                                BrandMasterDao brandMasterDao = new BrandMasterDao(dhbDao.getDb());
                                brandMasterDao.insertOrUpdate(brandMaster);
                            }
                            int previousCount = masterTableUploaded.get(requestType);
                            masterTableUploaded.set(requestType, brandMastersArr.size() + previousCount);
                        } else {
                            masterTableUploaded.set(requestType, masterTableTotalToBeUploaded.get(requestType));
                        }
                    }
                    break;
                    case 1: {
                        ArrayList<BrandTestMasterModel> testMastersArr = new ArrayList<BrandTestMasterModel>();
                        testMastersArr = responseParser.getTestMaster(json, statusCode);
                        if (testMastersArr.size() > 0) {
                            for (BrandTestMasterModel brandTestMasterModel : testMastersArr) {
                                TestRateMasterDao testRateMasterDao = new TestRateMasterDao(dhbDao.getDb());
                                for (TestRateMasterModel testRateMasterModel :
                                        brandTestMasterModel.getTstratemaster()) {
                                    testRateMasterModel.setBrandId(brandTestMasterModel.getBrandId());
                                    testRateMasterModel.setBrandName(brandTestMasterModel.getBrandName());
                                    testRateMasterDao.insertOrUpdate(testRateMasterModel);
                                }
                            }
                            previousBrandCount++;
                            int previousCount = masterTableUploaded.get(requestType);
                            masterTableUploaded.set(requestType, testMastersArr.size() + previousCount);
                        } else {
                            masterTableUploaded.set(requestType, masterTableTotalToBeUploaded.get(requestType));
                        }
                    }
                    break;
                    default: {

                    }
                    break;
                }
                broadcastSyncProgress(false);
            }
            else {
                retryCount++;
            }

            if (retryCount < retryCountMax){
                startUpdatingMasterTables();
            } else {
                if (retryCount >= retryCountMax){
                    isIssueFound = true;
                    masterTableUploaded.set(requestType, masterTableTotalToBeUploaded.get(requestType));
                }
                broadcastSyncProgress(false);
                startUpdatingMasterTables();
            }
        }

        @Override
        public void onApiCancelled() {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dhbDao != null){
            dhbDao.closeDatabase();
        }
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
