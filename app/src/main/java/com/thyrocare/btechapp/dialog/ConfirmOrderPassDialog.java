package com.thyrocare.btechapp.dialog;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.btechapp.NewScreenDesigns.Fragments.VisitOrdersDisplayFragment_new;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.HomeScreenActivity;
import com.thyrocare.btechapp.delegate.refreshDelegate;
import com.thyrocare.btechapp.models.api.request.OrderPassRequestModel;
import com.thyrocare.btechapp.models.api.response.OrderPassresponseModel;
import com.thyrocare.btechapp.models.data.OrderVisitDetailsModel;
import com.thyrocare.btechapp.models.data.Orderallocation;


import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.Global;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;
import static com.thyrocare.btechapp.utils.api.NetworkUtils.isNetworkAvailable;

/**
 * Created by Orion on 4/24/2017.
 */

public class ConfirmOrderPassDialog extends Dialog implements View.OnClickListener {
    private static final String TAG = ConfirmOrderPassDialog.class.getSimpleName();
    private HomeScreenActivity activity;
    private Dialog d;
    private Button btn_yes, btn_no, btn_call, btn_send;
    private TextView tv_title, tv_cancel;
    private Spinner sp_btech;
    private TextView call_btech;
    private OrderPassresponseModel orderPassresponseModel;
    private ArrayList<Orderallocation> orderallocationsarr;
    private ArrayList<String> Btecharr;
    private refreshDelegate RefreshDelegate;
    private Orderallocation orderallocationmodel;
    private OrderVisitDetailsModel orderVisitDetailsModel;
    private OrderPassRequestModel orderPassRequestModel;
    private AppPreferenceManager appPreferenceManager;
    private String Pincode;
    private EditText edt_otp;
    private LinearLayout validateOtp;
    private Global global;

    public ConfirmOrderPassDialog(HomeScreenActivity activity, refreshDelegate RefreshDelegate, String pincode, OrderVisitDetailsModel orderVisitDetailsModel) {
        super(activity);
        this.activity = activity;
        global = new Global(activity);
        this.RefreshDelegate = RefreshDelegate;
        this.orderVisitDetailsModel = orderVisitDetailsModel;
        this.Pincode = pincode;

        // this.VisitID=VisitId;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm_order_pass);
        appPreferenceManager = new AppPreferenceManager(getContext());
        initUI();
        Logger.error("Pincode " + Pincode);
        Logger.error("VisitId: " + orderVisitDetailsModel.getVisitId());
        if (isNetworkAvailable(activity)){
            CallGetorderallocationApi(Pincode);
        }else{
            global.showCustomToast(activity, ConstantsMessages.SOMETHING_WENT_WRONG);
        }

        setListners();
    }

    private void setListners() {
        btn_no.setOnClickListener(this);
        btn_yes.setOnClickListener(this);
        call_btech.setOnClickListener(this);
        btn_call.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        btn_send.setOnClickListener(this);

    }

