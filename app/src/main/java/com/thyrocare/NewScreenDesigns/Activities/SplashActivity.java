package com.thyrocare.NewScreenDesigns.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.thyrocare.NewScreenDesigns.Models.RequestModels.LogoutRequestModel;
import com.thyrocare.NewScreenDesigns.Models.RequestModels.NotificationMappingRequestModel;
import com.thyrocare.NewScreenDesigns.Models.ResponseModel.CommonResponseModel;
import com.thyrocare.NewScreenDesigns.Models.ResponseModel.NotificationMappingResponseModel;
import com.thyrocare.NewScreenDesigns.Models.ResponseModel.TSP_NBT_AvailabilityResponseModel;
import com.thyrocare.NewScreenDesigns.Models.ResponseModel.VersionControlResponseModel;
import com.thyrocare.NewScreenDesigns.Utils.Constants;
import com.thyrocare.R;
import com.thyrocare.Retrofit.GetAPIInterface;
import com.thyrocare.Retrofit.PostAPIInterface;
import com.thyrocare.Retrofit.RetroFit_APIClient;
import com.thyrocare.activity.HomeScreenActivity;
import com.thyrocare.activity.ScheduleYourDayActivity;
import com.thyrocare.activity.ScheduleYourDayActivity2;
import com.thyrocare.activity.ScheduleYourDayActivity3;
import com.thyrocare.activity.ScheduleYourDayActivity4;
import com.thyrocare.activity.SelfieUploadActivity;
import com.thyrocare.activity.Tsp_ScheduleYourDayActivity;
import com.thyrocare.customview.CustomUpdateDailog;
import com.thyrocare.dao.CreateOrUpgradeDbTask;
import com.thyrocare.dao.DbHelper;
import com.thyrocare.dao.DhbDao;
import com.thyrocare.dao.utils.ConnectionDetector;
import com.thyrocare.delegate.CustomUpdateDialogOkButtonOnClickedDelegate;
import com.thyrocare.models.api.response.NewBtechAvaliabilityResponseModel;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.service.LocationUpdateService;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.AppConstants;
import com.thyrocare.utils.app.AppPreferenceManager;
import com.thyrocare.utils.app.DeviceUtils;
import com.thyrocare.utils.app.Global;
import com.thyrocare.utils.app.InputUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thyrocare.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;
import static com.thyrocare.NewScreenDesigns.Utils.ConstantsMessages.UnableToConnectMsg;
import static com.thyrocare.network.AbstractApiModel.B2B;
import static com.thyrocare.network.AbstractApiModel.SERVER_BASE_API_URL;

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
                        fetchVersionControlDetails();
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(mActivity, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                .check();
    }

    private void fetchVersionControlDetails() {
        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, SERVER_BASE_API_URL).create(GetAPIInterface.class);
        Call<VersionControlResponseModel> responseCall = apiInterface.VersionControlAPI();

        responseCall.enqueue(new Callback<VersionControlResponseModel>() {
            @Override
            public void onResponse(Call<VersionControlResponseModel> call, Response<VersionControlResponseModel> response) {
                System.out.println("VersionApi Onsuccess");
                if (response.isSuccessful() && response.body() != null) {
                     versionAPIResponseModel = response.body();
                    onVersionControlResponseReceived(versionAPIResponseModel);
                } else {
                    CloseApp();
                }
            }

            @Override
            public void onFailure(Call<VersionControlResponseModel> call, Throwable t) {
                Log.d("Errror", t.getMessage());
                CloseApp();
            }
        });
    }

    private void onVersionControlResponseReceived(final VersionControlResponseModel versionAPIResponseModel) {

        int APISide_App_Version = versionAPIResponseModel.getAPICurrentVerson() == 0 ? AppConstants.ANDROID_APP_VERSION : versionAPIResponseModel.getAPICurrentVerson();
        if (AppConstants.ANDROID_APP_VERSION  < APISide_App_Version) {
            CustomUpdateDailog cudd = new CustomUpdateDailog(mActivity, new CustomUpdateDialogOkButtonOnClickedDelegate() {
                    @Override
                    public void onUpdateClicked() {
                        if (!InputUtils.isNull(appPreferenceManager.getAPISessionKey())) {
                            ApiCallAsyncTask logoutAsyncTask = new AsyncTaskForRequest(mActivity).getLogoutRequestAsyncTask();
                            logoutAsyncTask.setApiCallAsyncTaskDelegate(new LogoutAsyncTaskDelegateResult());
                            if (cd.isConnectingToInternet()) {
                                logoutAsyncTask.execute(logoutAsyncTask);
                                if (appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.LME_ROLE_ID)) {

                                } else {
                                    CallLogoutAPI(versionAPIResponseModel);
                                }
                            } else {
                                Toast.makeText(mActivity, "Logout functionality is only available in Online Mode", Toast.LENGTH_SHORT).show();
                                finishAffinity();
                            }
                        } else {
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
                        if (AppConstants.ANDROID_APP_VERSION < versionAPIResponseModel.getAPICurrentVerson()) {
//                                System.exit(0);
                            finishAffinity();
                        } else {
                            GoAhead();
                        }
                    }
                });
            cudd.setCancelable(false);
                cudd.show();


        }else{
            GoAhead();
        }
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
                if (!DeviceUtils.isAppIsInBackground(mActivity)){
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
                    GetBtechAvailability();
                }

            }
        }
    }

    private void GetBtechAvailability() {

        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, SERVER_BASE_API_URL).create(GetAPIInterface.class);
        Call<NewBtechAvaliabilityResponseModel> responseCall = apiInterface.GetBtechAvailability(appPreferenceManager.getLoginResponseModel().getUserID());
        global.showProgressDialog(mActivity,"Please wait ..",false);
        responseCall.enqueue(new Callback<NewBtechAvaliabilityResponseModel>() {
            @Override
            public void onResponse(Call<NewBtechAvaliabilityResponseModel> call, Response<NewBtechAvaliabilityResponseModel> response) {
                global.hideProgressDialog();
                System.out.println("VersionApi Onsuccess");
                if (response.isSuccessful() && response.body() != null) {
                    NewBtechAvaliabilityResponseModel btechAvaliabilityResponseModel = response.body();
                    onBtechAvailabilityResponseReceived(btechAvaliabilityResponseModel);
                } else {
                    global.showCustomToast(mActivity,SomethingWentwrngMsg,Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<NewBtechAvaliabilityResponseModel> call, Throwable t) {
                global.hideProgressDialog();
                Log.d("Errror", t.getMessage());

            }
        });
    }

    private void onBtechAvailabilityResponseReceived(NewBtechAvaliabilityResponseModel btechAvaliabilityResponseModel) {

        if (btechAvaliabilityResponseModel != null && btechAvaliabilityResponseModel.getNumberOfDays() != null){
            appPreferenceManager.setNEWBTECHAVALIABILITYRESPONSEMODEL(btechAvaliabilityResponseModel);

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
                    Intent mIntent = new Intent(mActivity, ScheduleYourDayActivity.class);
                    mIntent.putExtra("WHEREFROM", "0");
                    startActivity(mIntent);

                }else if(appPreferenceManager.getNEWBTECHAVALIABILITYRESPONSEMODEL().getNumberOfDays().getDay2()==1){
                    Logger.error("THREEE");
                    Intent mIntent = new Intent(mActivity, ScheduleYourDayActivity2.class);
                    mIntent.putExtra("WHEREFROM", "0");
                    startActivity(mIntent);

                }else if(appPreferenceManager.getNEWBTECHAVALIABILITYRESPONSEMODEL().getNumberOfDays().getDay3()==1){
                    Logger.error("FOUR");
                    Intent mIntent = new Intent(mActivity, ScheduleYourDayActivity3.class);
                    mIntent.putExtra("WHEREFROM", "0");
                    startActivity(mIntent);

                }else if(appPreferenceManager.getNEWBTECHAVALIABILITYRESPONSEMODEL().getNumberOfDays().getDay4()==1){
                    Logger.error("FOUR");
                    Intent mIntent = new Intent(mActivity, ScheduleYourDayActivity4.class);
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

    private void fetchDataForTsp() {

        GetAPIInterface getAPIInterface = RetroFit_APIClient.getInstance().getClient(mActivity, SERVER_BASE_API_URL).create(GetAPIInterface.class);
        Call<ArrayList<TSP_NBT_AvailabilityResponseModel>> responseCall = getAPIInterface.GetTSP_NBT_Avialability(appPreferenceManager.getLoginResponseModel().getUserID());
        responseCall.enqueue(new Callback<ArrayList<TSP_NBT_AvailabilityResponseModel>>() {
            @Override
            public void onResponse(Call<ArrayList<TSP_NBT_AvailabilityResponseModel>> call, Response<ArrayList<TSP_NBT_AvailabilityResponseModel>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    ArrayList<TSP_NBT_AvailabilityResponseModel> tsp_nbt_availabilityArrayList = response.body();
                    if (tsp_nbt_availabilityArrayList.size() > 0) {
                        Intent mIntent = new Intent(mActivity, Tsp_ScheduleYourDayActivity.class);
                        mIntent.putExtra("WHEREFROM", "0");
                        mIntent.putParcelableArrayListExtra(Constants.TSP_NBT_Str, tsp_nbt_availabilityArrayList);
                        startActivity(mIntent);
                    } else {
                        Call_TspScreen();
                    }
                } else {
                    global.showCustomToast(mActivity,SomethingWentwrngMsg,Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TSP_NBT_AvailabilityResponseModel>> call, Throwable t) {
                Log.d("Errror", t.getMessage());
                global.showCustomToast(mActivity,SomethingWentwrngMsg,Toast.LENGTH_LONG);
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
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, B2B).create(PostAPIInterface.class);
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
                Log.d("Errror", t.getMessage());

            }
        });
    }

    private void CallLogoutAPI(final VersionControlResponseModel versionAPIResponseModel) {
        LogoutRequestModel model = new LogoutRequestModel();
        model.setDeviceId(global.getDeviceIMEI(mActivity));
        model.setUserId(appPreferenceManager.getLoginResponseModel().getUserID());
        try {
            PostAPIInterface postAPIInterface = RetroFit_APIClient.getInstance().getClient(mActivity, SERVER_BASE_API_URL).create(PostAPIInterface.class);
            Call<CommonResponseModel> commonResponeModelCall = postAPIInterface.CallLogoutAPI(model);
            global.showProgressDialog(mActivity, "Please wait..",false);
            commonResponeModelCall.enqueue(new Callback<CommonResponseModel>() {
                @Override
                public void onResponse(Call<CommonResponseModel> call, Response<CommonResponseModel> response) {
                    global.hideProgressDialog();
                    CommonResponseModel commonResponseModel = response.body();
                }
                @Override
                public void onFailure(Call<CommonResponseModel> call, Throwable t) {
                    global.hideProgressDialog();
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
        alertDialog.show();
    }

    private class LogoutAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                appPreferenceManager.clearAllPreferences();
                new DhbDao(mActivity).deleteTablesonLogout();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(versionAPIResponseModel.getAppUrl()));
                startActivity(intent);

                finish();
            } else if (statusCode == 401) {
                CallLogOutFromDevice();
            } else {
                Toast.makeText(mActivity, "Failed to Logout", Toast.LENGTH_SHORT).show();
                finishAffinity();
            }
        }

        @Override
        public void onApiCancelled() {

        }
    }

    public void CallLogOutFromDevice() {
        try {
            global.showCustomToast(mActivity, "Authorization failed, need to Login again...", Toast.LENGTH_SHORT);
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



