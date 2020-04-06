package com.thyrocare.NewScreenDesigns.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.NewScreenDesigns.Models.RequestModels.BtechWiseVersionTrackerRequestModel;
import com.thyrocare.NewScreenDesigns.Models.RequestModels.NotificationMappingRequestModel;
import com.thyrocare.NewScreenDesigns.Models.ResponseModel.NotificationMappingResponseModel;
import com.thyrocare.NewScreenDesigns.Utils.Constants;
import com.thyrocare.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.R;
import com.thyrocare.Retrofit.PostAPIInterface;
import com.thyrocare.Retrofit.RetroFit_APIClient;
import com.thyrocare.activity.ForgetPasswordActivity;
import com.thyrocare.activity.HomeScreenActivity;
import com.thyrocare.activity.SelfieUploadActivity;
import com.thyrocare.dao.utils.ConnectionDetector;
import com.thyrocare.models.api.request.Post_DeviceID;
import com.thyrocare.models.api.response.LoginDeviceResponseModel;
import com.thyrocare.models.api.response.LoginResponseModel;
import com.thyrocare.utils.app.AppConstants;
import com.thyrocare.utils.app.AppPreferenceManager;
import com.thyrocare.utils.app.Global;
import com.thyrocare.utils.app.InputUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thyrocare.NewScreenDesigns.Utils.ConstantsMessages.CheckInternetConnectionMsg;
import static com.thyrocare.NewScreenDesigns.Utils.ConstantsMessages.INVALID_LOG;
import static com.thyrocare.NewScreenDesigns.Utils.ConstantsMessages.SUCCESS_LOGIN;
import static com.thyrocare.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;
import static com.thyrocare.NewScreenDesigns.Utils.ConstantsMessages.VALID_BTECH_CREDENTIAL_ALERT;
import static com.thyrocare.network.AbstractApiModel.B2B;
import static com.thyrocare.network.AbstractApiModel.SERVER_BASE_API_URL;

public class LoginActivity extends AppCompatActivity {

    Activity mActivity;
    private EditText edtUserID, edtPassword;
    private Button btn_login;
    private AlertDialog.Builder alertDialogBuilder;
    private String UserID, password;
    private Cursor c1;
    private String PasswordtoVerify = "";
    private SharedPreferences prefs_user;
    Global globalclass;
    private TextView forget_password;
    ConnectionDetector cd;
    private AppPreferenceManager appPreferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mActivity = LoginActivity.this;
        appPreferenceManager = new AppPreferenceManager(mActivity);
        globalclass = new Global(mActivity);
        cd = new ConnectionDetector(mActivity);

