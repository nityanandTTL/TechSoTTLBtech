package com.thyrocare.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.NewScreenDesigns.Activities.SplashActivity;
import com.thyrocare.R;
import com.thyrocare.delegate.ConfirmOrderReleaseDialogButtonClickedDelegate;
import com.thyrocare.dialog.ConfirmRequestReleaseDialog;
import com.thyrocare.models.api.request.OrderStatusChangeRequestModel;
import com.thyrocare.models.data.OrderVisitDetailsModel;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.utils.app.BundleConstants;

import org.json.JSONException;

import static com.thyrocare.utils.api.NetworkUtils.isNetworkAvailable;

public class NotificationClickActivity extends AppCompatActivity {

    NotificationClickActivity mActivity;
    Context mContext;
    String visitID = "";
    String NotifID = "";
    String NotifSlotID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_click);
        mActivity = this;
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

                    System.out.println("Nitya >> " + visitID + " >> " + NotifID);
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
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(mActivity);
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
        builder1.show();
    }

    private void CallConfirmRequestReleaseDialog() {
        OrderVisitDetailsModel orderVisitDetailsModel = new OrderVisitDetailsModel();
        ConfirmRequestReleaseDialog crr = new ConfirmRequestReleaseDialog(mContext, new CConfirmOrderReleaseDialogButtonClickedDelegateResult(), orderVisitDetailsModel);
        crr.show();
    }

    private void CallAcceptOrderAPI(String slotId) {
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(mActivity);
        OrderStatusChangeRequestModel orderStatusChangeRequestModel = new OrderStatusChangeRequestModel();
        orderStatusChangeRequestModel.setId(slotId + "");
        orderStatusChangeRequestModel.setRemarks("");
        orderStatusChangeRequestModel.setStatus(8);
        ApiCallAsyncTask orderStatusChangeApiAsyncTask = asyncTaskForRequest.getOrderStatusChangeRequestAsyncTask(orderStatusChangeRequestModel);
        orderStatusChangeApiAsyncTask.setApiCallAsyncTaskDelegate(new OrderStatusChangeConfirmedApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(mActivity)) {
            orderStatusChangeApiAsyncTask.execute(orderStatusChangeApiAsyncTask);
        } else {
            TastyToast.makeText(mContext, getString(R.string.internet_connetion_error), TastyToast.LENGTH_LONG, TastyToast.ERROR);
            //  Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private class OrderStatusChangeConfirmedApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 204 || statusCode == 200) {
                BundleConstants.ORDER_Notification = true;
                TastyToast.makeText(mActivity, "Order Accepted Successfully", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                CallRestartApp();
            } else {
                TastyToast.makeText(mActivity, "" + json, TastyToast.LENGTH_LONG, TastyToast.INFO);
            }
        }

        @Override
        public void onApiCancelled() {

        }
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

    public class CConfirmOrderReleaseDialogButtonClickedDelegateResult implements ConfirmOrderReleaseDialogButtonClickedDelegate {
        @Override
        public void onOkButtonClicked(OrderVisitDetailsModel orderVisitDetailsModel, String remarks) {
            AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(mActivity);
            OrderStatusChangeRequestModel orderStatusChangeRequestModel = new OrderStatusChangeRequestModel();
            orderStatusChangeRequestModel.setId(NotifSlotID);
            orderStatusChangeRequestModel.setRemarks(remarks);
            orderStatusChangeRequestModel.setStatus(27);
            ApiCallAsyncTask orderStatusChangeApiAsyncTask = asyncTaskForRequest.getOrderStatusChangeRequestAsyncTask(orderStatusChangeRequestModel);
            orderStatusChangeApiAsyncTask.setApiCallAsyncTaskDelegate(new OrderStatusChangeApiAsyncTaskDelegateResult());
            if (isNetworkAvailable(mActivity)) {
                orderStatusChangeApiAsyncTask.execute(orderStatusChangeApiAsyncTask);
            } else {
                TastyToast.makeText(mActivity, getString(R.string.internet_connetion_error), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                //  Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelButtonClicked() {
            CallRestartApp();
        }
    }

    private class OrderStatusChangeApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 204 || statusCode == 200) {
                BundleConstants.ORDER_Notification = true;
                TastyToast.makeText(mActivity, "Order Released Successfully", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                CallRestartApp();
            } else {
                TastyToast.makeText(mActivity, "" + json, TastyToast.LENGTH_LONG, TastyToast.INFO);
            }
        }

        @Override
        public void onApiCancelled() {
            TastyToast.makeText(mActivity, "Network Error", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
        }
    }
}
