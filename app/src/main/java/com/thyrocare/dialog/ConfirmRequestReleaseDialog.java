package com.thyrocare.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.thyrocare.R;
import com.thyrocare.activity.HomeScreenActivity;
import com.thyrocare.delegate.ConfirmOrderReleaseDialogButtonClickedDelegate;
import com.thyrocare.models.api.response.RemarksResponseModel;
import com.thyrocare.models.data.OrderVisitDetailsModel;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.network.ResponseParser;
import com.thyrocare.utils.api.Logger;

import org.json.JSONException;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by Orion on 4/24/2017.
 */

public class ConfirmRequestReleaseDialog extends Dialog implements View.OnClickListener {
    private HomeScreenActivity activity;
    private Dialog d;
    private Button btn_yes, btn_no;
    private TextView tv_title;

    private EditText edt__release_remark;
    private Spinner edt_remark;
    private RemarksResponseModel remarksResponseModelmain;
    private ArrayList<RemarksResponseModel> remarksResponseModelsarr;
    private ArrayList<String> remarks;
    private ConfirmOrderReleaseDialogButtonClickedDelegate confirmOrderReleaseDialogButtonClickedDelegate;
    private OrderVisitDetailsModel orderVisitDetailsModel;
    public ConfirmRequestReleaseDialog(HomeScreenActivity activity, ConfirmOrderReleaseDialogButtonClickedDelegate confirmOrderReleaseDialogButtonClickedDelegate, OrderVisitDetailsModel orderVisitDetailsModel) {
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
        fetchremarks();

        setListners();
    }

    private void setListners() {
        btn_no.setOnClickListener(this);
        btn_yes.setOnClickListener(this);

    }
    private void fetchremarks() {
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchLeaveDetailApiAsyncTask = asyncTaskForRequest.getremarksRequestAsyncTask(27);
        fetchLeaveDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new FetchRemarksDetailsApiAsyncTaskDelegateResult());

            fetchLeaveDetailApiAsyncTask.execute(fetchLeaveDetailApiAsyncTask);


    }

    private class FetchRemarksDetailsApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                ResponseParser responseParser = new ResponseParser(activity);
                remarksResponseModelsarr = new ArrayList<>();

                remarksResponseModelsarr = responseParser.getRemarksResponseModel(json, statusCode);
                if (remarksResponseModelsarr != null) {
                    remarks = new ArrayList<>();
                  remarks.add(0,"--SELECT--");
                    for (final RemarksResponseModel remarksResponseModelss:
                            remarksResponseModelsarr) {
                        remarks.add(remarksResponseModelss.getReason().toUpperCase());
                        remarksResponseModelmain = new RemarksResponseModel();
                        ArrayAdapter<String> spinneradapter71 = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, remarks);
                        spinneradapter71.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        edt_remark.setAdapter(spinneradapter71);
                        edt_remark.setSelection(0);
                        remarksResponseModelmain = remarksResponseModelsarr.get(0);

                        edt_remark.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                //jai
                                /*remarksResponseModelmain = remarksResponseModelsarr.get(position);
                                String Remarksstr = remarks.get(position);*/
                                if(position>0){
                                    remarksResponseModelmain = remarksResponseModelsarr.get(position-1);
                                    String Remarksstr = remarks.get(position-1);
                                    //jai
                                    for (RemarksResponseModel RRM :
                                            remarksResponseModelsarr) {
                                        if (RRM.getReason().equals(Remarksstr)) {
                                            remarksResponseModelmain = RRM;

                                            break;
                                        }
                                    }
                                }


                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }



                }


            }
        }


        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, R.string.network_error, LENGTH_SHORT).show();
        }


    }



    private void initUI() {
        edt__release_remark = (EditText) findViewById(R.id.edt__release_remark);
        btn_yes = (Button) findViewById(R.id.btn_yes);
        btn_no = (Button) findViewById(R.id.btn_no);
        tv_title = (TextView) findViewById(R.id.tv_title);
        edt_remark = (Spinner) findViewById(R.id.sp_remark);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_yes) {
            if (/*!InputUtils.isNull(remarksResponseModelmain.getReason().trim())
                || remarksResponseModelmain.getReason().trim()!="--SELECT--"*/!edt__release_remark.getText().toString().equals("")) {
                //  Logger.error("reason "+remarksResponseModelmain.getReason());
                Logger.error("reason " + edt__release_remark.getText().toString());

                confirmOrderReleaseDialogButtonClickedDelegate.onOkButtonClicked(orderVisitDetailsModel, edt__release_remark.getText().toString().trim());
                //   confirmOrderReleaseDialogButtonClickedDelegate.onOkButtonClicked(orderVisitDetailsModel,remarksResponseModelmain.getReason().trim());
                confirmOrderReleaseDialogButtonClickedDelegate.onCancelButtonClicked();
                dismiss();

            } else {
                Toast.makeText(activity, R.string.enter_remarks, Toast.LENGTH_SHORT).show();
            }
        }
        if (v.getId() == R.id.btn_no) {
            confirmOrderReleaseDialogButtonClickedDelegate.onCancelButtonClicked();
            dismiss();
        }
    }
}
