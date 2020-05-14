package com.thyrocare.utils.app;

import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.thyrocare.Controller.TrackUserActivityController;
import com.thyrocare.R;
import com.thyrocare.application.ApplicationController;
import com.thyrocare.models.api.request.TrackUserActivityRequestModel;
import com.thyrocare.utils.api.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.TELEPHONY_SERVICE;

public class LogUserActivityTagging {
    public static final String TAG = LogUserActivityTagging.class.getSimpleName();
    SharedPreferences sharedPreferencesUserActivity;
    SharedPreferences.Editor editorUserActivity;
    public LogUserActivityTagging(Activity activity, String screen) {
        try {
            String appversion = CommonUtils.getAppVersion(activity);
            String os = "ANDROID " + Build.VERSION.RELEASE;
            sharedPreferencesUserActivity = activity.getSharedPreferences("TrackUserActivity", MODE_PRIVATE);
            editorUserActivity = sharedPreferencesUserActivity.edit();
            Boolean isFirstInstall = sharedPreferencesUserActivity.getBoolean("firstInstall", true);

            if (isFirstInstall) {
                editorUserActivity.putBoolean("firstInstall", false);
                editorUserActivity.putString("ModType", "INSTALL");
                editorUserActivity.putString("IsLogin", "N");
                editorUserActivity.putString("OS", os);
                editorUserActivity.putString("VERSION", appversion);
            } else {
                if (!os.equalsIgnoreCase(sharedPreferencesUserActivity.getString("OS", ""))) {
                    editorUserActivity.putString("OS", os);
                    editorUserActivity.putString("ModType", "OS UPDATED");
                }
                if (!appversion.equalsIgnoreCase(sharedPreferencesUserActivity.getString("VERSION", ""))) {
                    editorUserActivity.putString("VERSION", appversion);
                    editorUserActivity.putString("ModType", "VERSION UPDATED");
                }
            }
            AppPreferenceManager appPreferenceManager = new AppPreferenceManager(activity);
            if (appPreferenceManager.getLoginResponseModel() != null && !TextUtils.isEmpty(appPreferenceManager.getLoginResponseModel().getUserID())) {
                editorUserActivity.putString("UserID", appPreferenceManager.getUserID());
            } else {
                editorUserActivity.putString("UserID", "");
            }

            if (!TextUtils.isEmpty(screen) && screen.equalsIgnoreCase(BundleConstants.LOGIN)) {
                editorUserActivity.putString("ModType", "LOGIN");
                editorUserActivity.putString("IsLogin", "Y");
            }else if (!TextUtils.isEmpty(screen) && screen.equalsIgnoreCase(BundleConstants.LOGOUT)){
                editorUserActivity.putString("ModType", "LOGOUT");
                editorUserActivity.putString("IsLogin", "N");
            }
            editorUserActivity.apply();

            String imeiNo = null;
            try {
                TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    imeiNo = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
                } else {
                    if (telephonyManager.getDeviceId() != null) {
                        imeiNo = telephonyManager.getDeviceId();
                    } else {
                        imeiNo = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            TrackUserActivityRequestModel trackUserActivityRequestModel = new TrackUserActivityRequestModel();
            trackUserActivityRequestModel.setAppId(BundleConstants.APPID_TRACKACTIVITY);
            trackUserActivityRequestModel.setIMIENo(imeiNo);
            trackUserActivityRequestModel.setIslogin(sharedPreferencesUserActivity.getString("IsLogin", ""));
            trackUserActivityRequestModel.setModType(sharedPreferencesUserActivity.getString("ModType", ""));
            trackUserActivityRequestModel.setOS(sharedPreferencesUserActivity.getString("OS", ""));
            trackUserActivityRequestModel.setVersion(sharedPreferencesUserActivity.getString("VERSION", ""));
            trackUserActivityRequestModel.setUserID(sharedPreferencesUserActivity.getString("UserID", ""));
            trackUserActivityRequestModel.setToken("");

            String json = new Gson().toJson(trackUserActivityRequestModel);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                if (ApplicationController.trackUserActivityController != null) {
                    ApplicationController.trackUserActivityController = null;
                }
                if (NetworkUtils.isNetworkAvailable(activity)) {
                    ApplicationController.trackUserActivityController = new TrackUserActivityController(activity);
                    ApplicationController.trackUserActivityController.trackUserActivity(jsonObject);
                    Log.e(TAG, "LogUserActivityTagging: "+jsonObject);
                } else {
                    Toast.makeText(activity, "" + activity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void clearPreferences() {
        editorUserActivity.clear();
        editorUserActivity.commit();
    }
}
