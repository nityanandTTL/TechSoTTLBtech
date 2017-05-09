package com.dhb.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.delegate.OrderCancelDialogButtonClickedDelegate;
import com.dhb.models.data.BtechClientsModel;
import com.dhb.models.data.OrderDetailsModel;
import com.dhb.utils.app.InputUtils;

/**
 * Created by vendor1 on 4/24/2017.
 */

public class ConfirmCallDialog extends Dialog implements View.OnClickListener {
    private Activity activity;
    private Dialog d;
    private Button btn_yes, btn_no,btn_send_otp;
    BtechClientsModel btechClientsModel;

    public ConfirmCallDialog(Activity activity, BtechClientsModel btechClientsModel) {
        super(activity);
        this.activity = activity;
       this.btechClientsModel=btechClientsModel;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm_call);
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
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_yes) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" +btechClientsModel.getMobile()));
            activity.startActivity(intent);
            dismiss();
        }
        if(v.getId()==R.id.btn_no){
            dismiss();
        }

    }
}
