package com.thyrocare.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/*import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.VerifyResult;

import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.contract.VerifyResult;

*/


import com.mikhaellopez.circularimageview.CircularImageView;
import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.Controller.DeviceLogOutController;
import com.thyrocare.R;
import com.thyrocare.application.ApplicationController;
import com.thyrocare.customview.TouchImageView;
import com.thyrocare.dao.DhbDao;
import com.thyrocare.models.api.request.SelfieUploadRequestModel;
import com.thyrocare.models.api.response.BtechImageResponseModel;
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
import com.thyrocare.utils.app.BundleConstants;
import com.thyrocare.utils.app.CommonUtils;
import com.thyrocare.utils.app.InputUtils;

import org.json.JSONException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

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
    private int faceDetected = 0;

    // The IDs of the two faces to be verified.
    private UUID mFaceId0;
    private UUID mFaceId1;
    ProgressDialog progressDialog;

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

        if (BundleConstants.Flag_facedetection) {
            CallApiOpenImage(appPreferenceManager.getLoginResponseModel().getUserID());
        } else {

        }

    }

    private void faceCount(Bitmap bitmap) {
       /* InputStream stream = getResources().openRawResource(R.raw.image01);
        Bitmap bitmap = BitmapFactory.decodeStream(stream);*/

        try {
            com.google.android.gms.vision.face.FaceDetector detector = new com.google.android.gms.vision.face.FaceDetector.Builder(getApplicationContext())
                    .setTrackingEnabled(false)
                    .build();

            // Create a frame from the bitmap and run face detection on the frame.
            com.google.android.gms.vision.Frame frame = new com.google.android.gms.vision.Frame.Builder().setBitmap(bitmap).build();
            SparseArray<com.google.android.gms.vision.face.Face> faces = detector.detect(frame);

       /* TextView faceCountView = (TextView) findViewById(R.id.face_count);
        faceCountView.setText(faces.size() + " faces detected");*/
            //Toast.makeText(activity, "faces detected "+faces.size(), Toast.LENGTH_SHORT).show();
            faceDetected = faces.size();
//            Toast.makeText(activity, ""+faces.size(), Toast.LENGTH_SHORT).show();
            detector.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.READ_PHONE_STATE}, AppConstants.APP_PERMISSIONS);
                } else {
                    ApiCallAsyncTask logoutAsyncTask = new AsyncTaskForRequest(activity).getLogoutRequestAsyncTask();
                    logoutAsyncTask.setApiCallAsyncTaskDelegate(new LogoutAsyncTaskDelegateResult());
                    if (isNetworkAvailable(activity)) {
                        logoutAsyncTask.execute(logoutAsyncTask);
                        if (appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.LME_ROLE_ID)) {

                        } else {
                            CallLogOutDevice();
                        }
                    } else {
                        TastyToast.makeText(activity, "Logout functionality is only available in Online Mode", TastyToast.LENGTH_LONG, TastyToast.INFO);
                        // Toast.makeText(activity, "Logout functionality is only available in Online Mode", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void CallLogOutDevice() {
        try {
            if (!InputUtils.isNull(appPreferenceManager.getLoginResponseModel().getUserID())) {
                String device_id = "";
                try {
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    device_id = telephonyManager.getDeviceId();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (ApplicationController.mDeviceLogOutController != null) {
                    ApplicationController.mDeviceLogOutController = null;
                }

                ApplicationController.mDeviceLogOutController = new DeviceLogOutController(activity);
                ApplicationController.mDeviceLogOutController.CallLogOutDevice(appPreferenceManager.getLoginResponseModel().getUserID(), device_id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class LogoutAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                try {
                    appPreferenceManager.clearAllPreferences();
                    dhbDao.deleteTablesonLogout();

                    Intent n = new Intent(activity, LoginScreenActivity.class);
                    n.setAction(Intent.ACTION_MAIN);
                    n.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(n);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (statusCode == 401) {
                CommonUtils.CallLogOutFromDevice(activity, activity, appPreferenceManager, dhbDao);
            } else {
                TastyToast.makeText(activity, "Failed to Logout ", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                // Toast.makeText(activity, "Failed to Logout", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {

        }
    }

    public void initUI() {
        super.initUI();
        Logout = (Button) findViewById(R.id.btn_Logout);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_user_address = (TextView) findViewById(R.id.tv_user_address);
        img_user_picture = (CircularImageView) findViewById(R.id.img_user_picture);
        //img_user_picture = (RoundedImageView) findViewById(R.id.img_user_picture);
        btn_takePhoto = (Button) findViewById(R.id.btn_takePhoto);
        btn_uploadPhoto = (Button) findViewById(R.id.btn_uploadPhoto);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_takePhoto) {
            faceDetected = 0;
            cameraIntent();
        }
        if (v.getId() == R.id.btn_uploadPhoto) {
            if (validate()) {
                if (BundleConstants.Flag_facedetection) {
                    if (mFaceId0 == null) {
                        Toast.makeText(getApplicationContext(), "Please take photo", Toast.LENGTH_SHORT).show();
                    } else if (mFaceId1 == null) {
                        Toast.makeText(getApplicationContext(), "Please Upload photo", Toast.LENGTH_SHORT).show();
                    } else {
                        new VerificationTask(mFaceId0, mFaceId1).execute();
                    }
                } else {
                    CallPostImageData();
                }
            } /*else {
                TastyToast.makeText(activity,getString(R.string.add_selfie_error), TastyToast.LENGTH_LONG, TastyToast.WARNING);
               // Toast.makeText(activity, R.string.add_selfie_error, Toast.LENGTH_SHORT).show();
            }*/
        }
        if (v.getId() == R.id.img_user_picture) {
            //showImage(thumbnail);
        }
    }

    private void CallPostImageData() {
        try {
            AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
            SelfieUploadRequestModel selfieUploadRequestModel = new SelfieUploadRequestModel();
            if (appPreferenceManager.getLoginResponseModel() != null) {
                if (appPreferenceManager.getLoginResponseModel().getUserID() != null) {
                    selfieUploadRequestModel.setBtechId(appPreferenceManager.getLoginResponseModel().getUserID());
                }
            }

            selfieUploadRequestModel.setPic("" + encodedProImg);

            ApiCallAsyncTask selfieUploadApiAsyncTask = asyncTaskForRequest.getSelfieUploadRequestAsyncTask(selfieUploadRequestModel);
            selfieUploadApiAsyncTask.setApiCallAsyncTaskDelegate(new SelfieApiAsyncTaskDelegateResult());
            if (isNetworkAvailable(activity)) {
                selfieUploadApiAsyncTask.execute(selfieUploadApiAsyncTask);
            } else {
                TastyToast.makeText(activity, "Check Internet Connection", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                // Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validate() {
        if (encodedProImg.isEmpty()) {
            TastyToast.makeText(activity, getString(R.string.add_selfie_error), TastyToast.LENGTH_LONG, TastyToast.WARNING);
            return false;
        } else if (faceDetected == 0) {
            TastyToast.makeText(activity, getString(R.string.no_face_detected), TastyToast.LENGTH_LONG, TastyToast.WARNING);
            btn_takePhoto.setVisibility(View.VISIBLE);
            btn_uploadPhoto.setVisibility(View.GONE);
            return false;
        }

        return true;
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
                //FaceDetector faceDetector=new FaceDetector(70,70,1);

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

        if (BundleConstants.Flag_facedetection) {
            detect(thumbnail, 1);
        } else {
            faceCount(thumbnail);
        }
        //===========================
        /*FaceDetector faceDetector=new FaceDetector(thumbnail.getWidth(),thumbnail.getHeight(),1);
        FaceDetector.Face[] face = new FaceDetector.Face[1];
        try{
            int f=faceDetector.findFaces(thumbnail,face);
            Logger.error("faces "+f);
        }catch (Exception e){
            e.printStackTrace();
            Logger.error("error123 "+e);
        }*/
        //=================================
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        encodedProImg = CommonUtils.encodeImage(thumbnail);

        if (!InputUtils.isNull(encodedProImg)) {
            //Toast.makeText(activity, "if", Toast.LENGTH_SHORT).show();

            if (BundleConstants.Flag_facedetection) {

            } else {
                if (faceDetected == 0) {
                    TastyToast.makeText(activity, getString(R.string.no_face_detected), TastyToast.LENGTH_LONG, TastyToast.WARNING);
                    btn_takePhoto.setVisibility(View.VISIBLE);
                    btn_uploadPhoto.setVisibility(View.GONE);
                }else if (faceDetected > 1) {
                    TastyToast.makeText(activity, "Image contain more than 1 faces", TastyToast.LENGTH_LONG, TastyToast.WARNING);
                    btn_takePhoto.setVisibility(View.VISIBLE);
                    btn_uploadPhoto.setVisibility(View.GONE);
                } else {
                    btn_uploadPhoto.setVisibility(View.VISIBLE);
                    btn_takePhoto.setVisibility(View.INVISIBLE);
                }
            }
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
            } else if (statusCode == 401) {
                CommonUtils.CallLogOutFromDevice(activity, activity, appPreferenceManager, dhbDao);
            } else {
                if (IS_DEBUG)
                    TastyToast.makeText(activity, "" + json, TastyToast.LENGTH_LONG, TastyToast.INFO);
                //  Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            TastyToast.makeText(activity, getString(R.string.network_error), TastyToast.LENGTH_LONG, TastyToast.ERROR);
            // Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show();
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
                TastyToast.makeText(activity, getString(R.string.progress_message_fetching_test_master_please_wait), TastyToast.LENGTH_LONG, TastyToast.DEFAULT);
                // Toast.makeText(activity, getString(R.string.progress_message_fetching_test_master_please_wait), Toast.LENGTH_LONG).show();
            } else {
                callMasterTableSyncService();
            }
        } else {
            TastyToast.makeText(activity, getString(R.string.logout_message_offline), TastyToast.LENGTH_LONG, TastyToast.INFO);
            //Toast.makeText(activity, getString(R.string.logout_message_offline), Toast.LENGTH_LONG).show();

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
                            TastyToast.makeText(activity, getString(R.string.sync_master_error), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                            // Toast.makeText(activity, getResources().getString(R.string.sync_master_error), Toast.LENGTH_SHORT).show();

                            if (appPreferenceManager != null) {
                                appPreferenceManager.clearAllPreferences();
                            }

                            //appPreferenceManager.setIsAfterLogin(false);
                            if (dhbDao == null) {
                                dhbDao = new DhbDao(activity);
                            }
                            dhbDao.deleteTablesonLogout();
                            TastyToast.makeText(activity, getString(R.string.sync_master_error), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                            //  Toast.makeText(activity, getResources().getString(R.string.sync_master_error), Toast.LENGTH_SHORT).show();

                        } else {
                            TastyToast.makeText(activity, getString(R.string.sync_done_master_table), TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                            //Toast.makeText(activity, getResources().getString(R.string.sync_done_master_table), Toast.LENGTH_SHORT).show();

                            appPreferenceManager.setIsAfterLogin(true);
                            isAfterMasterSyncDone = true;
                            appPreferenceManager.setLeaveFlag(leaveFlag);
                            appPreferenceManager.setLeaveFromDate(fromdateapi);
                            appPreferenceManager.setLeaveToDate(todateapi);
                            Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
                            i.putExtra("LEAVEINTIMATION", "0");
                            startActivity(i);
                        }

                        hideSyncProgressBarDialog();
                        syncBarProgressDialog = null;
                    }

                } else if (intent.getAction().equals(AppConstants.OFFLINE_STATUS_ACTION_NO_DATA)
                        || intent.getAction().equals(AppConstants.MASTER_TABLE_UPDATE_ACTION_NO_DATA)) {
                    TastyToast.makeText(activity, getString(R.string.sync_no_data), TastyToast.LENGTH_LONG, TastyToast.INFO);
                    // Toast.makeText(activity, getResources().getString(R.string.sync_no_data), Toast.LENGTH_SHORT).show();

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

    private void CallApiOpenImage(String BTechId) {
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchSlotsApiAsyncTask = asyncTaskForRequest.getBtechFaceImageRequestAsyncTask(BTechId);
        fetchSlotsApiAsyncTask.setApiCallAsyncTaskDelegate(new fetchBtechImageAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            fetchSlotsApiAsyncTask.execute(fetchSlotsApiAsyncTask);
        } else {
            TastyToast.makeText(activity, "Check Internet Connection..", TastyToast.LENGTH_LONG, TastyToast.INFO);
        }
    }

    private class fetchBtechImageAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                ResponseParser responseParser = new ResponseParser(activity);
                BtechImageResponseModel availableSlotsResponseModel = responseParser.getBTECHIMAGEModel(json, statusCode);
                if (availableSlotsResponseModel != null) {
                    GetResponseBtechImage(availableSlotsResponseModel);
                } else {
                    BundleConstants.Flag_facedetection = false;
                    TastyToast.makeText(activity, "" + json, TastyToast.LENGTH_LONG, TastyToast.INFO);
                }

            } else {
                BundleConstants.Flag_facedetection = false;
                TastyToast.makeText(activity, "" + json, TastyToast.LENGTH_LONG, TastyToast.INFO);
            }
        }

        @Override
        public void onApiCancelled() {

        }
    }

    private void GetResponseBtechImage(final BtechImageResponseModel availableSlotsResponseModel) {
        if (availableSlotsResponseModel.getFlag() == 1) {
            if (availableSlotsResponseModel.getImgUrl() != null) {
                if (!availableSlotsResponseModel.getImgUrl().equals("")) {
                    try {
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    //Your code goes here
                                    Bitmap image = getBitmapFromURL("" + availableSlotsResponseModel.getImgUrl());
                                    detect(image, 0);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        thread.start();

                    } catch (Exception e) {
                        System.out.println(e);
                    }
                } else {
                    BundleConstants.Flag_facedetection = false;
                    TastyToast.makeText(activity, "Image not Available", TastyToast.LENGTH_LONG, TastyToast.INFO);
                }
            } else {
                BundleConstants.Flag_facedetection = false;
                TastyToast.makeText(activity, "Image not Available", TastyToast.LENGTH_LONG, TastyToast.INFO);
            }
        }else if(availableSlotsResponseModel.getFlag() == 0){
            BundleConstants.Flag_facedetection = false;
        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Start detecting in image specified by index.
    private void detect(Bitmap bitmap, int index) {
        // Put the image into an input stream for detection.
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());

        // Start a background task to detect faces in the image.
        new DetectionTask(index).execute(inputStream);
    }

    // Background task of face detection.
    private class DetectionTask extends AsyncTask<InputStream, String, com.microsoft.projectoxford.face.contract.Face[]> {
        // Index indicates detecting in which of the two images.
        private int mIndex;
        private boolean mSucceed = true;

        DetectionTask(int index) {
            mIndex = index;
        }

        @Override
        protected com.microsoft.projectoxford.face.contract.Face[] doInBackground(InputStream... params) {
            // Get an instance of face service client to detect faces in image.
            com.microsoft.projectoxford.face.FaceServiceClient faceServiceClient = new com.microsoft.projectoxford.face.FaceServiceRestClient(getString(R.string.endpoint), getString(R.string.subscription_key));
            try {
                publishProgress("Detecting...");

                // Start detection.
                return faceServiceClient.detect(
                        params[0],  /* Input stream of image to detect */
                        true,       /* Whether to return face ID */
                        false,       /* Whether to return face landmarks */
                        /* Which face attributes to analyze, currently we support:
                           age,gender,headPose,smile,facialHair */
                        null);
            } catch (Exception e) {
                mSucceed = false;
                publishProgress(e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            if (mIndex == 1) {
                if (progressDialog != null) {
                    progressDialog = null;
                }
                progressDialog = new ProgressDialog(activity);
                progressDialog.setTitle(getString(R.string.loading));
                progressDialog.show();
            }
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            if (mIndex == 1) {
                progressDialog.setMessage(progress[0]);
            }
        }

        @Override
        protected void onPostExecute(com.microsoft.projectoxford.face.contract.Face[] result) {
            if (mIndex == 1) {
                try {
                    if (progressDialog != null) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Show the result on screen when detection is done.
            setUiAfterDetection(result, mIndex, mSucceed);
        }
    }

    // Show the result on screen when detection in image that indicated by index is done.
    private void setUiAfterDetection(com.microsoft.projectoxford.face.contract.Face[] result, int index, boolean succeed) {
        List<com.microsoft.projectoxford.face.contract.Face> faces;
        faces = new ArrayList<>();
        if (result != null) {
            faces = Arrays.asList(result);
            if (faces.size() != 0) {
                try {
                    if (faces.size() > 1) {
                       /* if (index == 1) {
                            img_gal.setImageDrawable(null);
                        } else {
                            img_cam.setImageDrawable(null);
                        }*/

                        if (index == 1) {
                            btn_takePhoto.setVisibility(View.VISIBLE);
                            btn_uploadPhoto.setVisibility(View.GONE);
                        } else {
                            BundleConstants.Flag_facedetection = false;
                        }

                        Toast.makeText(getApplicationContext(), "Image contain more than 1 faces.", Toast.LENGTH_LONG).show();
                    } else {
                        if (index == 1) {
                            faceDetected = faces.size();
                            mFaceId1 = faces.get(0).faceId;
                            btn_uploadPhoto.setVisibility(View.VISIBLE);
                            btn_takePhoto.setVisibility(View.INVISIBLE);
                        } else {
                            mFaceId0 = faces.get(0).faceId;
                        }
                    }
                    System.out.println("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (index == 1) {
                    TastyToast.makeText(activity, getString(R.string.no_face_detected), TastyToast.LENGTH_LONG, TastyToast.WARNING);
                    btn_takePhoto.setVisibility(View.VISIBLE);
                    btn_uploadPhoto.setVisibility(View.GONE);
                } else {
                    BundleConstants.Flag_facedetection = false;
                }
            }
        }

        if (result != null && result.length == 0) {
            Toast.makeText(getApplicationContext(), "No face detected!", Toast.LENGTH_SHORT).show();
        }
    }

    // Background task for face verification.
    private class VerificationTask extends AsyncTask<Void, String, com.microsoft.projectoxford.face.contract.VerifyResult> {
        // The IDs of two face to verify.
        private UUID mFaceId0;
        private UUID mFaceId1;

        VerificationTask(UUID faceId0, UUID faceId1) {
            mFaceId0 = faceId0;
            mFaceId1 = faceId1;
        }

        @Override
        protected com.microsoft.projectoxford.face.contract.VerifyResult doInBackground(Void... params) {
            // Get an instance of face service client to detect faces in image.
            com.microsoft.projectoxford.face.FaceServiceClient faceServiceClient = new com.microsoft.projectoxford.face.FaceServiceRestClient(getString(R.string.endpoint), getString(R.string.subscription_key));
            try {
                publishProgress("Verifying...");

                // Start verification.
                return faceServiceClient.verify(
                        mFaceId0,      /* The first face ID to verify */
                        mFaceId1);     /* The second face ID to verify */
            } catch (Exception e) {
                publishProgress(e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            if (progressDialog != null) {
                progressDialog = null;
            }
            progressDialog = new ProgressDialog(activity);
            progressDialog.setTitle(getString(R.string.loading));
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            progressDialog.setMessage(progress[0]);
        }

        @Override
        protected void onPostExecute(com.microsoft.projectoxford.face.contract.VerifyResult result) {

            try {
                if (progressDialog != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (result != null) {
                /*addLog("Response: Success. Face " + mFaceId0 + " and face "
                        + mFaceId1 + (result.isIdentical ? " " : " don't ")
                        + "belong to the same person");*/
            }

            // Show the result on screen when verification is done.
            setUiAfterVerification(result);
        }
    }

    // Show the result on screen when verification is done.
    private void setUiAfterVerification(com.microsoft.projectoxford.face.contract.VerifyResult result) {
        // Verification is done, hide the progress dialog.
        // progressDialog.dismiss();

        // Enable all the buttons.
        // setAllButtonEnabledStatus(true);

        // Show verification result.
        if (result != null) {
            DecimalFormat formatter = new DecimalFormat("#0.00");
            String verificationResult = (result.isIdentical ? "The same person" : "Different persons")
                    + ". The confidence is " + formatter.format(result.confidence);
            int s = 0;
            if (result.isIdentical) {
                s = 1;
            } else {
                s = 2;
            }
            setInfo(verificationResult, s);
        }
    }

    private void setInfo(String verificationResult, int isIdentical) {
        if (isIdentical == 1) {
            TastyToast.makeText(activity, "" + verificationResult, TastyToast.LENGTH_LONG, TastyToast.INFO);
            CallPostImageData();
        } else if (isIdentical == 2) {
            btn_takePhoto.setVisibility(View.VISIBLE);
            btn_uploadPhoto.setVisibility(View.GONE);
            TastyToast.makeText(activity, "" + verificationResult, TastyToast.LENGTH_LONG, TastyToast.ERROR);
        }
    }


}
