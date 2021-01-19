package com.thyrocare.btechapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.models.api.request.ResetPasswordRequestModel;



import com.thyrocare.btechapp.uiutils.AbstractActivity;
import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;
import com.thyrocare.btechapp.utils.app.OtpListenerUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ForgetPasswordActivity extends AbstractActivity implements View.OnClickListener {
    EditText edt_mobile_no, edt_otp, edt_new_password, edt_confirm_new_password;
    Button btn_send_otp, btn_verify_otp;
    LinearLayout ll_send_otp, ll_verify_otp;
    private String code;
    String regexp = ".{6,12}";
    String str;
    private Global globalClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        globalClass = new Global(ForgetPasswordActivity.this);
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
                if (isNetworkAvailable(this)) {
                    getOtpApi("send otp");
                } else {
                    Toast.makeText(this, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
                }
            } else {
                edt_mobile_no.setError(getString(R.string.enter_mobile_number));
                edt_mobile_no.requestFocus();
            }
        }
        if (v.getId() == R.id.btn_verify_otp) {
            if (validateFields()) {
//                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();

                if (isNetworkAvailable(this)) {
                    getOtpApi("verify otp");
                } else {
                    Toast.makeText(this, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
                }
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

    private void getOtpApi(final String action){

        ResetPasswordRequestModel resetPasswordRequestModel = new ResetPasswordRequestModel();
        resetPasswordRequestModel.setEmail(edt_mobile_no.getText().toString());
        if (action.equals("verify otp")) {
            if (code != null) {
                str = code.trim().replaceAll("\n ", "");
            } else {
                str = "";
            }
            Logger.error(str.replaceAll("^\"|\"$", ""));
            resetPasswordRequestModel.setCode(str.replaceAll("^\"|\"$", ""));
            resetPasswordRequestModel.setConfirmPassword(edt_confirm_new_password.getText().toString());
            resetPasswordRequestModel.setPassword(edt_new_password.getText().toString());
            resetPasswordRequestModel.setOTP(edt_otp.getText().toString());
        }

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(ForgetPasswordActivity.this, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallResetPasswordAPI(resetPasswordRequestModel);
        globalClass.showProgressDialog(ForgetPasswordActivity.this,"Please wait..");
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                globalClass.hideProgressDialog(ForgetPasswordActivity.this);
                if (response.isSuccessful()){

                    if (action.equals("send otp")) {
                        OtpListenerUtil.startSmsUserConsent(ForgetPasswordActivity.this);
                        Toast.makeText(ForgetPasswordActivity.this, "OTP sent Successfully", Toast.LENGTH_SHORT).show();
                        code = !StringUtils.isNull(response.body()) ? response.body() : "";
                        ll_send_otp.setVisibility(View.GONE);
                        ll_verify_otp.setVisibility(View.VISIBLE);
                        edt_otp.requestFocus();
                        edt_mobile_no.setEnabled(false);
                    } else {
                        Toast.makeText(ForgetPasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }else{
                    Toast.makeText(ForgetPasswordActivity.this, !StringUtils.isNull(response.body()) ? response.body() : ConstantsMessages.SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                globalClass.hideProgressDialog(ForgetPasswordActivity.this);
            }
        });
    }

    private boolean validate() {
        if (InputUtils.isNull(edt_mobile_no.getText().toString())) {
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == OtpListenerUtil.REQ_USER_CONSENT) {
            if ((resultCode == RESULT_OK) && (data != null)) {
                //That gives all message to us.
                // We need to get the code from inside with regex
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                String OTP  = OtpListenerUtil.getOtpFromMessag(message);
                if (StringUtils.isNull(OTP)){
                    globalClass.showcenterCustomToast(ForgetPasswordActivity.this, "Failed to Detect OTP",Toast.LENGTH_LONG);
                }else{
                    edt_otp.setText(OTP);

                }

            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        OtpListenerUtil.UnregisteredReceiver();
    }
}
