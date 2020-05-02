package com.thyrocare.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.thyrocare.application.ApplicationController;
import com.thyrocare.models.api.request.DownloadDetailsRequestModel;
import com.thyrocare.models.api.request.LoginRequestModel;
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
import com.thyrocare.utils.app.Global;
import com.thyrocare.utils.app.InputUtils;
import com.thyrocare.utils.app.UiUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginScreenActivity_old extends AbstractActivity implements View.OnClickListener {

    EditText edt_username_login, edt_password_login;
    TextView tv_forget_password;
    Button btn_login,btn_login_otp;
    LinearLayout ll_login;
    Activity activity;
    AppConstants appConstants;
    private FirebaseAnalytics mFirebaseAnalytics;
    AppPreferenceManager appPreferenceManager;
    private BtechAvaliabilityResponseModel btechAvaliabilityResponseModel;
    LoginResponseModel loginResponseModel;
    LoginScreenActivity_old mLoginScreenActivity_old;

    CheckBox ch_otp;
    LinearLayout password_block;
    TextView forget_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen1);
        activity = this;
        mLoginScreenActivity_old = this;
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

        ch_otp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(ch_otp.isChecked()){
                    // CustomDialogClass cdd=new CustomDialogClass(LoginScreenActivity_old.this);
                    //   cdd.show();
                    forget_password.setVisibility(View.GONE);
                    password_block.setVisibility(View.GONE);
                    btn_login.setVisibility(View.GONE);
                    btn_login_otp.setVisibility(View.VISIBLE);
                }else {
                    forget_password.setVisibility(View.VISIBLE);
                    password_block.setVisibility(View.VISIBLE);
                    btn_login.setVisibility(View.VISIBLE);
                    btn_login_otp.setVisibility(View.GONE);
                }
            }
        });
        btn_login_otp.setOnClickListener(this);


    }

    private void initUi() {
        edt_username_login = (EditText) findViewById(R.id.username_login);
        edt_password_login = (EditText) findViewById(R.id.password_login);
        tv_forget_password = (TextView) findViewById(R.id.forget_password);
        edt_password_login.setTypeface(Typeface.DEFAULT);
        btn_login = (Button) findViewById(R.id.login_button);
        btn_login_otp = (Button) findViewById(R.id.btn_login_otp);
        ll_login = (LinearLayout) findViewById(R.id.ll_login);

        ch_otp=(CheckBox)findViewById(R.id.ch_otp);
        password_block=(LinearLayout)findViewById(R.id.password_block);
        forget_password=(TextView) findViewById(R.id.forget_password);
        /*final ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (toggleButton.getText().toString().equals("ON")) {
                    BundleConstants.Flag_facedetection = true;
                } else if (toggleButton.getText().toString().equals("OFF")) {
                    BundleConstants.Flag_facedetection = false;
                }

                StringBuilder result = new StringBuilder();
                result.append("ToggleButton1 : ").append(toggleButton.getText());
                //Displaying the message in toast
                Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
            }
        });*/
    }

    /*private void showSuccess(String title, String message) {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(""+title)
                .setContentText(""+message)
                .show();

    }
    private void showError(String title, String message) {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(""+title)
                .setContentText(""+message)
                .show();
    }
    private void showWarning(String title, String message) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Won't be able to recover this file!")
                .setConfirmText("OK")
                .show();
    }
    */
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
        }else if (v.getId() == R.id.btn_login_otp) {
            CustomDialogClass cdd=new CustomDialogClass(LoginScreenActivity_old.this);
            cdd.show();
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

    private void registerChat() {
        Logger.error("in register chat");
        final ProgressDialog pd = new ProgressDialog(LoginScreenActivity_old.this);
        pd.setMessage("Loading...");
        pd.show();
        String pwd = edt_username_login.getText().toString();
        appPreferenceManager.setChatPassword(pwd);
        String url = "https://finalchat-df79b.firebaseio.com/users.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Firebase reference = new Firebase("https://finalchat-df79b.firebaseio.com/users");

                if (s.equals("null")) {
                    reference.child(appPreferenceManager.getLoginResponseModel().getUserName()).child("password").setValue(edt_username_login.getText().toString());
                    //  TastyToast.makeText(getApplicationContext(), ""+json, TastyToast.LENGTH_LONG, TastyToast.INFO);
                    Toast.makeText(LoginScreenActivity_old.this, "registration successful", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        JSONObject obj = new JSONObject(s);

                        // if (!obj.has(appPreferenceManager.getLoginResponse().getUserName())) {

                        reference.child(appPreferenceManager.getLoginResponseModel().getUserName()).child("password").setValue(edt_username_login.getText().toString());
                        Toast.makeText(LoginScreenActivity_old.this, "registration successful", Toast.LENGTH_LONG).show();
                        appPreferenceManager.setChatRegister(true);
                        Logger.error("about to start activity");
                        switchToActivity(activity, HomeScreenActivity.class, new Bundle());
                        Logger.error("registration successful");
                      /*  } else {
                            Toast.makeText(LoginScreenActivity_old.this, "username already exists", Toast.LENGTH_LONG).show();
                        }*/

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                pd.dismiss();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
                pd.dismiss();
            }
        });

        RequestQueue rQueue = Global.setVolleyReq(LoginScreenActivity_old.this);
        rQueue.add(request);
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
                //switchToActivity(activity, SelfieUploadActivity.class, new Bundle());
                switchToActivity(activity, SplashScreenActivity.class, new Bundle());
            } else if (loginResponseModel.getRole().equals(AppConstants.TSP_ROLE_ID)) {//this is for tsp
                appPreferenceManager.setLoginResponseModel(loginResponseModel);
                appPreferenceManager.setAPISessionKey(loginResponseModel.getAccess_token());
                       /* Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
                        i.putExtra("LEAVEINTIMATION", "0");
                        startActivity(i);*/

                switchToActivity(activity, SplashScreenActivity.class, new Bundle());
            } else if (loginResponseModel.getRole().equals(AppConstants.NBTTSP_ROLE_ID)) {
                appPreferenceManager.setLoginResponseModel(loginResponseModel);
                appPreferenceManager.setAPISessionKey(loginResponseModel.getAccess_token());
                //jai
                Intent i = new Intent(getApplicationContext(), SelfieUploadActivity.class);
                // Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);


                i.putExtra("LEAVEINTIMATION", "0");
                startActivity(i);
            } else if (loginResponseModel.getRole().equals(AppConstants.LME_ROLE_ID)) {
                appPreferenceManager.setLoginResponseModel(loginResponseModel);
                appPreferenceManager.setAPISessionKey(loginResponseModel.getAccess_token());

                switchToActivity(activity, SplashScreenActivity.class, new Bundle());
            } else {
                TastyToast.makeText(activity, getString(R.string.pls_use_valid_btech_credential_to_log_in), TastyToast.LENGTH_LONG, TastyToast.WARNING);
                //   Toast.makeText(activity, "Please use valid BTECH credentials to log in", Toast.LENGTH_SHORT).show();
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

    private class LoginApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
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

            }else if (statusCode == 400) {
                TastyToast.makeText(getApplicationContext(), "" + json, TastyToast.LENGTH_LONG, TastyToast.INFO);
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

    @SuppressLint("MissingPermission")
    private void CallDeviceIDLoginAPI(LoginResponseModel loginResponseModel) {
        try {
            if (!InputUtils.isNull(loginResponseModel.getUserID())) {
                String device_id = "";
                device_id = DeviceUtils.getDeviceId(activity);

                if (ApplicationController.mDeviceLogOutController != null) {
                    ApplicationController.mDeviceLogOutController = null;
                }

                ApplicationController.mDeviceLogOutController = new DeviceLogOutController(mLoginScreenActivity_old);
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
    private class CustomDialogClass extends Dialog
    {

        public Activity c;
        public Dialog d;
        public Button yes, no;

        public CustomDialogClass(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_login_otp);

        }


    }
}
