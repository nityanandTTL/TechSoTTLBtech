package com.dhb.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dhb.R;
import com.dhb.activity.EditTestListActivity;
import com.dhb.adapter.CancelOrderAdapter;
import com.dhb.delegate.AddTestListDailogDelegate;
import com.dhb.delegate.CancelButtonDailogDelegate;
import com.dhb.delegate.CancelTestsDialogDelegate;
import com.dhb.utils.app.BundleConstants;

import java.util.ArrayList;

/**
 * Created by E4904 on 6/15/2017.
 */

public class EditTestListDailog extends Dialog {
    private Activity activity;
    private TextView txt_names;
    private ImageView cancel_test;
    private Button btn_addtest, btn_cancel;
    private ArrayList<String> testCodesArr;
    private ListView listtest;
    private CancelButtonDailogDelegate CancelButtonDailogDelegate;
    private CancelOrderAdapter cancelAdap;
    private AddTestListDailogDelegate addTestListDailogDelegate;

    public EditTestListDailog(Activity activity, ArrayList<String> testCodesArr, CancelButtonDailogDelegate cancelButtonDailogDelegate, AddTestListDailogDelegate addTestListDailogDelegate) {
        super(activity);
        this.activity = activity;
        this.setCanceledOnTouchOutside(false);
        this.testCodesArr = testCodesArr;
        this.CancelButtonDailogDelegate = cancelButtonDailogDelegate;
        this.addTestListDailogDelegate=addTestListDailogDelegate;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dailog_edit_order);
        initUI();
        setListners();
    }

    private void setListners() {

        btn_addtest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

          addTestListDailogDelegate.onItemClick();


            }
        });


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
                CancelButtonDailogDelegate.onItemClick();


            }
        });

    }


    private void initUI() {
        btn_addtest = (Button) findViewById(R.id.btn_addtest);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        listtest = (ListView) findViewById(R.id.test_names);


        cancelAdap = new CancelOrderAdapter(activity, testCodesArr, new CancelTestsDialogDelegate() {

            @Override
            public void onCancelButtonClicked(String testcode) {
                testCodesArr.remove(testcode);
                cancelAdap.notifyDataSetChanged();
            }

        });

        listtest.setAdapter(cancelAdap);
    }


}
