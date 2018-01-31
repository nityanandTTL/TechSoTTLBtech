package com.thyrocare.dialog;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.R;
import com.thyrocare.activity.HomeScreenActivity;
import com.thyrocare.delegate.ConfirmOrderReleaseDialogButtonClickedDelegate;
import com.thyrocare.delegate.refreshDelegate;
import com.thyrocare.fragment.VisitOrdersDisplayFragment;
import com.thyrocare.models.api.request.OrderPassRequestModel;
import com.thyrocare.models.api.request.OrderStatusChangeRequestModel;
import com.thyrocare.models.api.response.LoginResponseModel;
import com.thyrocare.models.api.response.OrderPassresponseModel;
import com.thyrocare.models.api.response.RemarksResponseModel;
import com.thyrocare.models.data.OrderVisitDetailsModel;
import com.thyrocare.models.data.Orderallocation;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.network.ResponseParser;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.AppConstants;
import com.thyrocare.utils.app.AppPreference;
import com.thyrocare.utils.app.AppPreferenceManager;
import com.thyrocare.utils.app.InputUtils;

import org.json.JSONException;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;
import static com.thyrocare.utils.api.NetworkUtils.isNetworkAvailable;

/**
 * Created by Orion on 4/24/2017.
 */

public class ConfirmOrderPassDialog extends Dialog implements View.OnClickListener {
    private HomeScreenActivity activity;
    private Dialog d;
    private Button btn_yes, btn_no;
    private TextView tv_title;
    private Spinner sp_btech;
    private OrderPassresponseModel orderPassresponseModel;
    private ArrayList<Orderallocation> orderallocationsarr;
    private ArrayList<String> Btecharr;
    private refreshDelegate RefreshDelegate;
    private Orderallocation orderallocationmodel;
    private OrderVisitDetailsModel orderVisitDetailsModel;
    private OrderPassRequestModel orderPassRequestModel;
    private AppPreferenceManager appPreferenceManager;
    private String Pincode;


    public ConfirmOrderPassDialog(HomeScreenActivity activity, refreshDelegate RefreshDelegate, String pincode, OrderVisitDetailsModel orderVisitDetailsModel) {
        super(activity);
        this.activity = activity;
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
        fetchOrderpass(Pincode);

        setListners();
    }

    private void setListners() {
        btn_no.setOnClickListener(this);
        btn_yes.setOnClickListener(this);

    }

    private void fetchOrderpass(String Pincode) {
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchLeaveDetailApiAsyncTask =
                asyncTaskForRequest.getorderallocation(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()), "" + Pincode/*orderVisitDetailsModel.getAllOrderdetails().get(0).get*/);
        fetchLeaveDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new FetchOrderPassDetailsApiAsyncTaskDelegateResult());

        fetchLeaveDetailApiAsyncTask.execute(fetchLeaveDetailApiAsyncTask);


    }

    private class FetchOrderPassDetailsApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                ResponseParser responseParser = new ResponseParser(activity);
                orderallocationsarr = new ArrayList<>();

                orderPassresponseModel = responseParser.getOrderPassresponseModel(json, statusCode);
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
            }

        }


        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, R.string.network_error, LENGTH_SHORT).show();
        }
    }


    private void initUI() {
        btn_yes = (Button) findViewById(R.id.btn_yes);
        btn_no = (Button) findViewById(R.id.btn_no);
        tv_title = (TextView) findViewById(R.id.tv_title);
        sp_btech = (Spinner) findViewById(R.id.sp_Btech);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_yes) {
            if (validate()) {
                PostOrderpass();
            }

            //   confirmOrderReleaseDialogButtonClickedDelegate.onOkButtonClicked(orderPassRequestModel);
            RefreshDelegate.onRefreshClicked();
            dismiss();

        }
        if (v.getId() == R.id.btn_no) {
            //confirmOrderReleaseDialogButtonClickedDelegate.onCancelButtonClicked();
            dismiss();
        }
    }

    private boolean validate() {
        if (sp_btech.getSelectedItem().equals("--SELECT--")) {
            TastyToast.makeText(activity, "Select BTECH Name", TastyToast.LENGTH_LONG, TastyToast.WARNING);
            // TastyToast
            return false;
        }
        if (orderallocationmodel == null) {
            return false;
        }
        return true;
    }

    private void PostOrderpass() {
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        OrderPassRequestModel orderPassRequestModel = new OrderPassRequestModel();
        orderPassRequestModel.setBtechId(orderallocationmodel.getBtechId());
        orderPassRequestModel.setVisitId(orderVisitDetailsModel.getVisitId());
        Logger.error("btech " + orderallocationmodel.getBtechName());

        ApiCallAsyncTask orderStatusChangeApiAsyncTask = asyncTaskForRequest.getOrderPassRequestModelAsyncTask(orderPassRequestModel);
        orderStatusChangeApiAsyncTask.setApiCallAsyncTaskDelegate(new OrderPassApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            orderStatusChangeApiAsyncTask.execute(orderStatusChangeApiAsyncTask);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }

    }

    private class OrderPassApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                //Toast.makeText(activity, ""+json, Toast.LENGTH_SHORT).show();


                dismiss();
            } else {
                showAlert("This Order can not be pass to other BTECH");
                // Toast.makeText(activity, ""+json, Toast.LENGTH_SHORT).show();
                dismiss();

            }
        }

        @Override
        public void onApiCancelled() {

        }

    }

    private void showAlert(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

        alertDialog.setMessage("" + message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        activity.pushFragments(VisitOrdersDisplayFragment.newInstance(), false, false, VisitOrdersDisplayFragment.TAG_FRAGMENT, R.id.fl_homeScreen, VisitOrdersDisplayFragment.TAG_FRAGMENT);
                        dialog.dismiss();

                    }
                });

        alertDialog.show();
    }


}
