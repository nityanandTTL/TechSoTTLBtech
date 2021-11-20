package com.thyrocare.btechapp.Controller;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.CheckoutWoeActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.LogUserActivityTagging;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.HomeScreenActivity;
import com.thyrocare.btechapp.activity.KIOSK_Scanner_Activity;
import com.thyrocare.btechapp.activity.PaymentsActivity;
import com.thyrocare.btechapp.models.api.request.OrderBookingRequestModel;
import com.thyrocare.btechapp.models.data.OrderVisitDetailsModel;
import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.AppConstants;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;

import static android.widget.Toast.LENGTH_SHORT;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.PLEASE_WAIT;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SOMETHING_WENT_WRONG;

public class WOEController {
    Activity activity;
    Global globalClass;
    CheckoutWoeActivity checkoutWoeActivity;
    PaymentsActivity paymentsActivity;
    AppPreferenceManager appPreferenceManager;
    int flag;

    public WOEController(CheckoutWoeActivity checkoutWoeActivity) {
        this.activity = checkoutWoeActivity;
        this.checkoutWoeActivity = checkoutWoeActivity;
        globalClass = new Global(activity);
        appPreferenceManager =new AppPreferenceManager(activity);
        flag = 1;

    }
    public WOEController(PaymentsActivity paymentsActivity) {
        this.activity = paymentsActivity;
        this.paymentsActivity = paymentsActivity;
        globalClass = new Global(activity);
        appPreferenceManager =new AppPreferenceManager(activity);
        flag = 2;

    }

