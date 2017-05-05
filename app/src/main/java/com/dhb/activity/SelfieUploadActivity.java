package com.dhb.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.customview.RoundedImageView;
import com.dhb.customview.TouchImageView;
import com.dhb.dao.DhbDao;
import com.dhb.models.api.request.SelfieUploadRequestModel;
import com.dhb.models.api.response.SelfieUploadResponseModel;
import com.dhb.network.ApiCallAsyncTask;
import com.dhb.network.ApiCallAsyncTaskDelegate;
import com.dhb.network.AsyncTaskForRequest;
import com.dhb.network.ResponseParser;
import com.dhb.service.MasterTablesSyncService;
import com.dhb.uiutils.AbstractActivity;
import com.dhb.utils.api.NetworkUtils;
import com.dhb.utils.app.AppConstants;
import com.dhb.utils.app.AppPreferenceManager;
import com.dhb.utils.app.CommonUtils;
import com.dhb.utils.app.DeviceUtils;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;


public class SelfieUploadActivity extends AbstractActivity implements View.OnClickListener {
    private static final String TAG = SelfieUploadActivity.class.getSimpleName();
    TextView tv_username, tv_user_address;
    RoundedImageView img_user_picture;
    Button btn_takePhoto, btn_uploadPhoto;
    String userChoosenTask, encodedProImg;
    Bitmap thumbnail;// = null;
    private static final int REQUEST_CAMERA = 100;
    Activity activity;
    AppPreferenceManager appPreferenceManager;
    private DhbDao dhbDao;
    private PowerManager.WakeLock wakeLock;
    private ProgressDialog syncBarProgressDialog;
    private boolean isAfterMasterSyncDone;
    SyncStatusReceiver syncStatusReceiver;

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filterAutoUploadProgress = new IntentFilter(AppConstants.OFFLINE_STATUS_ACTION_IN_PROGRESS);
        IntentFilter filterAutoUploadDone = new IntentFilter(AppConstants.OFFLINE_STATUS_ACTION_DONE);
        IntentFilter filterAutoUploadNoData = new IntentFilter(AppConstants.OFFLINE_STATUS_ACTION_NO_DATA);
        IntentFilter filterAutoUploadIssue = new IntentFilter(AppConstants.OFFLINE_STATUS_ACTION_ISSUE);

        IntentFilter filterMasterSyncProgress = new IntentFilter(AppConstants.MASTER_TABLE_UPDATE_ACTION_IN_PROGRESS);
        IntentFilter filterMasterSyncDone = new IntentFilter(AppConstants.MASTER_TABLE_UPDATE_ACTION_DONE);
        IntentFilter filterMasterSyncNoData = new IntentFilter(AppConstants.MASTER_TABLE_UPDATE_ACTION_NO_DATA);
        IntentFilter filterMasterSyncIssue = new IntentFilter(AppConstants.MASTER_TABLE_UPDATE_ACTION_ISSUE);

        syncStatusReceiver = new SyncStatusReceiver();

        registerReceiver(syncStatusReceiver, filterAutoUploadProgress);
        registerReceiver(syncStatusReceiver, filterAutoUploadDone);
        registerReceiver(syncStatusReceiver, filterAutoUploadNoData);
        registerReceiver(syncStatusReceiver, filterAutoUploadIssue);

        registerReceiver(syncStatusReceiver, filterMasterSyncProgress);
        registerReceiver(syncStatusReceiver, filterMasterSyncDone);
        registerReceiver(syncStatusReceiver, filterMasterSyncNoData);
        registerReceiver(syncStatusReceiver, filterMasterSyncIssue);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (syncStatusReceiver != null){
            unregisterReceiver(syncStatusReceiver);
            syncStatusReceiver = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (syncStatusReceiver != null){
            unregisterReceiver(syncStatusReceiver);
            syncStatusReceiver = null;
        }
        if (dhbDao != null){
            dhbDao.closeDatabase();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfie_upload);
        activity = this;
        isAfterMasterSyncDone = false;
        dhbDao = new DhbDao(activity);
        appPreferenceManager = new AppPreferenceManager(activity);
        initUI();
        initData();
        setListners();
    }

    private void initData() {
        tv_username.setText(appPreferenceManager.getLoginResponseModel().getUserName());
    }

    private void setListners() {
        btn_takePhoto.setOnClickListener(this);
        btn_uploadPhoto.setOnClickListener(this);
        img_user_picture.setOnClickListener(this);
    }

