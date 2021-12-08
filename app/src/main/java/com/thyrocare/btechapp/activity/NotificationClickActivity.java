package com.thyrocare.btechapp.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.SplashActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.delegate.ConfirmOrderReleaseDialogButtonClickedDelegate;
import com.thyrocare.btechapp.dialog.ConfirmRequestReleaseDialog;
import com.thyrocare.btechapp.models.api.request.OrderStatusChangeRequestModel;
import com.thyrocare.btechapp.models.data.OrderVisitDetailsModel;


import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.Global;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;
import static com.thyrocare.btechapp.utils.api.NetworkUtils.isNetworkAvailable;

public class NotificationClickActivity extends AppCompatActivity {

    NotificationClickActivity mActivity;
    Context mContext;
    String visitID = "";
    String NotifID = "";
    String NotifSlotID = "";
    private Global global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_click);
        mActivity = this;
        global = new Global(mActivity);
        mContext = this;

        if (getIntent().getAction() != null) {

        } else {
            finish();
        }

        processIntentAction(getIntent());
    }

    private void processIntentAction(Intent intent) {
        try {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            try {
                if (intent.getExtras() != null) {
                    visitID = getIntent().getExtras().getString(BundleConstants.VISIT_ID);
                    NotifID = getIntent().getExtras().getString(BundleConstants.YESNO_ID);
                    NotifSlotID = getIntent().getExtras().getString(BundleConstants.ORDER_SLOTID);

                    MessageLogger.PrintMsg("Nitya >> " + visitID + " >> " + NotifID);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            if (intent.getAction() != null) {
                switch (intent.getAction()) {
                    case BundleConstants.YES_ACTION:
                        Toast.makeText(this, "Yes :)", Toast.LENGTH_SHORT).show();
                        notificationManager.cancel(Integer.parseInt(NotifID));
                        CallAcceptOrderAPI(NotifSlotID);
                        break;
                    case BundleConstants.NO_ACTION:
                        Toast.makeText(this, "No :|", Toast.LENGTH_SHORT).show();
                        notificationManager.cancel(Integer.parseInt(NotifID));
                        CallRejectDialog();
                        break;

                    default:
                        finish();
                        break;

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void CallRejectDialog() {
        CallConfirmRequestReleaseDialog();
        //TODO Removed Relese pop up
       /* final AlertDialog.Builder builder1 = new AlertDialog.Builder(mActivity);
        builder1.setTitle("Warning ")
                .setMessage("Rs 200 debit will be levied for Releasing this Order ").setPositiveButton("Accept Debit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                CallConfirmRequestReleaseDialog();
            }
        }).setNegativeButton("Cancel Request", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                CallRestartApp();
            }
        });
        builder1.show();*/
    }

    private void CallConfirmRequestReleaseDialog() {
        OrderVisitDetailsModel orderVisitDetailsModel = new OrderVisitDetailsModel();
        ConfirmRequestReleaseDialog crr = new ConfirmRequestReleaseDialog(mActivity, new CConfirmOrderReleaseDialogButtonClickedDelegateResult(), orderVisitDetailsModel);
        crr.show();
    }

    private void CallAcceptOrderAPI(String slotId) {

        OrderStatusChangeRequestModel orderStatusChangeRequestModel = new OrderStatusChangeRequestModel();
        orderStatusChangeRequestModel.setId(slotId + "");
        orderStatusChangeRequestModel.setRemarks("");
        orderStatusChangeRequestModel.setStatus(8);

        if (isNetworkAvailable(mActivity)) {
            callOrderStatusChangeApi(orderStatusChangeRequestModel);
        } else {
            TastyToast.makeText(mContext, getString(R.string.internet_connetion_error), TastyToast.LENGTH_LONG, TastyToast.ERROR);
            //  Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    public class CConfirmOrderReleaseDialogButtonClickedDelegateResult implements ConfirmOrderReleaseDialogButtonClickedDelegate {
        @Override
        public void onOkButtonClicked(OrderVisitDetailsModel orderVisitDetailsModel, String remarks) {

            OrderStatusChangeRequestModel orderStatusChangeRequestModel = new OrderStatusChangeRequestModel();
            orderStatusChangeRequestModel.setId(NotifSlotID);
            orderStatusChangeRequestModel.setRemarks(remarks);
            orderStatusChangeRequestModel.setStatus(27);
            if (isNetworkAvailable(mActivity)) {
                callOrderStatusChangeApi(orderStatusChangeRequestModel);
            } else {
                TastyToast.makeText(mContext, getString(R.string.internet_connetion_error), TastyToast.LENGTH_LONG, TastyToast.ERROR);
            }
        }

        @Override
        public void onCancelButtonClicked() {
            CallRestartApp();
        }
    }

    private void callOrderStatusChangeApi(final OrderStatusChangeRequestModel orderStatusChangeRequestModel) {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallOrderStatusChangeAPI(orderStatusChangeRequestModel, orderStatusChangeRequestModel.getId());
        global.showProgressDialog(mActivity, ConstantsMessages.PLEASE_WAIT);

        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                global.hideProgressDialog(mActivity);
                if (response.code() == 200 || response.code() == 204) {
                    BundleConstants.ORDER_Notification = true;
                    if (orderStatusChangeRequestModel.getStatus() == 8) {
                        TastyToast.makeText(mActivity, "Order Accepted Successfully", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                    } else {
                        TastyToast.makeText(mActivity, "Order Released Successfully", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                    }
                    CallRestartApp();
                } else {
                    try {
                        global.showCustomToast(mActivity, response.errorBody() != null ? response.errorBody().string() : SomethingWentwrngMsg, Toast.LENGTH_LONG);
//                        Toast.makeText(mActivity, response.errorBody() != null ? response.errorBody().string() : SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(mActivity, SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                global.hideProgressDialog(mActivity);
                Toast.makeText(mActivity, SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CallRestartApp() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               /* Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);*/

                Intent i = new Intent(mContext, SplashActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                mActivity.finish();
            }
        }, 100);

    }

}
