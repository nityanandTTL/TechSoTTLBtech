package com.thyrocare.btechapp.NewScreenDesigns.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.scottyab.rootbeer.RootBeer;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.GetSSLKeyRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.LogoutRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.NotificationMappingRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.CommonResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.GetSSLKeyResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.NotificationMappingResponseModel;

import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.VersionControlResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.DateUtil;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.LogUserActivityTagging;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.DynamicScheduleYourDayActivity;
import com.thyrocare.btechapp.activity.HomeScreenActivity;
import com.thyrocare.btechapp.activity.ScheduleYourDayActivity;
import com.thyrocare.btechapp.activity.ScheduleYourDayActivity2;
import com.thyrocare.btechapp.activity.ScheduleYourDayActivity3;
import com.thyrocare.btechapp.activity.ScheduleYourDayActivity4;
import com.thyrocare.btechapp.activity.SelfieUploadActivity;
import com.thyrocare.btechapp.activity.Tsp_ScheduleYourDayActivity;
import com.thyrocare.btechapp.customview.CustomUpdateDailog;
import com.thyrocare.btechapp.dao.CreateOrUpgradeDbTask;
import com.thyrocare.btechapp.dao.DbHelper;
import com.thyrocare.btechapp.dao.DhbDao;
import com.thyrocare.btechapp.dao.utils.ConnectionDetector;
import com.thyrocare.btechapp.delegate.CustomUpdateDialogOkButtonOnClickedDelegate;
import com.thyrocare.btechapp.models.api.response.DynamicBtechAvaliabilityResponseModel;
import com.thyrocare.btechapp.models.api.response.NewBtechAvaliabilityResponseModel;
import com.thyrocare.btechapp.models.data.TSPNBT_AvilModel;


import com.thyrocare.btechapp.service.LocationUpdateService;
import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.AppConstants;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.DeviceUtils;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.UnableToConnectMsg;


import static com.thyrocare.btechapp.utils.app.BundleConstants.LOGOUT;

public class SplashActivity extends AppCompatActivity {