        initview();
        initlistener();

    }



    private void initview() {
        edtUserID = (EditText) findViewById(R.id.edtUserID);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btn_login = (Button) findViewById(R.id.btn_login);
        forget_password = (TextView) findViewById(R.id.forget_password);
    }

    private void initlistener() {

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallLoginFunction();
            }
        });

        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentForgotPassword = new Intent(mActivity, ForgetPasswordActivity.class);
                startActivity(intentForgotPassword);
            }
        });

    }

    private void CallLoginFunction() {

        UserID = edtUserID.getText().toString().trim();
        password = edtPassword.getText().toString().trim();

        alertDialogBuilder = new AlertDialog.Builder(mActivity);
        if (StringUtils.isNull(UserID) || StringUtils.isNull(password)) {
            String message = "Please enter userID and password to login.";
            if (StringUtils.isNull(UserID)) {
                message = "Plese enter UserID";
                edtUserID.requestFocus();
            } else if (StringUtils.isNull(UserID)) {
                message = "Please enter password";
                edtPassword.requestFocus();
            }
            globalclass.showalert_OK(message,mActivity);

        } else {
            if (cd.isConnectingToInternet()){
                VerifyLogin();
            }else{
                globalclass.showCustomToast(mActivity,CheckInternetConnectionMsg, Toast.LENGTH_LONG);
            }
        }
    }

    private void VerifyLogin() {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, SERVER_BASE_API_URL).create(PostAPIInterface.class);
        Call<LoginResponseModel> responseCall = apiInterface.CallLoginAPI(UserID,password,"password");
        globalclass.showProgressDialog(mActivity, "Please wait..",false);
        responseCall.enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                globalclass.hideProgressDialog();
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponseModel responseModel = response.body();
                    globalclass.showCustomToast(mActivity,SUCCESS_LOGIN,Toast.LENGTH_LONG);
                    if (responseModel.getRole().equals(AppConstants.LME_ROLE_ID)) {
                        OnLoginResponseReceived(responseModel);
                    } else {
                        CallDeviceIDLoginAPI(responseModel);
                    }

                } else {
                    globalclass.showCustomToast(mActivity,INVALID_LOG,Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                globalclass.hideProgressDialog();
                globalclass.showCustomToast(mActivity,SomethingWentwrngMsg,Toast.LENGTH_LONG);

            }
        });

    }

    private void CallDeviceIDLoginAPI(final LoginResponseModel responseModel) {

        Post_DeviceID post_deviceID = new Post_DeviceID();
        post_deviceID.setUserId(responseModel.getUserID());
        post_deviceID.setDeviceId( globalclass.getDeviceIMEI(mActivity));
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, SERVER_BASE_API_URL).create(PostAPIInterface.class);
        Call<LoginDeviceResponseModel> responseCall = apiInterface.PostLoginUserDeviceAPI(post_deviceID);
        globalclass.showProgressDialog(mActivity, "Please wait..",false);
        responseCall.enqueue(new Callback<LoginDeviceResponseModel>() {
            @Override
            public void onResponse(Call<LoginDeviceResponseModel> call, Response<LoginDeviceResponseModel> response) {
                globalclass.hideProgressDialog();
                if (response.isSuccessful() && response.body() != null) {
                    LoginDeviceResponseModel model = response.body();
                    if (model.getRespId() == 1) {
                        OnLoginResponseReceived(responseModel);
                    } else {
                        TastyToast.makeText(mActivity, !InputUtils.isNull(model.getRespMessage()) ? model.getRespMessage() :SomethingWentwrngMsg, TastyToast.LENGTH_LONG, TastyToast.ERROR);
                    }

                } else {
                    globalclass.showCustomToast(mActivity,INVALID_LOG,Toast.LENGTH_LONG);
                }
            }
            @Override
            public void onFailure(Call<LoginDeviceResponseModel> call, Throwable t) {
                globalclass.hideProgressDialog();
                globalclass.showCustomToast(mActivity,SomethingWentwrngMsg,Toast.LENGTH_LONG);
            }
        });

    }

    private void OnLoginResponseReceived(LoginResponseModel responseModel) {
        if (responseModel != null){
            //btech_hub
            appPreferenceManager.setLoginRole(responseModel.getRole());
            appPreferenceManager.setUserID(responseModel.getUserID());
            appPreferenceManager.setAPISessionKey(responseModel.getAccess_token());
            appPreferenceManager.setLoginResponseModel(responseModel);
            CallBtechWiseVersionTrackerAPI();
            if (appPreferenceManager.getLoginResponseModel().getRole().equals(Constants.BTECH_ROLE_ID) || appPreferenceManager.getLoginResponseModel().getRole().equals(Constants.HUB_ROLE_ID) || appPreferenceManager.getLoginResponseModel().getRole().equals(Constants.NBT_ROLE_ID)) {//4 is for btech login & 6 is for hub 13 is for NBT
                notificationMapping();
                Intent mIntent = new Intent(mActivity, SplashActivity.class);
                startActivity(mIntent);
            } else if (appPreferenceManager.getLoginResponseModel().getRole().equals(Constants.TSP_ROLE_ID)) {//this is for tsp
                notificationMapping();
                Intent mIntent = new Intent(mActivity, SplashActivity.class);
                startActivity(mIntent);
            } else if (appPreferenceManager.getLoginResponseModel().getRole().equals(Constants.NBTTSP_ROLE_ID)) {
                notificationMapping();
                Intent i = new Intent(mActivity, SelfieUploadActivity.class);
                i.putExtra("LEAVEINTIMATION", "0");
                startActivity(i);
            } else if (appPreferenceManager.getLoginResponseModel().getRole().equals(Constants.LME_ROLE_ID)) {
                notificationMapping();
                Intent mIntent = new Intent(mActivity, SplashActivity.class);
                startActivity(mIntent);
            } else {
                globalclass.showCustomToast(mActivity,VALID_BTECH_CREDENTIAL_ALERT,Toast.LENGTH_LONG);
            }
        }
    }

    private void CallBtechWiseVersionTrackerAPI() {
        int appLevelVersionCode = 0;
        try {
            appLevelVersionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        BtechWiseVersionTrackerRequestModel model = new BtechWiseVersionTrackerRequestModel();
        model.setBtechID(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
        model.setVersion(appLevelVersionCode);
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, SERVER_BASE_API_URL).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.BtechWiseVersionTrackerAPI(model);
        globalclass.showProgressDialog(mActivity, "Please wait..",false);
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                globalclass.hideProgressDialog();
                if (response.isSuccessful() && response.body() != null && response.body().contains("SUCCESS")) {
                    MessageLogger.info(mActivity,"Track Btech Version  - Success");
                } else {
                    MessageLogger.info(mActivity,"Track Btech Version  - Failure");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                globalclass.hideProgressDialog();

            }
        });

    }

    private void show_dialog() {
        AlertDialog alertDialog = alertDialogBuilder.create();
        if (!mActivity.isFinishing()) {
            alertDialog.show();
        }
    }

    private void notificationMapping() {

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


    @Override
    protected void onResume() {

        super.onResume();
    }
}
