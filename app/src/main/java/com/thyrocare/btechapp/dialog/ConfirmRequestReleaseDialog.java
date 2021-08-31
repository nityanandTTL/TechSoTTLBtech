package com.thyrocare.btechapp.dialog;

import android.app.Activity;
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

import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.delegate.ConfirmOrderReleaseDialogButtonClickedDelegate;
import com.thyrocare.btechapp.models.api.response.RemarksRequestToReleaseResponseModel;
import com.thyrocare.btechapp.models.data.OrderVisitDetailsModel;


import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.Global;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;

/**
 * Created by Orion on 4/24/2017.
 */

public class ConfirmRequestReleaseDialog extends Dialog implements View.OnClickListener {
    private Activity activity;
    private Dialog d;
    private Button btn_yes, btn_no;
    private TextView tv_title;

    private EditText edt__release_remark;
    private Spinner edt_remark;
    private RemarksRequestToReleaseResponseModel remarksResponseModelmain;
    private ArrayList<RemarksRequestToReleaseResponseModel> remarksResponseModelsarr;
    private ArrayList<String> remarks;
    private ConfirmOrderReleaseDialogButtonClickedDelegate confirmOrderReleaseDialogButtonClickedDelegate;
    private OrderVisitDetailsModel orderVisitDetailsModel;
    private Global global;

    public ConfirmRequestReleaseDialog(Activity activity, ConfirmOrderReleaseDialogButtonClickedDelegate confirmOrderReleaseDialogButtonClickedDelegate, OrderVisitDetailsModel orderVisitDetailsModel) {
        super(activity);
        this.activity = activity;
        global = new Global(activity);
        this.confirmOrderReleaseDialogButtonClickedDelegate = confirmOrderReleaseDialogButtonClickedDelegate;
        this.orderVisitDetailsModel = orderVisitDetailsModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm_order_release);
        initUI();
        GetremarksForRequestToReleaseApi();

        setListners();
    }

    private void setListners() {
        btn_no.setOnClickListener(this);
        btn_yes.setOnClickListener(this);

    }

    private void GetremarksForRequestToReleaseApi() {
        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<ArrayList<RemarksRequestToReleaseResponseModel>> responseCall = apiInterface.GetremarksForRequestToReleaseApi(27);
        global.showProgressDialog(activity,"Please wait..");
        responseCall.enqueue(new Callback<ArrayList<RemarksRequestToReleaseResponseModel>>() {
            @Override
            public void onResponse(Call<ArrayList<RemarksRequestToReleaseResponseModel>> call, retrofit2.Response<ArrayList<RemarksRequestToReleaseResponseModel>> response) {
                global.hideProgressDialog(activity);
                if (response.isSuccessful() && response.body() != null) {
                    remarksResponseModelsarr = new ArrayList<>();

                    remarksResponseModelsarr = response.body();
                    if (remarksResponseModelsarr != null) {
                        remarks = new ArrayList<>();
                        remarks.add(0, "--SELECT--");
                        for (final RemarksRequestToReleaseResponseModel remarksResponseModelss :
                                remarksResponseModelsarr) {
                            remarks.add(remarksResponseModelss.getRemarks().toUpperCase());
                        }

                        remarksResponseModelmain = new RemarksRequestToReleaseResponseModel();
                        ArrayAdapter<String> spinneradapter71 = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, remarks);
                        spinneradapter71.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        edt_remark.setAdapter(spinneradapter71);
                        edt_remark.setSelection(0);
                        remarksResponseModelmain = remarksResponseModelsarr.get(0);

                        edt_remark.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                if (position > 0) {
                                    remarksResponseModelmain = remarksResponseModelsarr.get(position - 1);
                                    String Remarksstr = remarks.get(position);
                                    for (RemarksRequestToReleaseResponseModel RRM :
                                            remarksResponseModelsarr) {
                                        if (RRM.getRemarks().equals(Remarksstr)) {
                                            remarksResponseModelmain = RRM;
                                            break;
                                        }
                                    }
                                    if(Remarksstr.equalsIgnoreCase("other") || Remarksstr.equalsIgnoreCase("others")){
                                        edt__release_remark.setVisibility(View.VISIBLE);
                                    }else {
                                        edt__release_remark.setVisibility(View.GONE);
                                    }
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                } else {
                    Toast.makeText(activity, SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<RemarksRequestToReleaseResponseModel>> call, Throwable t) {
                global.hideProgressDialog(activity);
                global.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });
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
            if (edt_remark == null || edt_remark.getSelectedItem() == null) {
                TastyToast.makeText(activity, "Select Remark", TastyToast.LENGTH_LONG, TastyToast.WARNING);
            } else if ( edt_remark.getSelectedItem().equals("--SELECT--")) {
                TastyToast.makeText(activity, "Select Remark", TastyToast.LENGTH_LONG, TastyToast.WARNING);
            }else if (ifOTHERRemarksVisible()) {

            } else {
                Logger.error("reason " + edt__release_remark.getText().toString());
                confirmOrderReleaseDialogButtonClickedDelegate.onOkButtonClicked(orderVisitDetailsModel, getReasonRemarks());
                dismiss();
            }
        }
        if (v.getId() == R.id.btn_no) {
            confirmOrderReleaseDialogButtonClickedDelegate.onCancelButtonClicked();
            dismiss();
        }
    }

    private boolean ifOTHERRemarksVisible() {
        if(edt__release_remark.getVisibility() == View.VISIBLE){
            if (edt__release_remark.getText().toString().equals("")) {
                global.showCustomToast(activity,activity.getResources().getString(R.string.enter_remarks), Toast.LENGTH_SHORT);
//                Toast.makeText(activity, R.string.enter_remarks, Toast.LENGTH_SHORT).show();
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }

    private String getReasonRemarks() {
        String st = "";
        if(edt__release_remark.getVisibility() == View.VISIBLE){
            st = remarksResponseModelmain.getRemarks().toString().trim()+" - "+edt__release_remark.getText().toString().trim();
        }else {
            st = remarksResponseModelmain.getRemarks().toString().trim();
        }

        return st;
    }
}