    private void CallGetorderallocationApi(String Pincode) {
        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.DecodeString64(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<OrderPassresponseModel> responseCall = apiInterface.CallGetorderallocationApi(appPreferenceManager.getLoginResponseModel().getUserID(),Pincode);
        global.showProgressDialog(activity, "Please wait..");
        responseCall.enqueue(new Callback<OrderPassresponseModel>() {
            @Override
            public void onResponse(Call<OrderPassresponseModel> call, retrofit2.Response<OrderPassresponseModel> response) {
                global.hideProgressDialog(activity);
                if (response.isSuccessful() && response.body() != null) {
                    orderallocationsarr = new ArrayList<>();
                    orderPassresponseModel = response.body();
                    if (orderPassresponseModel != null) {

                        orderallocationsarr = orderPassresponseModel.getOrderAllocation();
                        if (orderallocationsarr.size() > 0) {

                            Btecharr = new ArrayList<>();
                            Btecharr.add(0, "--SELECT--");

                            for (final Orderallocation orderallocation :
                                    orderallocationsarr) {
                                Btecharr.add(orderallocation.getBtechName().toUpperCase());
                                orderallocationmodel = new Orderallocation();
                                ArrayAdapter<String> spinneradapter71 = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, Btecharr);
                                spinneradapter71.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                sp_btech.setAdapter(spinneradapter71);
                                sp_btech.setSelection(0);
                                orderallocationmodel = orderallocationsarr.get(0);

                                sp_btech.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        //jai
                                    /*orderallocationmodel = orderallocationsarr.get(position);
                                    String Btechstr = Btecharr.get(position);*/
                                        if (position > 0) {
                                            orderallocationmodel = orderallocationsarr.get(position - 1);
                                            String Btechstr = Btecharr.get(position - 1);
                                            for (Orderallocation OA :
                                                    orderallocationsarr) {
                                                if (String.valueOf(OA.getBtechId()).equals(Btechstr)) {
                                                    orderallocationmodel = OA;

                                                    break;
                                                }
                                            }
                                        }

                                        //jai


                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                        } else {
                            Logger.error("empty btech");
                            btn_yes.setEnabled(false);
                            TastyToast.makeText(activity, "No Btech Available", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }
                    } else {
                        Logger.error("empty btech");
                        btn_yes.setEnabled(false);
                    }
                } else {
                    Toast.makeText(activity, SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<OrderPassresponseModel> call, Throwable t) {
                global.hideProgressDialog(activity);
                global.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });
    }


    private void initUI() {
        btn_yes = (Button) findViewById(R.id.btn_yes);
        btn_no = (Button) findViewById(R.id.btn_no);
        btn_call = (Button) findViewById(R.id.btn_call);
        tv_title = (TextView) findViewById(R.id.tv_title);
        sp_btech = (Spinner) findViewById(R.id.sp_Btech);
        call_btech = (TextView) findViewById(R.id.call_btech);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        btn_send = (Button) findViewById(R.id.btn_send);
        edt_otp = (EditText) findViewById(R.id.edt_otp);
        validateOtp = (LinearLayout) findViewById(R.id.validateOtp);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_yes) {
            if (validate()) {
                PostSendOTPOrderpass();

            }

            // RefreshDelegate.onRefreshClicked();
            // dismiss();

        }
        if (v.getId() == R.id.btn_no) {
            dismiss();
        }
        if (v.getId() == R.id.btn_send) {
            validateOTP();
            //showAlert("test");

            // dismiss();
        }
        if (v.getId() == R.id.tv_cancel) {
            dismiss();
        }
        if (v.getId() == R.id.call_btech) {
            try {
                if (validate()) {
                    if (orderallocationmodel.getMobile() != null) {
                        MessageLogger.LogError(TAG, "onClick: mobile " + orderallocationmodel.getMobile());

                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + orderallocationmodel.getMobile()));
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        getContext().startActivity(intent);

                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void validateOTP() {
        if (!edt_otp.getText().toString().equals("")) {
            OrderPassRequestModel orderPassRequestModel = new OrderPassRequestModel();
            orderPassRequestModel.setMobile(orderallocationmodel.getMobile());
            orderPassRequestModel.setVisitId(orderVisitDetailsModel.getVisitId());
            orderPassRequestModel.setOTP("" + edt_otp.getText().toString());
            Logger.error("btech " + orderallocationmodel.getBtechName());

            if (isNetworkAvailable(activity)) {
                CallgetOrderPassVerifyOtpRequestModelAPI(orderPassRequestModel);
            } else {
                Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(activity, "Enter OTP", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean validate() {
        try {
            if (sp_btech.getSelectedItem().equals("--SELECT--")) {
                TastyToast.makeText(activity, "Select BTECH Name", TastyToast.LENGTH_LONG, TastyToast.WARNING);
                // TastyToast
                return false;
            }
            if (orderallocationmodel == null) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
    private void PostSendOTPOrderpass() {
        OrderPassRequestModel orderPassRequestModel = new OrderPassRequestModel();
        if (orderallocationmodel != null) {
            if (orderallocationmodel.getMobile() != null && orderallocationmodel.getMobile().length() > 0) {
                orderPassRequestModel.setMobile(orderallocationmodel.getMobile());
            }
        }
        orderPassRequestModel.setVisitId(orderVisitDetailsModel.getVisitId());

        if (isNetworkAvailable(activity)) {
            CallgetOrderPassSendOtpRequestAPI(orderPassRequestModel);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void PostOrderpass() {

        OrderPassRequestModel orderPassRequestModel = new OrderPassRequestModel();
        orderPassRequestModel.setBtechId(orderallocationmodel.getBtechId());
        orderPassRequestModel.setVisitId(orderVisitDetailsModel.getVisitId());
        Logger.error("btech " + orderallocationmodel.getBtechName());

        if (isNetworkAvailable(activity)) {
            CallgetOrderPassRequestModelAPI(orderPassRequestModel);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }


    }

    private void CallgetOrderPassSendOtpRequestAPI( OrderPassRequestModel orderPassRequestModel){
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.DecodeString64(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallgetOrderPassSendOtpRequestAPI(orderPassRequestModel);
        global.showProgressDialog(activity,"Please wait..");
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                global.hideProgressDialog(activity);
                if (response.isSuccessful() && response.body() != null){
                    validateOtp.setVisibility(View.VISIBLE);
                    btn_yes.setText("Resend");
                    showAlert1("" + response.body());
                }else{
                    Toast.makeText(activity, ConstantsMessages.SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                global.hideProgressDialog(activity);
            }
        });
    }


    private void showAlert(String message) {
        final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

        alertDialog.setMessage("" + message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        MessageLogger.LogError(TAG, "onClick: dismiss ");

                        activity.pushFragments(VisitOrdersDisplayFragment_new.newInstance(), false, false, VisitOrdersDisplayFragment_new.TAG_FRAGMENT, R.id.fl_homeScreen, VisitOrdersDisplayFragment_new.TAG_FRAGMENT);

                        dismissD();
                    }
                });

        alertDialog.show();

    }

    private void showAlert1(String message) {
        final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

        alertDialog.setMessage("" + message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        MessageLogger.LogError(TAG, "onClick: dismiss ");

                        activity.pushFragments(VisitOrdersDisplayFragment_new.newInstance(), false, false, VisitOrdersDisplayFragment_new.TAG_FRAGMENT, R.id.fl_homeScreen, VisitOrdersDisplayFragment_new.TAG_FRAGMENT);

                        dialog.dismiss();
                    }
                });

        alertDialog.show();

    }

    private void dismissD() {
        dismiss();
    }


    private void CallgetOrderPassVerifyOtpRequestModelAPI( OrderPassRequestModel orderPassRequestModel){
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.DecodeString64(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallgetOrderPassVerifyOtpRequestModelAPI(orderPassRequestModel);
        global.showProgressDialog(activity,"Please wait..");
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                global.hideProgressDialog(activity);
                if (response.isSuccessful() && response.body() != null){
                    Toast.makeText(activity, "Valid OTP", Toast.LENGTH_SHORT).show();
                    PostOrderpass();
                }else{
                    Toast.makeText(activity, ConstantsMessages.SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                global.hideProgressDialog(activity);
            }
        });
    }

    private void CallgetOrderPassRequestModelAPI( OrderPassRequestModel orderPassRequestModel){
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.DecodeString64(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallgetOrderPassRequestModelAPI(orderPassRequestModel);
        global.showProgressDialog(activity,"Please wait..");
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                global.hideProgressDialog(activity);
                if (response.isSuccessful()){
                    showAlert("Order Passed successfully");
                }else{
                    Toast.makeText(activity, ConstantsMessages.SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                global.hideProgressDialog(activity);
            }
        });
    }

}
