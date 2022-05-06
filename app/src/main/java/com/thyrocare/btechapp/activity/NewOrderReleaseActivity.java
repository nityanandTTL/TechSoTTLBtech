package com.thyrocare.btechapp.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.btechapp.Controller.OrderReleaseRemarksController;
import com.thyrocare.btechapp.NewScreenDesigns.Fragments.B2BVisitOrdersDisplayFragment;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.FixAppointmentDataModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.ResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConnectionDetector;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.adapter.NewOrderReleaseAdapter;
import com.thyrocare.btechapp.models.api.request.OrderStatusChangeRequestModel;
import com.thyrocare.btechapp.models.api.request.PECutomerIntimationSMSRequestModel;
import com.thyrocare.btechapp.models.api.request.ServiceUpdateRequestModel;
import com.thyrocare.btechapp.models.api.response.GetPECancelRemarksResponseModel;
import com.thyrocare.btechapp.models.api.response.PECutomerIntimationSMSResponeModel;
import com.thyrocare.btechapp.models.data.OrderVisitDetailsModel;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.DateUtils;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.CheckInternetConnectionMsg;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;

public class NewOrderReleaseActivity extends AppCompatActivity {
    RecyclerView rec_rel_list;
    NewOrderReleaseActivity mActivity;
    int pos;
    ImageView iv_home, iv_back;
    Button btn_back, btn_submit;
    TextView tv_toolbar;
    GetPECancelRemarksResponseModel remarksResponseModel;
    ArrayList<GetPECancelRemarksResponseModel> reasonsDTOS = new ArrayList<>();
    String Str_remarks, Str_other, orderno, pincode,appoinmentdate,token,remarksID,reasonID;
    ConnectionDetector cd;
    AppPreferenceManager appPreferenceManager;
    Global globalclass;
    EditText edt_other;
    int slotID;
    OrderVisitDetailsModel orderDetailsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order_release);
        mActivity = this;
        globalclass = new Global(this);
        cd = new ConnectionDetector(this);
        appPreferenceManager = new AppPreferenceManager(this);
        reasonsDTOS = (ArrayList<GetPECancelRemarksResponseModel>) getIntent().getSerializableExtra(BundleConstants.RELEASE_REMARKS);
        orderno = getIntent().getStringExtra(BundleConstants.ORDER);
        pincode = getIntent().getStringExtra(BundleConstants.PINCODE);
        appoinmentdate = getIntent().getStringExtra(BundleConstants.APPOINTMENT_DATE);
        slotID = getIntent().getIntExtra(BundleConstants.ORDER_SLOTID, 0);
        orderDetailsModel = getIntent().getExtras().getParcelable(BundleConstants.VISIT_ORDER_DETAILS_MODEL);
        remarksID = getIntent().getStringExtra(BundleConstants.REMARKS_ID);
        inITUI();
        onClickListener();
    }

    private void onClickListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });

        edt_other.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Str_other = s.toString().trim();
                if (Str_other.startsWith(".") || Str_other.startsWith(",") || Str_other.startsWith(" ")) {
                    Toast.makeText(mActivity, "Invalid reason", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkremarks()) {
                    if (Global.selectedPosition == 0) {
                        Intent intent = new Intent(mActivity, RescheduleSlotActivity.class);
                        intent.putExtra(BundleConstants.ORDER, orderno);
                        intent.putExtra(BundleConstants.PINCODE, pincode);
                        intent.putExtra(BundleConstants.RES_REMARKS, remarksResponseModel.getId());
                        intent.putExtra(BundleConstants.ORDER_SLOTID,slotID);
                        intent.putExtra(BundleConstants.REMARKS_ID,remarksID);
                        intent.putExtra(BundleConstants.APPOINTMENT_DATE,appoinmentdate);
                        intent.putExtra(BundleConstants.RES_RESPONSE, remarksResponseModel.getReason() + "-" + edt_other.getText().toString().trim());
                        intent.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL,orderDetailsModel);
                        startActivity(intent);
                    } else if (Global.selectedPosition == 1) {
                        dialogToCancelOrder("Do you want to cancel this order.", 2);
//                        dialogToReleaseOrder("Do you want to cancel this order.", 2);
                    } else if (Global.selectedPosition == 2) {
                        if (Global.todisplaytimerforPosition && Global.toDisplayTimerFlag) {
                            dialogToReleaseOrder("Do you want to release this order.", 27);
                        } else {
                            if (remarksResponseModel.getId().equals(BundleConstants.ToDisplayTimerOnSelectedPosition)) {
                                displayDialogTimer();
                            } else {
                                dialogToReleaseOrder("Do you want to  release this order.", 27);
                            }
                        }
                    }
                }
            }
        });
    }

    private void dialogToCancelOrder(String s, final int i) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialogBuilder.setMessage(s);
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (cd.isConnectingToInternet()) {

                            FixAppointmentDataModel dataModel = new FixAppointmentDataModel();
                            dataModel.setBTechId(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
                            dataModel.setOrderNo(orderno);
                            dataModel.setAppointmentDate(appoinmentdate);
//                            dataModel.setAppointmentSlot(slotID);
                            dataModel.setAppointmentSlot(0);
                            dataModel.setStatus(i);
                            dataModel.setRemarksId(Global.selectedRemarksID);
                            dataModel.setReasonId(remarksResponseModel.getId());
                            dataModel.setVisitId(orderno);
                            dataModel.setOthers(remarksResponseModel.getReason()+edt_other.getText().toString().trim());
                            dataModel.setUserId(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
                            dataModel.setVipOrder(false);
                            callSubmitReleaseAPI(dataModel);
                        } else {
                            globalclass.showCustomToast(mActivity, CheckInternetConnectionMsg, Toast.LENGTH_LONG);
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        }).show();

    }

    private void dialogToReleaseOrder(String s, final int i) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialogBuilder.setMessage(s);
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (cd.isConnectingToInternet()) {

                            /*FixAppointmentDataModel dataModel = new FixAppointmentDataModel();
                            dataModel.setBTechId(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
                            dataModel.setOrderNo(orderno);
                            dataModel.setAppointmentDate(appoinmentdate);
                            dataModel.setAppointmentSlot(slotID);
                            dataModel.setStatus(i);
                            dataModel.setRemarksId(Global.selectedRemarksID);
                            dataModel.setReasonId(remarksResponseModel.getId());
                            dataModel.setVisitId(orderno);
                            dataModel.setOthers(remarksResponseModel.getReason()+edt_other.getText().toString().trim());
                            dataModel.setUserId(52);
                            dataModel.setVipOrder(false);
                            callSubmitReleaseAPI(dataModel);*/
                            callAPIreleaseOrder(i);
                        } else {
                            globalclass.showCustomToast(mActivity, CheckInternetConnectionMsg, Toast.LENGTH_LONG);
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        }).show();
    }

    private void callSubmitReleaseAPI(FixAppointmentDataModel dataModel) {
        if (cd.isConnectingToInternet()) {
            OrderReleaseRemarksController ordc = new OrderReleaseRemarksController(this);
            ordc.updateOrderHistory(1,token, dataModel);
        } else {
            Toast.makeText(mActivity, CheckInternetConnectionMsg, Toast.LENGTH_SHORT).show();
        }
    }

    private void callAPIreleaseOrder(int i) {
        if (cd.isConnectingToInternet()) {
            OrderStatusChangeRequestModel orderStatusChangeRequestModel = new OrderStatusChangeRequestModel();
            orderStatusChangeRequestModel.setId(String.valueOf(slotID));
            orderStatusChangeRequestModel.setReasonId(remarksResponseModel.getId());
            orderStatusChangeRequestModel.setRemarksId(Integer.parseInt(remarksID));
            orderStatusChangeRequestModel.setAppointmentDate(DateUtils.Req_Date_Req(orderDetailsModel.getAppointmentDate(), "dd-MM-yyyy", "yyyy-MM-dd"));
            if (InputUtils.isNull(Str_other)) {
                orderStatusChangeRequestModel.setRemarks(Str_remarks);
            } else {
                orderStatusChangeRequestModel.setRemarks(Str_remarks + "-" + Str_other);
            }
            orderStatusChangeRequestModel.setStatus(i);
            callOrderStatusChangeApi(orderStatusChangeRequestModel);
        } else {
            globalclass.showCustomToast(mActivity, CheckInternetConnectionMsg, Toast.LENGTH_LONG);
        }
    }

    private void callOrderStatusChangeApi(OrderStatusChangeRequestModel orderStatusChangeRequestModel) {
        String id = String.valueOf(slotID);
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(mActivity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallOrderStatusChangeAPI(orderStatusChangeRequestModel, id);
        globalclass.showProgressDialog(mActivity, mActivity.getResources().getString(R.string.progress_message_changing_order_status_please_wait));
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                globalclass.hideProgressDialog(mActivity);
                if (response.code() == 200) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                    builder.setMessage("Order Released successfully")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Global.todisplaytimerforPosition = false;
                                    Global.toDisplayTimerFlag = false;
                                    dialog.dismiss();
                                    startActivity(new Intent(mActivity, B2BVisitOrdersDisplayFragment.class));
                                }
                            }).show();

                } else {
                    try {
                        Toast.makeText(mActivity, response.errorBody() != null ? response.errorBody().string() : SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
                        globalclass.hideProgressDialog(mActivity);
                    } catch (IOException e) {
                        e.printStackTrace();
                        globalclass.hideProgressDialog(mActivity);
                        Toast.makeText(mActivity, SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                globalclass.hideProgressDialog(mActivity);
                Toast.makeText(mActivity, SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayDialogTimer() {
        final Dialog dialog_batch = new Dialog(mActivity);
        dialog_batch.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog_batch.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_batch.setContentView(R.layout.dialog_pe_woe);
        dialog_batch.setCanceledOnTouchOutside(false);
        dialog_batch.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView tv_msg, tv_msg1;
        Button btn_add_on, btn_next_ord;
        RelativeLayout rel_main;

        tv_msg = dialog_batch.findViewById(R.id.tv_msg);
        tv_msg1 = dialog_batch.findViewById(R.id.tv_msg1);
        btn_add_on = dialog_batch.findViewById(R.id.btn_add_on);
        btn_next_ord = dialog_batch.findViewById(R.id.btn_next_ord);
        rel_main = dialog_batch.findViewById(R.id.rel_main);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int height = 0, width = 0;
        if (displayMetrics != null) {
            try {
                height = displayMetrics.heightPixels;
                width = displayMetrics.widthPixels;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width - 150, FrameLayout.LayoutParams.WRAP_CONTENT);
        rel_main.setLayoutParams(lp);

        tv_msg.setVisibility(View.GONE);
        tv_msg1.setText("Wait for 5 minutes for receiving the call from customer");
        btn_add_on.setText("Back");
        btn_next_ord.setText("OK");
        btn_add_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_batch.dismiss();

            }
        });

        btn_next_ord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog_batch.isShowing()) {
                    dialog_batch.dismiss();
                }
                callAPIforTimer();
            }
        });
        dialog_batch.show();
    }

    private void callAPIforTimer() {
        PECutomerIntimationSMSRequestModel smsRequestModel = null;
        try {
            smsRequestModel = new PECutomerIntimationSMSRequestModel();
            smsRequestModel.setBtechId(Integer.valueOf(appPreferenceManager.getLoginResponseModel().getUserID()));
            smsRequestModel.setOrderNo(orderno);
            smsRequestModel.setUserId(0);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (cd.isConnectingToInternet()) {
            OrderReleaseRemarksController orcController = new OrderReleaseRemarksController(this);
            if (BundleConstants.isPEPartner || BundleConstants.PEDSAOrder){
                orcController.getPE_SMS(smsRequestModel);
            }else {
                orcController.getSMS(smsRequestModel);
            }
            /*OrderReleaseRemarksController orcController = new OrderReleaseRemarksController(this);
            orcController.getPE_SMS(smsRequestModel);*/
        } else {
            Toast.makeText(mActivity, CheckInternetConnectionMsg, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkremarks() {
        if (InputUtils.isNull(Str_remarks)) {
            Toast.makeText(mActivity, "Kindly select any 1 reason", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Str_remarks.equalsIgnoreCase("Other")) {
            if (InputUtils.isNull(Str_other)) {
                Toast.makeText(mActivity, "kindly enter remarks", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (Str_other.length() < 10) {
                Toast.makeText(mActivity, "Enter minimum 10 characters", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }
        return true;
    }

    private void inITUI() {
        rec_rel_list = findViewById(R.id.rec_rel_list);
        iv_home = findViewById(R.id.iv_home);
        iv_back = findViewById(R.id.iv_back);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        btn_back = findViewById(R.id.btn_back);
        btn_submit = findViewById(R.id.btn_submit);
        edt_other = findViewById(R.id.edt_other);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rec_rel_list.setLayoutManager(linearLayoutManager);
        iv_home.setVisibility(View.GONE);
        if (Global.selectedPosition == 0) {
            tv_toolbar.setText("Select Reason for Reschedule");
        } else if (Global.selectedPosition == 1) {
            tv_toolbar.setText("Select Reason for Cancel");
        } else if (Global.selectedPosition == 2) {
            tv_toolbar.setText("Explain why");
        }
        token = "Bearer " + appPreferenceManager.getAccess_Token();
        tv_toolbar.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        setAdapter();
    }

    private void setAdapter() {
        NewOrderReleaseAdapter norAdapter = new NewOrderReleaseAdapter(this, reasonsDTOS);
        rec_rel_list.setAdapter(norAdapter);
    }

    public void smsSent(PECutomerIntimationSMSResponeModel smsResponeModel) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(smsResponeModel.getMessage().toString().trim());
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int arg1) {
                        appPreferenceManager.setOrderNo(orderno);
                        Global.toDisplayTimerFlag = true;
                        dialogInterface.dismiss();
                        mActivity.finish();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    private void CallServiceUpdateAPI() {
        ServiceUpdateRequestModel serviceUpdateRequestModel = new ServiceUpdateRequestModel();
        serviceUpdateRequestModel.setVisitId(orderno);
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(mActivity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallServiceUpdateAPI(serviceUpdateRequestModel);
        globalclass.showProgressDialog(mActivity, mActivity.getResources().getString(R.string.progress_message_changing_order_status_please_wait));
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                globalclass.hideProgressDialog(mActivity);
                if (response.code() == 200) {
                    AlertDialog alertDialog = new AlertDialog.Builder(mActivity).create();
                    alertDialog.setMessage("Order cancelled successfully");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    startActivity(new Intent(mActivity, B2BVisitOrdersDisplayFragment.class));
                                }
                            });
                    alertDialog.show();
                } else {
                    globalclass.showCustomToast(mActivity, SomethingWentwrngMsg);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                globalclass.hideProgressDialog(mActivity);
                MessageLogger.LogDebug("Errror", t.getMessage());
            }
        });
    }

    public void getSelection(GetPECancelRemarksResponseModel getPECancelRemarksResponseModel) {
        remarksResponseModel = getPECancelRemarksResponseModel;
        if (remarksResponseModel.getId().equals(BundleConstants.ToDisplayTimerOnSelectedPosition)) {
            Global.todisplaytimerforPosition = true;
        } else {
            Global.todisplaytimerforPosition = false;
        }

        if (remarksResponseModel.getReason().equalsIgnoreCase("Other") ||
                remarksResponseModel.getReason().equalsIgnoreCase("Others")) {
            Str_remarks = remarksResponseModel.getReason();
            edt_other.setVisibility(View.VISIBLE);
        } else {
            Str_remarks = remarksResponseModel.getReason();
            edt_other.setVisibility(View.GONE);
        }
    }

    public void slotSubmitresponse(ResponseModel responseModel) {
        if (responseModel != null) {
            if (responseModel.getResponse().equalsIgnoreCase("SUCCESS")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle("Order Status")
                        .setMessage("Order Cancelled successfully")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(mActivity, B2BVisitOrdersDisplayFragment.class));
                            }
                        })
                        .setCancelable(false)
                        .show();
            } else {
                TastyToast.makeText(mActivity, responseModel.getResponseMessage(), TastyToast.LENGTH_SHORT, TastyToast.WARNING);
            }
        } else {
            Toast.makeText(mActivity, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
}