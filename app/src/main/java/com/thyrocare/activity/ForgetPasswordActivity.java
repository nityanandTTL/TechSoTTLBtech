package com.thyrocare.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.thyrocare.R;
import com.thyrocare.models.api.request.ResetPasswordRequestModel;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.uiutils.AbstractActivity;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.InputUtils;

import org.json.JSONException;


public class ForgetPasswordActivity extends AbstractActivity implements View.OnClickListener {
    EditText edt_mobile_no, edt_otp, edt_new_password, edt_confirm_new_password;
    Button btn_send_otp, btn_verify_otp;
    LinearLayout ll_send_otp, ll_verify_otp;
    private String code;
    String regexp = ".{6,12}";
    String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initUI();
        setListeners();
    }

    private void setListeners() {
        btn_send_otp.setOnClickListener(this);
        btn_verify_otp.setOnClickListener(this);
    }

    @Override
    public void initUI() {
        super.initUI();
        edt_mobile_no = (EditText) findViewById(R.id.edt_mobile_no);
        btn_send_otp = (Button) findViewById(R.id.btn_send_otp);
        btn_verify_otp = (Button) findViewById(R.id.btn_verify_otp);
        ll_verify_otp = (LinearLayout) findViewById(R.id.ll_verify_otp);
        ll_send_otp = (LinearLayout) findViewById(R.id.ll_send_otp);
        edt_otp = (EditText) findViewById(R.id.edt_otp);
        edt_confirm_new_password = (EditText) findViewById(R.id.edt_confirm_new_password);
        edt_new_password = (EditText) findViewById(R.id.edt_new_password);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_send_otp) {
            if (validate()) {
                getOtpApi("send otp");
            } else {
                edt_mobile_no.setError(getString(R.string.enter_mobile_number));
                edt_mobile_no.requestFocus();
            }
        }
        if (v.getId() == R.id.btn_verify_otp) {
            if (validateFields()) {
//                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                  getOtpApi("verify otp");
            }
        }
    }

    private boolean validateFields() {
        if (InputUtils.isNull(edt_otp.getText().toString())) {
            edt_otp.requestFocus();
            edt_otp.setError("Enter OTP");

            return false;
        }
        if (!edt_new_password.getText().toString().matches(regexp)) {
            edt_new_password.requestFocus();
            edt_new_password.setError(getString(R.string.password_criteria));
            return false;
        }
        if (InputUtils.isNull(edt_confirm_new_password.getText().toString())) {
            edt_confirm_new_password.setError(getString(R.string.password_criteria));
           edt_confirm_new_password.requestFocus();
            return false;
        }
        if (!edt_confirm_new_password.getText().toString().equals(edt_new_password.getText().toString())) {
            edt_confirm_new_password.setError(getString(R.string.password_do_not_match));
        edt_confirm_new_password.requestFocus();
            return false;
        }
        return true;
    }

    private void getOtpApi(String action) {
        ResetPasswordRequestModel resetPasswordRequestModel = new ResetPasswordRequestModel();
        resetPasswordRequestModel.setEmail(edt_mobile_no.getText().toString());
        if (action.equals("verify otp")) {
            str = code.trim().replaceAll("\n ", "");
            Logger.error(str.replaceAll("^\"|\"$", ""));
            resetPasswordRequestModel.setCode(str.replaceAll("^\"|\"$", ""));
            resetPasswordRequestModel.setConfirmPassword(edt_confirm_new_password.getText().toString());
            resetPasswordRequestModel.setPassword(edt_new_password.getText().toString());
            resetPasswordRequestModel.setOTP(edt_otp.getText().toString());
        }

        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(this);
        ApiCallAsyncTask changePasswordApiAsyncTask = asyncTaskForRequest.getResetPasswordRequestAsyncTask(resetPasswordRequestModel);
        if (action.equals("send otp")) {
            changePasswordApiAsyncTask.setApiCallAsyncTaskDelegate(new ResetPasswordApiAsyncTaskDelegateResult());
        } else {
            changePasswordApiAsyncTask.setApiCallAsyncTaskDelegate(new ResetPasswordVerifyOtpApiAsyncTaskDelegateResult());
        }

        if (isNetworkAvailable(this)) {
            changePasswordApiAsyncTask.execute(changePasswordApiAsyncTask);
        } else {
            Toast.makeText(this, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validate() {
        if (InputUtils.isNull(edt_mobile_no.getText().toString())) {
            return false;
        }
        return true;
    }

    private class ResetPasswordApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                Toast.makeText(ForgetPasswordActivity.this, "OTP sent Successfully", Toast.LENGTH_SHORT).show();
                code = json;
                ll_send_otp.setVisibility(View.GONE);
                ll_verify_otp.setVisibility(View.VISIBLE);
                edt_otp.requestFocus();
                edt_mobile_no.setEnabled(false);
            } else {
                Toast.makeText(ForgetPasswordActivity.this, "" + json, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {

        }
    }

    private class ResetPasswordVerifyOtpApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                Toast.makeText(ForgetPasswordActivity.this, "Success", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(ForgetPasswordActivity.this, "" + json, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(ForgetPasswordActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }
}
