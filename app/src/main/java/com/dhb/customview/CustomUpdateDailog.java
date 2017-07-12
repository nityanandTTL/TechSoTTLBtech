package com.dhb.customview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dhb.R;
import com.dhb.delegate.CustomUpdateDialogOkButtonOnClickedDelegate;

/**
 * Created by E4904 on 5/11/2017.
 */


public class CustomUpdateDailog extends Dialog implements View.OnClickListener {
    public Activity activity;
    public Dialog d;
    public Button btn_update, btn_ok;
    public TextView tv_title;
    public EditText edt_remark;
    private CustomUpdateDialogOkButtonOnClickedDelegate customupdateDialogOkButtonOnClickedDelegate ;

    public CustomUpdateDailog(Context context, CustomUpdateDialogOkButtonOnClickedDelegate customupdateDialogOkButtonOnClickedDelegate) {
        super(context);
        this.customupdateDialogOkButtonOnClickedDelegate = customupdateDialogOkButtonOnClickedDelegate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_update_dailog);
        initUI();
        setListners();
    }

    private void setListners() {
        btn_update.setOnClickListener(this);
        btn_ok.setOnClickListener(this);

    }

    private void initUI() {
        btn_update = (Button) findViewById(R.id.btn_Update);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        tv_title = (TextView) findViewById(R.id.tv_title);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_Update) {
            customupdateDialogOkButtonOnClickedDelegate.onClicked();
            dismiss();
        }
        else if (v.getId() == R.id.btn_ok) {
            System.exit(0);
        }
    }
}

