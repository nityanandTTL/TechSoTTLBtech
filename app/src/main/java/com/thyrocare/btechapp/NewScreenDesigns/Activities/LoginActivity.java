package com.thyrocare.btechapp.NewScreenDesigns.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.btechapp.BuildConfig;
import com.thyrocare.btechapp.Controller.PostTokenController;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.BtechWiseVersionTrackerRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.NotificationMappingRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.NotificationMappingResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.LogUserActivityTagging;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.ForgetPasswordActivity;
import com.thyrocare.btechapp.activity.SelfieUploadActivity;
import com.thyrocare.btechapp.dao.utils.ConnectionDetector;
import com.thyrocare.btechapp.models.api.request.NotificationTokenRequestModel;
import com.thyrocare.btechapp.models.api.request.Post_DeviceID;
import com.thyrocare.btechapp.models.api.response.LoginDeviceResponseModel;
import com.thyrocare.btechapp.models.api.response.LoginResponseModel;
import com.thyrocare.btechapp.utils.app.AppConstants;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.DeviceUtils;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;
import com.thyrocare.btechapp.utils.app.InstallationID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.CheckInternetConnectionMsg;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.INVALID_LOG;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SUCCESS_LOGIN;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.VALID_BTECH_CREDENTIAL_ALERT;
import static com.thyrocare.btechapp.utils.app.BundleConstants.LOGIN;


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
    private ImageView img_password_toggle_id;
    private boolean showpassword = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mActivity = LoginActivity.this;
        appPreferenceManager = new AppPreferenceManager(mActivity);
        alertDialogBuilder = new AlertDialog.Builder(mActivity);
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
//        img_password_toggle_id = (ImageView) findViewById(R.id.img_password_toggle_id);
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

        /*img_password_toggle_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showpassword){
                    showpassword = false;
                    edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    img_password_toggle_id.setImageResource(R.drawable.close_eye_icon);
                    edtPassword.setSelection(edtPassword.getText().length());
                }else{
                    showpassword = true;
                    edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    img_password_toggle_id.setImageResource(R.drawable.icons8_eye_32);
                    edtPassword.setSelection(edtPassword.getText().length());
                }
            }
        });*/
    }

    private void CallLoginFunction() {
        UserID = edtUserID.getText().toString().trim();
        password = edtPassword.getText().toString().trim();

        if (StringUtils.isNull(UserID) || StringUtils.isNull(password)) {
            String message = "";
            if (StringUtils.isNull(UserID) && StringUtils.isNull(password)) {
                message = "Please enter Username and password to login.";
                edtUserID.requestFocus();
            } else if (StringUtils.isNull(UserID)) {
                message = "Plese enter Username";
                edtUserID.requestFocus();
            } else if (StringUtils.isNull(password)) {
                message = "Please enter password";
                edtPassword.requestFocus();
            }
            globalclass.showalert_OK(message, mActivity);

        } else {
            if (cd.isConnectingToInternet()) {
                VerifyLogin();
            } else {
                globalclass.showCustomToast(mActivity, CheckInternetConnectionMsg, Toast.LENGTH_LONG);
            }
        }
    }

    private void VerifyLogin() {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<LoginResponseModel> responseCall = apiInterface.CallLoginAPI(UserID + "|" + DeviceUtils.getDeviceId(mActivity), password, "password");
        globalclass.showProgressDialog(mActivity, "Please wait..", false);
        responseCall.enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                globalclass.hideProgressDialog(mActivity);
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponseModel responseModel = response.body();
                    if (/*BuildConfig.DEBUG ||*/ Global.checkLogin(responseModel.getCompanyName())) {
                        OnLoginResponseReceived(responseModel);
                    } else {
                        if (responseModel.getRole().equals(AppConstants.LME_ROLE_ID)) {
//                        globalclass.showCustomToast(mActivity, SUCCESS_LOGIN, Toast.LENGTH_LONG);
                            OnLoginResponseReceived(responseModel);
                        } else {
                        /*if (responseModel != null && !StringUtils.isNull(responseModel.getRespId()) && responseModel.getRespId().equalsIgnoreCase("1")){
                            new LogUserActivityTagging(mActivity, LOGIN);
                            OnLoginResponseReceived(responseModel);
                        }else{
                            TastyToast.makeText(mActivity, !InputUtils.isNull(responseModel.getRespMessage()) ? responseModel.getRespMessage() :SomethingWentwrngMsg, TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }*/

                            CallDeviceIDLoginAPI(responseModel);
                        }
                    }

                } else {
                    globalclass.showCustomToast(mActivity, INVALID_LOG, Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                globalclass.hideProgressDialog(mActivity);
                globalclass.showCustomToast(mActivity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });
    }

    private void CallDeviceIDLoginAPI(final LoginResponseModel responseModel) {

        Post_DeviceID post_deviceID = new Post_DeviceID();
        post_deviceID.setUserId(responseModel.getUserID());
        post_deviceID.setDeviceId(DeviceUtils.getDeviceId(mActivity));
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<LoginDeviceResponseModel> responseCall = apiInterface.PostLoginUserDeviceAPI(post_deviceID);
        globalclass.showProgressDialog(mActivity, "Please wait..", false);
        responseCall.enqueue(new Callback<LoginDeviceResponseModel>() {
            @Override
            public void onResponse(Call<LoginDeviceResponseModel> call, Response<LoginDeviceResponseModel> response) {
                globalclass.hideProgressDialog(mActivity);
                if (response.isSuccessful() && response.body() != null) {
                    LoginDeviceResponseModel model = response.body();
                    if (model.getRespId() == 1) {
                        OnLoginResponseReceived(responseModel);
                    } else {
                        TastyToast.makeText(mActivity, !InputUtils.isNull(model.getRespMessage()) ? model.getRespMessage() : SomethingWentwrngMsg, TastyToast.LENGTH_LONG, TastyToast.ERROR);
                    }
                } else {
                    globalclass.showCustomToast(mActivity, INVALID_LOG, Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<LoginDeviceResponseModel> call, Throwable t) {
                globalclass.hideProgressDialog(mActivity);
                globalclass.showCustomToast(mActivity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });

    }

    private void OnLoginResponseReceived(LoginResponseModel responseModel) {

        new LogUserActivityTagging(mActivity, LOGIN, "");
        if (responseModel != null) {
            //btech_hub
            appPreferenceManager.setLoginRole(responseModel.getRole());
            appPreferenceManager.setUserID(responseModel.getUserID());
            appPreferenceManager.setAPISessionKey(responseModel.getAccess_token());
            appPreferenceManager.setLoginResponseModel(responseModel);
            CallBtechWiseVersionTrackerAPI();
            PostToken();
            if (appPreferenceManager.getLoginResponseModel().getRole().equals(Constants.BTECH_ROLE_ID) || appPreferenceManager.getLoginResponseModel().getRole().equals(Constants.HUB_ROLE_ID) || appPreferenceManager.getLoginResponseModel().getRole().equals(Constants.NBT_ROLE_ID)) {//4 is for btech login & 6 is for hub 13 is for NBT
//                globalclass.showCustomToast(mActivity, SUCCESS_LOGIN, Toast.LENGTH_LONG);
                notificationMapping();
                Intent mIntent = new Intent(mActivity, SplashActivity.class);
                startActivity(mIntent);
                finish();
            } else if (appPreferenceManager.getLoginResponseModel().getRole().equals(Constants.TSP_ROLE_ID)) {//this is for tsp
//                globalclass.showCustomToast(mActivity, SUCCESS_LOGIN, Toast.LENGTH_LONG);
                notificationMapping();
                Intent mIntent = new Intent(mActivity, SplashActivity.class);
                startActivity(mIntent);
                finish();
            } else if (appPreferenceManager.getLoginResponseModel().getRole().equals(Constants.NBTTSP_ROLE_ID)) {
//                globalclass.showCustomToast(mActivity, SUCCESS_LOGIN, Toast.LENGTH_LONG);
//                Toast.makeText(mActivity, SUCCESS_LOGIN, Toast.LENGTH_SHORT).show();
                notificationMapping();
//                selfieUploadBottomSheet();
                Intent i = new Intent(mActivity, SelfieUploadActivity.class);
                i.putExtra("LEAVEINTIMATION", "0");
                startActivity(i);
                finish();
            } else if (appPreferenceManager.getLoginResponseModel().getRole().equals(Constants.LME_ROLE_ID)) {

                globalclass.showCustomToast(mActivity, "Your are not authorized to use this App.\nUser type found : LME", Toast.LENGTH_LONG);
                /*notificationMapping();
                Intent mIntent = new Intent(mActivity, SplashActivity.class);
                startActivity(mIntent);
                finish();*/
            } else {
                globalclass.showCustomToast(mActivity, VALID_BTECH_CREDENTIAL_ALERT, Toast.LENGTH_LONG);
            }
        }
    }

    private void selfieUploadBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottomsheet_selfie);

        Button btn_takePhoto = findViewById(R.id.btn_takePhoto);

        btn_takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    private void PostToken() {
        try {
            SharedPreferences pref_firebaseToken = mActivity.getSharedPreferences("Firebase_Pref", 0);
            String IsTokensendToServer = pref_firebaseToken.getString("Token", "");
            NotificationTokenRequestModel notificationTokenRequestModel = new NotificationTokenRequestModel();
            notificationTokenRequestModel.setAppId("" + Constants.APP_ID_FOR_TOKEN);
            notificationTokenRequestModel.setDeviceId("" + InstallationID.id(mActivity));
            notificationTokenRequestModel.setNotificationId("" + IsTokensendToServer);
            notificationTokenRequestModel.setUserId("" + appPreferenceManager.getLoginResponseModel().getUserID());
            PostTokenController postTokenController = new PostTokenController(this);
            postTokenController.CallAPI(notificationTokenRequestModel);
        } catch (Exception e) {
            e.printStackTrace();
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
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.BtechWiseVersionTrackerAPI(model);
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null && response.body().contains("SUCCESS")) {
                    MessageLogger.info(mActivity, "Track Btech Version  - Success");
                } else {
                    MessageLogger.info(mActivity, "Track Btech Version  - Failure");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

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


    @Override
    protected void onResume() {

        super.onResume();
    }
}