    public void initUI() {
        super.initUI();
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_user_address = (TextView) findViewById(R.id.tv_user_address);
        img_user_picture = (RoundedImageView) findViewById(R.id.img_user_picture);
        btn_takePhoto = (Button) findViewById(R.id.btn_takePhoto);
        btn_uploadPhoto = (Button) findViewById(R.id.btn_uploadPhoto);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_takePhoto) {
            clickPhoto();
        }
        if (v.getId() == R.id.btn_uploadPhoto) {
            if (validate()) {
                AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
                SelfieUploadRequestModel selfieUploadRequestModel = new SelfieUploadRequestModel();
                Log.e(TAG, "onClick: btechId : "+ appPreferenceManager.getLoginResponseModel().getUserID());
                Log.e(TAG, "onClick: encodedProImg "+encodedProImg );
                selfieUploadRequestModel.setBtechId(appPreferenceManager.getLoginResponseModel().getUserID());
                selfieUploadRequestModel.setPic(""+encodedProImg);
                ApiCallAsyncTask selfieUploadApiAsyncTask = asyncTaskForRequest.getSelfieUploadRequestAsyncTask(selfieUploadRequestModel);
                selfieUploadApiAsyncTask.setApiCallAsyncTaskDelegate(new SelfieApiAsyncTaskDelegateResult());
                if (isNetworkAvailable(activity)) {
                    selfieUploadApiAsyncTask.execute(selfieUploadApiAsyncTask);
                } else {
                    Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(activity, R.string.add_selfie_error, Toast.LENGTH_SHORT).show();
            }
        }
        if (v.getId() == R.id.img_user_picture) {
            showImage(thumbnail);
        }
    }

    private boolean validate() {
        return !encodedProImg.isEmpty();
    }

