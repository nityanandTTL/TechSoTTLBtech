package com.thyrocare.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.Controller.DeviceLogOutController;
import com.thyrocare.Controller.NotificationMappingController;
import com.thyrocare.R;
import com.thyrocare.application.ApplicationController;
import com.thyrocare.customview.CustomUpdateDailog;
import com.thyrocare.dao.CreateOrUpgradeDbTask;
import com.thyrocare.dao.DbHelper;
import com.thyrocare.dao.DhbDao;
import com.thyrocare.delegate.CustomUpdateDialogOkButtonOnClickedDelegate;
import com.thyrocare.models.api.request.NotificationMappingModel;
import com.thyrocare.models.api.response.BtechAvaliabilityResponseModel;
import com.thyrocare.models.api.response.NewBtechAvaliabilityResponseModel;
import com.thyrocare.models.data.TSPNBT_AvilModel;
import com.thyrocare.models.data.VersionControlMasterModel;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.network.ResponseParser;
import com.thyrocare.service.LocationUpdateService;
import com.thyrocare.uiutils.AbstractActivity;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.AppConstants;
import com.thyrocare.utils.app.AppPreferenceManager;
import com.thyrocare.utils.app.CommonUtils;
import com.thyrocare.utils.app.DeviceUtils;
import com.thyrocare.utils.app.InputUtils;
import com.thyrocare.utils.app.LogUserActivityTagging;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;

import static android.widget.Toast.LENGTH_SHORT;
import static com.thyrocare.utils.app.BundleConstants.LOGIN;


/**
 * The type Splash screen activity.
 */
public class SplashScreenActivity extends AbstractActivity{
    private Activity activity;
    private AppPreferenceManager appPreferenceManager;
    /**
     * The constant TAG_FRAGMENT.
     */
    public static final String TAG_FRAGMENT = "SPLASH_SCREEN_ACTIVITY";
    /**
     * The Cudd.
     */
    CustomUpdateDailog cudd;
    private BtechAvaliabilityResponseModel btechAvaliabilityResponseModel;
    private int AppId;
    private static Intent TImeCheckerIntent;

    Boolean isFromNotification = false;

    int screenCategory;

    private static Intent locationUpdateIntent;
    /**
     * The Version control master model.
     */
    VersionControlMasterModel versionControlMasterModel;
    private ArrayList<TSPNBT_AvilModel> TSP_NBTAvailArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        activity = this;
        AppId = AppConstants.BTECH_APP_ID;

        if (getIntent().hasExtra("isFromNotification") &&  getIntent().hasExtra("screenCategory") ){
            isFromNotification = getIntent().getBooleanExtra("isFromNotification",false);
            screenCategory = getIntent().getIntExtra("screenCategory",0);
        }

