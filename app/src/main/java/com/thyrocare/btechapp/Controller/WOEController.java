package com.thyrocare.btechapp.Controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.AddEditBenificaryActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.CheckoutWoeActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.SplashActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Fragments.B2BVisitOrdersDisplayFragment;
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
import com.thyrocare.btechapp.models.api.response.FetchOrderDetailsResponseModel;
import com.thyrocare.btechapp.models.data.OrderVisitDetailsModel;
import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import org.json.JSONObject;

import java.util.ArrayList;

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
    private ArrayList<OrderVisitDetailsModel> orderDetailsResponseModels = new ArrayList<>();
    boolean companyFlag;


    public WOEController(CheckoutWoeActivity checkoutWoeActivity) {
        this.activity = checkoutWoeActivity;
        this.checkoutWoeActivity = checkoutWoeActivity;
        globalClass = new Global(activity);
        appPreferenceManager = new AppPreferenceManager(activity);
        flag = 1;

    }

    public WOEController(PaymentsActivity paymentsActivity) {
        this.activity = paymentsActivity;
        this.paymentsActivity = paymentsActivity;
        globalClass = new Global(activity);
        appPreferenceManager = new AppPreferenceManager(activity);
        flag = 2;

    }

    public void CallWorkOrderEntryAPI(OrderBookingRequestModel orderBookingRequestModel, final OrderVisitDetailsModel orderVisitDetailsModel, final String test, final String newTimeaddTwoHrs, final String newTimeaddTwoHalfHrs, final TextView btn_Pay) {

        try {
            companyFlag = orderVisitDetailsModel.getAllOrderdetails().get(0).isPEPartner();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            for (int i = 0; i < orderBookingRequestModel.getBendtl().size(); i++) {
                if (!InputUtils.isNull(orderBookingRequestModel.getBendtl().get(i).getVenepuncture())) {
                    orderBookingRequestModel.getBendtl().get(i).setVenepuncture("");
                }
            }

            for (int i = 0; i < orderBookingRequestModel.getOrdbooking().getOrddtl().size(); i++) {
                for (int j = 0; j < orderBookingRequestModel.getOrdbooking().getOrddtl().get(i).getBenMaster().size(); j++) {
                    if (!InputUtils.isNull(orderBookingRequestModel.getOrdbooking().getOrddtl().get(i).getBenMaster().get(j).getVenepuncture())) {
                        orderBookingRequestModel.getOrdbooking().getOrddtl().get(i).getBenMaster().get(j).setVenepuncture("");
                    }
                }

            }

            for (int i = 0; i < orderBookingRequestModel.getOrddtl().size(); i++) {
                for (int j = 0; j < orderBookingRequestModel.getOrddtl().get(i).getBenMaster().size(); j++) {
                    if (!InputUtils.isNull(orderBookingRequestModel.getOrddtl().get(i).getBenMaster().get(j).getVenepuncture())) {
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
                        //fungible
                        if (BundleConstants.isPEPartner) {
//                        if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())) {

                            //TODO local storage for B2B login
                            //                        RemoveOrderFromLocal(orderVisitDetailsModel.getVisitId());
                            //    clearOrderFromLocal(orderVisitDetailsModel.getVisitId());
                            toDisplayWOEDialogPE(orderVisitDetailsModel, test, newTimeaddTwoHrs, newTimeaddTwoHalfHrs);
                        } else {
                            //                        RemoveOrderFromLocal(orderVisitDetailsModel.getVisitId());
                            toDisplayWOEDialogTC(orderVisitDetailsModel, test, newTimeaddTwoHrs, newTimeaddTwoHalfHrs);
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
                                        if (flag == 1) {
                                            btn_Pay.performClick();
                                        } else {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void toDisplayWOEDialogPE(final OrderVisitDetailsModel orderVisitDetailsModel, final String test, final String newTimeaddTwoHrs, final String newTimeaddTwoHalfHrs) {

        final Dialog dialog_batch = new Dialog(activity);
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
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

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

        tv_msg.setText("Order Served");
        tv_msg1.setText("Work order entry successful.");


        btn_add_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    boolean FlagADDEditBen = true;
                    int peAddben = 1;
                    Intent intent = new Intent(checkoutWoeActivity, AddEditBenificaryActivity.class);
                    intent.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL, orderVisitDetailsModel);
                    intent.putExtra(Constants.PEAddBen, peAddben);
                    intent.putExtra("IsAddBen", FlagADDEditBen);
                    activity.startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    dialog_batch.dismiss();
                    activity.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btn_next_ord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialog_batch.dismiss();
                    String remark = orderVisitDetailsModel.getVisitId();
                    new LogUserActivityTagging(activity, BundleConstants.WOE, remark);


                    if (BundleConstants.setSecondOrderFlag) {
                        BundleConstants.setSecondOrderFlag = false;
                        Logger.error("should print revisit dialog for ppbs and rbs: ");

                        Logger.error("for PPBS and RBS");
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//                        builder.setMessage("Please note you have to revisit at customer place to collect sample for PPBS and RBS in between " + newTimeaddTwoHrs + " to " + newTimeaddTwoHalfHrs)
                        builder.setMessage("Please note you have to revisit at customer place to collect sample for non-fasting tests/profiles in between " + newTimeaddTwoHrs + " to " + newTimeaddTwoHalfHrs)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Constants.Finish_barcodeScanAcitivty = true;
                                        Constants.isWOEDone = true;
                                        BundleConstants.isPEPartner = false;
                                        BundleConstants.PEDSAOrder = false;
                                        ResetOTPFlag();
                                        if (BundleConstants.isKIOSKOrder) {
                                            activity.finish();
                                            Intent intent = new Intent(activity, KIOSK_Scanner_Activity.class);
                                            activity.startActivity(intent);
                                        } else {
                                            Intent intent = new Intent(activity, B2BVisitOrdersDisplayFragment.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            activity.startActivity(intent);
//                                            activity.startActivity(new Intent(activity, B2BVisitOrdersDisplayFragment.class));
                                            //activity.finish();
                                        }
                                    }
                                })
                                /*.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })*/
                                .setCancelable(false)
                                .show();
                    } else {


                        Logger.error("testcode in else " + test.toUpperCase());
                        Constants.Finish_barcodeScanAcitivty = true;
                        Constants.isWOEDone = true;
                        BundleConstants.isPEPartner = false;
                        BundleConstants.PEDSAOrder = false;
                        ResetOTPFlag();
                        if (BundleConstants.isKIOSKOrder) {
                            activity.finish();
                            Intent intent = new Intent(activity, HomeScreenActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            activity.startActivity(intent);
                        } else {
                            Intent intent = new Intent(activity, B2BVisitOrdersDisplayFragment.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            activity.startActivity(intent);
//                            activity.startActivity(new Intent(activity, B2BVisitOrdersDisplayFragment.class));
//                            activity.finish();
                        }
/*
                        //TODO clear order from local after WOE done
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

                                                restart(activity);
                                            *//*Intent intent = new Intent(activity, B2BVisitOrdersDisplayFragment.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            activity.startActivity(intent);*//*
//                                            activity.startActivity(new Intent(activity, HomeScreenActivity.class));
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
                                                Intent intent = new Intent(activity, B2BVisitOrdersDisplayFragment.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                activity.startActivity(intent);
//                                            activity.startActivity(new Intent(activity, B2BVisitOrdersDisplayFragment.class));
                                                //activity.finish();
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
                                                Intent intent = new Intent(activity, B2BVisitOrdersDisplayFragment.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                activity.startActivity(intent);
//                                            activity.startActivity(new Intent(activity, B2BVisitOrdersDisplayFragment.class));
//                                            activity.finish();
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
                                                Intent intent = new Intent(activity, B2BVisitOrdersDisplayFragment.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                activity.startActivity(intent);
//                                            activity.startActivity(new Intent(activity, B2BVisitOrdersDisplayFragment.class));
//                                            activity.finish();
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
                                                Intent intent = new Intent(activity, B2BVisitOrdersDisplayFragment.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                activity.startActivity(intent);
//                                            activity.startActivity(new Intent(activity, B2BVisitOrdersDisplayFragment.class));
//                                            activity.finish();
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
                                Intent intent = new Intent(activity, B2BVisitOrdersDisplayFragment.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                activity.startActivity(intent);
//                            activity.startActivity(new Intent(activity, B2BVisitOrdersDisplayFragment.class));
//                            activity.finish();
                            }
                        }*/
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        dialog_batch.show();

        /*AlertDialog.Builder builder = new AlertDialog.Builder(activity);
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
        }*/
    }

    private void RemoveOrderFromLocal(String orderVisitID) {
        try {
            FetchOrderDetailsResponseModel fetchOrderDetailsResponseModel = appPreferenceManager.getfetchOrderDetailsResponseModel();
            if (fetchOrderDetailsResponseModel != null) {
                if (fetchOrderDetailsResponseModel.getOrderVisitDetails() != null) {
                    for (int i = 0; i < fetchOrderDetailsResponseModel.getOrderVisitDetails().size(); i++) {
                        System.out.println("Order ID >> " + orderVisitID + " >> " + fetchOrderDetailsResponseModel.getOrderVisitDetails().get(i).getVisitId());
                        if (orderVisitID.toString().trim().equalsIgnoreCase(fetchOrderDetailsResponseModel.getOrderVisitDetails().get(i).getVisitId().toString().trim())) {
                            fetchOrderDetailsResponseModel.getOrderVisitDetails().remove(i);
                            break;
                        }
                    }
                }
            }
            appPreferenceManager.setfetchOrderDetailsResponseModel(fetchOrderDetailsResponseModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void toDisplayWOEDialogTC(final OrderVisitDetailsModel orderVisitDetailsModel, final String test, final String newTimeaddTwoHrs, final String newTimeaddTwoHalfHrs) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Order Status")
                    .setCancelable(false)
                    .setMessage("Work order entry successful!\nPlease note ref id - " + orderVisitDetailsModel.getVisitId() + " for future references.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String remark = orderVisitDetailsModel.getVisitId();
                            new LogUserActivityTagging(activity, BundleConstants.WOE, remark);

                            if (BundleConstants.setSecondOrderFlag) {
                                BundleConstants.setSecondOrderFlag = false;
                                Logger.error("should print revisit dialog for ppbs and rbs: ");

                                Logger.error("for PPBS and RBS");
                                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//                        builder.setMessage("Please note you have to revisit at customer place to collect sample for PPBS and RBS in between " + newTimeaddTwoHrs + " to " + newTimeaddTwoHalfHrs)
                                builder.setMessage("Please note you have to revisit at customer place to collect sample for non-fasting tests/profiles in between " + newTimeaddTwoHrs + " to " + newTimeaddTwoHalfHrs)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Constants.Finish_barcodeScanAcitivty = true;
                                                Constants.isWOEDone = true;
                                                BundleConstants.isPEPartner = false;
                                                BundleConstants.PEDSAOrder = false;
                                                ResetOTPFlag();
                                                if (BundleConstants.isKIOSKOrder) {
                                                    activity.finish();
                                                    Intent intent = new Intent(activity, KIOSK_Scanner_Activity.class);
                                                    activity.startActivity(intent);
                                                } else {
                                                    Intent intent = new Intent(activity, B2BVisitOrdersDisplayFragment.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    activity.startActivity(intent);
//                                            activity.startActivity(new Intent(activity, B2BVisitOrdersDisplayFragment.class));
                                                    //activity.finish();
                                                }
                                            }
                                        })
                                        /*.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })*/
                                        .setCancelable(false)
                                        .show();
                            } else {
                                Logger.error("testcode in else " + test.toUpperCase());
                                Constants.Finish_barcodeScanAcitivty = true;
                                Constants.isWOEDone = true;
                                BundleConstants.isPEPartner = false;
                                BundleConstants.PEDSAOrder = false;
                                ResetOTPFlag();
                                if (BundleConstants.isKIOSKOrder) {
                                    activity.finish();
                                    Intent intent = new Intent(activity, HomeScreenActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    activity.startActivity(intent);
                                } else {
                                    Intent intent = new Intent(activity, B2BVisitOrdersDisplayFragment.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    activity.startActivity(intent);
//                            activity.startActivity(new Intent(activity, B2BVisitOrdersDisplayFragment.class));
//                            activity.finish();
                                }
                            }

                            /*if (test.toUpperCase().contains(AppConstants.PPBS) && test.toUpperCase().contains(AppConstants.FBS)
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
                                                    //                                                activity.finish();
                                                    Intent intent = new Intent(activity, KIOSK_Scanner_Activity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    activity.startActivity(intent);
                                                } else {
                                                    Intent intent = new Intent(activity, B2BVisitOrdersDisplayFragment.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    activity.startActivity(intent);
                                                    //                                                activity.startActivity(new Intent(activity, B2BVisitOrdersDisplayFragment.class));
                                                    //                                                activity.startActivity(new Intent(activity, HomeScreenActivity.class));
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
                                                    Intent intent = new Intent(activity, B2BVisitOrdersDisplayFragment.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    activity.startActivity(intent);
                                                    //                                                activity.startActivity(new Intent(activity, B2BVisitOrdersDisplayFragment.class));
                                                    //                                                activity.finish();
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
                                                    Intent intent = new Intent(activity, B2BVisitOrdersDisplayFragment.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    activity.startActivity(intent);
                                                    //                                                activity.startActivity(new Intent(activity, B2BVisitOrdersDisplayFragment.class));
                                                    //                                                activity.finish();
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
                                                    Intent intent = new Intent(activity, B2BVisitOrdersDisplayFragment.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    activity.startActivity(intent);
                                                    //                                                activity.startActivity(new Intent(activity, B2BVisitOrdersDisplayFragment.class));
                                                    //                                                activity.finish();
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
                                                    Intent intent = new Intent(activity, B2BVisitOrdersDisplayFragment.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    activity.startActivity(intent);
                                                    //                                                activity.finish();
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
                                    Intent intent = new Intent(activity, B2BVisitOrdersDisplayFragment.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    activity.startActivity(intent);
                                    //                                activity.startActivity(new Intent(activity, B2BVisitOrdersDisplayFragment.class));
                                    //                                activity.finish();
                                }
                            }*/
                        }
                    }).create();

            if (!activity.isFinishing()) {
                builder.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ResetOTPFlag() {
        try {
            //fungible
            if (BundleConstants.isPEPartner) {
//            if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())) {
                if (BundleConstants.callOTPFlag == 1) {
                    BundleConstants.callOTPFlag = 0;
                }
            } else {
                BundleConstants.callOTPFlag = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void restart(Activity activity) {
        try {
            BundleConstants.setVisitOrderScreen = true;
            Intent intent = new Intent(activity, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
            activity.finishAffinity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


