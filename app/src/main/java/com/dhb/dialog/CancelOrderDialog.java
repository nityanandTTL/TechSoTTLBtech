package com.dhb.dialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.delegate.OrderCancelDialogButtonClickedDelegate;
import com.dhb.delegate.OrderRescheduleDialogButtonClickedDelegate;
import com.dhb.models.data.OrderDetailsModel;
import com.dhb.utils.app.InputUtils;

import java.util.Calendar;

/**
 * Created by vendor1 on 4/24/2017.
 */

public class CancelOrderDialog extends Dialog implements View.OnClickListener {
    private Activity activity;
    private Dialog d;
    private Button btn_yes, btn_no,btn_send_otp;
    private EditText edt_remark,edt_otp;
    public static LinearLayout ll_reason_for_cancel,ll_enter_otp;

    private OrderCancelDialogButtonClickedDelegate orderCancelDialogButtonClickedDelegate;
    private OrderDetailsModel orderDetailsModel;
    public CancelOrderDialog(Activity activity, OrderCancelDialogButtonClickedDelegate orderCancelDialogButtonClickedDelegate, OrderDetailsModel orderDetailsModel) {
        super(activity);
        this.activity = activity;
        this.orderCancelDialogButtonClickedDelegate = orderCancelDialogButtonClickedDelegate;
        this.orderDetailsModel = orderDetailsModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_cancel_order);
        initUI();
        setListners();
    }

    private void setListners() {
        btn_no.setOnClickListener(this);
        btn_yes.setOnClickListener(this);
        btn_send_otp.setOnClickListener(this);
    }

    private void initUI() {
        btn_yes = (Button) findViewById(R.id.btn_yes);
        btn_no = (Button) findViewById(R.id.btn_no);
        edt_remark = (EditText) findViewById(R.id.edt_remark);
        ll_enter_otp=(LinearLayout)findViewById(R.id.ll_enter_otp);
        ll_reason_for_cancel=(LinearLayout)findViewById(R.id.ll_reason_for_cancel);
        edt_otp=(EditText)findViewById(R.id.edt_otp);
        btn_send_otp=(Button)findViewById(R.id.btn_send_otp);
        ll_enter_otp.setVisibility(View.GONE);
        ll_reason_for_cancel.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_yes) {
            if(!InputUtils.isNull(edt_remark.getText().toString().trim())) {
                orderCancelDialogButtonClickedDelegate.onOkButtonClicked(orderDetailsModel,edt_remark.getText().toString().trim());
                dismiss();
            }else{
                Toast.makeText(activity, R.string.enter_remarks,Toast.LENGTH_SHORT).show();
            }
        }
        if(v.getId()==R.id.btn_no){
            orderCancelDialogButtonClickedDelegate.onCancelButtonClicked();
            dismiss();
        }
        if(v.getId()==R.id.btn_send_otp){
            orderCancelDialogButtonClickedDelegate.onOkButtonClicked(orderDetailsModel,edt_otp.getText().toString().trim());
            dismiss();
        }
    }
}
