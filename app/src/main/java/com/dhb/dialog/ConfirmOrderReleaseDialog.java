package com.dhb.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.activity.HomeScreenActivity;
import com.dhb.delegate.ConfirmOrderReleaseDialogButtonClickedDelegate;
import com.dhb.fragment.VisitOrdersDisplayFragment;
import com.dhb.models.data.OrderVisitDetailsModel;
import com.dhb.utils.app.InputUtils;

/**
 * Created by vendor1 on 4/24/2017.
 */

public class ConfirmOrderReleaseDialog extends Dialog implements View.OnClickListener {
    private HomeScreenActivity activity;
    private Dialog d;
    private Button btn_yes, btn_no;
    private TextView tv_title;
    private EditText edt_remark;
    private ConfirmOrderReleaseDialogButtonClickedDelegate confirmOrderReleaseDialogButtonClickedDelegate;
    private OrderVisitDetailsModel orderVisitDetailsModel;
    public ConfirmOrderReleaseDialog(HomeScreenActivity activity, ConfirmOrderReleaseDialogButtonClickedDelegate confirmOrderReleaseDialogButtonClickedDelegate, OrderVisitDetailsModel orderVisitDetailsModel) {
        super(activity);
        this.activity = activity;
        this.confirmOrderReleaseDialogButtonClickedDelegate = confirmOrderReleaseDialogButtonClickedDelegate;
        this.orderVisitDetailsModel = orderVisitDetailsModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm_order_release);
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
        edt_remark = (EditText) findViewById(R.id.edt_emark);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_yes) {
            if(!InputUtils.isNull(edt_remark.getText().toString().trim())) {
                confirmOrderReleaseDialogButtonClickedDelegate.onOkButtonClicked(orderVisitDetailsModel,edt_remark.getText().toString().trim());
                dismiss();
            }else{
                Toast.makeText(activity, R.string.enter_remarks,Toast.LENGTH_SHORT).show();
            }
        }
        if(v.getId()==R.id.btn_no){
            confirmOrderReleaseDialogButtonClickedDelegate.onCancelButtonClicked();
            dismiss();
        }
    }
}
