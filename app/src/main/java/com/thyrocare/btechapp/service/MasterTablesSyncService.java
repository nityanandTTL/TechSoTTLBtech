package com.thyrocare.btechapp.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.dao.DhbDao;
import com.thyrocare.btechapp.dao.models.BrandMasterDao;
import com.thyrocare.btechapp.dao.models.LabAlertMasterDao;
import com.thyrocare.btechapp.dao.models.TestRateMasterDao;
import com.thyrocare.btechapp.models.api.response.FetchLabAlertMasterAPIResponseModel;
import com.thyrocare.btechapp.models.data.BrandMasterModel;
import com.thyrocare.btechapp.models.data.BrandTestMasterModel;
import com.thyrocare.btechapp.models.data.LabAlertMasterModel;
import com.thyrocare.btechapp.models.data.TestRateMasterModel;


import com.thyrocare.btechapp.network.ResponseParser;
import com.thyrocare.btechapp.utils.api.NetworkUtils;
import com.thyrocare.btechapp.utils.app.AppConstants;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.Global;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class MasterTablesSyncService extends Service {
    private static final int MASTER_TABLE_BRANDS = 0;
    private static final int MASTER_TABLE_LAB_ALERTS = 1;
    private static final int MASTER_TABLE_TESTS = 2;
    private static final int retryCountMax = 5;
    private static int uploadTotalCount = 2;
    private static int previousBrandCount = 0;
    private DhbDao dhbDao;
    private AppPreferenceManager appPreferenceManager;
    private Context context;
    private int retryCount = 0;
    private boolean isIssueFound = false;
    private int serviceId;
    private ArrayList<Integer> masterTableUploaded = new ArrayList<>();
    private ArrayList<Integer> masterTableTotalToBeUploaded = new ArrayList<>();
    private Global globalclass;

    public MasterTablesSyncService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        globalclass = new Global(context);
        dhbDao = new DhbDao(getApplicationContext());
        appPreferenceManager = new AppPreferenceManager(getApplicationContext());
        masterTableUploaded = new ArrayList<>();
        masterTableTotalToBeUploaded = new ArrayList<>();
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
        for (int i = 0; i < uploadTotalCount; i++) {
            masterTableUploaded.add(i, 0);
            masterTableTotalToBeUploaded.add(i, 1);
        }
        if (appPreferenceManager.getLoginResponseModel() == null) {
            broadcastSyncIssue();
            stopSelf(serviceId);
        }
        if (NetworkUtils.isNetworkAvailable(getApplicationContext())) {
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
        try {
            if (masterTableUploaded != null && masterTableTotalToBeUploaded != null
                    && masterTableUploaded.get(MASTER_TABLE_BRANDS) < masterTableTotalToBeUploaded.get(MASTER_TABLE_BRANDS)) {
                callMasterTableUpdateUploadAPI(MASTER_TABLE_BRANDS, "", 0);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //LAB ALERT MASTER
        try {
            if (masterTableUploaded != null && masterTableTotalToBeUploaded != null
                    && masterTableUploaded.get(MASTER_TABLE_LAB_ALERTS) < masterTableTotalToBeUploaded.get(MASTER_TABLE_LAB_ALERTS)) {
                callMasterTableUpdateUploadAPI(MASTER_TABLE_LAB_ALERTS, "", 0);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // BRAND WISE TEST MASTER
        try {
            BrandMasterDao brandMasterDao = new BrandMasterDao(dhbDao.getDb());
            ArrayList<BrandMasterModel> brandMasterModels = brandMasterDao.getAllModels();
            if (masterTableUploaded != null && masterTableTotalToBeUploaded != null && masterTableUploaded.size() > MASTER_TABLE_TESTS && masterTableTotalToBeUploaded.size() > MASTER_TABLE_TESTS &&
                    (masterTableUploaded.get(MASTER_TABLE_TESTS) < masterTableTotalToBeUploaded.get(MASTER_TABLE_TESTS)) && previousBrandCount < brandMasterModels.size()) {
                callMasterTableUpdateUploadAPI(MASTER_TABLE_TESTS, brandMasterModels.get(previousBrandCount).getBrandId() + "", previousBrandCount);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        broadcastSyncDone();
        stopSelf(serviceId);
    }


    private void callMasterTableUpdateUploadAPI(final int infoType, String brandId, final int previousCount) {

        Call<String> responseCall;
        if (infoType == MASTER_TABLE_BRANDS) {
            GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(context, "https://phlebo-management-public.thyrocare.com/").create(GetAPIInterface.class);
            responseCall = apiInterface.CallFetchBrandMasterApi();
        } else if (infoType == MASTER_TABLE_LAB_ALERTS) {
            GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(context, EncryptionUtils.Dcrp_Hex(context.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
            responseCall = apiInterface.CallGetFetchLabAlertMasterApi();
        } else {
            GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(context, EncryptionUtils.Dcrp_Hex(context.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
            responseCall = apiInterface.CallGetFetchBrandwiseTestMasterApi(brandId);
        }
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    retryCount = 0;
                    ResponseParser responseParser = new ResponseParser(getApplicationContext());
                    responseParser.setToShowErrorDailog(false);
                    responseParser.setToShowToast(false);
                    switch (infoType) {
                        case MASTER_TABLE_BRANDS: {
                            ArrayList<BrandMasterModel> brandMastersArr = new ArrayList<BrandMasterModel>();
                            brandMastersArr = responseParser.getBrandMaster(response.body(), response.code());
                            masterTableUploaded.set(MASTER_TABLE_BRANDS, 1);
                            masterTableTotalToBeUploaded.set(MASTER_TABLE_BRANDS, 0);

                            ////////////////// FOR HANDLING BRAND WISE TEST MASTER FETCH API CALL
                            if (brandMastersArr != null && brandMastersArr.size() > 0) {
                                for (int i = uploadTotalCount; i < uploadTotalCount + brandMastersArr.size(); i++) {
                                    masterTableUploaded.add(i, 0);
                                    masterTableTotalToBeUploaded.add(i, 1);
                                }
                            }

                            int size = brandMastersArr != null ? brandMastersArr.size() : 0;
                            uploadTotalCount = uploadTotalCount + size;
                            ///////////////// END OF FOR HANDLING BRAND WISE TEST MASTER FETCH API CALL

                            if (brandMastersArr != null && brandMastersArr.size() > 0) {
                                for (BrandMasterModel brandMaster : brandMastersArr) {
                                    BrandMasterDao brandMasterDao = new BrandMasterDao(dhbDao.getDb());
                                    brandMasterDao.insertOrUpdate(brandMaster);
                                }
                                int previousCount = masterTableUploaded.get(infoType);
                                masterTableUploaded.set(infoType, brandMastersArr.size() + previousCount);
                            } else {
                                masterTableUploaded.set(infoType, masterTableTotalToBeUploaded.get(infoType));
                            }
                        }
                        break;
                        case MASTER_TABLE_LAB_ALERTS: {
                            FetchLabAlertMasterAPIResponseModel fetchLabAlertMasterAPIResponseModel = new FetchLabAlertMasterAPIResponseModel();
                            fetchLabAlertMasterAPIResponseModel = responseParser.getLabAlertMasterAPIResponseModel(response.body(), response.code());
                            masterTableUploaded.set(MASTER_TABLE_LAB_ALERTS, 1);
                            masterTableTotalToBeUploaded.set(MASTER_TABLE_LAB_ALERTS, 0);

                            if (fetchLabAlertMasterAPIResponseModel != null && fetchLabAlertMasterAPIResponseModel.getTestLabAlerts().size() > 0) {
                                for (LabAlertMasterModel labAlertMasterModel : fetchLabAlertMasterAPIResponseModel.getTestLabAlerts()) {
                                    LabAlertMasterDao labAlertMasterDao = new LabAlertMasterDao(dhbDao.getDb());
                                    labAlertMasterDao.insertOrUpdate(labAlertMasterModel);
                                }
                                int previousCount = masterTableUploaded.get(infoType);
                                masterTableUploaded.set(infoType, fetchLabAlertMasterAPIResponseModel.getTestLabAlerts().size() + previousCount);
                            } else {
                                masterTableUploaded.set(infoType, masterTableTotalToBeUploaded.get(infoType));
                            }
                        }
                        break;
                        case MASTER_TABLE_TESTS: {
                            BrandTestMasterModel brandTestMasterModel = new BrandTestMasterModel();
                            brandTestMasterModel = responseParser.getBrandTestMaster(response.body(), response.code());
                            if (brandTestMasterModel != null && brandTestMasterModel.getTstratemaster() != null && brandTestMasterModel.getTstratemaster().size() > 0) {
                                TestRateMasterDao testRateMasterDao = new TestRateMasterDao(dhbDao.getDb());
                                for (TestRateMasterModel testRateMasterModel :
                                        brandTestMasterModel.getTstratemaster()) {
                                    testRateMasterModel.setBrandId(brandTestMasterModel.getBrandId());
                                    testRateMasterModel.setBrandName(brandTestMasterModel.getBrandName());
                                    testRateMasterDao.insertOrUpdate(testRateMasterModel);
                                }
                                previousBrandCount++;
                                int previousCnt = masterTableUploaded.get(infoType + previousCount);
                                int toBeUploadedCnt = masterTableTotalToBeUploaded.get(infoType + previousCount);
                                masterTableUploaded.set(infoType + previousCount, brandTestMasterModel.getTstratemaster().size() + previousCnt);
                                masterTableTotalToBeUploaded.set(infoType + previousCount, toBeUploadedCnt - brandTestMasterModel.getTstratemaster().size());
                            } else {
                                masterTableUploaded.set(infoType + previousCount, masterTableTotalToBeUploaded.get(infoType));
                            }
                        }
                        break;
                        default: {

                        }
                        break;
                    }
                    broadcastSyncProgress(false);
                } else {
                    retryCount++;
                }

                if (retryCount < retryCountMax) {
                    startUpdatingMasterTables();
                } else {
                    if (retryCount >= retryCountMax) {
                        isIssueFound = true;
                        masterTableUploaded.set(infoType, masterTableTotalToBeUploaded.get(infoType));
                    }
                    broadcastSyncProgress(false);
                    startUpdatingMasterTables();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                broadcastSyncDone();
                stopSelf(serviceId);
            }
        });
    }


    public void broadcastSyncProgress(boolean isInInitialMode) {
        Intent intent = new Intent();
        intent.setAction(AppConstants.MASTER_TABLE_UPDATE_ACTION_IN_PROGRESS);
        if (isInInitialMode) {
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
        for (int i = 0; i < masterTableUploaded.size(); i++) {
            if (masterTableTotalToBeUploaded.get(i) != 0) {
                totalUploadCount = totalUploadCount + masterTableTotalToBeUploaded.get(i);
            }
        }
        return totalUploadCount;
    }

    public int getUploadCount() {
        int uploadCount = 0;
        for (int i = 0; i < masterTableUploaded.size(); i++) {
            if (masterTableUploaded.get(i) != 0) {
                uploadCount = uploadCount + masterTableUploaded.get(i);
            }
        }
        return uploadCount;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dhbDao != null) {
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