        appPreferenceManager = new AppPreferenceManager(activity);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED )
                {
                    ActivityCompat.requestPermissions(activity,
                            new String[]{
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.READ_CONTACTS,
                                    Manifest.permission.CALL_PHONE,
                                    Manifest.permission.READ_SMS,
                                    Manifest.permission.ACCESS_NETWORK_STATE,
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.VIBRATE,
                                    Manifest.permission.WAKE_LOCK,
                                    Manifest.permission.INTERNET,
                                    Manifest.permission.READ_PHONE_STATE},
                            AppConstants.APP_PERMISSIONS);
                } else {
                    fetchVersionControlDetails();
                }
            }
        }, AppConstants.SPLASH_SCREEN_TIMEOUT);
        //Call Service
        Logger.error("locationUpdateIntent Executed 1");

        locationUpdateIntent = new Intent(this, LocationUpdateService.class);
    }



    private void fetchData() {
        Logger.error(TAG_FRAGMENT + "--fetchData: ");
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchbtechavailAsyncTask = asyncTaskForRequest.getBtechAvaliability();
        fetchbtechavailAsyncTask.setApiCallAsyncTaskDelegate(new DispatchToHubDetailDisplayApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            fetchbtechavailAsyncTask.execute(fetchbtechavailAsyncTask);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    /* private class DispatchToHubDetailDisplayApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
         @Override
         public void apiCallResult(String json, int statusCode) throws JSONException {
             Logger.debug(TAG_FRAGMENT + "dayssssssssss ");
             if (statusCode == 200) {
                 ResponseParser responseParser = new ResponseParser(activity);
                 BtechAvaliabilityResponseModel btechAvaliabilityResponseModel = new BtechAvaliabilityResponseModel();
                 btechAvaliabilityResponseModel = responseParser.getBtechAvaliabilityResponseModel(json, statusCode);
                 if (btechAvaliabilityResponseModel != null) {
                   if(btechAvaliabilityResponseModel.getNumberofDays()==0) {
                     if (appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.NBTTSP_ROLE_ID)) {
                         Intent i = new Intent(getApplicationContext(), SelfieUploadActivity.class);
                         i.putExtra("LEAVEINTIMATION", "0");
                         startActivity(i);
                     }
                    *//* else if (appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.LME_ROLE_ID)) {

                        Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
                        i.putExtra("LEAVEINTIMATION", "0");
                        startActivity(i);
                    }*//*
                    else if (btechAvaliabilityResponseModel.getNumberofDays() == 0) {
                        Logger.error("ZERRO");
                        Bundle bundle = new Bundle();

                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.MILLISECOND, 0);
                        c.set(Calendar.SECOND, 0);
                        c.set(Calendar.MINUTE, 0);
                        c.set(Calendar.HOUR_OF_DAY, 0);
                        if (appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.TSP_ROLE_ID)) {
                            Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
                            i.putExtra("LEAVEINTIMATION", "0");
                            startActivity(i);
                            finish();
                        } else {
                            if (appPreferenceManager.getSelfieResponseModel() != null && c.getTimeInMillis() < appPreferenceManager.getSelfieResponseModel().getTimeUploaded()) {
                                // switchToActivity(activity, ScheduleYourDayActivity.class, new Bundle());
                                Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
                                i.putExtra("LEAVEINTIMATION", "0");
                                startActivity(i);
                                finish();
                            } else {
                                switchToActivity(activity, SelfieUploadActivity.class, new Bundle());
                            }
                        }
                    } else if (btechAvaliabilityResponseModel.getNumberofDays() == 1) {
                        Logger.error("ONEEEE");
                        Intent mIntent = new Intent(activity, ScheduleYourDayActivity.class);
                        mIntent.putExtra("WHEREFROM", "0");
                        startActivity(mIntent);
                    } else if (btechAvaliabilityResponseModel.getNumberofDays() == 3) {
                        Logger.error("THREEE");
                        Intent mIntent = new Intent(activity, ScheduleYourDayActivity2.class);
                        mIntent.putExtra("WHEREFROM", "0");
                        startActivity(mIntent);

                    }else if (btechAvaliabilityResponseModel.getNumberofDays() == 2) {
                        Logger.error("FOURRRRR");
                        Intent mIntent = new Intent(activity, ScheduleYourDayIntentActivity.class);
                        mIntent.putExtra("WHEREFROM", "0");
                        startActivity(mIntent);
                    }
                } else {
                    Logger.error("else " + json);
                }
            } else {
                Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();
        }
    }
*/
    private class DispatchToHubDetailDisplayApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            Logger.debug(TAG_FRAGMENT + "dayssssssssss ");
            if (statusCode == 200) {
                ResponseParser responseParser = new ResponseParser(activity);
                NewBtechAvaliabilityResponseModel newBtechAvaliabilityResponseModel = new NewBtechAvaliabilityResponseModel();
                newBtechAvaliabilityResponseModel = responseParser.getNewBtechAvaliabilityResponseModel(json, statusCode);
                appPreferenceManager.setNEWBTECHAVALIABILITYRESPONSEMODEL(newBtechAvaliabilityResponseModel);


                if (appPreferenceManager.getNEWBTECHAVALIABILITYRESPONSEMODEL() != null) {
                    if (appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.NBTTSP_ROLE_ID)) {
                        Intent i = new Intent(getApplicationContext(), SelfieUploadActivity.class);
                        i.putExtra("LEAVEINTIMATION", "0");
                        startActivity(i);
                    }
                    else if (appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.LME_ROLE_ID)) {

                        Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
                        i.putExtra("isFromNotification",isFromNotification);
                        i.putExtra("screenCategory",screenCategory);
                        i.putExtra("LEAVEINTIMATION", "0");
                        startActivity(i);
                    }
                    else if(appPreferenceManager.getNEWBTECHAVALIABILITYRESPONSEMODEL().getNumberOfDays().getDay1()==1){
                        Logger.error("ONEEEE");
                        Intent mIntent = new Intent(activity, ScheduleYourDayActivity.class);
                        mIntent.putExtra("WHEREFROM", "0");
                        startActivity(mIntent);

                    }else if(appPreferenceManager.getNEWBTECHAVALIABILITYRESPONSEMODEL().getNumberOfDays().getDay2()==1){
                        Logger.error("THREEE");
                        Intent mIntent = new Intent(activity, ScheduleYourDayActivity2.class);
                        mIntent.putExtra("WHEREFROM", "0");
                        startActivity(mIntent);

                    }else if(appPreferenceManager.getNEWBTECHAVALIABILITYRESPONSEMODEL().getNumberOfDays().getDay3()==1){
                        Logger.error("FOUR");
                        Intent mIntent = new Intent(activity, ScheduleYourDayActivity3.class);
                        mIntent.putExtra("WHEREFROM", "0");
                        startActivity(mIntent);

                    }else if(appPreferenceManager.getNEWBTECHAVALIABILITYRESPONSEMODEL().getNumberOfDays().getDay4()==1){
                        Logger.error("FOUR");
                        Intent mIntent = new Intent(activity, ScheduleYourDayActivity4.class);
                        mIntent.putExtra("WHEREFROM", "0");
                        startActivity(mIntent);
                    }else {
                        Logger.error("ZERRO");
                        Bundle bundle = new Bundle();

                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.MILLISECOND, 0);
                        c.set(Calendar.SECOND, 0);
                        c.set(Calendar.MINUTE, 0);
                        c.set(Calendar.HOUR_OF_DAY, 0);
                        if (appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.TSP_ROLE_ID)) {
                            Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
                            i.putExtra("LEAVEINTIMATION", "0");
                            i.putExtra("isFromNotification",isFromNotification);
                            i.putExtra("screenCategory",screenCategory);
                            startActivity(i);
                            finish();
                        } else {
                            if (appPreferenceManager.getSelfieResponseModel() != null && c.getTimeInMillis() < appPreferenceManager.getSelfieResponseModel().getTimeUploaded()) {
                                // switchToActivity(activity, ScheduleYourDayActivity.class, new Bundle());
                                Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
                                i.putExtra("LEAVEINTIMATION", "0");
                                i.putExtra("isFromNotification",isFromNotification);
                                i.putExtra("screenCategory",screenCategory);
                                startActivity(i);
                                finish();
                            } else {
                                switchToActivity(activity, SelfieUploadActivity.class, new Bundle());
                            }
                        }
                    }
                } else {
                    Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
                }
            }


        }

        @Override
        public void onApiCancelled() {

        }
    }

    /**
     * go ahest with next funtion
     */
    private void goAhead() {
        DbHelper.init(activity.getApplicationContext());
        new CreateOrUpgradeDbTask(new DhbDbDelegate(), getApplicationContext()).execute();
        Logger.error("locationUpdateIntent Executed 3");
    }

    /**
     * fetech version control details
     */
    private void fetchVersionControlDetails() {
        Log.d("result***", "start");
        Logger.error(TAG_FRAGMENT + "--fetchData: ");
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchVersionDetailApiAsyncTask = asyncTaskForRequest.getVersionControlDetailsRequestAsyncTask(AppId);
        fetchVersionDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new FetchVersionDetailsApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            fetchVersionDetailApiAsyncTask.execute(fetchVersionDetailApiAsyncTask);
            trackUserActivity();
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, LENGTH_SHORT).show();
        }
    }

    private class FetchVersionDetailsApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            Log.d("result***", "stop");
            Logger.debug(TAG_FRAGMENT + "--apiCallResult: ");
            if (statusCode == 200) {
                ResponseParser responseParser = new ResponseParser(activity);
                versionControlMasterModel = responseParser.getVersionControlMasterResponse(json, statusCode);

                try {
                    SharedPreferences.Editor editor = getSharedPreferences("VersionControlFlags", MODE_PRIVATE).edit();
                    editor.putInt("OTPEnabled", versionControlMasterModel != null ? versionControlMasterModel.getOTPEnabled() : 0);
                    editor.putInt("SSLPinning", versionControlMasterModel != null ? versionControlMasterModel.getSSLPinning() : 0);
                    editor.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (AppConstants.ANDROID_APP_VERSION < versionControlMasterModel.getAPICurrentVerson()) {
                    cudd = new CustomUpdateDailog(activity, new CustomUpdateDialogOkButtonOnClickedDelegate() {
                        @Override
                        public void onUpdateClicked() {
                            if (!InputUtils.isNull(appPreferenceManager.getAPISessionKey())) {
                                ApiCallAsyncTask logoutAsyncTask = new AsyncTaskForRequest(activity).getLogoutRequestAsyncTask();
                                logoutAsyncTask.setApiCallAsyncTaskDelegate(new LogoutAsyncTaskDelegateResult());
                                if (isNetworkAvailable(activity)) {
                                    logoutAsyncTask.execute(logoutAsyncTask);
                                    if (appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.LME_ROLE_ID)) {

                                    } else {
                                        CallLogOutDevice();
                                    }
                                } else {
                                    Toast.makeText(activity, "Logout functionality is only available in Online Mode", Toast.LENGTH_SHORT).show();
                                    finishAffinity();
                                }
                            } else {
                                appPreferenceManager.clearAllPreferences();
                                new DhbDao(activity).deleteTablesonLogout();
                                //jai
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(versionControlMasterModel.getAppUrl()));
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onOkClicked() {
                            if (AppConstants.ANDROID_APP_VERSION < versionControlMasterModel.getAPICurrentVerson()) {
//                                System.exit(0);
                                finishAffinity();
                            } else {
                                goAhead();
                            }
                        }
                    });
                    cudd.show();
                    cudd.setCancelable(false);
                } else {
                    goAhead();
                }
            } else {
                Toast.makeText(activity, "Unable to Fetch Version Information", LENGTH_SHORT).show();
                goAhead();
            }

        }

        /**
         *
         */
        private class DownloadApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
            @Override
            public void apiCallResult(String json, int statusCode) throws JSONException {
                Logger.error("apiCallResult " + json);
            }

            @Override
            public void onApiCancelled() {
                Logger.error("onApiCancelled ");
            }
        }

        @Override
        public void onApiCancelled() {
            Logger.error(TAG_FRAGMENT + "onApiCancelled: ");
            Toast.makeText(activity, R.string.network_error, LENGTH_SHORT).show();
        }


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

    private class LogoutAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                appPreferenceManager.clearAllPreferences();
                new DhbDao(activity).deleteTablesonLogout();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(versionControlMasterModel.getAppUrl()));
                startActivity(intent);

                finish();
            } else if (statusCode == 401) {
                CallLogOutFromDevice();
            } else {
                Toast.makeText(activity, "Failed to Logout", Toast.LENGTH_SHORT).show();
                finishAffinity();
            }
        }

        @Override
        public void onApiCancelled() {

        }
    }

    /**
     * call logout from device
     */
    public void CallLogOutFromDevice() {
        try {
            TastyToast.makeText(activity, "Authorization failed, need to Login again...", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
            appPreferenceManager.clearAllPreferences();
            try {
                new DhbDao(activity).deleteTablesonLogout();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            // stopService(TImeCheckerIntent);
               /* finish();
                finishAffinity();*/

            Intent n = new Intent(activity, LoginScreenActivity.class);
            n.setAction(Intent.ACTION_MAIN);
            n.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(n);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case AppConstants.APP_PERMISSIONS: {
                if (grantResults.length > 0) {
                    boolean allGranted = true;
                    for (int result : grantResults
                            ) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            allGranted = false;
                            break;
                        }
                    }
                    if (!allGranted) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setMessage("Please provide the permissions requested. Application will shutdown now.")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        activity.finish();
                                    }
                                });
                        builder.create().
                                show();
                    } else {
                        fetchVersionControlDetails();
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("Failed to recognize the permissions. Please restart the app and try again")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    activity.finish();
                                }
                            });
                    builder.create().
                            show();
                }
                return;
            }
        }

    }

    private class DhbDbDelegate implements CreateOrUpgradeDbTask.DbTaskDelegate {
        @Override
        public void dbTaskCompletedWithResult(Boolean result) {
            //   StartLocationUpdateService();
//            startService(locationUpdateIntent);
            if (DeviceUtils.isMyServiceRunning(LocationUpdateService.class, activity)) {
            } else {
                if (!DeviceUtils.isAppIsInBackground(SplashScreenActivity.this)){
                    startService(locationUpdateIntent);
                }
            }
            if (InputUtils.isNull(appPreferenceManager.getAPISessionKey())) {
                switchToActivity(activity, LoginScreenActivity.class, new Bundle());
            } else {

                notificationMapping();
              /*  Calendar c = Calendar.getInstance();
                c.set(Calendar.MILLISECOND, 0);
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.HOUR_OF_DAY, 0);*/
                if (appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.TSP_ROLE_ID)) {
                    //                    Call_TspScreen();
                    fetchDataForTsp();
                } else if (appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.NBTTSP_ROLE_ID)) {
                    Logger.error("role1: " + AppConstants.NBTTSP_ROLE_ID);

                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.MILLISECOND, 0);
                    c.set(Calendar.SECOND, 0);
                    c.set(Calendar.MINUTE, 0);
                    c.set(Calendar.HOUR_OF_DAY, 0);

                    if (appPreferenceManager.getSelfieResponseModel() != null && c.getTimeInMillis() < appPreferenceManager.getSelfieResponseModel().getTimeUploaded()) {


                        Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
                        i.putExtra("LEAVEINTIMATION", "0");
                        i.putExtra("isFromNotification",isFromNotification);
                        i.putExtra("screenCategory",screenCategory);
                        startActivity(i);
                        finish();
                    } else {
                        Intent i = new Intent(getApplicationContext(), SelfieUploadActivity.class);
                        i.putExtra("LEAVEINTIMATION", "0");
                        startActivity(i);

                    }


                } else {
                    fetchData();
                }

            }
        }
    }

    /**
     * make call to TSP screen
     */
    private void Call_TspScreen() {
        Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
        i.putExtra("LEAVEINTIMATION", "0");
        i.putExtra("isFromNotification",isFromNotification);
        i.putExtra("screenCategory",screenCategory);
        startActivity(i);
        finish();
    }

    /**
     * fetch data for tsp
     */
    private void fetchDataForTsp() {
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchtspnbtavailAsyncTask = asyncTaskForRequest.getTSPNBTAvaliability(appPreferenceManager.getLoginResponseModel().getUserID());
        fetchtspnbtavailAsyncTask.setApiCallAsyncTaskDelegate(new TSPNBTDetailDisplayApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            fetchtspnbtavailAsyncTask.execute(fetchtspnbtavailAsyncTask);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private class TSPNBTDetailDisplayApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                TSP_NBTAvailArr = new ResponseParser(activity).getTSPNBTDetailsResponseModel(json, statusCode);
                if (TSP_NBTAvailArr.size() != 0) {
                    Intent mIntent = new Intent(activity, Tsp_ScheduleYourDayActivity.class);
                    mIntent.putExtra("WHEREFROM", "0");
                    mIntent.putParcelableArrayListExtra(CommonUtils.TSP_NBT_Str, TSP_NBTAvailArr);
                    startActivity(mIntent);
                } else {
                    Call_TspScreen();
                }
            } else {
                /*Intent mIntent = new Intent(activity, Tsp_ScheduleYourDayActivity.class);
                mIntent.putExtra("WHEREFROM", "0");
                mIntent.putParcelableArrayListExtra(CommonUtils.TSP_NBT_Str, TSP_NBTAvailArr);
                startActivity(mIntent);*/
                Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();
        }
    }

    public void notificationMapping() {
        NotificationMappingModel notificationMappingModel = new NotificationMappingModel();

        String clientID = appPreferenceManager.getLoginResponseModel().getUserID();
        String token = FirebaseInstanceId.getInstance().getToken();
        System.out.println("Token : "+token);
        String appName = "BTech_AllDevices";
        String entryBy = appPreferenceManager.getLoginResponseModel().getUserID();
        String topic = "";

        notificationMappingModel.setAppName(appName);
        notificationMappingModel.setClient_Id(clientID);
        notificationMappingModel.setEnterBy(entryBy);
        notificationMappingModel.setToken(token);
        notificationMappingModel.setTopic(topic);

        if (!TextUtils.isEmpty(notificationMappingModel.getToken())) {
            if (ApplicationController.notificationMappingController != null) {
                ApplicationController.notificationMappingController = null;
            }

            ApplicationController.notificationMappingController = new NotificationMappingController(activity);
            ApplicationController.notificationMappingController.getNotificationMapping(notificationMappingModel);
            Log.e("shami -- ", "notificationMapping: Token Generated" );
        } else {
            Log.e("shami -- ", "notificationMapping: Token not generated" );
        }
    }

    private void trackUserActivity() {
        new LogUserActivityTagging(activity, "");
    }
}