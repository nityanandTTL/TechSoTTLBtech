package com.thyrocare.btechapp.NewScreenDesigns.Utils;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.TELEPHONY_SERVICE;

import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.thyrocare.btechapp.NewScreenDesigns.Controllers.TrackUserActivityController;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.TrackUserActivityRequestModel;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.utils.api.NetworkUtils;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.CommonUtils;
import com.thyrocare.btechapp.utils.app.InputUtils;

import application.ApplicationController;

public class LogUserActivityTagging {

    public static final String TAG = LogUserActivityTagging.class.getSimpleName();
    String str_modType;

    public LogUserActivityTagging(Activity activity, String screen, String remark) {
        try {
            String appversion = CommonUtils.getAppVersion(activity);
            String os = "ANDROID " + Build.VERSION.RELEASE;
            SharedPreferences sharedPreferencesUserActivity = activity.getSharedPreferences("TrackUserActivity", MODE_PRIVATE);
            SharedPreferences.Editor editorUserActivity = sharedPreferencesUserActivity.edit();
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
            } else if (!TextUtils.isEmpty(screen) && screen.equalsIgnoreCase(BundleConstants.LOGOUT)) {
                editorUserActivity.putString("ModType", "LOGOUT");
                editorUserActivity.putString("IsLogin", "N");
            } else if (!TextUtils.isEmpty(screen) && screen.equalsIgnoreCase(BundleConstants.WOE)) {
                str_modType = BundleConstants.WOE;
            } else if (!TextUtils.isEmpty(screen) && screen.equalsIgnoreCase(Constants.LEAD)) {
                str_modType = Constants.LEAD;
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
            if (!InputUtils.isNull(str_modType)) {
                trackUserActivityRequestModel.setModType(str_modType);
            } else {
                trackUserActivityRequestModel.setModType(sharedPreferencesUserActivity.getString("ModType", ""));
            }
            trackUserActivityRequestModel.setOS(sharedPreferencesUserActivity.getString("OS", ""));
            trackUserActivityRequestModel.setVersion(sharedPreferencesUserActivity.getString("VERSION", ""));
            trackUserActivityRequestModel.setUserID(sharedPreferencesUserActivity.getString("UserID", ""));
            trackUserActivityRequestModel.setToken("");
            trackUserActivityRequestModel.setLat(CommonUtils.getCurrentLatLong(activity).getmLatitude());
            trackUserActivityRequestModel.setLongi(CommonUtils.getCurrentLatLong(activity).getmLongitude());
            trackUserActivityRequestModel.setIpadd(CommonUtils.getIPAddress(true));
            trackUserActivityRequestModel.setMacadd(CommonUtils.getMACAddress());
            if (!InputUtils.isNull(str_modType) && !InputUtils.isNull(remark)) {
                trackUserActivityRequestModel.setRemark(remark);
            } else {
                trackUserActivityRequestModel.setRemark("");
            }

            try {
                if (ApplicationController.trackUserActivityController != null) {
                    ApplicationController.trackUserActivityController = null;
                }
                if (NetworkUtils.isNetworkAvailable(activity)) {
                    ApplicationController.trackUserActivityController = new TrackUserActivityController(activity);
                    ApplicationController.trackUserActivityController.trackUserActivity(trackUserActivityRequestModel);
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
}
