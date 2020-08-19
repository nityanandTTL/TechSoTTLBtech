package com.thyrocare.btechapp.customview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.delegate.CustomUpdateDialogOkButtonOnClickedDelegate;

/**
 * Created by Orion on 5/11/2017.
 */


public class CustomDeviceResetDailog extends Dialog implements View.OnClickListener {
    public Activity activity;
    public Dialog d;
    public Button btn_ok;
    private CustomUpdateDialogOkButtonOnClickedDelegate customupdateDialogOkButtonOnClickedDelegate ;

    public CustomDeviceResetDailog(Context context, CustomUpdateDialogOkButtonOnClickedDelegate customupdateDialogOkButtonOnClickedDelegate) {
        super(context);
        this.customupdateDialogOkButtonOnClickedDelegate = customupdateDialogOkButtonOnClickedDelegate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_deviceid_dailog);
        initUI();
        setListners();
    }

    private void setListners() {
        btn_ok.setOnClickListener(this);

    }

    private void initUI() {
        btn_ok = (Button) findViewById(R.id.btn_ok);
    }


    @Override
    public void onClick(View v) {
       if (v.getId() == R.id.btn_ok) {
            customupdateDialogOkButtonOnClickedDelegate.onOkClicked();
        }
    }
}

