package com.dhb.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.customview.CustomUpdateDailog;
import com.dhb.dao.CreateOrUpgradeDbTask;
import com.dhb.dao.DbHelper;
import com.dhb.dao.DhbDao;
import com.dhb.delegate.CustomUpdateDialogOkButtonOnClickedDelegate;
import com.dhb.models.data.VersionControlMasterModel;
import com.dhb.network.ApiCallAsyncTask;
import com.dhb.network.ApiCallAsyncTaskDelegate;
import com.dhb.network.AsyncTaskForRequest;
import com.dhb.network.ResponseParser;
import com.dhb.service.LocationUpdateService;
import com.dhb.uiutils.AbstractActivity;
import com.dhb.utils.api.Logger;
import com.dhb.utils.app.AppConstants;
import com.dhb.utils.app.AppPreferenceManager;
import com.dhb.utils.app.InputUtils;

import org.json.JSONException;

import java.util.Calendar;

import static android.widget.Toast.LENGTH_SHORT;


public class SplashScreenActivity extends AbstractActivity {
    private VersionControlMasterModel versionControlMasterModels;
    private Activity activity;
    private AppPreferenceManager appPreferenceManager;
    public static final String TAG_FRAGMENT = "SPLASH_SCREEN_ACTIVITY";
    CustomUpdateDailog cudd;
    private static Intent locationUpdateIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        activity = this;
        appPreferenceManager = new AppPreferenceManager(activity);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity,
                            new String[]{
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.READ_CONTACTS,
                                    Manifest.permission.CALL_PHONE,
                                    Manifest.permission.ACCESS_NETWORK_STATE,
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.VIBRATE,
                                    Manifest.permission.WAKE_LOCK,
                                    Manifest.permission.INTERNET},
                            AppConstants.APP_PERMISSIONS);
                } else {
                    fetchVersionControlDetails();
                }
            }
        }, AppConstants.SPLASH_SCREEN_TIMEOUT);
        //Call Service
        locationUpdateIntent= new Intent(this,LocationUpdateService.class);
    }
    void StartLocationUpdateService() {
        try {

            try {

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.SECOND, 1);
                if (!IsAlarmSet()) {
                    PendingIntent pintent = PendingIntent.getService(this, 0,
                            locationUpdateIntent, 0);
                    AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarm.setRepeating(AlarmManager.RTC_WAKEUP,
                            cal.getTimeInMillis(),15000L,
                            pintent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            startService(locationUpdateIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    boolean IsAlarmSet() {
        return PendingIntent.getBroadcast(this, 0, locationUpdateIntent,
                PendingIntent.FLAG_NO_CREATE) != null;
    }
    private void goAhead() {
        DbHelper.init(activity.getApplicationContext());
        new CreateOrUpgradeDbTask(new DhbDbDelegate(), getApplicationContext()).execute();
    }

    private void fetchVersionControlDetails() {
        Logger.error(TAG_FRAGMENT + "--fetchData: ");
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchVersionDetailApiAsyncTask = asyncTaskForRequest.getVersionControlDetailsRequestAsyncTask();
        fetchVersionDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new FetchVersionDetailsApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            fetchVersionDetailApiAsyncTask.execute(fetchVersionDetailApiAsyncTask);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, LENGTH_SHORT).show();
        }
    }

    private class FetchVersionDetailsApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            Logger.debug(TAG_FRAGMENT + "--apiCallResult: ");
            if (statusCode == 200) {
                ResponseParser responseParser = new ResponseParser(activity);
                VersionControlMasterModel versionControlMasterModel = new VersionControlMasterModel();

                versionControlMasterModel = responseParser.getVersionControlMasterResponse(json, statusCode);
                versionControlMasterModels = versionControlMasterModel;

                if (AppConstants.ANDROID_APP_VERSION < versionControlMasterModel.getCurrentVirson()) {
                    cudd = new CustomUpdateDailog(activity, new CustomUpdateDialogOkButtonOnClickedDelegate() {
                        @Override
                        public void onClicked() {
                            ApiCallAsyncTask logoutAsyncTask = new AsyncTaskForRequest(activity).getLogoutRequestAsyncTask();
                            logoutAsyncTask.setApiCallAsyncTaskDelegate(new LogoutAsyncTaskDelegateResult());
                            if (isNetworkAvailable(activity)) {
                                logoutAsyncTask.execute(logoutAsyncTask);
                            } else {
                                Toast.makeText(activity, "Logout functionality is only available in Online Mode", Toast.LENGTH_SHORT).show();
                                System.exit(0);
                            }
                        }
                    });
                    cudd.show();
                    cudd.setCancelable(false);
                } else {
                    goAhead();
                }
            }
            else{
                Toast.makeText(activity,"Unable to Fetch Version Information",LENGTH_SHORT).show();
                goAhead();
            }

        }

        @Override
        public void onApiCancelled() {
            Logger.error(TAG_FRAGMENT + "onApiCancelled: ");
            Toast.makeText(activity, R.string.network_error, LENGTH_SHORT).show();
        }


    }

    private class LogoutAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                appPreferenceManager.clearAllPreferences();
                new DhbDao(activity).deleteTablesonLogout();
                finish();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.dhb.btech"));
                startActivity(intent);
            } else {
                Toast.makeText(activity, "Failed to Logout", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {

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
                        goAhead();
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
            StartLocationUpdateService();
            if (InputUtils.isNull(appPreferenceManager.getAPISessionKey())) {
                switchToActivity(activity, LoginScreenActivity.class, new Bundle());
            } else {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.MILLISECOND, 0);
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.HOUR_OF_DAY, 0);
                if (appPreferenceManager.getSelfieResponseModel() != null && c.getTimeInMillis() < appPreferenceManager.getSelfieResponseModel().getTimeUploaded()) {
                    switchToActivity(activity, HomeScreenActivity.class, new Bundle());
                } else {
                    switchToActivity(activity, SelfieUploadActivity.class, new Bundle());
                }
            }
        }
    }
}
