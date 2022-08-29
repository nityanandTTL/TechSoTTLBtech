package com.thyrocare.btechapp.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.btechapp.Controller.DeviceLogOutController;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.LoginActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.LogUserActivityTagging;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.HomeScreenActivity;

import application.ApplicationController;
import retrofit2.Call;
import retrofit2.Callback;

import com.thyrocare.btechapp.dao.DhbDao;
import com.thyrocare.btechapp.models.api.request.ChangePasswordRequestModel;


import com.thyrocare.btechapp.uiutils.AbstractFragment;
import com.thyrocare.btechapp.utils.app.AppConstants;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.CommonUtils;
import com.thyrocare.btechapp.utils.app.DeviceUtils;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.PLEASE_WAIT;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SOMETHING_WENT_WRONG;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;
import static com.thyrocare.btechapp.utils.api.NetworkUtils.isNetworkAvailable;
import static com.thyrocare.btechapp.utils.app.BundleConstants.LOGOUT;

public class ChangePasswordFragment extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG_FRAGMENT = ChangePasswordFragment.class.getSimpleName();
    String regexp = ".{6,12}";
    Activity activity;
    Global globalclass;
    TextView tv_toolbar;
    ImageView iv_back, iv_home;
    AppPreferenceManager appPreferenceManager;
    private EditText edt_old_password, edt_new_password, edt_confirm_password;
    private Button btn_reset_password;
    private DhbDao dhbDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_reset_password_modified);

        activity = this;
        globalclass = new Global(activity);

        appPreferenceManager = new AppPreferenceManager(activity);
//        activity.isOnHome = false;
        dhbDao = new DhbDao(activity);
        initUI();
        setListeners();
    }

    private void setListeners() {
        btn_reset_password.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        iv_home.setOnClickListener(this);
    }


    private void initUI() {
        edt_old_password = findViewById(R.id.edt_old_password);
        edt_new_password = findViewById(R.id.edt_new_password);
        edt_confirm_password = findViewById(R.id.edt_confirm_password);
        btn_reset_password = findViewById(R.id.btn_reset_password);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        iv_back = findViewById(R.id.iv_back);
        iv_home = findViewById(R.id.iv_home);
        tv_toolbar.setText("Change Password");
    }


    private boolean validate() {
        if (!edt_old_password.getText().toString().matches(regexp)) {
            edt_old_password.setError(getString(R.string.password_criteria));
            edt_old_password.requestFocus();
            return false;
        } else if (!edt_new_password.getText().toString().matches(regexp)) {
            edt_new_password.setError(getString(R.string.password_criteria));
            edt_new_password.requestFocus();
            return false;
        } else if (!edt_confirm_password.getText().toString().matches(regexp)) {
            edt_confirm_password.setError(getString(R.string.password_criteria));
            edt_confirm_password.requestFocus();
            return false;
        } else if (!edt_new_password.getText().toString().equals(edt_confirm_password.getText().toString())) {
            edt_confirm_password.setError(getString(R.string.password_do_not_match));
            edt_confirm_password.requestFocus();
            return false;
        } else if (edt_old_password.getText().toString().equals(edt_new_password.getText().toString())) {
            edt_new_password.setError(getString(R.string.old_pwd_and_new_pwd_should_not_be_same));
            edt_new_password.requestFocus();
            return false;
        }
        return true;
    }

    private void changePasswordApi() {
        ChangePasswordRequestModel changePasswordRequestModel = new ChangePasswordRequestModel();
        changePasswordRequestModel.setOldPassword(edt_old_password.getText().toString());
        changePasswordRequestModel.setNewPassword(edt_new_password.getText().toString());
        changePasswordRequestModel.setConfirmPassword(edt_confirm_password.getText().toString());
        if (isNetworkAvailable(activity)) {
            CallGetChangePasswordRequestApi(changePasswordRequestModel);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_reset_password) {
            if (validate()) {
                changePasswordApi();
            }
        } else if (v.getId() == R.id.iv_back) {
            finish();

        } else if (v.getId() == R.id.iv_home) {
            finish();
        }
    }

    private void CallGetChangePasswordRequestApi(final ChangePasswordRequestModel changePasswordRequestModel) {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallGetChangePasswordRequestApi(changePasswordRequestModel);
        globalclass.showProgressDialog(activity, "Please wait..");
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                globalclass.hideProgressDialog(activity);
                if (response.isSuccessful()) {
                    androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder;
                    alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(activity);
                    alertDialogBuilder
                            .setMessage(R.string.password_changed_successfully)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    if (isNetworkAvailable(activity)) {
                                        CallLogoutRequestApi();
                                        if (appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.LME_ROLE_ID)) {
                                            CallLogOutDevice();
                                        }
                                    } else {
                                        Toast.makeText(activity, "Logout functionality is only available in Online Mode", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                } else if (response.code() == 400) {
                    TastyToast.makeText(activity, "Incorrect old password.", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                } else {
                    TastyToast.makeText(activity, SOMETHING_WENT_WRONG, TastyToast.LENGTH_LONG, TastyToast.ERROR);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                globalclass.hideProgressDialog(activity);
                globalclass.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });
    }


    public void CallLogoutRequestApi() {
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallLogoutRequestApi();
        globalclass.showProgressDialog(activity, PLEASE_WAIT);
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                globalclass.hideProgressDialog(activity);
                if (response.isSuccessful()) {
                    try {
                        new LogUserActivityTagging(activity, LOGOUT, "");
                        appPreferenceManager.clearAllPreferences();
                        dhbDao.deleteTablesonLogout();
                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        homeIntent.addCategory(Intent.CATEGORY_HOME);
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);

                        Intent n = new Intent(activity, LoginActivity.class);
                        n.setAction(Intent.ACTION_MAIN);
                        n.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(n);
                        activity.finish();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (response.code() == 401) {
                    CommonUtils.CallLogOutFromDevice(activity, (Activity) activity, appPreferenceManager, dhbDao);
                } else {
                    Toast.makeText(activity, "Failed to Logout", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                globalclass.hideProgressDialog(activity);
                MessageLogger.LogDebug("Errror", t.getMessage());
            }
        });
    }


    @SuppressLint("MissingPermission")
    private void CallLogOutDevice() {
        try {
            if (!InputUtils.isNull(appPreferenceManager.getLoginResponseModel().getUserID())) {
                String device_id = "";

                device_id = DeviceUtils.getDeviceId(activity);

                if (ApplicationController.mDeviceLogOutController != null) {
                    ApplicationController.mDeviceLogOutController = null;
                }

                ApplicationController.mDeviceLogOutController = new DeviceLogOutController(activity);
                ApplicationController.mDeviceLogOutController.CallLogOutDevice(appPreferenceManager.getLoginResponseModel().getUserID(), device_id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