    public void CallWorkOrderEntryAPI(OrderBookingRequestModel orderBookingRequestModel, final OrderVisitDetailsModel orderVisitDetailsModel, final String test, final String newTimeaddTwoHrs, final String newTimeaddTwoHalfHrs, final TextView btn_Pay) {

        for (int i = 0; i <orderBookingRequestModel.getBendtl().size() ; i++) {
            if (!InputUtils.isNull(orderBookingRequestModel.getBendtl().get(i).getVenepuncture())){
                orderBookingRequestModel.getBendtl().get(i).setVenepuncture("");
            }
        }

        for (int i = 0; i < orderBookingRequestModel.getOrdbooking().getOrddtl().size(); i++) {
            for (int j = 0; j < orderBookingRequestModel.getOrdbooking().getOrddtl().get(i).getBenMaster().size(); j++) {
                if (!InputUtils.isNull(orderBookingRequestModel.getOrdbooking().getOrddtl().get(i).getBenMaster().get(j).getVenepuncture())){
                    orderBookingRequestModel.getOrdbooking().getOrddtl().get(i).getBenMaster().get(j).setVenepuncture("");
                }
            }

        }

        for (int i = 0; i <orderBookingRequestModel.getOrddtl().size(); i++) {
            for (int j = 0; j < orderBookingRequestModel.getOrddtl().get(i).getBenMaster().size(); j++) {
                if (!InputUtils.isNull(orderBookingRequestModel.getOrddtl().get(i).getBenMaster().get(j).getVenepuncture())){
                    orderBookingRequestModel.getOrddtl().get(i).getBenMaster().get(j).setVenepuncture("");
                }
            }
        }


        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallWorkOrderEntryAPI(orderBookingRequestModel);
        String abc = new Gson().toJson(orderBookingRequestModel);
        System.out.println("" + abc);
        globalClass.showProgressDialog(activity, PLEASE_WAIT);
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> res) {
                globalClass.hideProgressDialog(activity);
                if (res.isSuccessful() && res.body() != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Order Status")
                            .setCancelable(false)
                            .setMessage("Work order entry successful!\nPlease note ref id - " + orderVisitDetailsModel.getVisitId() + " for future references.")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String remark = orderVisitDetailsModel.getVisitId();
                                    new LogUserActivityTagging(activity, BundleConstants.WOE, remark);
                                    if (test.toUpperCase().contains(AppConstants.PPBS) && test.toUpperCase().contains(AppConstants.FBS)
                                            && test.toUpperCase().contains("INSPP") && test.toUpperCase().contains("INSFA")
                                            && test.toUpperCase().contains(AppConstants.RBS) && test.toUpperCase().contains(AppConstants.FBS)) {
                                        Logger.error("should print revisit dialog for both: ");
                                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                        builder.setMessage("Please note you have to revisit at customer place to collect sample for PPBS/RBS and INSPP in between " + newTimeaddTwoHrs + " to " + newTimeaddTwoHalfHrs)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Constants.Finish_barcodeScanAcitivty = true;
                                                        Constants.isWOEDone = true;
                                                        ResetOTPFlag();
                                                        if (BundleConstants.isKIOSKOrder) {
                                                            activity.finish();
                                                            Intent intent = new Intent(activity, KIOSK_Scanner_Activity.class);
                                                            activity.startActivity(intent);
                                                        } else {
                                                           activity.startActivity(new Intent(activity, HomeScreenActivity.class));
//                                                            mActivity.finish();
                                                        }
                                                    }
                                                })
                                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .setCancelable(false)
                                                .show();

                                    } else if (test.toUpperCase().contains(AppConstants.PPBS)
                                            && test.toUpperCase().contains(AppConstants.RBS)
                                            && test.toUpperCase().contains(AppConstants.FBS)) {

                                        Logger.error("should print revisit dialog for ppbs and rbs: ");

                                        Logger.error("for PPBS and RBS");
                                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                        builder.setMessage("Please note you have to revisit at customer place to collect sample for PPBS and RBS in between " + newTimeaddTwoHrs + " to " + newTimeaddTwoHalfHrs)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Constants.Finish_barcodeScanAcitivty = true;
                                                        Constants.isWOEDone = true;
                                                        ResetOTPFlag();
                                                        if (BundleConstants.isKIOSKOrder) {
                                                            activity.finish();
                                                            Intent intent = new Intent(activity, KIOSK_Scanner_Activity.class);
                                                            activity.startActivity(intent);
                                                        } else {
                                                            activity.finish();
                                                        }
                                                    }
                                                })
                                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .setCancelable(false)
                                                .show();

                                    } else if (test.toUpperCase().contains(AppConstants.INSPP) && test.toUpperCase().contains(AppConstants.INSFA)) {
                                        Logger.error("should print revisit dialog for insfa: ");
                                        Logger.error("for INSPP");
                                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                        builder.setMessage("Please note you have to revisit at customer place to collect sample for INSPP in between " + newTimeaddTwoHrs + " to " + newTimeaddTwoHalfHrs)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Constants.Finish_barcodeScanAcitivty = true;
                                                        Constants.isWOEDone = true;
                                                        ResetOTPFlag();
                                                        if (BundleConstants.isKIOSKOrder) {
                                                           activity.finish();
                                                            Intent intent = new Intent(activity, HomeScreenActivity.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            activity.startActivity(intent);
                                                        } else {
                                                            activity.finish();
                                                        }
                                                    }
                                                })
                                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .setCancelable(false)
                                                .show();

                                    } else if (test.toUpperCase().contains(AppConstants.RBS) && test.toUpperCase().contains(AppConstants.FBS)) {
                                        Logger.error("should print revisit dialog for rbs: ");
                                        Logger.error("for rbs");
                                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                        builder.setMessage("Please note you have to revisit at customer place to collect sample for RBS in between " + newTimeaddTwoHrs + " to " + newTimeaddTwoHalfHrs)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Constants.Finish_barcodeScanAcitivty = true;
                                                        Constants.isWOEDone = true;
                                                        ResetOTPFlag();
                                                        if (BundleConstants.isKIOSKOrder) {
                                                            activity.finish();
                                                            Intent intent = new Intent(activity, HomeScreenActivity.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            activity.startActivity(intent);
                                                        } else {
                                                            activity.finish();
                                                        }
                                                    }
                                                })
                                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .setCancelable(false)
                                                .show();

                                    } else if (test.toUpperCase().contains(AppConstants.PPBS) && test.toUpperCase().contains(AppConstants.FBS)) {
                                        Logger.error("should print revisit dialog for rbs: ");
                                        Logger.error("for ppbs");
                                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                        builder.setMessage("Please note you have to revisit at customer place to collect sample for PPBS in between " + newTimeaddTwoHrs + " to " + newTimeaddTwoHalfHrs)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Constants.Finish_barcodeScanAcitivty = true;
                                                        Constants.isWOEDone = true;
                                                        ResetOTPFlag();
                                                        if (BundleConstants.isKIOSKOrder) {
                                                            activity.finish();
                                                            Intent intent = new Intent(activity, HomeScreenActivity.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            activity.startActivity(intent);
                                                        } else {
                                                            activity.finish();
                                                        }
                                                    }
                                                })
                                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .setCancelable(false)
                                                .show();
                                    } else {
                                        Logger.error("testcode in else " + test.toUpperCase());
                                        Constants.Finish_barcodeScanAcitivty = true;
                                        Constants.isWOEDone = true;
                                        ResetOTPFlag();
                                        if (BundleConstants.isKIOSKOrder) {
                                            activity.finish();
                                            Intent intent = new Intent(activity, HomeScreenActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            activity.startActivity(intent);
                                        } else {
                                            activity.finish();
                                        }
                                    }
                                }
                            })
                            .create();

                    if (!activity.isFinishing()) {
                        builder.show();
                    }
                } else {
                    try {
                        if (res.errorBody() != null) {
                            JSONObject jObjError = new JSONObject(res.errorBody().string());
                            globalClass.showCustomToast(activity, jObjError.optString("Message", SOMETHING_WENT_WRONG));
                        } else {
                            globalClass.showCustomToast(activity, SOMETHING_WENT_WRONG);
                        }
                    } catch (Exception e) {
                        globalClass.showCustomToast(activity, SOMETHING_WENT_WRONG);
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Order Status")
                            .setMessage("Work Order Entry Failed!")
                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Constants.Finish_barcodeScanAcitivty = true;
                                    Constants.isWOEDone = true;
                                    activity.finish();
                                }
                            })
                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (flag == 1){
                                        btn_Pay.performClick();
                                    }else{

                                    }

                                }
                            })
                            .create();

                    if (!activity.isFinishing()) {
                        builder.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                globalClass.hideProgressDialog(activity);
                Toast.makeText(activity, SOMETHING_WENT_WRONG, LENGTH_SHORT).show();
            }
        });
    }

    private void ResetOTPFlag() {
        if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())){
            if(BundleConstants.callOTPFlag == 1){
                BundleConstants.callOTPFlag=0;
            }
        }else{
            BundleConstants.callOTPFlag=0;
        }
    }
}


