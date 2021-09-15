package com.thyrocare.btechapp.activity;

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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;

import androidx.core.app.ActivityCompat;

import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.FaceDetector;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.mindorks.paracamera.Camera;
import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.btechapp.Controller.DeviceLogOutController;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.LoginActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.LogUserActivityTagging;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;

import application.ApplicationController;

import com.thyrocare.btechapp.customview.TouchImageView;
import com.thyrocare.btechapp.dao.DhbDao;
import com.thyrocare.btechapp.models.api.response.BtechImageResponseModel;
import com.thyrocare.btechapp.models.api.response.SelfieUploadResponseModel;


import com.thyrocare.btechapp.service.MasterTablesSyncService;
import com.thyrocare.btechapp.uiutils.AbstractActivity;
import com.thyrocare.btechapp.utils.api.NetworkUtils;
import com.thyrocare.btechapp.utils.app.AppConstants;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.CommonUtils;
import com.thyrocare.btechapp.utils.app.DeviceUtils;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
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

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;

import static com.thyrocare.btechapp.utils.app.BundleConstants.LOGOUT;

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
    private String sub_key = "";
    private String end_key = "";
    private File imagefile;
    private Global globalClass;
    private Camera camera;
    private EditText edt_bodyTemp;
    private Spinner spn_aarogyaApp;
    private String strAarogyaSetuApp = "";
    ImageView iv_refresh, iv_capture;


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
        globalClass = new Global(activity);
        dhbDao = new DhbDao(activity);
        appPreferenceManager = new AppPreferenceManager(activity);
        initUI();
        initData();
        setListners();

        if (BundleConstants.b_facedetection) {
            CallApiOpenImage(appPreferenceManager.getLoginResponseModel().getUserID());
        } else {

        }

    }

    private void faceCount(Bitmap bitmap) {
       /* InputStream stream = getResources().openRawResource(R.raw.image01);
        Bitmap bitmap = BitmapFactory.decodeStream(stream);*/

        try {
            FaceDetector detector = new FaceDetector.Builder(getApplicationContext())
                    .setTrackingEnabled(false)
                    .build();

            // Create a frame from the bitmap and run face detection on the frame.
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<com.google.android.gms.vision.face.Face> faces = detector.detect(frame);

       /* TextView faceCountView = (TextView) findViewById(R.id.face_count);
        faceCountView.setText(faces.size() + " faces detected");*/
            //Toast.makeText(activity, "faces detected "+faces.size(), Toast.LENGTH_SHORT).show();
            faceDetected = faces.size();
//            Toast.makeText(activity, "" + faces.size(), Toast.LENGTH_SHORT).show();
            detector.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        if (appPreferenceManager.getLoginResponseModel() != null) {
            tv_username.setText("Welcome, " + Global.toCamelCase(!InputUtils.isNull(appPreferenceManager.getLoginResponseModel().getUserName()) ? appPreferenceManager.getLoginResponseModel().getUserName() : ""));
        }


        final ArrayList<String> AarogyaSetuAppList = new ArrayList<>();
        AarogyaSetuAppList.add("-Select-");
        AarogyaSetuAppList.add("YES");
        AarogyaSetuAppList.add("NO");
        ArrayAdapter<String> spinnerCampArrayAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, AarogyaSetuAppList);
        spinnerCampArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_aarogyaApp.setAdapter(spinnerCampArrayAdapter);
        spn_aarogyaApp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    strAarogyaSetuApp = "";
                } else {
                    strAarogyaSetuApp = AarogyaSetuAppList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                    if (isNetworkAvailable(activity)) {
                        CallLogoutRequestApi();
                        if (!appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.LME_ROLE_ID)) {
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
                device_id = DeviceUtils.getDeviceId(activity);

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

    public void CallLogoutRequestApi() {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallLogoutRequestApi();
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.code() == 200) {
                    try {
                        new LogUserActivityTagging(activity, LOGOUT, "");
                        appPreferenceManager.clearAllPreferences();
                        dhbDao.deleteTablesonLogout();

                        Intent n = new Intent(activity, LoginActivity.class);
                        n.setAction(Intent.ACTION_MAIN);
                        n.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(n);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == 401) {
                    CommonUtils.CallLogOutFromDevice(activity, activity, appPreferenceManager, dhbDao);
                } else {
                    TastyToast.makeText(activity, "Failed to Logout ", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                    // Toast.makeText(activity, "Failed to Logout", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                MessageLogger.LogDebug("Errror", t.getMessage());

            }
        });
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
        edt_bodyTemp = (EditText) findViewById(R.id.edt_bodyTemp);
        spn_aarogyaApp = (Spinner) findViewById(R.id.spn_aarogyaApp);

        iv_refresh = findViewById(R.id.iv_refresh);
        iv_capture = findViewById(R.id.iv_capture);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_takePhoto) {
            faceDetected = 0;
            cameraIntent();
        }
        if (v.getId() == R.id.btn_uploadPhoto) {
            if (validate()) {
                if (BundleConstants.Flag_facedetection == 1) {
                    if (mFaceId0 == null) {
                        Toast.makeText(getApplicationContext(), "Please take photo", Toast.LENGTH_SHORT).show();
                    } else if (mFaceId1 == null) {
                        Toast.makeText(getApplicationContext(), "Please Upload photo", Toast.LENGTH_SHORT).show();
                    } else {
                        new VerificationTask(mFaceId0, mFaceId1).execute();
                    }
                } else {
                    if (imagefile != null && imagefile.isAbsolute()) {
                        if (appPreferenceManager.getLoginResponseModel() != null && !InputUtils.isNull(appPreferenceManager.getLoginResponseModel().getUserID())) {
                            CallSelfieUploadAPI(appPreferenceManager.getLoginResponseModel().getUserID(), imagefile);
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid Login", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Please take photo", Toast.LENGTH_SHORT).show();
                    }

//                    CallPostImageData();
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


    private void CallSelfieUploadAPI(String Btechid, final File imagefile) {

        // add another part within the multipart request
        RequestBody requestBtechid = RequestBody.create(MediaType.parse("multipart/form-data"), Btechid);
        RequestBody requestAPPDOWNLOAD = RequestBody.create(MediaType.parse("multipart/form-data"), StringUtils.CheckEqualIgnoreCase(strAarogyaSetuApp, "YES") ? "1" : "0");
        RequestBody requestTEMP = RequestBody.create(MediaType.parse("multipart/form-data"), edt_bodyTemp.getText().toString().trim());

        MultipartBody.Part ImageFileMultiBody = null;
        if (imagefile != null && imagefile.exists()) {

            MessageLogger.info(activity, "FileName " + imagefile.getName());
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imagefile);
            ImageFileMultiBody = MultipartBody.Part.createFormData("file", imagefile.getName(), requestFile);
        }

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<SelfieUploadResponseModel> responseCall = apiInterface.uploadSelfieToServer(ImageFileMultiBody, requestBtechid, requestAPPDOWNLOAD, requestTEMP);
        globalClass.showProgressDialog(activity, "Please wait", false);

        responseCall.enqueue(new Callback<SelfieUploadResponseModel>() {
            @Override
            public void onResponse(Call<SelfieUploadResponseModel> call, Response<SelfieUploadResponseModel> response) {
                globalClass.hideProgressDialog(activity);

                if (response.isSuccessful() && response.body() != null) {
                    SelfieUploadResponseModel selfieUploadResponseModel = response.body();
                    if (selfieUploadResponseModel != null) {
                        Calendar c = Calendar.getInstance();
                        selfieUploadResponseModel.setTimeUploaded(c.getTimeInMillis());
                        if (appPreferenceManager.getLoginResponseModel() != null && InputUtils.isNull(appPreferenceManager.getLoginResponseModel().getUserID())) {
                            selfieUploadResponseModel.setBtechId(appPreferenceManager.getLoginResponseModel().getUserID());
                        } else {
                            selfieUploadResponseModel.setBtechId("");
                        }

                        selfieUploadResponseModel.setPic(imagefile.getAbsolutePath());
                        appPreferenceManager.setSelfieResponseModel(selfieUploadResponseModel);
//                    Toast.makeText(getApplicationContext(),+selfieUploadResponseModel.getFlag()+"",Toast.LENGTH_SHORT).show();
                        leaveFlag = selfieUploadResponseModel.getFlag();
                        fromdateapi = selfieUploadResponseModel.getFromDate();
                        todateapi = selfieUploadResponseModel.getToDate();
                        callMasterSync();
                    }

                } else if (response.code() == 401) {
                    CommonUtils.CallLogOutFromDevice(activity, activity, appPreferenceManager, dhbDao);
                } else {
                    globalClass.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<SelfieUploadResponseModel> call, Throwable t) {
                globalClass.hideProgressDialog(activity);
                globalClass.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });
    }

    private boolean validate() {
        if (imagefile == null || !imagefile.exists()) {
            TastyToast.makeText(activity, getString(R.string.add_selfie_error), TastyToast.LENGTH_LONG, TastyToast.WARNING);
            return false;
        }
        /*if (StringUtils.isNull(edt_bodyTemp.getText().toString().trim())) {
            TastyToast.makeText(activity, ConstantsMessages.ENterBodyTemperature, TastyToast.LENGTH_LONG, TastyToast.WARNING);
            return false;
        }
        float temperature = StringUtils.parseFloat(edt_bodyTemp.getText().toString().trim());
        if (temperature < 80 || temperature > 110) {
            TastyToast.makeText(activity, ConstantsMessages.BodyTempLimit, TastyToast.LENGTH_LONG, TastyToast.WARNING);
            return false;
        }
        if (StringUtils.isNull(strAarogyaSetuApp)) {
            TastyToast.makeText(activity, ConstantsMessages.SelecAarogyaSetuAppIsInstalled, TastyToast.LENGTH_LONG, TastyToast.WARNING);
            return false;
        }*/

        return true;
    }

    private void selectImage() {
        camera = new Camera.Builder()
                .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(1)
                .setDirectory("BtechApp/SelfieUploads")
                .setName("img" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_JPG)
                .setCompression(60)
                .setImageHeight(720)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                .build(this);
        try {
            camera.takePicture();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void cameraIntent() {
        /*if (checkCameraFront(activity)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                intent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
                intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
                intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
            } else {
                intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
            }
            startActivityForResult(intent, REQUEST_CAMERA);
        }else{
            selectImage();
        }
        selectImage();*/

      /*  Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
            intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
            intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
        } else {
            intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        }
        startActivityForResult(intent, REQUEST_CAMERA);*/

        selectImage();
    }

    public static boolean checkCameraFront(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }

            if (requestCode == Camera.REQUEST_TAKE_PHOTO) {
                try {
                    onCaptureImageResult();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void onCaptureImageResult() {

        thumbnail = camera.getCameraBitmap();
        if (BundleConstants.Flag_facedetection == 1) {
            detect(thumbnail, 1);
        } else if (BundleConstants.Flag_facedetection == 2) {
            detect(thumbnail, 1);
        } else {
            faceCount(thumbnail);
        }

        String imageurl = "";
        imageurl = camera.getCameraBitmapPath();
        imagefile = new File(imageurl);
        if (imagefile != null && imagefile.exists()) {
            if (BundleConstants.Flag_facedetection == 1) {
            } else {
                iv_refresh.setVisibility(View.VISIBLE);
                iv_capture.setVisibility(View.VISIBLE);
                btn_uploadPhoto.setVisibility(View.VISIBLE);
                btn_takePhoto.setVisibility(View.GONE);
            }
        } else {
            iv_refresh.setVisibility(View.GONE);
            iv_capture.setVisibility(View.GONE);
            btn_uploadPhoto.setVisibility(View.GONE);
            btn_takePhoto.setVisibility(View.VISIBLE);
        }
        globalClass.DisplayDeviceImages(activity, camera.getCameraBitmapPath(), img_user_picture);
    }

    private void onCaptureImageResult(Intent data) {
        thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        if (BundleConstants.Flag_facedetection == 1) {
            detect(thumbnail, 1);
        } else if (BundleConstants.Flag_facedetection == 2) {
            detect(thumbnail, 1);
        } else {
            faceCount(thumbnail);
        }
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        encodedProImg = CommonUtils.encodeImage(thumbnail);

        if (!InputUtils.isNull(encodedProImg)) {
            if (BundleConstants.Flag_facedetection == 1) {
            } else {
                iv_refresh.setVisibility(View.VISIBLE);
                iv_capture.setVisibility(View.VISIBLE);
                btn_uploadPhoto.setVisibility(View.VISIBLE);
                btn_takePhoto.setVisibility(View.GONE);
            }
        } else {
            iv_refresh.setVisibility(View.GONE);
            iv_capture.setVisibility(View.GONE);
            btn_uploadPhoto.setVisibility(View.GONE);
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
                new LogUserActivityTagging(activity, LOGOUT, "");
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
                                new LogUserActivityTagging(activity, LOGOUT, "");
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
//                            TastyToast.makeText(activity, getString(R.string.sync_done_master_table), TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
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
            syncBarProgressDialog.setMessage(getResources().getString(R.string.progress_message_camp_count_details_please_wait));
            syncBarProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            syncBarProgressDialog.setProgress(0);
//			syncBarProgressDialog.setMax(100);
            syncBarProgressDialog.setCancelable(false);
        }
    }

    public void setSyncProgressDialogTypeToHorizontalOrSpinner(boolean isHorizontalMode) {
        if (isHorizontalMode) {
            syncBarProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            syncBarProgressDialog.setMessage(getResources().getString(R.string.progress_message_camp_count_details_please_wait));
        } else {
            syncBarProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }

    }

    private void CallApiOpenImage(String BTechId) {
        if (isNetworkAvailable(activity)) {
            CallgetBtechFaceImageRequestApi(BTechId);
        } else {
            TastyToast.makeText(activity, "Check Internet Connection..", TastyToast.LENGTH_LONG, TastyToast.INFO);
        }
    }

    private void CallgetBtechFaceImageRequestApi(String BTechId) {
        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<BtechImageResponseModel> responseCall = apiInterface.CallgetBtechFaceImageRequestApi(BTechId);
        globalClass.showProgressDialog(activity, "Please wait..");
        responseCall.enqueue(new Callback<BtechImageResponseModel>() {
            @Override
            public void onResponse(Call<BtechImageResponseModel> call, retrofit2.Response<BtechImageResponseModel> response) {
                globalClass.hideProgressDialog(activity);
                if (response.isSuccessful() && response.body() != null) {
                    BtechImageResponseModel availableSlotsResponseModel = response.body();
                    if (availableSlotsResponseModel != null) {
                        BundleConstants.Flag_facedetection = availableSlotsResponseModel.getFlag();
                        sub_key = "" + availableSlotsResponseModel.getSubscriptionKey();
                        end_key = "" + availableSlotsResponseModel.getEndpointKey();
                        GetResponseBtechImage(availableSlotsResponseModel);
                    } else {
                        BundleConstants.Flag_facedetection = 0;
                        sub_key = "";
                        end_key = "";
                        globalClass.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
                    }
                } else {
                    BundleConstants.Flag_facedetection = 0;
                    sub_key = "";
                    end_key = "";
                    globalClass.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<BtechImageResponseModel> call, Throwable t) {
                globalClass.hideProgressDialog(activity);
                globalClass.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });
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

                                    if (image != null) {

                                    } else {
                                        String string = "" + availableSlotsResponseModel.getImgUrl();
                                        String newurl = string.replace("https", "http");
                                        image = getBitmapFromURL("" + newurl);
                                    }

                                    if (image == null) {
                                        BundleConstants.Flag_facedetection = 0;
                                        sub_key = "";
                                        end_key = "";
                                    }

                                    detect(image, 0);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        thread.start();

                    } catch (Exception e) {
                        MessageLogger.PrintMsg(e.getLocalizedMessage());
                    }
                } else {
                    BundleConstants.Flag_facedetection = 0;
                    TastyToast.makeText(activity, "Image not Available", TastyToast.LENGTH_LONG, TastyToast.INFO);
                }
            } else {
                BundleConstants.Flag_facedetection = 0;
                TastyToast.makeText(activity, "Image not Available", TastyToast.LENGTH_LONG, TastyToast.INFO);
            }
        } else if (availableSlotsResponseModel.getFlag() == 0) {
            BundleConstants.Flag_facedetection = 0;
        } else if (availableSlotsResponseModel.getFlag() == 2) {
            BundleConstants.Flag_facedetection = 2;
        }
    }

    public Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty(Constants.HEADER_USER_AGENT, Global.getHeaderValue(activity));
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
        try {
            if (bitmap != null) {
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());
                // Start a background task to detect faces in the image.
                new DetectionTask(index).execute(inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            com.microsoft.projectoxford.face.FaceServiceClient faceServiceClient = new com.microsoft.projectoxford.face.FaceServiceRestClient(end_key, sub_key);
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
                            BundleConstants.Flag_facedetection = 0;
                        }

                        Toast.makeText(getApplicationContext(), "Image contain more than 1 faces.", Toast.LENGTH_LONG).show();
                    } else {
                        if (index == 1) {
                            faceDetected = faces.size();
                            mFaceId1 = faces.get(0).faceId;
                            btn_uploadPhoto.setVisibility(View.VISIBLE);
                            btn_takePhoto.setVisibility(View.GONE);
                        } else {
                            mFaceId0 = faces.get(0).faceId;
                        }
                    }
                    MessageLogger.PrintMsg("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (index == 1) {
                    TastyToast.makeText(activity, getString(R.string.no_face_detected), TastyToast.LENGTH_LONG, TastyToast.WARNING);
                    btn_takePhoto.setVisibility(View.VISIBLE);
                    btn_uploadPhoto.setVisibility(View.GONE);
                } else {
                    BundleConstants.Flag_facedetection = 0;
                }
            }
        }

        if (result != null && result.length == 0) {
            TastyToast.makeText(activity, getString(R.string.no_face_detected), TastyToast.LENGTH_LONG, TastyToast.WARNING);
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
            com.microsoft.projectoxford.face.FaceServiceClient faceServiceClient = new com.microsoft.projectoxford.face.FaceServiceRestClient(end_key, sub_key);
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
            if (appPreferenceManager.getLoginResponseModel() != null && !InputUtils.isNull(appPreferenceManager.getLoginResponseModel().getUserID())) {
                CallSelfieUploadAPI(appPreferenceManager.getLoginResponseModel().getUserID(), imagefile);
            } else {
                Toast.makeText(getApplicationContext(), "Invalid Login", Toast.LENGTH_SHORT).show();
            }
        } else if (isIdentical == 2) {
            btn_takePhoto.setVisibility(View.VISIBLE);
            btn_uploadPhoto.setVisibility(View.GONE);
            TastyToast.makeText(activity, "" + verificationResult, TastyToast.LENGTH_LONG, TastyToast.ERROR);
        }
    }


}
