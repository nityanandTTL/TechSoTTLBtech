package com.dhb.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dhb.R;
import com.dhb.uiutils.AbstractActivity;
import com.dhb.utils.app.InputUtils;


public class ForgetPasswordActivity extends AbstractActivity implements View.OnClickListener {
    EditText edt_mobile_no;
    Button btn_send_otp, btn_verify_otp;

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
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_send_otp) {
            if (validate()) {

            } else {
                edt_mobile_no.setError(getString(R.string.enter_mobile_number));
            }
        }
        if (v.getId() == R.id.btn_verify_otp) {

        }
    }

    private boolean validate() {
        if (InputUtils.isNull(edt_mobile_no.getText().toString())) {
            return false;
        }
        return true;
    }
}
