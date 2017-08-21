package com.thyrocare.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.thyrocare.R;
import com.thyrocare.customview.TouchImageView;
import com.thyrocare.dao.DhbDao;
import com.thyrocare.models.api.request.SelfieUploadRequestModel;
import com.thyrocare.models.api.response.SelfieUploadResponseModel;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.network.ResponseParser;
import com.thyrocare.service.MasterTablesSyncService;
import com.thyrocare.uiutils.AbstractActivity;
import com.thyrocare.utils.api.NetworkUtils;
import com.thyrocare.utils.app.AppConstants;
import com.thyrocare.utils.app.AppPreferenceManager;
import com.thyrocare.utils.app.CommonUtils;
import com.thyrocare.utils.app.InputUtils;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;


public class SelfieUploadActivity extends AbstractActivity implements View.OnClickListener {
    private static final String TAG = SelfieUploadActivity.class.getSimpleName();
    TextView tv_username, tv_user_address;

    //changes_1june2017
    //RoundedImageView img_user_picture;
    CircularImageView img_user_picture;
    //changes_1june2017

    Button btn_takePhoto, btn_uploadPhoto;
    String userChoosenTask, encodedProImg;
    Bitmap thumbnail, thumbnailToDisplay;// = null;
    private static final int REQUEST_CAMERA = 100;
    Activity activity;
    AppPreferenceManager appPreferenceManager;
    private DhbDao dhbDao;
    private PowerManager.WakeLock wakeLock;
    private ProgressDialog syncBarProgressDialog;
    private boolean isAfterMasterSyncDone;
    SyncStatusReceiver syncStatusReceiver;
    private int leaveFlag = 0;
    private String fromdateapi, todateapi;
    Uri outPutfileUri;
    private Button Logout;

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
        if (syncStatusReceiver != null) {
            unregisterReceiver(syncStatusReceiver);
            syncStatusReceiver = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (syncStatusReceiver != null) {
            unregisterReceiver(syncStatusReceiver);
            syncStatusReceiver = null;
        }
        if (dhbDao != null) {
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

  Logout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          ApiCallAsyncTask logoutAsyncTask = new AsyncTaskForRequest(activity).getLogoutRequestAsyncTask();
          logoutAsyncTask.setApiCallAsyncTaskDelegate(new LogoutAsyncTaskDelegateResult());
          if (isNetworkAvailable(activity)) {
              logoutAsyncTask.execute(logoutAsyncTask);
          } else {
              Toast.makeText(activity, "Logout functionality is only available in Online Mode", Toast.LENGTH_SHORT).show();
          }
      }
  });
    }

    private class LogoutAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                appPreferenceManager.clearAllPreferences();
                dhbDao.deleteTablesonLogout();
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory(Intent.CATEGORY_HOME);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                finish();
            } else {
                Toast.makeText(activity, "Failed to Logout", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {

        }
    }

    public void initUI() {
        super.initUI();
        Logout=(Button) findViewById(R.id.btn_Logout);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_user_address = (TextView) findViewById(R.id.tv_user_address);
        //changes_1june2017
        img_user_picture = (CircularImageView) findViewById(R.id.img_user_picture);
        //img_user_picture = (RoundedImageView) findViewById(R.id.img_user_picture);
        //changes_1june2017
        btn_takePhoto = (Button) findViewById(R.id.btn_takePhoto);
        btn_uploadPhoto = (Button) findViewById(R.id.btn_uploadPhoto);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_takePhoto) {
            cameraIntent();
        }
        if (v.getId() == R.id.btn_uploadPhoto) {
            if (validate()) {
                AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
                SelfieUploadRequestModel selfieUploadRequestModel = new SelfieUploadRequestModel();
                Log.e(TAG, "onClick: btechId : " + appPreferenceManager.getLoginResponseModel().getUserID());
                Log.e(TAG, "onClick: encodedProImg " + encodedProImg);
                selfieUploadRequestModel.setBtechId(appPreferenceManager.getLoginResponseModel().getUserID());
                selfieUploadRequestModel.setPic("" + encodedProImg);
                ApiCallAsyncTask selfieUploadApiAsyncTask = asyncTaskForRequest.getSelfieUploadRequestAsyncTask(selfieUploadRequestModel);
                selfieUploadApiAsyncTask.setApiCallAsyncTaskDelegate(new SelfieApiAsyncTaskDelegateResult());
                if (isNetworkAvailable(activity)) {
                    selfieUploadApiAsyncTask.execute(selfieUploadApiAsyncTask);
                } else {
                    Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
                }
            } else {
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

    private void cameraIntent() {

        //changes_1june2017
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);

        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory(), "MyPhoto.jpg");
        outPutfileUri = FileProvider.getUriForFile(SelfieUploadActivity.this, SelfieUploadActivity.this.getApplicationContext().getPackageName() + ".provider", file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri);
        startActivityForResult(intent, REQUEST_CAMERA);*/
        //changes_1june2017

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void onCaptureImageResult(Intent data) {


        /*try {
            //This image is for upload purpose...
            thumbnail = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outPutfileUri);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 10, bytes);
            encodedProImg = CommonUtils.encodeImage(thumbnail);

            String uri = outPutfileUri.toString();
            Log.e("uri-:", uri);
            //Toast.makeText(this, outPutfileUri.toString(), Toast.LENGTH_LONG).show();

            if (!InputUtils.isNull(encodedProImg)) {
                //Toast.makeText(activity, "if", Toast.LENGTH_SHORT).show();

                btn_uploadPhoto.setVisibility(View.VISIBLE);
                btn_takePhoto.setVisibility(View.INVISIBLE);
            } else {
                //Toast.makeText(activity, "else", Toast.LENGTH_SHORT).show();

                btn_uploadPhoto.setVisibility(View.INVISIBLE);
                btn_takePhoto.setVisibility(View.VISIBLE);
            }

            //This image is for display purpose...
            thumbnailToDisplay = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outPutfileUri);
            ByteArrayOutputStream bytesToDisplay = new ByteArrayOutputStream();
            thumbnailToDisplay.compress(Bitmap.CompressFormat.JPEG, 90, bytesToDisplay);
            Drawable img = new BitmapDrawable(getResources(), thumbnailToDisplay);
            img_user_picture.setImageDrawable(img);

        } catch (IOException e) {
            e.printStackTrace();
        }*/

        /*************************************************************************************/

        thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        encodedProImg = CommonUtils.encodeImage(thumbnail);
        if (!InputUtils.isNull(encodedProImg)) {
            //Toast.makeText(activity, "if", Toast.LENGTH_SHORT).show();

            btn_uploadPhoto.setVisibility(View.VISIBLE);
            btn_takePhoto.setVisibility(View.INVISIBLE);
        } else {
            //Toast.makeText(activity, "else", Toast.LENGTH_SHORT).show();

            btn_uploadPhoto.setVisibility(View.INVISIBLE);
            btn_takePhoto.setVisibility(View.VISIBLE);
        }
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
            if (statusCode == 200 || statusCode == 201) {
                ResponseParser responseParser = new ResponseParser(activity);
                SelfieUploadResponseModel selfieUploadResponseModel = responseParser.getSelfieUploadResponseModel(json, statusCode);
                if (selfieUploadResponseModel != null) {
                    Calendar c = Calendar.getInstance();
                    selfieUploadResponseModel.setTimeUploaded(c.getTimeInMillis());
                    selfieUploadResponseModel.setBtechId(appPreferenceManager.getLoginResponseModel().getUserID());
                    selfieUploadResponseModel.setPic(encodedProImg);
                    appPreferenceManager.setSelfieResponseModel(selfieUploadResponseModel);
//                    Toast.makeText(getApplicationContext(),+selfieUploadResponseModel.getFlag()+"",Toast.LENGTH_SHORT).show();
                    leaveFlag = selfieUploadResponseModel.getFlag();
                    fromdateapi = selfieUploadResponseModel.getFromDate();
                    todateapi = selfieUploadResponseModel.getToDate();
                    callMasterSync();
                }
            } else {
                if(IS_DEBUG)
                    Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
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
        stopService(new Intent(activity, MasterTablesSyncService.class));
        releaseWakeLock();
    }


    public void callMasterSync() {
        if (NetworkUtils.isNetworkAvailable(activity)) {
            if (isMasterTableSyncServiceIsInProgress()) {
                Toast.makeText(activity, getString(R.string.progress_message_fetching_test_master_please_wait), Toast.LENGTH_LONG).show();
            } else {
                callMasterTableSyncService();
            }
        } else {
            Toast.makeText(activity, getString(R.string.logout_message_offline), Toast.LENGTH_LONG).show();

            if (appPreferenceManager != null) {
                appPreferenceManager.clearAllPreferences();
            }

            if (dhbDao == null) {
                dhbDao = new DhbDao(activity);
            }
            dhbDao.deleteTablesonLogout();
        }
    }

    public class SyncStatusReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent != null && intent.getAction() != null) {

                if (intent.getAction().equals(AppConstants.OFFLINE_STATUS_ACTION_IN_PROGRESS)
                        || intent.getAction().equals(AppConstants.MASTER_TABLE_UPDATE_ACTION_IN_PROGRESS)) {

                    int totalUploadCount = 0, uploadedCount = 0;

                    if (intent.getExtras() != null) {
                        if (intent.getExtras().containsKey(AppConstants.MASTER_TABLE_UPDATE_TOTAL_COUNT)) {
                            totalUploadCount = intent.getExtras().getInt(AppConstants.MASTER_TABLE_UPDATE_TOTAL_COUNT);
                        }
                        if (intent.getExtras().containsKey(AppConstants.MASTER_TABLE_UPDATE_COMPLETED_COUNT)) {
                            uploadedCount = intent.getExtras().getInt(AppConstants.MASTER_TABLE_UPDATE_COMPLETED_COUNT);
                        }
                    }

                    initSyncProgressBarDialog();

                    if (totalUploadCount == 0 && uploadedCount == 0) {
                        setSyncProgressDialogTypeToHorizontalOrSpinner(false);
                        showSyncProgressBarDialog();
                    } else {
                        setSyncProgressDialogTypeToHorizontalOrSpinner(true);

                        syncBarProgressDialog.setMax(totalUploadCount);

                        if (uploadedCount <= totalUploadCount) {
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

                } else if (intent.getAction().equals(AppConstants.MASTER_TABLE_UPDATE_ACTION_DONE)) {

                    if (intent.getExtras() != null) {
                        boolean isIssueFoundInSync = false;
                        if (intent.getExtras().containsKey(AppConstants.MASTER_TABLE_UPDATE_ISSUE_FOUND)) {
                            isIssueFoundInSync = intent.getExtras().getBoolean(AppConstants.MASTER_TABLE_UPDATE_ISSUE_FOUND);
                        }

                        if (isMasterTableSyncServiceIsInProgress()) {
                            stopMasterTableSyncService();
                        }

                        if (isIssueFoundInSync) {
                            Toast.makeText(activity, getResources().getString(R.string.sync_master_error), Toast.LENGTH_SHORT).show();

                            if (appPreferenceManager != null) {
                                appPreferenceManager.clearAllPreferences();
                            }

                            //appPreferenceManager.setIsAfterLogin(false);
                            if (dhbDao == null) {
                                dhbDao = new DhbDao(activity);
                            }
                            dhbDao.deleteTablesonLogout();

                            Toast.makeText(activity, getResources().getString(R.string.sync_master_error), Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(activity, getResources().getString(R.string.sync_done_master_table), Toast.LENGTH_SHORT).show();

                            appPreferenceManager.setIsAfterLogin(true);
                            isAfterMasterSyncDone = true;
                            appPreferenceManager.setLeaveFlag(leaveFlag);
                            appPreferenceManager.setLeaveFromDate(fromdateapi);
                            appPreferenceManager.setLeaveToDate(todateapi);
                            Intent i = new Intent(getApplicationContext(),HomeScreenActivity.class);
                            i.putExtra("LEAVEINTIMATION", "0");
                            startActivity(i);
                        }

                        hideSyncProgressBarDialog();
                        syncBarProgressDialog = null;
                    }

                } else if (intent.getAction().equals(AppConstants.OFFLINE_STATUS_ACTION_NO_DATA)
                        || intent.getAction().equals(AppConstants.MASTER_TABLE_UPDATE_ACTION_NO_DATA)) {

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
        if (!syncBarProgressDialog.isShowing()) {
            syncBarProgressDialog.show();
        }
    }

    public void hideSyncProgressBarDialog() {
        if (syncBarProgressDialog != null && syncBarProgressDialog.isShowing()) {
            syncBarProgressDialog.dismiss();
        }
    }

    public void initSyncProgressBarDialog() {
        if (syncBarProgressDialog == null) {
            syncBarProgressDialog = new ProgressDialog(activity);
            syncBarProgressDialog.setTitle("Please wait");
            syncBarProgressDialog.setMessage(getResources().getString(R.string.progress_message_fetching_brand_master_please_wait));
            syncBarProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            syncBarProgressDialog.setProgress(0);
//			syncBarProgressDialog.setMax(100);
            syncBarProgressDialog.setCancelable(false);
        }
    }

    public void setSyncProgressDialogTypeToHorizontalOrSpinner(boolean isHorizontalMode) {
        if (isHorizontalMode) {
            syncBarProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            syncBarProgressDialog.setMessage(getResources().getString(R.string.progress_message_fetching_test_master_please_wait));
        } else {
            syncBarProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }

    }

}