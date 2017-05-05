package com.dhb.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import com.dhb.R;
import com.dhb.dao.CreateOrUpgradeDbTask;
import com.dhb.dao.DbHelper;
import com.dhb.uiutils.AbstractActivity;
import com.dhb.utils.app.AppConstants;
import com.dhb.utils.app.AppPreferenceManager;
import com.dhb.utils.app.InputUtils;

import java.util.Calendar;


public class SplashScreenActivity extends AbstractActivity {

    private Activity activity;
    private AppPreferenceManager appPreferenceManager;

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
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.CHANGE_WIFI_STATE,
                                    Manifest.permission.ACCESS_WIFI_STATE,
                                    Manifest.permission.READ_CONTACTS,
                                    Manifest.permission.WRITE_CONTACTS,
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
                    goAhead();
                }
            }
        }, AppConstants.SPLASH_SCREEN_TIMEOUT);
    }

    private void goAhead() {
        DbHelper.init(activity.getApplicationContext());
        new CreateOrUpgradeDbTask(new DhbDbDelegate(), getApplicationContext()).execute();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
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
            if(InputUtils.isNull(appPreferenceManager.getAPISessionKey())) {
                switchToActivity(activity, LoginScreenActivity.class, new Bundle());
            }
            else {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.MILLISECOND, 0);
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.HOUR_OF_DAY, 0);
                if(appPreferenceManager.getSelfieResponseModel()!=null && c.getTimeInMillis()<appPreferenceManager.getSelfieResponseModel().getTimeUploaded()) {
                    switchToActivity(activity, HomeScreenActivity.class, new Bundle());
                }else{
                    switchToActivity(activity, SelfieUploadActivity.class, new Bundle());
                }
            }
        }
    }
}
