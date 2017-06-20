package com.dhb.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.models.api.request.LoginRequestModel;
import com.dhb.models.api.response.LoginResponseModel;
import com.dhb.network.ApiCallAsyncTask;
import com.dhb.network.ApiCallAsyncTaskDelegate;
import com.dhb.network.AsyncTaskForRequest;
import com.dhb.network.ResponseParser;
import com.dhb.uiutils.AbstractActivity;
import com.dhb.utils.app.AppPreferenceManager;
import com.dhb.utils.app.InputUtils;
import com.dhb.utils.app.UiUtils;

import org.json.JSONException;

public class LoginScreenActivity extends AbstractActivity implements View.OnClickListener {
    EditText edt_username_login, edt_password_login;
    TextView tv_forget_password;
    Button btn_login;
    LinearLayout ll_login;
    Activity activity;
    AppPreferenceManager appPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        activity = this;
        appPreferenceManager=new AppPreferenceManager(activity);
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
            Intent intentForgotPassword = new Intent(activity,ForgetPasswordActivity.class);
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
                }else {
                    Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
                }
            }

        } else if (v.getId() == R.id.ll_login) {
            UiUtils.hideSoftKeyboard(this);
        }
    }

    private boolean validate() {
        if (InputUtils.isNull(edt_username_login.getText().toString())) {
            Toast.makeText(activity, "Enter User name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (InputUtils.isNull(edt_password_login.getText().toString())) {
            Toast.makeText(activity, "Enter Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private class LoginApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if(statusCode==200){
                ResponseParser responseParser=new ResponseParser(activity);
                LoginResponseModel loginResponseModel=new LoginResponseModel();

                loginResponseModel=responseParser.getLoginResponseModel(json,statusCode);
                if(loginResponseModel!=null){

                    appPreferenceManager.setLoginResponseModel(loginResponseModel);
                    appPreferenceManager.setAPISessionKey(loginResponseModel.getAccess_token());
                   // switchToActivity(activity,SelfieUploadActivity.class,new Bundle());

                    if (loginResponseModel.getRole().equals("4"))
                        switchToActivity(activity, SelfieUploadActivity.class, new Bundle());
                    else
                        Toast.makeText(activity, "You are not authorized to log in...", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(activity,""+json,Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }
}
