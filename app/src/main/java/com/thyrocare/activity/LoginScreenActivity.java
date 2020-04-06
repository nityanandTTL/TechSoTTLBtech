
package com.thyrocare.activity;

import com.google.firebase.iid.FirebaseInstanceId;
import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.Controller.NotificationMappingController;
import com.thyrocare.R;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.Controller.DeviceLogOutController;
import com.thyrocare.R;
import com.thyrocare.activity.ForgetPasswordActivity;
import com.thyrocare.activity.HomeScreenActivity;
import com.thyrocare.activity.SelfieUploadActivity;
import com.thyrocare.activity.SplashScreenActivity;
import com.thyrocare.application.ApplicationController;
import com.thyrocare.models.api.request.DownloadDetailsRequestModel;
import com.thyrocare.models.api.request.LoginRequestModel;
import com.thyrocare.models.api.request.NotificationMappingModel;
import com.thyrocare.models.api.response.BtechAvaliabilityResponseModel;
import com.thyrocare.models.api.response.LoginResponseModel;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.network.ResponseParser;
import com.thyrocare.uiutils.AbstractActivity;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.AppConstants;
import com.thyrocare.utils.app.AppPreferenceManager;
import com.thyrocare.utils.app.DeviceUtils;
import com.thyrocare.utils.app.InputUtils;
import com.thyrocare.utils.app.UiUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginScreenActivity extends AbstractActivity implements View.OnClickListener {
    public static final String TAG_LOGIN = "LOGIN_SCREEN_ACTIVITY";

    EditText edt_username_login, edt_password_login;
    TextView tv_forget_password;
    Button btn_login;
    LinearLayout ll_login;
    Activity activity;
    AppConstants appConstants;
    private FirebaseAnalytics mFirebaseAnalytics;
    AppPreferenceManager appPreferenceManager;
    private BtechAvaliabilityResponseModel btechAvaliabilityResponseModel;
    LoginResponseModel loginResponseModel;
    LoginScreenActivity mLoginScreenActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        activity = this;
        mLoginScreenActivity = this;
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(activity);
        appPreferenceManager = new AppPreferenceManager(activity);
        initUi();
        setListeners();
    }


    private void setListeners() {
        tv_forget_password.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        ll_login.setOnClickListener(this);


    }

    private void initUi() {
        edt_username_login = (EditText) findViewById(R.id.username_login);
        edt_password_login = (EditText) findViewById(R.id.password_login);
        tv_forget_password = (TextView) findViewById(R.id.forget_password);
        edt_password_login.setTypeface(Typeface.DEFAULT);
        btn_login = (Button) findViewById(R.id.login_button);
        ll_login = (LinearLayout) findViewById(R.id.ll_login);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.forget_password) {
            Intent intentForgotPassword = new Intent(activity, ForgetPasswordActivity.class);
            startActivity(intentForgotPassword);
        } else if (v.getId() == R.id.login_button) {
            if (validate()) {
                AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);

                LoginRequestModel loginRequestModel = new LoginRequestModel();
                loginRequestModel.setUserName(edt_username_login.getText().toString().trim());
                loginRequestModel.setPassword(edt_password_login.getText().toString().trim());
                loginRequestModel.setGrant_type("password");

                ApiCallAsyncTask logiApiAsyncTask = asyncTaskForRequest.getLoginRequestAsyncTask(loginRequestModel);
                logiApiAsyncTask.setApiCallAsyncTaskDelegate(new LoginApiAsyncTaskDelegateResult());
                if (isNetworkAvailable(activity)) {
                    logiApiAsyncTask.execute(logiApiAsyncTask);

                } else {
                    TastyToast.makeText(activity, getString(R.string.internet_connetion_error), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                    // Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
                }

            }


        } else if (v.getId() == R.id.ll_login) {
            UiUtils.hideSoftKeyboard(this);
        }
    }

    private boolean validate() {
        if (InputUtils.isNull(edt_username_login.getText().toString())) {
            TastyToast.makeText(activity, getString(R.string.enter_user_name), TastyToast.LENGTH_LONG, TastyToast.WARNING);
            // Toast.makeText(activity, "Enter User name", Toast.LENGTH_SHORT).show();

            return false;
        } else if (InputUtils.isNull(edt_password_login.getText().toString())) {
            TastyToast.makeText(activity, getString(R.string.enter_password), TastyToast.LENGTH_LONG, TastyToast.WARNING);
            // Toast.makeText(activity, "Enter Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private class LoginApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (!TextUtils.isEmpty(json)) {
                System.out.println("login - " + json);
            }
            if (statusCode == 200) {
                System.out.println("login - " + json);
                ResponseParser responseParser = new ResponseParser(activity);
                loginResponseModel = new LoginResponseModel();
                loginResponseModel = responseParser.getLoginResponseModel(json, statusCode);
                if (loginResponseModel != null) {
                    if (loginResponseModel.getRole().equals(AppConstants.LME_ROLE_ID)) {
                        setLoginDeviceResponse();
                    } else {
                        CallDeviceIDLoginAPI(loginResponseModel);
                    }
                }

            } else if (statusCode == 400) {
                JSONObject jsonObject = new JSONObject(json);
                TastyToast.makeText(getApplicationContext(), "" + jsonObject.getString("error_description"), TastyToast.LENGTH_LONG, TastyToast.INFO);
            } else {
                JSONObject jsonObject = new JSONObject(json);

                TastyToast.makeText(getApplicationContext(), "" + jsonObject.getString("error_description"), TastyToast.LENGTH_LONG, TastyToast.INFO);
                //  Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            TastyToast.makeText(getApplicationContext(), "" + R.string.network_error, TastyToast.LENGTH_LONG, TastyToast.INFO);
            // Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }

    public void setLoginDeviceResponse() {
        if (loginResponseModel != null) {

            //btech_hub
            appPreferenceManager.setLoginRole(loginResponseModel.getRole());
            appPreferenceManager.setUserID(loginResponseModel.getUserID());

            if (loginResponseModel.getRole().equals(AppConstants.BTECH_ROLE_ID) || loginResponseModel.getRole().equals(AppConstants.HUB_ROLE_ID) || loginResponseModel.getRole().equals(AppConstants.NBT_ROLE_ID)) {//4 is for btech login & 6 is for hub 13 is for NBT

                Logger.error("" + loginResponseModel.getUserID());
                appPreferenceManager.setLoginResponseModel(loginResponseModel);
                appPreferenceManager.setAPISessionKey(loginResponseModel.getAccess_token());
                notificationMapping();
                switchToActivity(activity, SplashScreenActivity.class, new Bundle());

            } else if (loginResponseModel.getRole().equals(AppConstants.TSP_ROLE_ID)) {//this is for tsp
                appPreferenceManager.setLoginResponseModel(loginResponseModel);
                appPreferenceManager.setAPISessionKey(loginResponseModel.getAccess_token());
                notificationMapping();
                switchToActivity(activity, SplashScreenActivity.class, new Bundle());
            } else if (loginResponseModel.getRole().equals(AppConstants.NBTTSP_ROLE_ID)) {
                appPreferenceManager.setLoginResponseModel(loginResponseModel);
                appPreferenceManager.setAPISessionKey(loginResponseModel.getAccess_token());
                notificationMapping();
                //jai
                Intent i = new Intent(getApplicationContext(), SelfieUploadActivity.class);
                i.putExtra("LEAVEINTIMATION", "0");
                startActivity(i);
            } else if (loginResponseModel.getRole().equals(AppConstants.LME_ROLE_ID)) {
                appPreferenceManager.setLoginResponseModel(loginResponseModel);
                appPreferenceManager.setAPISessionKey(loginResponseModel.getAccess_token());
                notificationMapping();
                switchToActivity(activity, SplashScreenActivity.class, new Bundle());
            } else {
                TastyToast.makeText(activity, getString(R.string.pls_use_valid_btech_credential_to_log_in), TastyToast.LENGTH_LONG, TastyToast.WARNING);
            }


        }

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, appPreferenceManager.getBtechID());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, appPreferenceManager.getUserID());
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        DownloadDetailsRequestModel downloadDetailsRequestModel = new DownloadDetailsRequestModel();
        downloadDetailsRequestModel.setVersion(AppConstants.ANDROID_APP_VERSION);
        downloadDetailsRequestModel.setBtechID(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));

        AsyncTaskForRequest asyncTaskForRequest2 = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask setDownloadingDetailApiAsyncTask = asyncTaskForRequest2.getDownloadDetails(downloadDetailsRequestModel);
        setDownloadingDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new DownloadApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            setDownloadingDetailApiAsyncTask.execute(setDownloadingDetailApiAsyncTask);
        } else {
            TastyToast.makeText(activity, getString(R.string.internet_connetion_error), TastyToast.LENGTH_LONG, TastyToast.ERROR);
            // Toast.makeText(activity, R.string.internet_connetion_error, LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void CallDeviceIDLoginAPI(LoginResponseModel loginResponseModel) {
        try {
            if (!InputUtils.isNull(loginResponseModel.getUserID())) {
                String device_id = "";
                device_id = DeviceUtils.getDeviceId(activity);

                if (ApplicationController.mDeviceLogOutController != null) {
                    ApplicationController.mDeviceLogOutController = null;
                }

                ApplicationController.mDeviceLogOutController = new DeviceLogOutController(mLoginScreenActivity);
                ApplicationController.mDeviceLogOutController.CallLogInDevice(loginResponseModel.getUserID(), device_id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class DownloadApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {

        }

        @Override
        public void onApiCancelled() {

        }
    }

    public void notificationMapping() {


        NotificationMappingModel notificationMappingModel = new NotificationMappingModel();

        String clientID = appPreferenceManager.getLoginResponseModel().getUserID();
        String token = FirebaseInstanceId.getInstance().getToken();

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
            Log.e("shami -- ", "notificationMapping: Token Generated");
        } else {
            Log.e("shami -- ", "notificationMapping: Token not generated");
        }
    }
}