    private void clickPhoto() {
        final CharSequence[] items = {"Take Photo",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(SelfieUploadActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = DeviceUtils.checkPermission(SelfieUploadActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            boolean isFileCreated = destination.createNewFile();
            if(isFileCreated) {
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());

                fo.close();
            }
            else{
                Toast.makeText(activity,"Failed to create file",Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        encodedProImg = CommonUtils.encodeImage(thumbnail);
        img_user_picture.setImageBitmap(thumbnail);
    }

    private void showImage(Bitmap bm) {

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_full_image_display);
        TouchImageView imgFullDisplay = (TouchImageView) dialog.findViewById(R.id.img_selfie_full);
        Button btnClose = (Button) dialog.findViewById(R.id.btn_selfie_close);
        imgFullDisplay.setImageBitmap(bm);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private class SelfieApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200 || statusCode==201) {
                ResponseParser responseParser = new ResponseParser(activity);
                SelfieUploadResponseModel selfieUploadResponseModel = responseParser.getSelfieUploadResponseModel(json, statusCode);
                if (selfieUploadResponseModel != null) {
                    Calendar c = Calendar.getInstance();
                    selfieUploadResponseModel.setTimeUploaded(c.getTimeInMillis());
                    appPreferenceManager.setSelfieResponseModel(selfieUploadResponseModel);
                    callMasterSync();
                }
            }
            else{
                Toast.makeText(activity,""+json,Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if (serviceClass.getName().equals(service.service.getClassName())){
                return true;
            }
        }
        return false;
    }

    public boolean isMasterTableSyncServiceIsInProgress() {
        return isServiceRunning(MasterTablesSyncService.class);
    }

    private void callMasterTableSyncService() {
        Intent intent = new Intent(activity, MasterTablesSyncService.class);
        startService(intent);
        acquirewakeLock();
    }

    public void stopMasterTableSyncService() {
        stopService(new Intent(activity, MasterTablesSyncService.class ));
        releaseWakeLock();
    }


    public void callMasterSync() {
        if (NetworkUtils.isNetworkAvailable(activity)){
            if (isMasterTableSyncServiceIsInProgress()){
                Toast.makeText(activity, getString(R.string.progress_message_fetching_test_master_please_wait), Toast.LENGTH_LONG).show();
            } else {
                callMasterTableSyncService();
            }
        } else {
            Toast.makeText(activity, getString(R.string.logout_message_offline), Toast.LENGTH_LONG).show();

            if (appPreferenceManager != null){
                appPreferenceManager.clearAllPreferences();
            }

            if (dhbDao == null){
                dhbDao = new DhbDao(activity);
            }
            dhbDao.deleteTablesonLogout();
        }
    }

    public class SyncStatusReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent != null && intent.getAction() != null){

                if (intent.getAction().equals(AppConstants.OFFLINE_STATUS_ACTION_IN_PROGRESS)
                        || intent.getAction().equals(AppConstants.MASTER_TABLE_UPDATE_ACTION_IN_PROGRESS)){

                    int totalUploadCount = 0, uploadedCount = 0;

                    if (intent.getExtras() != null){
                        if (intent.getExtras().containsKey(AppConstants.MASTER_TABLE_UPDATE_TOTAL_COUNT)){
                            totalUploadCount = intent.getExtras().getInt(AppConstants.MASTER_TABLE_UPDATE_TOTAL_COUNT);
                        }
                        if (intent.getExtras().containsKey(AppConstants.MASTER_TABLE_UPDATE_COMPLETED_COUNT)){
                            uploadedCount = intent.getExtras().getInt(AppConstants.MASTER_TABLE_UPDATE_COMPLETED_COUNT);
                        }
                    }

                    initSyncProgressBarDialog();

                    if (totalUploadCount == 0 && uploadedCount == 0){
                        setSyncProgressDialogTypeToHorizontalOrSpinner(false);
                        showSyncProgressBarDialog();
                    } else {
                        setSyncProgressDialogTypeToHorizontalOrSpinner(true);

                        syncBarProgressDialog.setMax(totalUploadCount);

                        if (uploadedCount <= totalUploadCount){
                            syncBarProgressDialog.setProgress(uploadedCount);
                            showSyncProgressBarDialog();
                        } else {

                            syncBarProgressDialog.setProgress(totalUploadCount);
                            showSyncProgressBarDialog();

//						if(ApplicationController.isOfflineSyncServiceIsInProgress(activity)){
//							stopOfflineSyncService(activity);
//						}
                        }
                    }

                } else if (intent.getAction().equals(AppConstants.MASTER_TABLE_UPDATE_ACTION_DONE)){

                    if (intent.getExtras() != null){
                        boolean isIssueFoundInSync = false;
                        if (intent.getExtras().containsKey(AppConstants.MASTER_TABLE_UPDATE_ISSUE_FOUND)){
                            isIssueFoundInSync = intent.getExtras().getBoolean(AppConstants.MASTER_TABLE_UPDATE_ISSUE_FOUND);
                        }

                        if (isMasterTableSyncServiceIsInProgress()){
                            stopMasterTableSyncService();
                        }

                        if (isIssueFoundInSync){
                            Toast.makeText(activity, getResources().getString(R.string.sync_master_error), Toast.LENGTH_SHORT).show();

                            if (appPreferenceManager != null){
                                appPreferenceManager.clearAllPreferences();
                            }

                            //appPreferenceManager.setIsAfterLogin(false);
                            if (dhbDao == null){
                                dhbDao = new DhbDao(activity);
                            }
                            dhbDao.deleteTablesonLogout();

                            Toast.makeText(activity, getResources().getString(R.string.sync_master_error), Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(activity, getResources().getString(R.string.sync_done_master_table), Toast.LENGTH_SHORT).show();

                            appPreferenceManager.setIsAfterLogin(true);
                            isAfterMasterSyncDone = true;
                            switchToActivity(activity, HomeScreenActivity.class, new Bundle());
                        }

                        hideSyncProgressBarDialog();
                        syncBarProgressDialog = null;
                    }

                } else if (intent.getAction().equals(AppConstants.OFFLINE_STATUS_ACTION_NO_DATA)
                        || intent.getAction().equals(AppConstants.MASTER_TABLE_UPDATE_ACTION_NO_DATA)){

                    Toast.makeText(activity, getResources().getString(R.string.sync_no_data), Toast.LENGTH_SHORT).show();

                }
            }
        }

    }

    public void acquirewakeLock() {

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Login Wakelock");
        wakeLock.acquire();
    }

    public void releaseWakeLock() {
        wakeLock.release();
    }

    public void showSyncProgressBarDialog() {
        if (!syncBarProgressDialog.isShowing()){
            syncBarProgressDialog.show();
        }
    }

    public void hideSyncProgressBarDialog() {
        if (syncBarProgressDialog != null && syncBarProgressDialog.isShowing()){
            syncBarProgressDialog.dismiss();
        }
    }

    public void initSyncProgressBarDialog() {
        if (syncBarProgressDialog == null){
            syncBarProgressDialog = new ProgressDialog(activity);
            syncBarProgressDialog.setTitle("Please wait");
            syncBarProgressDialog.setMessage("Updating Test Menu\n\nThis may take few minutes for first time");
            syncBarProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            syncBarProgressDialog.setProgress(0);
//			syncBarProgressDialog.setMax(100);
            syncBarProgressDialog.setCancelable(false);
        }
    }

    public void setSyncProgressDialogTypeToHorizontalOrSpinner(boolean isHorizontalMode) {
        if (isHorizontalMode){
            syncBarProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            syncBarProgressDialog.setMessage("Updating Test Menu \n\nThis may take few minutes for first time");
        } else {
            syncBarProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }

    }

}