    Activity mActivity;
    private AppPreferenceManager appPreferenceManager;
    ConnectionDetector cd;
    Global global;
    Boolean isFromNotification = false;
    int screenCategory = 0;
    private VersionControlResponseModel versionAPIResponseModel;
    private Intent locationUpdateIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        init();
        initData();

    }

    private void init() {
        BundleConstants.setStechDialogFlag = 0;
        mActivity = SplashActivity.this;
        appPreferenceManager = new AppPreferenceManager(mActivity);
        global = new Global(mActivity);
        cd = new ConnectionDetector(mActivity);
        if (getIntent().hasExtra("isFromNotification") && getIntent().hasExtra("screenCategory")) {
            isFromNotification = getIntent().getBooleanExtra("isFromNotification", false);
            screenCategory = getIntent().getIntExtra("screenCategory", 0);
        }
        locationUpdateIntent = new Intent(this, LocationUpdateService.class);
    }

    private void CheckIfDeviceIsRooted() {
        RootBeer rootBeer = new RootBeer(SplashActivity.this);
        if (rootBeer.isRooted()) {
            androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder;
            alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(SplashActivity.this);
            alertDialogBuilder
                    .setMessage("Looks like this device is rooted. Sorry, this App cannot be used on rooted device. ")
                    .setCancelable(true)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finishAffinity();
                            dialog.dismiss();
                        }
                    });
            androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            MessageLogger.verbose(SplashActivity.this, "This Device is rooted");
        } else {
            MessageLogger.verbose(SplashActivity.this, "This Device is not rooted");
            initData();
        }
    }

    private void initData() {
        TedPermission.with(mActivity)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE,
                        Manifest.permission.READ_SMS, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.VIBRATE, Manifest.permission.WAKE_LOCK, Manifest.permission.INTERNET, Manifest.permission.READ_PHONE_STATE)
                .setRationaleMessage("We require few permission for proper functioning of App - Please grant all permissions request in next screen")
                .setRationaleConfirmText("OK")
                .setDeniedMessage("If you reject permission,you can not use all service\n\nPlease turn on permissions at [Setting] > Permission")
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        SharedPreferences pref_SSL = getSharedPreferences("SSLKeysPref", 0);
                        String SLLKeyAPICalledDate = pref_SSL.getString("DateOfSSLKeyAPICalled", "");
                        String CurrentDate = DateUtil.getDateFromLong(System.currentTimeMillis(), "dd-MM-yyyy");
                        if (!CurrentDate.equals(SLLKeyAPICalledDate)) {
                            CallGetSSLKeyAPI();
                        } else {
                            fetchVersionControlDetails();
                        }
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(mActivity, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                .check();
    }

    private void CallGetSSLKeyAPI() {

        SharedPreferences.Editor prefsEditor = getSharedPreferences("SSLKeysPref", 0).edit();
        prefsEditor.putBoolean("ApplySSLPining", false);
        prefsEditor.commit();

        GetSSLKeyRequestModel model = new GetSSLKeyRequestModel();
        model.setAppId("" + BundleConstants.APPID_TRACKACTIVITY);
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(SplashActivity.this, EncryptionUtils.Dcrp_Hex(getString(R.string.B2C_API_VERSION))).create(PostAPIInterface.class);
        Call<GetSSLKeyResponseModel> responseCall = apiInterface.CallGetSSLAPI(model);
        responseCall.enqueue(new Callback<GetSSLKeyResponseModel>() {
            @Override
            public void onResponse(Call<GetSSLKeyResponseModel> call, Response<GetSSLKeyResponseModel> response) {
                GetSSLKeyResponseModel responseModel = null;
                if (response.isSuccessful() && response.body() != null) {
                    responseModel = response.body();
                }
                onSSLKeyApiResponseReceived(responseModel);
            }

            @Override
            public void onFailure(Call<GetSSLKeyResponseModel> call, Throwable t) {
                GetSSLKeyResponseModel responseModel = null;
                onSSLKeyApiResponseReceived(responseModel);
            }
        });
    }

    private void onSSLKeyApiResponseReceived(GetSSLKeyResponseModel responseModel) {
        if (responseModel != null && !StringUtils.isNull(responseModel.getRespId()) && responseModel.getRespId().equalsIgnoreCase("RES0000") && !StringUtils.isNull(responseModel.getAppId()) && responseModel.getAppId().equalsIgnoreCase("" + BundleConstants.APPID_TRACKACTIVITY)) {

            SharedPreferences.Editor prefsEditor = getSharedPreferences("SSLKeysPref", 0).edit();
            Gson gson = new Gson();
            String json = gson.toJson(responseModel);
            prefsEditor.putBoolean("ApplySSLPining", true);
            prefsEditor.putString("SSLKeyResponseModel", json);
            prefsEditor.putString("DateOfSSLKeyAPICalled", DateUtil.getDateFromLong(System.currentTimeMillis(), "dd-MM-yyyy"));
            prefsEditor.commit();
        }

        fetchVersionControlDetails();
    }

    private void fetchVersionControlDetails() {
        trackUserActivity();
        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<VersionControlResponseModel> responseCall = apiInterface.VersionControlAPI();
        responseCall.enqueue(new Callback<VersionControlResponseModel>() {
            @Override
            public void onResponse(Call<VersionControlResponseModel> call, Response<VersionControlResponseModel> response) {
                MessageLogger.PrintMsg("VersionApi Onsuccess");
                if (response.isSuccessful() && response.body() != null) {
                    versionAPIResponseModel = response.body();
                    try {
                        SharedPreferences.Editor editor = getSharedPreferences("VersionControlFlags", MODE_PRIVATE).edit();
                        editor.putInt("OTPEnabled", versionAPIResponseModel != null ? versionAPIResponseModel.getOTPEnabled() : 0);
                        editor.putInt("SSLPinning", versionAPIResponseModel != null ? versionAPIResponseModel.getSSLPinning() : 0);
                        editor.commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    onVersionControlResponseReceived(versionAPIResponseModel);
                } else {
                    CloseApp();
                }
            }

            @Override
            public void onFailure(Call<VersionControlResponseModel> call, Throwable t) {
                MessageLogger.LogDebug("Errror", t.getMessage());
                CloseApp();
            }
        });
    }

    private void trackUserActivity() {
        new LogUserActivityTagging(mActivity, "","");
    }

    private void onVersionControlResponseReceived(final VersionControlResponseModel versionAPIResponseModel) {

        int appLevelVersionCode = 0;
        try {
            appLevelVersionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        int APISide_App_Version = versionAPIResponseModel.getAPICurrentVerson() == 0 ? appLevelVersionCode : versionAPIResponseModel.getAPICurrentVerson();

        if (appLevelVersionCode < APISide_App_Version) {
            final int finalAppLevelVersionCode = appLevelVersionCode;
            CustomUpdateDailog cudd = new CustomUpdateDailog(mActivity, new CustomUpdateDialogOkButtonOnClickedDelegate() {
                @Override
                public void onUpdateClicked() {

                    if (!InputUtils.isNull(appPreferenceManager.getAPISessionKey())) {
                        if (cd.isConnectingToInternet()) {
                            CallLogoutRequestApi();
                            if (!appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.LME_ROLE_ID)) {
                                CallLogoutAPI(versionAPIResponseModel);
                            }
                        } else {
                            Toast.makeText(mActivity, "Logout functionality is only available in Online Mode", Toast.LENGTH_SHORT).show();
                            finishAffinity();
                        }
                    } else {
                        new LogUserActivityTagging(mActivity, LOGOUT,"");
                        appPreferenceManager.clearAllPreferences();
                        new DhbDao(mActivity).deleteTablesonLogout();
                        //jai
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(versionAPIResponseModel.getAppUrl()));
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onOkClicked() {
                    if (finalAppLevelVersionCode < versionAPIResponseModel.getAPICurrentVerson()) {
//                                System.exit(0);
                        finishAffinity();
                    } else {
                        GoAhead();
                    }
                }
            });
            cudd.setCancelable(false);
            if (!mActivity.isFinishing()) {
                cudd.show();
            }


        } else {
            GoAhead();
        }
    }

    public void CallLogoutRequestApi() {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(mActivity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallLogoutRequestApi();
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.code() == 200) {
                    new LogUserActivityTagging(mActivity, LOGOUT,"");
                    appPreferenceManager.clearAllPreferences();
                    new DhbDao(mActivity).deleteTablesonLogout();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(versionAPIResponseModel.getAppUrl()));
                    startActivity(intent);

                    finish();
                } else if (response.code() == 401) {
                    CallLogOutFromDevice();
                } else {
                    Toast.makeText(mActivity, "Failed to Logout", Toast.LENGTH_SHORT).show();
                    finishAffinity();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                MessageLogger.LogDebug("Errror", t.getMessage());

            }
        });
    }

    private void GoAhead() {

        DbHelper.init(mActivity.getApplicationContext());
        new CreateOrUpgradeDbTask(new DhbDbDelegate(), getApplicationContext()).execute();
        Logger.error("locationUpdateIntent Executed 3");

    }

    private class DhbDbDelegate implements CreateOrUpgradeDbTask.DbTaskDelegate {
        @Override
        public void dbTaskCompletedWithResult(Boolean result) {

            if (DeviceUtils.isMyServiceRunning(LocationUpdateService.class, mActivity)) {
            } else {
                if (!DeviceUtils.isAppIsInBackground(mActivity)) {
                    startService(locationUpdateIntent);
                }
            }
            if (InputUtils.isNull(appPreferenceManager.getAPISessionKey())) {
                Intent n = new Intent(mActivity, LoginActivity.class);
                startActivity(n);
                finish();
            } else {
                notificationMapping();
                if (appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.TSP_ROLE_ID)) {
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
                        i.putExtra("isFromNotification", isFromNotification);
                        i.putExtra("screenCategory", screenCategory);
                        startActivity(i);
                        finish();
                    } else {
                        Intent i = new Intent(getApplicationContext(), SelfieUploadActivity.class);
                        i.putExtra("LEAVEINTIMATION", "0");
                        startActivity(i);
                    }
                } else {
                    //                    GetBtechAvailability();
                    //Added to make it dynamic availability
                    GetDynBtechAvailability();
                }

            }
        }
    }

    private void GetBtechAvailability() {

        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<NewBtechAvaliabilityResponseModel> responseCall = apiInterface.GetBtechAvailability(appPreferenceManager.getLoginResponseModel().getUserID());
        responseCall.enqueue(new Callback<NewBtechAvaliabilityResponseModel>() {
            @Override
            public void onResponse(Call<NewBtechAvaliabilityResponseModel> call, Response<NewBtechAvaliabilityResponseModel> response) {
                MessageLogger.PrintMsg("VersionApi Onsuccess");
                if (response.isSuccessful() && response.body() != null) {
                    NewBtechAvaliabilityResponseModel btechAvaliabilityResponseModel = response.body();
                    onBtechAvailabilityResponseReceived(btechAvaliabilityResponseModel);
                } else {
                    global.showCustomToast(mActivity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<NewBtechAvaliabilityResponseModel> call, Throwable t) {
                MessageLogger.LogDebug("Errror", t.getMessage());

            }
        });
    }

    private void onBtechAvailabilityResponseReceived(NewBtechAvaliabilityResponseModel btechAvaliabilityResponseModel) {

        if (btechAvaliabilityResponseModel != null && btechAvaliabilityResponseModel.getNumberOfDays() != null) {
            appPreferenceManager.setNEWBTECHAVALIABILITYRESPONSEMODEL(btechAvaliabilityResponseModel);

            if (appPreferenceManager.getNEWBTECHAVALIABILITYRESPONSEMODEL() != null) {
                if (appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.NBTTSP_ROLE_ID)) {
                    Intent i = new Intent(getApplicationContext(), SelfieUploadActivity.class);
                    i.putExtra("LEAVEINTIMATION", "0");
                    startActivity(i);
                } else if (appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.LME_ROLE_ID)) {

                    Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
                    i.putExtra("isFromNotification", isFromNotification);
                    i.putExtra("screenCategory", screenCategory);
                    i.putExtra("LEAVEINTIMATION", "0");
                    startActivity(i);
                } else if (appPreferenceManager.getNEWBTECHAVALIABILITYRESPONSEMODEL().getNumberOfDays().getDay1() == 1) {
                    Logger.error("ONEEEE");
                    Intent mIntent = new Intent(mActivity, ScheduleYourDayActivity.class);
                    mIntent.putExtra("WHEREFROM", "0");
                    startActivity(mIntent);

                } else if (appPreferenceManager.getNEWBTECHAVALIABILITYRESPONSEMODEL().getNumberOfDays().getDay2() == 1) {
                    Logger.error("THREEE");
                    Intent mIntent = new Intent(mActivity, ScheduleYourDayActivity2.class);
                    mIntent.putExtra("WHEREFROM", "0");
                    startActivity(mIntent);
                } else if (appPreferenceManager.getNEWBTECHAVALIABILITYRESPONSEMODEL().getNumberOfDays().getDay3() == 1) {
                    Logger.error("FOUR");
                    Intent mIntent = new Intent(mActivity, ScheduleYourDayActivity3.class);
                    mIntent.putExtra("WHEREFROM", "0");
                    startActivity(mIntent);
                } else if (appPreferenceManager.getNEWBTECHAVALIABILITYRESPONSEMODEL().getNumberOfDays().getDay4() == 1) {
                    Logger.error("FOUR");
                    Intent mIntent = new Intent(mActivity, ScheduleYourDayActivity4.class);
                    mIntent.putExtra("WHEREFROM", "0");
                    startActivity(mIntent);
                } else {
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
                        i.putExtra("isFromNotification", isFromNotification);
                        i.putExtra("screenCategory", screenCategory);
                        startActivity(i);
                        finish();
                    } else {
                        if (appPreferenceManager.getSelfieResponseModel() != null && c.getTimeInMillis() < appPreferenceManager.getSelfieResponseModel().getTimeUploaded()) {
                            // switchToActivity(activity, ScheduleYourDayActivity.class, new Bundle());
                            Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
                            i.putExtra("LEAVEINTIMATION", "0");
                            i.putExtra("isFromNotification", isFromNotification);
                            i.putExtra("screenCategory", screenCategory);
                            startActivity(i);
                            finish();
                        } else {
                            Intent i = new Intent(getApplicationContext(), SelfieUploadActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }
                }
            } else {
                Toast.makeText(mActivity, SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void GetDynBtechAvailability() {

        Constants.setAvailabiltity = null;

        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<DynamicBtechAvaliabilityResponseModel> responseCall = apiInterface.GetDynamicBtechAvailability(appPreferenceManager.getLoginResponseModel().getUserID());
        responseCall.enqueue(new Callback<DynamicBtechAvaliabilityResponseModel>() {
            @Override
            public void onResponse(Call<DynamicBtechAvaliabilityResponseModel> call, Response<DynamicBtechAvaliabilityResponseModel> response) {
                MessageLogger.PrintMsg("VersionApi Onsuccess");
                if (response.isSuccessful() && response.body() != null) {
                    DynamicBtechAvaliabilityResponseModel btechAvaliabilityResponseModel = response.body();
                    onDynBtechAvailabilityResponseReceived(btechAvaliabilityResponseModel);
                } else {
                    global.showCustomToast(mActivity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<DynamicBtechAvaliabilityResponseModel> call, Throwable t) {
                MessageLogger.LogDebug("Errror", t.getMessage());

            }
        });
    }

    private void onDynBtechAvailabilityResponseReceived(DynamicBtechAvaliabilityResponseModel btechAvaliabilityResponseModel) {

        if (btechAvaliabilityResponseModel != null && btechAvaliabilityResponseModel.getAllDays() != null) {

            if (appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.NBTTSP_ROLE_ID)) {
                Intent i = new Intent(getApplicationContext(), SelfieUploadActivity.class);
                i.putExtra("LEAVEINTIMATION", "0");
                startActivity(i);
            } else if (appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.LME_ROLE_ID)) {

                Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
                i.putExtra("isFromNotification", isFromNotification);
                i.putExtra("screenCategory", screenCategory);
                i.putExtra("LEAVEINTIMATION", "0");
                startActivity(i);
            } else if (validateDays(btechAvaliabilityResponseModel)) {
                Logger.error("ONEEEE");
                Constants.setAvailabiltity = btechAvaliabilityResponseModel;
                Intent mIntent = new Intent(mActivity, DynamicScheduleYourDayActivity.class);
                mIntent.putExtra("WHEREFROM", "0");
                startActivity(mIntent);
            } /*else if (appPreferenceManager.getNEWBTECHAVALIABILITYRESPONSEMODEL().getNumberOfDays().getDay1() == 1) {
                Logger.error("ONEEEE");
                Intent mIntent = new Intent(mActivity, ScheduleYourDayActivity.class);
                mIntent.putExtra("WHEREFROM", "0");
                startActivity(mIntent);

            } else if (appPreferenceManager.getNEWBTECHAVALIABILITYRESPONSEMODEL().getNumberOfDays().getDay2() == 1) {
                Logger.error("THREEE");
                Intent mIntent = new Intent(mActivity, ScheduleYourDayActivity2.class);
                mIntent.putExtra("WHEREFROM", "0");
                startActivity(mIntent);
            } else if (appPreferenceManager.getNEWBTECHAVALIABILITYRESPONSEMODEL().getNumberOfDays().getDay3() == 1) {
                Logger.error("FOUR");
                Intent mIntent = new Intent(mActivity, ScheduleYourDayActivity3.class);
                mIntent.putExtra("WHEREFROM", "0");
                startActivity(mIntent);
            } else if (appPreferenceManager.getNEWBTECHAVALIABILITYRESPONSEMODEL().getNumberOfDays().getDay4() == 1) {
                Logger.error("FOUR");
                Intent mIntent = new Intent(mActivity, ScheduleYourDayActivity4.class);
                mIntent.putExtra("WHEREFROM", "0");
                startActivity(mIntent);
            }*/ else {
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
                    i.putExtra("isFromNotification", isFromNotification);
                    i.putExtra("screenCategory", screenCategory);
                    startActivity(i);
                    finish();
                } else {
                    if (appPreferenceManager.getSelfieResponseModel() != null && c.getTimeInMillis() < appPreferenceManager.getSelfieResponseModel().getTimeUploaded()) {
                        // switchToActivity(activity, ScheduleYourDayActivity.class, new Bundle());
                        Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
                        i.putExtra("LEAVEINTIMATION", "0");
                        i.putExtra("isFromNotification", isFromNotification);
                        i.putExtra("screenCategory", screenCategory);
                        startActivity(i);
                        finish();
                    } else {

                        Intent i = new Intent(getApplicationContext(), SelfieUploadActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            }


        }
    }

    private boolean validateDays(DynamicBtechAvaliabilityResponseModel btechAvaliabilityResponseModel) {
        if(btechAvaliabilityResponseModel != null){
            if(btechAvaliabilityResponseModel.getAllDays() != null){
                for (int i = 0; i < btechAvaliabilityResponseModel.getAllDays().size(); i++) {
                    if(btechAvaliabilityResponseModel.getAllDays().get(i).getDay() == 1){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void fetchDataForTsp() {

        GetAPIInterface getAPIInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<ArrayList<TSPNBT_AvilModel>> responseCall = getAPIInterface.GetTSP_NBT_Avialability(appPreferenceManager.getLoginResponseModel().getUserID());
        responseCall.enqueue(new Callback<ArrayList<TSPNBT_AvilModel>>() {
            @Override
            public void onResponse(Call<ArrayList<TSPNBT_AvilModel>> call, Response<ArrayList<TSPNBT_AvilModel>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    ArrayList<TSPNBT_AvilModel> tsp_nbt_availabilityArrayList = response.body();
                    if (tsp_nbt_availabilityArrayList.size() > 0) {
                        Intent mIntent = new Intent(mActivity, Tsp_ScheduleYourDayActivity.class);
                        mIntent.putExtra("WHEREFROM", "0");
                        mIntent.putParcelableArrayListExtra(Constants.TSP_NBT_Str, tsp_nbt_availabilityArrayList);
                        startActivity(mIntent);
                    } else {
                        Call_TspScreen();
                    }
                } else {
                    global.showCustomToast(mActivity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TSPNBT_AvilModel>> call, Throwable t) {
                MessageLogger.LogDebug("Errror", t.getMessage());
                global.showCustomToast(mActivity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });

    }

    private void Call_TspScreen() {
        Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
        i.putExtra("LEAVEINTIMATION", "0");
        i.putExtra("isFromNotification", isFromNotification);
        i.putExtra("screenCategory", screenCategory);
        startActivity(i);
        finish();
    }

    private void notificationMapping() {
        // Get new Instance ID token
        String token = FirebaseInstanceId.getInstance().getToken();
        NotificationMappingRequestModel model = new NotificationMappingRequestModel();
        model.setAppName("BTech");
        model.setClient_Id(appPreferenceManager.getLoginResponseModel().getUserID());
        model.setEnterBy(appPreferenceManager.getLoginResponseModel().getUserID());
        model.setToken(token);
        model.setTopic("BTech_AllDevices");
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(getString(R.string.B2B_API_VERSION))).create(PostAPIInterface.class);
        Call<NotificationMappingResponseModel> responseCall = apiInterface.NotificationTokenMappingAPI(model);
        responseCall.enqueue(new Callback<NotificationMappingResponseModel>() {
            @Override
            public void onResponse(Call<NotificationMappingResponseModel> call, Response<NotificationMappingResponseModel> response) {

                if (response.isSuccessful() && response.body() != null) {
                    NotificationMappingResponseModel model1 = response.body();
                    if (model1.getResponseId() != null && model1.getResponseId().equalsIgnoreCase("RES0000")) {
                    } else {
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<NotificationMappingResponseModel> call, Throwable t) {
                MessageLogger.LogDebug("Errror", t.getMessage());

            }
        });
    }

    private void CallLogoutAPI(final VersionControlResponseModel versionAPIResponseModel) {
        LogoutRequestModel model = new LogoutRequestModel();
        model.setDeviceId(DeviceUtils.getDeviceId(mActivity));
        model.setUserId(appPreferenceManager.getLoginResponseModel().getUserID());
        try {
            new LogUserActivityTagging(mActivity, LOGOUT,"");
            PostAPIInterface postAPIInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
            Call<CommonResponseModel> commonResponeModelCall = postAPIInterface.CallLogoutAPI(model);
            global.showProgressDialog(mActivity, "Please wait..", false);
            commonResponeModelCall.enqueue(new Callback<CommonResponseModel>() {
                @Override
                public void onResponse(Call<CommonResponseModel> call, Response<CommonResponseModel> response) {
                    global.hideProgressDialog(mActivity);
                    CommonResponseModel commonResponseModel = response.body();
                }

                @Override
                public void onFailure(Call<CommonResponseModel> call, Throwable t) {
                    global.hideProgressDialog(mActivity);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CloseApp() {
        AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(mActivity);
        alertDialogBuilder
                .setMessage(UnableToConnectMsg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        finishAffinity();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        if (!mActivity.isFinishing()) {
            alertDialog.show();
        }
    }


    public void CallLogOutFromDevice() {
        try {
            global.showCustomToast(mActivity, "Authorization failed, need to Login again...", Toast.LENGTH_SHORT);
            new LogUserActivityTagging(mActivity, LOGOUT,"");
            appPreferenceManager.clearAllPreferences();
            try {
                new DhbDao(mActivity).deleteTablesonLogout();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);

            Intent n = new Intent(mActivity, LoginActivity.class);
            n.setAction(Intent.ACTION_MAIN);
            n.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(n);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}



