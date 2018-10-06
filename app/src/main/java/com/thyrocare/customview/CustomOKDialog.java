package com.thyrocare.customview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.thyrocare.R;
import com.thyrocare.delegate.CustomOkDialogOkButtonOnClickedDelegate;

/**
 * Created by Orion on 4/24/2017.
 */

public class CustomOKDialog extends Dialog implements View.OnClickListener {
    public Activity activity;
    public Dialog d;
    public Button btn_yes, btn_no;
    public TextView tv_title;
    public EditText edt_remark;
    private CustomOkDialogOkButtonOnClickedDelegate customOkDialogOkButtonOnClickedDelegate;

    public CustomOKDialog(Context context,CustomOkDialogOkButtonOnClickedDelegate customOkDialogOkButtonOnClickedDelegate) {
        super(context);
        this.customOkDialogOkButtonOnClickedDelegate = customOkDialogOkButtonOnClickedDelegate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_custom_ok);
        initUI();
        setListners();
    }

    private void setListners() {
         btn_no.setOnClickListener(this);
        btn_yes.setOnClickListener(this);

    }

    private void initUI() {
        btn_yes = (Button) findViewById(R.id.btn_yes);
           btn_no = (Button) findViewById(R.id.btn_no);
        tv_title = (TextView) findViewById(R.id.tv_title);
        edt_remark = (EditText) findViewById(R.id.edt_remark);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_yes) {
            customOkDialogOkButtonOnClickedDelegate.onClicked(edt_remark.getText().toString());
            dismiss();
        }
        else if (v.getId() == R.id.btn_no) {
            dismiss();
        }
    }
}
